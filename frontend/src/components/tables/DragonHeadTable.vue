<template>
  <div class="card">
      <Toolbar class="mb-6">
          <template #start>
              <Button label="New Dragon Head" icon="pi pi-plus" class="mr-2" @click="openNew" />
              <Button 
                  label="Delete Selected" 
                  icon="pi pi-trash" 
                  severity="danger" 
                  outlined 
                  @click="confirmDeleteSelected" 
                  :disabled="!selectedDragonHeads || !selectedDragonHeads.length" 
              />
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
          :loading="loading"
          paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown"
          :rowsPerPageOptions="[5, 10, 25]"
          currentPageReportTemplate="Showing {first} to {last} of {totalRecords} dragon heads"
          filterDisplay="menu"
      >
          <template #header>
              <div class="flex flex-wrap gap-2 items-center justify-between">
                  <h4 class="m-0">Manage Dragon Heads</h4>
                  <span class="p-input-icon-left">
                      <i class="pi pi-search" />
                      <InputText v-model="filters['global'].value" placeholder="Search..." />
                  </span>
              </div>
          </template>

          <Column selectionMode="multiple" style="width: 3rem"></Column>
          <Column field="id" header="ID" sortable style="min-width: 8rem"></Column>
          <Column field="creatorId" header="Creator ID" sortable style="min-width: 10rem"></Column>
          <Column field="updatedTime" header="Updated Time" sortable style="min-width: 12rem">
              <template #body="slotProps">
                  {{ formatDate(slotProps.data.updatedTime) }}
              </template>
          </Column>
          <Column field="eyesCount" header="Eyes Count" sortable style="min-width: 8rem"></Column>
          <Column field="toothCount" header="Tooth Count" sortable style="min-width: 8rem"></Column>
          <Column :exportable="false" style="min-width: 12rem">
              <template #body="slotProps">
                  <Button 
                      icon="pi pi-pencil" 
                      outlined 
                      rounded 
                      class="mr-2" 
                      @click="editDragonHead(slotProps.data)" 
                  />
                  <Button 
                      icon="pi pi-trash" 
                      outlined 
                      rounded 
                      severity="danger" 
                      @click="confirmDeleteDragonHead(slotProps.data)" 
                  />
              </template>
          </Column>
      </DataTable>
  </div>

  <!-- Edit/Create Dialog -->
  <Dialog 
      v-model:visible="dragonHeadDialog" 
      :style="{ width: '450px' }" 
      header="Dragon Head Details" 
      :modal="true"
      class="p-fluid"
  >
      <div class="flex flex-col gap-4">
          <div class="field">
              <label for="eyesCount">Eyes Count</label>
              <InputNumber 
                  id="eyesCount" 
                  v-model="dragonHead.eyesCount" 
                  :required="true" 
                  autofocus 
                  :invalid="submitted && !dragonHead.eyesCount"
              />
              <small class="p-error" v-if="submitted && !dragonHead.eyesCount">
                  Eyes count is required.
              </small>
          </div>
          <div class="field">
              <label for="toothCount">Tooth Count</label>
              <InputNumber 
                  id="toothCount" 
                  v-model="dragonHead.toothCount" 
                  :required="true"
              />
              <small class="p-error" v-if="submitted && !dragonHead.toothCount">
                  Tooth count is required.
              </small>
          </div>
      </div>
      <template #footer>
          <Button label="Cancel" icon="pi pi-times" text @click="hideDialog" />
          <Button label="Save" icon="pi pi-check" @click="saveDragonHead" />
      </template>
  </Dialog>

  <!-- Delete Dialog -->
  <Dialog 
      v-model:visible="deleteDragonHeadDialog" 
      :style="{ width: '450px' }" 
      header="Confirm" 
      :modal="true"
  >
      <div class="flex items-center gap-4">
          <i class="pi pi-exclamation-triangle !text-3xl" />
          <span v-if="dragonHead">
              Are you sure you want to delete this dragon head?
          </span>
      </div>
      <template #footer>
          <Button label="No" icon="pi pi-times" text @click="deleteDragonHeadDialog = false" />
          <Button label="Yes" icon="pi pi-check" text @click="deleteDragonHead" />
      </template>
  </Dialog>

  <!-- Delete Selected Dialog -->
  <Dialog 
      v-model:visible="deleteDragonHeadsDialog" 
      :style="{ width: '450px' }" 
      header="Confirm" 
      :modal="true"
  >
      <div class="flex items-center gap-4">
          <i class="pi pi-exclamation-triangle !text-3xl" />
          <span>Are you sure you want to delete the selected dragon heads?</span>
      </div>
      <template #footer>
          <Button label="No" icon="pi pi-times" text @click="deleteDragonHeadsDialog = false" />
          <Button label="Yes" icon="pi pi-check" text @click="deleteSelectedDragonHeads" />
      </template>
  </Dialog>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useToast } from 'primevue/usetoast';
// import Toast from 'primevue/toast';
// import { FilterMatchMode } from 'primevue/api';
import axios from 'axios';

// Component state
const toast = useToast();
const dt = ref();
const loading = ref(true);
const dragonHeads = ref([]);
const dragonHeadDialog = ref(false);
const deleteDragonHeadDialog = ref(false);
const deleteDragonHeadsDialog = ref(false);
const dragonHead = ref({});
const selectedDragonHeads = ref();
const submitted = ref(false);
const filters = ref({
  global: { value: null, matchMode: "contains" },
});

// API endpoints
const API_BASE_URL = '/api/dragon-heads';

// Load initial data
onMounted(async () => {
  try {
      const response = await axios.get(API_BASE_URL);
      dragonHeads.value = response.data;
  } catch (error) {
      toast.add({
          severity: 'error',
          summary: 'Error',
          detail: 'Failed to load dragon heads',
          life: 3000
      });
      console.error('Error loading dragon heads:', error);
  } finally {
      loading.value = false;
  }
});

// Utility functions
const formatDate = (date) => {
  return new Date(date).toLocaleString();
};

// CRUD Operations
const openNew = () => {
  dragonHead.value = {
      eyesCount: null,
      toothCount: null
  };
  submitted.value = false;
  dragonHeadDialog.value = true;
};

const hideDialog = () => {
  dragonHeadDialog.value = false;
  submitted.value = false;
};

const saveDragonHead = async () => {
  submitted.value = true;

  if (dragonHead.value.eyesCount && dragonHead.value.toothCount) {
      try {
          if (dragonHead.value.id) {
              // Update
              await axios.put(`${API_BASE_URL}/${dragonHead.value.id}`, dragonHead.value);
              toast.add({
                  severity: 'success',
                  summary: 'Success',
                  detail: 'Dragon Head Updated',
                  life: 3000
              });
          } else {
              // Create
              const response = await axios.post(API_BASE_URL, dragonHead.value);
              dragonHeads.value.push(response.data);
              toast.add({
                  severity: 'success',
                  summary: 'Success',
                  detail: 'Dragon Head Created',
                  life: 3000
              });
          }

          dragonHeadDialog.value = false;
          dragonHead.value = {};
          
          // Refresh the data
          const response = await axios.get(API_BASE_URL);
          dragonHeads.value = response.data;
      } catch (error) {
          toast.add({
              severity: 'error',
              summary: 'Error',
              detail: 'Failed to save dragon head',
              life: 3000
          });
          console.error('Error saving dragon head:', error);
      }
  }
};

const editDragonHead = (data) => {
  dragonHead.value = { ...data };
  dragonHeadDialog.value = true;
};

const confirmDeleteDragonHead = (data) => {
  dragonHead.value = data;
  deleteDragonHeadDialog.value = true;
};

const deleteDragonHead = async () => {
  try {
      await axios.delete(`${API_BASE_URL}/${dragonHead.value.id}`);
      dragonHeads.value = dragonHeads.value.filter(val => val.id !== dragonHead.value.id);
      deleteDragonHeadDialog.value = false;
      dragonHead.value = {};
      toast.add({
          severity: 'success',
          summary: 'Success',
          detail: 'Dragon Head Deleted',
          life: 3000
      });
  } catch (error) {
      toast.add({
          severity: 'error',
          summary: 'Error',
          detail: 'Failed to delete dragon head',
          life: 3000
      });
      console.error('Error deleting dragon head:', error);
  }
};

const confirmDeleteSelected = () => {
  deleteDragonHeadsDialog.value = true;
};

const deleteSelectedDragonHeads = async () => {
  try {
      await Promise.all(
          selectedDragonHeads.value.map(head => 
              axios.delete(`${API_BASE_URL}/${head.id}`)
          )
      );
      
      dragonHeads.value = dragonHeads.value.filter(
          val => !selectedDragonHeads.value.includes(val)
      );
      deleteDragonHeadsDialog.value = false;
      selectedDragonHeads.value = null;
      toast.add({
          severity: 'success',
          summary: 'Success',
          detail: 'Dragon Heads Deleted',
          life: 3000
      });
  } catch (error) {
      toast.add({
          severity: 'error',
          summary: 'Error',
          detail: 'Failed to delete selected dragon heads',
          life: 3000
      });
      console.error('Error deleting selected dragon heads:', error);
  }
};
</script>