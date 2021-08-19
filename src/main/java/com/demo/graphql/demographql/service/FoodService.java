package com.demo.graphql.demographql.service;

import com.demo.graphql.demographql.model.Food;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FoodService {

    private final static LinkedList<Food> cache = new LinkedList<>();

    public List<Food> getRecentFoods(String name) {
        return cache.stream().filter(food -> food.getName().contains(name)).collect(Collectors.toList());
    }

    public void addFoods(Food food) {
        cache.add(food);
    }
}
