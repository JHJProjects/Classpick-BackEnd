package com.classpick.springbootproject.requestmodels;

import lombok.Data;

@Data
public class AddClassRequest {

    private String title;

    private String teacher;

    private String description;

    private int classes;

    private String category;

    private String img;

}
