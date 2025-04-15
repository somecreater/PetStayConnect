import React, { createContext, useEffect, useContext, useReducer  } from 'react';

const initialUser = {
  id: null,
  userLoginId: '',
  email: '',
  name: '',
  role: null,
};
  
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
  
  useEffect(() => {
    const storedUser = localStorage.getItem('user');
    if (storedUser) {
      dispatch({ type: 'SET_USER', payload: JSON.parse(storedUser) });
    }
  }, []);

  const updateUser = (newData) => {
    dispatch({ type: 'SET_USER', payload: newData });
    localStorage.setItem('user', JSON.stringify({ ...user, ...newData }));
  };
  
  const resetUser = () => {
    dispatch({ type: 'RESET_USER' });
    localStorage.removeItem('user');
  };
  
  return (
    <UserContext.Provider value={{ user, updateUser, resetUser }}>
      {children}
    </UserContext.Provider>
  );
};