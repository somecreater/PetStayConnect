package com.petservice.main.business.database.repository;


import com.petservice.main.business.database.entity.PetBusiness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PetBusinessRepository extends JpaRepository<PetBusiness,Long> {

  PetBusiness findByUser_Id(Long id);

  /**
   * 1. 서비스에 등록된 내부 사업자 중
   *    - 사업체명(businessName) like 검색
   *    - sectorCode / typeCode = null 허용
   *    - 'is_around' true: 같은 city(시)만
   */
  @Query(value = """
        SELECT pb.* 
          FROM pet_business pb
          JOIN users u ON pb.user_id = u.id
          JOIN address a ON a.user_id = u.id
          JOIN pet_business_type t ON pb.business_type_id = t.id
         WHERE (:businessName IS NULL OR pb.business_name LIKE %:businessName%)
           AND (:sectorCode   IS NULL OR t.sector_code = :sectorCode)
           AND (:typeCode     IS NULL OR t.type_code   = :typeCode)
           AND (:city         IS NULL OR a.city         = :city)
        """,
      countQuery = """
        SELECT COUNT(*) 
          FROM pet_business pb
          JOIN users u ON pb.user_id = u.id
          JOIN address a ON a.user_id = u.id
          JOIN pet_business_type t ON pb.business_type_id = t.id
         WHERE (:businessName IS NULL OR pb.business_name LIKE %:businessName%)
           AND (:sectorCode   IS NULL OR t.sector_code = :sectorCode)
           AND (:typeCode     IS NULL OR t.type_code   = :typeCode)
           AND (:city         IS NULL OR a.city         = :city)
        """,
      nativeQuery = true)
  Page<PetBusiness> findServiceAndAround(
      String businessName,
      String sectorCode,
      String typeCode,
      String city,
      Pageable pageable
  );

  /**
   * 2. 서비스에 등록된 내부 사업자 중
   *    - 주변 조건 제외, 전체 조회
   */
  @Query(value = """
        SELECT pb.* 
          FROM pet_business pb
          JOIN pet_business_type t ON pb.business_type_id = t.id
         WHERE (:businessName IS NULL OR pb.business_name LIKE %:businessName%)
           AND (:sectorCode   IS NULL OR t.sector_code = :sectorCode)
           AND (:typeCode     IS NULL OR t.type_code   = :typeCode)
        """,
      countQuery = """
        SELECT COUNT(*) 
          FROM pet_business pb
          JOIN pet_business_type t ON pb.business_type_id = t.id
         WHERE (:businessName IS NULL OR pb.business_name LIKE %:businessName%)
           AND (:sectorCode   IS NULL OR t.sector_code = :sectorCode)
           AND (:typeCode     IS NULL OR t.type_code   = :typeCode)
        """,
      nativeQuery = true)
  Page<PetBusiness> findServiceAll(
      String businessName,
      String sectorCode,
      String typeCode,
      Pageable pageable
  );

  boolean existsByPetBusinessType_Id(Long id);

}
