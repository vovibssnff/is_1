<template>
  <div>
    <h2>Person List</h2>

    <Toolbar class="mb-6">
      <template #start>
        <Button label="New" icon="pi pi-plus" class="mr-2" @click="openNew" />
      </template>
      <template #end>
        <FileUpload mode="basic" accept=".csv" :maxFileSize="1000000" label="Import" customUpload chooseLabel="Import" class="mr-2" auto :chooseButtonProps="{ severity: 'secondary' }" />
        <Button label="Export" icon="pi pi-upload" severity="secondary" @click="exportCSV" />
      </template>
    </Toolbar>

    <DataTable
      ref="dt"
      :value="persons"
      :paginator="true"
      :rows="10"
      :filters="filters"
      paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown"
      :rowsPerPageOptions="[5, 10, 20]"
      currentPageReportTemplate="Showing {first} to {last} of {totalRecords} Persons"
    >
      <template #header>
        <div class="flex justify-end">
          <InputText v-model="filters['global'].value" placeholder="Search..." class="w-full" />
        </div>
      </template>

      <Column field="id" header="ID" sortable></Column>
      <Column field="creatorId" header="Creator ID" sortable></Column>
      <Column field="name" header="Name" :body="formatName" sortable></Column>
      <Column field="eyeColor" header="Eye Color" :body="formatEnum('eyeColor')" sortable></Column>
      <Column field="hairColor" header="Hair Color" :body="formatEnum('hairColor')" sortable></Column>
      <Column field="x" header="X Coordinate" :body="formatX" sortable></Column>
      <Column field="y" header="Y Coordinate" :body="formatY" sortable></Column>
      <Column field="passportId" header="Passport ID" sortable></Column>
      <Column field="nationality" header="Nationality" :body="formatEnum('nationality')" sortable></Column>
      <Column field="updatedTime" header="Updated Time" :body="formatDateTime" sortable></Column>
      <Column header="Actions">
        <template #body="slotProps">
          <Button icon="pi pi-pencil" outlined rounded class="mr-2" @click="editPerson(slotProps.data)" />
          <Button icon="pi pi-trash" outlined rounded severity="danger" @click="deletePerson(slotProps.data.id)" />
        </template>
      </Column>
    </DataTable>

    <Dialog v-model:visible="personDialog" :style="{ width: '450px' }" header="Person Details" :modal="true">
      <div class="flex flex-col gap-4">
        <div>
          <label for="name" class="block font-bold mb-2">Name</label>
          <InputText id="name" v-model="person.name" class="w-full" placeholder="Enter name" />
        </div>
        <div>
          <label for="eyeColor" class="block font-bold mb-2">Eye Color</label>
          <Dropdown id="eyeColor" v-model="person.eyeColor" :options="ENUMS.eyeColor" class="w-full" />
        </div>
        <div>
          <label for="hairColor" class="block font-bold mb-2">Hair Color</label>
          <Dropdown id="hairColor" v-model="person.hairColor" :options="ENUMS.hairColor" class="w-full" />
        </div>
        <div>
          <label for="coordinates" class="block font-bold mb-2">Coordinates</label>
          <div class="flex gap-2">
            <InputNumber v-model="person.x" placeholder="X" class="w-1/2" :minFractionDigits="2" :maxFractionDigits="2" />
            <InputNumber v-model="person.y" placeholder="Y" class="w-1/2" :minFractionDigits="1" :maxFractionDigits="1" />
          </div>
        </div>
        <div>
          <label for="passportId" class="block font-bold mb-2">Passport ID</label>
          <InputText id="passportId" v-model="person.passportId" class="w-full" placeholder="Enter passport ID" />
        </div>
        <div>
          <label for="nationality" class="block font-bold mb-2">Nationality</label>
          <Dropdown id="nationality" v-model="person.nationality" :options="ENUMS.nationality" class="w-full" />
        </div>
      </div>

      <template #footer>
        <Button label="Cancel" icon="pi pi-times" text @click="hideDialog" />
        <Button label="Save" icon="pi pi-check" @click="savePerson" />
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
import Dropdown from "primevue/dropdown";

const API_URL = "/api/persons";

const ENUMS = {
  eyeColor: ["GREEN", "RED", "BLUE", "YELLOW"],
  hairColor: ["GREEN", "RED", "BLUE", "YELLOW"],
  nationality: ["CHINA", "VATICAN", "NORTH_KOREA"]
};

export default {
  components: {
    DataTable,
    Column,
    InputText,
    InputNumber,
    Button,
    Dialog,
    Toolbar,
    FileUpload,
    Dropdown
  },
  setup() {
    const toast = useToast();
    const persons = ref([]);
    const personDialog = ref(false);
    const person = ref(createEmptyPerson());
    const filters = ref({
      global: { value: null, matchMode: "contains" },
    });
    const errorMessage = ref(null);

    function createEmptyPerson() {
      return {
        id: null,
        creatorId: null,
        name: "",
        eyeColor: null,
        hairColor: null,
        x: null,
        y: null,
        passportId: "",
        nationality: null,
        updatedTime: null
      };
    }

    const fetchPersons = async () => {
      try {
        const response = await axios.get(API_URL);
        persons.value = response.data;
      } catch (error) {
        toast.add({
          severity: "error",
          summary: "Error",
          detail: "Failed to fetch persons",
          life: 3000,
        });
      }
    };

    const formatEnum = (field) => (row) => row[field] || "Unknown";
    
    const formatName = (row) => {
      return row.name || 'N/A';
    };

    const formatX = (row) => {
      return row.x !== null ? row.x.toFixed(2) : 'N/A';
    };

    const formatY = (row) => {
      return row.y !== null ? row.y.toFixed(1) : 'N/A';
    };

    const formatDateTime = (row) => {
      if (row.updatedTime) {
        return new Date(row.updatedTime).toLocaleString();
      }
      return "N/A";
    };

    const openNew = () => {
      person.value = createEmptyPerson();
      personDialog.value = true;
    };

    const hideDialog = () => {
      personDialog.value = false;
      errorMessage.value = null;
    };

    const editPerson = (data) => {
      person.value = { ...data };
      personDialog.value = true;
    };

    const deletePerson = async (id) => {
      try {
        await axios.delete(`${API_URL}/${id}`);
        await fetchPersons();
        toast.add({
          severity: 'success',
          summary: 'Success',
          detail: 'Person deleted successfully',
          life: 3000
        });
      } catch (error) {
        toast.add({
          severity: 'error',
          summary: 'Error',
          detail: 'Failed to delete person',
          life: 3000
        });
      }
    };

    const savePerson = async () => {
      try {
        const payload = {
          ...person.value,
          passportId: person.value.passportId?.trim(),
        };

        if (!payload.passportId) {
          throw new Error("Passport ID is required");
        }

        if (payload.id) {
          await axios.put(`${API_URL}/${payload.id}`, payload);
        } else {
          await axios.post(API_URL, payload);
        }

        await fetchPersons();
        hideDialog();
        toast.add({
          severity: "success",
          summary: "Success",
          detail: `Person ${payload.id ? 'updated' : 'created'} successfully`,
          life: 3000,
        });
      } catch (error) {
        errorMessage.value = error.message || "An error occurred while saving";
        toast.add({
          severity: "error",
          summary: "Error",
          detail: errorMessage.value,
          life: 3000,
        });
      }
    };

    const exportCSV = () => {
      // Implementation for CSV export
    };

    let longPollingInterval;

    const startLongPolling = () => {
      longPollingInterval = setInterval(() => {
        fetchPersons();
      }, 5000);
    };

    onMounted(() => {
      fetchPersons();
      startLongPolling();
    });

    onUnmounted(() => {
      clearInterval(longPollingInterval);
    });

    return {
      persons,
      personDialog,
      person,
      filters,
      ENUMS,
      fetchPersons,
      formatEnum,
      formatName,
      formatX,
      formatY,
      formatDateTime,
      openNew,
      hideDialog,
      editPerson,
      deletePerson,
      savePerson,
      exportCSV,
      errorMessage,
    };
  },
};
</script>