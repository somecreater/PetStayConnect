import React, {useState,useEffect} from "react";
import ApiService from "../../common/Api/ApiService";
import CusomP from "../../common/Ui/CusomP";
import Modal from "../../common/Ui/Modal";
import Button from "../../common/Ui/Button";
import BusinessManageTypeList from "../Component/BusinessTypeManageList";
import { useNavigate } from "react-router-dom";
import BusinessTypeRegisterForm from "../Form/BusinessTypeRegisterForm";

function BusinessTypeManagePage(props){
  
  const [typelist, setTypelist] = useState([]);
  const [isModalOpen,setModalOpen]=useState(false);
  const navigate=useNavigate();

  const updateTypeList = async () => {
    const response = await ApiService.businessTypeService.list();
    if(response.data.result === true){
      const listdata=response.data.typeList;
      setTypelist(listdata);
    }else{
      alert(response.data.message);
    }
  }

  useEffect(() => {
    updateTypeList();
  }, []);

  return(
    <div className="container py-4">
      <div className="d-flex justify-content-between align-items-center mb-3">
        <CusomP classtext="h5 mb-0" title="사업자 타입 리스트 (관리용)" />
        <Button
          classtext="btn btn-success"
          type="button"
          title="타입 추가"
          onClick={() => setModalOpen(true)}
        />
      </div>

      <BusinessManageTypeList types={typelist} onRefresh={updateTypeList} />

      <Modal isOpen={isModalOpen} onClose={() => setModalOpen(false)}>
        <BusinessTypeRegisterForm />
      </Modal>
    </div>
  );
}

export default BusinessTypeManagePage;
