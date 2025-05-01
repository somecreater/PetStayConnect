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

  pet:{
    register: (dto) => RefreshApi.post(API_ENDPOINTS.pets, dto,  { 
      headers: createHeaders(),
      withCredentials: true, 
    }),
    update: (petId,dto) => RefreshApi.put(`${API_ENDPOINTS.pets}/${petId}`, dto,{
      headers: createHeaders(),
      withCredentials: true, 
    }),
    delete: (petId) => RefreshApi.delete(`${API_ENDPOINTS.pets}/${petId}`,{
      headers: createHeaders(),
      withCredentials: true, 
    }),
    userpet: () => RefreshApi.get(`${API_ENDPOINTS.pets}/user`,{
      headers: createHeaders(),
      withCredentials: true, 
    }),
    detail: (petId) => RefreshApi.get(`${API_ENDPOINTS.pets}/${petId}`,{
      headers: createHeaders(),
      withCredentials: true, 
    })
  },
  qnas:{
    post:{
      register: (dto) => RefreshApi.post(API_ENDPOINTS.qna+ `/question`, dto, {
        headers: createHeaders(),
        withCredentials: true, 
      }),
      list: (page,size) => RefreshApi.get(API_ENDPOINTS.qna+ `/list`,{
        params:{ page,size },
        headers: createHeaders(),
        withCredentials: true,   
      }),
      mine: () => RefreshApi.get(API_ENDPOINTS.qna+ `/mine`,{
        headers: createHeaders(),
        withCredentials: true, 
      }),
      detail: (question_id) => RefreshApi.get(`${API_ENDPOINTS.qna}/${question_id}`, {
        headers: createHeaders(),
        withCredentials: true, 
      }),
      update: (question_id,dto) => RefreshApi.put(`${API_ENDPOINTS.qna}/${question_id}`, dto, 
      {
        headers: createHeaders(),
        withCredentials: true, 
      }),
      delete: (question_id) => RefreshApi.delete(`${API_ENDPOINTS.qna}/${question_id}`,{
        headers: createHeaders(),
        withCredentials: true, 
      })
    },
    answer:{
      register: (question_id,dto) => RefreshApi.post(`${API_ENDPOINTS.qna}/${question_id}/answer`, dto,
      {
        headers: createHeaders(),
        withCredentials: true,  
      }),
      list: (question_id) => RefreshApi.get(`${API_ENDPOINTS.qna}/${question_id}/answer`,{
        headers: createHeaders(),
        withCredentials: true,  
      }),
      detaiil: (question_id,answer_id) => RefreshApi.get(`${API_ENDPOINTS.qna}/${question_id}/answer/${answer_id}`,
      {
        headers: createHeaders(),
        withCredentials: true,
      }),
      update: (question_id,answer_id,dto) => RefreshApi.put(`${API_ENDPOINTS.qna}/${question_id}/answer/${answer_id}`,
      dto,
      {
        headers: createHeaders(),
        withCredentials: true,
      }),
      delete: (question_id,answer_id) => RefreshApi.delete(`${API_ENDPOINTS.qna}/${question_id}/answer/${answer_id}`,
      {
        headers: createHeaders(),
        withCredentials: true,
      }),
      accept: (question_id,answer_id) => RefreshApi.post(`${API_ENDPOINTS.qna}/${question_id}/answer/${answer_id}/accept`,
      {
        headers: createHeaders(),
        withCredentials: true,
      })
    }
  },
  businessTypeService:{
    list: () => RefreshApi.get(API_ENDPOINTS.businesstype, {
      headers: createHeaders(),
      withCredentials: true,
    }),
    detail: (type_id) => RefreshApi.get(`${API_ENDPOINTS.businesstype}/${type_id}`, {
      headers: createHeaders(),
      withCredentials: true,
    }),
    register: (dto) => RefreshApi.post(API_ENDPOINTS.businesstype, dto, {
      headers: createHeaders(),
      withCredentials: true,
    }),
    update: (type_id,dto) => RefreshApi.put(`${API_ENDPOINTS.businesstype}/${type_id}`,
    dto,
    {
      headers: createHeaders(),
      withCredentials: true,
    }),
    delete: (type_id,dto) => RefreshApi.delete(`${API_ENDPOINTS.businesstype}/${type_id}`,
    {
      headers: createHeaders(),
      withCredentials: true,
      data: dto
    })
  },

  businessService:{

  },

}

export default ApiService;