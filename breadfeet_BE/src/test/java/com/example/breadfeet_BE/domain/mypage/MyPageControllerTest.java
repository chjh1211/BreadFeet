package com.example.breadfeet_BE.domain.mypage;

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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MyPageControllerTest {

    @Autowired
    private MockMvc mockMvc;

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
}
