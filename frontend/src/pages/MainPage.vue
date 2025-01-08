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

    <component 
      :is="currentTable" 
      :dragons="dragons" 
      :persons="persons" 
      :dragonCaves="dragonCaves" 
      :dragonHeads="dragonHeads" 
      @edit-dragon="openEditDragonForm"
    />
  </div>
</template>

<script>
import VerticalMenu from '@/components/VerticalMenu.vue'
import PopupMenu from '@/components/PopupMenu.vue'
import DragonTable from '@/components/tables/DragonTable.vue'
import DragonHeadTable from '@/components/tables/DragonHeadTable.vue'
import PersonTable from '@/components/tables/PersonTable.vue'
import DragonCaveTable from '@/components/tables/DragonCaveTable.vue'

export default {
  components: {
    VerticalMenu,
    PopupMenu,
    DragonTable,
    DragonHeadTable,
    PersonTable,
    DragonCaveTable,
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
      this.currentTable = 'DragonTable';
      this.currentCreateButton = 'CreateDragonButton';
    },
    showDragonHeadTable() {
      this.currentTable = 'DragonHeadTable';
      this.currentCreateButton = 'CreateDragonHeadButton';
    },
    showDragonCaveTable() {
      this.currentTable = 'DragonCaveTable';
      this.currentCreateButton = 'CreateDragonCaveButton';
    },
    showPersonTable() {
      this.currentTable = 'PersonTable';
      this.currentCreateButton = 'CreatePersonButton';
    },
    showPopupMenu() {
      this.isPopupVisible = !this.isPopupVisible;
    },
    closePopupMenu() {
      this.isPopupVisible = false;
    },
  },
  mounted() {
    this.navigateToMainPage();
  },
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
