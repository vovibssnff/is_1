<template>
  <div class="app-container">
    <VerticalMenu 
      @navigate="navigateToMainPage"
      @show-dragonhead-table="showDragonHeadTable"
      @show-dragoncave-table="showDragonCaveTable"
      @show-person-table="showPersonTable"
    />
    <div class="top-right">
      <button @click="showPopupMenu" class="popup-button">Menu</button>
      <PopupMenu v-if="isPopupVisible" @close="closePopupMenu" />
    </div>

    <div class="create-button-container">
      <component
        :is="currentCreateButton"
        @create-dragon="openCreateDragonForm"
        @create-dragon-head="openCreateDragonHeadForm"
        @create-dragon-cave="openCreateDragonCaveForm"
        @create-person="openCreatePersonForm"
      />
    </div>
    <component :is="currentTable" :dragons="dragons" :persons="persons" :dragonCaves="dragonCaves" :dragonHeads="dragonHeads" @edit-dragon="openEditDragonForm"/>

    <DragonForm
      v-if="isCreateDragonFormVisible"
      :killers="persons"
      :dragon-data="editData"
      @submit-dragon="handleDragonSubmit"
      @close-form="closeCreateDragonForm"
    />
    <DragonHeadForm
      v-if="isCreateDragonHeadFormVisible"
      @submit-dragon-head="handleDragonHeadSubmit"
      @close-form="closeCreateDragonHeadForm"
    />
    <DragonCaveForm
      v-if="isCreateDragonCaveFormVisible"
      @submit-dragon-cave="handleDragonCaveSubmit"
      @close-form="closeCreateDragonCaveForm"
    />
    <PersonForm
      v-if="isCreatePersonFormVisible"
      @submit-person="handlePersonSubmit"
      @close-form="closeCreatePersonForm"
    />
  </div>
</template>

<script>
import VerticalMenu from '@/components/VerticalMenu.vue'
import PopupMenu from '@/components/PopupMenu.vue'
import DragonTable from '@/components/tables/DragonTable.vue'
import DragonHeadTable from '@/components/tables/DragonHeadTable.vue'
import PersonTable from '@/components/tables/PersonTable.vue'
import CreateDragonButton from '@/components/buttons/CreateDragonButton.vue'
import CreateDragonHeadButton from '@/components/buttons/CreateDragonHeadButton.vue'
import CreatePersonButton from '@/components/buttons/CreatePersonButton.vue'
import DragonForm from '@/components/forms/DragonForm.vue'
import DragonHeadForm from '@/components/forms/DragonHeadForm.vue'
import PersonForm from '@/components/forms/PersonForm.vue'
import axios, { formToJSON } from 'axios'
import DragonCaveTable from '@/components/tables/DragonCaveTable.vue'
import CreateDragonCaveButton from '@/components/buttons/CreateDragonCaveButton.vue'
import DragonCaveForm from '@/components/forms/DragonCaveForm.vue'

export default {
  
  components: {
    VerticalMenu,
    PopupMenu,
    DragonTable,
    DragonHeadTable,
    PersonTable,
    DragonCaveTable,
    CreateDragonButton,
    CreateDragonHeadButton,
    CreatePersonButton,
    CreateDragonCaveButton,
    DragonForm,
    DragonHeadForm,
    PersonForm,
    DragonCaveForm,
  },
  data() {
    return {
      isPopupVisible: false,
      currentTable: 'DragonTable',
      currentCreateButton: 'CreateDragonButton',
      dragons: [],
      dragonHeads: [],
      persons: [],
      polling: {
        dragons: null,
        dragonHeads: null,
        dragonCaves: null,
        persons: null,
      },
      pollingInterval: 5000,
      isCreateDragonFormVisible: false,
      isCreateDragonHeadFormVisible: false,
      isCreatePersonFormVisible: false,
      isCreateDragonCaveFormVisible: false,
      editData: null,
    }
  },
  methods: {
    navigateToMainPage() {
      this.$router.push({ name: 'main' });
      this.stopPollingDragonHeads();
      this.stopPollingPersons();
      this.stopPollingDragonCaves();
      this.startPollingDragons();

      this.currentTable = 'DragonTable';
      this.currentCreateButton = 'CreateDragonButton';
    },
    showDragonHeadTable() {
      this.stopPollingDragons();
      this.stopPollingPersons();
      this.stopPollingDragonCaves();
      this.startPollingDragonHeads();
      this.currentTable = 'DragonHeadTable';
      this.currentCreateButton = 'CreateDragonHeadButton';
    },
    showDragonCaveTable() {
      this.stopPollingDragons();
      this.stopPollingPersons();
      this.stopPollingDragonHeads();
      this.startPollingDragonCaves();
      this.currentTable = 'DragonCaveTable';
      this.currentCreateButton = 'CreateDragonCaveButton';
    },
    showPersonTable() {
      this.stopPollingDragons();
      this.stopPollingDragonHeads();
      this.stopPollingDragonCaves();
      this.startPollingPersons();
      this.currentTable = 'PersonTable';
      this.currentCreateButton = 'CreatePersonButton';
    },
    showPopupMenu() {
      this.isPopupVisible = !this.isPopupVisible;
    },
    closePopupMenu() {
      this.isPopupVisible = false;
    },
    openCreateDragonForm() {
      this.isCreateDragonFormVisible = true;
    },
    closeCreateDragonForm() {
      this.isCreateDragonFormVisible = false;
    },
    openCreateDragonHeadForm() {
      this.isCreateDragonHeadFormVisible = true;
    },
    closeCreateDragonHeadForm() {
      this.isCreateDragonHeadFormVisible = false;
    },
    openCreateDragonCaveForm() {
      this.isCreateDragonCaveFormVisible = true;
    },
    closeCreateDragonCaveForm() {
      this.isCreateDragonCaveFormVisible = false;
    },
    openCreatePersonForm() {
      this.isCreatePersonFormVisible = true;
    },
    closeCreatePersonForm() {
      this.isCreatePersonFormVisible = false;
    },
    openEditDragonForm(dragon) {
      this.editData = dragon;
      this.isCreateDragonFormVisible = true;
    },
    handleDragonSubmit(dragonData) {
      axios.post('/backend-1.0-SNAPSHOT/api/dragons', dragonData)
        .then(() => {
          this.closeCreateDragonForm();
          this.editData = null;
        })
        .catch(error => {
          console.error('Error submitting dragon:', error);
        });
    },
    handleDragonHeadSubmit(dragonHeadData) {
      axios.post('/backend-1.0-SNAPSHOT/api/dragon-heads', dragonHeadData)
        .then(() => {
          this.closeCreateDragonHeadForm();
          this.editData = null;
        })
        .catch(error => {
          console.error('Error submitting dragonHead:', error);
        });
    },
    handleDragonCaveSubmit(dragonCaveData) {
      axios.post('/backend-1.0-SNAPSHOT/api/dragon-caves', dragonCaveData)
        .then(() => {
          this.closeCreateDragonHeadForm();
          this.editData = null;
        })
        .catch(error => {
          console.error('Error submitting dragonHead:', error);
        });
    },
    handlePersonSubmit(personData) {
      axios.post('/backend-1.0-SNAPSHOT/api/persons', personData)
        .then(() => {
          this.closeCreatePersonForm();
          this.editData = null;
        })
        .catch(error => {
          console.error('Error submitting person:', error);
        });
    },
    startPollingDragons() {
      const pollDragons = async () => {
        try {
          const response = await axios.get('/backend-1.0-SNAPSHOT/api/dragons');
          this.dragons = response.data;
        } catch (error) {
          console.error('Error fetching dragons:', error);
        } finally {
          // Schedule next poll
          this.polling.dragons = setTimeout(pollDragons, this.pollingInterval);
        }
      };
      pollDragons();
    },
    startPollingDragonHeads() {
      const pollDragonHeads = async () => {
        try {
          const response = await axios.get('/backend-1.0-SNAPSHOT/api/dragon-heads');
          this.dragonHeads = response.data;
        } catch (error) {
          console.error('Error fetching dragon heads:', error);
        } finally {
          // Schedule next poll
          this.polling.dragonHeads = setTimeout(pollDragonHeads, this.pollingInterval);
        }
      };
      pollDragonHeads();
    },
    startPollingDragonCaves() {
      const pollDragonsCaves = async () => {
        try {
          const response = await axios.get('/backend-1.0-SNAPSHOT/api/dragon-caves');
          this.dragonCaves = response.data;
        } catch (error) {
          console.error('Error fetching dragonCaves:', error);
        } finally {
          // Schedule next poll
          this.polling.dragonCaves = setTimeout(pollDragonsCaves, this.pollingInterval);
        }
      };
      pollDragonsCaves();
    },
    startPollingPersons() {
      const pollPersons = async () => {
        try {
          const response = await axios.get('/backend-1.0-SNAPSHOT/api/persons');
          this.persons = response.data;
        } catch (error) {
          console.error('Error fetching persons:', error);
        } finally {
          // Schedule next poll
          this.polling.persons = setTimeout(pollPersons, this.pollingInterval);
        }
      };
      pollPersons();
    },
    stopPollingDragons() {
      clearTimeout(this.polling.dragons);
      this.polling.dragons = null;
    },
    stopPollingDragonHeads() {
      clearTimeout(this.polling.dragonHeads);
      this.polling.dragonHeads = null;
    },
    stopPollingDragonCaves() {
      clearTimeout(this.polling.dragonCaves);
      this.polling.dragonCaves = null;
    },
    stopPollingPersons() {
      clearTimeout(this.polling.persons);
      this.polling.persons = null;
    },

  },
  mounted() {
    this.navigateToMainPage();
  },
  beforeDestroy() {
    this.stopPollingDragons();
    this.stopPollingDragonHeads();
    this.stopPollingDragonCaves();
    this.stopPollingPersons();
  }
}
</script>

<style scoped>
.app-container {
  display: flex;
  position: relative;
}

.top-right {
  position: absolute;
  top: 10px;
  right: 10px;
}

.popup-button {
  padding: 10px;
}

.create-button-container {
  margin: 20px 0;
}
</style>
