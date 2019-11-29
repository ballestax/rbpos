/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bacon.gui;

import com.bacon.Aplication;
import com.bacon.Configuration;
import com.bacon.GUIManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;

/**
 *
 * @author ballestax
 */
public class PanelConfigPrint extends javax.swing.JPanel implements ActionListener {

    public static final String ACTION_APPLY = "ACTION_SAVE";

    private final Aplication app;
    private String exportDIR;

    /**
     * Creates new form PanelConfigMotor
     *
     * @param app
     */
    public PanelConfigPrint(Aplication app) {
        this.app = app;
        initComponents();
        createComponents();
    }

    private void createComponents() {

        lbTitle.setText("Configurar impresion");

        lbInfo.setText("Selecccione la impresora POS");

//        String property = app.getConfiguration().getProperty(Configuration.EXPORT_DIR, Aplication.DEFAULT_EXPORT_DIR);
//        exportDIR = property;

        regDir.setText(exportDIR);

        btBrowse.setText("");
        btBrowse.setIcon(new ImageIcon(app.getImgManager().getImagen(app.getFolderIcons() + "folder-open.png", 18, 18)));
        btBrowse.addActionListener(this);

        btApply.setText("Aplicar");
        btApply.setActionCommand(ACTION_APPLY);
        btApply.addActionListener(this);
    }

    private void openDir(String dir) {

        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new File(dir));
        fc.setMultiSelectionEnabled(false);
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fc.getSelectedFile();
            regDir.setText(selectedFile.getPath());
            exportDIR = selectedFile.getPath();

        }
    }

    private boolean getProperties() {
        String dir = regDir.getText();
        if (Files.exists(Paths.get(dir, ""), LinkOption.NOFOLLOW_LINKS)) {
//            app.getConfiguration().setProperty(Configuration.EXPORT_DIR, dir);
            return true;
        } else {
            String msg = "<html>'" + dir + "'<br> No es un directorio valido</html>";
            GUIManager.showErrorMessage(null, msg, "Advertencia");
            return false;
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        lbTitle = new javax.swing.JLabel();
        lbInfo = new javax.swing.JLabel();
        btBrowse = new javax.swing.JButton();
        regDir = new org.dz.Registro(BoxLayout.X_AXIS, "Directorio","");
        btApply = new javax.swing.JButton();

        lbTitle.setBackground(java.awt.Color.lightGray);
        lbTitle.setOpaque(true);

        lbInfo.setText("jLabel2");
        lbInfo.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btBrowse.setText("Guardar");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lbTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btApply, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(lbInfo, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(regDir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btBrowse)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(lbTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btBrowse)
                    .addComponent(regDir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btApply, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(149, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btBrowse, regDir});

    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btApply;
    private javax.swing.JButton btBrowse;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel lbInfo;
    private javax.swing.JLabel lbTitle;
    private org.dz.Registro regDir;
    // End of variables declaration//GEN-END:variables

    @Override
    public void actionPerformed(ActionEvent e) {
        if (ACTION_APPLY.equals(e.getActionCommand())) {
            if (getProperties()) {
                app.getConfiguration().save();
            }
        }
    }
}
