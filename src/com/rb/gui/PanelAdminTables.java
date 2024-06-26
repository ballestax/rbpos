/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rb.gui;

import com.rb.gui.util.MyPopupListener;
import com.rb.Aplication;
import com.rb.GUIManager;
import com.rb.domain.Permission;
import com.rb.domain.Table;
import com.rb.persistence.JDBC.JDBCDAOFactory;
import com.rb.persistence.JDBC.JDBCUtilDAO;
import com.rb.persistence.dao.DAOException;
import com.rb.persistence.dao.DAOFactory;
import com.rb.persistence.dao.RemoteUserResultsInterface;
import com.rb.persistence.dao.UserRetrieveException;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import static javax.swing.BorderFactory.createLineBorder;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import org.dz.MyDefaultTableModel;
import org.dz.MyTableCellRenderer;
import org.dz.PanelCapturaMod;

/**
 *
 * @author ballestax
 */
public class PanelAdminTables extends PanelCapturaMod implements ActionListener, PropertyChangeListener {

    private final Aplication app;
    private MyDefaultTableModel model;
    private JPopupMenu popupTable;
    private MyPopupListener popupListenerTabla;

    public static final String AC_MOD_TABLE = "AC_MOD_TABLE";
    private static final String ACTION_UPDATE = "ACTION_UPDATE";
     private static final String ACTION_UPDATE_TABLE = "ACTION_UPDATE_TABLE";
    private static final String ACTION_NEW_TABLE = "ACTION_NEW_TABLE";
    private JButton btNewTable;
    private JButton btUpdate;

    /**
     * Creates new form PanelAdminUsers
     *
     * @param app
     */
    public PanelAdminTables(Aplication app, PropertyChangeListener pcl) {
        super();
        this.app = app;
        initComponents();
        createComponents();
        addPropertyChangeListener(pcl);

    }

    private void createComponents() {
        toolbar.setFloatable(false);

        btNewTable = new JButton("Nueva Mesa");
        btNewTable.setActionCommand(ACTION_NEW_TABLE);
        btNewTable.addActionListener(this);

        btUpdate = new JButton("Actualizar");
        btUpdate.setActionCommand(ACTION_UPDATE);
        btUpdate.addActionListener(this);

        toolbar.add(btNewTable);
        toolbar.add(btUpdate);

        String[] colNames = {"N°", "Nombre", "Estado", "Modificar"};
        model = new MyDefaultTableModel(colNames, 0);
        tableTables.setModel(model);
        tableTables.setRowHeight(25);

        int[] colW = {20, 100, 70, 50};
        for (int i = 0; i < colW.length; i++) {
            tableTables.getColumnModel().getColumn(i).setMinWidth(colW[i]);
            tableTables.getColumnModel().getColumn(i).setPreferredWidth(colW[i]);
            tableTables.getColumnModel().getColumn(i).setCellRenderer(new TableCellRender(""));
        }
        tableTables.getColumnModel().getColumn(model.getColumnCount() - 1).setCellEditor(new BotonEditor(tableTables, this, AC_MOD_TABLE));
        tableTables.getColumnModel().getColumn(model.getColumnCount() - 1).setCellRenderer(new ButtonCellRenderer("Modificar"));

        popupTable = new JPopupMenu();
        popupListenerTabla = new MyPopupListener(popupTable, true);
        JMenuItem item1 = new JMenuItem("Eliminar");
        item1.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int r = tableTables.getSelectedRow();
                int confirm = JOptionPane.showConfirmDialog(null, "Desea borrar la mesa permanentemente",
                        "Eliminar mesa", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
                if (confirm == JOptionPane.OK_OPTION) {

                    try {
                        String ID = tableTables.getValueAt(r, 0).toString();
                        Table table = app.getControl().getTableByID(Integer.parseInt(ID));
                        if (table != null) {
                            table.setName(table.getName() + "[DEL]");
                            table.setStatus(-1);
                            app.getControl().updateTable(table);
                            pcs.firePropertyChange(PanelNewTable.AC_NEW_TABLE, true, true);
                            loadTables();
                        }
                    } catch (Exception ex) {
                        GUIManager.showErrorMessage(null, "Error al intentar borrar el mesero", "Eliminar mesero");
                    }
                }
            }
        });
        popupTable.add(item1);
        tableTables.addMouseListener(popupListenerTabla);

        loadTables();

    }

    private void loadTables() {
        try {
            ArrayList<Table> tables = ((JDBCUtilDAO) DAOFactory.getInstance().getUtilDAO()).getTablesList("status!=-1", "");

            model.setRowCount(0);
            for (int i = 0; i < tables.size(); i++) {
                Table table = tables.get(i);
                model.addRow(new Object[]{
                    table.getId(),
                    table.getName().toUpperCase(),
                    table.getStatus() == 1 ? "ACTIVA" : "INACTIVA",
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
        tableTables = new javax.swing.JTable();

        toolbar.setRollover(true);

        tableTables.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tableTables);

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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 469, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tableTables;
    private javax.swing.JToolBar toolbar;
    // End of variables declaration//GEN-END:variables

    @Override
    public void actionPerformed(ActionEvent e) {
        if (ACTION_NEW_TABLE.equals(e.getActionCommand())) {
            app.getGuiManager().showNewTable(this, null);
        } else if (ACTION_UPDATE.equals(e.getActionCommand())) {
            loadTables();
        }
    }
   

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (PanelNewTable.AC_NEW_TABLE.equals(evt.getPropertyName())) {
            pcs.firePropertyChange(PanelNewTable.AC_NEW_TABLE, true, true);
            loadTables();
        } else if (PanelNewTable.AC_UPDATE_TABLE.equals(evt.getPropertyName())) {
            pcs.firePropertyChange(PanelNewTable.AC_NEW_TABLE, true, true);
            loadTables();
        }

    }

    public class TableCellRender extends JLabel implements TableCellRenderer {

        private Color color;

        public TableCellRender(String text) {
            setText(text);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Object act = table.getValueAt(row, 2);
            setEnabled(act != null && "ACTIVA".equals(act.toString()));
            if (value != null) {

                setText(value.toString());
            }

            if (isSelected) {
                setForeground(Color.black);
                //setBackground(column == 3 ? color.darker() : Color.white);
                if (hasFocus) {
                    setBorder(BorderFactory.createLineBorder(Color.darkGray));
                } else {
                    setBorder(createLineBorder(Color.lightGray));
                }
            } else {
                setForeground(Color.black);
                //setBackground(column == 3 ? color : Color.white);
                setBorder(UIManager.getBorder("Table.cellBorder"));
            }
            return this;
        }
    }

    public class ButtonCellRenderer extends JButton implements TableCellRenderer {

        public ButtonCellRenderer(String text) {
            setText(text);
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
                String ID = tabla.getModel().getValueAt(row, 0).toString();
                Table table = app.getControl().getTableByID(Integer.parseInt(ID));
                app.getGuiManager().showNewTable(PanelAdminTables.this, table);
            }
            try {
                fireEditingStopped();
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
            }
        }
    }

}
