<template>
  <div>
    <h2>Dragon Cave List</h2>

    <Toolbar class="mb-6">
      <template #start>
        <Button label="New" icon="pi pi-plus" class="mr-2" @click="openNew" />
      </template>

      <template #end>
        <FileUpload mode="basic" accept="image/*" :maxFileSize="1000000" label="Import" customUpload chooseLabel="Import" class="mr-2" auto :chooseButtonProps="{ severity: 'secondary' }" />
        <Button label="Export" icon="pi pi-upload" severity="secondary" @click="exportCSV" />
      </template>
    </Toolbar>

    <DataTable
      ref="dt"
      v-model:selection="selectedDragonCaves"
      :value="dragonCaves"
      dataKey="id"
      :paginator="true"
      :rows="10"
      :filters="filters"
      paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown"
      :rowsPerPageOptions="[5, 10, 20]"
      currentPageReportTemplate="Showing {first} to {last} of {totalRecords} Dragon Caves"
    >
      <template #header>
        <div class="flex justify-end">
          <InputText v-model="filters['global'].value" placeholder="Search..." class="w-full" />
        </div>
      </template>

      <Column field="id" header="ID" sortable></Column>
      <Column field="creatorId" header="Creator ID" sortable></Column>
      <Column field="updatedTime" header="Updated Time" sortable></Column>
      <Column field="depth" header="Depth" sortable></Column>
      <Column field="numberOfTreasures" header="Number of Treasures" sortable></Column>
      <Column header="Actions">
        <template #body="slotProps">
          <Button icon="pi pi-pencil" outlined rounded class="mr-2" @click="editDragonCave(slotProps.data)" />
          <Button icon="pi pi-trash" outlined rounded severity="danger" @click="confirmDeleteDragonCave(slotProps.data.id)" />
        </template>
      </Column>
    </DataTable>

    <!-- Error Message -->
    <div v-if="errorMessage" class="error-message">
      {{ errorMessage }}
    </div>

    <!-- Add/Edit Dialog -->
    <Dialog v-model:visible="dragonCaveDialog" :style="{ width: '450px' }" header="Dragon Cave Details" :modal="true">
      <div class="flex flex-col gap-6">
        <div>
          <label for="depth" class="block font-bold mb-3">Depth</label>
          <InputNumber 
            id="depth" 
            v-model="dragonCave.depth" 
            :minFractionDigits="1" 
            :maxFractionDigits="2" 
            fluid 
          />
        </div>
        <div>
          <label for="numberOfTreasures" class="block font-bold mb-3">Number of Treasures</label>
          <InputNumber 
            id="numberOfTreasures" 
            v-model="dragonCave.numberOfTreasures" 
            :minFractionDigits="0" 
            :maxFractionDigits="0" 
            fluid 
          />
        </div>
      </div>

      <template #footer>
        <Button label="Cancel" icon="pi pi-times" text @click="hideDialog" />
        <Button label="Save" icon="pi pi-check" @click="saveDragonCave" />
      </template>
    </Dialog>
  </div>
</template>

<script>
import { ref, onMounted, onUnmounted } from "vue";
import axios from "axios";
import { useToast } from "primevue/usetoast";
import DataTable from "primevue/datatable";
import Column from "primevue/column";
import InputText from "primevue/inputtext";
import InputNumber from "primevue/inputnumber";
import Button from "primevue/button";
import Dialog from "primevue/dialog";
import Toolbar from "primevue/toolbar";
import FileUpload from "primevue/fileupload";

const API_URL = '/api/dragon-caves';

export default {
  components: {
    DataTable,
    Column,
    InputText,
    InputNumber,
    Button,
    Dialog,
    Toolbar,
    FileUpload
  },
  setup() {
    const toast = useToast();
    const dragonCaves = ref([]);
    const dragonCaveDialog = ref(false);
    const dragonCave = ref({ depth: null, numberOfTreasures: null });
    const selectedDragonCaves = ref([]);
    const filters = ref({
      global: { value: null, matchMode: "contains" },
    });
    const errorMessage = ref(null);

    const fetchDragonCaves = async () => {
      try {
        const response = await axios.get(API_URL);
        dragonCaves.value = response.data;
      } catch (error) {
        toast.add({
          severity: 'error',
          summary: 'Error',
          detail: 'Failed to fetch dragon caves',
          life: 3000
        });
      }
    };

    const openNew = () => {
      errorMessage.value = null;
      dragonCave.value = { depth: null, numberOfTreasures: null };
      dragonCaveDialog.value = true;
    };

    const hideDialog = () => {
      dragonCaveDialog.value = false;
    };

    const saveDragonCave = async () => {
      try {
        if (dragonCave.value.id) {
          const response = await axios.put(`${API_URL}/${dragonCave.value.id}`, {
            id: dragonCave.value.id,
            depth: dragonCave.value.depth,
            numberOfTreasures: dragonCave.value.numberOfTreasures
          });

          const index = dragonCaves.value.findIndex((cave) => cave.id === dragonCave.value.id);
          if (index !== -1) {
            dragonCaves.value[index] = response.data;
          }

          toast.add({
            severity: 'success',
            summary: 'Successful',
            detail: 'Dragon Cave Updated',
            life: 3000
          });
        } else {
          const response = await axios.post(API_URL, {
            depth: dragonCave.value.depth,
            numberOfTreasures: dragonCave.value.numberOfTreasures
          });

          dragonCaves.value.push(response.data);
          toast.add({
            severity: 'success',
            summary: 'Successful',
            detail: 'Dragon Cave Created',
            life: 3000
          });
        }

        dragonCaveDialog.value = false;
      } catch (error) {
        if (error.response?.status === 403) {
          hideDialog();
          errorMessage.value = "You do not have permission to update this dragon cave.";
        } else {
          toast.add({
            severity: 'error',
            summary: 'Error',
            detail: error.response?.data?.message || 'Failed to save dragon cave',
            life: 3000
          });
        }
      }
    };

    const editDragonCave = (dragonCaveItem) => {
      dragonCave.value = { ...dragonCaveItem };
      dragonCaveDialog.value = true;
    };

    const confirmDeleteDragonCave = async (id) => {
      try {
        await axios.delete(`${API_URL}/${id}`);
        dragonCaves.value = dragonCaves.value.filter((cave) => cave.id !== id);

        toast.add({
          severity: 'success',
          summary: 'Successful',
          detail: 'Dragon Cave Deleted',
          life: 3000
        });
      } catch (error) {
        if (error.response?.status === 403) {
          errorMessage.value = "You do not have permission to delete this dragon cave.";
        } else {
          toast.add({
            severity: 'error',
            summary: 'Error',
            detail: error.response?.data?.message || 'Failed to delete dragon cave',
            life: 3000
          });
        }
      }
    };

    let longPollingInterval;

    const startLongPolling = () => {
      longPollingInterval = setInterval(() => {
        fetchDragonCaves();
      }, 5000);
    };

    onMounted(() => {
      fetchDragonCaves();
      startLongPolling();
    });

    onUnmounted(() => {
      clearInterval(longPollingInterval);
    });

    return {
      dragonCaves,
      dragonCaveDialog,
      dragonCave,
      selectedDragonCaves,
      filters,
      openNew,
      hideDialog,
      saveDragonCave,
      fetchDragonCaves,
      editDragonCave,
      confirmDeleteDragonCave,
      errorMessage
    };
  }
};
</script>