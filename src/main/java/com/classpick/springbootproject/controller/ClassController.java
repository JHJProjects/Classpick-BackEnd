package com.classpick.springbootproject.controller;

import com.classpick.springbootproject.entity.OnedayClass;
import com.classpick.springbootproject.responsemodels.ClassroomRegisterResponse;
import com.classpick.springbootproject.service.ClassService;
import com.classpick.springbootproject.utils.ExtractJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/onedayClasses")
public class ClassController {

    private ClassService classService;

    @Autowired
    public ClassController(ClassService classService){ this.classService = classService; }

    //공개 목록 선반 현재 신청 응답
    @GetMapping("/secure/currentloans")
    public List<ClassroomRegisterResponse> currentRegistrations(@RequestHeader(value = "Authorization") String token)
            throws Exception
    {
        String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");
        return classService.currentRegistrations(userEmail);
    }

    //현재 대출개수 (사용자 이메일을 매개변수로 잡아서 확인)
    @GetMapping("/secure/currentloans/count")
    public int currentRegisterCount(@RequestHeader(value = "Authorization") String token) {
        String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");
        return classService.currentRegisterCount(userEmail);
    }

    //클래스 신청여부 확인 메소드
    @GetMapping("/secure/ischeckedout/byuser")
    public Boolean isregisteredByUser(@RequestHeader(value = "Authorization") String token,
                                      @RequestParam Long classId) {
        //사용자 정보 대신 토큰 값 전달
        String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");
        return classService.isregisteredByUser(userEmail, classId);
    }

    //사용자가 클래스 예약했는지 알아보는 메소드
    @PutMapping("/secure/checkout")
    public OnedayClass registerClass (@RequestHeader(value = "Authorization") String token,
                                      @RequestParam Long classId) throws Exception {
        String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");
        return classService.registerClass(userEmail, classId);
    }

    //클래스취소 서비스
    @PutMapping("/secure/return")
    public void cancelClass(@RequestHeader(value = "Authorization") String token,
                            @RequestParam Long classId) throws Exception {
        String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");
        classService.cancelClass(userEmail, classId);
    }

    //클래스신청 갱신 서비스
    @PutMapping("/secure/renew/loan")
    public void renewRegister (@RequestHeader(value = "Authorization") String token,
                               @RequestParam Long classId) throws Exception {
        String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");
        classService.renewRegister(userEmail, classId);
    }



}
