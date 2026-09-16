package com.oraclejava.springrest;

import com.oraclejava.springrest.dtos.LoginRequestDto;
import com.oraclejava.springrest.models.Member;
import com.oraclejava.springrest.repositories.MemberRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class LoginController {

    @Autowired
    private MemberRepository memberRepository;

    //로그인 경로에서 값을 넣어서 POST
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody
            //로그인요청 DTO에서의 객체("request")
            LoginRequestDto request,
            //HttpSession의 세션객체("session") 를 매개변수로 설정
            HttpSession session
    ) {
        Member member = memberRepository
                .findByUsername(request.username())
                .orElse(null);
        if (member == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("아이디가 없습니다.");
        }
        // 실제로는 평문으로 비교보다는 SHA-256, BCrypt등 해시 비교 권장
        if (!member.getPassword().equals(request.password())) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("비밀번호가 일치하지 않습니다");
        }

        //세션에 memberRepository의 getUsername()과 getId()를 넣는다.
        session.setAttribute("LoginUser", member.getUsername());
        session.setAttribute("LoginUserId", member.getId());

        return ResponseEntity.ok(
                Map.of("username", member.getUsername())
        );
    }

    //현재 미구현상태
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session){
        //세션을 무효화시킴.
        session.invalidate();
        return ResponseEntity.noContent().build();
    }

    //현재 미구현상태
    @GetMapping("/me")
    public ResponseEntity<?> me(HttpSession session) {
        String username =
                (String)session.getAttribute("LoginUser");

        if (username == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .build();
        }

        return ResponseEntity.ok(
                Map.of("username", username)
        );
    }
}