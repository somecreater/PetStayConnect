import React, { useState, useEffect } from 'react';
import { useParams, Link }            from 'react-router-dom';
import ApiService                     from '../../common/Api/ApiService';
import AnswerList                     from '../component/AnswerList';
import AnswerRegisterForm             from '../form/AnswerRegisterForm';
import AnswerUpdateForm               from '../form/AnswerUpdateForm';
import PostDeleteButton               from '../component/PostDeleteButton';

export default function QnaDetailPage() {
  const { id }           = useParams();
  const navigate = useNavigate();
  const [post, setPost]  = useState(null);
  const [editing, setEditing] = useState({});

  const loadPost = () =>
    ApiService.qnas.post.detail(id)
    .then(res => setPost(res.data))
    .catch(() => navigate('/qnas'));

   useEffect(() => {
    loadPost();
  }, [id]);

  if (!post) return null;

  return (
      <div>
        <h2>#{post.id} {post.title}</h2>
        <p>{post.content}</p>
        <PostDeleteButton
          postId={id}
          onDeleted={() => navigate('/qnas')}
        />

        <h3>답변 ({post.qnaAnswerDTOList.length})</h3>
        {post.qnaAnswerDTOList.map(ans => (
          <div key={ans.id}>
            {editing[ans.id] ? (
              <AnswerUpdateForm
                postId={id}
                answer={ans}
                onSuccess={() => {
                  loadPost();
                  setEditing(prev => ({ ...prev, [ans.id]: false }));
                }}
              />
            ) : (
              <>
                <AnswerList answers={[ans]} />
                <button onClick={() => setEditing(prev => ({ ...prev, [ans.id]: true }))}>
                  수정
                </button>
              </>
            )}
          </div>
        ))}

        <AnswerRegisterForm
          postId={id}
          onSuccess={loadPost}
        />

        <Link to="/qnas">목록으로</Link>
      </div>
    );
  }