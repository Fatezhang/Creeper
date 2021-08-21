package com.demo.graphql.demographql.creeper.douban;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class LaunchDateUtilTest {
    @Test
    void shouldExtractDate() {
        var dateStr1 = "2020-01-01some text";
        var date1 = LaunchDateUtil.extractDateFromString(dateStr1);
        assertThat(date1).isEqualTo(LocalDate.parse("2020-01-01"));
        var dateStr2 = "2020-01上映";
        var date2 = LaunchDateUtil.extractDateFromString(dateStr2);
        assertThat(date2).isEqualTo(LocalDate.parse("2020-01-01"));
        var dateStr3 = "2020上映";
        var date3 = LaunchDateUtil.extractDateFromString(dateStr3);
        assertThat(date3).isEqualTo(LocalDate.parse("2020-01-01"));
    }
}
