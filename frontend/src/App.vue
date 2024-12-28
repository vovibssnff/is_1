<template>
  <div class="app">
    <router-view></router-view>
  </div>
</template>

<script>
  export default {
    mounted() {
      // let primevue_1 = document.createElement('script');
      // let primevue_2 = document.createElement('script');
      // let primevue_3 = document.createElement('script');
      // primevue_1.setAttribute('src', 'https://unpkg.com/vue@3/dist/vue.global.js');
      // primevue_2.setAttribute('src', 'https://unpkg.com/primevue/umd/primevue.min.js');
      // primevue_3.setAttribute('src', 'https://unpkg.com/@primevue/themes/umd/aura.min.js');
      // document.head.appendChild(primevue_1);
      // document.head.appendChild(primevue_2);
      // document.head.appendChild(primevue_3);
    }
  }
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

nav {
  padding: 30px;
}

nav a {
  font-weight: bold;
  color: #2c3e50;
}

nav a.router-link-exact-active {
  color: #42b983;
}
</style>
