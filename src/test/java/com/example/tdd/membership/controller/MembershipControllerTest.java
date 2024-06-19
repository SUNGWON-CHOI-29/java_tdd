package com.example.tdd.membership.controller;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.tdd.common.GlobalExceptionHandler;
import com.example.tdd.common.constant.MembershipErrorResult;
import com.example.tdd.common.constant.MembershipType;
import com.example.tdd.exception.MembershipException;
import com.example.tdd.membership.dto.MembershipDetailResponse;
import com.example.tdd.membership.dto.MembershipRequest;
import com.example.tdd.membership.dto.MembershipAddResponse;
import com.example.tdd.membership.entity.Membership;
import com.example.tdd.membership.service.MembershipService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import static com.example.tdd.common.constant.MembershipConstatns.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class MembershipControllerTest {

    @InjectMocks
    private MembershipController target;

    @Mock
    private MembershipService membershipService;

    private MockMvc mockMvc;
    private Gson gson;

    @BeforeEach
    public void init() {
        gson = new Gson();
        mockMvc = MockMvcBuilders.standaloneSetup(target)
            .setControllerAdvice(new GlobalExceptionHandler())
            .build();
    }

    // @Test
    // public void mockMvc가Null이아님() {
    //     assertThat(target).isNotNull();
    //     assertThat(mockMvc).isNotNull();
    // }

    @Test
    public void 멤버십등록실패_사용자식별값이헤더에없음() throws Exception {

        //given
        final String url = "/api/v1/memberships";

        //when
        final ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.post(url)
                .content(gson.toJson(membershipRequest(10000, MembershipType.NAVER)))
                .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions.andExpect(status().isBadRequest());
    }

    private MembershipRequest membershipRequest(final Integer point, final MembershipType membershipType) {
        return MembershipRequest.builder()
            .point(point)
            .membershipType(membershipType)
            .build();
    }

    @ParameterizedTest
    @MethodSource("invalidMembershipAddParameter")
    public void 멤버십등록실패_잘못된파라미터(final Integer point, final MembershipType membershipType) throws Exception {

        // given
        final String url = "/api/v1/memberships";

        // when
        final ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.post(url)
                .header(USER_ID_HEADER, "12345")
                .content(gson.toJson(membershipRequest(point, membershipType)))
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    private static Stream<Arguments> invalidMembershipAddParameter() {
        return Stream.of(
            Arguments.of(null, MembershipType.NAVER),
            Arguments.of(-1000, MembershipType.NAVER),
            Arguments.of(10000, null)

        );
    }
    // @Test
    // public void 멤버십등록실패_포인트가null() throws Exception {
    //
    //     // given
    //     final String url = "/api/v1/memberships";
    //
    //     // when
    //     final ResultActions resultActions = mockMvc.perform(
    //         MockMvcRequestBuilders.post(url)
    //             .header(USER_ID_HEADER, "12345")
    //             .content(gson.toJson(membershipRequest(null, MembershipType.NAVER)))
    //             .contentType(MediaType.APPLICATION_JSON)
    //     );
    //
    //     // then
    //     resultActions.andExpect(status().isBadRequest());
    // }
    //
    // @Test
    // public void 멤버십등록실패_포인트가음수() throws Exception {
    //
    //     // given
    //     final String url = "/api/v1/memberships";
    //
    //     // when
    //     final ResultActions resultActions = mockMvc.perform(
    //         MockMvcRequestBuilders.post(url)
    //             .header(USER_ID_HEADER, "12345")
    //             .content(gson.toJson(membershipRequest(-1000, MembershipType.NAVER)))
    //             .contentType(MediaType.APPLICATION_JSON)
    //     );
    //
    //     // then
    //     resultActions.andExpect(status().isBadRequest());
    // }
    //
    // @Test
    // public void 멤버십등록실패_멤버십종류가Null() throws Exception {
    //
    //     // given
    //     final String url = "/api/v1/memberships";
    //
    //     // when
    //     final ResultActions resultActions = mockMvc.perform(
    //         MockMvcRequestBuilders.post(url)
    //             .header(USER_ID_HEADER, "12345")
    //             .content(gson.toJson(membershipRequest(1000, null)))
    //             .contentType(MediaType.APPLICATION_JSON)
    //     );
    //
    //     // then
    //     resultActions.andExpect(status().isBadRequest());
    // }
    //
    // @Test
    // public void 멤버십등록실패_MemberService에서에러Throw() throws Exception {
    //
    //     final String url = "/api/v1/memberships";
    //
    //     // given
    //     doThrow(new MembershipException(MembershipErrorResult.DUPLICATED_MEMBERSHIP_REGISTER))
    //         .when(membershipService)
    //         .addMembership("12345", MembershipType.NAVER, 10000);
    //
    //     // when
    //     final ResultActions resultActions = mockMvc.perform(
    //         MockMvcRequestBuilders.post(url)
    //             .header(USER_ID_HEADER, "12345")
    //             .content(gson.toJson(membershipRequest(10000, MembershipType.NAVER)))
    //             .contentType(MediaType.APPLICATION_JSON)
    //     );
    //
    //     // then
    //     resultActions.andExpect(status().isBadRequest());
    // }

    @Test
    public void 멤버십등록성공() throws Exception {

        // given
        final String url = "/api/v1/memberships";
        final MembershipAddResponse membershipAddResponse = MembershipAddResponse.builder()
            .id(-1L)
            .membershipType(MembershipType.NAVER)
            .build();

        doReturn(membershipAddResponse).when(membershipService).addMembership("12345", MembershipType.NAVER, 10000);

        // when
        final ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.post(url)
                .header(USER_ID_HEADER, "12345")
                .content(gson.toJson(membershipRequest(10000, MembershipType.NAVER)))
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isCreated());

        final MembershipAddResponse response = gson.fromJson(
            resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8),
            MembershipAddResponse.class);

        assertThat(response.getMembershipType()).isEqualTo(MembershipType.NAVER);
        assertThat(response.getId()).isNotNull();
    }

    @Test
    public void 멤버십목록조회실패_사용자식별값없음() throws Exception {

        // given
        final String url = "/api/v1/memberships";

        // when
        ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.get(url)
        );

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void 멤버십목록조회성공() throws Exception {

        // given
        final String url = "/api/v1/memberships";
        doReturn(Arrays.asList(
            Membership.builder().build(),
            Membership.builder().build(),
            Membership.builder().build()
        )).when(membershipService).getMembershipList("12345");

        // when
        ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.get(url)
                .header(USER_ID_HEADER, "12345")
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void 멤버십상세조회실패_멤버십이없음() throws Exception {

        // given
        final String url = "/api/v1/memberships/-1";
        doThrow(new MembershipException(MembershipErrorResult.MEMBERSHIP_NOT_FOUND))
            .when(membershipService)
            .getMembership(-1L, "12345");

        // when
        final ResultActions result = mockMvc.perform(
            MockMvcRequestBuilders.get(url)
                .header(USER_ID_HEADER, "12345")
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isBadRequest());
    }

    @Test
    public void 멤버십상세조회실패_헤더값누락() throws Exception {

        // given
        final String url = "/api/v1/memberships/1";

        // when
        ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.get(url)
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void 멤버십상세조회성공() throws Exception {

        // given
        final String url = "/api/v1/memberships/1";
        doReturn(MembershipDetailResponse.builder()
            .id(1L)
            .membershipType(MembershipType.KAKAO)
            .point(10000)
            .createdAt(LocalDateTime.now())
            .build()
        ).when(membershipService).getMembership(1L, "userId");

        // when
        final ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.get(url)
                .header(USER_ID_HEADER, "userId")
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then

        resultActions.andExpect(status().isOk());

        // String contentAsString = resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        // System.out.println(contentAsString);
        //
        // // MembershipDetailResponse result = objectMapper.readValue(contentAsString,
        // //     MembershipDetailResponse.class);
        // MembershipDetailResponse result = gson.fromJson(contentAsString,
        //     MembershipDetailResponse.class);
        //
        //
        //
        // assertThat(result.getId()).isEqualTo(1L);
        // assertThat(result.getMembershipType()).isEqualTo(MembershipType.KAKAO);
        // assertThat(result.getPoint()).isEqualTo(10000);
    }
}
