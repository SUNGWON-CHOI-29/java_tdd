package com.example.tdd.membership.entity;

import java.io.Serializable;

import com.example.tdd.common.constant.MembershipType;
import com.example.tdd.common.domain.Common;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity
@Table
@NoArgsConstructor
@Getter
@ToString
public class Membership extends Common implements Serializable {

    @Builder
    public Membership(String userId, MembershipType membershipType, Integer point) {
        this.userId = userId;
        this.membershipType = membershipType;
        this.point = point;
    }

    private String userId;

    @Enumerated(EnumType.STRING)
    private MembershipType membershipType;

    private Integer point;
}
