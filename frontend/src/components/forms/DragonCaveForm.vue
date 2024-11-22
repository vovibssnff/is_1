<template>
    <div class="form-overlay">
      <div class="form-container">
        <h3>Create New Dragon Cave</h3>
        <form @submit.prevent="submitDragonCave">
          <input v-model="dragonCaveData.depth" placeholder="Depth" type="number" required />
          <input v-model="dragonHeadData.numberOfTreasures" placeholder="Number of treasures" type="number" required />
  
          <button type="submit">Create</button>
          <button type="button" @click="closeForm">Cancel</button>
        </form>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    data() {
      return {
        dragonCaveData: {
          depth: '',
          numberOfTreasures: '',
        }
      };
    },
    methods: {
      submitDragonCave() {
        this.$emit('submit-dragon-cave', this.dragonCaveData);
      },
      closeForm() {
        this.$emit('close-form');
      }
    }
  };
  </script>
  
  <style scoped>
  .form-overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.7);
    display: flex;
    justify-content: center;
    align-items: center;
  }
  .form-container {
    background: white;
    padding: 20px;
    border-radius: 8px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
  }
  .form-container input {
    display: block;
    margin-bottom: 10px;
    padding: 8px;
    width: 100%;
  }
  </style>
  