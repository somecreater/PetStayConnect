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
    <div className="container py-4">
      <form onSubmit={handleSubmit} className="border p-4 rounded bg-light">
        <h5 className="mb-3">펫 정보 수정</h5>

        <div className="mb-3 row">
          <label className="col-sm-3 col-form-label">ID</label>
          <div className="col-sm-9">
            <CusomP classtext="form-control-plaintext" title={pet.id} />
          </div>
        </div>

        <div className="mb-3">
          <label className="col-sm-3 col-form-label">이름</label>
          <TextInput
            classtext="form-control"
            id="name"
            value={name}
            onChange={e => setName(e.target.value)}
            placeholderText="이름"
          />
        </div>

        <div className="mb-3">
          <label className="col-sm-3 col-form-label">종류</label>
          <TextInput
            classtext="form-control"
            id="species"
            value={species}
            onChange={e => setSpecies(e.target.value)}
            placeholderText="종류"
          />
        </div>

        <div className="mb-3">
          <label className="col-sm-3 col-form-label">나이</label>
          <input
            type="number"
            className="form-control"
            value={age}
            onChange={e => setAge(e.target.value)}
          />
        </div>

        <div className="mb-3">
          <label className="col-sm-3 col-form-label">성별</label>
          <select
            className="form-select"
            value={gender}
            onChange={e => setGender(e.target.value)}
          >
            <option value="MAN">수컷</option>
            <option value="WOMAN">암컷</option>
          </select>
        </div>

        <div className="mb-3">
          <label className="col-sm-3 col-form-label" for="birthDate">생년월일:</label>
          <input
            type="date"
            className="form-control"
            value={birthDate}
            onChange={e => setBirthDate(e.target.value)}
          />
        </div>

        <div className="mb-3">
          <label className="col-sm-3 col-form-label" for="breed">임신여부</label>
          <TextInput
            classtext="form-control"
            value={breed}
            onChange={e => setBreed(e.target.value)}
            placeholderText="임신여부"
          />
        </div>

        <div className="mb-4">
          <label className="col-sm-3 col-form-label" for="healthInfo">건강 정보</label>
          <textarea
            className="form-control"
            rows={3}
            value={healthInfo}
            onChange={e => setHealthInfo(e.target.value)}
            placeholder="건강 정보"
          />
        </div>

        <div className="d-flex gap-2">
          <Button
            classtext="btn btn-primary"
            type="submit"
            title="수정"
          />
          <Button
            classtext="btn btn-secondary"
            type="button"
            title="취소"
            onClick={onCancel}
          />
        </div>
      </form>
    </div>
  );
}

export default PetUpdateForm;
