package com.demo.graphql.demographql.resolver;

import com.demo.graphql.demographql.model.Food;
import com.demo.graphql.demographql.service.FoodService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Query implements GraphQLQueryResolver {

    private final FoodService foodService;

    public Query(FoodService foodService) {
        this.foodService = foodService;
    }

    public List<Food> recentFoods(String name) {
        return foodService.getRecentFoods(name);
    }
}
