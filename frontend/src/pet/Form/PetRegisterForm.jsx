import React, { useState } from "react";
import ApiService from "../../common/Api/ApiService";
import TextInput from "../../common/Ui/TextInput";

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
    <form className="PetRegisterForm" onSubmit={handleSubmit}>

      <TextInput
        classtext={'petregister'}
        value={name}
        onChange={(e) => setName(e.target.value)}
        placeholderText="이름"
      />

      <TextInput
        classtext={'petregister'}
        value={species}
        onChange={(e) => setSpecies(e.target.value)}
        placeholderText="종류"
      />

      <input
        className="petregister"
        value={age}
        onChange={(e) => setAge(e.target.value)}
        placeholder="나이"
      />

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
      
      <input
        classtext={'petregister'}
        type="date"
        value={birthDate}
        onChange={(e) => setBirthDate(e.target.value)}
        placeholder="생년월일"
      />

      <TextInput
        classtext={'petregister'}
        value={breed}
        onChange={(e) => setBreed(e.target.value)}
        placeholderText="임신여부"
      />
      
      <textarea className="petlonginfo" name="description" value={healthInfo}
        placeholder="건강 정보" onChange={e => setHealthInfo(e.target.value)}/>

      
      <button type="submit">등록</button>
    </form>
  );
}

export default PetRegisterForm;
