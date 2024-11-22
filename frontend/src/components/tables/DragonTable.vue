<template>
  <div>
    <h2>Dragon List</h2>
    <table>
      <thead>
        <tr>
          <th>ID</th>
          <th>Creator ID</th>
          <th>Name</th>
          <th>Coordinates (X, Y)</th>
          <th>Creation Date</th>
          <th>Cave Depth</th>
          <th>Number of Treasures</th>
          <th>Killer Name</th>
          <th>Age</th>
          <th>Color</th>
          <th>Type</th>
          <th>Character</th>
          <th>Head</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="dragon in dragons" :key="dragon.id">
          <td>{{ dragon.id }}</td>
          <td>{{ dragon.creatorID }}</td>
          <td>{{ dragon.name }}</td>
          <td>{{ dragon.coordinates.x }}, {{ dragon.coordinates.y }}</td>
          <td>{{ dragon.creationDate }}</td>
          <td>{{ dragon.cave.depth }}</td>
          <td>{{ dragon.cave.numberOfTreasures }}</td>
          <td>{{ dragon.killer ? dragon.killer.name : 'N/A' }}</td>
          <td>{{ dragon.age }}</td>
          <td>{{ dragon.color }}</td>
          <td>{{ dragon.type }}</td>
          <td>{{ dragon.character }}</td>
          <td>{{ dragon.head }}</td>
          <td>
            <button @click="$emit('edit-dragon', dragon)">Edit</button>
            <button @click="$emit('delete-dragon', dragon_id)">Delete</button>
          </td>
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
      dragons: []
    };
  },
  mounted() {
    // this.fetchDragons();
  },
  // methods: {
  //   async fetchDragons() {
  //     try {
  //       const response = await axios.get('/api/dragons'); // Adjust the API endpoint as needed
  //       this.dragons = response.data;
  //     } catch (error) {
  //       console.error('Error fetching dragons:', error);
  //     }
  //   }
  // }
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
