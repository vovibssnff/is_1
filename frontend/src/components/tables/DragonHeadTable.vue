<template>
    <div>
      <h2>Dragon Head List</h2>
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>CreatorID</th>
            <th>Eyes Count</th>
            <th>Tooth Count</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="dragonHead in dragonHeads" :key="dragonHead.id">
            <td>{{ dragonHead.id }}</td>
            <td>{{ dragonHead.creatorID }}</td>
            <td>{{ dragonHead.eyesCount }}</td>
            <td>{{ dragonHead.toothCount }}</td>
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
        dragonHeads: [] // Array to store dragon head data
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
  