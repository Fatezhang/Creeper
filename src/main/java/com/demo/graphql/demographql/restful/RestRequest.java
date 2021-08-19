package com.demo.graphql.demographql.restful;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.LocalDate;

public class RestRequest {

    @NotBlank
    @Size(min = 12)
    private String name;
    @Min(value = 12, groups = BasicInfo.class)
    private Integer age;
    @Past
    private LocalDate birthday;

    @Override
    public String toString() {
        return "RestRequest{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", birthday=" + birthday +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }
}
