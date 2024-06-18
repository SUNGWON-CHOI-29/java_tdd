package com.example.tdd.membership.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import com.example.tdd.common.constant.MembershipType;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MembershipRequest {

    @NotNull
    @Min(0)
    private Integer point;

    @NotNull
    private MembershipType membershipType;
}
