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
      <h3>내 북마크</h3>
      <ul style={{ listStyle: 'none', padding: 0 }}>
        {bookmarks.map(item => (
          <Bookmark key={item.id} item={item} />
        ))}
      </ul>
    </div>
  );
}
