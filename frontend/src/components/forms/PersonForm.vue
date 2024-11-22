<template>
    <div class="form-overlay">
      <div class="form-container">
        <h3>Create New Person</h3>
        <form @submit.prevent="submitPerson">
          <input v-model="personData.name" placeholder="Name" required />
          <input v-model="personData.age" placeholder="Age" type="number" required />
          <input v-model="personData.occupation" placeholder="Occupation" />
          <input v-model="personData.address.city" placeholder="City" />
          <input v-model="personData.address.street" placeholder="Street" />
          <input v-model="personData.address.zipCode" placeholder="Zip Code" />
  
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
        personData: {
          name: '',
          age: '',
          occupation: '',
          address: {
            city: '',
            street: '',
            zipCode: ''
          }
        }
      };
    },
    methods: {
      submitPerson() {
        this.$emit('submit-person', this.personData);
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
  