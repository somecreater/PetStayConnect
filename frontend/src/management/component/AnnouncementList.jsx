import React from 'react';
import { Link } from 'react-router-dom';

function AnnouncementList(props){

  const {list}=props;

  return (
    <div className="container py-4">
      <ul
        className="list-group mb-4 overflow-auto"
        style={{ maxHeight: '60vh' }}
      >
        {list.map(announcement=>(
          <li key={announcement.id} className="list-group-item d-flex justify-content-between align-items-start">
            <div>
              <span className="badge bg-secondary me-2">#{announcement.id}</span>
              <Link to={``} className="fw-bold text-decoration-none">
                {announcement.title}
              </Link>
            </div>
            <small className="text-muted">
              작성자: {announcement.userLoginId}
            </small>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default AnnouncementList;