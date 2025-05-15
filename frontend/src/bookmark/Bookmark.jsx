import React from 'react';
import { FaHeart } from 'react-icons/fa'

// 북마크 하나를 보여주는 컴포넌트
export default function Bookmark({ item }) {
  return (
    <li
      style={{
        marginBottom: '1em',
        display: 'flex',
        alignItems: 'center',
        background: '#fff7ea',
        borderRadius: '12px',
        padding: '0.5em 1em',
        boxShadow: '0 2px 8px rgba(0,0,0,0.04)',
      }}
    >
      <FaHeart color="#f44336" size={22} style={{ marginRight: 12 }} />
      {item.image && (
        <img
          src={item.image}
          alt={item.name}
          width={48}
          height={48}
          style={{ borderRadius: 8, marginRight: 16, objectFit: 'cover' }}
        />
      )}
      <span style={{ fontWeight: 500, fontSize: '1.08rem' }}>{item.name}</span>
      {/* 필요하다면 북마크 타입, 날짜 등 추가 */}
    </li>
  );
}