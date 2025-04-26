import axios from 'axios';
import { API_BASE_URL, API_ENDPOINTS } from './Api';

const RefreshApi = axios.create({
    baseURL: API_BASE_URL,
    withCredentials: true,
});

RefreshApi.interceptors.response.use(
  (response) =>  response,
  (error) => {
    if(error.response?.status === 401){
      window.location.href = '/user/login';
    }
    return Promise.reject(error);
  }

);

export default RefreshApi;
