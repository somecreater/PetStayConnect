import axios from 'axios';
import { API_BASE_URL } from './Api';
const RefreshApi = axios.create({
    baseURL: API_BASE_URL,
    withCredentials: true,
});

RefreshApi.interceptors.response.use(
  (response) =>  response,
  (error) => {
    const status=error.response?.status;
    const key = 'lastError';
    const payload = error.response?.data || {}
    localStorage.setItem(key, JSON.stringify(payload));

    if      (status === 401) window.location.href='/error/401';
    else if (status === 402) window.location.href='/error/402';
    else if (status === 403) window.location.href='/error/403';
    else if (status === 404) window.location.href='/error/404';
    else if (status >= 400 && status < 500) window.location.href='/error/unknown';
    else if (status >= 500) window.location.href='/error/500';

    return Promise.reject(error);
  }

);

export default RefreshApi;
