<template>
  <div class="form-overlay">
    <div class="form-container">
      <h3>Create New Dragon</h3>
      <form @submit.prevent="submitDragon">
        <input v-model="formData.name" placeholder="Name" required />
        <input v-model="formData.coordinates.x" placeholder="Coordinates X" required />
        <input v-model="formData.coordinates.y" placeholder="Coordinates Y" required />
        <input v-model="formData.age" placeholder="Age" required />
        <select v-model="formData.killerId" required>
          <option v-for="killer in killers" :key="killer.id" :value="killer.id">
            {{ killer.name }}
          </option>
        </select>
        <input v-model="formData.color" placeholder="Color" />
        <input v-model="formData.type" placeholder="Type" />
        <input v-model="formData.character" placeholder="Character" />
        <input v-model="formData.head.eyesCount" placeholder="Head Eyes Count" required />
        <input v-model="formData.head.toothCount" placeholder="Head Tooth Count" required />
        <input v-model="formData.cave.depth" type="number" placeholder="Cave Depth" required />
        <input v-model="formData.cave.numberOfTreasures" type="number" placeholder="Number of Treasures" required />

        <button type="submit">Create</button>
        <button type="button" @click="closeForm">Cancel</button>
      </form>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    killers: Array,
    dragonData: {
      type: Object,
      default: null,
    },
  },
  computed: {
    formData() {
      // Provide default structure if dragonData is null
      return this.dragonData || {
        name: '',
        coordinates: { x: '', y: '' },
        age: '',
        killerId: null,
        color: '',
        type: '',
        character: '',
        head: { eyesCount: '', toothCount: '' },
        cave: { depth: '', numberOfTreasures: '' }
      };
    },
  },
  methods: {
    submitDragon() {
      this.$emit('submit-dragon', this.formData);
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
.form-container input,
.form-container select {
  display: block;
  margin-bottom: 10px;
  padding: 8px;
  width: 100%;
}
</style>
