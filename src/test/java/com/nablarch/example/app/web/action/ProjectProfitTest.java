package com.nablarch.example.app.web.action;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * {@link ProjectProfit}のテスト
 */
class ProjectProfitTest {

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class 売上総利益 {
        List<Param<Long>> parameters() {
            return Arrays.asList(
                    new Param<>(new ProjectProfit(987654321, 123456789, 200, 300), 864197532L),
                    new Param<>(new ProjectProfit(999999999, 0, 200, 300), 999999999L),
                    new Param<>(new ProjectProfit(0, 999999999, 200, 300), -999999999L),
                    new Param<>(new ProjectProfit(0, 0, 200, 300), 0L),
                    new Param<Long>(new ProjectProfit(null, 100000000, 200, 300), null),
                    new Param<Long>(new ProjectProfit(10000, null, 200, 300), null),
                    new Param<>(new ProjectProfit(10000, 1000, null, 300), 9000L),
                    new Param<>(new ProjectProfit(10000, 1000, 200, null), 9000L),
                    new Param<Long>(new ProjectProfit(null, null, 200, 300), null));
        }

        @ParameterizedTest
        @MethodSource("parameters")
        public void test(Param<Long> param) throws Exception {
            assertThat(param.projectProfit.getGrossProfit(), is(param.expected));
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class 配賦前利益 {
        List<Param<Long>> parameters() {
            return Arrays.asList(
                    new Param<>(new ProjectProfit(987654321, 123456789, 12345678, 300), 851851854L),
                    new Param<>(new ProjectProfit(999999999, 0, 0, 300), 999999999L),
                    new Param<>(new ProjectProfit(0, 999999999, 999999999, 300), -1999999998L),
                    new Param<>(new ProjectProfit(0, 0, 0, 300), 0L),
                    new Param<Long>(new ProjectProfit(null, 2000000, 1000000, 300), null),
                    new Param<Long>(new ProjectProfit(3000000, null, 1000000, 300), null),
                    new Param<Long>(new ProjectProfit(3000000, 2000000, null, 300), null),
                    new Param<>(new ProjectProfit(3000000, 2000000, 1000000, null), 0L),
                    new Param<Long>(new ProjectProfit(null, null, null, 300), null)
            );
        }

        @ParameterizedTest
        @MethodSource("parameters")
        void test(Param<Long> param) throws Exception {
            assertThat(param.projectProfit.getProfitBeforeAllocation(), is(param.expected));
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class 配賦前利益率 {
        List<Param<BigDecimal>> parameters() {
            return Arrays.asList(
                    new Param<>(new ProjectProfit(987654321, 123456789, 12345678, 300), new BigDecimal("0.862")),
                    new Param<>(new ProjectProfit(999999999, 0, 0, 300), new BigDecimal("1.000")),
                    new Param<>(new ProjectProfit(0, 999999999, 999999999, 300), new BigDecimal("0.000")),
                    new Param<>(new ProjectProfit(0, 0, 0, 300), new BigDecimal("0.000")),
                    new Param<BigDecimal>(new ProjectProfit(null, 2000000, 1000000, 300), null),
                    new Param<BigDecimal>(new ProjectProfit(3000000, null, 1000000, 300), null),
                    new Param<BigDecimal>(new ProjectProfit(3000000, 2000000, null, 300), null),
                    new Param<>(new ProjectProfit(3000000, 2000000, 1000000, null), new BigDecimal("0.000")),
                    new Param<BigDecimal>(new ProjectProfit(null, null, null, 300), null)
            );
        }

        @ParameterizedTest
        @MethodSource("parameters")
        void test(Param<Long> param) throws Exception {
            assertThat(param.projectProfit.getProfitRateBeforeAllocation(), is(param.expected));
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class 営業利益 {
        List<Param<Long>> parameters() {
            return Arrays.asList(
                    new Param<>(new ProjectProfit(987654321, 123456789, 12345678, 1234567), 850617287L),
                    new Param<>(new ProjectProfit(999999999, 0, 0, 0), 999999999L),
                    new Param<>(new ProjectProfit(0, 999999999, 999999999, 999999999), -2999999997L),
                    new Param<>(new ProjectProfit(0, 0, 0, 0), 0L),
                    new Param<Long>(new ProjectProfit(null, 5000000, 3000000, 1000000), null),
                    new Param<Long>(new ProjectProfit(7000000, null, 3000000, 1000000), null),
                    new Param<Long>(new ProjectProfit(7000000, 5000000, null, 1000000), null),
                    new Param<Long>(new ProjectProfit(7000000, 5000000, 3000000, null), null),
                    new Param<Long>(new ProjectProfit(null, null, null, null), null)
            );
        }

        @ParameterizedTest
        @MethodSource("parameters")
        void test(Param<Long> param) throws Exception {
            assertThat(param.projectProfit.getOperatingProfit(), is(param.expected));
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class 営業利益率 {
        List<Param<BigDecimal>> parameters() {
            return Arrays.asList(
                    new Param<>(new ProjectProfit(987654321, 123456789, 12345678, 1234567), new BigDecimal("0.861")),
                    new Param<>(new ProjectProfit(999999999, 0, 0, 0), new BigDecimal("1.000")),
                    new Param<>(new ProjectProfit(0, 999999999, 999999999, 999999999), new BigDecimal("0.000")),
                    new Param<>(new ProjectProfit(0, 0, 0, 0), new BigDecimal("0.000")),
                    new Param<>(new ProjectProfit(7000000, 3500000, 2500000, 1000000), new BigDecimal("0.000")),
                    new Param<BigDecimal>(new ProjectProfit(null, 5000000, 3000000, 1000000), null),
                    new Param<BigDecimal>(new ProjectProfit(7000000, null, 3000000, 1000000), null),
                    new Param<BigDecimal>(new ProjectProfit(7000000, 5000000, null, 1000000), null),
                    new Param<BigDecimal>(new ProjectProfit(7000000, 5000000, 3000000, null), null),
                    new Param<BigDecimal>(new ProjectProfit(null, null, null, null), null)
            );
        }

        @ParameterizedTest
        @MethodSource("parameters")
        void test(Param<Long> param) throws Exception {
            assertThat(param.projectProfit.getOperatingProfitRate(), is(param.expected));
        }
    }

    private static class Param<T> {

        private ProjectProfit projectProfit;

        private T expected;

        public Param(final ProjectProfit projectProfit, final T expected) {
            this.projectProfit = projectProfit;
            this.expected = expected;
        }
    }
}