/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rb.gui;

import com.rb.gui.util.MyPopupListener;
import com.rb.Aplication;
import com.rb.GUIManager;
import com.rb.domain.Waiter;
import com.rb.persistence.JDBC.JDBCUtilDAO;
import com.rb.persistence.dao.DAOException;
import com.rb.persistence.dao.DAOFactory;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.EventObject;
import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import static javax.swing.BorderFactory.createLineBorder;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import org.dz.MyDefaultTableModel;
import org.dz.MyTableCellRenderer;

/**
 *
 * @author ballestax
 */
public class PanelAdminWaiters extends javax.swing.JPanel implements ActionListener, PropertyChangeListener {

    private final Aplication app;
    private MyDefaultTableModel model;
    private JPopupMenu popupTable;
    private MyPopupListener popupListenerTabla;

    public static final String AC_MOD_WAITER = "AC_MOD_WAITER";
    private JButton btUpdate;
    private JButton btNewWaiter;

    /**
     * Creates new form PanelAdminUsers
     *
     * @param app
     */
    public PanelAdminWaiters(Aplication app) {
        this.app = app;
        initComponents();
        createComponents();
    }

    private void createComponents() {
        toolbar.setFloatable(false);

        btNewWaiter = new JButton("Nuevo Mesero");
        btNewWaiter.setActionCommand(ACTION_NEW_WAITER);
        btNewWaiter.addActionListener(this);

        btUpdate = new JButton("Actualizar");
        btUpdate.setActionCommand(ACTION_UPDATE);
        btUpdate.addActionListener(this);
        
        toolbar.add(btNewWaiter);
        toolbar.add(btUpdate);

        String[] colNames = {"ID", "Nombre", "Estado", "Color", "Ver"};
        model = new MyDefaultTableModel(colNames, 0);
        tableWaiters.setModel(model);
        tableWaiters.setRowHeight(30);

        int[] colW = {20, 100, 70, 70, 50};
        for (int i = 0; i < colW.length; i++) {
            tableWaiters.getColumnModel().getColumn(i).setMinWidth(colW[i]);
            tableWaiters.getColumnModel().getColumn(i).setPreferredWidth(colW[i]);
            tableWaiters.getColumnModel().getColumn(i).setCellRenderer(new MyTableCellRenderer(true));
        }
        tableWaiters.getColumnModel().getColumn(model.getColumnCount() - 2).setCellRenderer(new ColorCellRenderer(""));
        tableWaiters.getColumnModel().getColumn(model.getColumnCount() - 1).setCellEditor(new BotonEditor(tableWaiters, this, AC_MOD_WAITER));
        tableWaiters.getColumnModel().getColumn(model.getColumnCount() - 1).setCellRenderer(new ButtonCellRenderer("Modificar"));

        popupTable = new JPopupMenu();
        popupListenerTabla = new MyPopupListener(popupTable, true);
        JMenuItem item1 = new JMenuItem("Eliminar");
        item1.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int r = tableWaiters.getSelectedRow();
                try {
                    String username = tableWaiters.getValueAt(r, 1).toString();

                } catch (Exception ex) {
                    GUIManager.showErrorMessage(null, "Error al intentar borrar el usuario", "Eliminar usuario");
                }

            }
        });
        popupTable.add(item1);
        tableWaiters.addMouseListener(popupListenerTabla);

        loadWaiters();

    }
    private static final String ACTION_UPDATE = "ACTION_UPDATE";
    private static final String ACTION_NEW_WAITER = "ACTION_NEW_WAITER";

    private void loadWaiters() {
        try {
            ArrayList<Waiter> waiters = ((JDBCUtilDAO) DAOFactory.getInstance().getUtilDAO()).getWaitersList("", "");

            model.setRowCount(0);
            for (int i = 0; i < waiters.size(); i++) {
                Waiter waiter = waiters.get(i);
                model.addRow(new Object[]{
                    waiter.getId(),
                    waiter.getName(),
                    waiter.getStatus() == 1 ? "ACTIVO" : "INACTIVO",
                    waiter.getColor(),
                    true
                });
                model.setRowEditable(model.getRowCount() - 1, false);
                model.setCellEditable(model.getRowCount() - 1, 3, true);
            }

        } catch (DAOException ex) {
            System.err.println(ex.getMessage());
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

        toolbar = new javax.swing.JToolBar();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableWaiters = new javax.swing.JTable();

        toolbar.setRollover(true);

        tableWaiters.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tableWaiters);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(toolbar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 531, Short.MAX_VALUE)
                .addGap(13, 13, 13))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(toolbar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1)
                .addGap(14, 14, 14))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tableWaiters;
    private javax.swing.JToolBar toolbar;
    // End of variables declaration//GEN-END:variables

    @Override
    public void actionPerformed(ActionEvent e) {
        if (ACTION_NEW_WAITER.equals(e.getActionCommand())) {
            app.getGuiManager().showNewWaiter(this);
        } else if (ACTION_UPDATE.equals(e.getActionCommand())) {
            loadWaiters();
        }
    }
   

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(PanelNewWaiter.AC_NEW_WAITER)) {

        }
    }

    public class ButtonCellRenderer extends JButton implements TableCellRenderer {

        public ButtonCellRenderer(String text) {
            setText(text);

            setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.yellow), BorderFactory.createEmptyBorder(5, 5, 5, 5)
            ));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

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

    public class ColorCellRenderer extends JLabel implements TableCellRenderer {

        private Color color;

        public ColorCellRenderer(String text) {
            setText(text);
            setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
            color = getColor(text);

            setOpaque(true);
        }

        private Color getColor(String text) {
            Color color = Color.black;
            try {
                color = Color.decode(text);
            } catch (Exception e) {
            }
            return color;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

            if (value != null) {
                color = getColor(value.toString());
            }

            if (isSelected) {
                setForeground(Color.black);
                setBackground(color.darker());
                if (hasFocus) {
                    setBorder(BorderFactory.createLineBorder(Color.darkGray));
                } else {
                    setBorder(createLineBorder(Color.lightGray));
                }
            } else {
                setBackground(color);
                setForeground(Color.black);
                setBorder(UIManager.getBorder("Table.cellBorder"));
            }
            return this;
        }
    }

    public class BotonEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {

        private JTextField campo;
        Boolean currentValue;
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
            currentValue = (Boolean) value;
            return button;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            final int c = tabla.getEditingColumn();
            final int f = tabla.getEditingRow();
            if (f != -1 && c != -1) {
                int row = tabla.convertRowIndexToModel(f);
                String name = tabla.getModel().getValueAt(row, 1).toString();
                // TODO
            }
            try {
                fireEditingStopped();
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
            }
        }
    }

    public class ColorEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {

        private JTextField campo;
        Color currentValue;
        JColorChooser colorChooser;
        protected static final String EDIT = "edit";
        private JTable tabla;
        private ActionListener acList;
        private String acCommand;

        public ColorEditor(JTable tabla, ActionListener listener, String acCommand) {
            colorChooser = new JColorChooser();
            this.tabla = tabla;
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
            currentValue = (Color) value;
            return colorChooser;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            final int c = tabla.getEditingColumn();
            final int f = tabla.getEditingRow();
            if (f != -1 && c != -1) {
                int row = tabla.convertRowIndexToModel(f);
                String name = tabla.getModel().getValueAt(row, 1).toString();
                // TODO
            }
            try {
                fireEditingStopped();
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
            }
        }
    }

}
