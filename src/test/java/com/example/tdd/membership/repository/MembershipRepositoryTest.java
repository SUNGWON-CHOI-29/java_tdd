package com.example.tdd.membership.repository;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.tdd.common.constant.MembershipType;
import com.example.tdd.membership.entity.Membership;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
public class MembershipRepositoryTest {

    @Autowired
    private MembershipRepository membershipRepository;

    @Test
    public void MembershipRepository빈등록여부() {
        assertThat(membershipRepository).isNotNull();
    }

    @Test
    public void 멤버십등록() {
        //given
        final Membership membership = Membership.builder()
            .userId("userId")
            .membershipType(MembershipType.NAVER)
            .point(10000)
            .build();

        //when
        final Membership result = membershipRepository.save(membership);

        //then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getUserId()).isEqualTo("userId");
        assertThat(result.getMembershipType()).isEqualTo(MembershipType.NAVER);
        assertThat(result.getPoint()).isEqualTo(10000);
    }

    @Test
    public void 멤버십이존재하는지테스트() {
        //given
        Membership membership = Membership.builder()
            .userId("testId")
            .membershipType(MembershipType.KAKAO)
            .point(10000)
            .build();

        //when
        membershipRepository.save(membership);
        final Membership findResult = membershipRepository.findByUserIdAndMembershipType("testId", MembershipType.KAKAO);

        //then
        assertThat(findResult.getId()).isNotNull();
        assertThat(findResult.getUserId()).isEqualTo("testId");
        assertThat(findResult.getMembershipType()).isEqualTo(MembershipType.KAKAO);
        assertThat(findResult.getPoint()).isEqualTo(10000);
    }

}
