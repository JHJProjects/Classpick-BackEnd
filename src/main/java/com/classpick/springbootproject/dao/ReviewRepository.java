package com.classpick.springbootproject.dao;

import com.classpick.springbootproject.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

public interface ReviewRepository extends JpaRepository<Review,Long> {

    //id로 책을 찾을 수 있음
    Page<Review> findByClassId(@RequestParam("class_id")Long classId, Pageable pageable);

    //사용자 이메일과 책ID로 책을 찾고 싶음
    Review findByUserEmailAndClassId(String userEmail, Long classId);

    //책 삭제할 때 리뷰도 전체 삭제
    @Modifying
    @Query("delete from Review where classId in :class_id")
    void deleteAllByClassId(@Param("class_id") Long class_id);

}