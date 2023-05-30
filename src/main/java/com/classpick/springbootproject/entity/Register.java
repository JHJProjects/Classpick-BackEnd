package com.classpick.springbootproject.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "checkout")
@Data
public class Register {
        public Register() {}

        public Register(String userEmail, String checkoutDate, String returnDate, Long classId){
            this.userEmail = userEmail;
            this.checkoutDate = checkoutDate;
            this.returnDate = returnDate;
            this.classId = classId;
        }

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private Long id;

        @Column(name = "user_email")
        private String userEmail;

        @Column(name = "checkout_date")
        private String checkoutDate;

        @Column(name = "return_date")
        private String returnDate;

        @Column(name = "class_id")
        private Long classId;
}


