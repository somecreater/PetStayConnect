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
    <div className="PostListPage">
      <h1 className="PageTitle">질문 목록</h1>

      <div className="Actions">
        <Button
          type="button"
          title="질문 등록"
          classtext="PrimaryButton"
          onClick={() => navigate('/qnas/register')}
        />
      </div>

      <ul className="PostList">
        {posts.map(post => (
          <li key={post.id} className="PostListItem">
              <div className="PostHeader">
                  <span className="PostId">#{post.id}</span>
                  <Link to={`/qnas/${post.id}`} className="PostLink">
                        {post.title}
                  </Link>
               </div>
                <span className="PostMeta">
                조회수: {post.viewCount} | 작성일: {new Date(post.createdAt).toLocaleString()}
                </span>
               </li>
            ))}
        </ul>

      <div className="Pagination">
        <Button
          type="button"
          title="Prev"
          classtext="PageButton"
          onClick={() => setPage(p => p - 1)}
          disabled={page === 0}
        />

        {[...Array(totalPages)].map((_, idx) => (
          <Button
            key={idx}
            type="button"
            title={`${idx + 1}`}
            classtext={`PageButton${idx === page ? 'Active' : ''}`}
            onClick={() => setPage(idx)}
          />
        ))}

        <Button
          type="button"
          title="Next"
          classtext="PageButton"
          onClick={() => setPage(p => p + 1)}
          disabled={page + 1 >= totalPages}
        />
      </div>
    </div>
  );
}
