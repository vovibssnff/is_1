<template>
  <div>
    <h2>Dragon Head List</h2>
    <DataTable
      :value="dragonHeads"
      paginator
      :rows="10"
      :rowsPerPageOptions="[5, 10, 20]"
      :loading="loading"
      :filters="filters"
      :globalFilterFields="['id', 'creatorId', 'updatedTime', 'eyesCount', 'toothCount']"
      filterDisplay="row"
    >
      <template #header>
        <div class="flex justify-end">
          <InputText
            v-model="filters['global'].value"
            placeholder="Keyword Search"
            class="w-full"
          />
        </div>
      </template>
      <Column field="id" header="ID" sortable></Column>
      <Column field="creatorId" header="Creator ID" sortable></Column>
      <Column field="updatedTime" header="Updated Time" sortable></Column>
      <Column field="eyesCount" header="Eyes Count" sortable></Column>
      <Column field="toothCount" header="Tooth Count" sortable></Column>
      <Column header="Actions">
        <template #body="slotProps">
          <button @click="editDragonHead(slotProps.data)">Edit</button>
          <button @click="deleteDragonHead(slotProps.data.id)">Delete</button>
        </template>
      </Column>
    </DataTable>
  </div>
</template>

<script>
import { ref, onMounted } from "vue";
import axios from "axios";
import DataTable from "primevue/datatable";
import Column from "primevue/column";
import InputText from "primevue/inputtext";

export default {
  components: {
    DataTable,
    Column,
    InputText,
  },
  props: {
    dragonHeads: Array,
  },
  emits: ["edit-dragon-head", "delete-dragon-head"],
  setup(_, { emit }) {
    const loading = ref(true);
    const filters = ref({
      global: { value: null, matchMode: "contains" },
    });

    const editDragonHead = (dragonHead) => {
      emit("edit-dragon-head", dragonHead);
    };

    const deleteDragonHead = async (id) => {
      if (confirm("Are you sure you want to delete this Dragon Head?")) {
        try {
          await axios.delete(`/api/dragon-heads/${id}`);
          emit("delete-dragon-head", id);
        } catch (error) {
          console.error("Error deleting dragon head:", error);
        }
      }
    };

    return {
      loading,
      filters,
      editDragonHead,
      deleteDragonHead,
    };
  },
};
</script>
