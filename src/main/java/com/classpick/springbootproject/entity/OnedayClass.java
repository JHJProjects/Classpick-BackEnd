package com.classpick.springbootproject.entity;

import lombok.Data;
import javax.persistence.*;

@Entity
@Table(name = "onedayclass")
@Data
public class OnedayClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "teacher")
    private String teacher;

    @Column(name = "description")
    private String description;

    @Column(name = "classes")
    private int classes;

    @Column(name = "classes_available")
    private int classesAvailable;

    @Column(name = "category")
    private String category;

    @Column(name = "img")
    private String img;
}