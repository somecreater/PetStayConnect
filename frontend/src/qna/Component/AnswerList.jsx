import React from 'react'
import AnswerUpdateForm from '../form/AnswerUpdateForm'
import AnswerDeleteButton from '../component/AnswerDeleteButton'

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
    <div className="AnswerList">
      {answers.map(ans => {
        const isQuestionOwner =
          user?.id === postUserId ||
          user?.petBusinessDTO?.id === postBusinessId
        const isAnswerAuthor =
          user?.id === ans.userId ||
          user?.petBusinessDTO?.id === ans.businessId
        const isEditing = editingAnswerId === ans.id

        return (
          <div key={ans.id} className="AnswerCard">
            {isEditing ? (
              <AnswerUpdateForm
                postId={postId}
                answer={ans}
                onSuccess={() => {
                    setEditingAnswerId(null)
                    onDeleted()
                }}
              />
            ) : (
              <>
                <p className="AnswerContent">{ans.content}</p>
                <p className="AnswerMeta">
                  {new Date(ans.createdAt).toLocaleString()}
                </p>
              </>
            )}
            <div className="AnswerActions">
              {isBiz && isQuestionOwner && !isAnswerAuthor && !ans.accepted && (
                <button
                  className="AcceptButton"
                  onClick={() => onAccept(ans.id)}
                >
                  채택
                </button>
              )}
              {!isEditing && user?.id === ans.userId && (
                <button
                  className="EditButton"
                  onClick={() => setEditingAnswerId(ans.id)}
                >
                  수정
                </button>
              )}
              {user?.id === ans.userId && (
                <AnswerDeleteButton postId={postId} answerId={ans.id} onDeleted={onDeleted} />
              )}
            </div>
          </div>
        )
      })}
    </div>
  )
}

