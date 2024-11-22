<template>
    <button class="create-dragon-head-button" @click="$emit('create-dragon-head')">Create DragonHead</button>
  </template>
  
  <script>
  export default {
    name: 'CreateDragonHeadButton'
  }
  </script>
  
  <style scoped>
  .create-dragon-head-button {
    padding: 10px 20px;
    background-color: #4CAF50; /* Green */
    color: white;
    border: none;
    border-radius: 5px;
    cursor: pointer;
  }
  
  .create-dragon-head-button:hover {
    background-color: #45a049; /* Darker green */
  }
  </style>
  