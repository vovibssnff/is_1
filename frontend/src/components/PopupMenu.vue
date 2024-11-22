<template>
  <div class="popup-menu">
    <p>This is a popup menu.</p>
    <button @click="logout">Logout</button>
    <button @click="$emit('close')">Close</button>
  </div>
</template>

<script>
export default {
  name: 'PopupMenu',
  methods: {
    logout() {
      axios.post('/backend-1.0-SNAPSHOT/auth/logout')
        .then(response => {
          console.log(response.data);
          localStorage.clear();
          this.$router.push({ path: '/login' });
        })
        .catch(error => {
          console.error('Error during logout:', error.response?.data || error.message);
          alert('Failed to log out. Please try again.');
        });
    }
  },
  mounted() {
    console.log(localStorage.getItem('isAuthorized'), localStorage.getItem('username'), localStorage.getItem('userId'));
    // axios.get('/api/session')
    //   .then(res => {
    //     //TODO correct display
    //     console.log(res.data);
    //   })
    //   .catch(() => {
    //     router.push('/login');
    //   });
  }
}
</script>

<style>
.popup-menu {
  position: absolute;
  top: 40px;
  right: 0;
  background-color: white;
  border: 1px solid #ccc;
  padding: 15px;
  box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.2);
}

button {
  display: block;
  margin-top: 10px;
  padding: 8px 12px;
}
</style>
