package com.rb.gui;

import static com.rb.gui.PanelPedido.AC_EDITAR_PEDIDO;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListSelectionModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.UIManager;

import java.util.EventObject;
import javax.swing.AbstractCellEditor;

import org.dz.MyDefaultTableModel;
import org.dz.PanelCapturaMod;
import org.dz.Resources;
import org.ocpsoft.prettytime.PrettyTime;

import com.rb.Aplication;
import com.rb.Configuration;
import com.rb.MyConstants;
import com.rb.domain.AdditionalPed;
import com.rb.domain.ConfigDB;
import com.rb.domain.Cycle;
import com.rb.domain.Invoice;
import com.rb.domain.Permission;
import com.rb.domain.ProductoPed;
import com.rb.domain.Waiter;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import static javax.swing.BorderFactory.createLineBorder;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.Timer;

/**
 *
 * @author lrod
 */
public class PanelOrderList extends PanelCapturaMod implements ActionListener, ListSelectionListener {

    private final Aplication app;
    private List<Invoice> orderslList;
    private MyDefaultTableModel model;
    private JLabel labelInfo;
    private JButton btFactura;
    // private final InvoiceController invoiceController;
    private Invoice invoice;
    private JButton btGenInvoice;
    private Timer timer;
    private PrettyTime pTime;
    private Map<String, Integer> mapStatus;

    private static final String AC_FILTER = "AC_FILTER";

    /**
     * Creates new form PanelOrdersList
     *
     * @param app
     */
    public PanelOrderList(Aplication app) {
        this.app = app;
        // invoiceController = new InvoiceController(app);
        initComponents();
        createComponents();
    }

    private void createComponents() {

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_F5) {
                    populateList();
                }
                return false;
            }
        });

        pTime = new PrettyTime(new Locale("es"));

        String[] colNames = {"No", "Pedido", "Valor", "Fecha", "Tiempo", "Estado", "Accion"};
        model = new MyDefaultTableModel(colNames, 0);
        tbOrders.setModel(model);
        tbOrders.setRowHeight(35);

        ImageIcon iconPrint = new ImageIcon(
                app.getImgManager().getImagen(app.getFolderIcons() + "Printer-orange.png", 20, 20));
        ImageIcon iconTickets = new ImageIcon(
                app.getImgManager().getImagen(app.getFolderIcons() + "tickets.png", 20, 20));
        ImageIcon iconCancel = new ImageIcon(
                app.getImgManager().getImagen(app.getFolderIcons() + "cancel.png", 20, 20));
        ImageIcon iconFacturar = new ImageIcon(
                app.getImgManager().getImagen(app.getFolderIcons() + "autoship.png", 20, 20));

        DefaultListSelectionModel selectionModel = new DefaultListSelectionModel();
        selectionModel.addListSelectionListener(this);

        regFilter1.setLabelText("Pedido");
        regFilter1.setText(new String[]{"--TODOS--", "LOCAL", "DOMICILIO", "PARA LLEVAR"});

        ArrayList<Waiter> waiterslList = app.getControl().getWaitresslList("status=1", "name");
        waiterslList.add(0, new Waiter("--TODOS--", 1));
        regFilter2.setText(waiterslList.toArray());
        regFilter2.setLabelText("Mesero");

        regFilter3.setLabelText("Estado");
        mapStatus = IntStream.range(0, Invoice.STATUSES.length)
                .boxed()
                .collect(Collectors.toMap(i -> Invoice.STATUSES[i], i -> i));

//        List<String> STATUSES = new ArrayList<>(Arrays.asList(Invoice.STATUSES));
//        STATUSES.add(0, "--TODOS--");
//        regFilter3.setText(STATUSES.toArray());
        regFilter3.setText(new String[]{"--TODOS--", "NORMAL", "ANULADA", "MODIFICADA", "ENVIADA", "PAGADA"});

        tbOrders.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tbOrders.setSelectionModel(selectionModel);
        tbOrders.getTableHeader().setReorderingAllowed(false);

        // TimeCellRenderer timeRenderer = new TimeCellRenderer();
        int[] colW = new int[]{50, 120, 50, 80, 50, 30, 30};
        for (int i = 0; i < colW.length; i++) {
            tbOrders.getColumnModel().getColumn(i).setMinWidth(colW[i]);
            tbOrders.getColumnModel().getColumn(i).setPreferredWidth(colW[i]);
            tbOrders.getColumnModel().getColumn(i).setCellRenderer(new OrderCellRenderer());
        }

        tbOrders.getColumnModel().getColumn(model.getColumnCount() - 1).setCellEditor(new BotonEditor(tbOrders, this, AC_EDITAR_PEDIDO));
        tbOrders.getColumnModel().getColumn(model.getColumnCount() - 1).setCellRenderer(new ButtonCellRenderer(""));

        labelInfo = new JLabel();

        panelDetail.setLayout(new BorderLayout());

        JPanel panelInfo = new JPanel(new BorderLayout());
        Box boxButtons = new Box(BoxLayout.X_AXIS);

        btGenInvoice = new JButton("Facturar");
        btGenInvoice.setIcon(iconFacturar);
        btGenInvoice.setActionCommand(AC_FACTURAR);
        btGenInvoice.addActionListener(this);

        JButton btCancelar = new JButton("Cancelar");
        btCancelar.setIcon(iconCancel);
        JButton btGuia = new JButton("Guia");
        btGuia.setIcon(iconPrint);
        btFactura = new JButton("Factura");
        btFactura.setIcon(iconPrint);

        JButton btComandas = new JButton("Comandas");
        btComandas.setIcon(iconTickets);

        JComboBox cbComandas = new JComboBox();

        boxButtons.add(btGenInvoice);
        boxButtons.add(Box.createHorizontalStrut(5));
        boxButtons.add(btCancelar);
        boxButtons.add(Box.createHorizontalStrut(5));
        boxButtons.add(btComandas);
        boxButtons.add(Box.createHorizontalStrut(5));
        boxButtons.add(btGuia);
        boxButtons.add(Box.createHorizontalStrut(5));
        boxButtons.add(btFactura);

        boxButtons.setVisible(false);

        JScrollPane scroll = new JScrollPane();
        scroll.setViewportView(labelInfo);

        labelInfo.setVerticalAlignment(SwingConstants.TOP);

        panelInfo.add(scroll, BorderLayout.CENTER);
        panelInfo.add(boxButtons, BorderLayout.SOUTH);

        panelDetail.add(panelInfo);

        splitPane.setLeftComponent(scTableOrders);
        splitPane.setRightComponent(panelDetail);
        
        ConfigDB config = app.getControl().getConfigLocal(Configuration.SPLIT_PANE_ORDERS_LIST);
        String splitPosition = config != null ? config.getValor() : "600";
        int pos = Integer.parseInt(splitPosition);

        splitPane.setDividerLocation(pos);

        splitPane.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                int newPosition = splitPane.getDividerLocation();
                app.getControl().addConfig(
                        new ConfigDB(Configuration.SPLIT_PANE_ORDERS_LIST, ConfigDB.INTEGER, String.valueOf(newPosition), app.getUser().getUsername(), Aplication.getUserDevice()));
            }
        });

        regFilter1.setActionCommand(AC_FILTER);
        regFilter1.addActionListener(this);

        regFilter2.setActionCommand(AC_FILTER);
        regFilter2.addActionListener(this);

        regFilter3.setActionCommand(AC_FILTER);
        regFilter3.addActionListener(this);

        btUpdate.setIcon(new ImageIcon(app.getImgManager().getImagen(app.getFolderIcons() + "update.png", 24, 24)));
        btUpdate.addActionListener(this);
        btUpdate.setActionCommand(AC_UPDATE);

        cbPend.setActionCommand(AC_SHOW_ONLY_PEND);
        cbPend.setText("Solo pend");
        cbPend.addActionListener(this);

        timer = new Timer(1000, e
                -> updateElapsedTime());
        timer.start();

        populateList();

    }
    public static final String AC_SHOW_ONLY_PEND = "AC_SHOW_ONLY_PEND";
    public static final String AC_UPDATE = "AC_UPDATE";

    public static final String AC_FACTURAR = "AC_FACTURAR";

    private void updateElapsedTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        for (int i = 0; i < model.getRowCount(); i++) {
            Date startTime = (Date) model.getValueAt(i, 3);
            String formattedTime = pTime.formatDuration(pTime.calculatePreciseDuration(startTime));
            model.setValueAt(formattedTime, i, 4);
        }
    }

    public void populateList() {

        int selected = regFilter1.getSelected();
        String filter = selected == 0 ? "" : " AND deliveryType=" + selected;

        int status = regFilter3.getSelected();
        String range = cbPend.isSelected() ? " in (0,2,3)" : ">=0";

        String filter3 = " AND " + (status > 0 ? "status=" + (mapStatus.get(regFilter3.getText())) : "status" + range);

        int idWaiter = 0;
        if (regFilter2.isEnabled()) {
            Waiter selWaiter = (Waiter) regFilter2.getSelectedItem();
            idWaiter = selWaiter.getId();
        }

        filter += idWaiter == 0 ? "" : " AND idMesero=" + idWaiter;

        Cycle lastCycle = app.getControl().getLastCycle();
        long idCycyle = lastCycle != null ? lastCycle.getId() : 0;

        orderslList = app.getControl().getInvoiceslList("ciclo=" + idCycyle + filter + filter3, "sale_date DESC");
        model.setRowCount(0);

        app.getGuiManager().setWaitCursor();
        SwingWorker sw = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                for (Invoice order : orderslList) {
                    model.addRow(new Object[]{
                        order.getId(),
                        order,
                        order.getValor(),
                        order.getFecha(),
                        "",//pt.formatDuration(pt.calculatePreciseDuration(order.getFecha())),
                        Invoice.STATUSES[order.getStatus()],
                        ""
                    });
                    model.setRowEditable(model.getRowCount() - 1, false);
                    model.setCellEditable(model.getRowCount() - 1, model.getColumnCount() - 1, true);
//                    model.setCellEditable(model.getRowCount() - 1, model.getColumnCount() - 2, true);
                }
                app.getGuiManager().setDefaultCursor();
                return true;
            }
        };

        sw.execute();
    }

    public void showTable(Invoice order) {
        StringBuilder str = new StringBuilder();
        if (order != null) {

            String color = "#34cdaa";
            Waiter waiter = app.getControl().getWaitressByID(order.getIdWaitress());

            str.append("<html><table width=\"600\" cellspacing=\"0\" border=\"1\">");
            str.append("<tr>");
            str.append("<td bgcolor=").append(color).append(">").append("ID").append("</td>");
            str.append("<td>").append(order.getId()).append("</td></tr>");
            // str.append("<td
            // bgcolor=").append(color).append(">").append("Consecutivo").append("</td>");
            // str.append("<td>").append(order.getConsecutive()).append("</td></tr>");
            if (order.getTipoEntrega() == PanelPedido.TIPO_LOCAL) {
                str.append("<tr><td bgcolor=").append(color).append(">").append("Mesa").append("</td>");
                str.append("<td>").append(order.getTable()).append("</td></tr>");
                str.append("<tr><td bgcolor=").append(color).append(">").append("Mesero").append("</td>");
                str.append("<td>").append(waiter.getName().toUpperCase()).append("</td></tr>");
            } else {
                str.append("<tr><td bgcolor=").append(color).append(">").append("Cliente").append("</td>");
                str.append("<td>").append(order.getIdCliente()).append("</td></tr>");
            }
            str.append("<tr><td bgcolor=").append(color).append(">").append("Fecha").append("</td>");
            str.append("<td>").append(app.DF_FULL3.format(order.getFecha())).append("</td></tr>");
            str.append("<tr><td bgcolor=").append(color).append(">").append("Transcurrido").append("</td>");
            str.append("<td>").append(pTime.formatDuration(pTime.calculatePreciseDuration(order.getFecha())))
                    .append("</td></tr>");
            str.append("</table>");

            str.append("<br><br><br>");

            List<ProductoPed> productos = order.getProducts();
            // StringBuilder str = new StringBuilder();
            str.append("<table width=\"600\" cellspacing=\"0\" border=\"1\">");
            str.append("<html><table width=\"600\" cellspacing=\"0\" border=\"1\">");
            str.append("<tr bgcolor=\"#A4C1FF\">");
            str.append("<td width=\"45%\">").append("Producto").append("</td>");
            // str.append("<td>").append("Codigo").append("</td>");
            str.append("<td width=\"10%\">").append("Cant.").append("</td>");
            str.append("<td width=\"20%\">").append("V. Uni").append("</td>");
            str.append("<td width=\"25%\">").append("V. total").append("</td></tr>");

            double total = 0;

            for (ProductoPed product : productos) {

                int cantidad = product.getCantidad();
                double price = product.getValueAdicionales() + product.getPrecio();
                total += cantidad * price;
                str.append("<tr><td bgcolor=\"#F6FFDB\">").append((product.getPresentation() != null
                        ? (product.getProduct().getName() + " (" + product.getPresentation().getName() + ")")
                        : product.getProduct().getName()).toUpperCase());
                for (AdditionalPed adicional : product.getAdicionales()) {
                    str.append("<br><font color=blue size=2> +").append(adicional.getAdditional().getName())
                            .append("(x").append(adicional.getCantidad()).append(")").append("</font>");
                }

                str.append("<br>").append(product.hasExcluisones() ? "Sin: " : "").append("<font color=red size=2>")
                        .append(product.getStExclusiones()).append("</font></td>");
                // str.append("<td
                // bgcolor=\"#FFFFFF\">").append(product.getProduct().getCode()).append("</td>");
                str.append("<td bgcolor=\"#FFFFFF\" align=\"right\">").append(app.DCFORM_P.format(cantidad))
                        .append("</td>");
                str.append("<td bgcolor=\"#FFFFFF\" align=\"right\">").append(app.DCFORM_P.format(price))
                        .append("</td>");
                str.append("<td bgcolor=\"#FFFFFF\" align=\"right\">").append(app.DCFORM_P.format(cantidad * price))
                        .append("</td>");
                str.append("</tr>");
            }
            str.append("</table></html>");

            labelInfo.setText(str.toString());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (AC_FILTER.equals(e.getActionCommand())) {
            int SEL = regFilter1.getSelected();
            if (SEL == 1 || SEL == 0) {
                regFilter2.setEnabled(true);
                regFilter2.setActionCommand(AC_FILTER);
            } else {
                regFilter2.setEnabled(false);
                regFilter2.setActionCommand(null);
                regFilter2.setSelected(0);
            }
            populateList();

        } else if (AC_EDITAR_PEDIDO.equals(e.getActionCommand())) {

            int r = tbOrders.getSelectedRow();
            String fact = ((Invoice) tbOrders.getValueAt(r, 1)).getFactura();
            Invoice inv = app.getControl().getInvoiceByCode(fact);

            pcs.firePropertyChange(PanelListPedidos.AC_SHOW_INVOICE, inv, null);

            Permission perm = app.getControl().getPermissionByName(MyConstants.PERM_ORDERS_MODULE);
            app.getGuiManager().showBasicPanel(app.getGuiManager().getPanelBasicPedidos(), perm);

        } else if (AC_UPDATE.equals(e.getActionCommand())) {
            populateList();
        } else if (AC_SHOW_ONLY_PEND.equals(e.getActionCommand())) {
            if (cbPend.isSelected()) {
                regFilter3.setText(new String[]{"--TODOS--", "NORMAL", "MODIFICADA", "ENVIADA"});
            } else {
                regFilter3.setText(new String[]{"--TODOS--", "NORMAL", "ANULADA", "MODIFICADA", "ENVIADA", "PAGADA"});
            }
            populateList();
        }

    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        int row = tbOrders.getSelectedRow();
        if (row < 0) {
            showTable(null);
            invoice = null;
        }
        try {
            invoice = (Invoice) model.getValueAt(row, 1);
            showTable(invoice);
        } catch (Exception ex) {
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelDetail = new javax.swing.JPanel();
        scTableOrders = new javax.swing.JScrollPane();
        tbOrders = new javax.swing.JTable();
        panelTop = new javax.swing.JPanel();
        regFilter1 = new com.rb.gui.util.Registro(1, "Filter1", new String[0]);
        regFilter2 = new com.rb.gui.util.Registro(1, "Filter1", new String[0]);
        regFilter3 = new com.rb.gui.util.Registro(1, "Filter1", new String[0]);
        btUpdate = new javax.swing.JButton();
        cbPend = new javax.swing.JCheckBox();
        splitPane = new javax.swing.JSplitPane();
        lbStatus = new javax.swing.JLabel();

        javax.swing.GroupLayout panelDetailLayout = new javax.swing.GroupLayout(panelDetail);
        panelDetail.setLayout(panelDetailLayout);
        panelDetailLayout.setHorizontalGroup(
            panelDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        panelDetailLayout.setVerticalGroup(
            panelDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        tbOrders.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        scTableOrders.setViewportView(tbOrders);

        panelTop.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout panelTopLayout = new javax.swing.GroupLayout(panelTop);
        panelTop.setLayout(panelTopLayout);
        panelTopLayout.setHorizontalGroup(
            panelTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTopLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(regFilter1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(regFilter2, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(regFilter3, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cbPend)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelTopLayout.setVerticalGroup(
            panelTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTopLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(cbPend)
                    .addComponent(btUpdate)
                    .addComponent(regFilter3, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(regFilter2, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(regFilter1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelTopLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btUpdate, regFilter1, regFilter2, regFilter3});

        lbStatus.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(splitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 654, Short.MAX_VALUE)
            .addComponent(panelTop, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lbStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelTop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(splitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 363, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btUpdate;
    private javax.swing.JCheckBox cbPend;
    private javax.swing.JLabel lbStatus;
    private javax.swing.JPanel panelDetail;
    private javax.swing.JPanel panelTop;
    private com.rb.gui.util.Registro regFilter1;
    private com.rb.gui.util.Registro regFilter2;
    private com.rb.gui.util.Registro regFilter3;
    private javax.swing.JScrollPane scTableOrders;
    private javax.swing.JSplitPane splitPane;
    private javax.swing.JTable tbOrders;
    // End of variables declaration//GEN-END:variables

    public class TimeCellRenderer implements TableCellRenderer {

        private BoxContain box;
        private final JLabel lbDate;
        private final JLabel lbTime;

        public DateFormat DF_TIME_FULL = new SimpleDateFormat("HH:mm:ss");

        public TimeCellRenderer() {
            box = new BoxContain(BoxLayout.X_AXIS);

            box.setOpaque(true);

            Border borde = BorderFactory.createLineBorder(Color.red);

            lbDate = new JLabel();
            // lbDate.setBorder(borde);
            lbDate.setOpaque(true);
            lbDate.setPreferredSize(new Dimension(80, 30));

            lbTime = new JLabel();
            lbTime.setOpaque(true);
            // lbTime.setBorder(borde);
            lbTime.setPreferredSize(new Dimension(80, 30));

            box.add(lbDate);
            box.add(Box.createHorizontalStrut(10));
            box.add(Box.createHorizontalGlue());
            box.add(lbTime);

        }

        private void setBackground(Color color) {
            box.setBackground(color);
            lbTime.setBackground(color);
            lbDate.setBackground(color);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {

            if (value != null && value instanceof Date) {
                Date date = (Date) value;
                lbDate.setText(DF_TIME_FULL.format(date));
                PrettyTime pt = new PrettyTime(new Locale("es"));
                lbTime.setText(pt.formatDuration(pt.calculatePreciseDuration(date)));

            }

            if (isSelected) {
                setBackground(new Color(226, 200, 230));
            } else {
                setBackground(Color.white);
            }

            return box;
        }

    }

    public class OrderCellRenderer implements TableCellRenderer {

        JLabel label;

        JButton btn;
        JButton btn2;

        public OrderCellRenderer() {
            label = new JLabel();
            label.setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 3));
            label.setOpaque(true);
            btn = new JButton();
            btn.setPreferredSize(new Dimension(28, 28));
            btn.setMaximumSize(new Dimension(28, 28));
            btn.setIcon(
                    new ImageIcon(Resources.getImagen("gui/img/icons/navigate-down.png", Aplication.class, 20, 20)));
            btn.setActionCommand(AC_DISPLAY_OPTIONS);

            btn2 = new JButton();
            btn2.addActionListener(PanelOrderList.this);
            btn2.setPreferredSize(new Dimension(28, 28));
            btn2.setMaximumSize(new Dimension(28, 28));
            btn2.setIcon(
                    new ImageIcon(Resources.getImagen("gui/img/icons/edit.png", Aplication.class, 20, 20)));
            btn2.setActionCommand(AC_EDITAR_PEDIDO);

        }

        public static final String AC_DISPLAY_OPTIONS = "AC_DISPLAY_OPTIONS";

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {

            if (value != null) {
                switch (column) {
                    case 0:
                        label.setText("<html><font color=blue size=+1>#" + value + "</html>");
                        break;
                    case 1:
                        String html = "<html>Domicilio<br><font color=red>3006052119<font></html>";
                        Invoice order = (Invoice) value;
                        Waiter waiter = app.getControl().getWaitressByID(order.getIdWaitress());
                        if (order.getTipoEntrega() == PanelPedido.TIPO_LOCAL) {
                            html = "<html>Local<br><table width=\"100%\"><tr><td align=\"left\"><font color=red>Mesa: " + order.getTable() + "<font></td>"
                                    + "<td bgcolor=\"#" + waiter.getColor().darker() + "\" align=\"right\"><font color=blue>" + waiter.getName() + "</font></td></tr></table></html>";
                        } else if (order.getTipoEntrega() == PanelPedido.TIPO_DOMICILIO) {
                            html = "<html>Domicilio<br><font color=red>" + order.getIdCliente() + "<font></html>";
                        }
                        label.setText(String.valueOf(html));
                        break;

                    case 2:
                        String format = "0";
                        if (value instanceof BigDecimal) {
                            format = app.DCFORM_P.format((BigDecimal) value);
                        }
                        label.setHorizontalAlignment(SwingConstants.RIGHT);
                        label.setText(String.valueOf(format));

                        break;
                    case 3:
                        String fecha = "0";
                        if (value instanceof Date) {
                            fecha = Aplication.DF_FULL4.format(value);
                        }
                        label.setText(fecha);
                        break;
                    case 4:
                        label.setText(String.valueOf(value));
                        label.setToolTipText(String.valueOf(value));
                        break;
                    default:
                        label.setText(String.valueOf(value));
                        break;
                }
            } else {
                label.setText("");
            }
            if (isSelected) {
                label.setBackground(new Color(226, 200, 230));
            } else {
                label.setBackground(Color.white);
            }

            return label;
        }
    }

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
                if (AC_EDITAR_PEDIDO.equals(e.getActionCommand())) {
                    System.out.println("Editar pedido");
                    int r = tbOrders.getSelectedRow();
                    String fact = ((Invoice) tbOrders.getValueAt(r, 1)).getFactura();
                    Invoice inv = app.getControl().getInvoiceByCode(fact);

                    pcs.firePropertyChange(PanelListPedidos.AC_SHOW_INVOICE, inv, null);

                    Permission perm = app.getControl().getPermissionByName(MyConstants.PERM_ORDERS_MODULE);
                    app.getGuiManager().showBasicPanel(app.getGuiManager().getPanelBasicPedidos(), perm);
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
            setIcon(new ImageIcon(app.getImgManager().getImagen(app.getFolderIcons() + "edit.png", 16, 16)));
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

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
        // Tools | Templates.
    }

}
