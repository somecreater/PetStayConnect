import axios from 'axios';

// 북마크 목록 불러오기
export async function fetchBookmarks() {
  const res = await axios.get('/api/bookmarks');
  // 실제 응답 구조에 따라 bookmarks만 반환
  return res.data.bookmarks || [];
}
// 북마크 추가
export async function addBookmark({ bookmarkType, targetId }) {
  return axios.post(`/api/bookmarks?bookmarkType=${bookmarkType}&targetId=${targetId}`);
}
// 북마크 삭제
export async function removeBookmark({ bookmarkType, targetId }) {
  return axios.delete(`/api/bookmarks?bookmarkType=${bookmarkType}&targetId=${targetId}`);
}
// 북마크 체크
export async function checkBookmark({ bookmarkType, targetId }) {
  const res = await axios.get(`/api/bookmarks/check?bookmarkType=${bookmarkType}&targetId=${targetId}`);
  return res.data.isBookmarked;
}
