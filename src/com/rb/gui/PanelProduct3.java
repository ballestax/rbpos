package com.rb.gui;

import com.rb.Aplication;
import com.rb.Configuration;
import com.rb.domain.ConfigDB;
import com.rb.domain.Presentation;
import com.rb.domain.Product;
import com.rb.domain.ProductoPed;
import static com.rb.gui.PanelCustomPedido.AC_CUSTOM_ADD;
import static com.rb.gui.PanelProduct2.AC_ADD_CUSTOM;
import static com.rb.gui.PanelProduct2.AC_ADD_QUICK;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.beans.PropertyChangeEvent;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import org.dz.PanelCapturaMod;
import org.dz.Utiles;

/**
 *
 * @author lrod
 */
public class PanelProduct3 extends PanelCapturaMod implements ActionListener {

    private final Aplication app;
    private final Product product;
    private JPopupMenu popPres;

    /**
     * Creates new form PanelProduct4
     */
    public PanelProduct3(Aplication app, Product product) {
        this.app = app;
        this.product = product;
        initComponents();
        createComponents();

    }

    private void createComponents() {

        setBorder(BorderFactory.createLineBorder(new Color(112, 12, 44), 2, true));

        String image = product.getImage();

        String path = app.getConfiguration().getProperty(Configuration.PATH_IMG, "img");

        path = Aplication.getDirPics();

        ImageIcon icon = null;
        try {
            icon = new ImageIcon(app.getImgManager().getBufImagen(path + "/" + image, 65, 65));
        } catch (Exception e) {
            icon = new ImageIcon(app.getImgManager().getImagen(app.getFolderIcons() + "no-photo.png", 65, 65));
        }

        Font font1 = new Font("Sans", 1, 14);
        Font font2 = new Font("Serif", 2, 12);                                                                                                                                              
        Font font3 = new Font("Tahoma", 1, 16);

        NumberFormat NF = DecimalFormat.getCurrencyInstance();
        NF.setMaximumFractionDigits(0);

        Color color = new Color(125, 11, 7);

        ConfigDB config = app.getControl().getConfigLocal(Configuration.MAX_LETTERS);
        int valueMaxLetters = config != null ? (int) config.castValor() : 25;

        int MAX_LENGTH = valueMaxLetters;
        String pName = product.getName();
        if (pName.length() > MAX_LENGTH) {
            pName = pName.substring(0, MAX_LENGTH) + "..";
        }

        setBackground(Utiles.colorAleatorio(220, 255));

        lbName.setText(pName.toUpperCase());
        lbName.setToolTipText(product.getName().toUpperCase());
        lbName.setFont(font1);
        lbName.setForeground(Color.blue.darker().darker());
        lbName.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, color),
                BorderFactory.createEmptyBorder(2, 2, 2, 2)
        ));

        lbCategory.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, color),
                BorderFactory.createEmptyBorder(2, 1, 1, 2)));
        lbCategory.setForeground(color.darker().darker());
        lbCategory.setFont(font2);
        lbCategory.setText(product.getCategory().toUpperCase());

        lbPrice.setFont(font3);
        lbPrice.setForeground(Color.red.darker());
        lbPrice.setText(NF.format(product.getPrice()));

//        lbImage.setIcon(icon);
//        lbImage.setBorder(BorderFactory.createLineBorder(color, 1));
//        lbImage.setVisible(false);
        btAddQuick.setActionCommand(PanelProduct2.AC_ADD_QUICK);
        btAddQuick.setMargin(new Insets(1, 1, 1, 1));
        btAddQuick.setFocusPainted(false);
        btAddQuick.setIcon(new ImageIcon(app.getImgManager().getImagen(app.getFolderIcons() + "add1.png", 18, 18)));
        btAddQuick.addActionListener(this);
        btAddQuick.setSize(new Dimension(60, 60));
        btAddQuick.setPreferredSize(new Dimension(60, 60));
        btAddQuick.setMaximumSize(new Dimension(60, 60));

        btAddCustom.setActionCommand(PanelProduct2.AC_ADD_CUSTOM);
        btAddCustom.setMargin(new Insets(1, 1, 1, 1));
        btAddCustom.setFocusPainted(false);
        btAddCustom.setIcon(new ImageIcon(app.getImgManager().getImagen(app.getFolderIcons() + "process-accept.png", 18, 18)));
        btAddCustom.addActionListener(this);
        btAddCustom.setSize(new Dimension(60, 60));
        btAddCustom.setPreferredSize(new Dimension(60, 60));
        btAddCustom.setMaximumSize(new Dimension(60, 60));

        btAddPress.setActionCommand(AC_ADD_PRESS);
        btAddPress.setMargin(new Insets(1, 1, 1, 1));
        btAddPress.setFocusPainted(false);
        btAddPress.setIcon(new ImageIcon(app.getImgManager().getImagen(app.getFolderIcons() + "navigate-down.png", 18, 18)));
        btAddPress.addActionListener(this);
        btAddPress.setSize(new Dimension(60, 60));
        btAddPress.setPreferredSize(new Dimension(60, 60));
        btAddPress.setMaximumSize(new Dimension(60, 60));

        popPres = new JPopupMenu();

        ArrayList<Presentation> presList = app.getControl().getPresentationsByProduct(product.getId());

        for (Presentation press : presList) {
            String html = "<html><font color='#610034'size=4>" + press.getName().toUpperCase() + "</font> [<font color='#3d0021'>" + app.DCFORM_P.format(press.getPrice()) + "</font>]</html>";
            JMenuItem item = new JMenuItem(html);
            item.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(4, 2, 2, 2), BorderFactory.createMatteBorder(0, 0, 1, 0, Color.DARK_GRAY)));
            popPres.add(item);
            item.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ProductoPed prod = new ProductoPed(product);
                    prod.setPresentation(press);
                    //prod.setTermino("");
                    prod.setEspecificaciones("");
                    pcs.firePropertyChange(AC_CUSTOM_ADD, new Object[]{1, press.getPrice()}, prod);
                }
            });
        }

        if (product.isVariablePrice()) {
            List<Double> rankProductsByVarPriceList = app.getControl().getRankProductsByVarPriceList(product.getId(), 6)
                    .stream().sorted().collect(Collectors.toList());

            for (Double price : rankProductsByVarPriceList) {
                if (product.getPrice() == price) {
                    continue;
                }
                String name = price >= 1000 ? (app.DCFORM_W.format(price / 1000) + "K") : String.valueOf(price);
                String html = "<html><font color='#610034'size=4>" + name.toUpperCase() + "</font> [<font color='#3d0021'>" + app.DCFORM_P.format(price) + "</font>]</html>";
                JMenuItem item = new JMenuItem(html);
                item.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(4, 2, 2, 2), BorderFactory.createMatteBorder(0, 0, 1, 0, Color.DARK_GRAY)));
                popPres.add(item);
                item.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        ProductoPed prod = new ProductoPed(product);
                        prod.setPrecio(price);
                        //prod.setTermino("");
                        prod.setEspecificaciones("");
                        pcs.firePropertyChange(AC_CUSTOM_ADD, new Object[]{1, price}, prod);
                    }
                });
            }
            JMenuItem item = new JMenuItem("<html><font color='#61AA34'size=4>" + "OTROS..." + "</font></html>");
            item.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(4, 2, 2, 2), BorderFactory.createMatteBorder(0, 0, 1, 0, Color.DARK_GRAY)));
            item.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    app.getGuiManager().showCustomPedido(product, app);
                }
            });
            popPres.add(item);
        }

        btAddCustom.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    popPres.show(btAddCustom, 0, 0);
                }
            }

        });

    }
    public static final String AC_ADD_PRESS = "AC_ADD_PRESS";

    public void setColor(Color color) {
        setBackground(color);
        updateUI();
    }

    public static int getWordWidth(Font font, String word) {
        FontRenderContext frc = new FontRenderContext(null, true, true);
        GlyphVector gv = font.createGlyphVector(frc, word);
        return (int) gv.getVisualBounds().getWidth();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (AC_ADD_QUICK.equals(e.getActionCommand())) {
            pcs.firePropertyChange(AC_ADD_QUICK, null, product);
        } else if (AC_ADD_CUSTOM.equals(e.getActionCommand())) {
            app.getGuiManager().showCustomPedido(product, app);
        } else if (AC_ADD_PRESS.equals(e.getActionCommand())) {
            popPres.show(btAddPress, 0, btAddPress.getHeight());
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

        lbName = new javax.swing.JLabel();
        lbCategory = new javax.swing.JLabel();
        lbPrice = new javax.swing.JLabel();
        btAddQuick = new javax.swing.JButton();
        btAddCustom = new javax.swing.JButton();
        btAddPress = new javax.swing.JButton();

        setAlignmentY(0.0F);
        setPreferredSize(new java.awt.Dimension(225, 110));

        lbName.setText("jLabel1");

        lbCategory.setText("jLabel2");

        lbPrice.setText("jLabel2");

        btAddQuick.setAlignmentX(0.5F);
        btAddQuick.setPreferredSize(new java.awt.Dimension(40, 40));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbPrice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(24, 24, 24)
                        .addComponent(btAddPress, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btAddCustom, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btAddQuick, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lbCategory, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbName, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addComponent(lbName)
                .addGap(2, 2, 2)
                .addComponent(lbCategory)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(btAddPress, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btAddCustom, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btAddQuick, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btAddCustom, btAddPress, btAddQuick});

    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btAddCustom;
    private javax.swing.JButton btAddPress;
    private javax.swing.JButton btAddQuick;
    private javax.swing.JLabel lbCategory;
    private javax.swing.JLabel lbName;
    private javax.swing.JLabel lbPrice;
    // End of variables declaration//GEN-END:variables

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
