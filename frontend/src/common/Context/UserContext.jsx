import React, { createContext, useEffect, useContext, useReducer  } from 'react';

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

  useEffect(() => {
    const storedUser = sessionStorage.getItem('user');
    if (storedUser) {
      dispatch({ type: 'SET_USER', payload: JSON.parse(storedUser) });
    }
  }, []);

  
  return (
    <UserContext.Provider value={{ user, mapUserDto, updateUser, resetUser }}>
      {children}
    </UserContext.Provider>
  );
};
