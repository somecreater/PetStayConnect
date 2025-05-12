import React, { useState } from "react";
import ApiService from "../../common/Api/ApiService";
import TextInput from "../../common/Ui/TextInput";
import Button from "../../common/Ui/Button";
function PetRegisterForm({ onRegister }) {

  const [name, setName] = useState("");
  const [species, setSpecies] = useState("");
  const [age, setAge] = useState(0);
  const [birthDate, setBirthDate] = useState("");
  const [breed, setBreed] = useState("");
  const [healthInfo, setHealthInfo] = useState("");
  const [gender, setGender] = useState("MAN");


  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const dto = { 
        id: null, 
        name:name, 
        species:species, 
        breed:breed,
        birthDate:birthDate,
        age:age,
        healthInfo:healthInfo,
        gender:gender,
        createAt:null,
        updateAt:null,
        userId:null,
        userLoginId:null
      };
      const response = await ApiService.pet.register(dto);
      
      onRegister();
      setName("");
      setSpecies("");
      setAge("");
      setBirthDate("");
      setBreed("");
      setHealthInfo("");
      setGender("MAN");
      if(response.data.result){
        alert('애완동물 한마리 등록 완료!!');
      }else{
        alert(response.data.message);
      }
    } catch (err) {
      if (err instanceof Error) {
        alert("등록 실패: " + err.message);
      } else {
        alert("등록 실패");
      }
    }
  };

  return (
    <div className="container py-4">
      <form onSubmit={handleSubmit} className="border p-4 rounded bg-light">
        <h5 className="mb-4">펫 정보 등록</h5>

        <div className="mb-3">
          <TextInput
            classtext="form-control"
            value={name}
            placeholderText="이름"
            onChange={e => setName(e.target.value)}
          />
        </div>

        <div className="mb-3">
          <TextInput
            classtext="form-control"
            value={species}
            placeholderText="종류"
            onChange={e => setSpecies(e.target.value)}
          />
        </div>

        <div className="mb-3">
          <input
            type="number"
            className="form-control"
            value={age}
            placeholder="나이"
            onChange={e => setAge(e.target.value)}
          />
        </div>

        <div className="mb-3">
          <label className="form-label">성별</label>
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
          <input
            type="date"
            className="form-control"
            value={birthDate}
            onChange={e => setBirthDate(e.target.value)}
          />
        </div>

        <div className="mb-3">
          <TextInput
            classtext="form-control"
            value={breed}
            placeholderText="임신여부"
            onChange={e => setBreed(e.target.value)}
          />
        </div>

        <div className="mb-4">
          <textarea
            className="form-control"
            rows={3}
            placeholder="건강 정보"
            value={healthInfo}
            onChange={e => setHealthInfo(e.target.value)}
          />
        </div>

        <Button classtext="btn btn-success w-100" type="submit" title="등록" />
      </form>
    </div>
  );
}

export default PetRegisterForm;
