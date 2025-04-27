import React from 'react';
import RefreshApi from './RefreshApi'
import { API_ENDPOINTS, createHeaders } from './Api';
import axios from 'axios';

const ApiService = {
  
    userService:{
    login: (dto) => RefreshApi.post(API_ENDPOINTS.auth.login, dto, {
      headers: createHeaders(),
      withCredentials: true, 
    }),
    register: (dto) => RefreshApi.post(API_ENDPOINTS.auth.register, dto, { 
      headers: createHeaders(),
      withCredentials: true, 
    }),

    delete: (loginId,password) => RefreshApi.post(API_ENDPOINTS.auth.delete, 
    { LoginId: loginId, Password: password }, 
    {
      headers: createHeaders(),
      withCredentials: true,
    }),
    update: (dto) => RefreshApi.put(API_ENDPOINTS.auth.update, dto, {
      headers: createHeaders(),
      withCredentials: true,
    }),
    logout: ()=> RefreshApi.post(API_ENDPOINTS.auth.logout, null, {
      headers: createHeaders(),
      withCredentials: true,
    }), 
    info: () => RefreshApi.get(API_ENDPOINTS.auth.info, {
      headers: createHeaders(),
      withCredentials: true,
    }),
    detail: (userLoginId) =>RefreshApi.get(API_ENDPOINTS.auth.detail, { 
      params: { userLoginId }, 
      headers: createHeaders(),
      withCredentials: true, 
    }), 
  },

  businessService:{
    typelist: () => RefreshApi.get(),

  },

}

export default ApiService;