import { Routes, Route } from 'react-router-dom';
import { UserProvider } from './common/Context/UserContext';
import RegisterPage from './user/Page/RegisterPage';
import LoginPage from './user/Page/LoginPage';
import UserInfoPage from './user/Page/UserInfoPage';


function App() {

  return (
    <>
    <UserProvider>
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/info" element={<UserInfoPage/>}/>
      </Routes>
    </UserProvider>
    </>
  )
}

export default App
