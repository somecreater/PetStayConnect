import React, { useState, useEffect } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import ApiService from '../../common/Api/ApiService'
import Button from '../../common/Ui/Button'

export default function QnaListPage() {
  const [posts, setPosts] = useState([])
  const [page, setPage] = useState(0)
  const [size] = useState(5)
  const [totalPages, setTotalPages] = useState(0)
  const navigate = useNavigate()

  const loadPosts = async (pageIndex) => {
    try {
      const res = await ApiService.qnas.post.list(pageIndex, size)
      console.log('받은 데이터:', res.data) // 👈 이거 꼭 찍어봐
      setPosts(res.data.content)
      setTotalPages(res.data.totalPages)
    } catch (err) {
      console.error('목록 불러오기 실패:', err)
    }
  }

  useEffect(() => {
    loadPosts(page)
  }, [page, size])

  return (
    <div className="container py-4">
      <div className="d-flex justify-content-between align-items-center mb-3">
        <h1>질문 목록</h1>
        <Button
          type="button"
          title="질문 등록"
          classtext="btn btn-primary"
          onClick={() => navigate('/qnas/register')}
        />
      </div>

      <ul className="list-group mb-4">
        {posts.map((post) => (
          <li key={post.id} className="list-group-item">
            <div className="d-flex justify-content-between">
             <div>
              <div className="mb-1 d-flex align-items-center">
                <span className="text-muted small me-2">#{post.id}</span>
                <Link to={`/qnas/${post.id}`} className="fw-bold text-decoration-none text-dark">
                  {post.title}
                </Link>
              </div>
              <div className="text-muted small">
                작성자: <span className="fw-semibold">{post.userLoginId}</span> &nbsp;|&nbsp;
                작성일: {new Date(post.createdAt).toLocaleString()}
              </div>
            </div>
            <small className="text-muted">
               조회수 {post.viewCount}
            </small>
           </div>
          </li>
        ))}
      </ul>

      <nav>
        <ul className="pagination justify-content-center">
          <li className={`page-item ${page === 0 ? 'disabled' : ''}`}>
            <button className="page-link" onClick={() => setPage(p => Math.max(p - 1, 0))}>
              Previous
            </button>
          </li>
          {[...Array(totalPages)].map((_, idx) => (
            <li key={idx} className={`page-item ${idx === page ? 'active' : ''}`}>
              <button className="page-link" onClick={() => setPage(idx)}>
                {idx + 1}
              </button>
            </li>
          ))}
          <li className={`page-item ${page + 1 >= totalPages ? 'disabled' : ''}`}>
            <button className="page-link" onClick={() => setPage(p => Math.min(p + 1, totalPages - 1))}>
              Next
            </button>
          </li>
        </ul>
      </nav>
    </div>
  );
}
