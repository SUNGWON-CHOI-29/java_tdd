package com.example.tdd.membership.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.tdd.common.constant.MembershipErrorResult;
import com.example.tdd.common.constant.MembershipType;
import com.example.tdd.exception.MembershipException;
import com.example.tdd.membership.dto.MembershipResponse;
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
        final MembershipResponse result = target.addMembership(userId, membershipType, point);

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
}
