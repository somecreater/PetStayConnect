import React from 'react';
import { FaHeart } from 'react-icons/fa'

// 북마크 하나를 보여주는 컴포넌트
export default function Bookmark({ item }) {
  return (
    <li style={{ marginBottom: '1em', display: 'flex', alignItems: 'center' }}>
         <FaHeart color="#f44336" size={24} style={{marginRight: 12}} />
      <img src={item.image} alt={item.name} width={60} height={60} style={{ borderRadius: 8, marginRight: 16 }} />
      <span>{item.name}</span>
    </li>
  );
}
