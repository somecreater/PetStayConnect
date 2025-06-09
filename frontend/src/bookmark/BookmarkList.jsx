import React, { useEffect, useState } from 'react';
import Bookmark from './Bookmark';
import ApiService from '../common/Api/ApiService';
import Button from '../common/Ui/Button';

export default function BookmarkList() {
  const [bookmarks, setBookmarks] = useState([]);
  const [loading, setLoading] = useState(true);
  const [isOpen, setIsOpen] = useState(false);
  const bookmarklist= async () => {
    const response= await ApiService.bookmark.list();
    const data=response.data;
    
    if(data.result){
      setBookmarks(data.bookmarks);
      setLoading(false);
    }
  }
  useEffect(() => {
    bookmarklist();
  }, []);

  if (loading) return <div>불러오는 중...</div>;
  if (!bookmarks.length) return <div>관심목록에 등록된 항목이 없습니다.</div>;

return (
    <div>
      <h3 style={{ marginBottom: '1.5rem', color: '#ff9800', fontWeight: 700 }}>내 관심목록</h3>
      <Button
        classtext="btn btn-sm btn-outline-secondary"
        type="button"
        title={isOpen ? '접기' : '펼치기'}
        onClick={(e) => { e.stopPropagation(); setIsOpen(prev => !prev); }}
      />
      {isOpen &&(<ul style={{ listStyle: 'none', padding: 0, maxWidth: 420, margin: '0 auto' }}>
        {bookmarks.map(item => (
          <Bookmark key={item.id} item={item} />
        ))}
      </ul>
      )}
    </div>
  );
}
