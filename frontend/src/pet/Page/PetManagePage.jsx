import React, { useEffect, useState, useCallback } from 'react';
import ApiService from '../../common/Api/ApiService';
import PetList from '../Component/PetList';
import PetRegisterForm from '../Form/PetRegisterForm';
import PetUpdateForm from '../Form/PetUpdateForm';
import { useUser } from '../../common/Context/UserContext';

function PetManagePage() {
  const { user } = useUser();
  const [pets, setPets] = useState([]);
  const [editingPet, setEditingPet] = useState(null);

  const fetchPets = useCallback(async () => {
    if (!user) return;
    const res = await ApiService.pet.userpet(user.loginId);
    setPets(res.data);
  }, [user]);

  useEffect(() => {
    fetchPets();
  }, [fetchPets]);

  // 이하 동일
  const handleRegister = () => fetchPets();
  const handleEdit = (pet) => setEditingPet(pet);
  const handleUpdate = () => {
    setEditingPet(null);
    fetchPets();
  };
  const handleDelete = async (petId) => {
    if (window.confirm('정말 삭제하시겠습니까?')) {
      await ApiService.pet.delete(petId);
      fetchPets();
    }
  };

  return (
    <div className="PetManagePage">
      <h2>내 반려동물 관리</h2>
      {editingPet ? (
        <PetUpdateForm pet={editingPet} onUpdate={handleUpdate} onCancel={() => setEditingPet(null)} />
      ) : (
        <PetRegisterForm onRegister={handleRegister} />
      )}
      <PetList pets={pets} onEdit={handleEdit} onDelete={handleDelete} />
    </div>
  );
}

export default PetManagePage;
