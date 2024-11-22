<template>
    <button class="create-person-button" @click="$emit('create-person')">Create Person</button>
  </template>
  
  <script>
  export default {
    name: 'CreatePersonButton'
  }
  </script>
  
  <style scoped>
  .create-person-button {
    padding: 10px 20px;
    background-color: #4CAF50; /* Green */
    color: white;
    border: none;
    border-radius: 5px;
    cursor: pointer;
  }
  
  .create-person-button:hover {
    background-color: #45a049; /* Darker green */
  }
  </style>
  