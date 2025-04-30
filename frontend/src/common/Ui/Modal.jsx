import React from 'react';
import ReactDOM from 'react-dom';
import '../Css/common.css';

function Modal(props){
    
  const {isOpen, onClose, children} = props;
  if (!isOpen) return null;

  return(
    <>
      <div className="modal-backdrop" onClick={onClose} />
      <div className="modal-content">
        <button className="modal-close" onClick={onClose}>×</button>
        {children}
      </div>
    </>,
    document.getElementById('modal-root') 
  );
}

export default Modal;