import React from 'react';
import { Link, NavLink } from 'react-router-dom';
import { FaUser } from 'react-icons/fa';

export default function Header() {
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
              </form>
            </li>
          </ul>
        </div>
      </div>
    </nav>
  );
}
