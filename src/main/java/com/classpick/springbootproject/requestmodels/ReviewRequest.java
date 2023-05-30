package com.classpick.springbootproject.requestmodels;

import lombok.Data;

import java.util.Optional;

@Data
//@Getter, @Setter, @RequiredArgsConstructor, @ToString, @EqualsAndHashCode을 한꺼번에 설정해주는 매우 유용한 어노테이션
public class ReviewRequest {

    //개인 이중 등급
    private double rating;

    private Long classId;

    //리뷰설명의 비공개 옵션 가정(설명없이 별점만 남길 수 있음)
    //Optional로 감싸서 리뷰설명 없이 별만 남기는 경우 고려
    private Optional<String> reviewDescription;


}
