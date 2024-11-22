<template>
    <div>
      <h2>Person List</h2>
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>CreatorID</th>
            <th>Name</th>
            <th>Eye Color</th>
            <th>Hair Color</th>
            <th>Coordinates (X, Y)</th>
            <th>Location Name</th>
            <th>Passport ID</th>
            <th>Nationality</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="person in persons" :key="person.id">
            <td>{{ person.id }}</td>
            <td>{{ person.creator_id }}</td>
            <td>{{ person.name }}</td>
            <td>{{ person.eyeColor }}</td>
            <td>{{ person.hairColor ? person.hairColor : 'N/A' }}</td>
            <td>{{ person.x }}, {{ person.y }}</td>
            <td>{{ person.locationName ? person.locationName : 'N/A' }}</td>
            <td>{{ person.passportID }}</td>
            <td>{{ person.nationality }}</td>
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
        persons: [] // Array to store person data
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
  