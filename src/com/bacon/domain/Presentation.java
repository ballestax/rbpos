package com.bacon.domain;

import java.util.Objects;

/**
 *
 * @author lrod
 */
public class Presentation {

    private int serie;
    private long idProduct;
    private String name;
    private double price;

    public Presentation() {
    }

    public int getSerie() {
        return serie;
    }

    public void setSerie(int id) {
        this.serie = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public long getIDProd() {
        return idProduct;
    }

    public void setIDProd(long idProduct) {
        this.idProduct = idProduct;
    }

    @Override
    public String toString() {
        return name + ":(" + price + ")";
    }
    
     @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        Presentation pres = (Presentation) obj;
        if (idProduct  != pres.getIDProd()) {
            return false;
        }
        if (serie != pres.getSerie()) {
            return false;
        }
        if(!name.equals(pres.getName())){
            return false;
        }
        return true;
        
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + this.serie;
        hash = 53 * hash + (int) (this.idProduct ^ (this.idProduct >>> 32));
        hash = 53 * hash + Objects.hashCode(this.name);
        return hash;
    }
    
    
}
