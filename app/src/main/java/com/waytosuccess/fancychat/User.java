package com.waytosuccess.fancychat;

public class User {

    private String name;
    private String email;
    private String id;
    private int imageMockUpResource;

    public User() {
    }

    public User(String name, String email, String id, int imageMockUpResource) {
        this.name = name;
        this.email = email;
        this.id = id;
        this.imageMockUpResource = imageMockUpResource;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getImageMockUpResource() {
        return imageMockUpResource;
    }

    public void setImageMockUpResource(int imageMockUpResource) {
        this.imageMockUpResource = imageMockUpResource;
    }
}
