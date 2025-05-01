import React, { useState } from "react";
import ApiService from "../../common/Api/ApiService";

function PetUpdateForm({ pet, onUpdate, onCancel }) {
  const [name, setName] = useState(pet.name);
  const [type, setType] = useState(pet.type);
  const [age, setAge] = useState(pet.age);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const dto = { name, type, age };
      await ApiService.pet.update(pet.id, dto);
      onUpdate();
    } catch (err) {
      if (err instanceof Error) {
        alert("수정 실패: " + err.message);
      } else {
        alert("수정 실패");
      }
    }
  };

  return (
    <form className="PetUpdateForm" onSubmit={handleSubmit}>
      <input
        value={name}
        onChange={(e) => setName(e.target.value)}
        placeholder="이름"
      />
      <input
        value={type}
        onChange={(e) => setType(e.target.value)}
        placeholder="종류"
      />
      <input
        value={age}
        onChange={(e) => setAge(e.target.value)}
        placeholder="나이"
      />
      <button type="submit">수정</button>
      <button type="button" onClick={onCancel}>
        취소
      </button>
    </form>
  );
}

export default PetUpdateForm;
