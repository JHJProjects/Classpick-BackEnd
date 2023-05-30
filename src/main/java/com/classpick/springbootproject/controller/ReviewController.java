package com.classpick.springbootproject.controller;

import com.classpick.springbootproject.requestmodels.ReviewRequest;
import com.classpick.springbootproject.service.ReviewService;
import com.classpick.springbootproject.utils.ExtractJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private ReviewService reviewService;

    //생성자 만들깅
    @Autowired
    public ReviewController (ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/secure/user/onedayClasses")
    public Boolean reviewClassByUser(@RequestHeader(value = "Authorization") String token,
                                    @RequestParam Long classId) throws Exception {
        String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");

        //사용자 이메일이 아닐경우
        if (userEmail == null) {
            throw new Exception("사용자 이메일이 누락되었습니다");
        }
        return reviewService.userReviewListen(userEmail, classId);
    }


    //리뷰 작성
    @PostMapping("/secure")
    public void postReview(@RequestHeader(value = "Authorization") String token,
                           @RequestBody ReviewRequest reviewRequest) throws Exception {

        //사용자 이메일이 우리가 추출한 sub(email)와 같다
        String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");
        //만약 사용자 이메일이 null이면 이메일이 누락되었다는 새로운 예외 던지기
        if(userEmail == null ) {
            throw new Exception("사용자 이메일이 존재하지 않습니다");
        }
        //아닌 경우 방금 작성한 서비스 호출
        reviewService.postReview(userEmail, reviewRequest);


    }



}
