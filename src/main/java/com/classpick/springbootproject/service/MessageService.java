package com.classpick.springbootproject.service;

import com.classpick.springbootproject.dao.MessageRepository;
import com.classpick.springbootproject.entity.Message;
import com.classpick.springbootproject.requestmodels.AdminQuestionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class MessageService {

    //메세지 저장소 생성
    private MessageRepository messageRepository;

    //메세지 저장소 연결
    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    //메세지 요청과 이메일 수신
    public void postMessage(Message messageRequest, String userEmail) {
        //새 메세지 생성, 사용자 이메일,제목,질문을 db에 저장
        Message message = new Message(messageRequest.getTitle(), messageRequest.getQuestion());
        message.setUserEmail(userEmail);
        messageRepository.save(message);
    }

    //현재 메세지를 업데이트 할 수 있도록
    public void putMessage(AdminQuestionRequest adminQuestionRequest, String userEmail) throws Exception {
        //메세지 저장소 (아이디로 찾기)
        Optional<Message> message = messageRepository.findById(adminQuestionRequest.getId());

        //메세지가 없는 경우
        if (!message.isPresent()) {
            throw new Exception("메세지를 찾을 수 없습니다.");
        }

        //메세지를 받고 관리자 이메일을 이 사용자의 이메일로 설정
        message.get().setAdminEmail(userEmail);
        message.get().setResponse(adminQuestionRequest.getResponse());
        //메세지가 닫히고 메세지 repository에 저장
        message.get().setClosed(true);
        messageRepository.save(message.get());
    }

}
