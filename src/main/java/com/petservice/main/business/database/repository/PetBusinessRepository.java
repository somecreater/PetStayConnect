package com.petservice.main.business.database.repository;


import com.petservice.main.business.database.entity.PetBusiness;
import com.petservice.main.business.database.entity.Varification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PetBusinessRepository extends JpaRepository<PetBusiness,Long> {

  PetBusiness findByUser_Id(Long id);

  /**
   * 1. 서비스에 등록된 내부 사업자 중
   *    - 사업체명(businessName) like 검색
   *    - sectorCode / typeCode = null 허용
   *    - 'is_around' true: 같은 city(시)만
   *    (주소 관련 문제 발생, 애완동물 사업자 엔티티와 주소 엔티티 연관 관계 없음)
   */

  @Query(value = """
        SELECT pb.*
          FROM pet_business pb
          LEFT JOIN pet_business_type t ON pb.business_type_id = t.id
         WHERE (:businessName IS NULL OR :businessName = '' 
           OR pb.business_name LIKE CONCAT('%', :businessName, '%'))
           AND (:sectorCode   IS NULL OR :sectorCode   = '' OR t.sector_code = :sectorCode)
           AND (:typeCode     IS NULL OR :typeCode     = '' OR t.type_code   = :typeCode)
           AND (:city         IS NULL OR :city         = '' OR pb.city         = :city)
        """,
      countQuery = """
        SELECT COUNT(*)
          FROM pet_business pb
          LEFT JOIN pet_business_type t ON pb.business_type_id = t.id
         WHERE (:businessName IS NULL OR :businessName = '' 
           OR pb.business_name LIKE CONCAT('%', :businessName, '%'))
           AND (:sectorCode   IS NULL OR :sectorCode   = '' OR t.sector_code = :sectorCode)
           AND (:typeCode     IS NULL OR :typeCode     = '' OR t.type_code   = :typeCode)
           AND (:city         IS NULL OR :city         = '' OR pb.city         = :city)
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
          LEFT JOIN pet_business_type t ON pb.business_type_id = t.id
         WHERE (:businessName IS NULL OR :businessName = '' 
           OR pb.business_name LIKE CONCAT('%', :businessName, '%'))
           AND (:sectorCode   IS NULL OR :sectorCode   = '' OR t.sector_code = :sectorCode)
           AND (:typeCode     IS NULL OR :typeCode     = '' OR t.type_code   = :typeCode)
        """,
      countQuery = """
        SELECT COUNT(*)
          FROM pet_business pb
          LEFT JOIN pet_business_type t ON pb.business_type_id = t.id
         WHERE (:businessName IS NULL OR :businessName = '' 
           OR pb.business_name LIKE CONCAT('%', :businessName, '%'))
           AND (:sectorCode   IS NULL OR :sectorCode   = '' OR t.sector_code = :sectorCode)
           AND (:typeCode     IS NULL OR :typeCode     = '' OR t.type_code   = :typeCode)
        """,
      nativeQuery = true)
  Page<PetBusiness> findServiceAll(
      String businessName,
      String sectorCode,
      String typeCode,
      Pageable pageable
  );

  boolean existsByPetBusinessType_Id(Long id);

  @Query("""
        SELECT CASE WHEN COUNT(pb) > 0 THEN TRUE ELSE FALSE END
        FROM PetBusiness pb
        WHERE pb.registrationNumber = :registrationNumber
          AND pb.varification       = :varification
    """)
  boolean existByRegistrationNumberAndVarification(
      @Param("registrationNumber") String registrationNumber,
      @Param("varification") Varification varification);

  boolean existsByRegistrationNumber(String registrationNumber);

  PetBusiness findByUser_UserLoginId(String userLoginId);

  PetBusiness findByRegistrationNumber(String registrationNumber);

  @Modifying
  @Query("UPDATE PetBusiness pb SET pb.user = null WHERE pb.user.id = :userId")
  int nullifyUserReference(@Param("userId") Long userId);
}
