package com.classpick.springbootproject.dao;

import com.classpick.springbootproject.entity.OnedayClass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;


public interface ClassRepository extends JpaRepository<OnedayClass,Long> {

    Page<OnedayClass> findByTitleContaining(@RequestParam("title") String title, Pageable pageable);

    Page<OnedayClass> findByCategory(@RequestParam("category")String category, Pageable pageable);


    //필요한 데이터만 선별적으로 추출하는 기능
    //데이터베이스에 맞는 Native SQL 사용하는 기능
    @Query("select o from OnedayClass o where id in :class_ids")
    List<OnedayClass> findClassesByClassIds (@Param("class_ids") List<Long> classId);

}