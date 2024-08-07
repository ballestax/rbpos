/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rb.gui;

import com.rb.Aplication;
import com.rb.Configuration;
import com.rb.GUIManager;
import com.rb.MyConstants;
import com.rb.domain.CashMov;
import com.rb.domain.ConfigDB;
import com.rb.domain.Cycle;
import com.rb.domain.Invoice;
import com.rb.domain.Permission;
import com.rb.domain.Table;
import com.rb.domain.Waiter;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.EventObject;
import java.util.Map;
import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import static javax.swing.BorderFactory.createLineBorder;
import javax.swing.DefaultListSelectionModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import org.apache.log4j.Logger;
import org.dz.MyDefaultTableModel;
import org.dz.PanelCapturaMod;

/**
 *
 * @author lrod
 */
public class PanelCash extends PanelCapturaMod implements ActionListener, ListSelectionListener, PropertyChangeListener {

    private final Aplication app;
    private Cycle cycle;
    private BigDecimal total;
    public static final Logger logger = Logger.getLogger(PanelCash.class.getCanonicalName());
    private MyDefaultTableModel model;
    private MyDefaultTableModel modelExt;
    private ImageIcon icOpen;
    private ImageIcon icClose;
    private JPopupMenu popupTable;
    private com.rb.gui.util.MyPopupListener popupListenerTabla;
    private String colSelection;
    private SwingWorker currentSwingWorker;

    /**
     * Creates new form PanelCash
     *
     * @param app
     * @param cycle
     */
    public PanelCash(Aplication app, Cycle cycle) {
        this.app = app;
        this.cycle = cycle;
        initComponents();
        createComponents();
    }

    private void createComponents() {

        String[] cols = new String[]{"Ticket", "Fecha", "Valor", "Tipo", "Mesa", "Mesero", "Pago", "Ver", "Pagar"};
        model = new MyDefaultTableModel(cols, 0);

        String[] cols2 = new String[]{"Tipo", "Categoria", "Descripcion", "Valor"};
        modelExt = new MyDefaultTableModel(cols2, 0);

        lbFacturas.setText("Facturas");
        lbGastos.setText("Extras");

        total = new BigDecimal(0);

        regFilter1.setLabelText("Pedido");
        regFilter1.setText(new String[]{"--TODOS--", "LOCAL", "DOMICILIO", "PARA LLEVAR"});

        ArrayList<Waiter> waiterslList = app.getControl().getWaitresslList("status=1", "name");
        waiterslList.add(0, new Waiter("--TODOS--", 1));
        regFilter2.setText(waiterslList.toArray());
        regFilter2.setLabelText("Mesero");

        btRefresh.setActionCommand(AC_REFRESH);
        btRefresh.addActionListener(this);
        btRefresh.setIcon(new ImageIcon(app.getImgManager().getImagen(app.getFolderIcons() + "update.png", 32, 32)));

        btOpenCash.setActionCommand(AC_OPEN_CASH);
        btOpenCash.addActionListener(this);
        btOpenCash.setIcon(new ImageIcon(app.getImgManager().getImagen(app.getFolderIcons() + "cashdrawer.png", 32, 32)));

        btAddExtra.setActionCommand(AC_ADD_GASTO);
        btAddExtra.addActionListener(this);
        btAddExtra.setIcon(new ImageIcon(app.getImgManager().getImagen(app.getFolderIcons() + "add1.png", 24, 24)));

        tableInvoices.setModel(model);
        tableInvoices.setRowHeight(24);
        Font f = new Font("Sans", 0, 14);
        int[] colW = new int[]{40, 100, 45, 45, 20, 40, 30, 25, 25};
        for (int i = 0; i < colW.length; i++) {
            tableInvoices.getColumnModel().getColumn(i).setMinWidth(colW[i]);
            tableInvoices.getColumnModel().getColumn(i).setPreferredWidth(colW[i]);
            tableInvoices.getColumnModel().getColumn(i).setCellRenderer(new TablaCellRenderer(true, f));
        }

        popupTable = new JPopupMenu();
        popupListenerTabla = new com.rb.gui.util.MyPopupListener(popupTable, true);
        JMenuItem item1 = new JMenuItem("Pagar");
        item1.addActionListener((ActionEvent e) -> {
            int r = tableInvoices.getSelectedRow();
            String fact = tableInvoices.getValueAt(r, 0).toString();
            Invoice inv = app.getControl().getInvoiceByCode(fact);
            showPayInvoice(inv);
        });
        popupTable.add(item1);

        tableInvoices.addMouseListener(popupListenerTabla);
        tableInvoices.getTableHeader().setReorderingAllowed(false);

        DefaultListSelectionModel selModel = new DefaultListSelectionModel();
        selModel.addListSelectionListener(this);
        tableInvoices.setSelectionModel(selModel);

        tableExtras.setModel(modelExt);
        tableExtras.setRowHeight(24);
        int[] colWE = new int[]{40, 100, 100, 60};
        for (int i = 0; i < colWE.length; i++) {
            tableExtras.getColumnModel().getColumn(i).setMinWidth(colWE[i]);
            tableExtras.getColumnModel().getColumn(i).setPreferredWidth(colWE[i]);
            tableExtras.getColumnModel().getColumn(i).setCellRenderer(new TablaCellRenderer(true));
        }

        TablaCellRenderer rightRenderer = new TablaCellRenderer(true, f);
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);

        ImageIcon icon1 = new ImageIcon(app.getImgManager().getImagen("gui/img/icons/right.png", 16, 16));
        IconCellRenderer iconRenderer = new IconCellRenderer("", icon1);
        tableExtras.getColumnModel().getColumn(0).setCellRenderer(iconRenderer);

        tableInvoices.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
        tableExtras.getColumnModel().getColumn(modelExt.getColumnCount() - 1).setCellRenderer(rightRenderer);

        tableInvoices.getColumnModel().getColumn(model.getColumnCount() - 2).setCellEditor(new BotonEditor(tableInvoices, this, AC_PAY_INVOICE));
        tableInvoices.getColumnModel().getColumn(model.getColumnCount() - 2).setCellRenderer(new ButtonCellRenderer("Pagar"));
        tableInvoices.getColumnModel().getColumn(model.getColumnCount() - 1).setCellEditor(new BotonEditor(tableInvoices, this, AC_REVIEW_INVOICE));
        tableInvoices.getColumnModel().getColumn(model.getColumnCount() - 1).setCellRenderer(new ButtonCellRenderer("Ver"));

        Font font1 = new Font("sans", Font.BOLD, 16);

        Color color1 = new Color(45, 167, 72);
        lbTit1.setText("Ventas");
        lbTit1.setOpaque(true);
        lbTit1.setForeground(color1);
        lbTit1.setBorder(BorderFactory.createLineBorder(color1.darker(), 1, true));
        lbData1.setOpaque(true);
        lbData1.setBorder(BorderFactory.createLineBorder(color1.darker(), 1, true));
        lbData1.setBackground(color1.brighter());
        lbData1.setFont(font1);

        Color color2 = new Color(187, 65, 92);
        lbTit2.setText("Salidas");
        lbTit2.setOpaque(true);
        lbTit2.setForeground(color2);
        lbData2.setOpaque(true);
        lbTit2.setBorder(BorderFactory.createLineBorder(color2.darker(), 1, true));
        lbData2.setBorder(BorderFactory.createLineBorder(color2.darker(), 1, true));
        lbData2.setBackground(color2.brighter());
        lbData2.setFont(font1);

        Color color3 = new Color(75, 102, 197);
        lbTit3.setText("Resultado");
        lbTit3.setOpaque(true);
        lbTit3.setForeground(color3);
        lbTit3.setBorder(BorderFactory.createLineBorder(color3.darker(), 1, true));
        lbData3.setOpaque(true);
        lbData3.setBorder(BorderFactory.createLineBorder(color3.darker(), 1, true));
        lbData3.setBackground(color3.brighter());
        lbData3.setFont(font1);

        Color color4 = new Color(45, 172, 167);
        lbTit4.setText("Inicial");
        lbTit4.setOpaque(true);
        lbTit4.setForeground(color4);
        lbTit4.setBorder(BorderFactory.createLineBorder(color4.darker(), 1, true));
        lbData4.setOpaque(true);
        lbData4.setBorder(BorderFactory.createLineBorder(color4.darker(), 1, true));
        lbData4.setBackground(color4.brighter());
        lbData4.setFont(font1);

        Color color5 = new Color(35, 142, 87);
        lbTit5.setText("Entradas");
        lbTit5.setOpaque(true);
        lbTit5.setForeground(color5);
        lbTit5.setBorder(BorderFactory.createLineBorder(color5.darker(), 1, true));
        lbData5.setOpaque(true);
        lbData5.setBorder(BorderFactory.createLineBorder(color5.darker(), 1, true));
        lbData5.setBackground(color5.brighter());
        lbData5.setFont(font1);

        Color color6 = new Color(175, 92, 147);
        lbTit6.setText("Transf - Bancos");
        lbTit6.setOpaque(true);
        lbTit6.setForeground(color6);
        lbTit6.setBorder(BorderFactory.createLineBorder(color6.darker(), 1, true));
        lbData6.setOpaque(true);
        lbData6.setBorder(BorderFactory.createLineBorder(color6.darker(), 1, true));
        lbData6.setBackground(color6.brighter());
        lbData6.setFont(font1);

        icOpen = new ImageIcon(app.getImgManager().getImagen(app.getFolderIcons() + "open.png", 32, 32));
        icClose = new ImageIcon(app.getImgManager().getImagen(app.getFolderIcons() + "close.png", 32, 32));

        btNewCiclo.setMargin(new Insets(2, 2, 2, 2));
        btNewCiclo.setIcon(icOpen);
        btNewCiclo.setActionCommand(AC_NEW_CYCLE);
        btNewCiclo.addActionListener(this);

        Border bordeOut = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED, color3, color2);
        Border bordeIn = BorderFactory.createEmptyBorder(10, 10, 10, 10);

        jLabel1.setText("Ciclo de caja");

        jLabel2.setBorder(BorderFactory.createCompoundBorder(bordeOut, BorderFactory.createEmptyBorder(2, 8, 2, 8)));

        lbInit.setBorder(BorderFactory.createCompoundBorder(bordeOut, bordeIn));

        lbEnd.setBorder(BorderFactory.createCompoundBorder(bordeOut, bordeIn));

//        populateTabla("");
        lbData1.setText("0");
        lbData2.setText("0");
        lbData3.setText("0");
        lbData4.setText("0");
        lbData5.setText("0");
        lbData6.setText("0");

        ConfigDB config = app.getControl().getConfigGlobal(Configuration.OPEN_CASH);
        btOpenCash.setEnabled(config != null ? (Boolean.valueOf(config.getValor())) : false);

        regFilter1.setActionCommand(AC_FILTER);
        regFilter1.addActionListener(this);

        regFilter2.setActionCommand(AC_FILTER);
        regFilter2.addActionListener(this);
//        regFilter2.setEnabled(false);

        selStatusBar.setOpaque(true);
        selStatusBar.setBorder(BorderFactory.createEtchedBorder());
        selStatusBar.setVisible(false);

        loadCycle();

        loadExtras();

    }
    private static final String AC_PAY_INVOICE = "AC_PAY_INVOICE";
    private static final String AC_REVIEW_INVOICE = "AC_REVIEW_INVOICE";
    private static final String AC_FILTER = "AC_FILTER";

    private void showPayInvoice(Invoice inv) {
        app.getGuiManager().showPanelPayInvoice(inv);
    }

    public void loadCycle() {
        cycle = app.getControl().getLastCycle();
        if (cycle != null) {
            showCycle(cycle);
        }
    }

    private static final String AC_ADD_GASTO = "AC_ADD_GASTO";
    public static final String AC_CLOSE_CYCLE = "AC_CLOSE_CYCLE";
    public static final String AC_NEW_CYCLE = "AC_NEW_CYCLE";
    public static final String AC_NEW_EXPESE_INCOME = "AC_NEW_EXPESE_INCOME";
    public static final String AC_REFRESH = "AC_REFRESH";
    public static final String AC_OPEN_CASH = "AC_OPEN_CASH";

    private void showCycle(Cycle cycle) {
        jLabel2.setText("<html><font color=blue size=5>" + cycle.getId() + "</font></html>");

        Color colorStatus = cycle.getStatus() == 1 ? Color.GREEN : Color.RED;
        lbStatus.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED, colorStatus, colorStatus.darker()));
        lbStatus.setText("<html><font color=green>" + (cycle.getStatus() == 1 ? "Abierto" : "Cerrado") + "</font></html>");
        lbInit.setText("<html>Apertura:<br><font color=red size=3>" + Aplication.DF_FULL3.format(cycle.getInit()) + "</font></html>");
        if (cycle.getEnd() != null) {
            lbEnd.setText("<html>Cierre:<br><font color=green size=3>" + Aplication.DF_FULL3.format(cycle.getEnd()) + "</font></html>");
        } else {
            lbEnd.setText("<html>Cierre:<br><font color=green size=3>" + "" + "</font></html>");
        }

        lbData4.setText("<html><font size=4>" + app.DCFORM_P.format(cycle.getInitialBalance().doubleValue()) + "</font></html>");

        if (cycle.getStatus() == 1) {
//            btNewCiclo.setEnabled(false);
            btNewCiclo.setIcon(icClose);
            btNewCiclo.setActionCommand(AC_CLOSE_CYCLE);
        } else {
//            btNewCiclo.setEnabled(true);
            btNewCiclo.setIcon(icOpen);
            btNewCiclo.setActionCommand(AC_NEW_CYCLE);
        }

        populateTabla("");
        calculateTotals();
    }

    private void populateTabla(String query) {

        String[] TYPE = {"EFECTIVO", "TRANSF", "TARJETA", "COMBO"};

        if (currentSwingWorker != null && !currentSwingWorker.isDone()) {
            currentSwingWorker.cancel(true);
        }

        SwingWorker sw;
        sw = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {

                model.setRowCount(0);

                int selected = regFilter1.getSelected();
                String filter = selected == 0 ? "" : " AND deliveryType=" + selected;

                int idWaiter = 0;
                if (regFilter2.isEnabled()) {
                    Waiter selWaiter = (Waiter) regFilter2.getSelectedItem();
                    idWaiter = selWaiter.getId();
                }

                filter += idWaiter == 0 ? "" : " AND idMesero=" + idWaiter;

                ArrayList<Invoice> invoiceslList = app.getControl().getInvoicesLitelList("ciclo=" + cycle.getId() + filter, "sale_date DESC");

                total = new BigDecimal(0);
                int totalProducts = 0;
                int anuladas = 0;
                int ct = 1;
                for (int i = 0; i < invoiceslList.size(); i++) {
                    Invoice invoice = invoiceslList.get(i);
                    Waiter waiter = app.getControl().getWaitressByID(invoice.getIdWaitress());
                    Table table = app.getControl().getTableByID(invoice.getTable());
                    Map pay = app.getControl().facturaIsPaga(invoice.getFactura());
                    boolean pago = pay != null && !pay.isEmpty();
                    if (invoice.getStatus() != Invoice.ST_ANULADA) {
                        total = total.add(invoice.getValor());
                        totalProducts += invoice.getProducts().size();
//                        servicio += invoice.getValueService();
                        model.addRow(new Object[]{
                            //                            ct++,
                            invoice.getFactura(),
                            Aplication.DF_FULL2.format(invoice.getFecha()),
                            app.DCFORM_P.format(invoice.getValor()),
                            MyConstants.TIPO_PEDIDO[invoice.getTipoEntrega() - 1],
                            //invoice.getIdCliente(),
                            table != null ? table.getName() : "-",
                            waiter != null ? waiter.getName() : "-",
                            pago ? TYPE[Integer.parseInt(pay.get("type").toString())] : "PENDIENTE",
                            pago ? "PAGO" : "PAGAR",
                            "Ver"
                        });

                        model.setRowEditable(model.getRowCount() - 1, false);
                        model.setCellEditable(model.getRowCount() - 1, model.getColumnCount() - 2, true);
                        model.setCellEditable(model.getRowCount() - 1, model.getColumnCount() - 1, true);
                    } else {
                        anuladas++;
                    }
                }

                return true;
            }

            @Override
            protected void done() {
                app.getGuiManager().setDefaultCursor();
                calculateTotals();
            }

        };
        app.getGuiManager().setWaitCursor();
        sw.execute();

        currentSwingWorker = sw;

    }

    private void calculateTotals() {
        long cycle_id = 0L;
        BigDecimal initial = BigDecimal.ZERO;
        if (cycle != null) {
            initial = cycle.getInitialBalance();
            cycle_id = cycle.getId();
        }
        BigDecimal sales = app.getControl().getValueSalesByCycle(cycle_id);
        BigDecimal expenses = app.getControl().getValueExpenseIncomeByCycle(cycle_id, CashMov.EXPENSE);
        BigDecimal incomes = app.getControl().getValueExpenseIncomeByCycle(cycle_id, CashMov.INCOME);
        BigDecimal transfer = app.getControl().getValueTranfersByCycle(cycle_id);
        BigDecimal outcome = initial.add(sales).add(incomes).add(expenses.negate()).subtract(transfer);

        lbData4.setText("<html><font size=4>" + app.DCFORM_P.format(initial) + "</font></html>");

        lbData1.setText("<html><font size=4>" + app.DCFORM_P.format(sales) + "</font></html>");

        lbData3.setText("<html><font size=4>" + app.DCFORM_P.format(outcome) + "</font></html>");

        lbData5.setText("<html><font size=4>" + app.DCFORM_P.format(incomes) + "</font></html>");

        lbData2.setText("<html><font size=4>" + app.DCFORM_P.format(expenses) + "</font></html>");

        lbData6.setText("<html><font size=4>" + app.DCFORM_P.format(transfer) + "</font></html>");

    }

    public void loadExtras() {
        if (cycle != null) {
            modelExt.setRowCount(0);
            ArrayList<CashMov> movs = app.getControl().getExpenseIncomeList("cycle_id=" + cycle.getId(), "eventDate");
            Map<Long, CashMov.Category> categories = app.getControl().getExpensesCategoriesMap("", "");
//        Map<Integer, String> mapCategories = categories.stream().collect(Collectors.toMap(cat -> {cat}); 

            for (CashMov mov : movs) {
                modelExt.addRow(
                        new Object[]{
                            mov.getType() == CashMov.EXPENSE ? PanelAddExtra.STR_EXPENSE : PanelAddExtra.STR_INCOME,
                            categories.get(mov.getIdCategory()).getName().toUpperCase(),
                            mov.getDescription().toUpperCase(),
                            app.DCFORM_P.format(mov.getValue())
                        });
                modelExt.setRowEditable(modelExt.getRowCount() - 1, false);
            }
            calculateTotals();
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (AC_NEW_CYCLE.equals(e.getActionCommand())) {
            app.getGuiManager().showPanelNewCycle(this);

        } else if (AC_CLOSE_CYCLE.equals(e.getActionCommand())) {
            StringBuilder msg = new StringBuilder();
            msg.append("<html>Esta seguro que desea cerrar el ciclo de caja ");
            msg.append("<font color=blue>").append(cycle.getId());
            msg.append(" </font> del ");
            msg.append("<font color=blue>").append(Aplication.DF_FULL.format(cycle.getInit())).append("</font>");
            msg.append("</html>");
            int opt = JOptionPane.showConfirmDialog(null, msg, "Advertencia", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
            if (opt == JOptionPane.OK_OPTION) {
                if (cycle.getStatus() == 1) {
                    cycle.setStatus(0);
                    cycle.setEnd(new Date());
                    app.getControl().saveSnapshotData(cycle);
                    app.getControl().updateCycle(cycle);
                    showCycle(cycle);
                    pulsePinPrinter();

                } else {
                    GUIManager.showErrorMessage(PanelCash.this, "Ya el ciclo esta cerrado", "Ciclo cerrado");
                }
            }

        } else if (AC_REFRESH.equals(e.getActionCommand())) {
            loadCycle();
            loadExtras();
        } else if (AC_ADD_GASTO.equals(e.getActionCommand())) {
            if (cycle.getStatus() == 1) {
                app.getGuiManager().showPanelAddExtra(this);
            } else {
                GUIManager.showErrorMessage(PanelCash.this, "Ya el ciclo esta cerrado", "Ciclo cerrado");
            }
        } else if (AC_FILTER.equals(e.getActionCommand())) {
            int SEL = regFilter1.getSelected();
            if (SEL == 1 || SEL == 0) {
                regFilter2.setEnabled(true);
                regFilter2.setActionCommand(AC_FILTER);
            } else {
                regFilter2.setEnabled(false);
                regFilter2.setActionCommand(null);
                regFilter2.setSelected(0);
            }
            loadCycle();
        } else if (AC_OPEN_CASH.equals(e.getActionCommand())) {
            Permission perm = app.getControl().getPermissionByName(MyConstants.PERM_OPEN_CASH);
            if (!app.getControl().hasPermission(app.getUser(), perm)) {
                GUIManager.showErrorMessage(this, "No tiene permisos para realizar esta accion", "Error de privilegios");
                return;
            } else {
                //app.getGuiManager().showPanelConfirmAccess(this);
                pulsePinPrinter();
            }

        }
    }

    private void pulsePinPrinter() {
        Permission perm = app.getControl().getPermissionByName(MyConstants.PERM_OPEN_CASH);
        if (!app.getControl().hasPermission(app.getUser(), perm)) {
            GUIManager.showErrorMessage(this, "No tiene permisos para abrir la caja", "Error de privilegios");
            return;
        }

        ConfigDB config = app.getControl().getConfigLocal(Configuration.PRINTER_SELECTED);
        if (config == null) {
            GUIManager.showErrorMessage(null, "Impresora no configurada", "Printer error");
        } else {
            String printer = config.getValor();
            app.getPrinterService().sendPulsePin(printer);
            logger.debug("Pulse pin: " + app.getUser() + " : " + new Date());
        }

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (AC_NEW_CYCLE.equals(evt.getPropertyName())) {
            Cycle lastCycle = app.getControl().getLastCycle();
            this.cycle = lastCycle;
            showCycle(cycle);
            pulsePinPrinter();
        } else if (PanelAddExtra.AC_ADD_EXTRA.equals(evt.getPropertyName())) {
            loadExtras();
            pulsePinPrinter();
        } else if (PanelPedido.AC_CONFIRMAR_PEDIDO.equals(evt.getPropertyName())) {
            populateTabla("");
        } else if (PanelPayInvoice.AC_PAY.equals(evt.getPropertyName())) {
            populateTabla("");
            pulsePinPrinter();
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        int[] selectedRows = tableInvoices.getSelectedRows();
        double total = 0, service = 0;

        for (int selectedRow : selectedRows) {
            double valInvoice = 0;
            try {
                valInvoice = app.getDCFORM_P().parse(tableInvoices.getValueAt(selectedRow, 2).toString()).doubleValue();
            } catch (ParseException ex) {
            }
//            double servInvoice = Double.parseDouble(tableInvoices.getValueAt(selectedRow, 9).toString());
            total += valInvoice;
//            service += servInvoice;
        }
        makeStatusLabelSelecteds(selectedRows.length, total, service);
    }

    private void makeStatusLabelSelecteds(int rows, double tot, double serv) {

        if (rows > 0) {
            selStatusBar.setVisible(true);
            selStatusBar.setText("<html>  <font color=" + colSelection + ">Selección</font> [<font color=blue>" + (rows)
                    + "</font> pedidos]  "
                    //                    + "Servicio: <font color=green>" + app.DCFORM_P.format(serv)
                    + "</font> - Total: <font color=blue>" + app.DCFORM_P.format(tot) + "</font></html>");
        } else {
            selStatusBar.setText("");
            selStatusBar.setVisible(false);
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
        jPanel1 = new javax.swing.JPanel();
        btNewCiclo = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        lbInit = new javax.swing.JLabel();
        lbEnd = new javax.swing.JLabel();
        lbStatus = new javax.swing.JLabel();
        btRefresh = new javax.swing.JButton();
        btOpenCash = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableInvoices = new javax.swing.JTable();
        lbFacturas = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        lbTit1 = new javax.swing.JLabel();
        lbData1 = new javax.swing.JLabel();
        lbTit2 = new javax.swing.JLabel();
        lbData2 = new javax.swing.JLabel();
        lbTit3 = new javax.swing.JLabel();
        lbData3 = new javax.swing.JLabel();
        lbTit4 = new javax.swing.JLabel();
        lbData4 = new javax.swing.JLabel();
        lbData5 = new javax.swing.JLabel();
        lbTit5 = new javax.swing.JLabel();
        lbTit6 = new javax.swing.JLabel();
        lbData6 = new javax.swing.JLabel();
        btAddExtra = new javax.swing.JButton();
        lbGastos = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tableExtras = new javax.swing.JTable();
        regFilter2 = new com.rb.gui.util.Registro(1, "Filter1", new String[0]);
        regFilter1 = new com.rb.gui.util.Registro(1, "Filter1", new String[0]);
        selStatusBar = new javax.swing.JLabel();

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setBackground(java.awt.Color.lightGray);
        jLabel1.setOpaque(true);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        lbInit.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lbInit.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(1, 17, 95), new java.awt.Color(10, 18, 180)));

        lbEnd.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lbEnd.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(1, 17, 95), new java.awt.Color(10, 18, 180)));

        lbStatus.setText("Estado");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btNewCiclo, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbInit, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btOpenCash, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btNewCiclo, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbInit, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.CENTER, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(lbStatus)
                            .addComponent(jLabel2)))
                    .addComponent(lbEnd, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator1))
                .addGap(5, 5, 5))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btOpenCash, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel2, lbStatus});

        tableInvoices.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tableInvoices);

        lbFacturas.setBackground(java.awt.Color.gray);
        lbFacturas.setOpaque(true);

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lbTit1.setText("jLabel1");

        lbData1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbData1.setText("jLabel2");

        lbTit2.setText("jLabel1");

        lbData2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbData2.setText("jLabel2");

        lbTit3.setText("jLabel1");

        lbData3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbData3.setText("jLabel2");

        lbTit4.setText("jLabel1");

        lbData4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbData4.setText("jLabel2");

        lbData5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbData5.setText("jLabel2");

        lbTit5.setText("jLabel1");

        lbTit6.setText("jLabel1");

        lbData6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbData6.setText("jLabel2");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lbTit4, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                    .addComponent(lbData4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lbData1, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbTit1, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lbData6, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbTit6, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lbData5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbTit5, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbData2, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbTit2, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lbData3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbTit3, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {lbData1, lbData2, lbData3, lbData4, lbTit1, lbTit2, lbTit3, lbTit4});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lbTit6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbData6, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lbTit5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbData5, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(lbTit3)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(lbData3, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(lbTit2)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(lbData2, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(lbTit1)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(lbData1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lbTit4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbData4, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        lbGastos.setBackground(java.awt.Color.gray);
        lbGastos.setOpaque(true);

        tableExtras.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane3.setViewportView(tableExtras);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lbFacturas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(1, 1, 1)
                                .addComponent(regFilter1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(2, 2, 2)
                                .addComponent(regFilter2, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane1)
                            .addComponent(selStatusBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lbGastos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(1, 1, 1)
                                .addComponent(btAddExtra, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(lbFacturas, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbGastos, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btAddExtra)
                    .addComponent(regFilter2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(regFilter1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 439, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addGap(2, 2, 2)
                        .addComponent(selStatusBar, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btAddExtra, lbFacturas, lbGastos, regFilter1, regFilter2});

    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btAddExtra;
    private javax.swing.JButton btNewCiclo;
    private javax.swing.JButton btOpenCash;
    private javax.swing.JButton btRefresh;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lbData1;
    private javax.swing.JLabel lbData2;
    private javax.swing.JLabel lbData3;
    private javax.swing.JLabel lbData4;
    private javax.swing.JLabel lbData5;
    private javax.swing.JLabel lbData6;
    private javax.swing.JLabel lbEnd;
    private javax.swing.JLabel lbFacturas;
    private javax.swing.JLabel lbGastos;
    private javax.swing.JLabel lbInit;
    private javax.swing.JLabel lbStatus;
    private javax.swing.JLabel lbTit1;
    private javax.swing.JLabel lbTit2;
    private javax.swing.JLabel lbTit3;
    private javax.swing.JLabel lbTit4;
    private javax.swing.JLabel lbTit5;
    private javax.swing.JLabel lbTit6;
    private com.rb.gui.util.Registro regFilter1;
    private com.rb.gui.util.Registro regFilter2;
    private javax.swing.JLabel selStatusBar;
    private javax.swing.JTable tableExtras;
    private javax.swing.JTable tableInvoices;
    // End of variables declaration//GEN-END:variables

    public class BotonEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {

        private JTextField campo;
        Object currentValue;
        JButton button;
        protected static final String EDIT = "edit";
        private JTable tabla;
        private ActionListener acList;
        private String acCommand;

        public BotonEditor(JTable tabla, ActionListener listener, String acCommand) {
            button = new JButton();
            button.setBorderPainted(false);
            this.tabla = tabla;
            this.acList = listener;
            this.acCommand = acCommand;
            button.setActionCommand(acCommand);
            button.addActionListener(BotonEditor.this);

        }

        @Override
        public boolean isCellEditable(EventObject e) {
            if (e instanceof MouseEvent) {
                return ((MouseEvent) e).getClickCount() >= 1;
            }
            return true;
        }

        @Override
        public Object getCellEditorValue() {
            return currentValue;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            currentValue = value;
            return button;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            final int c = tabla.getEditingColumn();
            final int f = tabla.getEditingRow();
            if (f != -1 && c != -1) {
                int row = tabla.convertRowIndexToModel(f);
                String code = model.getValueAt(row, 0).toString();
                Invoice invoice = app.getControl().getInvoiceByCode(code);
                if (AC_PAY_INVOICE.equals(e.getActionCommand())) {
                    showPayInvoice(invoice);
                } else if (AC_REVIEW_INVOICE.equals(e.getActionCommand())) {
                    app.getGuiManager().reviewFacture(invoice);
                }
            }
            try {
                fireEditingStopped();
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
            }
        }
    }

    public class ButtonCellRenderer extends JButton implements TableCellRenderer {

        public ButtonCellRenderer(String text) {
            setText(text);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

            if (value != null) {
                setText(value.toString());
            }
            if (isSelected) {
                setForeground(Color.black);
                setBackground(table.getSelectionBackground());
                if (hasFocus) {
                    setBorder(BorderFactory.createLineBorder(Color.darkGray));
                } else {
                    setBorder(createLineBorder(Color.lightGray));
                }
            } else {
                setBackground(table.getBackground());
                setForeground(Color.black);
                setBorder(UIManager.getBorder("Table.cellBorder"));
            }
            return this;
        }
    }

    public class IconCellRenderer extends JLabel implements TableCellRenderer {

        protected ImageIcon icon1 = new ImageIcon(app.getImgManager().getImagen("gui/img/icons/left_green.png", 16, 16));
        protected ImageIcon icon2 = new ImageIcon(app.getImgManager().getImagen("gui/img/icons/right_red.png", 16, 16));

        public IconCellRenderer(String text, ImageIcon icon) {
            setText(text);
            setIcon(icon);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

            if (value != null) {
                setText(value.toString());
                String text = value.toString();
                setHorizontalAlignment(SwingConstants.LEFT);
                if (PanelAddExtra.STR_EXPENSE.equals(text)) {
                    setIcon(icon2);
                } else {
                    setIcon(icon1);
                }
            }
            if (isSelected) {
                setForeground(Color.black);
                setBackground(table.getSelectionBackground());
                if (hasFocus) {
                    setBorder(BorderFactory.createLineBorder(Color.darkGray));
                } else {
                    setBorder(createLineBorder(Color.lightGray));
                }
            } else {
                setBackground(table.getBackground());
                setForeground(Color.black);
                setBorder(UIManager.getBorder("Table.cellBorder"));
            }
            return this;
        }
    }

}
