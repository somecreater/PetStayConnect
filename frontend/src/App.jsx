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

import CustomErrorPage from './common/ErrorPage/CustomErrorPage';
import BusinessTypePage from './businesstype/Page/BusinessTypePage';
import BusinessTypeManagePage from './businesstype/Page/BusinessTypeManagePage';


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
        
        <Route path="/type/list" element={<BusinessTypePage/>} />
        <Route path="/type/manage" element={<BusinessTypeManagePage/>} />

        <Route path="/error/:code" element={<CustomErrorPage />} />
        <Route path="*" element={<CustomErrorPage/>} />
      </Routes>
    </UserProvider>
    </>
  )
}

export default App
