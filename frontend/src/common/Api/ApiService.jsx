import React from 'react';
import RefreshApi from '../../common/Api/RefreshApi';
import { API_ENDPOINTS, createHeaders } from '../../common/Api/Api';
import axios from 'axios';

const header = () => ({ headers: createHeaders() });

const ApiService = {
  
    userService:{
    //로그인이나 회원 가입 과정에는 토큰 불필요
    login: (dto) => axios.post(API_ENDPOINTS.auth.login, dto, { withCredentials: true }),
    register: (dto) => axios.post(API_ENDPOINTS.auth.register, dto, { withCredentials: true }),

    delete: (loginId,password) => RefreshApi.post(API_ENDPOINTS.auth.delete, { LoginId: loginId, Password: password }, 
      ...header(),),
    update: (dto) => RefreshApi.put(API_ENDPOINTS.auth.update, dto, header(), ),
    logout: ()=> RefreshApi.post(API_ENDPOINTS.auth.logout, null, header(), ), 
    info: () => RefreshApi.get(API_ENDPOINTS.auth.info, header()),
    detail: (userLoginId) =>RefreshApi.get(API_ENDPOINTS.auth.detail, 
      { params: { userLoginId }, ...header() }) 
  },

  businessService:{
    typelist: () => RefreshApi.get(),

  },

}

export default ApiService;