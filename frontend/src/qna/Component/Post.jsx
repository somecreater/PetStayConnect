import React from 'react';
import { Link } from 'react-router-dom';

export default function Post({ post }) {
  return (
    <div>
      <Link to={`/qnas/${post.id}`}>#{post.id} {post.title}</Link>
      <div>조회수: {post.viewCount}</div>
      <div>작성일: {new Date(post.createdAt).toLocaleString()}</div>
    </div>
  );
}
