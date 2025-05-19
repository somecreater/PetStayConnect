import React, { useContext } from 'react';
import { Link, NavLink } from 'react-router-dom';
import { useNavigate } from 'react-router-dom'
import { UserContext } from '../Context/UserContext'; // 로그인, 로그아웃 버튼 추가. 경로 체크
import { FaUser } from 'react-icons/fa';

export default function Header() {
    // 1. useContext, useNavigate 선언
    const { user, resetUser } = useContext(UserContext);
    const navigate = useNavigate();

    // 2. 여기! 로그아웃 버튼 클릭 시 실행할 함수 선언
    const handleLogout = () => {
        resetUser();
        navigate('/');
  };

  return (
    <nav className="navbar navbar-expand-lg navbar-light navbar-custom-bg p-4 border-top">
      <div className="container-fluid">
        <Link className="navbar-brand brand-logo" to="/">PetStory</Link>

        {/* 모바일(작은 화면)에서만 보이는 아이콘 + 햄버거 */}
        <div className="d-flex align-items-center ms-auto d-lg-none">
          <button
            className="navbar-toggler"
            type="button"
            data-bs-toggle="collapse"
            data-bs-target="#navMenu"
            aria-controls="navMenu"
            aria-expanded="false"
            aria-label="Toggle navigation"
            style={{ marginRight: '10px' }}
          >
            <span className="navbar-toggler-icon" />
          </button>
          <Link
            to="/user/info"
            className="d-flex align-items-center"
            title="MyPage"
            style={{
              background: 'none',
              border: 'none',
              boxShadow: 'none',
              padding: '6px 10px',
              color: '#888'
            }}
          >
            <FaUser size={22} />
          </Link>
          {/* 모바일에서 로그인/로그아웃 버튼 추가*/}
          {user && user.id ? (
              <button
                className="btn btn-outline-dark ms-2"
                onClick={handleLogout}
              >
                로그아웃
              </button>
            ) : (
              <Link
                to="/user/login"
                className="btn btn-outline-dark ms-2"
              >
                로그인
              </Link>
            )}
          </div>

        {/* 메뉴(햄버거로 접히는 부분) */}
        <div className="collapse navbar-collapse" id="navMenu">
          <ul className="navbar-nav ms-auto">
            <li className="nav-item">
              <NavLink className="nav-link text-dark" to="/">Home</NavLink>
            </li>
             <li className="nav-item">
               <NavLink className="nav-link text-dark" to="/reservation">reservation</NavLink>
             </li>
             <li className="nav-item dropdown">
               <a
                 className="nav-link dropdown-toggle text-dark"
                 href="#!"
                 id="servicesMenu"
                 role="button"
                 data-bs-toggle="dropdown"
                 aria-expanded="false"
               >
                 Services
               </a>
               <ul className="dropdown-menu" aria-labelledby="servicesMenu">
                 <li><Link className="dropdown-item" to="/about">About</Link></li>
                 <li><Link className="dropdown-item" to="/contact">Contact</Link></li>
               </ul>
             </li>
            <li className="nav-item">
                 <NavLink className="nav-link text-dark disabled" to="#!">Coming Soon</NavLink>
                  </li>
                  <li className="nav-item">
              <form className="d-flex ms-lg-3 align-items-center">
                <input
                  className="form-control form-control-sm"
                  type="search"
                  placeholder="Search"
                  aria-label="Search"
                />
                <button className="btn btn-outline-dark btn-sm ms-2" type="submit">Go</button>
                {/* PC(큰 화면)에서만 보이는 아이콘 */}
                <Link
                  to="/user/info"
                  className="d-none d-lg-flex align-items-center ms-2"
                  title="MyPage"
                  style={{
                    background: 'none',
                    border: 'none',
                    boxShadow: 'none',
                    padding: '6px 10px',
                    color: '#888'
                  }}
                >
                  <FaUser size={22} />
                </Link>
                {/* PC에서만 보이는 로그인/로그아웃 버튼 추가*/}
                  {user && user.id ? (
                    <button
                      className="btn btn-outline-dark ms-2 d-none d-lg-block"
                      onClick={handleLogout}
                    >
                      로그아웃
                    </button>
                  ) : (
                    <Link
                      to="/user/login"
                      className="btn btn-outline-dark ms-2 d-none d-lg-block"
                    >
                      로그인
                    </Link>
                  )}
                </form>
            </li>
          </ul>
        </div>
      </div>
    </nav>
  );
}
