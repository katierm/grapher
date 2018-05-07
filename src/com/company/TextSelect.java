package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class TextSelect extends JDialog {
    Graphics2D g2;
    BufferedImage bf2;
    private JColorChooser colorChooser;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textField;
    private JSlider textSize;
    private JCheckBox b;
    private JCheckBox i;
    private JSlider angleSlide;
    private JRadioButton p;
    private String text=null;
    static int size=40;
    static int thickness;
    static int mode=Font.PLAIN;
    static double angle;
    static Color color=Color.black;
    public TextSelect(int x,int y, MyPanel jPanel,Graphics2D g,BufferedImage bufferedImage) {

        angle=0;
        mode=Font.PLAIN;
        size=40;
        color=Color.black;

        JMenuBar options = new JMenuBar();
        JMenuItem colorItem = new JMenuItem("Color");
        colorItem.setBackground(g.getColor());
        options.add(colorItem);
        setJMenuBar(options);

        colorChooser = new JColorChooser(color);
        colorItem.addActionListener(actionEvent -> {
            JDialog colorDialog = new JDialog(this,"Choose color");
            colorDialog.add(colorChooser);
            colorDialog.setSize(200, 200);
            colorDialog.setVisible(true);
        });
        colorChooser.getSelectionModel().addChangeListener(changeEvent -> {
            if(text==null) return;
            color = colorChooser.getColor();
            colorItem.setBackground(color);
            g2.setColor(color);
            g2.drawString(text,x,y);
            jPanel.setBufferedImage(bf2);
            jPanel.updateUI();
        });

        i.addActionListener(actionEvent -> {
            if(mode==Font.ITALIC) mode = Font.PLAIN;
                else if (mode == Font.ITALIC + Font.BOLD) mode = Font.BOLD;
                else if (mode == Font.BOLD) mode = Font.BOLD + Font.ITALIC;
                else if(mode == Font.PLAIN) mode = Font.ITALIC;
                repaintForButton(jPanel,bufferedImage,x,y);
        });

        b.addActionListener(actionEvent -> {
            if(mode==Font.BOLD) mode=Font.PLAIN;
            else if(mode==Font.ITALIC+Font.BOLD)mode=Font.ITALIC;
            else if (mode ==Font.ITALIC) mode=Font.BOLD+Font.ITALIC;
            else if(mode==Font.PLAIN)mode=Font.BOLD;
            repaintForButton(jPanel,bufferedImage,x,y);
        });


        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                onCancel();
            }
        });

        //keyListener
        textField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent ke) {
                bf2=new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), bufferedImage.getType());
                g2=(Graphics2D) bf2.getGraphics();
                g2.drawImage(bufferedImage, 0, 0, null);
                text= textField.getText();
                g2.setFont(new Font("Arial",mode,size ));
                g2.setColor(color);
                g2.rotate(Math.toRadians(angle));
                g2.drawString(text,x,y);
                jPanel.setBufferedImage(bf2);
                jPanel.updateUI();
            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {

            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {
                bf2=new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), bufferedImage.getType());
                g2=(Graphics2D) bf2.getGraphics();
                g2.drawImage(bufferedImage, 0, 0, null);
                text= textField.getText();
                g2.setFont(new Font("Arial",mode,size ));
                g2.setColor(color);
                g2.rotate(Math.toRadians(angle));
                g2.drawString(text,x,y);
                jPanel.setBufferedImage(bf2);
                jPanel.updateUI();
            }
        });


        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        textSize.setValue(40);
        textSize.addChangeListener(e -> {
            bf2=new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), bufferedImage.getType());
            g2=(Graphics2D) bf2.getGraphics();
            g2.drawImage(bufferedImage, 0, 0, null);
            size= ((JSlider) e.getSource()).getValue();
            text= textField.getText();
            if(text==null)return;
            g2.setFont(new Font("Arial",mode,size ));
            g2.setColor(color);
            g2.rotate(Math.toRadians(angle));
            g2.drawString(text,x,y);
            jPanel.setBufferedImage(bf2);
            jPanel.updateUI();

        });

        angleSlide.addChangeListener(e->{
            bf2=new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), bufferedImage.getType());
            g2=(Graphics2D) bf2.getGraphics();
            g2.drawImage(bufferedImage, 0, 0, null);
            angle= ((JSlider) e.getSource()).getValue() - 50;
            text= textField.getText();
            if(text==null)return;
            g2.setFont(new Font("Arial",mode,size ));
            g2.setColor(color);
            g2.rotate(Math.toRadians(angle));
            g2.drawString(text,x,y);
            jPanel.setBufferedImage(bf2);
            jPanel.updateUI();
        });

    }

    private void onOK() {
        text= textField.getText();
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        text=null;
        dispose();
    }
    public static  void flush(){
        angle=0;

        mode=Font.PLAIN;
        size=40;
    }
    void repaintForButton(MyPanel jPanel, BufferedImage bufferedImage,int x,int y){
        bf2=new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), bufferedImage.getType());
        g2=(Graphics2D) bf2.getGraphics();
        g2.drawImage(bufferedImage, 0, 0, null);
        text= textField.getText();
        if(text==null)return;
        g2.setFont(new Font("Arial",mode,size ));
        g2.setColor(color);
        g2.rotate(Math.toRadians(angle));
        g2.drawString(text,x,y);
        jPanel.setBufferedImage(bf2);
        jPanel.updateUI();
    }
    public static String run(int x, int y, MyPanel jPanel, Graphics2D g2, BufferedImage bufferedImage){
        TextSelect dialog = new TextSelect(x,y,jPanel,g2,bufferedImage);
        dialog.setBounds(x,y,0,0);
        dialog.pack();
        dialog.setVisible(true);
        return dialog.text;
    }

}
