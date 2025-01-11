import { createRouter, createWebHashHistory, createWebHistory } from 'vue-router'
import MainPage from '@/pages/MainPage.vue'
import AuthPage from '@/pages/AuthPage.vue'

const routes = [
  {
    path: '/',
    name: 'home',
    component: MainPage,
    meta: {requiresAuth: true },
  },
  {
    path: '/main',
    name: 'main',
    component: MainPage,
    meta: {requiresAuth: true },
  },
  {
    path: '/login',
    name: 'login',
    component: AuthPage
  },
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
});

router.beforeEach((to, from, next) => {
  if (to.meta.requiresAuth && !localStorage.getItem('isAuthorized')) {
    next('/login');
  } else {
    next();
  };
});

export default router;
