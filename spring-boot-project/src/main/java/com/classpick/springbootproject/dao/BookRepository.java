package com.classpick.springbootproject.dao;

import com.classpick.springbootproject.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book,Long> {


}
