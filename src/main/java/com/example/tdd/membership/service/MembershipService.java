package com.example.tdd.membership.service;

import org.springframework.stereotype.Service;

import com.example.tdd.common.constant.MembershipErrorResult;
import com.example.tdd.common.constant.MembershipType;
import com.example.tdd.exception.MembershipException;
import com.example.tdd.membership.dto.MembershipResponse;
import com.example.tdd.membership.entity.Membership;
import com.example.tdd.membership.repository.MembershipRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MembershipService {

    private final MembershipRepository membershipRepository;

    public MembershipResponse addMembership(final String userId, final MembershipType membershipType, final Integer point) {
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

        return MembershipResponse.builder()
            .id(saveMembership.getId())
            .userId(saveMembership.getUserId())
            .membershipType(saveMembership.getMembershipType())
            .build();
    }
}
