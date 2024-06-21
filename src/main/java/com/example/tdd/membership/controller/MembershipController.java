package com.example.tdd.membership.controller;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.tdd.membership.dto.MembershipAddRequest;
import com.example.tdd.membership.dto.MembershipDetailResponse;
import com.example.tdd.membership.dto.MembershipRequest;
import com.example.tdd.membership.dto.MembershipAddResponse;
import com.example.tdd.membership.service.MembershipService;

import lombok.RequiredArgsConstructor;

import static com.example.tdd.common.constant.MembershipConstatns.*;

@RestController
@RequiredArgsConstructor
public class MembershipController {

    private final MembershipService membershipService;

    @PostMapping("/api/v1/memberships")
    public ResponseEntity<MembershipAddResponse> addMembership(
        @RequestHeader(USER_ID_HEADER) final String userId,
        @RequestBody @Valid final MembershipRequest membershipRequest) {

        final MembershipAddResponse membershipAddResponse = membershipService.addMembership(userId,
            membershipRequest.getMembershipType(), membershipRequest.getPoint());

        return ResponseEntity.status(HttpStatus.CREATED).body(membershipAddResponse);
    }

    @GetMapping("/api/v1/memberships")
    public ResponseEntity<List<MembershipDetailResponse>> getMembershipList(
        @RequestHeader(USER_ID_HEADER) final String userId) {
        List<MembershipDetailResponse> membershipList = membershipService.getMembershipList(userId);

        return ResponseEntity.status(HttpStatus.OK).body(membershipList);
    }

    @GetMapping("/api/v1/memberships/{membershipId}")
    public ResponseEntity<MembershipDetailResponse> getMembership(
        @RequestHeader(USER_ID_HEADER) final String userId,
        @PathVariable final Long membershipId) {
        MembershipDetailResponse membershipDetailResponse = membershipService.getMembership(membershipId, userId);

        return ResponseEntity.status(HttpStatus.OK).body(membershipDetailResponse);
    }

    @DeleteMapping("/api/v1/memberships/{membershipId}")
    public ResponseEntity deleteMembership(
        @RequestHeader(USER_ID_HEADER) final String userId,
        @PathVariable final Long membershipId) {
        membershipService.deleteMembership(membershipId, userId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/api/v1/memberships/{membershipId}/points")
    public ResponseEntity accmulatePoint(
        @RequestHeader(USER_ID_HEADER) final String userId,
        @PathVariable final Long membershipId,
        @RequestBody @Valid final MembershipAddRequest membershipAddRequest) {

        membershipService.accmulateMembershipPoint(membershipId, userId, membershipAddRequest.getAmount());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
