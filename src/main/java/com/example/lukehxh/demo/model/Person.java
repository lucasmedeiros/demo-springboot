package com.example.lukehxh.demo.model;

import javax.persistence.Entity;

@Entity
public class Person extends AbstractEntity {
    private String name;

    public Person() {}

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}