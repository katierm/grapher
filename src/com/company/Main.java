package com.company;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.Stroke;
import java.math.BigInteger;

import static com.company.Brushes.PEN;
import static java.lang.Math.abs;

public class Main {

    public static void main(String[] args) {
	    ImageEdit imageEdit=new ImageEdit();
	    imageEdit.f.setVisible(true);
    }
}
class ImageEdit{
    private Brushes mode= PEN;
    //pos
    int xPad, yPad,xf,yf;
    //
    private JColorChooser colorChooser;
    private JColorChooser colorBackGroundChooser ;
    JFrame f;
    private MyPanel jPanel;
    boolean pressed=true;
    private Color currentColor;
    private BufferedImage bufferedImage;
    private Graphics2D g,g2;
    int thickness;
    //private final BufferedImage ;

    public ImageEdit() {
        f = new JFrame("Graph Editor");
        f.setSize(500, 500);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        currentColor = Color.black;

        jPanel = new MyPanel();
        jPanel.setBackground(Color.white);
        jPanel.setSize(450, 450);
        jPanel.setOpaque(true);
        jPanel.setLayout(null);
        f.add(jPanel);

        bufferedImage = new BufferedImage(jPanel.getWidth(), jPanel.getHeight(), BufferedImage.TYPE_INT_RGB);
         g=(Graphics2D) bufferedImage.getGraphics();
        g.setColor(Color.white);
        g.fillRect(0,0,jPanel.getWidth(),jPanel.getHeight());
        ///Tool panel;
        JToolBar jToolBar = new JToolBar("toolbar", JToolBar.VERTICAL);

        //f.add(jToolBar);

        //brushes
        JButton pen = new JButton(new ImageIcon(new ImageIcon("/home/katier/Рабочий стол/12/Grapher/src/com/company/pen.jpeg").
                getImage().getScaledInstance(20,20,Image.SCALE_DEFAULT)));
        pen.addActionListener(actionEvent -> {mode = PEN;});
        jToolBar.add(pen);

        JButton brush = new JButton(new ImageIcon(new ImageIcon("/home/katier/Рабочий стол/12/Grapher/src/com/company/brush.jpeg").
                getImage().getScaledInstance(20,20,Image.SCALE_DEFAULT)));
        brush.addActionListener(actionEvent -> {mode = Brushes.BRUSH;});
        jToolBar.add(brush);

        JButton eraser = new JButton(new ImageIcon(new ImageIcon("/home/katier/Рабочий стол/12/Grapher/src/com/company/eraser.jpeg").
                getImage().getScaledInstance(20,20,Image.SCALE_DEFAULT)));
        eraser.addActionListener(actionEvent -> mode = Brushes.ERASER);
        jToolBar.add(eraser);

        JButton net = new JButton(new ImageIcon(new ImageIcon("/home/katier/Рабочий стол/12/Grapher/src/com/company/net.jpeg").
                getImage().getScaledInstance(20,20,Image.SCALE_DEFAULT)));
        net.addActionListener(actionEvent -> mode = Brushes.NET);
        jToolBar.add(net);

        JButton oval = new JButton(new ImageIcon(new ImageIcon("/home/katier/Рабочий стол/12/Grapher/src/com/company/oval.jpeg").
                getImage().getScaledInstance(20,20,Image.SCALE_DEFAULT)));
        oval.addActionListener(actionEvent -> mode = Brushes.OVAL);
        jToolBar.add(oval);

        JButton rec = new JButton(new ImageIcon(new ImageIcon("/home/katier/Рабочий стол/12/Grapher/src/com/company/rec.jpeg").
                getImage().getScaledInstance(20,20,Image.SCALE_DEFAULT)));
        rec.addActionListener(actionEvent -> mode = Brushes.RECTANGLE);
        jToolBar.add(rec);

        f.add(jToolBar, BorderLayout.WEST);
        //

        ///Color panel
        JToolBar colorBar = new JToolBar("colorBar", JToolBar.HORIZONTAL);
        colorChooser = new JColorChooser(currentColor);
        colorBackGroundChooser = new JColorChooser(Color.white);
        JButton chooseColor = new JButton();
        chooseColor.addActionListener(actionEvent -> {
            JDialog colorDialog = new JDialog(f,"Choose color");
            colorDialog.add(colorChooser);
            colorDialog.setSize(200, 200);
            colorDialog.setVisible(true);
        });
        colorChooser.getSelectionModel().addChangeListener(changeEvent -> {
            currentColor = colorChooser.getColor();
            chooseColor.setBackground(currentColor);
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

        JSlider size=new JSlider();
        size.addChangeListener(e -> thickness=((JSlider)e.getSource()).getValue());
        size.setValue(1);
        colorBar.add(size);
        f.add(colorBar, BorderLayout.PAGE_START);
        JMenuBar options = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        JMenuItem background = new JMenuItem("New");
        menu.add(background);
        options.add(menu);
        background.addActionListener(actionEvent -> {
            JDialog colorDialog = new JDialog(f,"Choose background");
            colorDialog.add(colorBackGroundChooser);
            colorDialog.setSize(200, 200);
            colorDialog.setVisible(true);
        });
        colorBackGroundChooser.getSelectionModel().addChangeListener(changeEvent -> {
            g.setColor(colorBackGroundChooser.getColor());
            g.fillRect(0,0,jPanel.getWidth(),jPanel.getHeight());
            jPanel.updateUI();
        });
        f.setJMenuBar(options);
        jPanel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                    xPad = e.getX();
                    yPad = e.getY();
                    //g = bufferedImage.getGraphics();
                    xf = xPad;
                    yf = yPad;
                    jPanel.setBufferedImage(bufferedImage);
                    if(mode!=Brushes.OVAL||mode!=Brushes.RECTANGLE)jPanel.updateUI();

            }

            @Override
            public void mouseDragged(MouseEvent e) {
                g = (Graphics2D) bufferedImage.getGraphics();
                g.setColor(currentColor);
                double xp = (double) e.getX()/jPanel.getWidth();
                double yp = (double) e.getY()/jPanel.getHeight();
                double xp1 = (double) xPad /jPanel.getWidth();
                double yp1 = (double) yPad /jPanel.getHeight();
                switch (mode) {
                    case PEN:
                        g.setStroke(new BasicStroke(thickness,BasicStroke.CAP_ROUND,BasicStroke.JOIN_BEVEL,0,new float[]{3,1},0));
                        g.drawLine((int) (xp * bufferedImage.getWidth()), (int) (yp * bufferedImage.getHeight()),
                                (int) (xp1 * bufferedImage.getWidth()), (int) (yp1 * bufferedImage.getHeight()));
                        break;
                    case BRUSH:
                        g.setStroke(new BasicStroke(thickness,BasicStroke.CAP_SQUARE,BasicStroke.JOIN_BEVEL,0,new float[]{3,1},0));
                        g.drawLine((int) (xp * bufferedImage.getWidth()), (int) (yp * bufferedImage.getHeight()),
                                (int) (xp1 * bufferedImage.getWidth()), (int) (yp1 * bufferedImage.getHeight()));
                        break;
                    case ERASER:
                        g.setStroke(new BasicStroke(thickness,BasicStroke.CAP_SQUARE,BasicStroke.JOIN_BEVEL,0,new float[]{3,1},0));
                        g.setColor(Color.white);
                        g.drawLine((int) (xp * bufferedImage.getWidth()), (int) (yp * bufferedImage.getHeight()),
                                (int) (xp1 * bufferedImage.getWidth()), (int) (yp1 * bufferedImage.getHeight()));
                        break;
                    case NET:
                        g.setStroke(new BasicStroke(1,BasicStroke.CAP_SQUARE,BasicStroke.JOIN_BEVEL,0,new float[]{3,1},0));
                        g.drawLine((int) (xp * bufferedImage.getWidth()), (int) (yp * bufferedImage.getHeight()),
                                (int) (xp1 * bufferedImage.getWidth()+(thickness*thickness)),
                                (int) (yp1 * bufferedImage.getHeight()+(thickness*thickness)));
                        break;
                    case OVAL:
                         xp1 = (xPad-xf);
                         yp1 = (yPad-yf);
                         int xt=(int)xf;
                         int yt=(int)yf;
                        g.setStroke(new BasicStroke(thickness,BasicStroke.CAP_ROUND,BasicStroke.JOIN_BEVEL,0,new float[]{3,1},0));
                        g2=(Graphics2D) jPanel.getGraphics();
                        g2.setColor(currentColor);
                        g2.setStroke(new BasicStroke(thickness,BasicStroke.CAP_ROUND,BasicStroke.JOIN_BEVEL,0,new float[]{3,1},0));
                        if (xp1<0)xt=xPad;
                        if (yp1<0)yt=yPad;
                        g2.drawOval(xt,yt,(int)abs(xp1),(int)abs(yp1));
                        jPanel.updateUI();
                        break;
                    case RECTANGLE:
                        xp1 = (xPad-xf);
                        yp1 = (yPad-yf);
                         xt=(int)xf;
                         yt=(int)yf;
                        g.setStroke(new BasicStroke(thickness,BasicStroke.CAP_ROUND,BasicStroke.JOIN_BEVEL,0,new float[]{3,1},0));
                        g2=(Graphics2D) jPanel.getGraphics();
                        g2.setColor(currentColor);
                        g2.setStroke(new BasicStroke(thickness,BasicStroke.CAP_ROUND,BasicStroke.JOIN_BEVEL,0,new float[]{3,1},0));
                        if (xp1<0)xt=xPad;
                        if (yp1<0)yt=yPad;
                        g2.drawRect(xt,yt,(int)abs(xp1),(int)abs(yp1));
                        jPanel.updateUI();
                        break;
                }

                xPad =e.getX();
                yPad =e.getY();
                jPanel.setBufferedImage(bufferedImage);
                jPanel.updateUI();
            }


        });
        jPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if(mode==Brushes.OVAL || mode==Brushes.RECTANGLE) {
                    double xp1 = (double) (-xf+e.getX()) / jPanel.getWidth();
                    double yp1 = (double) (-yf+e.getY()) / jPanel.getHeight();
                    int xt=(int)xf;
                    int yt=(int)yf;
                    if(xp1<0) xt=e.getX();
                    if(yp1<0) yt=e.getY();
                    double xp = (double) xt / jPanel.getWidth();
                    double yp = (double) yt / jPanel.getHeight();
                    if(mode==Brushes.OVAL)g.drawOval((int) (xp * bufferedImage.getWidth()), (int) abs(yp * bufferedImage.getHeight()),
                            (int) abs(xp1 * bufferedImage.getWidth()), (int) abs(yp1 * bufferedImage.getHeight()));
                    if(mode==Brushes.RECTANGLE)g.drawRect((int) (xp * bufferedImage.getWidth()), (int) abs(yp * bufferedImage.getHeight()),
                            (int) abs(xp1 * bufferedImage.getWidth()), (int) abs(yp1 * bufferedImage.getHeight()));
                    jPanel.updateUI();
                }
            }
            @Override
            public void mousePressed(MouseEvent e) {
                xf=e.getX();
                yf=e.getY();

            }
        });
    }
}

class MyPanel extends JPanel{
    BufferedImage bufferedImage;
    void setBufferedImage(BufferedImage bufferedImage){
        this.bufferedImage=bufferedImage;
    }
    @Override
    public void paint(Graphics graphics){
        super.paint(graphics);
        graphics.drawImage(bufferedImage,0,0,getWidth(),getHeight(),null);
    }

}

enum  Brushes {
    PEN,
    BRUSH,
    ERASER,
    NET,
    OVAL,
    RECTANGLE
}