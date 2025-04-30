export const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080';
export const API_ENDPOINTS = {
  auth:{
    refresh:`${API_BASE_URL}/api/user/refresh`,
    register: `${API_BASE_URL}/api/user/register`,
    delete: `${API_BASE_URL}/api/user/delete`,
    update: `${API_BASE_URL}/api/user/update`,
    login: `${API_BASE_URL}/api/user/login`,
    logout: `${API_BASE_URL}/api/user/logout`,
    info: `${API_BASE_URL}/api/user/info`,
    detail: `${API_BASE_URL}/api/user/detailInfo`,
  },
  oauth2Google:{
    googleLogin:`${API_BASE_URL}/oauth2/authorization/google`
  },
  pets: `${API_BASE_URL}/api/pets`,
  qna: `${API_BASE_URL}/api/`
}

export const createHeaders = () => ({
  'Content-Type': 'application/json',
});

