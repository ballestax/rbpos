/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rb.gui;

import com.rb.Aplication;
import com.rb.domain.Module;
import com.rb.domain.Permission;
import com.rb.domain.User;
import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.dz.Imagenes;

/**
 *
 * @author hp
 */
public class PanelDash extends JPanel {

    private Aplication app;
    private final Image imagen;
    private final BufferedImage imgMas = null;
    private int wl = 0;
    private int hl = 0;

    public PanelDash(Aplication app) {
        this.app = app;
        setOpaque(true);
        setLayout(new GridBagLayout());
//        Font f1 = new Font("Tahoma", 1, 18);
//        Image centrarTexto = org.balx.Imagenes.centrarTexto(WC, WC, "DERO CAMPAÑA ELECTORAL", f1, Color.white, Color.blue);
//        BufferedImage textoImagen = ImagenesFx.textoImagen("DERO\nCAMPAÑA", WC, WC, Color.LIGHT_GRAY, Color.white, new Font("Tahoma", 1, 18));
//        imagen = centrarTexto;//ImagenesFx.rotarImagen(org.bx.Imagenes.toBuffereredImage(centrarTexto), -45);
        imagen = Imagenes.imagenToGray(app.getImageBC(), "");
//        try {
//            Image tmp = app.getImgManager().getImagen(app.getFolderIcons() + "logo-2.png");
//            imgMas = Imagenes.toBuffereredImage(tmp);
//            wl = imgMas.getWidth(this);
//            hl = imgMas.getHeight(this);
//        } catch (Exception e) {
//        }

//        tmp = ImagenFx.escarlarImagen(imgMas, 80, 30, true);
        loadModules();

    }

    private void clearModule() {

    }

    public void reloadModules() {
        loadModules();
    }

    private void loadModules() {
        removeAll();

        User user = app.getUser();
        List<Module> modules = app.getModules();
        int COLS = 5;
        int ix = 1, iy = 1;
        for (Module module : modules) {

            Permission perm = app.getControl().getPermissionByName(module.getPermision());
            if (user != null && app.getControl().hasPermission(user, perm)) {

                add(newButton(app.getAction(module.getName())), constrains(ix, iy, 30));
                ix++;
                if (ix == COLS) {
                    ix = 1;
                    iy++;
                }
            }
        }

        Box box = new Box(BoxLayout.X_AXIS);
        box.setAlignmentY(BOTTOM_ALIGNMENT);
        JButton btn = new JButton(app.getAction(Aplication.ACTION_EXIT_APP));
        btn.setText("");
        btn.setPreferredSize(new Dimension(30, 30));
        btn.setMinimumSize(new Dimension(30, 30));
        box.add(btn);
        //

        add(box, new GridBagConstraints(4, 3, 0, 0, 0, 0,
                GridBagConstraints.CENTER,
                GridBagConstraints.EAST,
                new Insets(30, 30, 30, 30), 100, 100));
    }

    @Override
    public void setVisible(boolean aFlag) {
        Component[] components = getComponents();
        for (Component component : components) {
            component.setVisible(aFlag);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Rectangle bounds = getBounds();
        Graphics2D g2 = (Graphics2D) g;
        int WC = Aplication.WC;
        int cx = (bounds.width / WC) + 1;
        int cy = (bounds.height / WC) + 1;
        for (int i = 0; i < cx; i++) {
            for (int j = 0; j < cy; j++) {
                if (i % 2 == 0) {
                    if (j % 2 != 0) {
                        g2.drawImage(imagen, i * WC - 10, j * WC - 10, null);
                    }
                } else if (j % 2 == 0) {
                    g2.drawImage(imagen, i * WC - 10, j * WC - 10, null);
                }
            }
        }
        if (Aplication.isMessaged()) {
            int px = bounds.width / 2 - wl / 2;
            int py = bounds.height / 2 - hl / 2;

//          String logo = "";
//        BufferedImage logoImage = org.dzur.ImagenesFx.textoImagen(logo, wl, hl, Color.blue, new Color(255,255,255,50), new Font("Tahoma",1,42));
//            if(imgMas!=null)
////            g2.drawImage(imgMas, px, py, this);
        }

    }

    private JButton newButton(AbstractAction action) {
        final JButton bt1 = new JButton();
//        bt1.setBorderPainted(false);
        bt1.setPreferredSize(new Dimension(50, 50));
        bt1.setMinimumSize(new Dimension(50, 50));
        bt1.setFocusPainted(false);
        bt1.setBackground(new Color(200, 210, 230));
//        bt1.setContentAreaFilled(false);
        bt1.setFont(new Font("tahoma", 1, 16));
        bt1.setAction(action);
        bt1.setRolloverIcon(bt1.getIcon());
        bt1.setVerticalTextPosition(SwingConstants.BOTTOM);
        bt1.setHorizontalTextPosition(SwingConstants.CENTER);

        bt1.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e); //To change body of generated methods, choose Tools | Templates.
                bt1.setForeground(Color.red);
                bt1.setFont(new Font("tahoma", 1, 17));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e); //To change body of generated methods, choose Tools | Templates.
                bt1.setForeground(Color.black);
                bt1.setFont(new Font("tahoma", 1, 16));
            }

        });

        bt1.addFocusListener(new FocusAdapter() {

            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e); //To change body of generated methods, choose Tools | Templates.
                bt1.setForeground(Color.black);
                bt1.setFont(new Font("tahoma", 1, 16));
            }

        });
        return bt1;
    }

    private GridBagConstraints constrains(int f, int c, int sep) {
        GridBagConstraints constrains = new GridBagConstraints();
        constrains.gridx = f;
        constrains.gridy = c;
        constrains.weightx = 0.0;
        constrains.weighty = 0.0;
        constrains.insets = new Insets(sep, sep, sep, sep);
        constrains.ipadx = 100;
        constrains.ipady = 100;
        constrains.fill = GridBagConstraints.NONE;
        constrains.anchor = GridBagConstraints.CENTER;
        return constrains;
    }

}
