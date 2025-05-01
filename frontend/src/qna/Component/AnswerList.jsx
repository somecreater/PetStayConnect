// src/qna/component/AnswerList.jsx
import React from 'react';
import Answer from './Answer';
import AnswerDeleteButton from './AnswerDeleteButton';

export default function AnswerList({ postId, answers, onDeleted }) {
  return (
    <div>
      {answers.map(ans => (
        <div key={ans.id}>
          <Answer answer={ans} />
          <AnswerDeleteButton
            postId={postId}
            answerId={ans.id}
            onDeleted={onDeleted}
          />
        </div>
      ))}
    </div>
  );
}
