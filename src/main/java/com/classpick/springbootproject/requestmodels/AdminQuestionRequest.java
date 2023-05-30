package com.classpick.springbootproject.requestmodels;

import lombok.Data;

@Data
public class AdminQuestionRequest {

    private Long id;

    //관리자의 응답
    private String response;
}
