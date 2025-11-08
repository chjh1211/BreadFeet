package com.example.breadfeet_BE.auth.controller; // 본인 패키지명 확인

import com.example.breadfeet_BE.domain.user.User;
import com.example.breadfeet_BE.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor // UserRepository 주입을 위해
@RestController // HTML 파일 없이 문자열을 바로 반환
public class TestController {

    private final UserRepository userRepository;

    @GetMapping("/testdb") // http://localhost:8080/testdb 주소로 테스트
    public String testDbConnection() {
        try {
            // 1. DB에 저장할 테스트용 User 객체를 만듭니다.
            // (주의!) User 엔티티의 Builder 형식과 일치해야 합니다.
            User testUser = User.builder()
                    .kakaoSocialId("test_kakao_id_12345") // ⬅️ unique 해야 함
                    .nickname("테스트유저_닉네임")        // ⬅️ unique 해야 함
                    .profileImageUrl("http://test.com/image.jpg")
                    .build();

            // 2. DB에 저장 (INSERT)
            userRepository.save(testUser);

            // 3. DB에서 방금 저장한 객체를 다시 조회 (SELECT)
            User savedUser = userRepository.findByKakaoSocialId("test_kakao_id_12345")
                    .orElse(null);

            if (savedUser != null) {
                return "✅ DB 저장 및 조회 성공! 닉네임: " + savedUser.getNickname();
            } else {
                return "❌ DB 저장 실패! (저장은 됐으나 조회가 안 됨)";
            }

        } catch (Exception e) {
            // ‼️ DB 저장 중 에러 발생 (예: 닉네임 중복, 필드 null)
            e.printStackTrace(); // 콘솔에 에러 로그 출력
            return "❌ DB 저장 중 에러 발생: " + e.getMessage();
        }
    }
}