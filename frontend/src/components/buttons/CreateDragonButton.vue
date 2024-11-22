<template>
  <button class="create-dragon-button" @click="$emit('create-dragon')">Create Dragon</button>
</template>

<script>
export default {
  name: 'CreateDragonButton'
}
</script>

<style scoped>
.create-dragon-button {
  padding: 10px 20px;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}
.create-dragon-button:hover {
  background-color: #45a049;
}
</style>
