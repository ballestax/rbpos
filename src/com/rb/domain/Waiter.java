package com.rb.domain;

import java.awt.Color;
import java.util.Objects;

/**
 *
 * @author lrod
 */
public class Waiter {

    private int id;
    private String name;
    private String stColor;
    private int status;

    public Waiter() {
    }

    public Waiter(String name, int status) {
        this.name = name;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStColor() {
        return stColor;
    }
    
    public Color getColor() {
        try{
            return Color.decode(stColor);
        }catch (Exception e){
            return Color.GRAY;
        }
    }

    public void setStColor(String color) {
        this.stColor = color;
    }

    @Override
    public String toString() {
        return name.toUpperCase();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Waiter)) {
            return false;
        }
        Waiter waiter = (Waiter) obj;
        if (id != waiter.id) {
            return false;
        }
        return name.equalsIgnoreCase(waiter.getName());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.name);
        return hash;
    }
}
