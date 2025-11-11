package com.example.breadfeet_BE.domain.mypage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.breadfeet_BE.domain.user.User;
import com.example.breadfeet_BE.security.WithMockCustomUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MyPageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MyPageService myPageService;

    @Test
    @WithMockCustomUser
    @DisplayName("내 프로필 조회 성공")
    void getMyProfile_success() throws Exception {
        // given
        User testUser = User.builder()
                .nickname("test-user")
                .profileImageUrl("http://test.com/image.jpg")
                .build();
        MyPageResponseDto responseDto = new MyPageResponseDto(testUser);

        given(myPageService.getMyProfile(any())).willReturn(responseDto);

        // when & then
        mockMvc.perform(get("/api/mypage/profile"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nickname").value("test-user"))
                .andExpect(jsonPath("$.profileImageUrl").value("http://test.com/image.jpg"));
    }

    @Test
    @WithMockCustomUser
    @DisplayName("내 프로필 수정 성공")
    void updateMyProfile_success() throws Exception {
        // given
        MyProfileUpdateRequestDto requestDto = new MyProfileUpdateRequestDto();
        requestDto.setNickname("new-nickname");
        requestDto.setProfileImageUrl("http://new.com/image.jpg");

        User updatedUser = User.builder()
                .nickname("new-nickname")
                .profileImageUrl("http://new.com/image.jpg")
                .build();
        MyPageResponseDto updatedResponseDto = new MyPageResponseDto(updatedUser);

        given(myPageService.updateMyProfile(any(), any(MyProfileUpdateRequestDto.class)))
                .willReturn(updatedResponseDto);

        // when & then
        mockMvc.perform(patch("/api/mypage/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nickname").value("new-nickname"))
                .andExpect(jsonPath("$.profileImageUrl").value("http://new.com/image.jpg"));
    }

    @Test
    @WithMockCustomUser
    @DisplayName("내 리뷰 목록 조회 성공")
    void getMyReviews_success() throws Exception {
        // given
        MyReviewResponseDto review1 = MyReviewResponseDto.builder()
                .reviewId(1L)
                .content("리뷰 내용 1")
                .rating(4.0)
                .createdAt(LocalDateTime.now())
                .bakeryId(101L)
                .bakeryName("빵집 이름 1")
                .build();
        MyReviewResponseDto review2 = MyReviewResponseDto.builder()
                .reviewId(2L)
                .content("리뷰 내용 2")
                .rating(5.0)
                .createdAt(LocalDateTime.now().minusDays(1))
                .bakeryId(102L)
                .bakeryName("빵집 이름 2")
                .build();
        List<MyReviewResponseDto> myReviewResponseDtoList = List.of(review1, review2);

        given(myPageService.getMyReviews(any())).willReturn(myReviewResponseDtoList);

        // when & then
        mockMvc.perform(get("/api/mypage/reviews"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].reviewId").value(1L))
                .andExpect(jsonPath("$[0].content").value("리뷰 내용 1"))
                .andExpect(jsonPath("$[0].rating").value(4.0))
                .andExpect(jsonPath("$[0].bakeryId").value(101L))
                .andExpect(jsonPath("$[0].bakeryName").value("빵집 이름 1"))
                .andExpect(jsonPath("$[1].reviewId").value(2L))
                .andExpect(jsonPath("$[1].content").value("리뷰 내용 2"))
                .andExpect(jsonPath("$[1].rating").value(5.0))
                .andExpect(jsonPath("$[1].bakeryId").value(102L))
                .andExpect(jsonPath("$[1].bakeryName").value("빵집 이름 2"));
    }
}
