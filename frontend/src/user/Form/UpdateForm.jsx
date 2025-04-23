import React, {useEffect, useState} from "react";
import { useNavigate } from 'react-router-dom';
import ApiService from '../../common/Api/ApiService';
import TextInput from '../../common/Ui/TextInput';
import Button from '../../common/Ui/Button';
import CusomP from '../../common/Ui/CusomP';
import CustomLabel from '../../common/Ui/CustomLabel';
import { useUser } from '../../common/Context/UserContext';
import '../../common/Css/common.css';

/* 
유저 정보의 추후 수정 가능(일단 간단한 정보 수정만 제공)
유저의 닉네임과 이메일만 수정 가능하게 변경(일반회원인 경우만, 구글 회원이면 변경 불가능하게 설정)
나중에 유저 변수, getUser 인라인 함수, 폼을 수정 하면된다.
*/
function UpdateForm(props){
  
  const navigate=useNavigate();
  const { user,mapUserDto,updateUser }= useUser();

  const [nomalUser, SetNomalUser]=useState({
    userLoginId: user.userLoginId,
    name: user.name,
    email: user.email,
    phone: user.phone,
    qnaScore: user.qnaScore,
    loginType: user.loginType,
    petNumber: user.petNumber,
    role: user.role,
    createAt: user.createAt,
    updateAt: user.updateAt,
  });

  const [bizUser,SetBizUser]=useState({
    businessName:user.petBusinessDTO.businessName,
    status:user.petBusinessDTO.status,
    minPrice:user.petBusinessDTO.minPrice,
    maxPrice:user.petBusinessDTO.maxPrice,
    facilities:user.petBusinessDTO.facilities,
    description:user.petBusinessDTO.description,
    avgRate: user.petBusinessDTO.avgRate,
    registrationNumber: user.petBusinessDTO.registrationNumber,
    bankAccount: user.petBusinessDTO.bankAccount,
    varification: user.petBusinessDTO.varification,
    petBusinessTypeName: user.petBusinessDTO.petBusinessTypeName,
    petBusinessTypeId: user.petBusinessDTO.petBusinessTypeId,
  });

  const handleChange = (e, isUpdateBiz = false) => {
    const { name, value } = e.target;
    if(isUpdateBiz){
      SetBizUser(prev => ({ ...prev, [name]: value }))
    }else{
      SetNomalUser(prev => ({ ...prev, [name]: value }))
    }
  };

  const getUser = async () => {
    try{
      const response = await ApiService.userService.detail(user.userLoginId);
        
      if(response.data.auth){
        
        const dto = response.data.userDetail;

        updateUser(mapUserDto(dto));
        SetNomalUser({
          userLoginId: dto.userLoginId,
          name:        dto.name,
          email:       dto.email,
          phone:       dto.phone,
          qnaScore:    dto.qnaScore,
          loginType:   dto.loginType,
          petNumber:   dto.petNumber,
          role:        dto.role,
          createAt:    dto.createAt,
          updateAt:    dto.updateAt,
        });
        SetBizUser(dto.petBusinessDTO || {});

      }else{
        console.log('자세한 사용자 정보 없음');
      }
    }catch(err){
      console.error('자세한 사용자 정보 가져오기 오류:', err);
    }
  };

  useEffect(() => {
        getUser();
  }, []);

  const handleUpdate= async(e) => {
    e.preventDefault();
    try{
      const payload={
        ...nomalUser,
        petBusinessDTO:
        nomalUser.role === 'SERVICE_PROVIDER' ? { ...bizUser, userId: nomalUser.userLoginId } : null,
        petDTOList       : [],
        bookmarkDTOList  : [],
        qnaPostDTOList   : [],
        qnaAnswerDTOList : [],
      };

      const response = await ApiService.userService.update(payload);

      if(response.data.result){
        alert('회원 정보가 성공적으로 수정되었습니다.');
        const updateinfo=response.data.updateUser;
        updateUser(mapUserDto(updateinfo));
        navigate('/user/info');
      }else{
        alert('회원 정보 수정에 실패했습니다.');
      }

    } catch(err) {
      console.error('업데이트 실패:', err);
      alert('회원 정보 수정 도중 오류가 발생하였습니다.');
    }
  }


  const isBiz = user.role === 'SERVICE_PROVIDER';
  const isNomal = user.loginType === 'NORMAL';
  return (
    <>
      <form className="UserUpdateForm" onSubmit={handleUpdate}>
        
        <CustomLabel classtetxt={'UserInfolabel'} title={'로그인 아이디:'} for={'UserUpdateInfo'}/>
        <CusomP
          classtext="UserUpdateInfo"
          title={nomalUser.userLoginId}
        />

        <CustomLabel classtetxt={'UserInfolabel'} title={'역할:'} for={'UserUpdateInfo'}/>
        <CusomP
          classtext="UserUpdateInfo"
          title={nomalUser.role}
        />

        <TextInput 
          classtext="UserUpdateInput"
          name="name" 
          value={nomalUser.name} 
          placeholderText="새로운 이름을 입력하세요" 
          onChange={handleChange}
        />
        {
          isNomal ?(
          <TextInput 
          classtext="UserUpdateInput"
          name="email"
          value={nomalUser.email} 
          placeholderText="새로운 이메일을 입력하세요"
          onChange={handleChange}
          />
        ):(
          <>
          <CusomP classtext={'UserInfo'} title={"구글로 가입된 회원은 이메일 변경이 불가능 합니다."}/>
          <CusomP classtext={'UserInfo'} title={nomalUser.email}/>
          </>
        )
        }

        <TextInput 
          classtext="UserUpdateInput"
          name="phone" 
          value={nomalUser.phone} 
          placeholderText="새로운 전화번호를 입력하세요" 
          onChange={handleChange}
        />

        {
          isBiz && (
            <fieldset className="BusinessUpdateInfo">
              <legend>새로운 사업자 정보</legend>
              <TextInput classtext="BusinessUpdateInput" name="businessName" value={bizUser.businessName}
                placeholderText="업체명" onChange={e => handleChange(e, true)} />
              <TextInput classtext="BusinessUpdateInput" name="bankAccount" value={bizUser.bankAccount}
                placeholderText="정산 계좌" onChange={e => handleChange(e, true)} />
              <TextInput classtext="BusinessUpdateInput" name="minPrice" value={bizUser.minPrice}
                placeholderText="최소 요금" onChange={e => handleChange(e, true)} />
              <TextInput classtext="BusinessUpdateInput" name="maxPrice" value={bizUser.maxPrice}
                placeholderText="최대 요금" onChange={e => handleChange(e, true)} />
              <TextInput classtext="BusinessUpdateInput" name="facilities" value={bizUser.facilities}
                placeholderText="시설 요약(쉼표 구분)" onChange={e => handleChange(e, true)} />
            <textarea className="BusinessLongUpdateInput" name="description" value={bizUser.description}
              placeholder="상세 설명" onChange={e => handleChange(e, true)} />
            </fieldset>
          )
        }
        <Button
          classtext="UserUpdateButton"
          type="submit" 
          title="회원정보 수정" 
        />
      </form>
    </>
  );
}

export default UpdateForm;
