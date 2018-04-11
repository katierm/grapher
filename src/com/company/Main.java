package com.company;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.Stroke;
import java.io.File;
import java.io.IOException;
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
    private MyPanel jPanel,jp;
    boolean pressed=true;
    private Color currentColor;
    private BufferedImage bufferedImage,bf2;
    private Graphics2D g,g2;
    int thickness;
    int textSize;
    //private final BufferedImage ;

    public ImageEdit() {
        f = new JFrame("Graph Editor");
        f.setSize(500, 500);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        currentColor = Color.black;

        jPanel = new MyPanel();
        jPanel.setBackground(Color.white);
        jPanel.setSize(450, 450);
        f.add(jPanel);
        bufferedImage = new BufferedImage(jPanel.getWidth(), jPanel.getHeight(), BufferedImage.TYPE_INT_RGB);
        bf2 = new BufferedImage(jPanel.getWidth(), jPanel.getHeight(), BufferedImage.TYPE_INT_RGB);
         g=(Graphics2D) bufferedImage.getGraphics();
        g.setColor(Color.white);
        g.fillRect(0,0,jPanel.getWidth(),jPanel.getHeight());

        g2=(Graphics2D) bf2.getGraphics();
        JToolBar jToolBar = new JToolBar("toolbar", JToolBar.VERTICAL);


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

        JButton text = new JButton(new ImageIcon(new ImageIcon("/home/katier/Рабочий стол/12/Grapher/src/com/company/t.png").
                getImage().getScaledInstance(20,20,Image.SCALE_DEFAULT)));
        text.addActionListener(actionEvent -> mode = Brushes.TEXT);
        jToolBar.add(text);
        //jToolBar.add(text);
        f.add(jToolBar, BorderLayout.WEST);
        //

        ///Color panel
        JToolBar colorBar = new JToolBar("colorBar", JToolBar.HORIZONTAL);
        colorChooser = new JColorChooser(currentColor);
        colorBackGroundChooser = new JColorChooser();
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
            jPanel.setBufferedImage(bufferedImage);
            //updateBackground(colorBackGroundChooser.getColor());
            jPanel.updateUI();
        });

        JMenuItem load = new JMenuItem("Load");
        menu.add(load);
        load.addActionListener(actionEvent -> {
            JFileChooser loadChooser = new JFileChooser();
            int res = loadChooser.showDialog(null,"Choose File");
            if(res==JFileChooser.APPROVE_OPTION) {
                String fileName = loadChooser.getSelectedFile().getAbsolutePath();
                loadChooser.addChoosableFileFilter(new FileFilter() {
                    @Override
                    public boolean accept(File file) {
                        if (file.isDirectory()) return true;
                        return (file.getName().endsWith(".png")||file.getName().endsWith(".jpeg"));
                    }

                    @Override
                    public String getDescription() {
                        return null;
                    }
                });
                try {
                    bufferedImage = ImageIO.read(new File(fileName));
                    g = (Graphics2D) bufferedImage.getGraphics();
                    //g.drawImage(bufferedImage,0,0,bufferedImage.getWidth(), bufferedImage.getHeight(),null);
                    jPanel.setBufferedImage(bufferedImage);
                    jPanel.updateUI();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        f.setJMenuBar(options);
        jPanel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                    xPad = e.getX();
                    yPad = e.getY();
                    //xf = xPad;
                    //yf = yPad;

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
                        jPanel.setBufferedImage(bufferedImage);
                        break;
                    case BRUSH:
                        g.setStroke(new BasicStroke(thickness,BasicStroke.CAP_SQUARE,BasicStroke.JOIN_BEVEL,0,new float[]{3,1},0));
                        g.drawLine((int) (xp * bufferedImage.getWidth()), (int) (yp * bufferedImage.getHeight()),
                                (int) (xp1 * bufferedImage.getWidth()), (int) (yp1 * bufferedImage.getHeight()));
                        jPanel.setBufferedImage(bufferedImage);
                        break;
                    case ERASER:
                        g.setStroke(new BasicStroke(thickness,BasicStroke.CAP_SQUARE,BasicStroke.JOIN_BEVEL,0,new float[]{3,1},0));
                        g.setColor(Color.white);
                        g.drawLine((int) (xp * bufferedImage.getWidth()), (int) (yp * bufferedImage.getHeight()),
                                (int) (xp1 * bufferedImage.getWidth()), (int) (yp1 * bufferedImage.getHeight()));
                        jPanel.setBufferedImage(bufferedImage);
                        break;
                    case NET:
                        g.setStroke(new BasicStroke(1,BasicStroke.CAP_SQUARE,BasicStroke.JOIN_BEVEL,0,new float[]{3,1},0));
                        g.drawLine((int) (xp * bufferedImage.getWidth()), (int) (yp * bufferedImage.getHeight()),
                                (int) (xp1 * bufferedImage.getWidth()+(thickness*thickness)),
                                (int) (yp1 * bufferedImage.getHeight()+(thickness*thickness)));
                        jPanel.setBufferedImage(bufferedImage);
                        break;
                    case OVAL:
                         xp1 = (double) (xPad-xf)/jPanel.getWidth();
                         yp1 = (double) (yPad-yf)/jPanel.getHeight();
                         double xt=(double) xf/jPanel.getWidth();
                         double yt= (double) yf/jPanel.getHeight();
                        g.setStroke(new BasicStroke(thickness,BasicStroke.CAP_ROUND,BasicStroke.JOIN_BEVEL,0,new float[]{3,1},0));
                        bf2=new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), bufferedImage.getType());
                        g2=(Graphics2D) bf2.getGraphics();
                        g2.drawImage(bufferedImage, 0, 0, null);
                        g2.setColor(currentColor);
                        g2.setStroke(new BasicStroke(thickness,BasicStroke.CAP_ROUND,BasicStroke.JOIN_BEVEL,0,new float[]{3,1},0));
                        if (xp1<0)xt= (double) xPad/jPanel.getWidth();
                        if (yp1<0)yt= (double) yPad/jPanel.getHeight();
                        g2.drawOval((int) (xt * bufferedImage.getWidth()), (int) abs(yt * bufferedImage.getHeight()),
                                (int) abs(xp1 * bufferedImage.getWidth()), (int) abs(yp1 * bufferedImage.getHeight()));
                        jPanel.setBufferedImage(bf2);
                        break;
                    case RECTANGLE:
                        xp1 = (double) (xPad-xf)/jPanel.getWidth();
                        yp1 = (double) (yPad-yf)/jPanel.getHeight();
                         xt=(double) xf/jPanel.getWidth();
                         yt= (double) yf/jPanel.getHeight();
                        g.setStroke(new BasicStroke(thickness,BasicStroke.CAP_ROUND,BasicStroke.JOIN_BEVEL,0,new float[]{3,1},0));
                        bf2=new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), bufferedImage.getType());
                        g2=(Graphics2D) bf2.getGraphics();
                        g2.drawImage(bufferedImage, 0, 0, null);
                        g2.setColor(currentColor);
                        g2.setStroke(new BasicStroke(thickness,BasicStroke.CAP_ROUND,BasicStroke.JOIN_BEVEL,0,new float[]{3,1},0));
                        if (xp1<0)xt= (double) xPad/jPanel.getWidth();
                        if (yp1<0)yt= (double) yPad/jPanel.getHeight();
                        g2.drawRect((int) (xt * bufferedImage.getWidth()), (int) abs(yt * bufferedImage.getHeight()),
                                (int) abs(xp1 * bufferedImage.getWidth()), (int) abs(yp1 * bufferedImage.getHeight()));
                        jPanel.setBufferedImage(bf2);
                        break;
                }

                xPad =e.getX();
                yPad =e.getY();
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
                    jPanel.setBufferedImage(bufferedImage);
                    jPanel.updateUI();
                }
            }
            @Override
            public void mousePressed(MouseEvent e) {
                xPad=e.getX();
                yPad=e.getY();
                xf = xPad;
                yf = yPad;
                if(mode==Brushes.TEXT) {
                    g=(Graphics2D) bufferedImage.getGraphics();
                    String text = TextSelect.run(xPad,yPad,jPanel,g,bufferedImage);
                    if(text==null){
                        jPanel.setBufferedImage(bufferedImage);
                        jPanel.updateUI();
                        return;
                    }
                    g.setColor(TextSelect.color);
                    g.setFont(new Font("Arial",TextSelect.mode,TextSelect.size ));
                    g.rotate(Math.toRadians(TextSelect.angle));
                    g.drawString(text,xPad,yPad);
                    jPanel.setBufferedImage(bufferedImage);
                    jPanel.updateUI();
                }

            }
        });

    }
    private void updateBackground(Color color) {
        //сделать заливку
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
    RECTANGLE,
    TEXT
}
