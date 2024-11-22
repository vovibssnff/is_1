<template>
    <button class="create-dragon-cave-button" @click="$emit('create-dragon-cave')">Create DragonCave</button>
  </template>
  
  <script>
  export default {
    name: 'CreateDragonCaveButton'
  }
  </script>
  
  <style scoped>
  .create-dragon-cave-button {
    padding: 10px 20px;
    background-color: #4CAF50; /* Green */
    color: white;
    border: none;
    border-radius: 5px;
    cursor: pointer;
  }
  
  .create-dragon-cave-button:hover {
    background-color: #45a049; /* Darker green */
  }
  </style>
  