<template>
    <div>
      <h2>Dragon Cave List</h2>
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>CreatorID</th>
            <th>Depth</th>
            <th>Number of Treasures</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="dragonCave in dragonCaves" :key="dragonCave.id">
            <td>{{ dragonHead.id }}</td>
            <td>{{ dragonHead.creatorID }}</td>
            <td>{{ dragonHead.depth }}</td>
            <td>{{ dragonHead.numberOfTreasures }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </template>
  
  <script>
  // import axios from 'axios';
  
  export default {
    data() {
      return {
        dragonCaves: [] // Array to store dragon head data
      };
    },
  }
  </script>
  
  <style scoped>
  table {
    width: 100%;
    border-collapse: collapse;
  }
  
  th, td {
    border: 1px solid #ddd;
    padding: 8px;
    text-align: left;
  }
  
  th {
    background-color: #f2f2f2;
  }
  </style>
  