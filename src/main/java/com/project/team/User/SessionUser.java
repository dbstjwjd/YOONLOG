package com.project.team.User;

import com.project.team.User.SiteUser;

import java.io.Serializable;

public class SessionUser implements Serializable {
    private String name;
    private String email;
    private String picture;


    public SessionUser(SiteUser siteUser){
        this.name = siteUser.getName();
        this.email = siteUser.getEmail();
        this.picture = siteUser.getPicture();
    }

    public SessionUser() {
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

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
