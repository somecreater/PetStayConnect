import React, { useEffect } from "react";

function BusinessTag(props){
  
  const {id, tagType, tagName}= props;
  const badgeTypeMap = {
    PET_SPECIES: "primary",
    PET_WEIGHT: "secondary",
  };
  const labelMap = {
    PET_SPECIES: "종",
    PET_WEIGHT: "사이즈",
  };
  const badgeColor = badgeTypeMap[tagType] || "light";
  const badgeClass = `badge bg-${badgeColor} rounded-pill m-1 d-inline-flex align-items-center`;
  const label = labelMap[tagType] || "";

  return (
    <span id={id} className={badgeClass}>
      {label && <strong className="me-1">{label}:</strong>}
      {tagName}
    </span>
  );  
}

export default BusinessTag;
