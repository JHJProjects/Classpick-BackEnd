package com.classpick.springbootproject.dao;

import com.classpick.springbootproject.entity.History;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RequestParam;

public interface HistoryRepository extends JpaRepository<History, Long> {

    //history 저장소
    Page<History> findBooksByUserEmail(@RequestParam("email") String userEmail, Pageable pageable);

}
