package com.demo.graphql.demographql.creeper.douban.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@EqualsAndHashCode
public class Movie {
    private String name;
    private String url;
    private Double rate;
    private String region;
    private String language;
    private LocalDate launchDate;
    private List<String> categories;
    private List<String> actors;


}
