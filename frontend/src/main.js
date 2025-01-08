import { createApp } from 'vue'
import PrimeVue from 'primevue/config';
// import Api from 'primevue/core/api';
import ToastService from 'primevue/toastservice';
import Aura from '@primevue/themes/aura';
import App from './App.vue'
import router from './router'
import store from './store'

createApp(App)
    .use(store)
    .use(router)
    .use(PrimeVue, {
        theme: {
            preset: Aura,
        },
    })
    .use(ToastService)
    .mount('#app')
