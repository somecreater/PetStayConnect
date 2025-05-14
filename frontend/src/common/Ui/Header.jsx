import React from 'react';
import { Link, NavLink } from 'react-router-dom';

export default function Header() {
  return (
    <nav className="navbar navbar-expand-lg navbar-light bg-warning-subtle p-4 border-top">
      <div className="container-fluid">
        <Link className="navbar-brand brand-logo" to="/">PetStory</Link>
        <button
          className="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#navMenu"
          aria-controls="navMenu"
          aria-expanded="false"
          aria-label="Toggle navigation"
        >
          <span className="navbar-toggler-icon" />
        </button>
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
                <NavLink className="nav-link text-dark disabled" to="#!">Coming Soon</NavLink>            </li>
            <li className="nav-item">
              <form className="d-flex ms-lg-3">
                <input
                  className="form-control form-control-sm"
                  type="search"
                  placeholder="Search"
                  aria-label="Search"
                />
                <button className="btn btn-outline-dark btn-sm ms-2" type="submit">Go</button>              </form>
            </li>
          </ul>
        </div>
      </div>
    </nav>
  );
}
