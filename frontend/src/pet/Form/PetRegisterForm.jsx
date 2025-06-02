import React, { useState } from "react";
import ApiService from "../../common/Api/ApiService";
import TextInput from "../../common/Ui/TextInput";
import Button from "../../common/Ui/Button";
import Modal from "../../common/Ui/Modal";
import BreedList from "../../common/Component/BreedList";

const style={
  Breeds:{
    padding: "1rem", 
    maxHeight: "70vh", 
  },
}

function PetRegisterForm({ onRegister, dogBreeds, catBreeds }) {

  const [name, setName] = useState("");
  const [species, setSpecies] = useState("");
  const [age, setAge] = useState(0);
  const [birthDate, setBirthDate] = useState("");
  const [breed, setBreed] = useState("");
  const [healthInfo, setHealthInfo] = useState("");
  const [gender, setGender] = useState("MAN");
  const [selectBreed,setSelectBreed]= useState("dog");
  const [selectBreedImage, setSelectBreedImage]= useState(false);
  const [breedModal,setBreedModal]= useState(false);

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
          <label>종류(선택, 이미지를 보고 선택해주세요.)</label>
          <select className="form-select"
            value={selectBreed}
            onChange={e => {
              setSelectBreed(e.target.value);
              setSpecies("");
            }}
          >
            <option value="dog">견종</option>
            <option value="cat">묘종</option>
            <option value="etc">그외</option>
          </select>
        </div>
        
        {(selectBreed === "dog" || selectBreed === "cat") && (
          <div className="mb-3 form-check">
            <input
              className="form-check-input"
              type="checkbox"
              id="includeImageCheck"
              checked={selectBreedImage}
              onChange={() => setSelectBreedImage((prev) => !prev)}
            />
            <label className="form-check-label" htmlFor="includeImageCheck">
              품종 이미지 포함하기
            </label>
          </div>
        )}

        {(selectBreed === "dog" || selectBreed === "cat") && 
        <div>
          <button
            className="btn btn-primary"
            type="button"
            onClick={()=>{setBreedModal(true)}}
          >
            현존하는 {selectBreed === "dog" ? "견종" : "묘종"} 보기
          </button>
          {breedModal &&(
            <Modal isOpen={breedModal} onClose={() => setBreedModal(false)}>
              <div style={style.Breeds}>
                <h5 className="mb-3">
                  {selectBreed === "dog" ? "견종 목록" : "묘종 목록"}
                </h5>
                <BreedList
                  breeds={selectBreed === "dog" ? dogBreeds : catBreeds}
                  isDog={selectBreed === "dog"}
                  isCat={selectBreed === "cat"}
                  isImage={selectBreedImage}
                />
              </div>
            </Modal>
          )}
        </div>
        }
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
