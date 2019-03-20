package com.example.demo;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    @Size(min = 3)
    private String categoryName;

    @OneToMany(mappedBy = "category",
            cascade = CascadeType.REMOVE,  // .ALL won't allow you to do the delete but .REMOVE will
            // cascade = data persistence. read it in context of hibernate (implementation of Jpa - turns JPQL queries to SQL queries)
            // https://www.baeldung.com/delete-with-hibernate
            fetch = FetchType.EAGER)
    public Set<Car> cars;   // Set must be public

    public Category(){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Set<Car> getCars() {
        return cars;
    }

    public void setCars(Set<Car> cars) {
        this.cars = cars;
    }
}
