package com.classpick.springbootproject.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "History")
//유일성이 보장된 데이터베이스 테이블을 이용
@Data
public class History {

    public History(){}

    public History(String userEmail, String checkoutDate, String returnedDate, String title,
                   String teacher, String description, String img) {
        this.userEmail = userEmail;
        this.checkoutDate = checkoutDate;
        this.returnedDate = returnedDate;
        this.title = title;
        this.teacher = teacher;
        this.description = description;
        this.img = img;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //기본 키 생성을 데이터베이스에 위임하는 방법 (데이터베이스에 의존적)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_email")
    private String userEmail;

    @Column(name = "checkout_date")
    private String checkoutDate;

    @Column(name = "returned_date")
    private String returnedDate;

    @Column(name = "title")
    private String title;

    @Column(name = "teacher")
    private String teacher;

    @Column(name = "description")
    private String description;

    @Column(name = "img")
    private String img;

}










