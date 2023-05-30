package com.classpick.springbootproject.dao;

import com.classpick.springbootproject.entity.Register;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RegisterRepository extends JpaRepository<Register, Long> {

    // 사용자 이메일과 책 ID를 기반으로 책이 대출되었는지 확인하는 개체생성
    Register findByUserEmailAndClassId(String userEmail, Long classId);

    //대출한 유형 리스트(사용자 이메일로 찾기)
    List<Register> findClassesByUserEmail(String userEmail);

    //책을 삭제할 때 대출 리포지토리에서도 삭제
    @Modifying
    @Query("delete from Register where classId in :class_id")
    void deleteAllByClassId(@Param("class_id") Long class_id);
}
