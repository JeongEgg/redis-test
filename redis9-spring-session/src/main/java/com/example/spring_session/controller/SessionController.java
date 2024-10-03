package com.example.spring_session.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class SessionController {

    /**
     * 스프링 세션이 유지되어 있으면, 계속해서 visits 값이 증가한다.
     * 다른 포트로 웹 어플리케이션을 실행하여 접속하더라도 마찬가지이다.
     * */
    @GetMapping("/")
    public Map<String, String> home(HttpSession session){

        Integer visitCount = (Integer) session.getAttribute("visits");
        if (visitCount == null){
            visitCount = 0;
        }
        session.setAttribute("visits",++visitCount);
        return Map.of("session id", session.getId(), "visits",visitCount.toString());
    }
}
