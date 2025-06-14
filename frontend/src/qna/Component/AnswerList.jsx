import React, { useState } from 'react';
import AnswerUpdateForm from '../form/AnswerUpdateForm'
import AnswerDeleteButton from '../Component/AnswerDeleteButton'

export default function AnswerList({
  postId,
  postUserId,
  postBusinessId,
  answers,
  editingAnswerId,
  setEditingAnswerId,
  onAccept,
  onDeleted,
  user,
  isBiz,
}) {

  const [expandedIds, setExpandedIds] = useState({});
  const toggleExpand = id => {
    setExpandedIds(prev => ({ ...prev, [id]: !prev[id] }));
  };
  return (
    <div
      className="list-group overflow-auto"
      style={{ maxHeight: '70vh' }}
    >
      {answers.map(ans => {
        const isQuestionOwner = user?.id === postUserId;
        const isAnswerAuthor = user?.id === ans.userId;
        const isAccepted = Boolean(ans.accepted);
        const isEditing = editingAnswerId === ans.id;

        const [expanded, setExpanded] = useState(false);
        const maxLength = 80;
        const shouldTruncate = ans.content.length > maxLength;
        const visibleContent = expanded
          ? ans.content
          : ans.content.slice(0, maxLength) + (shouldTruncate ? '...' : '');

        return (
          <div key={ans.id} className="card mb-2 shadow-sm">
            <div className="card-body py-2 px-3">
                  {/* 채택된 경우 배지 추가 */}
                  <div className="d-flex justify-content-between align-items-center mb-2">
                      <span className="fw-semibold text-primary">
                          {ans.userLoginId}
                      </span>
                    <div className="d-flex align-items-center gap-2">
                      {ans.accepted  && (
                        <span className="badge bg-success">채택된 답변</span>
                      )}
                      <small className="text-muted">
                        {new Date(ans.createdAt).toLocaleString()}
                      </small>
                    </div>

                    {/* 버튼들 우측 정렬 */}
                    <div className="btn-group btn-group-sm">
                      {isQuestionOwner && !isAnswerAuthor && !isAccepted && (
                        <button
                          className="btn btn-success"
                          onClick={() => onAccept(ans.id)}
                        >
                          채택
                        </button>
                      )}

                      {!isEditing && isAnswerAuthor && (
                        <button
                          className="btn btn-outline-secondary"
                          onClick={() => setEditingAnswerId(ans.id)}
                        >
                          수정
                        </button>
                      )}

                      {isAnswerAuthor && (
                        <AnswerDeleteButton
                          postId={postId}
                          answerId={ans.id}
                          onDeleted={onDeleted}
                        />
                      )}
                    </div>
                  </div>

                  {/* 내용 or 수정폼 */}
                  {isEditing ? (
                    <AnswerUpdateForm
                      postId={postId}
                      answer={ans}
                      onSuccess={() => {
                        setEditingAnswerId(null);
                        onDeleted();
                      }}
                    />
                  ) : (
                   <>
                    <p className="mb-1">{visibleContent}</p>
                    {shouldTruncate && (
                      <button
                       className="btn btn-sm btn-link p-0"
                       onClick={() => setExpanded(prev => !prev)}
                     >
                       {expanded ? '간략히' : '더보기'}
                     </button>
                     )}
                   </>
                 )}
               </div>
             </div>
           );
         })}
       </div>
     );
   }
