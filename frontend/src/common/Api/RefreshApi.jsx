import axios from 'axios';
import { API_BASE_URL, API_ENDPOINTS } from './Api';

const RefreshApi = axios.create({
    baseURL: API_BASE_URL,
    withCredentials: true,
});


RefreshApi.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;
    if (error.response && error.response.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;
      try {
        const storedRefreshToken = localStorage.getItem('refreshToken');
        const refreshResponse = await axios.post(
          API_ENDPOINTS.auth.refresh,
          { refreshToken: storedRefreshToken }, 
          { withCredentials: true }
        );
        return RefreshApi(originalRequest);
      } catch (refreshError) {
        
        return Promise.reject(refreshError);
      }
    }
    return Promise.reject(error);
  }
);

export default RefreshApi;