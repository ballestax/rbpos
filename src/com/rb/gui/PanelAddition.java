package com.rb.gui;

import com.rb.Aplication;
import com.rb.domain.Additional;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.SpinnerNumberModel;
import org.apache.commons.lang3.StringUtils;
import org.dz.PanelCaptura;

/**
 *
 * @author lrod
 */
public class PanelAddition extends PanelCaptura implements ActionListener {

    private final Aplication app;
    private final Additional addition;
    private int lastValue;
    private SpinnerNumberModel spModel;

    /**
     * Creates new form PanelAddition
     *
     * @param app
     * @param addition
     */
    public PanelAddition(Aplication app, Additional addition) {
        this.app = app;
        this.addition = addition;
        lastValue = 1;
        initComponents();
        createComponents();
    }

    private void createComponents() {

        MouseAdapter mouseClick = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                chSel.setSelected(!chSel.isSelected());
                activate(chSel.isSelected());
            }
        };

        setBorder(BorderFactory.createLineBorder(Color.lightGray, 1, true));
        lbPrice.setForeground(Color.blue.darker());
        lbPrice.addMouseListener(mouseClick);

//        lbName.setPreferredSize(new Dimension(160, 20));
//        lbName.setMinimumSize(new Dimension(160, 20));
        lbName.addMouseListener(mouseClick);

        lbName.setText("<html><font size=+1>"+StringUtils.capitalize(addition.getName())+"</font></html>");
        lbName.setToolTipText(StringUtils.capitalize(addition.getName()));
        lbPrice.setToolTipText(StringUtils.capitalize(addition.getName()));
        lbPrice.setText(app.getCurrencyFormat().format(addition.getPrecio()));

        btMinus.setIcon(new ImageIcon(app.getImgManager().getImagen(app.getFolderIcons() + "minus.png", 18, 18)));
        btMinus.setActionCommand(AC_QUANTITY_MINUS
        );
        btMinus.addActionListener(this);

        btAdd.setIcon(new ImageIcon(app.getImgManager().getImagen(app.getFolderIcons() + "plus.png", 18, 18)));
        btAdd.setActionCommand(AC_QUANTITY_ADD);
        btAdd.addActionListener(this);

        //spCant.setFont(new Font("Sans", 14, 1));
        spModel = new SpinnerNumberModel(1, 1, 100, 1);
        spCant.setModel(spModel);

        chSel.setActionCommand(AC_SEL_ADDITION);
        chSel.addActionListener(this);

        activate(false);
    }
    public static final String AC_QUANTITY_MINUS = "AC_QUANTITY_MINUS";
    public static final String AC_QUANTITY_ADD = "AC_QUANTITY_ADD";

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        chSel = new javax.swing.JCheckBox();
        lbName = new javax.swing.JLabel();
        lbPrice = new javax.swing.JLabel();
        spCant = new javax.swing.JSpinner();
        btAdd = new javax.swing.JButton();
        btMinus = new javax.swing.JButton();

        setToolTipText("");

        lbName.setFont(new java.awt.Font("Ubuntu", 0, 15)); // NOI18N
        lbName.setForeground(new java.awt.Color(0, 0, 102));
        lbName.setText("jLabel1");

        lbPrice.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        lbPrice.setText("jLabel2");

        spCant.setFont(new java.awt.Font("Liberation Sans", 1, 18)); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(chSel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lbName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbPrice, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btMinus, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(spCant, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(spCant, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chSel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(lbPrice))
                    .addComponent(btAdd)
                    .addComponent(btMinus))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btAdd, btMinus, spCant});

    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btAdd;
    private javax.swing.JButton btMinus;
    private javax.swing.JCheckBox chSel;
    private javax.swing.JLabel lbName;
    private javax.swing.JLabel lbPrice;
    private javax.swing.JSpinner spCant;
    // End of variables declaration//GEN-END:variables

    @Override
    public void reset() {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (AC_SEL_ADDITION.equals(e.getActionCommand())) {
            activate(chSel.isSelected());
        } else if (AC_QUANTITY_MINUS.equals(e.getActionCommand())) {
            updateSpCantidad(-1);
        } else if (AC_QUANTITY_ADD.equals(e.getActionCommand())) {
            updateSpCantidad(1);
        }
    }

    private void updateSpCantidad(int diff) {

        Integer value = Integer.valueOf(spModel.getValue().toString());
        value += diff;
        if (value >= 1 && value < 100) {
            spModel.setValue(value);
        }
        btMinus.setEnabled(isSelected() && value > 1);
        btAdd.setEnabled(isSelected() && value < 100);

        lastValue = value;
    }

    private void activate(boolean act) {
        lbName.setEnabled(act);
        lbPrice.setEnabled(act);
        spCant.setEnabled(act);
        btAdd.setEnabled(act);
        btMinus.setEnabled(act);
        lastValue = Integer.parseInt(spCant.getValue().toString());
        if (act) {
            spCant.setValue(lastValue);
        } else {
            spCant.setValue(1);
        }

    }

    public Additional getAddition() {
        return addition;
    }

    public int getQuantity() {
        return Integer.parseInt(spCant.getValue().toString());
    }

    public boolean isSelected() {
        
        return chSel.isSelected();
    }

    public static final String AC_SEL_ADDITION = "AC_SEL_ADDITION";

}
