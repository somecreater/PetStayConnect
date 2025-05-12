// src/review/Page/ReviewPage.jsx
import React from 'react';
import { Outlet } from 'react-router-dom';

export default function ReviewPage() {
  return (
    <main className="container py-4">
      <Outlet />
    </main>
  );
}
