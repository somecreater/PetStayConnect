import React, { useState, useEffect } from 'react';
import { useParams, Link, useNavigate } from 'react-router-dom';
import ApiService from '../../common/Api/ApiService';
import { useUser } from '../../common/Context/UserContext';
import Button from '../../common/Ui/Button';
import PostDeleteButton from '../component/PostDeleteButton';
import PostUpdateForm from '../form/PostUpdateForm';
import AnswerList from '../component/AnswerList';
import AnswerRegisterForm from '../form/AnswerRegisterForm';

export default function QnaDetailPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const { user } = useUser();

  const [post, setPost] = useState(null);
  const [answers, setAnswers] = useState([]);
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
      <main className="QnaDetailPage">
        {isEditingPost ? (
          <PostUpdateForm
            initialData={post}
            onSuccess={() => { setIsEditingPost(false); loadData(); }}
          />
        ) : (
          <article>
            <h2>#{post.id} {post.title}</h2>
            <p>{post.content}</p>
            <p>{new Date(post.createdAt).toLocaleString()}</p>
            {canEditPost && (
              <div>
                <Button title="Edit" onClick={() => setIsEditingPost(true)} />
                <PostDeleteButton postId={post.id} onDeleted={() => navigate('/qnas')} />
              </div>
            )}
          </article>
        )}

        <hr />

        <section>
          <h3>Answers</h3>
          {isBiz && editingAnswerId == null && (
            <AnswerRegisterForm postId={id} onSuccess={loadData} />
          )}
          <AnswerList
              postId={id}
              answers={answers}
              editingAnswerId={editingAnswerId}
              setEditingAnswerId={setEditingAnswerId}
              onAccept={handleAccept}
              onDeleted={loadData}
              user={user}
              isBiz={isBiz}
            />
          </section>

          <Link to="/qnas">Back to list</Link>
        </main>
      );
}
