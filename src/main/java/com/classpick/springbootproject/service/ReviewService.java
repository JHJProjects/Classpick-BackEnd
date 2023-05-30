package com.classpick.springbootproject.service;

import com.classpick.springbootproject.dao.ReviewRepository;
import com.classpick.springbootproject.entity.Review;
import com.classpick.springbootproject.requestmodels.ReviewRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;

@Service
@Transactional
public class ReviewService {

    private ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    //리뷰 검토 기능
    public void postReview(String userEmail, ReviewRequest reviewRequest) throws Exception {

        //validateReview는 사용자 이메일, 책 ID로 리뷰 저장소를 검토하는 것과 같다
        Review validateReview = reviewRepository.findByUserEmailAndClassId(userEmail, reviewRequest.getClassId());
        //리뷰 검토가 null이 아닌 경우 이미 리뷰 작성이 완료되었다는 예외 던지기(리뷰가 이미 존재)
        if(validateReview != null) {
            throw new Exception("리뷰를 이미 작성하셨습니다.");
        }

        //null이 아닌 경우 새 리뷰이기에 새로운 검토 대상 => 리뷰는 새로운 리뷰로 다시 받아와야함
        Review review = new Review();

        //다시 값 가져오기
        review.setClassId(reviewRequest.getClassId());
        review.setRating(reviewRequest.getRating());
        review.setUserEmail(userEmail);

        //리뷰 설명이 있는지 확인하기
        if(reviewRequest.getReviewDescription().isPresent()) {
            //맵(Map)의 가장 큰 특징이라면 key로 value를 얻어낸다는 점이다.
            //Optional은 문자열을 복사하기 때문에 map을 이용해서 선택적으로 Optional에서 데이터를 가져오기
            //리뷰 설명 가져오기
            review.setReviewDescription(reviewRequest.getReviewDescription().map(
                    //문자열로 변경
                    Object::toString
                    //또는 null
            ).orElse(null));
        }
        //검토 날짜
        review.setDate(Date.valueOf(LocalDate.now()));
        //리포지토리 저장을 검토하고 검토 통과
        reviewRepository.save(review);
    }

    //사용자 리뷰 나열
    public Boolean userReviewListen(String userEmail, Long classId) {
        Review validateReview = reviewRepository.findByUserEmailAndClassId(userEmail, classId);
        //검증 검토가 null이 아닌경우
        if (validateReview != null) {
            return true;
        } else {
            return false;
        }
    }

}
