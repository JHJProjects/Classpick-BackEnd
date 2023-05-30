package com.classpick.springbootproject.controller;

import com.classpick.springbootproject.requestmodels.AddClassRequest;
import com.classpick.springbootproject.service.AdminService;
import com.classpick.springbootproject.utils.ExtractJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin("http://localhost:3000")
@RequestMapping("/api/admin")
public class AdminController {

    private AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    //책 수량 증가
    @PutMapping("/secure/increase/onedayClasses/quantity")
    public void increaseClassQuantity(@RequestHeader(value = "Authorization") String token,
                                     @RequestParam Long classId) throws Exception {
        String admin = ExtractJWT.payloadJWTExtraction(token, "\"userType\"");
        if (admin == null || !admin.equals("admin")) {
            throw new Exception("관리자 전용 페이지");
        }
        adminService.increaseClassQuantity(classId);
    }

    //책 수량 감소
    @PutMapping("/secure/decrease/onedayClasses/quantity")
    public void decreaseClassQuantity(@RequestHeader(value = "Authorization") String token,
                                     @RequestParam Long classId) throws Exception {
        String admin = ExtractJWT.payloadJWTExtraction(token, "\"userType\"");
        if (admin == null || !admin.equals("admin")) {
            throw new Exception("관리자 전용 페이지");
        }
        adminService.decreaseClassQuantity(classId);
    }

    @PostMapping("/add/oneday")
    public void postClass2() throws Exception {
        System.out.println("oneday admin call");
    }

    //책 등록 메소드
    @PostMapping("/secure/add/onedayClasses")
    public void postClass(@RequestHeader(value = "Authorization") String token,
                         @RequestBody AddClassRequest addClassRequest) throws Exception {
        System.out.println("secure add oneday");
        String admin = ExtractJWT.payloadJWTExtraction(token, "\"userType\"");
        if (admin == null || !admin.equals("admin")) {
            throw new Exception("관리자 전용 페이지");
        }
        adminService.postClass(addClassRequest);
    }
    
    //책 삭제 메소드
    @DeleteMapping("/secure/delete/onedayClasses")
    public void deleteClass(@RequestHeader(value = "Authorization") String token,
                           @RequestParam Long classId) throws Exception {
        String admin = ExtractJWT.payloadJWTExtraction(token, "\"userType\"");
        if (admin == null || !admin.equals("admin")) {
            throw new Exception("관리자 전용 페이지");
        }
        adminService.deleteClass(classId);
    }
}
