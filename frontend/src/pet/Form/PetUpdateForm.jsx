import React, { useState } from "react";
import ApiService from "../../common/Api/ApiService";
import TextInput from "../../common/Ui/TextInput";
import CustomLabel from "../../common/Ui/CustomLabel";
import CusomP from "../../common/Ui/CusomP";
import Button from "../../common/Ui/Button";

function PetUpdateForm({ pet, onUpdate, onCancel }) {
  const [name, setName] = useState(pet.name);
  const [species, setSpecies] = useState(pet.species);
  const [age, setAge] = useState(pet.age);
  const [birthDate, setBirthDate] = useState(pet.birthDate || "");
  const [breed, setBreed] = useState(pet.breed || "");
  const [healthInfo, setHealthInfo] = useState(pet.healthInfo || "");
  const [gender, setGender] = useState(pet.gender || "MAN");

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const dto = {
        id: pet.id,
        name,
        species,
        age: parseInt(age, 10),
        birthDate,
        breed,
        healthInfo,
        gender,
      };
      const response = await ApiService.pet.update(pet.id, dto);
      if (!response.data.result) {
        return alert("수정 실패: " + response.data.message);
      }
      alert("펫 정보가 수정되었습니다.");
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
      
      
      <CustomLabel classtext="petlabel" title="ID:" />
      <CusomP classtext="petInfo" title={pet.id} />

      <CustomLabel classtext="petlabel" title="이름:" />
      <TextInput
        classtext={'petregister'}
        value={name}
        onChange={(e) => setName(e.target.value)}
        placeholderText="이름"
      />

      <CustomLabel classtext="petlabel" title="종류:" />
      <TextInput
        classtext={'petregister'}
        value={species}
        onChange={(e) => setSpecies(e.target.value)}
        placeholderText="종류"
      />

      <CustomLabel classtext="petlabel" title="나이:" />
      <input
        className="petregister"
        value={age}
        onChange={(e) => setAge(e.target.value)}
        placeholder="나이"
      />

      <CustomLabel classtext="petlabel" title="성별:" />
      <div className='Select'>
          <label htmlFor="role">성별: </label>
          <select
            id="gender"
            name="gender"
            value={gender}
            onChange={(e) => setGender(e.target.value)}
          >
            <option value="MAN">수컷</option>
            <option value="WOMAN">암컷</option>
          </select>
      </div>

      <CustomLabel classtext="petlabel" title="생년월일:" />
      <input
        classtext={'petregister'}
        type="date"
        value={birthDate}
        onChange={(e) => setBirthDate(e.target.value)}
        placeholder="생년월일"
      />

      <CustomLabel classtext="petlabel" title="임신여부:" />
      <TextInput
        classtext={'petregister'}
        value={breed}
        onChange={(e) => setBreed(e.target.value)}
        placeholderText="임신여부"
      />

      <CustomLabel classtext="petlabel" title="건강상 특이사항:" />
      <textarea className="petlonginfo" name="description" value={healthInfo}
        placeholder="건강 정보" onChange={e => setHealthInfo(e.target.value)}/>
      
      <button type="submit">수정</button>
      <button type="button" onClick={onCancel}>
        취소
      </button>
    </form>
  );
}

export default PetUpdateForm;
