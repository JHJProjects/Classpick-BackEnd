package com.classpick.springbootproject.controller;

import com.classpick.springbootproject.entity.Message;
import com.classpick.springbootproject.requestmodels.AdminQuestionRequest;
import com.classpick.springbootproject.service.MessageService;
import com.classpick.springbootproject.utils.ExtractJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("http://localhost:3000")
//@Controller에 @ResponseBody가 결합된 어노테이션
// @ResponseBody 어노테이션을 붙이지 않아도 문자열과 JSON 등을 전송할 수 있다.
@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    //사용자 메세지 작성
    @PostMapping("/secure/add/message")
    public void postMessage(@RequestHeader(value = "Authorization") String token,
                            @RequestBody Message messageRequest) {
        String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");
        messageService.postMessage(messageRequest, userEmail);
    }

    //관리자 메세지(답변) 작성
    @PutMapping("/secure/admin/message")
    public void putMessage(@RequestHeader(value = "Authorization")String token,
                           @RequestBody AdminQuestionRequest adminQuestionRequest) throws Exception {
        String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");
        //사용자가 실제로 관리자인지 확인하기 위해 토큰에서 추출= 사용자 유형
        String admin = ExtractJWT.payloadJWTExtraction(token, "\"userType\"");
        //admin이 아니라면
        if (admin == null || !admin.equals("admin")) {
            throw new Exception("관리자 전용 입니다");
        }
        messageService.putMessage(adminQuestionRequest, userEmail);
    }

}
