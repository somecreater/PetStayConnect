import React from 'react';
import BookmarkList from './BookmarkList';

export default function MyPage() {
  return (
    <div style={{ maxWidth: 600, margin: '0 auto', padding: '2em' }}>
      <h2>마이페이지</h2>
      {/* 북마크 리스트만 마이페이지에서 보여줌 */}
      <BookmarkList />
    </div>
  );
}
