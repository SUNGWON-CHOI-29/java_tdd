package com.example.tdd.membership.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.tdd.common.constant.MembershipErrorResult;
import com.example.tdd.common.constant.MembershipType;
import com.example.tdd.exception.MembershipException;
import com.example.tdd.membership.dto.MembershipAddResponse;
import com.example.tdd.membership.dto.MembershipDetailResponse;
import com.example.tdd.membership.entity.Membership;
import com.example.tdd.membership.repository.MembershipRepository;

import lombok.extern.slf4j.Slf4j;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class MembershipServiceTest {

    @InjectMocks
    private MembershipService target;

    @Mock
    private MembershipRepository membershipRepository;

    private final String userId = "userId";
    private final MembershipType membershipType = MembershipType.KAKAO;
    private final Integer point = 10000;

    private final Long membershipId = 1L;

    @Test
    public void 멤버십등록실패_이미존재함() {

        //given
        doReturn(Membership.builder().build()).when(membershipRepository)
            .findByUserIdAndMembershipType(userId, membershipType);

        //when
        final MembershipException result = assertThrows(MembershipException.class,
            () -> target.addMembership(userId, membershipType, point));

        //then
        assertThat(result.getErrorResult()).isEqualTo(MembershipErrorResult.DUPLICATED_MEMBERSHIP_REGISTER);
    }

    @Test
    public void 멤버십등록성공() {
        //given
        doReturn(null).when(membershipRepository).findByUserIdAndMembershipType(userId, membershipType);
        doReturn(membership()).when(membershipRepository).save(any(Membership.class));

        //when
        final MembershipAddResponse result = target.addMembership(userId, membershipType, point);

        //then
        assertThat(result.getUserId()).isNotNull();
        assertThat(result.getMembershipType()).isEqualTo(MembershipType.KAKAO);

        //verify
        verify(membershipRepository, times(1)).findByUserIdAndMembershipType(userId, membershipType);
        verify(membershipRepository, times(1)).save(any(Membership.class));

    }

    private Membership membership() {
        return Membership.builder()
            .userId(userId)
            .point(point)
            .membershipType(MembershipType.KAKAO)
            .build();
    }

    @Test
    public void 멤버십목록조회() {

        //given
        doReturn(Arrays.asList(
            Membership.builder().build(),
            Membership.builder().build(),
            Membership.builder().build()
        )).when(membershipRepository).findAllByUserId(userId);

        //when
        final List<MembershipDetailResponse> findResult = target.getMembershipList(userId);

        //then

        assertThat(findResult.size()).isEqualTo(3);
    }

    @Test
    public void 멤버십상세조회실패_멤버십이없음() {

        // given
        doReturn(Optional.empty()).when(membershipRepository).findById(membershipId);

        // when
        final MembershipException result = assertThrows(MembershipException.class,
            () -> target.getMembership(membershipId, userId));

        // then
        assertThat(result.getErrorResult()).isEqualTo(MembershipErrorResult.MEMBERSHIP_NOT_FOUND);
    }

    @Test
    public void 멤버십상세조회실패_본인이아님() {

        // given
        doReturn(Optional.of(membership())).when(membershipRepository).findById(membershipId);

        // when
        final MembershipException result = assertThrows(MembershipException.class,
            () -> target.getMembership(membershipId, "notowner"));

        // then
        assertThat(result.getErrorResult()).isEqualTo(MembershipErrorResult.NOT_MEMBERSHIP_OWNER);

    }

    @Test
    public void 멤버십상세조회성공() {

        // given
        doReturn(Optional.of(membership())).when(membershipRepository).findById(membershipId);
        // when
        MembershipDetailResponse membershipDetailResponse = target.getMembership(membershipId, userId);
        // then
        assertThat(membershipDetailResponse.getMembershipType()).isEqualTo(membershipType);
        assertThat(membershipDetailResponse.getPoint()).isEqualTo(point);
    }

    @Test
    public void 멤버십삭제실패_멤버십이없음() {

        // given
        doReturn(Optional.empty()).when(membershipRepository).findById(membershipId);

        // when
        final MembershipException result = assertThrows(MembershipException.class,
            () -> target.removeMembership(membershipId, userId));

        // then
        assertThat(result.getErrorResult()).isEqualTo(MembershipErrorResult.MEMBERSHIP_NOT_FOUND);
    }

    @Test
    public void 멤버십삭제실패_주인이아님() {

        // given
        doReturn(Optional.of(membership())).when(membershipRepository).findById(membershipId);

        // when
        final MembershipException result = assertThrows(MembershipException.class,
            () -> target.removeMembership(membershipId, "test1234"));

        // then
        assertThat(result.getErrorResult()).isEqualTo(MembershipErrorResult.NOT_MEMBERSHIP_OWNER);
    }

    @Test
    public void 멤버십삭제성공() {

        // given
        doReturn(Optional.of(membership())).when(membershipRepository).findById(membershipId);

        // when
        target.removeMembership(membershipId, userId);

        // then

    }
}
