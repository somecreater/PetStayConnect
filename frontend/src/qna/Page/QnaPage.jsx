import React from 'react';
import { Outlet } from 'react-router-dom';

export default function QnaPage() {
  return (
    <main className="QnaPage">
      <Outlet />
    </main>
  );
}
