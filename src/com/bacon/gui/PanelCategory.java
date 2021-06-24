/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bacon.gui;

import com.bacon.domain.Category;
import com.bacon.Aplication;
import com.bacon.domain.Product;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import org.apache.log4j.Logger;
import org.dz.PanelCapturaMod;

/**
 *
 * @author lrod
 */
public class PanelCategory extends PanelCapturaMod implements PropertyChangeListener {

    private final Aplication app;
    private Category category;
    private List<Product> products;
    public static final Logger logger = Logger.getLogger(PanelCategory.class.getCanonicalName());
    private int oldSize;
    private int view;
    private String selectedSort;

    public static final String ORDEN_ID = "ORDEN_ID";
    public static final String ORDEN_ALPHA = "ALFABETICO";
    public static final String ORDEN_PRICE = "PRECIO";
    public static final String ORDEN_RATING = "RATING";
    private HashMap<Long, PanelProduct> mapProdsV2;
    private HashMap<Long, PanelProduct2> mapProdsV1;

    /**
     * Creates new form PanelCategory
     *
     * @param category
     * @param products
     * @param app
     */
    public PanelCategory(Category category, ArrayList products, Aplication app) {
        this.app = app;
        pcs = new PropertyChangeSupport(this);
        this.category = category;
        this.products = products;
        this.selectedSort = null;
        mapProdsV1 = new HashMap<>();
        mapProdsV2 = new HashMap<>();
        initComponents();
        createProductsCard(products);
        createComponents();
    }

    private void createComponents() {
        lbTitle.setText(category.getName());
        lbTitle.setOpaque(true);
        lbTitle.setBorder(BorderFactory.createEtchedBorder());
        lbTitle.setBackground(new Color(84, 36, 0, 130));

        lbTitle.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }
        });

        lbTitle.setVisible(false);

        showView2();

        oldSize = products.size();
    }

    public void setProducts(List<Product> products) {
        this.products = products;
        if (products == null) {
            pnItems.removeAll();
            return;
        }
        if (view == 1) {
            showView1();
        } else {
            showView2();
        }
    }

    private void createProductsCard(List<Product> products) {
        mapProdsV1.clear();
        mapProdsV2.clear();
        products.forEach((product) -> {
            PanelProduct pnProd = new PanelProduct(app, product);
            pnProd.addPropertyChangeListener(app.getGuiManager().getPanelPedido());
            mapProdsV2.put(product.getId(), pnProd);

            PanelProduct2 pnProd2 = new PanelProduct2(app, product);
            pnProd.addPropertyChangeListener(this);
            mapProdsV1.put(product.getId(), pnProd2);

        });

    }

    public void showView2() {
        view = 2;
        app.getGuiManager().setWaitCursor();
        pnItems.removeAll();
        pnItems.setLayout(new GridBagLayout());

        if (products != null) {
            int COLS = 3;
            int LX = products.size() / COLS;
            int LY = (int) Math.ceil(products.size() / COLS);
            int c = 0;
            int i = 0, j = 0;
            for (i = 0; i <= LY; i++) {
                for (j = 0; j < COLS; j++) {
                    if (c >= products.size()) {
                        break;
                    }
                    PanelProduct pnProd = mapProdsV2.get(products.get(c).getId());
                    pnItems.add(pnProd,
                            new GridBagConstraints(j, i, 1, 1,
                                    0.1, 0,
                                    GridBagConstraints.NORTH,
                                    GridBagConstraints.HORIZONTAL,
                                    new Insets(2, 2, 2, 2),
                                    0, 0));
                    c++;
                }
            }
            pnItems.add(Box.createVerticalGlue(),
                    new GridBagConstraints(j + 1, i, 1, 1,
                            0, 0.1,
                            GridBagConstraints.SOUTH,
                            GridBagConstraints.BOTH,
                            new Insets(1, 1, 1, 1),
                            1, 1));

        }
        pnItems.updateUI();
        app.getGuiManager().setDefaultCursor();
    }

    public void showView1() {
        view = 1;
        app.getGuiManager().setWaitCursor();
        pnItems.removeAll();
        pnItems.setLayout(new GridLayout(0, 2, 10, 10));

        if (products != null) {
            for (int i = 0; i < products.size(); i++) {
                PanelProduct2 pnProd2 = mapProdsV1.get(products.get(i).getId());
                pnItems.add(pnProd2);
            }
        }

        pnItems.add(Box.createVerticalGlue());
        pnItems.add(Box.createVerticalGlue());
        pnItems.updateUI();
        app.getGuiManager().setDefaultCursor();
    }

    public void resizePanel() {

        if (pnItems != null) {
            int width = pnItems.getWidth();

            if (oldSize != products.size()) {
                if (width > 0) {
                    int h = 120 * ((products.size() + 1) / 2);

                    pnItems.setSize(width, h);
                }
            }
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        if (null != evt.getPropertyName()) {
            switch (evt.getPropertyName()) {
                case PanelProduct2.AC_ADD_QUICK:
                    pcs.firePropertyChange(evt.getPropertyName(), null, evt.getNewValue());
                    break;
                case PanelTopSearch.AC_SELECT_VIEW1:
                    showView1();
                    break;
                case PanelTopSearch.AC_SELECT_VIEW2:
                    showView2();
                    break;
                case PanelTopSearch.AC_CHANGE_SORT:
                    changeSort(evt.getNewValue().toString());
                    break;
                case PanelTopSearch.AC_REFRESH_PRODUCTS:
                    System.out.println("Refreshin products");
                    this.products = loadProductsFromDB(getSelectedSort());
                    createProductsCard(products);
                    setProducts(products);
                    break;                    
                default:
                    break;
            }
        }
    }

    public void changeSort(String sort) {        
        if (selectedSort==null  || !selectedSort.equals(sort)) {
            setProducts(loadProductsFromDB(sort));
        }
    }

    private List<Product> loadProductsFromDB(String sort) {
        selectedSort = sort;
        List<Product> listProducts;
        if (selectedSort.equalsIgnoreCase(ORDEN_ALPHA)) {
            listProducts = app.getControl().getProductsList("enabled=1", "name");
        } else if (selectedSort.equalsIgnoreCase(ORDEN_PRICE)) {
            listProducts = app.getControl().getProductsList("enabled=1", "price, name");
        } else {
            listProducts = app.getControl().getProductsList("enabled=1", "");
        }
        return listProducts;
    }

    public String getSelectedSort() {
        return selectedSort;
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
        pnItems = new javax.swing.JPanel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));

        pnItems.setLayout(new java.awt.GridLayout(1, 0));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(filler1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 811, Short.MAX_VALUE)
                    .addComponent(pnItems, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnItems, javax.swing.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(filler1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.Box.Filler filler1;
    private javax.swing.JLabel lbTitle;
    private javax.swing.JPanel pnItems;
    // End of variables declaration//GEN-END:variables

}
