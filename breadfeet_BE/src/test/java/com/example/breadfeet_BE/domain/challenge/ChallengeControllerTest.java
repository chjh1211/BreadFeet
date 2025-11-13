package com.example.breadfeet_BE.domain.challenge;

import com.example.breadfeet_BE.auth.config.auth.dto.CustomOAuth2User;
import com.example.breadfeet_BE.domain.challenge.dto.MyChallengeResponseDto;
import com.example.breadfeet_BE.domain.user.User;
import com.example.breadfeet_BE.domain.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ChallengeService challengeService;

    @MockBean
    private UserRepository userRepository;

    @Test
    @DisplayName("달성한 챌린지 목록 조회 성공")
    void getMyAchievedChallenges_success() throws Exception {
        // given
        User mockUser = new User("kakao123", "testuser", "profile.jpg");
        ReflectionTestUtils.setField(mockUser, "id", 1L);

        // Manually set up security context
        CustomOAuth2User principal = new CustomOAuth2User(mockUser, Collections.emptyMap());
        Authentication auth = new UsernamePasswordAuthenticationToken(principal, "password", principal.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        Challenge mockChallenge1 = new Challenge("챌린지1", ChallengeType.REGION, "서울 강남", 1, "설명1", "/img/badges/badge1.png");
        ReflectionTestUtils.setField(mockChallenge1, "id", 101L);
        UserChallenge mockUserChallenge1 = new UserChallenge(mockUser, mockChallenge1);
        ReflectionTestUtils.setField(mockUserChallenge1, "achievedAt", LocalDateTime.of(2023, 1, 1, 10, 0));

        Challenge mockChallenge2 = new Challenge("챌린지2", ChallengeType.FREQUENCY, null, 5, "설명2", "/img/badges/badge2.png");
        ReflectionTestUtils.setField(mockChallenge2, "id", 102L);
        UserChallenge mockUserChallenge2 = new UserChallenge(mockUser, mockChallenge2);
        ReflectionTestUtils.setField(mockUserChallenge2, "achievedAt", LocalDateTime.of(2023, 2, 1, 11, 0));


        MyChallengeResponseDto dto1 = MyChallengeResponseDto.builder().userChallenge(mockUserChallenge1).build();
        MyChallengeResponseDto dto2 = MyChallengeResponseDto.builder().userChallenge(mockUserChallenge2).build();

        List<MyChallengeResponseDto> expectedDtos = List.of(dto1, dto2);

        // userRepository mock is not directly relevant for this test case as User is passed directly from principal
        // given(userRepository.findById(any(Long.class))).willReturn(Optional.of(mockUser)); // Removed this line
        given(challengeService.getMyAchievedChallenges(any(User.class))).willReturn(expectedDtos);

        // when & then
        mockMvc.perform(get("/api/challenges/my-achieved"))
                .andDo(print()) // Print the response
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].challengeId").value(101L))
                .andExpect(jsonPath("$[0].challengeName").value("챌린지1"))
                .andExpect(jsonPath("$[0].challengeType").value("REGION"))
                .andExpect(jsonPath("$[0].challengeRegion").value("서울 강남"))
                .andExpect(jsonPath("$[0].challengeThreshold").value(1))
                .andExpect(jsonPath("$[0].challengeDescription").value("설명1"))
                .andExpect(jsonPath("$[0].achievedAt").exists())
                .andExpect(jsonPath("$[1].challengeId").value(102L))
                .andExpect(jsonPath("$[1].challengeName").value("챌린지2"))
                .andExpect(jsonPath("$[1].challengeType").value("FREQUENCY"))
                .andExpect(jsonPath("$[1].challengeRegion").isEmpty())
                .andExpect(jsonPath("$[1].challengeThreshold").value(5))
                .andExpect(jsonPath("$[1].challengeDescription").value("설명2"))
                .andExpect(jsonPath("$[1].achievedAt").exists());
    }
}
