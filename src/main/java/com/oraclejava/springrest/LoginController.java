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

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequestDto request,
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

        session.setAttribute("LoginUser", member.getUsename());
        session.setAttribute("LoginUserId", member.getId());

        return ResponseEntity.ok(
                Map.of("username", member.getUsename())
        );
    }

    @RequestMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session){

        session.invalidate();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(HttpSession session) {
        String username =
                (String)session.getAttribute("loginUser");

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
