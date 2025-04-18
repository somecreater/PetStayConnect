import React, { useState } from 'react';
import '../Css/common.css'
function PasswordInput(props){
    const { classtext, name, value, placeholderText, onChange} = props;

    return (
        <input className={classtext}
            type="password"
            name={name}
            value={value}
            placeholder={placeholderText}
            onChange={onChange}
        />
    );
}

export default PasswordInput;
