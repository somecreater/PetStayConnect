import React, { createContext, useEffect, useContext, useReducer  } from 'react';
import axios from 'axios';

const initialUser = {
  id: null,
  userLoginId: '',
  email: '',
  name: '',
  phone: '',
  loginType: '',
  petNumber: '',
  qnaScore: 0,
  point: 0,
  createAt: '',
  updateAt: '',
  role: null,
  petBusinessDTO: {
    id:'',
    businessName:'',
    status:'',
    minPrice: 0,
    maxPrice: 100000000,
    facilities: '',
    description: '',
    avgRate: 0,
    registrationNumber: '',
    bankAccount: '',
    varification: '',
    province: '',
    city: '',
    town: '',
    userId: '',
    petBusinessTypeName: '',
    petBusinessTypeId: '',
  },
  petDTOList: [],
  bookmarkDTOList: [],
  qnaPostDTOList: [],
  qnaAnswerDTOList: [],
};

const mapUserDto= (dto) => {
  return {
    id:            dto.id,
    userLoginId:   dto.userLoginId,
    email:         dto.email,
    name:          dto.name,
    phone:         dto.phone,
    loginType:     dto.loginType,
    petNumber:     dto.petNumber,
    qnaScore:      dto.qnaScore,
    point:         dto.point,
    createAt:      dto.createAt,
    updateAt:      dto.updateAt,
    role:          dto.role,

    petBusinessDTO: dto.petBusinessDTO
      ? {
          id:                  dto.petBusinessDTO.id,
          businessName:        dto.petBusinessDTO.businessName,
          status:              dto.petBusinessDTO.status,
          minPrice:            dto.petBusinessDTO.minPrice,
          maxPrice:            dto.petBusinessDTO.maxPrice,
          facilities:          dto.petBusinessDTO.facilities,
          description:         dto.petBusinessDTO.description,
          avgRate:             dto.petBusinessDTO.avgRate,
          registrationNumber:  dto.petBusinessDTO.registrationNumber,
          bankAccount:         dto.petBusinessDTO.bankAccount,
          varification:        dto.petBusinessDTO.varification,
          province:            dto.petBusinessDTO.province,
          city:                dto.petBusinessDTO.city,
          town:                dto.petBusinessDTO.town,
          userId:              dto.petBusinessDTO.userId,
          petBusinessTypeName: dto.petBusinessDTO.petBusinessTypeName,
          petBusinessTypeId:   dto.petBusinessDTO.petBusinessTypeId,
        }
      : null,
    petDTOList:       dto.petDTOList       || [],
    bookmarkDTOList:  dto.bookmarkDTOList  || [],
    qnaPostDTOList:   dto.qnaPostDTOList   || [],
    qnaAnswerDTOList: dto.qnaAnswerDTOList || [],
  };
}

const UserContext = createContext({
  user: initialUser,
  updateUser: () => {},
  resetUser: () => {},
  toggleBookmark: () => {},
});

const userReducer = (state, action) => {
  switch (action.type) {
    case 'SET_USER':
      return { ...state, ...action.payload };
    case 'RESET_USER':
      return initialUser;
    default:
      return state;
  }
};


export const useUser = () => useContext(UserContext);

export const UserProvider = ({ children }) => {
  const [user, dispatch] = useReducer(userReducer, initialUser);

  const updateUser = (newData) => {
    const updatedUser = { ...user, ...newData };
    dispatch({ type: 'SET_USER', payload: updatedUser });
    sessionStorage.setItem('user', JSON.stringify(updatedUser));
  };
  
  const resetUser = () => {
    dispatch({ type: 'RESET_USER' });
    sessionStorage.removeItem('user');
  };
    // ✅ 북마크 토글 함수 추가 (QnA, 호텔 등에서 하트 클릭 시 사용)
    const toggleBookmark = async (type, targetId) => {
      try {
        // 이미 북마크 되어있는지 확인
        const isBookmarked = user.bookmarkDTOList.some(
          b => b.type === type && b.targetId === targetId
        );

        if (isBookmarked) {
          // 북마크 해제 (DELETE)
          // 퀴리로 받는다면 await axios.delete(`/api/bookmarks?bookmarkType=${type}&targetId=${targetId}`);
          await axios.delete('/api/bookmarks', {
            data: { bookmarkType: type, targetId }
          });
          // 상태에서 해당 북마크 제거
          const updatedList = user.bookmarkDTOList.filter(
            b => !(b.type === type && b.targetId === targetId)
          );
          dispatch({
            type: 'SET_USER',
            payload: { ...user, bookmarkDTOList: updatedList }
          });
          sessionStorage.setItem('user', JSON.stringify({ ...user, bookmarkDTOList: updatedList }));
        } else {
          // 북마크 추가 (POST)
          await axios.post('/api/bookmarks', { bookmarkType: type, targetId });
          // 상태에 북마크 추가
          const updatedList = [...user.bookmarkDTOList, { type, targetId }];
          dispatch({
            type: 'SET_USER',
            payload: { ...user, bookmarkDTOList: updatedList }
          });
          sessionStorage.setItem('user', JSON.stringify({ ...user, bookmarkDTOList: updatedList }));
        }
      } catch (error) {
        console.error('북마크 토글 실패:', error);
      }
    };


  useEffect(() => {
    const storedUser = sessionStorage.getItem('user');
    if (storedUser) {
      dispatch({ type: 'SET_USER', payload: JSON.parse(storedUser) });
    }
  }, []);

  
  return (
    <UserContext.Provider value={{ user, mapUserDto, updateUser, resetUser, toggleBookmark }}>
      {children}
    </UserContext.Provider>
  );
};
