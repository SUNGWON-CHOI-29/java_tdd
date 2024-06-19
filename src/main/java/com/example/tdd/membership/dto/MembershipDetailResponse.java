package com.example.tdd.membership.dto;

import java.time.LocalDateTime;

import com.example.tdd.common.constant.MembershipType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
public class MembershipDetailResponse {
    private final Long id;
    private final MembershipType membershipType;
    private final Integer point;
    private final LocalDateTime createdAt;
}
