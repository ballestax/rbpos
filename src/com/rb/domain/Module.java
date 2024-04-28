package com.rb.domain;

/**
 *
 * @author nait
 */
public class Module {

    private String name;
    private String permision;

    public Module(String name, String permision) {
        this.name = name;
        this.permision = permision;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPermision() {
        return permision;
    }

    public void setPermision(String permision) {
        this.permision = permision;
    }

}
