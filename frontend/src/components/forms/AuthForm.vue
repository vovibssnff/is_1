<template>
  <form @submit.prevent>
    <input
      v-model.trim="usr.login"
      class="input"
      type="text"
      placeholder="login"
      :disabled="loading"
    />
    <p v-if="loginErrors.login" class="error-message">{{ loginErrors.login }}</p>
    <input
      v-model.trim="usr.password"
      class="input"
      type="password"
      placeholder="password"
      :disabled="loading"
    />
    <p v-if="loginErrors.password" class="error-message">{{ loginErrors.password }}</p>
    <div class="btn_container">
      <button
        class="btn"
        @click="try_register"
        :disabled="loading"
      >register</button>
      <button
        class="btn"
        @click="try_auth"
        :disabled="loading"
      >submit</button>
    </div>
    <p v-if="notification" class="notification">{{ notification }}</p>
  </form>
</template>

<script>
import { mapState, mapMutations, mapActions } from 'vuex';
import router from '@/router/index';

export default {
  data() {
    return {
      usr: {
        login: '',
        password: ''
      },
      loading: false
    };
  },
  computed: {
    ...mapState('auth', {
      loginErrors: state => state.loginErrors,
      notification: state => state.notification
    })
  },
  methods: {
    ...mapMutations('auth', ['setLoginErrors', 'clearErrors']),
    ...mapActions('auth', ['loginUser', 'registerUser']),
    
    validateFields() {
      let isValid = true;
      const errors = {
        login: '',
        password: ''
      };
      
      if (!this.usr.login) {
        errors.login = "Login can't be empty";
        isValid = false;
      }
      if (!this.usr.password) {
        errors.password = "Password can't be empty";
        isValid = false;
      }
      
      this.setLoginErrors(errors);
      return isValid;
    },
    
    async try_auth() {
      if (this.loading) return;
      
      this.clearErrors();
      if (this.validateFields()) {
        try {
          this.loading = true;
          const result = await this.loginUser({
            username: this.usr.login,
            password: this.usr.password
          });
          
          if (result.success) {
            await router.push('/main');
          }
        } finally {
          this.loading = false;
        }
      }
    },
    
    async try_register() {
      if (this.loading) return;
      
      this.clearErrors();
      if (this.validateFields()) {
        try {
          this.loading = true;
          await this.registerUser({
            username: this.usr.login,
            password: this.usr.password
          });
        } finally {
          this.loading = false;
        }
      }
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