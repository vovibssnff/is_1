<template>
  <div>
    <h2>Dragon Head List</h2>
    
    <Toolbar class="mb-6">
      <template #start>
        <Button label="New" icon="pi pi-plus" class="mr-2" @click="openNew" />
        <!-- <Button label="Delete" icon="pi pi-trash" severity="danger" outlined @click="confirmDeleteSelected" :disabled="!selectedDragonHeads || !selectedDragonHeads.length" /> -->
      </template>

      <template #end>
        <FileUpload mode="basic" accept="image/*" :maxFileSize="1000000" label="Import" customUpload chooseLabel="Import" class="mr-2" auto :chooseButtonProps="{ severity: 'secondary' }" />
        <Button label="Export" icon="pi pi-upload" severity="secondary" @click="exportCSV" />
      </template>
    </Toolbar>

    <DataTable
      ref="dt"
      v-model:selection="selectedDragonHeads"
      :value="dragonHeads"
      dataKey="id"
      :paginator="true"
      :rows="10"
      :filters="filters"
      paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown"
      :rowsPerPageOptions="[5, 10, 20]"
      currentPageReportTemplate="Showing {first} to {last} of {totalRecords} Dragon Heads"
    >
      <template #header>
        <div class="flex justify-end">
          <InputText v-model="filters['global'].value" placeholder="Search..." class="w-full" />
        </div>
      </template>

      <Column field="id" header="ID" sortable></Column>
      <Column field="creatorId" header="Creator ID" sortable></Column>
      <Column field="updatedTime" header="Updated Time" sortable></Column>
      <Column field="eyesCount" header="Eyes Count" sortable></Column>
      <Column field="toothCount" header="Tooth Count" sortable></Column>
      <Column header="Actions">
        <template #body="slotProps">
          <Button icon="pi pi-pencil" outlined rounded class="mr-2" @click="editDragonHead(slotProps.data)" />
          <Button icon="pi pi-trash" outlined rounded severity="danger" @click="confirmDeleteDragonHead(slotProps.data.id)" />
        </template>
      </Column>
    </DataTable>

    <!-- Error Message -->
    <div v-if="errorMessage" class="error-message">
      {{ errorMessage }}
    </div>

    <!-- Add/Edit Dialog -->
    <Dialog v-model:visible="dragonHeadDialog" :style="{ width: '450px' }" header="Dragon Head Details" :modal="true">
      <div class="flex flex-col gap-6">
        <div>
          <label for="eyesCount" class="block font-bold mb-3">Eyes Count</label>
          <InputNumber id="eyesCount" v-model="dragonHead.eyesCount" integeronly fluid />
        </div>
        <div>
          <label for="toothCount" class="block font-bold mb-3">Tooth Count</label>
          <InputNumber id="toothCount" v-model="dragonHead.toothCount" integeronly fluid />
        </div>
      </div>

      <template #footer>
        <Button label="Cancel" icon="pi pi-times" text @click="hideDialog" />
        <Button label="Save" icon="pi pi-check" @click="saveDragonHead" />
      </template>
    </Dialog>
    
  </div>

</template>

<script>
import { ref, onMounted } from "vue";
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

const API_URL = '/api/dragon-heads';

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
    const dragonHeads = ref([]);
    const dragonHeadDialog = ref(false);
    const dragonHead = ref({ eyesCount: null, toothCount: null });
    const selectedDragonHeads = ref([]);
    const filters = ref({
      global: { value: null, matchMode: "contains" },
    });
    const errorMessage = ref(null);

    const fetchDragonHeads = async () => {
      try {
        const response = await axios.get(API_URL);
        dragonHeads.value = response.data;
      } catch (error) {
        toast.add({
          severity: 'error',
          summary: 'Error',
          detail: 'Failed to fetch dragon heads',
          life: 3000
        });
      }
    };

    const openNew = () => {
      errorMessage.value = null;
      dragonHead.value = { eyesCount: null, toothCount: null };
      dragonHeadDialog.value = true;
    };

    const hideDialog = () => {
      dragonHeadDialog.value = false;
    };

    const saveDragonHead = async () => {
      try {
        if (dragonHead.value.id) {
          const response = await axios.put(`${API_URL}/${dragonHead.value.id}`, {
            id: dragonHead.value.id,
            eyesCount: dragonHead.value.eyesCount,
            toothCount: dragonHead.value.toothCount
          });

          const index = dragonHeads.value.findIndex((head) => head.id === dragonHead.value.id);
          if (index !== -1) {
            dragonHeads.value[index] = response.data;
          }

          toast.add({
            severity: 'success',
            summary: 'Successful',
            detail: 'Dragon Head Updated',
            life: 3000
          });
        } else {
          const response = await axios.post(API_URL, {
            eyesCount: dragonHead.value.eyesCount,
            toothCount: dragonHead.value.toothCount
          });

          dragonHeads.value.push(response.data);
          toast.add({
            severity: 'success',
            summary: 'Successful',
            detail: 'Dragon Head Created',
            life: 3000
          });
        }

        dragonHeadDialog.value = false;
      } catch (error) {
        if (error.response?.status === 403) {
          hideDialog();
          errorMessage.value = "You do not have permission to update this dragon head.";
        } else {
          toast.add({
            severity: 'error',
            summary: 'Error',
            detail: error.response?.data?.message || 'Failed to save dragon head',
            life: 3000
          });
        }
      }
    };

    const editDragonHead = (dragonHeadItem) => {
      dragonHead.value = { ...dragonHeadItem };
      dragonHeadDialog.value = true;
    };

    const confirmDeleteDragonHead = async (id) => {
      try {
        await axios.delete(`${API_URL}/${id}`);
        dragonHeads.value = dragonHeads.value.filter((head) => head.id !== id);

        toast.add({
          severity: 'success',
          summary: 'Successful',
          detail: 'Dragon Head Deleted',
          life: 3000
        });
      } catch (error) {
        if (error.response?.status === 403) {
          errorMessage.value = "You do not have permission to delete this dragon head.";
        } else {
          toast.add({
            severity: 'error',
            summary: 'Error',
            detail: error.response?.data?.message || 'Failed to delete dragon head',
            life: 3000
          });
        }
      }
    };

    const startLongPolling = () => {
      setInterval(() => {
        fetchDragonHeads();
      }, 5000);
    };

    onMounted(() => {
      fetchDragonHeads();
      startLongPolling();
    });

    return {
      dragonHeads,
      dragonHeadDialog,
      dragonHead,
      selectedDragonHeads,
      filters,
      openNew,
      hideDialog,
      saveDragonHead,
      fetchDragonHeads,
      editDragonHead,
      confirmDeleteDragonHead,
      errorMessage
    };
  }
};
</script>
