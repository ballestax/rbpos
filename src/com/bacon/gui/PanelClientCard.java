/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bacon.gui;

import com.bacon.Aplication;
import com.bacon.domain.Client;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import org.balx.Utiles;
import org.dz.PanelCaptura;
import org.dzur.StringUtil;
import org.dzur.Util;

/**
 *
 * @author lrod
 */
public class PanelClientCard extends PanelCaptura implements ActionListener {

    private final Aplication app;
    private final int wLabel;
    private Client client;

    /**
     * Creates new form PanelClientCard
     *
     * @param app
     * @param client
     */
    public PanelClientCard(Aplication app, Client client) {
        this.app = app;
        this.client = client;
        wLabel = 65;
        initComponents();
        createComponents();
    }

    private void createComponents() {

        icClient.setIcon(new ImageIcon(app.getImgManager().getImagen(app.getFolderIcons() + "number1.png", 50, 50)));
        icClient.setHorizontalAlignment(SwingConstants.CENTER);

//        Font f1 = new Font("Tahoma", Font.PLAIN, 10);
//        regCelular.setLabelFont(f1);
//        regApellidos.setLabelFont(f1);
//        regNombres.setLabelFont(f1);
        lbTitle.setText("Datos Personales");

        Font f2 = new Font("Tahoma", Font.BOLD, 15);
        regCelular.setFontCampo(f2);
        regApellidos.setFontCampo(f2);
        regNombres.setFontCampo(f2);
        regAddress.setFontCampo(f2);

        ((JTextArea) regAddress.getComponent()).setColumns(20);
        ((JTextArea) regAddress.getComponent()).setLineWrap(true);

        regAddress.requestFocus();
        regAddress.setLabelText("Direccion:");
        jToggleButton1.setText("Datos");
        jToggleButton2.setText("Direcciones");
        jToggleButton3.setText("Pedidos");

        jToggleButton1.setSelected(true);

        chSaveAndExit.setText("Guardar y salir");
        chSaveAndExit.setSelected(true);

        if (client != null) {
            regCelular.setText(client.getCellphone());
            regNombres.setText(client.getNames());
            regApellidos.setText(client.getLastName());
            if (client.getAddresses().size() > 0) {
                regAddress.setText(client.getAddresses().get(0).toString());
            }
            lbStatus.setText("Guardado");
        } else {
            lbStatus.setText("Nuevo");
        }

        jButton1.setText("Guardar");
        jButton1.setActionCommand(AC_SAVE_CLIENT);
        jButton1.addActionListener(this);

        lbStatus.setText("");

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (AC_SAVE_CLIENT.equals(e.getActionCommand())) {
            saveClient();
            pcs.firePropertyChange(AC_SAVE_CLIENT, null, client);
            if (chSaveAndExit.isSelected()) {
                getRootPane().getParent().setVisible(false);
            }
        }
    }

    public Client saveClient() {

        String celular = regCelular.getText();
        String direccion = Util.removerDobleEspacioBlanco(regAddress.getText());
        String nombre = regNombres.getText();
        String apellido = regApellidos.getText();

        boolean validate = true;
        if (celular.isEmpty()) {
            regCelular.setBorderToError();
            validate = false;
        }

        if (direccion.isEmpty()) {
            regAddress.setBorderToError();
            validate = false;
        }

        if (validate) {
            Client client = new Client(celular);
            client.addAddress(direccion.trim().toLowerCase());
            client.setNames(nombre.trim().toLowerCase());
            client.setLastName(apellido.trim().toLowerCase());

            int existClave = app.getControl().existClave("clients", "cellphone", celular);

            if (existClave > 0) {
                app.getControl().updateClient(client);
            } else {
                app.getControl().addClient(client);
            }
            this.client = client;
            return client;

        }
        return null;
    }

    public static final String AC_SAVE_CLIENT = "AC_SAVE_CLIENT";

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        icClient = new javax.swing.JLabel();
        pnContainer = new javax.swing.JPanel();
        regCelular = new com.bacon.gui.util.Registro(BoxLayout.X_AXIS, "Celular", "", wLabel);
        regNombres = new com.bacon.gui.util.Registro(BoxLayout.X_AXIS, "Nombres", "",wLabel);
        regApellidos = new com.bacon.gui.util.Registro(BoxLayout.X_AXIS, "Apellidos", "",wLabel);
        regAddress = new com.bacon.gui.util.Registro(BoxLayout.X_AXIS, "Nombres", new JTextArea(),wLabel);
        jButton1 = new javax.swing.JButton();
        lbStatus = new javax.swing.JLabel();
        chSaveAndExit = new javax.swing.JCheckBox();
        lbTitle = new javax.swing.JLabel();
        jToggleButton1 = new javax.swing.JToggleButton();
        jToggleButton2 = new javax.swing.JToggleButton();
        jToggleButton3 = new javax.swing.JToggleButton();

        pnContainer.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jButton1.setText("jButton1");

        lbStatus.setText("jLabel1");

        javax.swing.GroupLayout pnContainerLayout = new javax.swing.GroupLayout(pnContainer);
        pnContainer.setLayout(pnContainerLayout);
        pnContainerLayout.setHorizontalGroup(
            pnContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnContainerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(regNombres, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(regApellidos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnContainerLayout.createSequentialGroup()
                        .addComponent(regCelular, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 74, Short.MAX_VALUE))
                    .addComponent(regAddress, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnContainerLayout.createSequentialGroup()
                        .addComponent(chSaveAndExit)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1))
                    .addComponent(lbStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnContainerLayout.setVerticalGroup(
            pnContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnContainerLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(regCelular, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(regAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(regNombres, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(regApellidos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbStatus)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(chSaveAndExit))
                .addContainerGap())
        );

        pnContainerLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {regApellidos, regCelular, regNombres});

        lbTitle.setBackground(new java.awt.Color(91, 104, 130));
        lbTitle.setText("jLabel2");
        lbTitle.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lbTitle.setOpaque(true);

        buttonGroup1.add(jToggleButton1);

        buttonGroup1.add(jToggleButton2);

        buttonGroup1.add(jToggleButton3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(icClient, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jToggleButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jToggleButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 1, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(icClient, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jToggleButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jToggleButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 119, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pnContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(13, 13, 13))
        );
    }// </editor-fold>//GEN-END:initComponents

    @Override
    public void reset() {
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JCheckBox chSaveAndExit;
    private javax.swing.JLabel icClient;
    private javax.swing.JButton jButton1;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToggleButton jToggleButton2;
    private javax.swing.JToggleButton jToggleButton3;
    private javax.swing.JLabel lbStatus;
    private javax.swing.JLabel lbTitle;
    private javax.swing.JPanel pnContainer;
    private com.bacon.gui.util.Registro regAddress;
    private com.bacon.gui.util.Registro regApellidos;
    private com.bacon.gui.util.Registro regCelular;
    private com.bacon.gui.util.Registro regNombres;
    // End of variables declaration//GEN-END:variables
}
