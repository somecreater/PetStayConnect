import React from 'react'
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
  return (
    <div
      className="list-group overflow-auto"
      style={{ maxHeight: '70vh' }}
    >
      {answers.map(ans => {
        const isQuestionOwner =
          user?.id === postUserId ||
          user?.petBusinessDTO?.id === postBusinessId
        const isAnswerAuthor =
          user?.id === ans.userId ||
          user?.petBusinessDTO?.id === ans.businessId
        const isEditing = editingAnswerId === ans.id

        return (
          <div key={ans.id} className="list-group-item">
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
                <p className="mb-1">{ans.content}</p>
                <small className="text-muted">
                  {new Date(ans.createdAt).toLocaleString()}
                </small>
              </>
            )}

            <div className="mt-2">
              {isBiz && isQuestionOwner && !isAnswerAuthor && !ans.accepted && (
                <button
                  className="btn btn-success btn-sm me-2"
                  onClick={() => onAccept(ans.id)}>
                  채택
                </button>
              )}
              {!isEditing && user?.id === ans.userId && (
                <button
                  className="btn btn-outline-secondary btn-sm me-2"
                  onClick={() => setEditingAnswerId(ans.id)}>
                  수정
                </button>
              )}
              {user?.id === ans.userId && (
                <AnswerDeleteButton
                  postId={postId}
                  answerId={ans.id}
                  onDeleted={onDeleted}
                />
              )}
            </div>
          </div>
        );
      })}
    </div>
  );
}

