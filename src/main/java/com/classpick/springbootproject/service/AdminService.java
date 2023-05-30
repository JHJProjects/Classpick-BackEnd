package com.classpick.springbootproject.service;

import com.classpick.springbootproject.dao.ClassRepository;
import com.classpick.springbootproject.dao.RegisterRepository;
import com.classpick.springbootproject.dao.ReviewRepository;
import com.classpick.springbootproject.entity.OnedayClass;
import com.classpick.springbootproject.requestmodels.AddClassRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class AdminService {

    private ClassRepository classRepository;
    private ReviewRepository reviewRepository;
    private RegisterRepository registerRepository;

    @Autowired
    public AdminService (ClassRepository classRepository,
                         ReviewRepository reviewRepository,
                         RegisterRepository registerRepository) {
        this.classRepository = classRepository;
        this.reviewRepository = reviewRepository;
        this.registerRepository = registerRepository;
    }

    //책 수량 증가
    public void increaseClassQuantity(Long classId) throws Exception {

        Optional<OnedayClass> aClass = classRepository.findById(classId);

        if (!aClass.isPresent()) {
            throw new Exception("클래스를 찾을 수 없습니다.");
        }
        aClass.get().setClassesAvailable(aClass.get().getClassesAvailable() + 1);
        aClass.get().setClasses(aClass.get().getClasses() + 1);

        classRepository.save(aClass.get());
    }

    //책 수량 감소
    public void decreaseClassQuantity(Long classId) throws Exception {

        Optional<OnedayClass> aClass= classRepository.findById(classId);

        if (!aClass.isPresent() || aClass.get().getClassesAvailable() <= 0 || aClass.get().getClasses() <= 0) {
            throw new Exception("클래스를 찾을 수 없거나 수강 가능한 날짜가 없습니다. ");
        }

        aClass.get().setClassesAvailable(aClass.get().getClassesAvailable() -1);
        aClass.get().setClasses(aClass.get().getClasses() -1);

        classRepository.save(aClass.get());
    }

    //관리자가 새 책 등록
    public void postClass (AddClassRequest addClassRequest) {
        OnedayClass onedayClass = new OnedayClass();
        onedayClass.setTitle(addClassRequest.getTitle());
        onedayClass.setTeacher(addClassRequest.getTeacher());
        onedayClass.setDescription(addClassRequest.getDescription());
        onedayClass.setClassesAvailable(addClassRequest.getClasses());
        onedayClass.setCategory(addClassRequest.getCategory());
        onedayClass.setImg(addClassRequest.getImg());
        classRepository.save(onedayClass);
    }

    //관리자가 책 삭제
    public void deleteClass(Long classId) throws Exception{
        Optional<OnedayClass> aClass = classRepository.findById(classId);

        if (!aClass.isPresent()) {
            throw new Exception("클래스을 찾을 수 없습니다.");
        }

        classRepository.delete(aClass.get());
        registerRepository.deleteAllByClassId(classId);
        reviewRepository.deleteAllByClassId(classId);
    }
}
