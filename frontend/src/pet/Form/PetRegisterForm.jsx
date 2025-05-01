import React, { useState } from "react";
import ApiService from "../../common/Api/ApiService";

function PetRegisterForm({ onRegister }) {
  const [name, setName] = useState("");
  const [type, setType] = useState("");
  const [age, setAge] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const dto = { name, type, age };
      await ApiService.pet.register(dto);
      onRegister();
    } catch (err) {
      if (err instanceof Error) {
        alert("등록 실패: " + err.message);
      } else {
        alert("등록 실패");
      }
    }
  };

  return (
    <form className="PetRegisterForm" onSubmit={handleSubmit}>
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
      <button type="submit">등록</button>
    </form>
  );
}

export default PetRegisterForm;
