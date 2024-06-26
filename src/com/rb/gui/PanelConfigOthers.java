/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rb.gui;

import com.rb.Aplication;
import com.rb.Configuration;
import com.rb.domain.ConfigDB;
import com.rb.gui.util.Registro;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import javax.swing.Box;
import javax.swing.BoxLayout;
import org.dz.TextFormatter;

/**
 *
 * @author ballestax
 */
public class PanelConfigOthers extends javax.swing.JPanel implements ActionListener {

    public static final String ACTION_APPLY = "ACTION_SAVE";

    private final Aplication app;
    private String selectedPrinter;
    private String printerName;
    private Registro regDelivery;
    private Registro regDocName;
    private Registro regNumZeros;
    private Registro regPrefix;
    private Registro regAllowFact;
    private Registro regAllowPreview;
    private Registro regShowExclusions;
    private Registro regNumColumnsV1;
    private Registro regNumColumnsV2;
    private Registro regNumCategories;
    private Registro regRowCategories;
    private Registro regAutoSendOrder;
    private Registro regDefServ;
    private Registro regAllowServ;
    private Registro regMaxLetters;
    private Registro regPaperWidth;

    /**
     * Creates new form PanelConfigMotor
     *
     * @param app
     */
    public PanelConfigOthers(Aplication app) {
        this.app = app;
        initComponents();
        createComponents();
        loadData();
    }

    private void createComponents() {

        jScrollPane1.getVerticalScrollBar().setUnitIncrement(22);

        Color color1 = new Color(205, 176, 225);
        Font font = new Font("Sans", 1, 16);

        jPanel1.setLayout(new BoxLayout(jPanel1, BoxLayout.Y_AXIS));

        regDelivery = new Registro(BoxLayout.X_AXIS, "Domicilio", "", 100);
        regDelivery.setBackground(color1);
        regDelivery.setFontCampo(font);
        regDelivery.setDocument(TextFormatter.getDoubleLimiter());
        ConfigCont cCont = new ConfigCont(app);
        cCont.setBackgroundTitle(new Color(200, 210, 220));
        cCont.setTitle("Valor del domicilio");
        cCont.addCampo(regDelivery);
        jPanel1.add(cCont);
        jPanel1.add(Box.createVerticalStrut(5));

        regDocName = new Registro(BoxLayout.X_AXIS, "Documento", "", 100);
        regDocName.setBackground(color1);
        regDocName.setFontCampo(font);
        cCont = new ConfigCont(app);
        cCont.setBackgroundTitle(new Color(200, 210, 220));
        cCont.setTitle("Nombre del documento");
        cCont.addCampo(regDocName);
        jPanel1.add(cCont);
        jPanel1.add(Box.createVerticalStrut(5));
        
        regPaperWidth= new Registro(BoxLayout.X_AXIS, "Ancho", "", 100);
        regPaperWidth.setBackground(color1);
        regPaperWidth.setFontCampo(font);
        regPaperWidth.setDocument(TextFormatter.getIntegerLimiter());
        cCont = new ConfigCont(app);
        cCont.setBackgroundTitle(new Color(200, 210, 220));
        cCont.setTitle("Ancho del papel (caracteres)");
        cCont.addCampo(regPaperWidth);
        jPanel1.add(cCont);
        jPanel1.add(Box.createVerticalStrut(5));

        regNumZeros = new Registro(BoxLayout.X_AXIS, "Ceros", "", 100);
        regNumZeros.setBackground(color1);
        regNumZeros.setFontCampo(font);
        regNumZeros.setDocument(TextFormatter.getIntegerLimiter());
        cCont = new ConfigCont(app);
        cCont.setBackgroundTitle(new Color(200, 210, 220));
        cCont.setTitle("Numero de ceros a formatear el consecutivo");
        cCont.addCampo(regNumZeros);
        jPanel1.add(cCont);
        jPanel1.add(Box.createVerticalStrut(5));

        regAllowServ = new Registro(BoxLayout.X_AXIS, "Permitir", false, 100);
        regAllowServ.setBackground(color1);
        regAllowServ.setFontCampo(font);
        cCont = new ConfigCont(app);
        cCont.setBackgroundTitle(new Color(200, 210, 220));
        cCont.setTitle("Permitir servicio voluntario");
        cCont.addCampo(regAllowServ);
        jPanel1.add(cCont);
        jPanel1.add(Box.createVerticalStrut(5));

        regDefServ = new Registro(BoxLayout.X_AXIS, "% servicio", "", 100);
        regDefServ.setBackground(color1);
        regDefServ.setFontCampo(font);
        regDefServ.setDocument(TextFormatter.getDoubleLimiter());
        cCont = new ConfigCont(app);
        cCont.setBackgroundTitle(new Color(200, 210, 220));
        cCont.setTitle("Porcentaje servicio voluntario");
        cCont.addCampo(regDefServ);
        jPanel1.add(cCont);
        jPanel1.add(Box.createVerticalStrut(5));

        regPrefix = new Registro(BoxLayout.X_AXIS, "Prefijo", "", 100);
        regPrefix.setBackground(color1);
        regPrefix.setFontCampo(font);
        cCont = new ConfigCont(app);
        cCont.setBackgroundTitle(new Color(200, 210, 220));
        cCont.setTitle("Prefijo del consecutivo");
        cCont.addCampo(regPrefix);
        jPanel1.add(cCont);
        jPanel1.add(Box.createVerticalStrut(5));

        regAutoSendOrder = new Registro(BoxLayout.X_AXIS, "Permitir", false, 100);
        regAutoSendOrder.setBackground(color1);
        regAutoSendOrder.setFontCampo(font);
        cCont = new ConfigCont(app);
        cCont.setBackgroundTitle(new Color(200, 210, 220));
        cCont.setTitle("Envio automatico de comandas");
        cCont.addCampo(regAutoSendOrder);
        jPanel1.add(cCont);
        jPanel1.add(Box.createVerticalStrut(5));

        regAllowFact = new Registro(BoxLayout.X_AXIS, "Permitir", false, 100);
        regAllowFact.setBackground(color1);
        regAllowFact.setFontCampo(font);
        cCont = new ConfigCont(app);
        cCont.setBackgroundTitle(new Color(200, 210, 220));
        cCont.setTitle("Permitir facturar sin existencias");
        cCont.addCampo(regAllowFact);
        jPanel1.add(cCont);
        jPanel1.add(Box.createVerticalStrut(5));

        regAllowPreview = new Registro(BoxLayout.X_AXIS, "Permitir", false, 100);
        regAllowPreview.setBackground(color1);
        regAllowPreview.setFontCampo(font);
        cCont = new ConfigCont(app);
        cCont.setBackgroundTitle(new Color(200, 210, 220));
        cCont.setTitle("Permitir imprimir pedido previo");
        cCont.addCampo(regAllowPreview);
        jPanel1.add(cCont);
        jPanel1.add(Box.createVerticalStrut(5));

        regShowExclusions = new Registro(BoxLayout.X_AXIS, "Permitir", false, 100);
        regShowExclusions.setBackground(color1);
        regShowExclusions.setFontCampo(font);
        cCont = new ConfigCont(app);
        cCont.setBackgroundTitle(new Color(200, 210, 220));
        cCont.setTitle("Mostrar exclusiones y notas del producto");
        cCont.addCampo(regShowExclusions);
        jPanel1.add(cCont);
        jPanel1.add(Box.createVerticalStrut(5));

        regNumColumnsV1 = new Registro(BoxLayout.X_AXIS, "Columnas V1", "", 100);
        regNumColumnsV1.setBackground(color1);
        regNumColumnsV1.setFontCampo(font);
        regNumColumnsV1.setDocument(TextFormatter.getIntegerLimiter());

        regNumColumnsV2 = new Registro(BoxLayout.X_AXIS, "Columnas V2", "", 100);
        regNumColumnsV2.setBackground(color1);
        regNumColumnsV2.setFontCampo(font);
        regNumColumnsV2.setDocument(TextFormatter.getIntegerLimiter());

        Box boxHoriz = new Box(BoxLayout.X_AXIS);
        boxHoriz.add(regNumColumnsV1);
        boxHoriz.add(Box.createVerticalStrut(4));
        boxHoriz.add(regNumColumnsV2);

        cCont = new ConfigCont(app);
        cCont.setBackgroundTitle(new Color(200, 210, 220));
        cCont.setTitle("Numero de columnas en el panel pedidos");
        cCont.addCampo(boxHoriz);
        jPanel1.add(cCont);
        jPanel1.add(Box.createVerticalStrut(5));

        regNumCategories = new Registro(BoxLayout.X_AXIS, "Max. Categorias", "", 100);
        regNumCategories.setBackground(color1);
        regNumCategories.setFontCampo(font);
        regNumCategories.setDocument(TextFormatter.getIntegerLimiter());
        cCont = new ConfigCont(app);
        cCont.setBackgroundTitle(new Color(200, 210, 220));
        cCont.setTitle("Numero de categorias a visualizar");
        cCont.addCampo(regNumCategories);
        jPanel1.add(cCont);
        jPanel1.add(Box.createVerticalStrut(5));

        regRowCategories = new Registro(BoxLayout.X_AXIS, "Filas", "", 100);
        regRowCategories.setBackground(color1);
        regRowCategories.setFontCampo(font);
        regRowCategories.setDocument(TextFormatter.getIntegerLimiter());
        cCont = new ConfigCont(app);
        cCont.setBackgroundTitle(new Color(200, 210, 220));
        cCont.setTitle("Numero de filas de categorias a visualizar");
        cCont.addCampo(regRowCategories);
        jPanel1.add(cCont);
        jPanel1.add(Box.createVerticalStrut(5));

        regMaxLetters = new Registro(BoxLayout.X_AXIS, "Filas", "", 100);
        regMaxLetters.setBackground(color1);
        regMaxLetters.setFontCampo(font);
        regMaxLetters.setDocument(TextFormatter.getIntegerLimiter());
        cCont = new ConfigCont(app);
        cCont.setBackgroundTitle(new Color(200, 210, 220));
        cCont.setTitle("Maximo numero de lestras en Card Producto");
        cCont.addCampo(regMaxLetters);
        jPanel1.add(cCont);
        jPanel1.add(Box.createVerticalGlue());
        jPanel1.add(Box.createVerticalStrut(5));

        btApply.setText("Aplicar");
        btApply.setActionCommand(ACTION_APPLY);
        btApply.addActionListener(this);
    }

    private void loadData() {
        ConfigDB config = app.getControl().getConfigGlobal(Configuration.DELIVERY_VALUE);
        double deliveryValue = config != null ? (double) config.castValor() : 0;
        regDelivery.setText(app.getDCFORM_W().format(deliveryValue));

        config = app.getControl().getConfigGlobal(Configuration.DOCUMENT_NAME);
        String docName = config != null ? config.getValor() : "";
        regDocName.setText(docName);

        config = app.getControl().getConfigGlobal(Configuration.ZEROS_INVOICES);
        int zeros = config != null ? (int) config.castValor() : 0;
        regNumZeros.setText(app.getDCFORM_W().format(zeros));

        config = app.getControl().getConfigGlobal(Configuration.PREFIX_INVOICES);
        String prefix = config != null ? config.getValor() : "";
        regPrefix.setText(prefix);

        config = app.getControl().getConfigGlobal(Configuration.PRINT_PREV_DELIVERY);
        boolean showPrev = config != null ? (boolean) config.castValor() : false;
        regAllowPreview.setSelected(showPrev);

        config = app.getControl().getConfigGlobal(Configuration.SHOW_EXCLUSIONS);
        boolean showExclusions = config != null ? (boolean) config.castValor() : false;
        regShowExclusions.setSelected(showExclusions);

        config = app.getControl().getConfigGlobal(Configuration.INVOICE_OUT_STOCK);
        boolean showOutStock = config != null ? (boolean) config.castValor() : false;
        regAllowFact.setSelected(showOutStock);

        config = app.getControl().getConfigLocal(Configuration.NUM_COLUMNS_VIEW1);
        int numColumns1 = config != null ? (int) config.castValor() : 2;
        regNumColumnsV1.setText(app.getDCFORM_W().format(numColumns1));

        config = app.getControl().getConfigLocal(Configuration.NUM_COLUMNS_VIEW2);
        int numColumns2 = config != null ? (int) config.castValor() : 2;
        regNumColumnsV2.setText(app.getDCFORM_W().format(numColumns2));

        config = app.getControl().getConfigLocal(Configuration.MAX_CATEGORIES_LIST);
        int numCategories = config != null ? (int) config.castValor() : 5;
        regNumCategories.setText(app.getDCFORM_W().format(numCategories));

        config = app.getControl().getConfigLocal(Configuration.CATEGORY_ROWS);
        int rowCategories = config != null ? (int) config.castValor() : 1;
        regRowCategories.setText(app.getDCFORM_W().format(rowCategories));

        config = app.getControl().getConfigLocal(Configuration.AUTO_SEND_ORDER_TO_STATIONS);
        boolean autoSendOrder = config != null ? (boolean) config.castValor() : false;
        regAutoSendOrder.setSelected(autoSendOrder);

        config = app.getControl().getConfigGlobal(Configuration.BS_SHOW_SERVICE);
        boolean allowService = config != null ? (boolean) config.castValor() : false;
        regAllowServ.setSelected(allowService);

        config = app.getControl().getConfigGlobal(Configuration.DEF_SERVICE_PORC);
        double defServValue = config != null ? (double) config.castValor() : 0;
        regDefServ.setText(app.getDCFORM_W().format(defServValue));

        config = app.getControl().getConfigLocal(Configuration.MAX_LETTERS);
        int maxLetters = config != null ? (int) config.castValor() : 25;
        regMaxLetters.setText(app.getDCFORM_W().format(maxLetters));
        
        config = app.getControl().getConfigGlobal(Configuration.BS_TICKECT_WIDTH);
        int ticketWidth = config != null ? (int) config.castValor() : 32;
        regPaperWidth.setText(app.getDCFORM_W().format(ticketWidth));

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
        btApply = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        lbInfo = new javax.swing.JLabel();

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 473, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbInfo, javax.swing.GroupLayout.DEFAULT_SIZE, 538, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btApply, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 473, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btApply, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbInfo))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btApply, lbInfo});

    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btApply;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbInfo;
    // End of variables declaration//GEN-END:variables

    @Override
    public void actionPerformed(ActionEvent e) {
        if (ACTION_APPLY.equals(e.getActionCommand())) {
            String value = regDelivery.getText();

            String userName = app.getUser().getUsername();
            String userDevice = Aplication.getUserDevice();

            app.getControl().addConfigOrUpdate(
                    new ConfigDB(com.rb.Configuration.DELIVERY_VALUE, ConfigDB.DOUBLE, value, userName, userDevice, true));

            boolean selected = regAllowPreview.isSelected();
            app.getControl().addConfigOrUpdate(
                    new ConfigDB(com.rb.Configuration.PRINT_PREV_DELIVERY, ConfigDB.BOOLEAN, String.valueOf(selected), userName, userDevice, true));

            boolean selected2 = regShowExclusions.isSelected();
            app.getControl().addConfigOrUpdate(
                    new ConfigDB(com.rb.Configuration.SHOW_EXCLUSIONS, ConfigDB.BOOLEAN, String.valueOf(selected2), userName, userDevice, true));

            boolean selected3 = regAllowFact.isSelected();
            app.getControl().addConfigOrUpdate(
                    new ConfigDB(com.rb.Configuration.INVOICE_OUT_STOCK, ConfigDB.BOOLEAN, String.valueOf(selected3), userName, userDevice, true));

            String prefix = regPrefix.getText();
            app.getControl().addConfigOrUpdate(
                    new ConfigDB(com.rb.Configuration.PREFIX_INVOICES, ConfigDB.STRING, prefix, userName, userDevice, true));

            String ceros = regNumZeros.getText();
            app.getControl().addConfigOrUpdate(
                    new ConfigDB(com.rb.Configuration.ZEROS_INVOICES, ConfigDB.INTEGER, ceros, userName, userDevice, true));

            String docName = regDocName.getText();
            app.getControl().addConfigOrUpdate(
                    new ConfigDB(com.rb.Configuration.DOCUMENT_NAME, ConfigDB.STRING, docName, userName, userDevice, true));

            String columnsV1 = regNumColumnsV1.getText();
            app.getControl().addConfig(
                    new ConfigDB(Configuration.NUM_COLUMNS_VIEW1, ConfigDB.INTEGER, columnsV1, userName, userDevice));

            String columnsV2 = regNumColumnsV2.getText();
            app.getControl().addConfig(
                    new ConfigDB(Configuration.NUM_COLUMNS_VIEW2, ConfigDB.INTEGER, columnsV2, userName, userDevice));

            String categories = regNumCategories.getText();
            app.getControl().addConfig(
                    new ConfigDB(Configuration.MAX_CATEGORIES_LIST, ConfigDB.INTEGER, categories, userName, userDevice));

            String rowCategories = regRowCategories.getText();
            app.getControl().addConfig(
                    new ConfigDB(Configuration.CATEGORY_ROWS, ConfigDB.INTEGER, rowCategories, userName, userDevice));

            boolean selected4 = regAutoSendOrder.isSelected();
            app.getControl().addConfig(
                    new ConfigDB(Configuration.AUTO_SEND_ORDER_TO_STATIONS, ConfigDB.BOOLEAN, String.valueOf(selected4), userName, userDevice));

            boolean selected5 = regAllowServ.isSelected();
            app.getControl().addConfigOrUpdate(
                    new ConfigDB(Configuration.BS_SHOW_SERVICE, ConfigDB.BOOLEAN, String.valueOf(selected5), userName, userDevice, true));

            String serv = regDefServ.getText();
            app.getControl().addConfigOrUpdate(
                    new ConfigDB(com.rb.Configuration.DEF_SERVICE_PORC, ConfigDB.DOUBLE, serv, userName, userDevice, true));

            String maxLetter = regMaxLetters.getText();
            app.getControl().addConfig(
                    new ConfigDB(com.rb.Configuration.MAX_LETTERS, ConfigDB.INTEGER, maxLetter, userName, userDevice));
            
            String ticketWidth = regPaperWidth.getText();
            app.getControl().addConfigOrUpdate(
                    new ConfigDB(com.rb.Configuration.BS_TICKECT_WIDTH, ConfigDB.INTEGER, ticketWidth, userName, userDevice, true));

            String time = Aplication.DF_SQL_TS.format(new Date());
            lbInfo.setText("<html><font color=blue>Ultima configuracion guardada: " + time + "</font></html>");
        }
    }
}
