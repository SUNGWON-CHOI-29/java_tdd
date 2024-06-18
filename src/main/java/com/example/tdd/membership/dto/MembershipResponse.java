package com.example.tdd.membership.dto;

import com.example.tdd.common.constant.MembershipType;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class MembershipResponse {

    private final Long id;
    private final String userId;
    private final MembershipType membershipType;
}
