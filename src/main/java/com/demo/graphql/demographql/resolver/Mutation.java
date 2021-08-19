package com.demo.graphql.demographql.resolver;

import com.demo.graphql.demographql.model.Food;
import com.demo.graphql.demographql.service.FoodService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import org.springframework.stereotype.Component;

@Component
public class Mutation implements GraphQLMutationResolver {

    private final FoodService foodService;

    public Mutation(FoodService foodService){
        this.foodService = foodService;
    }

    public Food addFood(String name, Integer kal, String expireDate) {
        Food food = Food.builder().name(name).kal(kal).expireDate(expireDate).build();
        foodService.addFoods(food);
        return food;
    }
}
