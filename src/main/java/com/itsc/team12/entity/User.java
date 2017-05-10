package com.itsc.team12.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by Sam on 5/10/2017.
 */

@Document
public class User {

    @Id
    private String id;

    private String firstName;
    private String lastName;
    private boolean isVisable;
    @DBRef
    private Location location;

    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isVisable() {
        return isVisable;
    }

    public void setVisable(boolean visable) {
        isVisable = visable;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public User(String id, String firstName, String lastName, boolean isVisable, Location location) {

        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isVisable = isVisable;
        this.location = location;
    }
}
