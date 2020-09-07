package com.marving.boot.base;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class AbstractAdmin extends AbstractUser{

    public interface Role{
        String sys = "sys";
        String admin = "admin";
    }

    @Column
    private String role;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
