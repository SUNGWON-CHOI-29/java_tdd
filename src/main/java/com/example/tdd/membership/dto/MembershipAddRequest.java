package com.example.tdd.membership.dto;

import lombok.Builder;
import lombok.Getter;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Getter
public class MembershipAddRequest {

    @Builder
    public MembershipAddRequest(Integer amount) {
        this.amount = amount;
    }

    @NotNull
    @Min(0)
    private Integer amount;
}
