<template>
  <div>
    <h2>Dragon List</h2>

    <Toolbar class="mb-6">
      <template #start>
        <Button label="New" icon="pi pi-plus" class="mr-2" @click="openNew" />
      </template>

      <template #end>
        <Button label="Export" icon="pi pi-upload" severity="secondary" @click="exportCSV" />
      </template>
    </Toolbar>

    <DataTable
      ref="dt"
      :value="dragons"
      dataKey="id"
      :paginator="true"
      :rows="10"
      :filters="filters"
      paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown"
      :rowsPerPageOptions="[5, 10, 20]"
      currentPageReportTemplate="Showing {first} to {last} of {totalRecords} Dragons"
    >
      <template #header>
        <div class="flex justify-end">
          <InputText v-model="filters['global'].value" placeholder="Search..." class="w-full" />
        </div>
      </template>

      <Column field="id" header="ID" sortable></Column>
      <Column field="creatorId" header="Creator ID" sortable></Column>
      <Column field="updatedTime" header="Updated Time" :body="formatDateTime" sortable></Column>
      <Column field="name" header="Name" sortable></Column>
      <Column field="x" header="X Coordinate" :body="formatX" sortable></Column>
      <Column field="y" header="Y Coordinate" sortable></Column>
      <Column field="age" header="Age" sortable></Column>
      <Column field="color" header="Color" :body="formatEnum('color')" sortable></Column>
      <Column field="type" header="Type" :body="formatEnum('type')" sortable></Column>
      <Column field="character" header="Character" :body="formatEnum('character')" sortable></Column>
      <Column header="Actions">
        <template #body="slotProps">
          <Button icon="pi pi-pencil" outlined rounded class="mr-2" @click="editDragon(slotProps.data)" />
          <Button icon="pi pi-trash" outlined rounded severity="danger" @click="deleteDragon(slotProps.data.id)" />
        </template>
      </Column>
    </DataTable>

    <Dialog v-model:visible="dragonDialog" :style="{ width: '600px' }" header="Dragon Details" :modal="true">
      <div class="flex flex-col gap-6">
        <div>
          <label for="name" class="block font-bold mb-3">Name</label>
          <InputText id="name" v-model="dragon.name"/>
        </div>
        <div>
          <label for="x" class="block font-bold mb-3">X Coordinate</label>
          <InputNumber id="x" v-model="dragon.x" :minFractionDigits="2" />
        </div>
        <div>
          <label for="y" class="block font-bold mb-3">Y Coordinate</label>
          <InputNumber id="y" v-model="dragon.y" :minFractionDigits="0" />
        </div>
        <div>
          <label for="age" class="block font-bold mb-3">Age</label>
          <InputNumber id="age" v-model="dragon.age" :minFractionDigits="0" />
        </div>
        <div>
          <label for="color" class="block font-bold mb-3">Color</label>
          <Dropdown 
            id="color" 
            v-model="dragon.color" 
            :options="ENUMS.color" 
            placeholder="Select Color"
            class="w-full"
          />
        </div>
        <div>
          <label for="type" class="block font-bold mb-3">Type</label>
          <Dropdown 
            id="type" 
            v-model="dragon.type" 
            :options="ENUMS.type" 
            placeholder="Select Type"
            class="w-full"
          />
        </div>
        <div>
          <label for="character" class="block font-bold mb-3">Character</label>
          <Dropdown 
            id="character" 
            v-model="dragon.character" 
            :options="ENUMS.character" 
            placeholder="Select Character"
            class="w-full"
          />
        </div>
        <div>
          <label for="head" class="block font-bold mb-3">Dragon Head</label>
          <Dropdown 
            id="head" 
            v-model="dragon.headId" 
            :options="dragonHeads" 
            optionLabel="id" 
            optionValue="id" 
            placeholder="Select a Dragon Head"
            class="w-full"
          />
        </div>
        <div>
          <label for="cave" class="block font-bold mb-3">Dragon Cave</label>
          <Dropdown 
            id="cave" 
            v-model="dragon.caveId" 
            :options="dragonCaves" 
            optionLabel="id" 
            optionValue="id" 
            placeholder="Select a Dragon Cave"
            class="w-full"
          />
        </div>
        <div>
          <label for="person" class="block font-bold mb-3">Person</label>
          <Dropdown 
            id="person" 
            v-model="dragon.personId" 
            :options="persons" 
            optionLabel="name" 
            optionValue="id" 
            placeholder="Select a Person"
            class="w-full"
          />
        </div>
      </div>

      <template #footer>
        <Button label="Cancel" icon="pi pi-times" text @click="hideDialog" />
        <Button label="Save" icon="pi pi-check" @click="saveDragon" />
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
import Dropdown from "primevue/dropdown";

const API_URL = '/api/dragons';

const ENUMS = {
  color: ["GREEN", "RED", "BLUE", "YELLOW"],
  type: ["WATER", "UNDERGROUND", "AIR", "FIRE"],
  character: ["GOOD", "CHAOTIC", "CHAOTIC_EVIL"]
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
    Dropdown
  },
  setup() {
    const toast = useToast();
    const dragons = ref([]);
    const dragonHeads = ref([]);
    const dragonCaves = ref([]);
    const persons = ref([]);
    const dragonDialog = ref(false);
    const dragon = ref(createEmptyDragon());
    const filters = ref({
      global: { value: null, matchMode: "contains" },
    });

    function createEmptyDragon() {
      return {
        id: null,
        creatorId: null,
        name: null,
        x: null,
        y: null,
        age: null,
        color: null,
        type: null,
        character: null,
        headId: null,
        caveId: null,
        personId: null,
        updatedTime: null
      };
    }

    const fetchEntities = async () => {
      try {
        const [dragonResponse, headResponse, caveResponse, personResponse] = await Promise.all([
          axios.get(API_URL),
          axios.get('/api/dragon-heads'),
          axios.get('/api/dragon-caves'),
          axios.get('/api/persons')
        ]);

        dragons.value = dragonResponse.data;
        dragonHeads.value = headResponse.data;
        dragonCaves.value = caveResponse.data;
        persons.value = personResponse.data;
      } catch (error) {
        toast.add({
          severity: 'error',
          summary: 'Error',
          detail: 'Failed to fetch data',
          life: 3000
        });
      }
    };

    const formatEnum = (field) => (row) => row[field] || "Unknown";

    const formatX = (row) => {
      return row.x !== null ? row.x.toFixed(2) : 'N/A';
    };

    const formatDateTime = (row) => {
      if (row.updatedTime) {
        return new Date(row.updatedTime).toLocaleString();
      }
      return "N/A";
    };

    const openNew = () => {
      dragon.value = createEmptyDragon();
      dragonDialog.value = true;
    };

    const hideDialog = () => {
      dragonDialog.value = false;
    };

    const saveDragon = async () => {
      try {
        if (dragon.value.id) {
          await axios.put(`${API_URL}/${dragon.value.id}`, dragon.value);
          toast.add({ severity: 'success', summary: 'Success', detail: 'Dragon updated', life: 3000 });
        } else {
          await axios.post(API_URL, dragon.value);
          toast.add({ severity: 'success', summary: 'Success', detail: 'Dragon created', life: 3000 });
        }
        await fetchEntities();
        dragonDialog.value = false;
      } catch (error) {
        toast.add({ severity: 'error', summary: 'Error', detail: 'Failed to save dragon', life: 3000 });
      }
    };

    const editDragon = (data) => {
      dragon.value = { ...data };
      dragonDialog.value = true;
    };

    const deleteDragon = async (id) => {
      try {
        await axios.delete(`${API_URL}/${id}`);
        await fetchEntities();
        toast.add({ severity: 'success', summary: 'Success', detail: 'Dragon deleted', life: 3000 });
      } catch (error) {
        toast.add({ severity: 'error', summary: 'Error', detail: 'Failed to delete dragon', life: 3000 });
      }
    };

    let longPollingInterval;

    const startLongPolling = () => {
      longPollingInterval = setInterval(() => {
        fetchEntities();
      }, 5000);
    };

    onMounted(() => {
      fetchEntities();
      startLongPolling();
    });

    onUnmounted(() => {
      clearInterval(longPollingInterval);
    });

    return {
      dragons,
      dragonHeads,
      dragonCaves,
      persons,
      dragonDialog,
      dragon,
      filters,
      ENUMS,
      openNew,
      hideDialog,
      saveDragon,
      editDragon,
      deleteDragon,
      formatEnum,
      formatX,
      formatDateTime
    };
  }
};
</script>