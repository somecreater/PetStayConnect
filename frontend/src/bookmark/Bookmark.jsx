import React, {useState} from 'react';
import { FaHeart } from 'react-icons/fa'
import { Link } from 'react-router-dom';

const styles = {
  item: {
    display: 'flex',
    alignItems: 'center',
    background: '#fff7ea',
    borderRadius: 12,
    padding: '0.75em 1em',
    boxShadow: '0 2px 8px rgba(0,0,0,0.1)',
    marginBottom: '0.75em',
    transition: 'transform 0.1s ease-in-out',
    cursor: 'pointer',
    textDecoration: 'none', 
    color: 'inherit',
    border: '4px solid #d6c390',
    transition: 'background 0.2s ease',
  },
  itemHover: {
    transform: 'scale(1.02)',
  },
  icon: {
    flexShrink: 0,
    marginRight: 16,
  },
  thumbnail: {
    width: 56,
    height: 56,
    borderRadius: 8,
    objectFit: 'cover',
    marginRight: 16,
    flexShrink: 0,
  },
  content: {
    flex: 1,
  },
  title: {
    margin: 0,
    fontSize: '1rem',
    fontWeight: 600
  },
  meta: {
    marginTop: 4,
    display: 'flex',
    flexDirection: 'column',
    gap: '4px',
    fontSize: '0.875rem',
    color: '#666',
  },
  badge: {
    borderRadius: 4,
    color: '#000',
    padding: '2px 6px',
    fontSize: '0.75rem',
    marginRight: 8,
  },
};

// 북마크 하나를 보여주는 컴포넌트
export default function Bookmark({ item }) {

  const {id, bookmarkType, targetId, createAt, updateAt}= item;

  const typeLabel = bookmarkType === 'BUSINESS_PROVIDER' ? '서비스' : '질문';
  const formattedDate = new Date(createAt).toLocaleDateString('ko-KR', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
  });
  const [hover, setHover] = useState(false);
  const linkTo =
    bookmarkType === 'QNA' 
      ? `/qnas/${targetId}`
      : `/business/list`;

  return (
    <li>
      <Link
        to={linkTo}
        style={{
          ...styles.item,
          ...(hover ? styles.itemHover : {}),
        }}
        onMouseEnter={() => setHover(true)}
        onMouseLeave={() => setHover(false)}
      >
        <FaHeart style={styles.icon} size={20} color="#f44336" />


        <div style={styles.content}>
          <h4 style={styles.title}>{`#${id}`}</h4>
          <div style={styles.meta}>
            <span style={styles.badge}>{typeLabel}</span>
            <span>아이디: {targetId}</span>
            <span>생성일: {formattedDate}</span>
          </div>
        </div>
      </Link>
    </li>
  );
}