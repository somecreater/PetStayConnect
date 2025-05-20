import React, { useState, useEffect } from 'react';
import { useParams, Link, useNavigate } from 'react-router-dom';
import ApiService from '../../common/Api/ApiService';
import { useUser } from '../../common/Context/UserContext';
import Button from '../../common/Ui/Button';
import PostDeleteButton from '../component/PostDeleteButton';
import PostUpdateForm from '../form/PostUpdateForm';
import AnswerList from '../component/AnswerList';
import AnswerRegisterForm from '../form/AnswerRegisterForm';
import BookmarkButton from '../../user/component/BookmarkButton';

export default function QnaDetailPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const { user } = useUser();

  const [post, setPost] = useState(null);
  const [answers, setAnswers] = useState([]);
  const [showAnswerForm, setShowAnswerForm] = useState(false);
  const [isEditingPost, setIsEditingPost] = useState(false);
  const [editingAnswerId, setEditingAnswerId] = useState(null);
  const [loading, setLoading] = useState(true);

  const loadData = async () => {
    try {
      setLoading(true);
      const { data: postData } = await ApiService.qnas.post.detail(id);
      const { data: ansData } = await ApiService.qnas.answer.list(id);
      const normalizedAnswers = ansData.map(ans => ({
        ...ans,
        accepted: ans.isAdopted,
        content: ans.content ?? ans.answer
      }));
      setPost(postData);
      setAnswers(normalizedAnswers);
    } catch (error) {
      console.error('Load failed:', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadData();
  }, [id]);

  if (loading) return <p>Loading...</p>;
  if (!post)  return <p>No post found.</p>;
  // 권한 체크: 작성자 또는 사업자 오너인지
  const isOwner = item =>
    (user?.id === item.userId) ||
    (user?.petBusinessDTO?.id === item.businessId);

  const canEditPost = isOwner(post);
  const canAccept = canEditPost;
  const isBiz = Boolean(user?.petBusinessDTO);

  const handleAccept = async answerId => {
    try {
      await ApiService.qnas.answer.accept(id, answerId);
      loadData();
    } catch (error) {
      console.error('Accept failed:', error);
    }
  };

   return (
    <div className="container py-4">
      {isEditingPost ? (
        <div className="card mb-4">
          <div className="card-body">
            <PostUpdateForm
              initialData={post}
              onSuccess={() => { setIsEditingPost(false); loadData(); }}
            />
          </div>
        </div>
      ) : (
        <div className="card mb-4">
          <div className="card-body">
            <h2 className="fw-bold d-flex align-items-center gap-2">
              <span className="text-secondary">#{post.id}</span>
              <span className="flex-grow-1">{post.title}</span>
              <BookmarkButton type="QNA" targetId={post.id} size={24} className="ms-2" />
            </h2>
            <div className="border rounded p-3 mb-3"
               style={{
                    minHeight: '120px',
                    lineHeight: '1.6',
                    whiteSpace: 'pre-wrap',
                    backgroundColor: '#fff'
               }}>
              {post.content}
            </div>
            <p className="text-muted small">
              작성자: <span className="fw-semibold">{post.userLoginId}</span> |
              작성일: {new Date(post.createdAt).toLocaleString()}
            </p>

            {canEditPost && (
              <div className="mt-2">
                <Button
                  type="button"
                  title="수정"
                  classtext="btn btn-outline-secondary btn-sm me-2"
                  onClick={() => setIsEditingPost(true)}
                />
                <PostDeleteButton
                  postId={post.id}
                  onDeleted={() => navigate('/qnas')}
                  className="btn btn-outline-danger btn-sm"
                 />
              </div>
            )}
          </div>
        </div>
      )}

      <div className="card mb-4">
        <div className="card-body">
          <h4 className="fw-bold mb-3 border-bottom pb-2">답변</h4>

          {isBiz && editingAnswerId == null && (
            <>
             {!showAnswerForm ? (
              <Button
                type="button"
                title="답변 등록"
                classtext="btn btn-primary mb-3"
                onClick={() => setShowAnswerForm(true)}
              />
            ) : (
                  <>
                    <AnswerRegisterForm
                      postId={id}
                      onSuccess={() => {
                        loadData();
                        setShowAnswerForm(false);
                      }}
                    />
                    <button
                      className="btn btn-outline-secondary btn-sm mt-2"
                      onClick={() => setShowAnswerForm(false)}
                    >
                      닫기
                    </button>
                  </>
                )}
              </>
            )}

            {!isBiz && (
              <p className="text-muted">※ 답변은 사업자만 작성할 수 있습니다.</p>
            )}

            <AnswerList
                postId={id}
                postUserId={post.userId}
                answers={answers}
                editingAnswerId={editingAnswerId}
                setEditingAnswerId={setEditingAnswerId}
                onAccept={handleAccept}
                onDeleted={loadData}
                user={user}
                isBiz={isBiz}
            />
        </div>
      </div>

      <Link to="/qnas" className="btn btn-link">목록으로 돌아가기</Link>
    </div>
  );
}
