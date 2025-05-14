import React, { useEffect, useState } from 'react';
import { fetchBookmarks } from '../../common/Api/BookmarkApi';

export default function BookmarkList() {
  const [bookmarks, setBookmarks] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchBookmarks()
      .then(setBookmarks)
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <div>불러오는 중...</div>;
  if (!bookmarks.length) return <div>북마크한 항목이 없습니다.</div>;

  return (
    <div>
      <h3>내 북마크</h3>
      <ul style={{ listStyle: 'none', padding: 0 }}>
        {bookmarks.map(item => (
          <li key={item.id} style={{ marginBottom: '1em', display: 'flex', alignItems: 'center' }}>
            <img src={item.image} alt={item.name} width={60} height={60} style={{ borderRadius: 8, marginRight: 16 }} />
            <span>{item.name}</span>
          </li>
        ))}
      </ul>
    </div>
  );
}
