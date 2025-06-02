import React from "react";
import Breed from "./Breed"; 

const style={
  container:{
    display: "grid",
    gridTemplateColumns: "repeat(auto-fill, minmax(220px, 1fr))",
    gap: "16px",
    padding: "16px",
    height: "600px",
    overflowY: "auto",
    boxSizing: "border-box"
  },
}

function BreedList( {breeds = [], isDog, isCat, isImage = true }){
  
  return (
    <>
      <div style={style.container}>
        {isDog && 
          breeds.map((breed)=>(
          <Breed 
            key={breed.id || breed.name} 
            isImage={isImage}
            title={breed.name}
            isDog={true}
            isCat={false}
          />
        ))}
        {isCat && 
          breeds.map((breed)=>(
          <Breed 
            key={breed.id || breed.name} 
            isImage={isImage}
            title={breed.name}
            isDog={false}
            isCat={true}
          /> 
        ))}
      </div>
    </>
  );
}

export default BreedList;