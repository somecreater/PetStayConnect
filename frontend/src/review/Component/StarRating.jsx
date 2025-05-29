import React, { useState } from 'react';

export default function StarRating({ rating = 0, onChange, readOnly = false }) {
  const [hovered, setHovered] = useState(0);
  const interactive = !readOnly && typeof onChange === 'function';

  return (
    <div role="radiogroup" aria-label="평점">
      {[1,2,3,4,5].map(star => {
          const isActive = interactive
            ? (hovered || rating) >= star
            : rating >= star;
          const eventProps = interactive
            ? {
                onClick: () => onChange(star),
                onMouseEnter: () => setHovered(star),
                onMouseLeave: () => setHovered(0),
              }
            : {};

        return (
        <span
          key={star}
          role="radio"
          aria-checked={rating === star}
          {...eventProps}
          style={{
            cursor: interactive ? 'pointer' : 'default',
            fontSize: '24px',
            color: isActive ? '#FFD700' : '#CCCCCC',
            marginRight: '4px',
          }}
        >
          ★
        </span>
        );
      })}
    </div>
  );
}
