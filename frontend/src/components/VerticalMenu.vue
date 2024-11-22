<template>
  <div class="vertical-menu">
    <button @click="$emit('navigate')">Dragon</button>
    <button @click="$emit('show-dragonhead-table')">DragonHead</button>
    <button @click="$emit('shot-dragoncave-table')">DragonCave</button>
    <button @click="$emit('show-person-table')">Person</button>
    <button @click="$emit('show-visualization')">Visualization</button>
  </div>
</template>


<script>
export default {
  name: 'VerticalMenu'
}
</script>

<style>
.vertical-menu {
  display: flex;
  flex-direction: column;
  width: 200px;
  padding: 10px;
  background-color: #f5f5f5;
  height: 100vh;
}
button {
  margin: 5px 0;
  padding: 10px;
}
</style>
