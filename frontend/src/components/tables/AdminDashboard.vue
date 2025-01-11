<template>
    <div class="card">
      <h2>Admin Request List</h2>
  
      <Toolbar class="mb-6">
        <template #start>
          <Button 
            label="New" 
            icon="pi pi-plus" 
            class="mr-2" 
            @click="openNew"
            v-if="canCreateNewRequest"
          />
        </template>
  
        <template #end>
          <Button 
            label="Export" 
            icon="pi pi-upload" 
            severity="secondary" 
            @click="exportCSV($event)"
            :disabled="!adminRequests.length"
          />
        </template>
      </Toolbar>
  
      <DataTable
        :value="adminRequests"
        :loading="loading"
        v-model:selection="selectedRequests"
        :paginator="true"
        :rows="10"
        :filters="filters"
        dataKey="id"
        paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown"
        :rowsPerPageOptions="[5, 10, 20]"
        currentPageReportTemplate="Showing {first} to {last} of {totalRecords} Requests"
        :rowHover="true"
        stripedRows
        responsiveLayout="scroll"
      >
        <template #header>
          <div class="flex justify-between items-center">
            <h3 class="m-0">Admin Requests</h3>
            <span class="p-input-icon-left">
              <i class="pi pi-search" />
              <InputText 
                v-model="filters.global.value" 
                placeholder="Search..." 
                class="p-2"
                @input="onSearch"
              />
            </span>
          </div>
        </template>
  
        <template #empty>No admin requests found</template>
        <template #loading>Loading admin requests...</template>
  
        <Column field="id" header="Request ID" sortable></Column>
        <Column field="requesterId" header="User ID" sortable></Column>
        <Column header="Actions" style="min-width: 8rem">
          <template #body="{ data }">
            <div class="flex gap-2">
              <Button
                icon="pi pi-check"
                severity="success"
                rounded
                :loading="processingRequests[data.id]"
                @click="acceptRequest(data.requesterId)"
                tooltip="Accept Request"
              />
              <Button
                icon="pi pi-times"
                severity="danger"
                rounded
                :loading="processingRequests[data.id]"
                @click="declineRequest(data.requesterId)"
                tooltip="Decline Request"
              />
            </div>
          </template>
        </Column>
      </DataTable>
  
      <!-- Toast for notifications -->
      <Toast position="top-right" />
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted, onUnmounted } from 'vue';
  import { useToast } from 'primevue/usetoast';
  import { computed } from 'vue';
  import axios from 'axios';
  import DataTable from 'primevue/datatable';
  import Column from 'primevue/column';
  import InputText from 'primevue/inputtext';
  import Button from 'primevue/button';
  import Toolbar from 'primevue/toolbar';
  import Toast from 'primevue/toast';
  import { debounce } from 'lodash';
  
  // Constants
  const POLL_INTERVAL = 5000;
  const API_BASE_URL = '/api/auth';
  
  // Composables
  const toast = useToast();
  
  // State
  const adminRequests = ref([]);
  const loading = ref(false);
  const processingRequests = ref({});
  const selectedRequests = ref([]);
  const filters = ref({
    global: { value: null, matchMode: "contains" },
  });
  let pollInterval = null;
  
  // API client
  const api = axios.create({
    baseURL: API_BASE_URL,
    headers: {
      'Content-Type': 'application/json',
      'Accept': 'application/json'
    }
  });
  
  // Error handler
  const handleError = (error, customMessage) => {
    console.error(error);
    toast.add({
      severity: 'error',
      summary: 'Error',
      detail: error.response?.data?.message || customMessage,
      life: 3000
    });
  };
  
  // Fetch admin requests
  const fetchAdminRequests = async () => {
    try {
      loading.value = true;
      const { data } = await api.get('/admin-request');
      adminRequests.value = data.map(request => ({
        id: request.id,
        requesterId: request.requesterId
      }));
    } catch (error) {
      handleError(error, 'Failed to fetch admin requests');
    } finally {
      loading.value = false;
    }
  };
  
  // Accept request
  const acceptRequest = async (id) => {
    processingRequests.value[id] = true;
    try {
      await api.post(`/admin-request/${id}/accept`);
      toast.add({
        severity: 'success',
        summary: 'Success',
        detail: 'Admin request accepted',
        life: 3000
      });
      await fetchAdminRequests();
    } catch (error) {
      handleError(error, 'Failed to accept admin request');
    } finally {
      processingRequests.value[id] = false;
    }
  };
  
  // Decline request
  const declineRequest = async (id) => {
    processingRequests.value[id] = true;
    try {
      await api.post(`/admin-request/${id}/decline`);
      toast.add({
        severity: 'success',
        summary: 'Success',
        detail: 'Admin request declined',
        life: 3000
      });
      await fetchAdminRequests();
    } catch (error) {
      handleError(error, 'Failed to decline admin request');
    } finally {
      processingRequests.value[id] = false;
    }
  };
  
  const onSearch = debounce(() => {
    // Add any additional search logic here
  }, 300);
  
  // Export functionality
  const exportCSV = (event) => {
    this.$refs.dt.exportCSV();
  };
  
  // Polling
  const startPolling = () => {
    pollInterval = setInterval(() => {
      if (!loading.value) {
        fetchAdminRequests();
      }
    }, POLL_INTERVAL);
  };
  
  // Lifecycle hooks
  onMounted(() => {
    fetchAdminRequests();
    startPolling();
  });
  
  onUnmounted(() => {
    if (pollInterval) {
      clearInterval(pollInterval);
    }
  });
  
  // Computed properties
  const canCreateNewRequest = computed(() => {
    // Add logic to determine if user can create new requests
    return true;
  });
  </script>
  
  <style scoped>
  .card {
    @apply p-4 bg-white rounded-lg shadow-sm;
  }
  
  .error-message {
    @apply text-red-500 text-base mt-4;
  }
  </style>