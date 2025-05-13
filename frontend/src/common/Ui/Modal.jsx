import React from 'react';
import ReactDOM from 'react-dom';
import '../Css/common.css';

function Modal(props){
    
  const {isOpen, onClose, children, maxWidth = '90vw', maxHeight = '90vh'} = props;
  if (!isOpen) return null;

  return ReactDOM.createPortal(
    <>
      {/* Backdrop */}
      <div
        className="modal-backdrop fade show"
        style={{ zIndex: 1050 }}
        onClick={onClose}
      />

      {/* Modal dialog */}
      <div
        className="modal fade show d-block"
        tabIndex="-1"
        role="dialog"
        style={{ zIndex: 1060 }}
      >
        <div
          className="modal-dialog modal-dialog-centered"
          role="document"
          style={{
            // 화면을 넘지 않는 최대 크기 설정
            maxWidth,
            margin: '0 auto',
          }}
        >
          <div
            className="modal-content"
            style={{
              display: 'flex',
              flexDirection: 'column',
              maxHeight,
            }}
          >
            <div className="modal-header">
              <button
                type="button"
                className="btn-close"
                aria-label="Close"
                onClick={onClose}
              />
            </div>
            <div
              className="modal-body"
              style={{
                // 본문만 스크롤 가능하도록
                overflowY: 'auto',
                padding: '1rem',
              }}
            >
              {children}
            </div>
          </div>
        </div>
      </div>
    </>,
    document.getElementById('modal-root')
  );
}

export default Modal;