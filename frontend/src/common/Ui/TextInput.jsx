import React from 'react';
import '../Css/common.css'
function TextInput(props){
    const { classtext, name, value, placeholderText, onChange} = props;

    return (
        <input className={classtext}
            type="text"
            name={name}
            value={value}
            placeholder={placeholderText}
            onChange={onChange}
        />
    );
}

export default TextInput;
