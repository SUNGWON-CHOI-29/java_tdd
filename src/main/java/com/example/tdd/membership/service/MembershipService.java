package com.example.tdd.membership.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.tdd.common.constant.MembershipErrorResult;
import com.example.tdd.common.constant.MembershipType;
import com.example.tdd.exception.MembershipException;
import com.example.tdd.membership.dto.MembershipAddResponse;
import com.example.tdd.membership.dto.MembershipDetailResponse;
import com.example.tdd.membership.entity.Membership;
import com.example.tdd.membership.repository.MembershipRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MembershipService {

    private final MembershipRepository membershipRepository;

    public MembershipAddResponse addMembership(final String userId, final MembershipType membershipType, final Integer point) {
        final Membership result = membershipRepository.findByUserIdAndMembershipType(userId,
            membershipType);

        if (result != null) {
            throw new MembershipException(MembershipErrorResult.DUPLICATED_MEMBERSHIP_REGISTER);
        }

        final Membership membership = Membership.builder()
            .userId(userId)
            .point(point)
            .membershipType(membershipType)
            .build();

        Membership saveMembership = membershipRepository.save(membership);

        return MembershipAddResponse.builder()
            .id(saveMembership.getId())
            .userId(saveMembership.getUserId())
            .membershipType(saveMembership.getMembershipType())
            .build();
    }

    public List<MembershipDetailResponse> getMembershipList(final String userId) {
        List<Membership> membershipList = membershipRepository.findAllByUserId(userId);
        return membershipList.stream()
            .map(membership -> MembershipDetailResponse.builder()
                    .id(membership.getId())
                    .membershipType(membership.getMembershipType())
                    .point(membership.getPoint())
                    .createdAt(membership.getCreatedAt())
                    .build())
            .toList();
    }

    public MembershipDetailResponse getMembership(final Long membershipId, final String userId) {
        final Optional<Membership> optionalMembership = membershipRepository.findById(membershipId);
        final Membership membership = optionalMembership.orElseThrow(() -> new MembershipException(MembershipErrorResult.MEMBERSHIP_NOT_FOUND));

        if (!membership.getUserId().equals(userId)) {
            throw new MembershipException(MembershipErrorResult.NOT_MEMBERSHIP_OWNER);
        }

        return MembershipDetailResponse.builder()
            .id(membership.getId())
            .membershipType(membership.getMembershipType())
            .point(membership.getPoint())
            .createdAt(membership.getCreatedAt())
            .build();
    }

    public void removeMembership(Long membershipId, String userId) {
        Optional<Membership> optionalMembership = membershipRepository.findById(membershipId);
        Membership membership = optionalMembership.orElseThrow(
            () -> new MembershipException(MembershipErrorResult.MEMBERSHIP_NOT_FOUND));

        if (!membership.getUserId().equals(userId)) {
            throw new MembershipException(MembershipErrorResult.NOT_MEMBERSHIP_OWNER);
        }

    }
}
