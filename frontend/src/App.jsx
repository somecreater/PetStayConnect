import { Routes, Route } from 'react-router-dom';
import { UserProvider } from './common/Context/UserContext';
import RegisterPage from './user/Page/RegisterPage';
import LoginPage from './user/Page/LoginPage';
import UserInfoPage from './user/Page/UserInfoPage';
import UpdatePage from './user/Page/UpdatePage';
import DeletePage from './user/Page/DeletePage';


function App() {

  return (
    <>
    <UserProvider>
      <Routes>
        <Route path="/user/login" element={<LoginPage />} />
        <Route path="/user/register" element={<RegisterPage />} />
        <Route path="/user/info" element={<UserInfoPage/>}/>
        <Route path="/user/update" element={<UpdatePage/>}/>
        <Route path="/user/delete" element={<DeletePage/>}/>
      </Routes>
    </UserProvider>
    </>
  )
}

export default App
