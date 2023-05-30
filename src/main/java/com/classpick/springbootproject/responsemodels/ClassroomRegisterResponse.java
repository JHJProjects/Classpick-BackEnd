package com.classpick.springbootproject.responsemodels;

import com.classpick.springbootproject.entity.OnedayClass;
import lombok.Data;

@Data
public class ClassroomRegisterResponse {

    public ClassroomRegisterResponse(OnedayClass onedayClass, int daysLeft) {
        this.onedayClass = onedayClass;
        this.daysLeft = daysLeft;
    }

    private OnedayClass onedayClass;

    //대출 반환까지 남은 날짜
    private int daysLeft;

}
