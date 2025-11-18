package com.example.breadfeet_BE.domain.challenge;

import com.example.breadfeet_BE.auth.config.auth.dto.CustomOAuth2User;
import com.example.breadfeet_BE.domain.challenge.dto.AllMyChallengesResponseDto;
import com.example.breadfeet_BE.domain.challenge.dto.ChallengeInfoResponseDto;
import com.example.breadfeet_BE.domain.challenge.dto.OngoingChallengeInfoResponseDto;
import com.example.breadfeet_BE.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ChallengeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChallengeService challengeService;

    private User mockUser;

    @BeforeEach
    void setUp() {
        // given
        mockUser = new User("kakao123", "testuser", "profile.jpg");
        ReflectionTestUtils.setField(mockUser, "id", 1L);

        // Manually set up security context
        CustomOAuth2User principal = new CustomOAuth2User(mockUser, Collections.emptyMap());
        Authentication auth = new UsernamePasswordAuthenticationToken(principal, "password", principal.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    @DisplayName("내 챌린지 목록 조회 (추천, 진행중, 완료 포함) 성공")
    void getMyChallenges_success() throws Exception {
        // given
        System.out.println(">> 테스트 시작: 내 챌린지 목록 조회");

        // 1. Mock 데이터 생성
        Challenge recommendedChallenge = new Challenge("추천 챌린지", ChallengeType.REGION, "서울 전체", 10, "서울의 빵집 10곳 방문", "/img/badges/badge_rec.png");
        Challenge ongoingChallenge = new Challenge("진행중 챌린지", ChallengeType.FREQUENCY, null, 5, "일주일에 빵 5번 먹기", "/img/badges/badge_on.png");
        Challenge completedChallenge = new Challenge("완료된 챌린지", ChallengeType.REGION, "부산 해운대구", 3, "해운대 빵집 3곳 방문", "/img/badges/badge_com.png");

        ChallengeInfoResponseDto recommendedDto = new ChallengeInfoResponseDto(recommendedChallenge);
        OngoingChallengeInfoResponseDto ongoingDto = new OngoingChallengeInfoResponseDto(ongoingChallenge, 2); // progress=2
        ChallengeInfoResponseDto completedDto = new ChallengeInfoResponseDto(completedChallenge);

        AllMyChallengesResponseDto responseDto = new AllMyChallengesResponseDto(
                List.of(recommendedDto),
                List.of(ongoingDto),
                List.of(completedDto)
        );
        System.out.println(">> Mock Service 응답 데이터 준비 완료");
        System.out.println("   - 추천: " + recommendedDto.getTitle());
        System.out.println("   - 진행중: " + ongoingDto.getTitle() + " (진행도: " + ongoingDto.getProgress() + "/" + ongoingDto.getTotal() + ")");
        System.out.println("   - 완료: " + completedDto.getTitle());


        // 2. Mock Service 설정
        given(challengeService.getAllMyChallenges(any(User.class))).willReturn(responseDto);

        // when & then
        System.out.println(">> /api/challenges/my API 호출 및 검증 시작");
        mockMvc.perform(get("/api/challenges/my"))
                .andDo(print()) // 요청/응답 전체 내용 출력
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // 추천 챌린지 검증
                .andExpect(jsonPath("$.recommended.length()").value(1))
                .andExpect(jsonPath("$.recommended[0].title").value("추천 챌린지"))
                .andExpect(jsonPath("$.recommended[0].body").value("서울의 빵집 10곳 방문"))
                // 진행중 챌린지 검증
                .andExpect(jsonPath("$.ongoing.length()").value(1))
                .andExpect(jsonPath("$.ongoing[0].title").value("진행중 챌린지"))
                .andExpect(jsonPath("$.ongoing[0].progress").value(2))
                .andExpect(jsonPath("$.ongoing[0].total").value(5))
                // 완료된 챌린지 검증
                .andExpect(jsonPath("$.completed.length()").value(1))
                .andExpect(jsonPath("$.completed[0].title").value("완료된 챌린지"));

        System.out.println(">> 테스트 성공: 모든 검증 완료");
    }
}
