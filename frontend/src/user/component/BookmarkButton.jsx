import React from 'react';
import { FaHeart, FaRegHeart } from 'react-icons/fa';
import { useUser } from '../../common/Context/UserContext';

export default function BookmarkButton({ type, targetId, size = 24 }) {
  const { user, toggleBookmark } = useUser();
  const isBookmarked = user.bookmarkDTOList.some(
    b => b.type === type && b.targetId === targetId
  );
  return (
    <button
      onClick={() => toggleBookmark(type, targetId)}
      style={{ background: 'none', border: 'none', cursor: 'pointer' }}
      aria-label={isBookmarked ? '북마크 해제' : '북마크'}
    >
      {isBookmarked
        ? <FaHeart color="#ff4d4f" size={size} />
        : <FaRegHeart color="#bbb" size={size} />}
    </button>
  );
}
