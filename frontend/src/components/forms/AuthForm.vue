<template>
  <form @submit.prevent>
    <input
      v-model.trim="usr.login"
      class="input"
      type="text"
      placeholder="login"
    />
    <p v-if="errors.login" class="error-message">{{ errors.login }}</p>
    
    <input
      v-model.trim="usr.password"
      class="input"
      type="password"
      placeholder="password"
    />
    <p v-if="errors.password" class="error-message">{{ errors.password }}</p>
    
    <div class="btn_container">
      <button
          class="btn"
          @click="try_register"
      >register</button>
      <button
          class="btn"
          @click="try_auth"
      >submit</button>
    </div>

    <p v-if="notification" class="notification">{{ notification }}</p>
  </form>
</template>

<script>
import axios from 'axios';
import { mapMutations } from 'vuex';
import router from '@/router/index';

export default {
  data() {
    return {
      usr: {
        login: '',
        password: ''
      },
      errors: {
        login: '',
        password: ''
      },
      notification: '' // Consolidated field for notifications and errors
    };
  },
  methods: {
    ...mapMutations('dotModule', ['setAuthorized']),
    auth(val) {
      this.setAuthorized(val);
    },
    try_auth() {
      if (this.validateFields()) {
        axios.post('/api/auth/login', {
          username: this.usr.login,
          password: this.usr.password
        })
        .then(res => {
          if (res.status === 200) {
            const user = res.data;
            this.auth(true);
            localStorage.setItem('isAuthorized', true);
            localStorage.setItem('userId', user.userId);
            localStorage.setItem('username', user.username);
            localStorage.setItem('userRole', user.role);
            router.push('/main');
          }
        })
        .catch(err => {
          console.error(err);
          this.notification = "Invalid username or password";
        });
      }
    },
    try_register() {
      if (this.validateFields()) {
        axios.post('/api/auth/register', {
          username: this.usr.login,
          password: this.usr.password
        })
        .then(res => {
          if (res.status === 201) {
            this.notification = 'Registration successful, now you can log in!';
          } else {
            this.notification = `Registration failed: ${res.data.message}`;
          }
        })
        .catch(err => {
          console.error(err);
          this.notification = err.response?.data || "Registration failed";
        });
      }
    },
    validateFields() {
      let isValid = true;
      this.errors.login = this.errors.password = ''; // Reset errors
      this.notification = ''; // Reset notification

      if (!this.usr.login) {
        this.errors.login = "Login can't be empty";
        isValid = false;
      }
      if (!this.usr.password) {
        this.errors.password = "Password can't be empty";
        isValid = false;
      }
      return isValid;
    }
  }
};
</script>

<style>
form {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: 10%;
}

.btn_container {
  display: flex;
  width: 20%;
  justify-content: center;
}

.btn {
  width: 40%;
  background-color: #ff2f2f;
  color: white;
  padding: 10px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-family: "DejaVu Sans Mono", sans-serif;
  margin: 0 10px;
}

.input {
  margin-bottom: 10px;
  padding: 8px;
  width: 20%;
  font-family: "DejaVu Sans Mono", sans-serif;
}

.error-message {
  color: red;
  font-size: 0.9em;
  margin-bottom: 10px;
  text-align: left;
  width: 20%;
}

.notification {
  color: #555;
  font-size: 0.9em;
  margin-top: 10px;
  text-align: center;
  width: 20%;
}

.btn:hover {
  background-color: #ff5258;
}
</style>
