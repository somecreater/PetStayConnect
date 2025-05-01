import React from 'react';
import CusomP from '../../common/Ui/CusomP';
import CustomLabel from '../../common/Ui/CustomLabel';
import Button from '../../common/Ui/Button';

// 단일 반려동물 정보와 수정 버튼을 표시합니다.
function Pet({ pet, onEdit }) {
  return (
    <div className="Pet">
      
      <CustomLabel classtext="petlabel" title="ID:" />
      <CusomP classtext="petInfo" title={pet.id} />

      <CustomLabel classtext="petlabel" title="이름:" />
      <CusomP classtext="petInfo" title={pet.name} />

      <CustomLabel classtext="petlabel" title="종류:" />
      <CusomP classtext="petInfo" title={pet.species} />

      <CustomLabel classtext="petlabel" title="임신여부:" />
      <CusomP classtext="petInfo" title={pet.breed} />

      <CustomLabel classtext="petlabel" title="나이:" />
      <CusomP classtext="petInfo" title={`${pet.age}살`} />

      <CustomLabel classtext="petlabel" title="성별:" />
      <CusomP classtext="petInfo" title={pet.gender} />

      <CustomLabel classtext="petlabel" title="생년월일:" />
      <CusomP classtext="petInfo" title={pet.birthDate} />

      <CustomLabel classtext="petlabel" title="건강상 특이사항:" />
      <CusomP classtext="petInfo" title={pet.healthInfo} />
      
      <CustomLabel classtext="petlabel" title="생성일자:" />
      <CusomP classtext="petInfo" title={pet.createAt} />
      
      <CustomLabel classtext="petlabel" title="수정일자:" />
      <CusomP classtext="petInfo" title={pet.updateAt} />

      <button onClick={() => onEdit(pet)}>수정</button>
    </div>
  );
}

export default Pet;
