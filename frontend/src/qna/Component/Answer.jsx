import React from 'react';

export default function Answer({ answer }) {
  return (
    <div>
      <div>#{answer.id} {answer.isAdopted ? '(채택됨)' : ''}</div>
      <div>{answer.content}</div>
      <div>작성일: {new Date(answer.createdAt).toLocaleString()}</div>
    </div>
  );
}
