package com.rb.gui;

import com.rb.Aplication;
import com.rb.Configuration;
import com.rb.GUIManager;
import com.rb.ProgAction;
import com.rb.domain.ConfigDB;
import com.rb.domain.Invoice;
import com.rb.domain.Pay;
import static com.rb.domain.Pay.PayType.EFECTY;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import org.dz.PanelCapturaMod;
import org.dz.TextFormatter;

/**
 *
 * @author lrod
 */
public class PanelPayInvoice extends PanelCapturaMod implements ActionListener, CaretListener {

    private final Aplication app;
    private final Invoice invoice;
    private ProgAction acEfecty;
    private double[] billetes = {5000.0, 10000.0, 20000.0, 50000.0, 100000.0};
    private Color COLOR_DEF;
    private Pay.PayType selMethod;
    private double total;
    private double efectivo;
    private double transfer;
    private double cambio;
    private double card;
    private Pay pay;

    /**
     * Creates new form PanelPayInvoice
     *
     * @param app
     * @param invoice
     */
    public PanelPayInvoice(Aplication app, Invoice invoice) {
        this.app = app;
        this.invoice = invoice;

        acEfecty = new ProgAction("Efectivo",
                new ImageIcon(app.getImgManager().getImagen(app.getFolderIcons() + "cash.png", 24, 24)), "Efectivo", 'e') {
            public void actionPerformed(ActionEvent e) {
//                invoice.getValor()
            }
        };

        initComponents();
        createComponents();
    }

    private void createComponents() {

        Font f1 = new Font("Sans serif", 1, 16);
        Font f2 = new Font("Tahoma", 1, 18);

        lbTitle.setText(invoice.getFactura());

        String docName = "TICKET";
        ConfigDB config = app.getControl().getConfig(Configuration.DOCUMENT_NAME);
        if (config != null && !config.getValor().isEmpty()) {
            docName = config.getValor();
        }
        lbInvoice.setText(docName);
        lbInvoice.setFont(f1);
        lbTitle.setForeground(Color.blue.darker());
        lbTitle.setFont(f2);

        lbSubt.setText("Subtotal");
        lbSubt.setFont(f1);
        lbValSubt.setText(app.getDCFORM_P().format(invoice.getValor().doubleValue()));
        lbValSubt.setFont(f2);
        lbVar.setText(invoice.getTipoEntrega() == 1 ? "Servicio" : "Domicilio");
        lbVar.setFont(f1);
        lbValVar.setText(app.getDCFORM_P().format(invoice.getTipoEntrega() == 1 ? invoice.getValueService() : invoice.getValorDelivery().doubleValue()));
        lbValVar.setFont(f2);
        lbTotal.setText("Total");
        lbTotal.setFont(f1);

        lbCambio.setText("Cambio");
        lbCambio.setFont(f1);
        lbValCambio.setText("0");
        lbValCambio.setFont(f2);

        lbMethod.setText("Metodo de pago");
        lbMethod.setFont(f1);

        lbValMethod.setFont(f2);

        lbValCambio.setText("0");
        lbValCambio.setFont(f2);

        BigDecimal total = invoice.getValor().add(invoice.getValorDelivery()).add(new BigDecimal(invoice.getValueService()));
        lbValTot.setText(app.getDCFORM_P().format(total));
        lbValTot.setFont(f2);

        regEfect.addCaretListener(this);
        regEfect.addActionListener(this);
        regEfect.setActionCommand("AC_EFECTIVO");
        regEfect.setLabelText("Efectivo");
        regEfect.setLabelFont(f1);
        regEfect.setFontCampo(f2);
        regEfect.setTextAligment(SwingConstants.RIGHT);
        regEfect.setDocument(TextFormatter.getDoubleLimiter());
        regEfect.setText("0");
//        regEfect.setText(app.getDCFORM_W().format(invoice.getValorTotal().doubleValue()));regEfect.setText(app.getDCFORM_W().format(invoice.getValorTotal().doubleValue()));

        COLOR_DEF = regEfect.getBackground();

        regTransfer.setLabelText("Tranferencias");
        regTransfer.setLabelFont(f1);
        regTransfer.setFontCampo(f2);
        regTransfer.setTextAligment(SwingConstants.RIGHT);
        regTransfer.setDocument(TextFormatter.getDoubleLimiter());
        regTransfer.setText("0");
        regTransfer.addActionListener(this);
        regTransfer.setActionCommand("AC_TRANSFERENCIA");

        regCard.setLabelText("Tarjeta");
        regCard.setLabelFont(f1);
        regCard.setFontCampo(f2);
        regCard.setTextAligment(SwingConstants.RIGHT);
        regCard.setDocument(TextFormatter.getDoubleLimiter());
        regCard.setText("0");
        regCard.addActionListener(this);
        regCard.setActionCommand("AC_CARD");

        regCardType.setLabelText("Tipo de tarjeta");
        regCardType.setLabelFont(f1);
        regCardType.setFontCampo(f2);
        regCardType.setTextAligment(SwingConstants.RIGHT);
        regCardType.setText("0");
        regCardType.addActionListener(this);
        regCardType.setActionCommand("AC_CARD_TYPE");
        regCardType.setText(new Object[]{"Debito", "Credito"});

        regBank.setLabelText("Banco");
        regBank.setLabelFont(f1);
        regBank.setFontCampo(f2);
        regBank.setText(new Object[]{"Nequi", "Bancolombia", "Davivienda", "BBVA", "Otro"});

        btPayMet1.setText("Efectivo");
        btPayMet1.setIcon(new ImageIcon(app.getImgManager().getImagen(app.getFolderIcons() + "cash.png", 20, 20)));
        btPayMet1.addActionListener(this);
        btPayMet1.setActionCommand(AC_SEL_EFECTY);
        btPayMet2.setText("Transferencia");
        btPayMet2.setIcon(new ImageIcon(app.getImgManager().getImagen(app.getFolderIcons() + "us-dollar.png", 20, 20)));
        btPayMet2.addActionListener(this);
        btPayMet2.setActionCommand(AC_SEL_TRANSFER);
        btPayMet3.setText("Tarjeta");
        btPayMet3.setIcon(new ImageIcon(app.getImgManager().getImagen(app.getFolderIcons() + "payment-card.png", 20, 20)));
        btPayMet3.addActionListener(this);
        btPayMet3.setActionCommand(AC_SEL_BANK);
        btPayMet4.setText("Combinado");
        btPayMet4.setIcon(new ImageIcon(app.getImgManager().getImagen(app.getFolderIcons() + "wallet.png", 20, 20)));
        btPayMet4.addActionListener(this);
        btPayMet4.setActionCommand(AC_SEL_COMBO);

//        btSwitch.setActionCommand("AC_SWITCH");
//        btSwitch.addActionListener(this);
        btPagar.setText("Pagar");
        btPagar.setActionCommand(AC_PAY);
        btPagar.addActionListener(this);

        btPayMet1.setSelected(true);
        showMethod(Pay.PayType.EFECTY);

        lbInfo.setVisible(false);
        btPrint.setText("Imprimir");
        btPrint.setActionCommand(AC_PRINT_INVOICE);
        btPrint.addActionListener(this);
        btPrint.setVisible(false);

        lbMethod.setVisible(false);
        lbValMethod.setVisible(false);

        regEfect.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                calcularValores(1);
            }
        });

        regTransfer.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                calcularValores(2);
            }
        });

        regCard.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                calcularValores(3);
            }
        });

        chechInvoiceIsPayed();

        calcularValores(1);

    }
    private static final String AC_PRINT_INVOICE = "AC_PRINT_INVOICE";
    private static final String AC_SEL_COMBO = "AC_SEL_COMBO";
    private static final String AC_SEL_BANK = "AC_SEL_BANK";
    private static final String AC_SEL_TRANSFER = "AC_SEL_TRANSFER";
    private static final String AC_SEL_EFECTY = "AC_SEL_EFECTY";
    private static final String AC_CHECK_TRANSFER = "AC_CHECK_TRANSFER";

    private void calcularValores(int reg) {
        try {
            total = invoice.getValorTotal().doubleValue();
            efectivo = app.getDCFORM_P().parse(regEfect.getText()).doubleValue();
            transfer = app.getDCFORM_P().parse(regTransfer.getText()).doubleValue();
            card = app.getDCFORM_P().parse(regCard.getText()).doubleValue();

            cambio = (efectivo + transfer + card) - total;
            lbValCambio.setForeground(cambio < 0 ? Color.red : Color.BLACK);
            lbValCambio.setText(app.DCFORM_P.format(cambio));

        } catch (Exception e) {
            System.err.println(e.getMessage());
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
        regEfect = new com.rb.gui.util.Registro(0, "","0",242,242);
        regTransfer = new com.rb.gui.util.Registro(0, "","0",242,242);
        lbInvoice = new javax.swing.JLabel();
        lbValSubt = new javax.swing.JLabel();
        lbValVar = new javax.swing.JLabel();
        lbSubt = new javax.swing.JLabel();
        lbVar = new javax.swing.JLabel();
        lbValTot = new javax.swing.JLabel();
        lbTotal = new javax.swing.JLabel();
        regBank = new com.rb.gui.util.Registro(0,"", new String[1] ,242);
        lbCambio = new javax.swing.JLabel();
        lbValCambio = new javax.swing.JLabel();
        btPrint = new javax.swing.JButton();
        btPagar = new javax.swing.JButton();
        btPayMet1 = new javax.swing.JToggleButton();
        btPayMet2 = new javax.swing.JToggleButton();
        btPayMet3 = new javax.swing.JToggleButton();
        btPayMet4 = new javax.swing.JToggleButton();
        regCard = new com.rb.gui.util.Registro(0, "","0",242,242);
        regCardType = new com.rb.gui.util.Registro(0,"", new String[1] ,242);
        lbInfo = new javax.swing.JLabel();
        lbMethod = new javax.swing.JLabel();
        lbValMethod = new javax.swing.JLabel();

        lbTitle.setBackground(new java.awt.Color(195, 195, 195));
        lbTitle.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbTitle.setText("jLabel1");
        lbTitle.setOpaque(true);

        lbInvoice.setBackground(new java.awt.Color(204, 204, 204));
        lbInvoice.setText("jLabel1");
        lbInvoice.setOpaque(true);

        lbValSubt.setBackground(new java.awt.Color(255, 204, 255));
        lbValSubt.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbValSubt.setText("jLabel1");
        lbValSubt.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 1, new java.awt.Color(255, 102, 102)));
        lbValSubt.setOpaque(true);

        lbValVar.setBackground(new java.awt.Color(255, 204, 255));
        lbValVar.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbValVar.setText("jLabel1");
        lbValVar.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 1, new java.awt.Color(255, 102, 102)));
        lbValVar.setOpaque(true);

        lbSubt.setBackground(new java.awt.Color(204, 204, 204));
        lbSubt.setText("jLabel1");
        lbSubt.setOpaque(true);

        lbVar.setBackground(new java.awt.Color(204, 204, 204));
        lbVar.setText("jLabel1");
        lbVar.setOpaque(true);

        lbValTot.setBackground(new java.awt.Color(165, 207, 255));
        lbValTot.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbValTot.setText("jLabel1");
        lbValTot.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 1, new java.awt.Color(0, 153, 153)));
        lbValTot.setOpaque(true);

        lbTotal.setBackground(new java.awt.Color(204, 204, 204));
        lbTotal.setText("jLabel1");
        lbTotal.setOpaque(true);

        lbCambio.setBackground(new java.awt.Color(204, 204, 204));
        lbCambio.setText("jLabel1");
        lbCambio.setOpaque(true);

        lbValCambio.setBackground(new java.awt.Color(153, 255, 153));
        lbValCambio.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbValCambio.setText("jLabel1");
        lbValCambio.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 1, new java.awt.Color(0, 153, 51)));
        lbValCambio.setOpaque(true);

        btPrint.setText("jButton1");

        btPagar.setText("jButton1");

        buttonGroup1.add(btPayMet1);
        btPayMet1.setText("jToggleButton1");

        buttonGroup1.add(btPayMet2);
        btPayMet2.setText("jToggleButton1");

        buttonGroup1.add(btPayMet3);
        btPayMet3.setText("jToggleButton1");

        buttonGroup1.add(btPayMet4);
        btPayMet4.setText("jToggleButton1");

        lbInfo.setBackground(new java.awt.Color(255, 255, 153));
        lbInfo.setText("jLabel1");
        lbInfo.setOpaque(true);

        lbMethod.setBackground(new java.awt.Color(102, 102, 255));
        lbMethod.setText("jLabel1");
        lbMethod.setOpaque(true);

        lbValMethod.setBackground(new java.awt.Color(102, 102, 255));
        lbValMethod.setText("jLabel1");
        lbValMethod.setOpaque(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btPrint)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btPagar, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6))
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(btPayMet1, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btPayMet2, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btPayMet3, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btPayMet4, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(regEfect, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbCambio, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbValCambio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbInvoice, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbSubt, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbVar, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lbValSubt, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lbTitle, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lbValVar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lbValTot, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(regTransfer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(regBank, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(regCard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(regCardType, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbMethod, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(lbValMethod, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {lbCambio, lbInvoice, lbSubt, lbTotal, lbVar});

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btPagar, btPrint});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbInvoice))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbSubt, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbValSubt, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbValVar, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbVar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbValTot, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbMethod, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbValMethod, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btPayMet1, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(btPayMet2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btPayMet3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btPayMet4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(regEfect, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(regTransfer, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(regBank, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(regCard, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(regCardType, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbCambio, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbValCambio, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(btPrint, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btPagar, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {lbCambio, lbInvoice, lbMethod, lbSubt, lbTotal, lbValMethod, lbVar});

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {lbTitle, lbValCambio, lbValSubt, lbValTot, lbValVar});

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btPayMet1, btPayMet2, btPayMet3, btPayMet4});

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {regBank, regCard, regCardType, regEfect, regTransfer});

    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btPagar;
    private javax.swing.JToggleButton btPayMet1;
    private javax.swing.JToggleButton btPayMet2;
    private javax.swing.JToggleButton btPayMet3;
    private javax.swing.JToggleButton btPayMet4;
    private javax.swing.JButton btPrint;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel lbCambio;
    private javax.swing.JLabel lbInfo;
    private javax.swing.JLabel lbInvoice;
    private javax.swing.JLabel lbMethod;
    private javax.swing.JLabel lbSubt;
    private javax.swing.JLabel lbTitle;
    private javax.swing.JLabel lbTotal;
    private javax.swing.JLabel lbValCambio;
    private javax.swing.JLabel lbValMethod;
    private javax.swing.JLabel lbValSubt;
    private javax.swing.JLabel lbValTot;
    private javax.swing.JLabel lbValVar;
    private javax.swing.JLabel lbVar;
    private com.rb.gui.util.Registro regBank;
    private com.rb.gui.util.Registro regCard;
    private com.rb.gui.util.Registro regCardType;
    private com.rb.gui.util.Registro regEfect;
    private com.rb.gui.util.Registro regTransfer;
    // End of variables declaration//GEN-END:variables

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (AC_SEL_EFECTY.equals(ae.getActionCommand())) {
            showMethod(Pay.PayType.EFECTY);
        } else if (AC_SEL_TRANSFER.equals(ae.getActionCommand())) {
            showMethod(Pay.PayType.TRANSFER);
        } else if (AC_SEL_BANK.equals(ae.getActionCommand())) {
            showMethod(Pay.PayType.CARD);
        } else if (AC_SEL_COMBO.equals(ae.getActionCommand())) {
            showMethod(Pay.PayType.COMBO);
        } else if (AC_CLOSE_PANEL.equals(ae.getActionCommand())) {
            getRootPane().getParent().setVisible(false);
        } else if (AC_PAY.equals(ae.getActionCommand())) {
            Pay pay = checkPay();
            if (pay != null) {
                app.getControl().addPay(pay);
                lbInfo.setVisible(true);
                btPagar.setText("Salir");
                btPagar.setActionCommand(AC_CLOSE_PANEL);
                lbInfo.setText("<html><font size=+1 color=blue>Factura paga</font></html>");
                btPrint.setVisible(true);
                
                pcs.firePropertyChange(AC_PAY, null, null);
            }
        } else if (AC_PRINT_INVOICE.equals(ae.getActionCommand())) {
            ConfigDB config = app.getControl().getConfigLocal(Configuration.PRINTER_SELECTED);
            String propPrinter = config != null ? config.getValor() : "";
            app.getPrinterService().imprimirFactura(invoice, pay, propPrinter);
        }

    }

    public void chechInvoiceIsPayed() {
        Map map = app.getControl().facturaIsPaga(invoice.getFactura());
        System.out.println(Arrays.toString(map.values().toArray()));
        if (map != null && !map.isEmpty()) {
            pay = app.getControl().getPay(Integer.parseInt(map.get("id").toString()));
//            System.out.println("pay = " + pay);
            lbValCambio.setText(app.DCFORM_P.format(pay.getCambio()));
            lbInfo.setText("<html><font size=+1 color=blue>Factura paga</font></html>");
            lbInfo.setVisible(true);
            btPagar.setText("Salir");
            btPagar.setActionCommand(AC_CLOSE_PANEL);

            btPayMet1.setVisible(false);
            btPayMet2.setVisible(false);
            btPayMet3.setVisible(false);
            btPayMet4.setVisible(false);

            lbMethod.setVisible(true);
            lbValMethod.setVisible(true);
            lbValMethod.setText(pay.getType().name());

            regEfect.setText(app.DCFORM_W.format(pay.getEfecty()));
            regTransfer.setText(app.DCFORM_W.format(pay.getTransfer()));
            regCard.setText(app.DCFORM_W.format(pay.getCard()));
            regBank.setSelected(pay.getBankTransfer());
            regCardType.setSelected(pay.getCardType() - 1);
            regEfect.setEnabled(true);
            regEfect.setEditable(false);
            regCard.setEnabled(true);
            regCard.setEditable(false);
            regBank.setEnabled(true);
            regBank.setEditable(false);
            regTransfer.setEnabled(true);
            regTransfer.setEditable(false);

            btPrint.setVisible(true);

        }
    }

    private static final String AC_CLOSE_PANEL = "AC_CLOSE_PANEL";
    public static final String AC_PAY = "AC_PAY";

    @Override
    public void reset() {
        regBank.hardReset();
        regCard.hardReset();
        regCardType.hardReset();
        regEfect.hardReset();
        regTransfer.hardReset();
    }

    private void showMethod(Pay.PayType method) {
        switch (method) {
            case EFECTY:
                selMethod = Pay.PayType.EFECTY;
                regEfect.setEnabled(true);
                regEfect.setLabelBackground(LABEL_GRAY);
                regTransfer.setEnabled(false);
                regTransfer.setText("0");
                regTransfer.setLabelBackground(COLOR_DEF);
                regBank.setEnabled(false);
                regBank.reset();
                regBank.setLabelBackground(COLOR_DEF);
                regCard.setEnabled(false);
                regCard.setText("0");
                regCard.setLabelBackground(COLOR_DEF);
                regCardType.setEnabled(false);
                regCardType.reset();
                regCardType.setLabelBackground(COLOR_DEF);
                break;
            case TRANSFER:
                selMethod = Pay.PayType.TRANSFER;
                regEfect.setEnabled(false);
                regEfect.setText("0");
                regEfect.setLabelBackground(COLOR_DEF);
                regTransfer.setEnabled(true);
                regTransfer.setLabelBackground(LABEL_GRAY);
                regBank.setEnabled(true);
                regBank.reset();
                regBank.setLabelBackground(LABEL_GRAY);
                regCard.setEnabled(false);
                regCard.setText("0");
                regCard.setLabelBackground(COLOR_DEF);
                regCardType.setEnabled(false);
                regCardType.reset();
                regCardType.setLabelBackground(COLOR_DEF);
                break;
            case CARD:
                selMethod = Pay.PayType.CARD;
                regEfect.setEnabled(false);
                regEfect.setText("0");
                regEfect.setLabelBackground(COLOR_DEF);
                regTransfer.setEnabled(false);
                regTransfer.setText("0");
                regTransfer.setLabelBackground(COLOR_DEF);
                regBank.setEnabled(false);
                regBank.reset();
                regBank.setLabelBackground(COLOR_DEF);
                regCard.setEnabled(true);
                regCard.setLabelBackground(LABEL_GRAY);
                regCardType.setEnabled(true);
                regCardType.setLabelBackground(LABEL_GRAY);
                break;
            case COMBO:
                selMethod = Pay.PayType.COMBO;
                regEfect.setEnabled(true);
                regEfect.setLabelBackground(LABEL_GRAY);
                regTransfer.setEnabled(true);
                regTransfer.setLabelBackground(LABEL_GRAY);
                regBank.setEnabled(true);
                regBank.setLabelBackground(LABEL_GRAY);
                regCard.setEnabled(true);
                regCard.setLabelBackground(LABEL_GRAY);
                regCardType.setEnabled(true);
                regCardType.setLabelBackground(LABEL_GRAY);
                break;
            default:
                throw new AssertionError();
        }
    }
    private static final Color LABEL_GRAY = new Color(204, 204, 204);

    private Pay checkPay() {
        boolean valido = true;
        Pay pay = null;
        switch (selMethod) {
            case EFECTY:
                if (regEfect.getText().trim().isEmpty()) {
                    regEfect.setBorder(bordeError);
                    valido = false;
                }
                break;
            case TRANSFER:
                if (regTransfer.getText().trim().isEmpty()) {
                    regTransfer.setBorder(bordeError);
                    valido = false;
                }
                if (regBank.getSelected() < 0) {
                    regBank.setBorder(bordeError);
                    valido = false;
                }
                break;
            case CARD:
                if (regCard.getText().trim().isEmpty()) {
                    regCard.setBorder(bordeError);
                    valido = false;
                }
                if (regCardType.getSelected() < 0) {
                    regCardType.setBorder(bordeError);
                    valido = false;
                }
                break;
            case COMBO:
                if (regEfect.getText().trim().isEmpty()) {
                    regEfect.setBorder(bordeError);
                    valido = false;
                }
                if (regTransfer.getText().trim().isEmpty()) {
                    regTransfer.setBorder(bordeError);
                    valido = false;
                }
                if (regBank.getSelected() < 0) {
                    regBank.setBorder(bordeError);
                    valido = false;
                }
                if (regCard.getText().trim().isEmpty()) {
                    regCard.setBorder(bordeError);
                    valido = false;
                }
                if (regCardType.getSelected() < 0) {
                    regCardType.setBorder(bordeError);
                    valido = false;
                }
                break;
            default:
                throw new AssertionError();
        }

        if ((efectivo + transfer + card) < total) {
            GUIManager.showErrorMessage(null, "El monto no cubre el valor total", "Revise la cantidad");
            return null;
        }

        if (valido) {
            pay = new Pay();
            pay.setIdInvoice(invoice.getId());
            pay.setCycle(invoice.getCiclo());
            pay.setType(selMethod.getCode());
            pay.setEfecty(new BigDecimal(efectivo));
            pay.setCard(new BigDecimal(card));
            pay.setTransfer(new BigDecimal(transfer));
            pay.setCambio(new BigDecimal(cambio));
            pay.setUser(app.getUser().getUsername());
            if (regCardType.getSelected() >= 0) {
                pay.setCardType(regCardType.getSelected());
            }
            if (regBank.getSelected() >= 0) {
                pay.setBankTransfer(regBank.getSelectedItem().toString());
            }
            pay.setValue(new BigDecimal(total));

            return pay;
        }
        return null;
    }

    @Override
    public void caretUpdate(CaretEvent ce) {

    }
}
