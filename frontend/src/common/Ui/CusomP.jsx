import React from "react";
import '../Css/common.css';

function CustomP(props){
    const { classtext, title }=props;
    return (
      <>
        <p className={classtext}>{title}</p>
      </>
    );
}

export default CustomP;