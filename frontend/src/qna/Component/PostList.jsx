import React, { useState, useEffect } from 'react';
import { Link, useNavigate }           from 'react-router-dom';
import ApiService                      from '../../common/Api/ApiService';
import Button                          from '../../common/Ui/Button';
import '../../common/Css/common.css';

export default function PostListPage() {
  const [posts, setPosts]           = useState([]);
  const [page, setPage]             = useState(0);
  const [size]                      = useState(5);
  const [totalPages, setTotalPages] = useState(0);
  const navigate                    = useNavigate();

  const fetchPosts = async (pageIndex) => {
    try {
      const res = await ApiService.qnas.post.list(pageIndex, size);
      setPosts(res.data.content);
      setTotalPages(res.data.totalPages);
    } catch (err) {
      console.error('목록 불러오기 실패:', err);
    }
  };

  useEffect(() => {
    fetchPosts(page);
  }, [page]);

  return (
    <div className="container py-4">
      <h1 className="mb-4">질문 목록</h1>

      <div className="mb-3">
        <Button
          type="button"
          title="질문 등록"
          classtext="btn btn-primary"
          onClick={() => navigate('/qnas/register')}
        />
      </div>

      <ul
        className="list-group mb-4 overflow-auto"
        style={{ maxHeight: '60vh' }}
      >
        {posts.map(post => (
          <li key={post.id} className="list-group-item d-flex justify-content-between align-items-start">
            <div>
              <span className="badge bg-secondary me-2">#{post.id}</span>
              <Link to={`/qnas/${post.id}`} className="fw-bold text-decoration-none">
                {post.title}
              </Link>
            </div>
            <small className="text-muted">
              조회수: {post.viewCount} | {new Date(post.createdAt).toLocaleString()}
            </small>
          </li>
        ))}
      </ul>

      <nav>
        <ul className="pagination justify-content-center">
          <li className={`page-item${page === 0 ? ' disabled' : ''}`}>
            <button className="page-link" onClick={() => setPage(p => p - 1)}>
              Prev
            </button>
          </li>
          {[...Array(totalPages)].map((_, idx) => (
            <li key={idx} className={`page-item${idx === page ? ' active' : ''}`}>
              <button className="page-link" onClick={() => setPage(idx)}>
                {idx + 1}
              </button>
            </li>
          ))}
          <li className={`page-item${page + 1 >= totalPages ? ' disabled' : ''}`}>
            <button className="page-link" onClick={() => setPage(p => p + 1)}>
              Next
            </button>
          </li>
        </ul>
      </nav>
    </div>
  );
}
