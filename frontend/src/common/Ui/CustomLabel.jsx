import React from 'react';
import '../Css/common.css';

function CustomLabel(props){
  const { classtext, title, fortext } = props;

  return (
    <>
      <label className={classtext} for={fortext}>{title}</label>
    </>
  );
}

export default CustomLabel;
