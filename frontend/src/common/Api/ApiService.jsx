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
    }),
    reservation: (reservation_id) => RefreshApi.get(`${API_ENDPOINTS.pets}/reservation/${reservation_id}`,{
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
  business:{
    detail: (business_id) => RefreshApi.get(`${API_ENDPOINTS.business.org}/${business_id}`,{
      headers: createHeaders(),
      withCredentials: true
    }),
    tag: (tag,page,size) => RefreshApi.get(API_ENDPOINTS.business.tag,{
      params: { ...tag, page, size},
      headers: createHeaders(),
      withCredentials: true
    }),
    list: (dto,page,size) => RefreshApi.get(API_ENDPOINTS.business.list, 
    {
      params:{ ...dto, page, size},
      headers: createHeaders(),
      withCredentials: true
    }),
    outerlist: (dto,page,size) => RefreshApi.get(API_ENDPOINTS.business.outerlist,
    {
      params:{ ...dto, page, size},
      headers: createHeaders(),
      withCredentials: true
    }),
    reservation: (dto, provider_id) => RefreshApi.post(`${API_ENDPOINTS.business.org}/${provider_id}/reservation`,
    dto,
    {
      headers: createHeaders(),
      withCredentials: true,
    }),
    bnsReservation: (page,size) => RefreshApi.get(API_ENDPOINTS.business.bnsReservation,
    {
      params:{page, size},
      headers: createHeaders(),
      withCredentials: true,
    }),
    conReservation: (page,size) => RefreshApi.get(API_ENDPOINTS.business.conReservation,
    {
      params:{page, size},
      headers: createHeaders(),
      withCredentials: true,
    }),
    detailReservation: (reservation_id) => RefreshApi.get(`${API_ENDPOINTS.business.reservation}/${reservation_id}`,
    {
      headers: createHeaders(),
      withCredentials: true,
    }),
    deleteReservation: (reservation_id) => RefreshApi.delete(`${API_ENDPOINTS.business.reservation}/${reservation_id}`,
    {
      headers: createHeaders(),
      withCredentials: true,
    }),
    updateReservation: (dto, reservation_id)=> RefreshApi.put(`${API_ENDPOINTS.business.reservation}/${reservation_id}`,
    dto,
    {
      headers: createHeaders(),
      withCredentials: true,
    }),
  },
  businesstag:{
    list: (business_id) => RefreshApi.get(`${API_ENDPOINTS.businesstag}/business/${business_id}`,
    {
      headers: createHeaders(),
      withCredentials: true,
    }),
    detail: (id) => RefreshApi.get(`${API_ENDPOINTS.businesstag}/${id}`,
    {
      headers: createHeaders(),
      withCredentials: true,
    }),
    register: (dto) => RefreshApi.post(`${API_ENDPOINTS.businesstag}`,
    dto,
    {
      headers: createHeaders(),
      withCredentials: true,
    }),
    delete: (id) => RefreshApi.delete(`${API_ENDPOINTS.businesstag}/${id}`,
    {
      headers: createHeaders(),
      withCredentials: true,
    })
  },
  businessroom:{
    list: (business_id)=> RefreshApi.get(`${API_ENDPOINTS.businessroom}/${business_id}`,
    {
      headers: createHeaders(),
      withCredentials: true,
    }),
    detail: (business_id, room_id)=> RefreshApi.get(`${API_ENDPOINTS.businessroom}/${business_id}/${room_id}`,
    {
      headers: createHeaders(),
      withCredentials: true,
    }),
    typeDetail:(business_id, room_type) =>RefreshApi.get(`${API_ENDPOINTS.businessroom}/${business_id}/${room_type}`,
    {
      headers: createHeaders(),
      withCredentials: true,
    }),
    update: (business_id, room_id, dto)=> RefreshApi.put(`${API_ENDPOINTS.businessroom}/${business_id}/${room_id}`,
    dto,
    {
      headers: createHeaders(),
      withCredentials: true,
    }),
    register: (business_id,dto)=> RefreshApi.post(`${API_ENDPOINTS.businessroom}/${business_id}`,
    dto,
    {
      headers: createHeaders(),
      withCredentials: true,
    }),
    delete: (business_id, room_id)=> RefreshApi.delete(`${API_ENDPOINTS.businessroom}/${business_id}/${room_id}`,
    {
      headers: createHeaders(),
      withCredentials: true,
    })
  },
  businessvalidation: {
    validation: (dto) => RefreshApi.post(`${API_ENDPOINTS.businessvalidation}/business`, dto, {
      headers: createHeaders(),
      withCredentials: true,
    })
  },
  reviews:{
    list: (page = 0, size = 10) => RefreshApi.get(`${API_ENDPOINTS.reviews}`, {
     params: { page, size },
     headers: createHeaders(),
     withCredentials: true,
     }),
    detail: reviewId => RefreshApi.get(`${API_ENDPOINTS.reviews}/${reviewId}`, {
      headers: createHeaders(),
      withCredentials: true,
    }),
    register: dto => RefreshApi.post(API_ENDPOINTS.reviews, dto, {
      headers: createHeaders(),
      withCredentials: true,
    }),
    update: (reviewId, dto) => RefreshApi.put(`${API_ENDPOINTS.reviews}/${reviewId}`, dto, {
      headers: createHeaders(),
      withCredentials: true,
    }),
    delete: reviewId => RefreshApi.delete(`${API_ENDPOINTS.reviews}/${reviewId}`, {
      headers: createHeaders(),
      withCredentials: true,
    }),
    myList: (page = 0, size = 10) => RefreshApi.get(`${API_ENDPOINTS.reviews}/my`, {
     params: { page, size },
     headers: createHeaders(),
     withCredentials: true,
    }),
},

  payments:{
    userlist: (page, size)=> RefreshApi.get(`${API_ENDPOINTS.payments}/user`,{
      params:{page, size},
      headers: createHeaders(),
      withCredentials: true,
    }),
    bnslist: (business_id, page, size) => RefreshApi.get(`${API_ENDPOINTS.payments}/business`,{
      params: {business_id, page, size},
      headers: createHeaders(),
      withCredentials: true,
    }),
    detail: (payment_id)=> RefreshApi.get(`${API_ENDPOINTS.payments}/${payment_id}`,{
      headers: createHeaders(),
      withCredentials: true,
    }),
    register: (dto)=>RefreshApi.post(API_ENDPOINTS.payments, dto, {
      headers: createHeaders(),
      withCredentials: true,
    }),
    pointregister: (dto)=>RefreshApi.post(`${API_ENDPOINTS.payments}/point`, dto, {
      headers: createHeaders(),
      withCredentials: true,
    }),
    delete: (dto, payment_id)=>RefreshApi.delete(`${API_ENDPOINTS.payments}/${payment_id}`,{
      headers: createHeaders(),
      withCredentials: true,
      data: dto
    })
  },
  accounts:{
    list: (page, size)=>RefreshApi.get(`${API_ENDPOINTS.accounts}/list`,{
      params:{page, size},
      headers: createHeaders(),
      withCredentials: true,
    }),
    user: ()=> RefreshApi.get(`${API_ENDPOINTS.accounts}/user`,{
      headers: createHeaders(),
      withCredentials: true,
    }),
    business: (business_id)=> RefreshApi.get(`${API_ENDPOINTS.accounts}/business/${business_id}`,{
      headers: createHeaders(),
      withCredentials: true,
    })
  },
  bookmark:{
    register: (bookmarkType, targetId)=> RefreshApi.post(API_ENDPOINTS.bookmarks,
      null,
      {
        params: { bookmarkType, targetId },
        headers: createHeaders(),
        withCredentials: true,
      }),
    delete: (bookmarkType, targetId)=> RefreshApi.delete(API_ENDPOINTS.bookmarks,{
      params: { bookmarkType, targetId },
      headers: createHeaders(),
      withCredentials: true,
    }),
    list: ()=> RefreshApi.get(API_ENDPOINTS.bookmarks,{
      headers: createHeaders(),
      withCredentials: true,
    }),
    check: (bookmarkType, targetId)=> RefreshApi.get(`${API_ENDPOINTS.bookmarks}/check`, {
      params: { bookmarkType, targetId },
      headers: createHeaders(),
      withCredentials: true,
    })
  },
  breed:{
    catList: () => RefreshApi.get(`${API_ENDPOINTS.breed}/cat`,{
      headers: createHeaders(),
      withCredentials: true,
    }),
    cat: (cat_id) => RefreshApi.get(`${API_ENDPOINTS.breed}/cat/${cat_id}`,{
      headers: createHeaders(),
      withCredentials: true,
    }),
    dogList: () => RefreshApi.get(`${API_ENDPOINTS.breed}/dog`,{
      headers: createHeaders(),
      withCredentials: true,
    }),
    dog: (dog_id) => RefreshApi.get(`${API_ENDPOINTS.breed}/dog/${dog_id}`,{
      headers: createHeaders(),
      withCredentials: true,
    })
  },
  recommend: (latitude, longitude, distance, page, size, searchType) =>
    RefreshApi.get(`${API_ENDPOINTS.recommend}`,{
      params:{latitude, longitude, distance, page, size, searchType},
      headers: createHeaders(),
      withCredentials: true,
    })
};


export default ApiService;
