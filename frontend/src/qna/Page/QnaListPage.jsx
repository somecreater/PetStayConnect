import React, { useState, useEffect } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import ApiService from '../../common/Api/ApiService'
import Button from '../../common/Ui/Button'

export default function QnaListPage() {
  const [posts, setPosts] = useState([])
  const [page, setPage] = useState(0)
  const [size] = useState(10)
  const [totalPages, setTotalPages] = useState(0)
  const [totalElements, setTotalElements] = useState(1);
  const navigate = useNavigate()

  const loadPosts = async (pageIndex) => {
    try {
      const res = await ApiService.qnas.post.list(pageIndex, size)
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
      <h2 className="mb-4">질문 목록</h2>

        <table className="table table-hover table-bordered text-center align-middle">
          <thead className="table-light">
            <tr>
              <th style={{ width: '5%' }}>No</th>
              <th className="text-start">제목</th>
              <th style={{ width: '15%' }}>작성자</th>
              <th style={{ width: '15%' }}>작성일</th>
            </tr>
          </thead>
          <tbody>
            {posts.map((post, index) => (
              <tr key={post.id}>
                <td>{post.id}</td>
                <td className="text-start">
                  <Link
                    to={`/qnas/${post.id}`}
                    className="text-decoration-none text-dark fw-semibold"
                  >
                    {post.title}
                  </Link>
                </td>
                <td>{post.userLoginId}</td>
                <td>{new Date(post.createdAt).toLocaleDateString()}</td>
              </tr>
            ))}
          </tbody>
        </table>

        {/* Pagination */}
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

        <div className="d-flex justify-content-end mt-3">
          <button className="btn btn-dark" onClick={() => navigate('/qnas/register')}>
            글쓰기
          </button>
        </div>
       </div>
      );
    }