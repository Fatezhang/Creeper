package com.demo.graphql.demographql.model;

public class Food {
    private String name;
    private Integer kal;
    private String expireDate;

    private int shouldEatTimes;

    public static FoodBuilder builder() {
        return new FoodBuilder();
    }

    public String getName() {
        return this.name;
    }

    public static class FoodBuilder {
        private final Food food;

        public FoodBuilder() {
            food = new Food();
        }

        public FoodBuilder name(String name) {
            this.food.name = name;
            return this;
        }

        public FoodBuilder kal(int kal) {
            this.food.kal = kal;
            return this;
        }

        public FoodBuilder expireDate(String expireDate) {
            this.food.expireDate = expireDate;
            return this;
        }

        public Food build() {
            return this.food;
        }
    }
}
