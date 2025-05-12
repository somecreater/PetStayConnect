import React, { useEffect, useState, useCallback } from 'react';
import ApiService from '../../common/Api/ApiService';
import PetList from '../Component/PetList';
import PetRegisterForm from '../Form/PetRegisterForm';
import PetUpdateForm from '../Form/PetUpdateForm';
import { useUser } from '../../common/Context/UserContext';
import { useNavigate } from 'react-router-dom';
import Button from '../../common/Ui/Button';
import Modal from '../../common/Ui/Modal';

function PetManagePage() {
  const { user } = useUser();
  const [pets, setPets] = useState([]);
  const [editingPet, setEditingPet] = useState(null);
  const [isModalOpen,setModalOpen]=useState(false);
  const navigate=useNavigate();

  const fetchPets = useCallback(async () => {

    const res = await ApiService.pet.userpet();
    if (res.data.result) {
      setPets(res.data.pets || []);
    } else {
      alert('펫 목록을 불러오지 못했습니다: ' + res.data.message);
      setPets([]);
    }

  }, [user]);

  useEffect(() => {
    fetchPets();
  }, [fetchPets]);

  const handleRegister = () => fetchPets();
  const handleEdit = (pet) => {
    setEditingPet(pet);
    setModalOpen(true);
  };
  const handleUpdate = () => {
    setEditingPet(null);
    setModalOpen(false);
    fetchPets();
  };
  const handleDelete = async (petId) => {
    if (window.confirm('정말 삭제하시겠습니까?(복구 불가)(일부 기록이 삭제될수 있습니다)')) {
      const response=await ApiService.pet.delete(petId);
      if(!response.data.result){
        alert(response.data.message);
      }else{
        alert(response.data.message);
      }
      fetchPets();
    }
  };

  return (
    <div className="container py-4">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h2 className="mb-0">내 반려동물 관리</h2>
        <Button
          classtext="btn btn-outline-primary"
          type="button"
          title="마이 페이지로 이동"
          onClick={() => navigate('/user/info')}
        />
      </div>

      <div className="mb-4">
        <PetRegisterForm onRegister={handleRegister} />
      </div>

      <Modal isOpen={isModalOpen} onClose={() => setModalOpen(false)}>
        {editingPet && (
          <PetUpdateForm
            pet={editingPet}
            onUpdate={handleUpdate}
            onCancel={() => {
              setEditingPet(null);
              setModalOpen(false);
            }}
          />
        )}
      </Modal>

      <PetList
        pets={pets}
        onEdit={handleEdit}
        isEdit={true}
        onDelete={handleDelete}
        isDelete={true}
      />
    </div>
  );
}

export default PetManagePage;
