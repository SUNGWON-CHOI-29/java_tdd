package com.example.tdd.membership.repository;

import java.util.List;
import java.util.Optional;

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

    @Test
    public void 멤버십조회결과가0() {

        //given

        //when
        final List<Membership> findResult = membershipRepository.findAllByUserId("userId");

        //then
        assertThat(findResult.size()).isEqualTo(0);
    }

    @Test
    public void 멤버십조회결과가2() {

        // given
        Membership naverMembership = Membership.builder()
            .userId("test1234")
            .membershipType(MembershipType.NAVER)
            .point(10000)
            .build();

        Membership kakaoMembership = Membership.builder()
            .userId("test1234")
            .membershipType(MembershipType.KAKAO)
            .point(5000)
            .build();

        membershipRepository.save(naverMembership);
        membershipRepository.save(kakaoMembership);

        // when
        List<Membership> findResult = membershipRepository.findAllByUserId("test1234");

        // then
        assertThat(findResult.size()).isEqualTo(2);
    }

    @Test
    public void 멤버십상세조회실패() {

        // given

        // when
        Optional<Membership> userId = membershipRepository.findById(1L);

        // then
        assertThat(userId.isPresent()).isFalse();
    }

    @Test
    public void 멤버십상세조회성공() {

        // given
        Membership membership = Membership.builder()
            .userId("test124")
            .point(10000)
            .membershipType(MembershipType.KAKAO)
            .build();

        Membership result = membershipRepository.save(membership);

        // when
        Optional<Membership> findResult = membershipRepository.findById(result.getId());

        // then
        assertThat(findResult.isPresent()).isTrue();
        assertThat(findResult.get().getUserId()).isEqualTo(result.getUserId());
    }

    // @Test
    // public void 멤버십삭제실패_멤버십없음() {
    //
    //     // given
    //
    //     // when
    //     int result = membershipRepository.deleteByIdAndUserId(1L, "userId");
    //
    //     // then
    //     assertThat(result).isEqualTo(0);
    // }
    //
    // @Test
    // public void 멤버십삭제실패_주인이아님() {
    //
    //     // given
    //     Membership memberhsip = Membership.builder()
    //         .userId("userId")
    //         .membershipType(MembershipType.KAKAO)
    //         .point(1000)
    //         .build();
    //
    //     Membership savedMembership = membershipRepository.save(memberhsip);
    //
    //     // when
    //     int result = membershipRepository.deleteByIdAndUserId(savedMembership.getId(), "test1234");
    //
    //     // then
    //     assertThat(result).isEqualTo(0);
    // }

    @Test
    public void 멤버십삭제성공() {

        // given
        Membership membership = Membership.builder()
            .userId("userId")
            .membershipType(MembershipType.NAVER)
            .point(10000)
            .build();

        Membership savedMembership = membershipRepository.save(membership);

        // when

        membershipRepository.deleteById(savedMembership.getId());

        // result

    }
}
