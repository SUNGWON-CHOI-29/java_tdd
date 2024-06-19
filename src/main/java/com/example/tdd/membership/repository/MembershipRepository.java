package com.example.tdd.membership.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tdd.common.constant.MembershipType;
import com.example.tdd.membership.entity.Membership;

public interface MembershipRepository extends JpaRepository<Membership, Long> {

    Membership findByUserIdAndMembershipType(final String userId, final MembershipType membershipType);

    List<Membership> findAllByUserId(final String userId);

    int deleteByIdAndUserId(Long id, String userId);
}
