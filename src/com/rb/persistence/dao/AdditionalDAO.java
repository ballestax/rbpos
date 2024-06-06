/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rb.persistence.dao;


import com.rb.domain.Additional;
import java.util.ArrayList;

/**
 *
 * @author LuisR
 */
public interface AdditionalDAO {

    public Additional getAdditional(long id) throws com.rb.persistence.dao.DAOException;

    public ArrayList<Additional> getAdditionalList() throws com.rb.persistence.dao.DAOException;

    public void addAdditional(Additional additional) throws com.rb.persistence.dao.DAOException;

    public void deleteAdditional(long id) throws com.rb.persistence.dao.DAOException;

    public void updateAdditional(Additional additional) throws com.rb.persistence.dao.DAOException;

}
