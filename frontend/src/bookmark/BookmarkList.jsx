import React, { useEffect, useState } from 'react';
import { fetchBookmarks } from './BookmarkApi';
import Bookmark from './Bookmark';

export default function BookmarkList() {
  const [bookmarks, setBookmarks] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchBookmarks()
      .then(setBookmarks)
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <div>불러오는 중...</div>;
  if (!bookmarks.length) return <div>관심목록에 등록된 항목이 없습니다.</div>;

return (
    <div>
      <h3 style={{ marginBottom: '1.5rem', color: '#ff9800', fontWeight: 700 }}>내 관심목록</h3>
      <ul style={{ listStyle: 'none', padding: 0, maxWidth: 420, margin: '0 auto' }}>
        {bookmarks.map(item => (
          <Bookmark key={item.id} item={item} />
        ))}
      </ul>
    </div>
  );
}