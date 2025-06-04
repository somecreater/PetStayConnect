import React from "react";
import BusinessTag from "./BusinessTag";

function BusinessTagList(props){
  const {tagList, onSelect, isDelete, onDelete} = props;

  const handleClick= (tag) => {
    if (onSelect) {
      onSelect(tag);
    }
  };

  return (
    <div className="d-flex flex-wrap">
      {tagList && tagList.length > 0 ? (
        tagList.map((tag) => (
          <div 
            key={tag.id}
            className="d-flex align-items-center"
            onClick={()=>handleClick(tag)}
          >
            <BusinessTag
              id={tag.id}
              tagType={tag.tagType}
              tagName={tag.tagName}
            />
            {isDelete && (
              <span
                className="text-danger ms-1"
                style={{ cursor: "pointer", fontSize: "0.9rem", lineHeight: 1 }}
                onClick={(e) => {
                  onDelete(tag.id);
                }}
                title="삭제"
              >
                &times;
              </span>
            )}
          </div>
        ))
      ) : (
        <p className="text-muted">태그가 없습니다.</p>
      )}
    </div>
  );
}

export default BusinessTagList;