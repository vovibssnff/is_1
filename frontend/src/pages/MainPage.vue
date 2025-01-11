<template>
  <div class="app-container">
    <div class="menu-container">
      <Menu :model="menuItems" class="w-full md:w-60">
        <template #start>
          <span class="inline-flex items-center gap-1 px-2 py-2">
            <span class="text-xl font-semibold">Dragon<span class="text-primary">App</span></span>
          </span>
        </template>
        
        <template #item="{ item, props }">
          <a 
            v-ripple 
            class="flex items-center" 
            v-bind="props.action" 
            @click.prevent="handleMenuClick(item)"
          >
            <span :class="item.icon" />
            <span>{{ item.label }}</span>
          </a>
        </template>

        <template #end>
          <div v-ripple class="relative overflow-hidden w-full border-0 bg-transparent flex items-start p-2 pl-4 hover:bg-surface-100 dark:hover:bg-surface-800 rounded-none">
            <span class="inline-flex flex-col items-start">
              <span class="font-bold">name: {{ username }} </span>
              <span class="text-sm">role: {{ userRole }} </span>
              <span class="text-xs text-gray-500">id: {{ userId }} </span>
            </span>
          </div>
        </template>
      </Menu>
    </div>

    <div class="content-container">
      <component
        :is="currentTable"
        :dragons="dragons"
        :persons="persons"
        :dragonCaves="dragonCaves"
        :dragonHeads="dragonHeads"
        @edit-dragon="openEditDragonForm"
      />
    </div>
    
    <Toast position="top-right" />
  </div>
</template>

<script>
import { ref, computed } from 'vue';
import { useStore } from 'vuex';
import { useRouter } from 'vue-router';
import { useToast } from 'primevue/usetoast';
import axios from 'axios';
import Menu from 'primevue/menu';
import Avatar from 'primevue/avatar';
import DragonTable from '@/components/tables/DragonTable.vue';
import DragonHeadTable from '@/components/tables/DragonHeadTable.vue';
import PersonTable from '@/components/tables/PersonTable.vue';
import DragonCaveTable from '@/components/tables/DragonCaveTable.vue';
import DragonSpecialTable from '@/components/tables/DragonSpecialTable.vue';
import AdminDashboard from '@/components/tables/AdminDashboard.vue';

export default {
  name: 'App',
  components: {
    Menu,
    Avatar,
    DragonTable,
    DragonHeadTable,
    PersonTable,
    DragonCaveTable,
    DragonSpecialTable,
    AdminDashboard
  },
  setup() {
    const store = useStore();
    const router = useRouter();
    const toast = useToast();
    const isRequestingAdmin = ref(false);

    const currentTable = ref('DragonTable');

    // User data from Vuex store
    const userId = computed(() => store.state.auth.userId);
    const username = computed(() => store.state.auth.username);
    const userRole = computed(() => store.state.auth.userRole);
    const isAdmin = computed(() => store.state.auth.userRole === 'ADMIN');
    const isUser = computed(() => store.state.auth.userRole === 'USER');

    const requestAdmin = async () => {
      if (isRequestingAdmin.value) return;
      
      isRequestingAdmin.value = true;
      try {
        await axios.post('/api/auth/admin-request');
        toast.add({
          severity: 'success',
          summary: 'Request Sent',
          detail: 'Admin rights request has been submitted successfully',
          life: 3000
        });
      } catch (error) {
        toast.add({
          severity: 'error',
          summary: 'Request Failed',
          detail: error.response?.data || 'Failed to submit admin rights request',
          life: 3000
        });
      } finally {
        isRequestingAdmin.value = false;
      }
    };

    const handleMenuClick = (item) => {
      if (item.command) {
        item.command();
      }
    };

    const menuItems = computed(() => [
      {
        label: 'Tables',
        items: [
          {
            label: 'Dragons',
            icon: 'pi pi-table',
            command: () => currentTable.value = 'DragonTable'
          },
          {
            label: 'Dragon Heads',
            icon: 'pi pi-table',
            command: () => currentTable.value = 'DragonHeadTable'
          },
          {
            label: 'Dragon Caves',
            icon: 'pi pi-table',
            command: () => currentTable.value = 'DragonCaveTable'
          },
          {
            label: 'Persons',
            icon: 'pi pi-table',
            command: () => currentTable.value = 'PersonTable'
          },
          {
            label: 'Dragon Special',
            icon: 'pi pi-table',
            command: () => currentTable.value = 'DragonSpecialTable'
          }
        ]
      },
      {
        label: 'Account',
        items: [
          ...(isUser.value ? [{
            label: 'Request Admin',
            icon: 'pi pi-user-plus',
            command: () => requestAdmin()
          }] : []),
          ...(isAdmin.value ? [{
            label: 'Admin Panel',
            icon: 'pi pi-cog',
            command: () => {
              console.log('Switching to AdminDashboard');
              currentTable.value = 'AdminDashboard';
            }
          }] : []),
          {
            label: 'Logout',
            icon: 'pi pi-sign-out',
            command: () => logout()
          }
        ]
      }
    ]);

    const logout = async () => {
      await store.dispatch('auth/logout');
      router.push('/login');
    };

    return {
      currentTable,
      menuItems,
      userId,
      username,
      userRole,
      handleMenuClick,
      dragons: ref([]),
      dragonHeads: ref([]),
      persons: ref([]),
      dragonCaves: ref([]),
    };
  }
};
</script>

<style scoped>
.app-container {
  display: flex;
  min-height: 100vh;
}

.menu-container {
  min-width: 250px;
  border-right: 1px solid var(--surface-border);
}

.content-container {
  flex: 1;
  padding: 20px;
}
</style>