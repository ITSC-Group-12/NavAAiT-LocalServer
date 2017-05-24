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
    private boolean isVisible = true;
    private String deviceId;
    @DBRef
    private Location location;

    public User() {
    }

    public User(String id, String firstName, String lastName, String deviceId, boolean isVisible, Location location) {

        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.deviceId = deviceId;
        this.isVisible = isVisible;
        this.location = location;
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

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public int hashCode() {
        return this.getId().hashCode();
    }
}
