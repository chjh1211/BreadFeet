package com.breadfeet.login.controller;

import com.breadfeet.login.dto.KakaoUserInfoResponseDto;
import com.breadfeet.login.service.KakaoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("")
public class KakaoLoginController {

    private final KakaoService kakaoService;

    @GetMapping("/callback")
    public String callback(@RequestParam("code") String code) {
        String accessToken = kakaoService.getAccessTokenFromKakao(code);

        KakaoUserInfoResponseDto userInfo = kakaoService.getUserInfo(accessToken);

        // User 로그인, 또는 회원가입 로직 추가
        log.info("✅ 로그인 완료: {} ({})",
                userInfo.getKakaoAccount().getProfile().getNickName(),
                userInfo.getKakaoAccount().getEmail());

        // 로그인 성공 시 페이지로 리다이렉트
        return "redirect:/login/success";
    }
}
