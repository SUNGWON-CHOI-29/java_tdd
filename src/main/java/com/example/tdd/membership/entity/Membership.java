package com.example.tdd.membership.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serializable;

import com.example.tdd.common.constant.MembershipType;
import com.example.tdd.common.domain.Common;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table
@NoArgsConstructor
@Getter
public class Membership extends Common implements Serializable {

    @Builder
    public Membership(String userId, MembershipType membershipName, long point) {
        this.userId = userId;
        this.membershipType = membershipName;
        this.point = point;
    }

    private String userId;

    @Enumerated(EnumType.STRING)
    private MembershipType membershipType;

    private long point;
}
