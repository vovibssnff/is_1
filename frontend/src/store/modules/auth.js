import axios from 'axios';

export const auth = {
  namespaced: true,
  state: {
    isAuthorized: false,
    userId: null,
    username: null,
    userRole: null,
    loginErrors: {
      login: '',
      password: ''
    },
    notification: ''
  },
  mutations: {
    setAuthorized(state, value) {
      state.isAuthorized = value;
    },
    setUserData(state, userData) {
      state.userId = userData.id;
      state.username = userData.username;
      state.userRole = userData.role;
    },
    setLoginErrors(state, errors) {
      state.loginErrors = errors;
    },
    setNotification(state, message) {
      state.notification = message;
    },
    clearErrors(state) {
      state.loginErrors = {
        login: '',
        password: ''
      };
      state.notification = '';
    }
  },
  actions: {
    async loginUser({ commit, state }, credentials) {
      try {
        const response = await axios.post('/api/auth/login', credentials);
        if (response.status === 200) {
          const userData = response.data;
          commit('setUserData', userData);
          commit('setAuthorized', true);
          localStorage.setItem('isAuthorized', 'true');
          localStorage.setItem('userId', userData.id);
          localStorage.setItem('username', userData.username);
          localStorage.setItem('userRole', userData.role);
          return { success: true, userData };
        }
        return { success: false, error: 'Login failed' };
      } catch (error) {
        commit('setNotification', 'Invalid username or password');
        return { success: false, error: error.message };
      }
    },
    async registerUser({ commit }, credentials) {
      try {
        const response = await axios.post('/api/auth/register', credentials);
        if (response.status === 201) {
          commit('setNotification', 'Registration successful, now you can log in!');
          return { success: true };
        }
        return { success: false, error: response.data.message };
      } catch (error) {
        const errorMessage = error.response?.data || 'Registration failed';
        commit('setNotification', errorMessage);
        return { success: false, error: errorMessage };
      }
    }
  },
  getters: {
    isAuthenticated: state => state.isAuthorized,
    currentUser: state => ({
      userId: state.userId,
      username: state.username,
      role: state.userRole
    }),
    getErrors: state => state.loginErrors,
    getNotification: state => state.notification
  }
};