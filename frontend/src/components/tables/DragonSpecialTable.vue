<template>
    <div class="card">
      <h2 class="text-2xl font-bold mb-4">Dragon Management System</h2>
      
      <!-- Minimal ID Dragon Section -->
      <div class="mb-6">
        <h3 class="text-xl mb-2">Minimal ID Dragon</h3>
        <Button label="Get Minimal ID Dragon" @click="getMinimalDragon" :loading="loading.minimal" />
        <div v-if="minimalDragon" class="mt-2">
          <Message severity="info">
            <div>ID: {{ minimalDragon.id }}</div>
            <div>Name: {{ minimalDragon.name }}</div>
            <div>Last Updated: {{ minimalDragon.updatedTime }}</div>
          </Message>
        </div>
      </div>
  
      <!-- Character Count Section -->
      <div class="mb-6">
        <h3 class="text-xl mb-2">Count Dragons by Character</h3>
        <div class="flex gap-2">
          <InputText 
            v-model="characterInput" 
            placeholder="Enter character"
            class="w-64"
          />
          <Button 
            label="Count Dragons" 
            @click="countDragonsWithCharacter"
            :loading="loading.count"
          />
        </div>
        <div v-if="dragonCount !== null" class="mt-2">
          <Message severity="info">
            Number of dragons with character greater than {{ characterInput }}: {{ dragonCount }}
          </Message>
        </div>
      </div>
  
      <!-- Dragon Character Grouping Section -->
      <div class="mb-6">
        <h3 class="text-xl mb-2">Dragon Character Grouping</h3>
        <Button 
          label="Get Grouping" 
          @click="getCharacterGrouping"
          :loading="loading.grouping"
        />
        <DataTable 
          v-if="characterGrouping.length" 
          :value="characterGrouping"
          class="mt-2"
        >
          <Column field="character" header="Character"></Column>
          <Column field="count" header="Count"></Column>
        </DataTable>
      </div>
  
      <!-- Kill Dragon Section -->
      <div class="mb-6">
        <h3 class="text-xl mb-2">Kill Dragon</h3>
        <div class="flex gap-2">
          <InputNumber 
            v-model="dragonIdToKill" 
            placeholder="Enter dragon ID"
            class="w-64"
          />
          <Button 
            label="Kill Dragon" 
            severity="danger"
            @click="killDragon"
            :loading="loading.kill"
          />
        </div>
      </div>
  
      <!-- Toast for notifications -->
      <Toast position="top-right" />
    </div>
  </template>
  
  <script setup>
  import { ref } from 'vue';
  import { useToast } from 'primevue/usetoast';
  import axios from 'axios';
import Button from 'primevue/button'
import InputText from 'primevue/inputtext'
import InputNumber from 'primevue/inputnumber'
import DataTable from 'primevue/datatable'
import Column from 'primevue/column'
import Message from 'primevue/message'
import Toast from 'primevue/toast'
  
  const toast = useToast();
  
  // State management
  const minimalDragon = ref(null);
  const characterInput = ref('');
  const dragonCount = ref(null);
  const characterGrouping = ref([]);
  const dragonIdToKill = ref(null);
  
  const loading = ref({
    minimal: false,
    count: false,
    grouping: false,
    kill: false
  });
  
  // Create axios instance with base configuration
  const api = axios.create({
    baseURL: '/api/special-dragons',
    headers: {
      'Content-Type': 'application/json',
      'Accept': 'application/json'
    }
  });
  
  // DateTime formatter
  const formatDateTime = (zonedDateTime) => {
    if (!zonedDateTime) return 'N/A';
    
    try {
      // Parse the ZonedDateTime string and format it
      const date = new Date(zonedDateTime);
      return new Intl.DateTimeFormat('default', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit',
        timeZoneName: 'short'
      }).format(date);
    } catch (error) {
      console.error('Error formatting date:', error);
      return 'Invalid date';
    }
  };
  
  // Transform character grouping data
  const transformGroupingData = (data) => {
    return data.map(([character, count]) => ({
      character,
      count
    }));
  };
  
  // Error handler
  const handleError = (error) => {
    const message = error.response?.data || error.message;
    toast.add({
      severity: 'error',
      summary: 'Error',
      detail: typeof message === 'string' ? message : 'An error occurred',
      life: 3000
    });
  };
  
  // Get minimal ID dragon
  const getMinimalDragon = async () => {
    loading.value.minimal = true;
    try {
      const { data } = await api.get('/minimal');
      minimalDragon.value = data;
      toast.add({
        severity: 'success',
        summary: 'Success',
        detail: 'Retrieved minimal ID dragon',
        life: 3000
      });
    } catch (error) {
      handleError(error);
    } finally {
      loading.value.minimal = false;
    }
  };
  
  // Count dragons with character greater than input
  const countDragonsWithCharacter = async () => {
    if (!characterInput.value) {
      toast.add({
        severity: 'warn',
        summary: 'Warning',
        detail: 'Please enter a character',
        life: 3000
      });
      return;
    }
  
    loading.value.count = true;
    try {
      const { data } = await api.get(`/count-with-character-greater/${characterInput.value}`);
      dragonCount.value = data;
    } catch (error) {
      handleError(error);
    } finally {
      loading.value.count = false;
    }
  };
  
  // Get character grouping
  const getCharacterGrouping = async () => {
    loading.value.grouping = true;
    try {
      const { data } = await api.get('/character-grouping');
      // Transform the array of tuples into array of objects for DataTable
      characterGrouping.value = transformGroupingData(data);
    } catch (error) {
      handleError(error);
    } finally {
      loading.value.grouping = false;
    }
  };
  
  // Kill dragon by ID
  const killDragon = async () => {
    if (!dragonIdToKill.value) {
      toast.add({
        severity: 'warn',
        summary: 'Warning',
        detail: 'Please enter a dragon ID',
        life: 3000
      });
      return;
    }
  
    loading.value.kill = true;
    try {
      await api.delete(`/${dragonIdToKill.value}/kill`);
      toast.add({
        severity: 'success',
        summary: 'Success',
        detail: `Dragon ${dragonIdToKill.value} has been killed`,
        life: 3000
      });
      dragonIdToKill.value = null;
    } catch (error) {
      handleError(error);
    } finally {
      loading.value.kill = false;
    }
  };
  </script>