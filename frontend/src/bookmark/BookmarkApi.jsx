import axios from 'axios';

// 북마크 목록 불러오기
export async function fetchBookmarks() {
  const res = await axios.get('/api/bookmarks');
  // 실제 응답 구조에 따라 bookmarks만 반환
  return res.data.bookmarks || [];
}
