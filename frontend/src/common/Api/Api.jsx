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
  qna: `${API_BASE_URL}/api/qnas`,
  businesstype:`${API_BASE_URL}/api/type`,
  business:{
    org: `${API_BASE_URL}/api/provider`,
    list: `${API_BASE_URL}/api/provider/list`,
    outerlist: `${API_BASE_URL}/api/provider/list/naver`,
    reservation: `${API_BASE_URL}/api/provider/reservation`,
    bnsReservation: `${API_BASE_URL}/api/provider/provider/reservation`,
    conReservation: `${API_BASE_URL}/api/provider/consumer/reservation`,
  },
  businessroom: `${API_BASE_URL}/api/room`,
  businessvalidation: `${API_BASE_URL}/api/validation`,
  reviews: `${API_BASE_URL}/api/reviews`,
  payments: `${API_BASE_URL}/api/payments`,
  bookmarks: `${API_BASE_URL}/api/bookmarks`
}

export const createHeaders = () => ({
  'Content-Type': 'application/json',
});

