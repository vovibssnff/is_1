<template>
  <div class="login-page">
    <div class="content-wrapper">
      <h1 class="page-description">Welcome to the Information Systems Lab - Login Page</h1>
      <auth-form />
    </div>
  </div>
</template>

<script>
import AuthForm from "@/components/forms/AuthForm.vue";

export default {
  components: {
    AuthForm,
  }
};
</script>

<style scoped>
/* Center the content and add a wrapper for separation */
.login-page {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100vh;
}

.content-wrapper {
  text-align: center;
  width: 100%;
  max-width: 800px; /* Increased max width for larger screens */
}

.page-description {
  margin-bottom: 20px;
  font-size: 1.5em;
  color: #333;
}

/* For screens 1259 pixels and above (desktop) */
@media only screen and (min-width: 1259px) {
  .auth-form {
    margin: 0 auto;
  }
}

/* For screens between 694 and 1259 pixels (tablet) */
@media only screen and (min-width: 694px) and (max-width: 1258px) {
  .auth-form {
    margin: 0 auto;
  }
}

/* For screens below 694 pixels (mobile) */
@media only screen and (max-width: 693px) {
  .auth-form {
    margin: 0 auto;
  }
}
</style>
