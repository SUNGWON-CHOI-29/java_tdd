package com.example.tdd.membership.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tdd.membership.entity.Membership;

public interface MembershipRepository extends JpaRepository<Membership, String> {
}
