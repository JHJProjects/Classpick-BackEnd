package com.classpick.springbootproject.service;

import com.classpick.springbootproject.dao.ClassRepository;
import com.classpick.springbootproject.dao.RegisterRepository;
import com.classpick.springbootproject.dao.HistoryRepository;
import com.classpick.springbootproject.entity.OnedayClass;
import com.classpick.springbootproject.entity.Register;
import com.classpick.springbootproject.entity.History;
import com.classpick.springbootproject.responsemodels.ClassroomRegisterResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
//import 스프링 프레임워크로 해주기

@Service
@Transactional
public class ClassService {

    //클래스 저장소
    private ClassRepository classRepository;

    //대출 저장소
    private RegisterRepository registerRepository;

    //history 저장소
    private HistoryRepository historyRepository;

    //클래스 서비스를 위한 생성자
    //생성자 종속성 주입
    public ClassService(ClassRepository classRepository, RegisterRepository registerRepository,
                        HistoryRepository historyRepository){
        this.classRepository = classRepository;
        this.registerRepository = registerRepository;
        this.historyRepository = historyRepository;
    }

    //클래스 아이디를 기반으로 특정 클래스 찾기
    public OnedayClass registerClass (String userEmail, Long classId) throws Exception{

        //클래스 저장소
        Optional<OnedayClass> aClass = classRepository.findById(classId);

        //데이터 베이스 호출
        Register validateRegister = registerRepository.findByUserEmailAndClassId(userEmail, classId);

        //유효성 검사, 사용자가 같은 책을 두번 이상 체크아웃하는 것을 허용x
        //체크아웃이 null인지 확인(null과 같으면 사용자 이메일과 책 ID와 일치하는 데이터베이스에서 책을 찾았다는 의미
        //사용자가 현재 이 책을 대출하지 않았는지 확인
        if (!aClass.isPresent() || validateRegister != null || aClass.get().getClassesAvailable() <= 0) {
            throw new Exception("현재 이 책은 존재하지 않거나 대출중입니다.");
        }

        //사용 가능한 사본에서 1을 뺀 값(대출했으니까!)
        aClass.get().setClassesAvailable(aClass.get().getClassesAvailable() -1);
        //데이터베이스에 업데이트
        classRepository.save(aClass.get());

        //데이터베이스에 업데이트 한 뒤 새 체크아웃 객체 만들고
        //체크아웃 기간인 +7일 더해주기
        Register register = new Register(
                userEmail,
                LocalDate.now().toString(),
                LocalDate.now().plusDays(7).toString(),
                aClass.get().getId()
        );

        //리포지토리 확인하고 위에 만든 새 개체 저장해주기
        registerRepository.save(register);
        //저장 한 뒤 되돌려주기
        return aClass.get();
    }

    //사용자가 클래스 신청했는지 확인하는 메소드
    public Boolean isregisteredByUser(String userEmail, Long classId) {
        //유효성 검사 (위에서 했던 거)
        Register validateRegister = registerRepository.findByUserEmailAndClassId(userEmail, classId);
        //대출했으면 true 아니면 false
        if (validateRegister != null) {
            return true;
        } else {
            return false;
        }
    }

    //사용자 이메일을 매개변수로 전달하고 체크아웃 반환
    //이 목록에 얼마나 요소가 있는지 확인하기 위해 사이즈 반환
    //현재 신청클래스 수
    public int currentRegisterCount(String userEmail) {
        return registerRepository.findClassesByUserEmail(userEmail).size();
    }

    //현재 대출 리스트
    public List<ClassroomRegisterResponse> currentRegistrations(String userEmail) throws Exception {

        //기한이 지난 신청 응답의 새 목록
        List<ClassroomRegisterResponse> classroomRegisterResponse = new ArrayList<>();

        //현재 신청한 모든 클래스의 리스트
        List<Register> registerList = registerRepository.findClassesByUserEmail(userEmail);
        List<Long> classIdList = new ArrayList<>();

        //클래스 ID 목록을 가지고 대출 리스트에서 모든 클래스 ID 추출
        for (Register i: registerList) {
            classIdList.add(i.getClassId());
        }

        //클래스 ID로 책 찾기 포함하도록 클래스 저장소 업데이트
        List<OnedayClass> onedayClasses = classRepository.findClassesByClassIds(classIdList);

        //날짜를 비교해서 마감 기한까지 남은 일 수 계산하기 위해 생성
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        //클래스 목록에 있는 각 클래스 반복
        for (OnedayClass onedayClass : onedayClasses) {
            //stream.filter() 메소드는 원하는 항목만 추출해낼 때 사용
            Optional<Register> checkout = registerList.stream()
                    //매개변수는 람다 표현식으로 전달
                    //x는 가져온 classId와 같음~
                    //findFirst() 메서드는 Stream 에서 첫 번째 요소를 찾아서 Optional 타입으로 리턴
                    .filter(x -> x.getClassId() == onedayClass.getId()).findFirst();

            //신청이 존재하면
            if (checkout.isPresent()) {

                //클래스의 마감일 날짜
                Date d1 = sdf.parse(checkout.get().getReturnDate());
                //오늘 날짜를 문자열로
                Date d2 = sdf.parse(LocalDate.now().toString());

                //단위 시간은 단위 일과 같음
                TimeUnit time = TimeUnit.DAYS;

                //시간 차이
                long difference_In_Time = time.convert(d1.getTime() - d2.getTime(),
                        TimeUnit.MILLISECONDS);

                //현재 클래스 응답 강의실(내학습)에 추가
                classroomRegisterResponse.add(new ClassroomRegisterResponse(onedayClass, (int) difference_In_Time));
            }
        }
        return classroomRegisterResponse;
    }

    //클래스취소 메소드
    public void cancelClass (String userEmail, Long classId) throws Exception {

        //클래스 저장소
        Optional<OnedayClass> aClass = classRepository.findById(classId);

        //신청 확인
        Register validateRegister = registerRepository.findByUserEmailAndClassId(userEmail, classId);

        //클래스가 존재하지 않거나 신청안한 경우
        if (!aClass.isPresent() || validateRegister == null) {
            throw new Exception("책이 존재하지 않거나 사용자가 대출하지 않았습니다.");
        }

        //클래스 세트 사본, 책 두권 얻고 +1 (반납했으니까)
        aClass.get().setClassesAvailable(aClass.get().getClassesAvailable() + 1);

        //클래스 저장
        classRepository.save(aClass.get());
        //신청 저장소에서 ID 삭제
        registerRepository.deleteById(validateRegister.getId());

        //새 기록 객체를 만들고 해당 개체를 history repository에 저장
        History history = new History(
                userEmail,
                validateRegister.getCheckoutDate(),
                LocalDate.now().toString(),
                aClass.get().getTitle(),
                aClass.get().getTeacher(),
                aClass.get().getDescription(),
                aClass.get().getImg()
        );
        //새 history 개체 만들고 저장
        historyRepository.save(history);
    }

    //클래스 신청일 갱신 서비스
    public void renewRegister(String userEmail, Long classId) throws Exception {

        //클래스신청 유효성 검사
        Register validateRegister = registerRepository.findByUserEmailAndClassId(userEmail, classId);

        //만약 null이면
        if (validateRegister == null) {
            //예외 던지기
            throw new Exception("해당 클래스가 존재하지 않거나 사용자가 클래스를 신청하지 않았습니다");
        }

        //클래스 기한이 넘지 않았는지 확인하기 위해 날짜 포맷 생성
        //신청기한이 지난 클래스는 갱신 못하게 할것이기 때문
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");

        //클래스의 신청 만기일
        Date d1 = sdFormat.parse(validateRegister.getReturnDate());
        //오늘 날짜
        Date d2 = sdFormat.parse(LocalDate.now().toString());

        //오늘이 신청 만기일이거나 아직 안왔다면
        if (d1.compareTo(d2) > 0 || d1.compareTo(d2) == 0) {
            //만기일 날짜 확인 가능, 현재 날짜에 7일 더하기
            validateRegister.setReturnDate(LocalDate.now().plusDays(7).toString());
            registerRepository.save(validateRegister); //저장
        }

    }

}






















