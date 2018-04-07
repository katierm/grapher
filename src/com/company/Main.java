package com.company;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class Main {

    public static void main(String[] args) {
	    ImageEdit imageEdit=new ImageEdit();
	    imageEdit.f.setVisible(true);
    }
}
class ImageEdit{
    int mode;
    //pos
    int x,y,xf,yf;
    //
    JColorChooser colorChooser;
    JFrame f;
    JPanel jPanel;
    boolean pressed=true;
    Color currentColor ;
    BufferedImage bufferedImage;
    public ImageEdit() {
        f = new JFrame("Graph Editor");
        f.setSize(500, 500);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        currentColor = Color.black;

        jPanel = new JPanel();
        jPanel.setBackground(Color.white);
        jPanel.setSize(450, 450);
        jPanel.setOpaque(true);
        jPanel.setLayout(null);
        f.add(jPanel);


        ///Tool panel
        JToolBar jToolBar = new JToolBar("toolbar", JToolBar.VERTICAL);
        bufferedImage = new BufferedImage(jPanel.getWidth(), jPanel.getHeight(), BufferedImage.TYPE_INT_RGB);
        //f.add(jToolBar);

        //brushes
        JButton pen = new JButton("pen");
        pen.addActionListener(actionEvent -> mode = 0);
        jToolBar.add(pen);

        JButton brush = new JButton("brush");
        pen.addActionListener(actionEvent -> mode = 1);
        jToolBar.add(brush);

        JButton eraser = new JButton("eraser");
        pen.addActionListener(actionEvent -> mode = 2);
        jToolBar.add(eraser);
        //jToolBar.setBounds(0, 0, 50, 450);
        f.add(jToolBar, BorderLayout.WEST);
        //

        ///Color panel
        JToolBar colorBar = new JToolBar("colorBar", JToolBar.HORIZONTAL);
        colorChooser = new JColorChooser(currentColor);
        JButton chooseColor = new JButton();
        chooseColor.addActionListener(actionEvent -> {
            JDialog colorDialog = new JDialog(f);
            colorDialog.add(colorChooser);
            colorDialog.setSize(200, 200);
            colorDialog.setVisible(true);
        });
        colorBar.add(chooseColor);
        JButton blue = new JButton();
        blue.addActionListener(actionEvent -> currentColor = Color.blue);
        blue.setBackground(Color.blue);
        colorBar.add(blue);

        JButton cyan = new JButton();
        cyan.addActionListener(actionEvent -> currentColor = Color.cyan);
        cyan.setBackground(Color.cyan);
        colorBar.add(cyan);

        JButton red = new JButton();
        red.addActionListener(actionEvent -> currentColor = Color.red);
        red.setBackground(Color.red);
        colorBar.add(red);

        JButton pink = new JButton();
        pink.addActionListener(actionEvent -> currentColor = Color.pink);
        pink.setBackground(Color.pink);
        colorBar.add(pink);

        JButton orange = new JButton();
        orange.addActionListener(actionEvent -> currentColor = Color.orange);
        orange.setBackground(Color.orange);
        colorBar.add(orange);

        JButton yellow = new JButton();
        yellow.addActionListener(actionEvent -> currentColor = Color.yellow);
        yellow.setBackground(Color.yellow);
        colorBar.add(yellow);

        JButton green = new JButton();
        green.addActionListener(actionEvent -> currentColor = Color.green);
        green.setBackground(Color.green);
        colorBar.add(green);

        JButton white = new JButton();
        white.addActionListener(actionEvent -> currentColor = Color.white);
        white.setBackground(Color.white);
        colorBar.add(white);

        JButton black = new JButton();
        black.addActionListener(actionEvent -> currentColor = Color.black);
        black.setBackground(Color.black);
        colorBar.add(black);

        colorChooser.getSelectionModel().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                currentColor = colorChooser.getColor();
                chooseColor.setBackground(currentColor);
            }
        });
        f.add(colorBar, BorderLayout.PAGE_START);

        jPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
               // if (pressed) {
                    Graphics g = bufferedImage.getGraphics();
                    Graphics2D g2 = (Graphics2D) g;
                    // установка цвета
                    g2.setColor(currentColor);

                    switch (mode) {
                        case 0:
                            g2.drawLine(100, 100, e.getX(), e.getY());
                            break;
                        case 1:
                            g2.setStroke(new BasicStroke(3.0f));
                            g2.drawLine(x, y, e.getX(), e.getY());
                            break;
                        // ластик
                        case 2:
                            g2.setStroke(new BasicStroke(3.0f));
                            g2.setColor(Color.white);
                            g2.drawLine(x, y, e.getX(), e.getY());
                            break;
                    }
                //}

                x = e.getX();
                y = e.getY();

                pressed = true;
                jPanel.repaint();
            }
        });
        jPanel.addMouseListener(new MouseAdapter() {
            public void mouseCliced(MouseEvent e) {
                Graphics g = bufferedImage.getGraphics();
                Graphics2D g2 = (Graphics2D) g;
                // установка цвета
                g2.setColor(currentColor);
                switch (mode) {
                    // карандаш
                    case 0:
                        g2.drawLine(x, y, x + 1, y + 1);
                        break;
                    // кисть
                    case 1:
                        g2.setStroke(new BasicStroke(3.0f));
                        g2.drawLine(x, y, x + 1, y + 1);
                        break;
                    // ластик
                    case 2:
                        g2.setStroke(new BasicStroke(3.0f));
                        g2.setColor(Color.white);
                        g2.drawLine(x, y, x + 1, y + 1);
                        break;
                }
                x=e.getX();
                y=e.getY();

                pressed=true;
                jPanel.repaint();
            }

            public void mousePressed(MouseEvent e) {
                x=e.getX();
                y=e.getY();
                xf=e.getX();
                yf=e.getY();
                pressed=true;
            }
        });
    }
}