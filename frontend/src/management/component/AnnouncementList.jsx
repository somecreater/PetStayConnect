import React from 'react';
import { Link } from 'react-router-dom';

function primaryColor(priority){
  switch(priority){
    case 'SPECIAL': return 'badge bg-danger';
    case 'GENERAL': return 'badge bg-success';
    default: return 'badge bg-secondary';
  }
}

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
              <span className={`${primaryColor(announcement.priority)} me-2`}>#{announcement.id}</span>
              <Link to={`/announcements/${announcement.id}`} className="fw-bold text-decoration-none">
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