import { Routes, Route } from 'react-router-dom';
import { UserProvider } from './common/Context/UserContext';
import './common/Api/Api'
import './common/Api/RefreshApi';
import './common/Api/ApiService'

import RegisterPage from './user/Page/RegisterPage';
import LoginPage from './user/Page/LoginPage';
import UserInfoPage from './user/Page/UserInfoPage';
import UpdatePage from './user/Page/UpdatePage';
import DeletePage from './user/Page/DeletePage';

import CustomUnauthorizedPage from './common/ErrorPage/CustomUnauthorizedPage';
import CustomForbiddenPage from './common/ErrorPage/CustomForbiddenPage';
import CustomServerErrorPage from './common/ErrorPage/CustomServerErrorPage';
import CustomErrorPage from './common/ErrorPage/CustomErrorPage';
import CustomNotFoundPage from './common/ErrorPage/CustomNotFoundPage';


function App() {

  return (
    <>
    <UserProvider>
      <Routes>
        <Route path="/error/401" element={<CustomUnauthorizedPage />} />
        <Route path="/error/403" element={<CustomForbiddenPage />} />
        <Route path="/error/404" element={<CustomNotFoundPage/>} />
        <Route path="/error/500" element={<CustomServerErrorPage />} />
        <Route path="/error/unknown" element={<CustomErrorPage />} />

        <Route path="/user/login" element={<LoginPage />} />
        <Route path="/user/register" element={<RegisterPage />} />
        <Route path="/user/info" element={<UserInfoPage/>}/>
        <Route path="/user/update" element={<UpdatePage/>}/>
        <Route path="/user/delete" element={<DeletePage/>}/>
        <Route path="*" element={<CustomErrorPage/>} />
      </Routes>
    </UserProvider>
    </>
  )
}

export default App
