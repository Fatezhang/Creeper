package com.demo.graphql.demographql.creeper.douban.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@EqualsAndHashCode
public class WatchedMovie {
    private Movie movie;
    private LocalDate watchDate;
    private Integer rate;
    private String comment;
}
