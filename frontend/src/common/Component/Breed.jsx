import React from 'react';

const style={
  item:{
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
    maxWidth: 300,
    margin: "16px auto",
    fontFamily: "sans-serif",
  },
  title:{ 
    marginBottom: 8, 
    textAlign: "center" 
  },
  image:{
    width: "100%",
    height: "auto",
    borderRadius: 8,
    boxShadow: "0 2px 6px rgba(0, 0, 0, 0.2)",
  },
  imageAlert:{
    color: "#777", 
    fontSize: 14, 
    textAlign: "center",
  }
}

function Breed(props){
  const {isImage, title, isDog, isCat} =props;
  const normalizeTitle = (s) => {
    if (typeof s !== "string" || s.trim() === "") {
      return "";
    }
    return s.trim().replace(/\s+/g, "_");
  };
  const folder = isDog ? "dog_images" : isCat ? "cat_images" : null;
  const baseName = normalizeTitle(title);
  const fileName = baseName ? baseName + ".jpg" : "";
  const imagePath = 
    folder && fileName ? `/assets/images/${folder}/${fileName}` : null;
  
  return (
    <>
      <div style={style.item}>
        <h3 style={style.title}>
          {title || "알 수 없는 품종"}
        </h3>
        {isImage && folder && fileName ? (
          <img
            src={imagePath}
            alt={`${folder} - ${title}`}
            style={style.image}
            onError={(e) => {
              e.target.onerror = null;
              e.target.src = ""; 
              e.target.alt = "이미지를 찾을 수 없습니다.";
            }}
          />
        ):
        (
          <p style={style.imageAlert}>
            { !folder 
            ? "유효한 동물 타입(cat/dog)이 아닙니다."
            : !fileName
            ? "유효한 품종명이 아닙니다."
            : "이미지 표시가 비활성화되어 있습니다." 
            }
          </p>
        )
        }
      </div>
    </>
  );  
}

export default Breed;