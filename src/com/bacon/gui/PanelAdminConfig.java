/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bacon.gui;



import com.bacon.Aplication;
import java.awt.BorderLayout;
import javax.swing.JTabbedPane;

/**
 *
 * @author ballestax
 */
public class PanelAdminConfig extends javax.swing.JPanel {

    private final Aplication app;
    private JTabbedPane tabPane;

    /**
     * Creates new form PanelAdminConfig
     *
     * @param app
     */
    public PanelAdminConfig(Aplication app) {
        this.app = app;
        initComponents();
        createComponents();
    }

    private void createComponents() {
        tabPane = new JTabbedPane();
        setLayout(new BorderLayout());
        add(tabPane, BorderLayout.CENTER);

        tabPane.addTab("Importar", app.getGuiManager().getPanelConfigPrint());
//        tabPane.addTab("Barrios", app.getGuiManager().getPanelConfigNeigbors());
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
