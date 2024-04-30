package com.rb.gui;

import com.rb.Aplication;
import com.rb.ProgAction;
import com.rb.domain.Invoice;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import org.dz.PanelCapturaMod;
import org.dz.TextFormatter;

/**
 *
 * @author lrod
 */
public class PanelPayInvoice extends PanelCapturaMod implements ActionListener {

    private final Aplication app;
    private final Invoice invoice;
    private ProgAction acEfecty;
    private double[] billetes = {5000.0, 10000.0, 20000.0, 50000.0, 100000.0};

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
        lbInvoice.setText("Factura");
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

        BigDecimal total = invoice.getValor().add(invoice.getValorDelivery()).add(new BigDecimal(invoice.getValueService()));
        lbValTot.setText(app.getDCFORM_P().format(total));
        lbValTot.setFont(f2);

        regEfect.addActionListener(this);
        regEfect.setActionCommand("AC_EFECTIVO");
        regEfect.setLabelText("Efectivo");
        regEfect.setLabelFont(f1);
        regEfect.setFontCampo(f2);
        regEfect.setTextAligment(SwingConstants.RIGHT);
        regEfect.setDocument(TextFormatter.getDoubleLimiter());
        regEfect.setText(app.getDCFORM_W().format(invoice.getValorTotal().doubleValue()));

        regTransfer.setLabelText("Tranferencias");
        regTransfer.setLabelFont(f1);
        regTransfer.setFontCampo(f2);
        regTransfer.setTextAligment(SwingConstants.RIGHT);
        regTransfer.setDocument(TextFormatter.getDoubleLimiter());
        regTransfer.setText("0");
        regTransfer.addActionListener(this);
        regTransfer.setActionCommand("AC_TRANSFERENCIA");

        regBank.setLabelText("Banco");
        regBank.setLabelFont(f1);
        regBank.setFontCampo(f2);
        regBank.setTextAligment(SwingConstants.RIGHT);
        regBank.setDocument(TextFormatter.getDoubleLimiter());
        regBank.setText(new Object[]{"Nequi", "Bancolombia", "Davivienda", "BBVA", "Datafono", "Otro"});

//        btSwitch.setActionCommand("AC_SWITCH");
//        btSwitch.addActionListener(this);
        btPagar.setText("Pagar");

        btPendiente.setText("Pendiente");

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

        panelValues.setLayout(new FlowLayout(FlowLayout.LEADING));
        panelValues.removeAll();

        Set<Double> valoresPosibles = valoresUnicos(total.doubleValue());
        
        for (Double valoresPosible : valoresPosibles) {
            panelValues.add(new JButton("$"+app.DCFORM_P.format(valoresPosible)));
        }

    }

    private static final double[] BILLETES = {5000, 10000, 20000, 50000, 100000};
    
    public static Set<Double> valoresUnicos(double monto) {
        Set<Double> valores = new HashSet<>();
        calcularValores(monto, 0, new ArrayList<>(), valores);
        return valores;
    }

    private static void calcularValores(double montoRestante, int indiceBillete, List<Double> combinacionActual, Set<Double> valores) {
        if (montoRestante == 0) {
            valores.add(calcularSuma(combinacionActual));
            return;
        }
        if (montoRestante < 0 || indiceBillete >= BILLETES.length) {
            return;
        }

        // Usar el billete actual
        combinacionActual.add(BILLETES[indiceBillete]);
        calcularValores(montoRestante - BILLETES[indiceBillete], indiceBillete, combinacionActual, valores);
        combinacionActual.remove(combinacionActual.size() - 1);

        // Pasar al siguiente billete
        calcularValores(montoRestante, indiceBillete + 1, combinacionActual, valores);
    }

    private static double calcularSuma(List<Double> combinacion) {
        double suma = 0;
        for (double valor : combinacion) {
            suma += valor;
        }
        return suma;
    }

    private void calcularValores(int reg) {
        try {
            double total = invoice.getValorTotal().doubleValue();
            double efectivo = app.getDCFORM_P().parse(regEfect.getText()).doubleValue();
            double transfer = app.getDCFORM_P().parse(regTransfer.getText()).doubleValue();

            switch (reg) {
                case 1:
                    transfer = total - efectivo;
                    regTransfer.setText(String.valueOf(app.getDCFORM_W().format(transfer)));
                    break;
                case 2:
                    efectivo = total - transfer;
                    regEfect.setText(String.valueOf(app.getDCFORM_W().format(efectivo)));
                    break;
            }

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

        lbTitle = new javax.swing.JLabel();
        regEfect = new com.rb.gui.util.Registro(0, "","0",162,162);
        registro2 = new com.rb.gui.util.Registro();
        regTransfer = new com.rb.gui.util.Registro(0, "","0",140);
        btPagar = new javax.swing.JButton();
        lbInvoice = new javax.swing.JLabel();
        lbValSubt = new javax.swing.JLabel();
        lbValVar = new javax.swing.JLabel();
        lbSubt = new javax.swing.JLabel();
        lbVar = new javax.swing.JLabel();
        lbValTot = new javax.swing.JLabel();
        lbTotal = new javax.swing.JLabel();
        btPendiente = new javax.swing.JButton();
        regBank = new com.rb.gui.util.Registro(0,"", new String[1] ,140);
        jCheckBox1 = new javax.swing.JCheckBox();
        lnDevolution = new javax.swing.JLabel();
        lbValDevolution = new javax.swing.JLabel();
        panelValues = new javax.swing.JPanel();

        lbTitle.setBackground(new java.awt.Color(195, 195, 195));
        lbTitle.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbTitle.setText("jLabel1");
        lbTitle.setOpaque(true);

        btPagar.setText("jButton1");

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

        btPendiente.setText("jButton1");

        lnDevolution.setBackground(new java.awt.Color(204, 204, 204));
        lnDevolution.setText("jLabel1");
        lnDevolution.setOpaque(true);

        lbValDevolution.setBackground(new java.awt.Color(153, 255, 153));
        lbValDevolution.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbValDevolution.setText("jLabel1");
        lbValDevolution.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 1, new java.awt.Color(0, 153, 51)));
        lbValDevolution.setOpaque(true);

        javax.swing.GroupLayout panelValuesLayout = new javax.swing.GroupLayout(panelValues);
        panelValues.setLayout(panelValuesLayout);
        panelValuesLayout.setHorizontalGroup(
            panelValuesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        panelValuesLayout.setVerticalGroup(
            panelValuesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 39, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btPendiente)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 340, Short.MAX_VALUE)
                                .addComponent(btPagar))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lbInvoice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(4, 4, 4))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jCheckBox1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(regTransfer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(regBank, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lnDevolution, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(lbValDevolution, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(regEfect, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(registro2, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(panelValues, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lbTotal, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lbVar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lbSubt, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lbValVar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lbValSubt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lbValTot, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addGap(12, 12, 12))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(lbInvoice)
                    .addComponent(lbTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbValSubt, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(lbSubt, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lbValVar, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbVar, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(3, 3, 3)))
                .addGap(5, 5, 5)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lbValTot, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(3, 3, 3)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelValues, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(regEfect, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(regTransfer, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                    .addComponent(jCheckBox1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(registro2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(regBank, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lbValDevolution, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lnDevolution, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(3, 3, 3)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 54, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btPagar)
                            .addComponent(btPendiente))))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {lbInvoice, lbTitle});

    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btPagar;
    private javax.swing.JButton btPendiente;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel lbInvoice;
    private javax.swing.JLabel lbSubt;
    private javax.swing.JLabel lbTitle;
    private javax.swing.JLabel lbTotal;
    private javax.swing.JLabel lbValDevolution;
    private javax.swing.JLabel lbValSubt;
    private javax.swing.JLabel lbValTot;
    private javax.swing.JLabel lbValVar;
    private javax.swing.JLabel lbVar;
    private javax.swing.JLabel lnDevolution;
    private javax.swing.JPanel panelValues;
    private com.rb.gui.util.Registro regBank;
    private com.rb.gui.util.Registro regEfect;
    private com.rb.gui.util.Registro regTransfer;
    private com.rb.gui.util.Registro registro2;
    // End of variables declaration//GEN-END:variables

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        System.out.println(ae.getActionCommand());
        if ("AC_EFECTIVO".equals(ae.getActionCommand())) {
            regEfect.setText(app.DCFORM_P.format(invoice.getValorTotal()));
        } else if ("AC_TRANSFERENCIA".equals(ae.getActionCommand())) {
            regTransfer.setText(app.DCFORM_P.format(invoice.getValorTotal()));
        } else if ("AC_SWITCH".equals(ae.getActionCommand())) {
            String temp = regEfect.getText();
            regEfect.setText(regTransfer.getText());
            regTransfer.setText(temp);

        }
    }
}
