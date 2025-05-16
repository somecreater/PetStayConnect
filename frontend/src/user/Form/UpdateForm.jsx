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
  const [types, setTypes] =useState([]);
  const [nomalUser, SetNomalUser]=useState({
    id:           user.id ?? '',
    userLoginId:  user.userLoginId ?? '',
    name:         user.name ?? '',
    email:        user.email ?? '',
    phone:        user.phone ?? '',
    qnaScore:     user.qnaScore ?? '',
    point:        user.point ?? '',
    loginType:    user.loginType ?? '',
    petNumber:    user.petNumber ?? '',
    role:         user.role ?? '',
    createAt:     user.createAt ?? '',
    updateAt:     user.updateAt ?? '',
  });

  const [bizUser,SetBizUser]=useState({
    businessName:         user.petBusinessDTO?.businessName ?? '',
    status:               user.petBusinessDTO?.status ?? 'OPERATION',
    minPrice:             user.petBusinessDTO?.minPrice ?? '',
    maxPrice:             user.petBusinessDTO?.maxPrice ?? '',
    facilities:           user.petBusinessDTO?.facilities ?? '',
    description:          user.petBusinessDTO?.description ?? '',
    avgRate:              user.petBusinessDTO?.avgRate ?? '',
    registrationNumber:   user.petBusinessDTO?.registrationNumber ?? '',
    bankAccount:          user.petBusinessDTO?.bankAccount ?? '',
    varification:         user.petBusinessDTO?.varification ?? '',
    province:             user.petBusinessDTO?.province ?? '',
    city:                 user.petBusinessDTO?.city ?? '',
    town:                 user.petBusinessDTO?.town ?? '',
    petBusinessTypeName:  user.petBusinessDTO?.petBusinessTypeName ?? '',
    petBusinessTypeId:    user.petBusinessDTO?.petBusinessTypeId ?? '',
  });

  const handleChange = (e, isUpdateBiz = false) => {
    const { name, value } = e.target;
    if(isUpdateBiz){
      SetBizUser(prev => ({ ...prev, [name]: value }))
    }else{
      SetNomalUser(prev => ({ ...prev, [name]: value }))
    }
  };

  const getBusinessType= async () => {
    try{
      const response= await ApiService.businessTypeService.list();
      const data= response.data;
      if(data.result){
        setTypes(data.typeList);
      }else{
        alert("서버상 오류로 사업자 타입을 가져오지 못했습니다. 다시 시도해주세요.");
      }
    }catch(err){
        console.log(err);
    }
  }

  const getUser = async () => {
    try{
      const response = await ApiService.userService.detail(user.userLoginId);
        
      if(response.data.auth){
        
        const dto = response.data.userDetail;

        updateUser(mapUserDto(dto));
        SetNomalUser({
          id:          dto.id ?? '',
          userLoginId: dto.userLoginId ?? '',
          name:        dto.name ?? '',
          email:       dto.email ?? '',
          phone:       dto.phone ?? '',
          qnaScore:    dto.qnaScore ?? '',
          loginType:   dto.loginType ?? '',
          petNumber:   dto.petNumber ?? '',
          role:        dto.role ?? '',
          createAt:    dto.createAt ?? '',
          updateAt:    dto.updateAt ?? '',
        });
        SetBizUser({
          businessName: dto.petBusinessDTO?.businessName ?? '',
          status:dto.petBusinessDTO?.status ?? 'OPERATION',
          registrationNumber: dto.petBusinessDTO?.registrationNumber ?? '',
          bankAccount: dto.petBusinessDTO?.bankAccount ?? '',
          minPrice: dto.petBusinessDTO?.minPrice ?? '',
          maxPrice: dto.petBusinessDTO?.maxPrice ?? '',
          province: dto.petBusinessDTO?.province ?? '',
          city: dto.petBusinessDTO?.city ?? '',
          town: dto.petBusinessDTO?.town ?? '',
          facilities: dto.petBusinessDTO?.facilities ?? '',
          description: dto.petBusinessDTO?.description ?? '',
          petBusinessTypeId: dto.petBusinessDTO?.petBusinessTypeId ?? '',
          petBusinessTypeName: dto.petBusinessDTO?.petBusinessTypeName ?? ''
        });

      }else{
        console.log('자세한 사용자 정보 없음');
      }
    }catch(err){
      console.error('자세한 사용자 정보 가져오기 오류:', err);
    }
  };

  useEffect(() => {
    getUser();
    getBusinessType();
  }, []);

  const handleUpdate= async(e) => {
    e.preventDefault();
    try{
      const payload={
        id:          nomalUser.id,
        userLoginId: nomalUser.userLoginId,
        name:        nomalUser.name,
        email:       nomalUser.email,
        phone:       nomalUser.phone,
        role:        nomalUser.role,
        petBusinessDTO:
        nomalUser.role === 'SERVICE_PROVIDER'
                ? {
                    businessName: bizUser.businessName,
                    status:       bizUser.status,
                    registrationNumber:  bizUser.registrationNumber,  
                    bankAccount:  bizUser.bankAccount,
                    minPrice:     parseInt(bizUser.minPrice, 10),
                    maxPrice:     parseInt(bizUser.maxPrice, 10),
                    province:     bizUser.province,
                    city:         bizUser.city,
                    town:         bizUser.town,
                    facilities:   bizUser.facilities,
                    description:  bizUser.description,
                    petBusinessTypeId: bizUser.petBusinessTypeId,
                    userId:       nomalUser.id,
                  }
                  : null,
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
  return(
    <div className="container py-4">
      <form onSubmit={handleUpdate} className="border p-4 rounded bg-light">
        <h5 className="mb-4">회원정보 수정</h5>

        <div className="mb-3 row">
          <label className="col-sm-3 col-form-label">아이디</label>
          <div className="col-sm-9">
            <CusomP classtext="form-control-plaintext" title={nomalUser.userLoginId} />
          </div>
        </div>

        <div className="mb-3 row">
          <label className="col-sm-3 col-form-label">역할</label>
          <div className="col-sm-9">
            <CusomP classtext="form-control-plaintext" title={nomalUser.role} />
          </div>
        </div>

        <div className="mb-3">
          <CustomLabel title="이름" for="name" />
          <TextInput
            classtext="form-control"
            id="name"
            name="name"
            value={nomalUser.name}
            onChange={handleChange}
            placeholderText="새로운 이름"
          />
        </div>

        {isNomal ? (
          <div className="mb-3">
            <CustomLabel title="이메일" for="email" />
            <TextInput
              classtext="form-control"
              id="email"
              name="email"
              value={nomalUser.email}
              onChange={handleChange}
              placeholderText="새로운 이메일"
            />
          </div>
        ) : (
          <div className="mb-3">
            <CusomP classtext="text-muted" title="구글 회원은 이메일 변경 불가" />
            <CusomP classtext="form-control-plaintext" title={nomalUser.email} />
          </div>
        )}

        <div className="mb-3">
          <CustomLabel title="전화번호" for="phone" />
          <TextInput
            classtext="form-control"
            id="phone"
            name="phone"
            value={nomalUser.phone}
            onChange={handleChange}
            placeholderText="새로운 전화번호"
          />
        </div>

        {isBiz && (
          <fieldset className="border rounded p-3 mb-4">
            <legend className="float-none w-auto px-2">사업자 정보</legend>
            
            <div className="mb-3">
              <CustomLabel title="업종" for="petBusinessTypeId" />
                <div className="mb-3">
                  <CusomP classtext="form-control-plaintext" title={`#${bizUser.petBusinessTypeId} ${bizUser.petBusinessTypeName}`} />
                </div>
                <select
                  title="변경 시 미인증 상태로 바뀝니다" 
                  id="petBusinessTypeId"
                  name="petBusinessTypeId"
                  className="form-select"
                  value={bizUser.petBusinessTypeId}
                  onChange={e => handleChange(e, true)}
                >
                  <option value="">선택하세요</option>
                  {types && types.map(type => (
                    <option key={type.id} value={type.id}>{type.typeCode}</option>
                  ))}
                </select>
            </div>

            <div className="mb-3">
              <CustomLabel title="업체명" for="businessName" />
              <TextInput
                classtext="form-control"
                id="businessName"
                name="businessName"
                value={bizUser.businessName}
                onChange={e => handleChange(e, true)}
              />
            </div>
            <div className="mb-3">
              <CustomLabel title="사업체 상태" for="status" />
                <select
                  id="status"
                  name="status"
                  className="form-select"
                  value={bizUser.status}
                  onChange={e => handleChange(e, true)}
                >
                  <option value="OPERATION">운영 중</option>
                  <option value="CLOSED">폐업</option>
              </select>
            </div>
            <div className="mb-3">
              <CustomLabel title="계좌번호" for="bankAccount" />
              <TextInput
                classtext="form-control"
                id="bankAccount"
                name="bankAccount"
                value={bizUser.bankAccount}
                onChange={e => handleChange(e, true)}
              />
            </div>

            <div className="row g-3">
              <div className="col-md-6">
                <CustomLabel title="최소 요금" for="minPrice" />
                <TextInput
                  classtext="form-control"
                  id="minPrice"
                  name="minPrice"
                  value={bizUser.minPrice}
                  onChange={e => handleChange(e, true)}
                />
              </div>
              <div className="col-md-6">
                <CustomLabel title="최대 요금" for="maxPrice" />
                <TextInput
                  classtext="form-control"
                  id="maxPrice"
                  name="maxPrice"
                  value={bizUser.maxPrice}
                  onChange={e => handleChange(e, true)}
                />
              </div>
            </div>

            <div className="row g-3 mt-3">
              {['province','city','town'].map(field => (
                <div key={field} className="col-md-4">
                  <CustomLabel title={field} for={field} />
                  <TextInput
                    classtext="form-control"
                    id={field}
                    name={field}
                    value={bizUser[field]}
                    onChange={e => handleChange(e, true)}
                  />
                </div>
              ))}
            </div>

            <div className="mb-3 mt-3">
              <CustomLabel title="시설 요약" for="facilities" />
              <TextInput
                classtext="form-control"
                id="facilities"
                name="facilities"
                value={bizUser.facilities}
                onChange={e => handleChange(e, true)}
              />
            </div>

            <div className="mb-3">
              <CustomLabel title="상세 설명" for="description" />
              <textarea
                className="form-control"
                id="description"
                name="description"
                rows={3}
                value={bizUser.description}
                onChange={e => handleChange(e, true)}
              />
            </div>
          </fieldset>
        )}

        <div className="d-grid">
          <Button
            classtext="btn btn-primary"
            type="submit"
            title="회원정보 수정"
          />
        </div>
      </form>
    </div>
  );
}

export default UpdateForm;
