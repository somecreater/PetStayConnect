import React, { useState } from 'react';
import '../Css/common.css';

function Button(props){
  const { classtext, type, title, onClick }=props;
  
  return(
    <>
      <button className={classtext} type={type} onClick={onClick}> 
        {title}
      </button>
    </>
  );
}

export default Button;