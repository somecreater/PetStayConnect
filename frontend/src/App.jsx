import { Routes, Route } from 'react-router-dom';
import { UserProvider } from './common/Context/UserContext';
import HomePage from './common/Page/HomePage.jsx';

import Header from './common/Ui/Header'; //헤더추가
import Footer from './common/Ui/Footer';//푸터추가

import AboutPage from './common/Page/AboutPage';
import ContactPage from './common/Page/ContactPage';

import './common/Api/Api'
import './common/Api/RefreshApi';
import './common/Api/ApiService'

import CustomErrorPage from './common/ErrorPage/CustomErrorPage';
import BusinessSearchPage from './business/Page/BusinessSearchPage';

import RegisterPage from './user/Page/RegisterPage';
import LoginPage from './user/Page/LoginPage';
import UserInfoPage from './user/Page/UserInfoPage';
import UpdatePage from './user/Page/UpdatePage';
import DeletePage from './user/Page/DeletePage';
import PetManagePage from './pet/Page/PetManagePage';

import BusinessTypePage from './businesstype/Page/BusinessTypePage';
import BusinessTypeManagePage from './businesstype/Page/BusinessTypeManagePage';

import QnaListPage from './qna/Page/QnaListPage';
import PostRegisterPage from './qna/Page/PostRegisterPage';
import QnaDetailPage from './qna/Page/QnaDetailPage';

import ReviewListPage from './review/Page/ReviewListPage';
import ReviewDetailPage from './review/Page/ReviewDetailPage';
import ReviewRegisterPage from './review/Page/ReviewRegisterPage';
import MyReviewListPage from './review/Page/MyReviewListPage';

import BusinessManagePage from './business/Page/BusinessManagePage';
import UserReservationManagePage from './business/Page/UserReservationManagePage.jsx';

function App() {

  return (
    <div className="app-wrapper">

    <UserProvider>
      <Header />
        <main className="main-content">
      <Routes>

        <Route path="/about" element={<AboutPage />} />
        <Route path="/contact" element={<ContactPage />} />

        <Route path="/" element={<HomePage />} />
        <Route path="/user/login" element={<LoginPage />} />
        <Route path="/user/register" element={<RegisterPage />} />
        <Route path="/user/info" element={<UserInfoPage/>}/>
        <Route path="/user/update" element={<UpdatePage/>}/>
        <Route path="/user/delete" element={<DeletePage/>}/>
        <Route path="/user/petmanage" element={<PetManagePage/>} />
        <Route path="/user/reservations" element={<UserReservationManagePage/>} />
        <Route path="/type/list" element={<BusinessTypePage/>} />
        <Route path="/type/manage" element={<BusinessTypeManagePage/>} />

        <Route path="/business/list" element={<BusinessSearchPage/>}/>
        <Route path="/business/manage" element={<BusinessManagePage/>}/>

        <Route path="/qnas" element={<QnaListPage/>}/>
        <Route path="/qnas/register" element={<PostRegisterPage/>}/>
        <Route path="/qnas/:id" element={<QnaDetailPage/>}/>

        <Route path="/reviews" element={<ReviewListPage/>}/>
        <Route path="/reviews/:id" element={<ReviewDetailPage/>}/>
        <Route path="/reviews/my"    element={<MyReviewListPage />} />
        <Route path="/reviews/register" element={<ReviewRegisterPage/>}/>

        <Route path="/error/:code" element={<CustomErrorPage />} />
        <Route path="*" element={<CustomErrorPage/>} />
      </Routes>
      </main>
      <Footer/>
    </UserProvider>
   </div>
  );
}

export default App
