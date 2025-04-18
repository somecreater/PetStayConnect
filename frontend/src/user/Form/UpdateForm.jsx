import React, {useEffect, useState} from "react";
import { useNavigate } from 'react-router-dom';
import { API_ENDPOINTS, createHeaders   } from '../../common/Api/Api';
import RefreshApi from '../../common/Api/RefreshApi';
import TextInput from '../../common/Ui/TextInput';
import Button from '../../common/Ui/Button';
import CustomP from '../../common/Ui/CusomP';
import { useUser } from '../../common/Context/UserContext';
import '../../common/Css/common.css';

/* 
유저 정보의 추후 수정 가능(일단 간단한 정보 수정만 제공)
유저의 닉네임과 이메일만 수정 가능하게 변경(일반회원인 경우만, 구글 회원이면 변경 불가능하게 설정)
나중에 유저 변수, getUser 인라인 함수, 폼을 수정 하면된다.
*/
function UpdateForm(props){
  const navigate=useNavigate();
  const { user: contextUser,resetUser,updateUser }= useUser();

  const  [user, setUser] = useState({
    userLoginId: '',
    name:'',
    email:'',
    role: ''
  });

  const getUser = async () => {
    try{
      const response = await RefreshApi.get(API_ENDPOINTS.auth.detail, {
        params: { userLoginId: contextUser.userLoginId },
        headers: createHeaders(),
      });
        
      if(response.data.auth){
        
        const userData = response.data.userDetail;

        setUser({
          userLoginId: userData.userLoginId,
          name: userData.name,
          email: userData.email,
          role: userData.role,
        });

      }else{
        console.log('자세한 사용자 정보 없음');
      }
    }catch(err){
      console.error('자세한 사용자 정보 가져오기 오류:', err);
    }
  };

  useEffect(() => {
    if (contextUser?.userLoginId) {
        getUser();
      }
  }, [contextUser]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setUser((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleUpdate= async(e) => {
    e.preventDefault();
    try{

      const response = await RefreshApi.put(API_ENDPOINTS.auth.update, user, {
        headers: createHeaders(),
      });

      if(response.data.result){
        alert('회원 정보가 성공적으로 수정되었습니다.');
        updateUser({
          name: response.data.updateUser.name,
          email: response.data.updateUser.email,
          userLoginId: response.data.updateUser.userLoginId,
          role: response.data.updateUser.role, 
        });

        navigate('/user/info');
      }else{
        alert('회원 정보 수정에 실패했습니다.');
      }

    } catch(err) {
      console.error('업데이트 실패:', err);
      alert('회원 정보 수정 도중 오류가 발생하였습니다.');
    }
  }

  return (
    <>
      <form className="UserUpdateForm" onSubmit={handleUpdate}>
        
        <label className="UpdateLabel" for="UserLoginId">로그인 아이디: </label>
        <CustomP
          classtext="UserLoginId"
          title={user.userLoginId}
        />
        <label className="UpdateLabel" for="UserRole">역할: </label>
        <CustomP
          classtext="UserRole"
          title={user.role}
        />
        <TextInput 
          classtext="NameInput"
          name="name" 
          value={user.name} 
          placeholderText="새로운 이름을 입력하세요" 
          onChange={handleChange}
        />
        <TextInput 
          classtext="EmailInput"
          name="email"
          value={user.email} 
          placeholderText="새로운 이메일을 입력하세요"
          onChange={handleChange}
        />
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