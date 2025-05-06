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
    <div className="QnaListPage">
      <h1 className="PageTitle">질문 목록</h1>
      <section className="QnaListPage-Actions">
        <Button
          type="button"
          title="질문 등록"
          classtext="PrimaryButton"
          onClick={() => navigate('/qnas/register')}
        />
      </section>
      <ul className="PostList">
        {posts.map((post) => (
          <li key={post.id} className="PostListItem">
            <p className="PostListItem-Header">
              <span className="PostListItem-Id">#{post.id}</span>
              <span className="PostListItem-Title">
                <Link to={`/qnas/${post.id}`} className="PostListItem-Link">
                  {post.title}
                </Link>
              </span>
            </p>
            <p className="PostListItem-Meta">
              <span className="Meta-Views">조회수: {post.viewCount}</span>
              <span className="Meta-Date">
                작성일: {new Date(post.createdAt).toLocaleString()}
              </span>
            </p>
          </li>
        ))}
      </ul>
      <nav className="PostListPage-Pagination">
        <Button
          type="button"
          title="Prev"
          classtext="PageButton"
          onClick={() => setPage((p) => Math.max(p - 1, 0))}
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
          onClick={() => setPage((p) => Math.min(p + 1, totalPages - 1))}
          disabled={page + 1 >= totalPages}
        />
      </nav>
    </div>
  )
}
