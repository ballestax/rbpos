/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rb.gui;

import com.rb.Aplication;
import com.rb.domain.Rol;
import com.rb.domain.Waiter;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.*;
import org.dz.PanelCaptura;
import org.dz.PanelCapturaMod;

/**
 *
 * @author LUISA
 */
public class PanelNewWaiter extends PanelCapturaMod implements ActionListener {

        private final Aplication app;

    public static final String AC_NEW_WAITER = "AC_NEW_WAITER";
    public static final String AC_UPDATE_WAITER = "AC_UPDATE_WAITER";

    private String title;
    private Color color;
    private int mode;
    private Waiter waiter;

    public PanelNewWaiter(Aplication app, Waiter waiter) {
        this.app = app;
        initComponents();
        createComponents();
        mode = 0;
        if (waiter != null) {
            mode = 1; //EDITANDO
            this.waiter = waiter;
            loadWaiter(waiter);
        }
    }

        private void createComponents() {

                ArrayList<Rol> rolesList = app.getControl().getRolesList();
                cbStatus.setModel(new DefaultComboBoxModel(new String[] { "ACTIVO", "INACTIVO" }));

                btColor.setText("");
                btColor.setOpaque(true);
                btColor.setContentAreaFilled(false);
                btColor.setBackground(Color.gray);
                btColor.setBorder(BorderFactory.createLineBorder(Color.red, 1, true));
                btColor.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent ae) {
                                color = JColorChooser.showDialog(btAcept, "Asignar Color", Color.yellow);
                                btColor.setBackground(color);
                        }
                });

                btCancel.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                                getRootPane().getParent().setVisible(false);
                        }
                });

                setTitle("");

                btAcept.setActionCommand(AC_NEW_WAITER);
                btAcept.addActionListener(this);

                // pattern = Pattern.compile(USERNAME_PATTERN);
        }

    public void setTitle(String title) {
        this.title = title;
        jLabel1.setText("Ingresar mesero.");
    }

    public void loadWaiter(Waiter waiter) {
        tfUser.setText(waiter.getName().toUpperCase());
        cbStatus.setSelectedIndex(waiter.getStatus() == 0 ? 1 : 0);
        btColor.setBackground(waiter.getColor());

        btAcept.setActionCommand(AC_UPDATE_WAITER);

    }

        /**
         * This method is called from within the constructor to initialize the form.
         * WARNING: Do NOT modify this code. The content of this method is always
         * regenerated by the Form Editor.
         */
        @SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated
        // Code">//GEN-BEGIN:initComponents
        private void initComponents() {

                jLabel1 = new javax.swing.JLabel();
                jLabel2 = new javax.swing.JLabel();
                btAcept = new javax.swing.JButton();
                btCancel = new javax.swing.JButton();
                jLabel4 = new javax.swing.JLabel();
                tfUser = new javax.swing.JTextField();
                jLabel5 = new javax.swing.JLabel();
                btColor = new javax.swing.JButton();
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

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("         Color:");

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
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btColor, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
                            .addComponent(tfUser)
                            .addComponent(cbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

                layout.linkSize(javax.swing.SwingConstants.HORIZONTAL,
                                new java.awt.Component[] { jLabel2, jLabel4, jLabel5 });

                layout.linkSize(javax.swing.SwingConstants.HORIZONTAL,
                                new java.awt.Component[] { btColor, cbStatus, tfUser });

                layout.setVerticalGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                                .addComponent(jLabel1,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                26,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(2, 2, 2)
                                                                .addGroup(layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(jLabel4)
                                                                                .addComponent(tfUser,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(jLabel2)
                                                                                .addComponent(cbStatus,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addGroup(layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.CENTER)
                                                                                .addComponent(jLabel5)
                                                                                .addComponent(btColor,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                40,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                                                8, Short.MAX_VALUE)
                                                                .addGroup(layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(btCancel,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                31,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(btAcept,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                31,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addContainerGap()));

                layout.linkSize(javax.swing.SwingConstants.VERTICAL,
                                new java.awt.Component[] { btColor, cbStatus, tfUser });

        }// </editor-fold>//GEN-END:initComponents

        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JButton btAcept;
        private javax.swing.JButton btCancel;
        private javax.swing.JButton btColor;
        private javax.swing.JComboBox<String> cbStatus;
        private javax.swing.JLabel jLabel1;
        private javax.swing.JLabel jLabel2;
        private javax.swing.JLabel jLabel4;
        private javax.swing.JLabel jLabel5;
        private javax.swing.JTextField tfUser;
        // End of variables declaration//GEN-END:variables

        @Override
        public void reset() {
                throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods,
                                                                               // choose
                                                                               // Tools | Templates.
        }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (AC_NEW_WAITER.equals(e.getActionCommand())) {
            String name = tfUser.getText();
            int status = cbStatus.getSelectedIndex() == 0 ? 1 : 0;
            if (!name.isEmpty()) {
                Waiter waiter = new Waiter(name, status);
                Color color = btColor.getBackground();
                waiter.setStColor(String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue()));
                app.getControl().addWaiter(waiter);
                pcs.firePropertyChange(AC_NEW_WAITER, waiter, null);

                getRootPane().getParent().setVisible(false);
            } else {
                JOptionPane.showMessageDialog(null, "Ingrese el nombre del mesero");
            }

        } else if (AC_UPDATE_WAITER.equals(e.getActionCommand())) {
            String name = tfUser.getText();
            int status = cbStatus.getSelectedIndex() == 0 ? 1 : 0;
            if (!name.isEmpty()) {
                waiter.setName(name);
                waiter.setStatus(status);
                Color color = btColor.getBackground();
                waiter.setStColor(String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue()));
                app.getControl().updateWaiter(waiter);
                pcs.firePropertyChange(AC_UPDATE_WAITER, waiter, null);

                getRootPane().getParent().setVisible(false);
            } else {
                JOptionPane.showMessageDialog(null, "Ingrese el nombre del mesero");
            }

                }
        }

    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
   
}
