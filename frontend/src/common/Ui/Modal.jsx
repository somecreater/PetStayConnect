// src/common/Ui/Modal.jsx
import React from 'react';
import ReactDOM from 'react-dom';
import '../Css/common.css';

function Modal(props) {
  const {
    isOpen,
    onClose,
    children,
    footer,
    maxWidth = '90vw',
    maxHeight = '90vh',
  } = props;
  if (!isOpen) return null;

  return ReactDOM.createPortal(
    <>
      {/* Backdrop */}
      <div
        className="modal-backdrop"
        onClick={onClose}
      />

      {/* Modal dialog */}
      <div
        className="modal d-block"
        style={{
          zIndex: 1060,
          padding: '1rem',
          overflow: 'hidden',
        }}
      >
        <div
          className="modal-dialog"
          role="document"
          style={{
            maxWidth,
            maxHeight,
            margin: '0 auto',
          }}
        >
          <div className="modal-content">
            {/* Header */}
            <div className="modal-header">
              <button
                type="button"
                className="btn-close"
                aria-label="Close"
                onClick={onClose}
              />
            </div>

            {/* Body */}
            <div className="modal-body">
              {children}
            </div>

            {/* Footer (선택적) */}
            {footer && (
              <div className="modal-footer">
                {footer}
              </div>
            )}
          </div>
        </div>
      </div>
    </>,
    document.getElementById('modal-root')
  );
}

export default Modal;
