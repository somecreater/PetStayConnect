import React from 'react';

function primaryColor(priority){
  switch(priority){
    case 'SPECIAL': return 'badge bg-danger';
    case 'GENERAL': return 'badge bg-success';
    default: return 'badge bg-secondary';
  }
}


function Announcement(props){
  const {announcement}= props;
  const {id, title, type, content, priority, createdAt, updatedAt, userLoginId} = announcement;

  const formatDate = (dateString) =>
    new Date(dateString).toLocaleDateString('ko-KR', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
  });

  return (
    <div className="card mb-4">
      <div className="card-body">
        <h2 className="fw-bold d-flex align-items-center gap-2">
          <span className="text-secondary">#{id}</span>
          <span className="badge bg-info text-white text-uppercase">{type}</span>
          <span className="flex-grow-1">{title}</span>
          <span className={getBadgeClass(priority)}>{priority}</span>
        </h2>

        <div
          className="border rounded p-4 mb-3 bg-white"
          style={{ minHeight: '120px', lineHeight: '1.6', whiteSpace: 'pre-wrap' }}
        >
          {content}
        </div>

        <p className="text-muted small mb-0">
          작성자: <span className="fw-semibold">{userLoginId}</span> |
          작성일: <span className="fw-semibold">{formatDate(createdAt)}</span> |
          수정일: <span className="fw-semibold">{formatDate(updatedAt)}</span>
        </p>
      </div>
    </div>
  );
}

export default Announcement;