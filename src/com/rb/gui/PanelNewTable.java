/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rb.gui;

import com.rb.Aplication;
import com.rb.domain.Rol;
import com.rb.domain.Table;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JColorChooser;
import javax.swing.JOptionPane;
import org.dz.PanelCapturaMod;

/**
 *
 * @author LUISA
 */
public class PanelNewTable extends PanelCapturaMod implements ActionListener {

    private final Aplication app;

    public static final String AC_NEW_TABLE = "AC_NEW_TABLE";
    public static final String AC_UPDATE_TABLE = "AC_UPDATE_TABLE";

    private String title;
    private Color color;
    private int mode;
    private Table table;

    public PanelNewTable(Aplication app, Table table) {
        this.app = app;
        initComponents();
        createComponents();
        mode = 0;
        if (table != null) {
            mode = 1; //EDITANDO
            this.table = table;
            loadTable(table);
        }
    }

    private void createComponents() {

        ArrayList<Rol> rolesList = app.getControl().getRolesList();
        cbStatus.setModel(new DefaultComboBoxModel(new String[]{"ACTIVO", "INACTIVO"}));

      
        btCancel.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                getRootPane().getParent().setVisible(false);
            }
        });

        setTitle("");

        btAcept.setActionCommand(AC_NEW_TABLE);
        btAcept.addActionListener(this);

        //pattern = Pattern.compile(USERNAME_PATTERN);
    }

    public void setTitle(String title) {
        this.title = title;
        jLabel1.setText("Ingresar Mesa.");
    }

    public void loadTable(Table table) {
        tfUser.setText(table.getName().toUpperCase());
        cbStatus.setSelectedIndex(table.getStatus() == 0 ? 1 : 0);
        btAcept.setActionCommand(AC_UPDATE_TABLE);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        btAcept = new javax.swing.JButton();
        btCancel = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        tfUser = new javax.swing.JTextField();
        cbStatus = new javax.swing.JComboBox<>();

        jLabel1.setBackground(java.awt.Color.lightGray);
        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel1.setOpaque(true);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Estado:");

        btAcept.setText("Aceptar");

        btCancel.setText("Cancelar");

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Nombre:");

        tfUser.setFont(new java.awt.Font("Liberation Sans", 0, 18)); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btAcept, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tfUser, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel2, jLabel4});

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cbStatus, tfUser});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(tfUser, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btAcept, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {cbStatus, tfUser});

    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btAcept;
    private javax.swing.JButton btCancel;
    private javax.swing.JComboBox<String> cbStatus;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JTextField tfUser;
    // End of variables declaration//GEN-END:variables

    @Override
    public void reset() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (AC_NEW_TABLE.equals(e.getActionCommand())) {
            String name = tfUser.getText();
            int status = cbStatus.getSelectedIndex() == 0 ? 1 : 0;
            if (!name.isEmpty()) {
                Table table = new Table(name, status);               
                app.getControl().addTable(table);
                pcs.firePropertyChange(AC_NEW_TABLE, table, null);

                getRootPane().getParent().setVisible(false);
            } else {
                JOptionPane.showMessageDialog(null, "Ingrese el nombre del mesero");
            }

        } else if (AC_UPDATE_TABLE.equals(e.getActionCommand())) {
            String name = tfUser.getText();
            int status = cbStatus.getSelectedIndex() == 0 ? 1 : 0;
            if (!name.isEmpty()) {
                table.setName(name);
                table.setStatus(status);               
                app.getControl().updateTable(table);
                pcs.firePropertyChange(AC_UPDATE_TABLE, table, null);

                getRootPane().getParent().setVisible(false);
            } else {
                JOptionPane.showMessageDialog(null, "Ingrese el nombre del mesero");
            }

        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        
    }
}
