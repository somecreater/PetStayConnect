import React, { useState } from 'react';

export default function StarRating({ rating = 0, onChange }) {
  const [hovered, setHovered] = useState(0);

  return (
    <div role="radiogroup" aria-label="평점">
      {[1,2,3,4,5].map(star => (
        <span
          key={star}
          role="radio"
          aria-checked={rating === star}
          onClick={() => onChange(star)}
          onMouseEnter={() => setHovered(star)}
          onMouseLeave={() => setHovered(0)}
          style={{
            cursor: 'pointer',
            fontSize: '24px',
            color: (hovered || rating) >= star ? '#FFD700' : '#CCCCCC',
            marginRight: '4px'
          }}
        >
          ★
        </span>
      ))}
    </div>
  );
}
