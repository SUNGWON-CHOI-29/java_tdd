package com.example.tdd.membership.service;

import java.util.Optional;
import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.tdd.common.constant.MembershipErrorResult;
import com.example.tdd.exception.MembershipException;
import com.example.tdd.membership.repository.MembershipRepository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RatePointServiceTest {

    @InjectMocks
    private PointService ratePointService;

    @ParameterizedTest
    @MethodSource("calculateParam")
    public void price의적립금은1퍼센트(final int price, final int expect) {

        // given

        // when
        final int result = ratePointService.calculateAmount(price);

        // then
        assertThat(result).isEqualTo(expect);
    }

    public static Stream<Arguments> calculateParam() {
        return Stream.of(
            Arguments.of(10000, 100),
            Arguments.of(20000, 200),
            Arguments.of(30000, 300)
        );
    }
}
