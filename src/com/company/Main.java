package com.company;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;
import java.util.Stack;

import static com.company.Brushes.PEN;
import static java.lang.Math.abs;


public class Main {

    public static void main(String[] args) {
        ImageEdit imageEdit = new ImageEdit();
        imageEdit.f.setVisible(true);
    }
}

class ImageEdit {
    private Brushes mode = PEN;
    //pos
    int xPad, yPad;

    //for oval and rec
    int xf, yf;
    //
    JFrame f;
    private JColorChooser colorChooser;
    private JColorChooser newPicture;
    private MyPanel jPanel;
    boolean pressed = true;
    private Color currentColor;
    public BufferedImage bufferedImage, bf2;
    private Graphics2D g, g2;
    private int thickness;
    private int backgroundColor = -1;
    public Stack<BufferedImage> redoStack = new Stack<>();
    public Stack<BufferedImage> undoStack = new Stack<>();
    ImageEdit ie;
    public ImageEdit() {
        f = new JFrame("Graph Editor");
        f.setSize(500, 500);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        currentColor = Color.black;


        jPanel = new MyPanel();
        jPanel.setBackground(Color.white);
        //jPanel.setSize();
        jPanel.setSize(450,450);
        //jPanel.setBounds(60,60,100,100);
        JScrollPane scrollPane = new JScrollPane(jPanel);
        //scrollPane.setSize(450,450);
        scrollPane.setFocusable(false);
        jPanel.setLayout(null);
        //scrollPane.setLayout(null);
        f.add(scrollPane);
        bufferedImage = new BufferedImage(jPanel.getWidth(), jPanel.getHeight(), BufferedImage.TYPE_INT_RGB);
        g = (Graphics2D) bufferedImage.getGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, jPanel.getWidth(), jPanel.getHeight());
        redoStack.push(clone(bufferedImage));

        setColorButtons();
        setToolButons();
        setListeners();
        setMenu();
        InputMap imap = jPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        imap.put(KeyStroke.getKeyStroke("ctrl S"), "test");

        ActionMap amap = jPanel.getActionMap();
        amap.put("test", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                openSaveDialog();
            }
        });
        jPanel.requestFocus();
        //jPanel.setSize(new Dimension((int)(bufferedImage.getWidth()),
        //        (int)(bufferedImage.getHeight())));
        System.out.println(jPanel.getWidth()+" "+jPanel.getHeight());
        //jPanel.setPreferredSize(new Dimension((int)(bufferedImage.getWidth()),
        //        (int)(bufferedImage.getHeight())));-
    }

    private void setColorButtons() {
        JToolBar colorBar = new JToolBar("colorBar", JToolBar.HORIZONTAL);
        colorChooser = new JColorChooser(currentColor);

        JButton chooseColor = new JButton();
        chooseColor.addActionListener(actionEvent -> {
            JDialog colorDialog = new JDialog(f, "Choose color");
            colorDialog.setVisible(true);
            colorDialog.add(colorChooser);
            colorDialog.setSize(200, 200);
        });
        colorChooser.getSelectionModel().addChangeListener(changeEvent -> {
            currentColor = colorChooser.getColor();
            chooseColor.setBackground(currentColor);
        });
        colorBar.add(chooseColor);

        JButton blue = new JButton();
        blue.addActionListener(actionEvent -> {
            currentColor = Color.blue;
        });
        blue.setBackground(Color.blue);
        colorBar.add(blue);

        JButton cyan = new JButton();
        cyan.addActionListener(actionEvent -> {
            currentColor = Color.cyan;
        });
        cyan.setBackground(Color.cyan);
        colorBar.add(cyan);

        JButton red = new JButton();
        red.addActionListener(actionEvent -> {
            currentColor = Color.red;
        });
        red.setBackground(Color.red);
        colorBar.add(red);

        JButton pink = new JButton();
        pink.addActionListener(actionEvent -> {
            currentColor = Color.pink;
        });
        pink.setBackground(Color.pink);
        colorBar.add(pink);

        JButton orange = new JButton();
        orange.addActionListener(actionEvent -> {
            currentColor = Color.orange;
        });
        orange.setBackground(Color.orange);
        colorBar.add(orange);

        JButton yellow = new JButton();
        yellow.addActionListener(actionEvent -> {
            currentColor = Color.yellow;
        });
        yellow.setBackground(Color.yellow);
        colorBar.add(yellow);

        JButton green = new JButton();
        green.addActionListener(actionEvent -> {
            currentColor = Color.green;
        });
        green.setBackground(Color.green);
        colorBar.add(green);

        JButton white = new JButton();
        white.addActionListener(actionEvent -> {
            currentColor = Color.white;
        });
        white.setBackground(Color.white);
        colorBar.add(white);

        JButton black = new JButton();
        black.addActionListener(actionEvent -> {
            currentColor = Color.black;
        });
        black.setBackground(Color.black);
        colorBar.add(black);

        JSlider size = new JSlider();
        size.addChangeListener(e -> {
            thickness = ((JSlider) e.getSource()).getValue();
        });
        size.setValue(1);
        colorBar.add(size);

        for (int i = 0; i < colorBar.getComponentCount(); i++) {
            Component comp = colorBar.getComponent(i);
            if (comp instanceof JButton || comp instanceof JSlider) {
                (comp).setFocusable(false);
            }

        }
        colorBar.setFloatable(false);
        f.add(colorBar, BorderLayout.PAGE_START);
        colorBar.setFloatable(false);
    }

    private void setToolButons() {

        JToolBar jToolBar = new JToolBar("toolbar", JToolBar.VERTICAL);
        JButton pen = new JButton(new ImageIcon(new ImageIcon("/home/katier/Рабочий стол/12/Grapher/src/com/company/pen.jpeg").
                getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT)));
        pen.addActionListener(actionEvent -> {
            mode = PEN;
        });
        jToolBar.add(pen);

        JButton brush = new JButton(new ImageIcon(new ImageIcon("/home/katier/Рабочий стол/12/Grapher/src/com/company/brush.jpeg").
                getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT)));
        brush.addActionListener(actionEvent -> {
            mode = Brushes.BRUSH;
        });
        jToolBar.add(brush);

        JButton eraser = new JButton(new ImageIcon(new ImageIcon("/home/katier/Рабочий стол/12/Grapher/src/com/company/eraser.jpeg").
                getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT)));
        eraser.addActionListener(actionEvent -> {
            mode = Brushes.ERASER;
        });
        jToolBar.add(eraser);

        JButton net = new JButton(new ImageIcon(new ImageIcon("/home/katier/Рабочий стол/12/Grapher/src/com/company/net.jpeg").
                getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT)));
        net.addActionListener(actionEvent -> {
            mode = Brushes.NET;
        });
        jToolBar.add(net);

        JButton oval = new JButton(new ImageIcon(new ImageIcon("/home/katier/Рабочий стол/12/Grapher/src/com/company/oval.jpeg").
                getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT)));
        oval.addActionListener(
                actionEvent -> {
                    mode = Brushes.OVAL;
                });
        jToolBar.add(oval);

        JButton rec = new JButton(new ImageIcon(new ImageIcon("/home/katier/Рабочий стол/12/Grapher/src/com/company/rec.jpeg").
                getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT)));
        rec.addActionListener(actionEvent -> {
            mode = Brushes.RECTANGLE;
        });
        jToolBar.add(rec);

        JButton text = new JButton(new ImageIcon(new ImageIcon("/home/katier/Рабочий стол/12/Grapher/src/com/company/t.png").
                getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT)));
        text.addActionListener(actionEvent -> {
            mode = Brushes.TEXT;
        });
        jToolBar.add(text);

        JButton fill = new JButton(new ImageIcon(new ImageIcon("/home/katier/Рабочий стол/12/Grapher/src/com/company/fill.jpeg").
                getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT)));
        fill.addActionListener(actionEvent -> {
            mode = Brushes.FILL;
        });
        jToolBar.add(fill);

        JButton defCol = new JButton(("c"));
        defCol.addActionListener(actionEvent -> {
            mode = Brushes.COLOR;
        });
        jToolBar.add(defCol);

        JButton plus = new JButton("+");
        plus.addActionListener(actionEvent -> {
            mode = Brushes.PLUS;
        });
        jToolBar.add(plus);

        JButton minus = new JButton("-");
        minus.addActionListener(actionEvent -> {
            mode = Brushes.MINUS;
        });
        jToolBar.add(minus);

        f.add(jToolBar, BorderLayout.WEST);
        jToolBar.setFloatable(false);
    }

    private void setListeners() {

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
                double xp = (double) e.getX() / jPanel.getWidth();
                double yp = (double) e.getY() / jPanel.getHeight();
                double xp1 = (double) xPad / jPanel.getWidth();
                double yp1 = (double) yPad / jPanel.getHeight();
                switch (mode) {
                    case PEN:
                        g.setStroke(new BasicStroke(thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL, 0, new float[]{3, 1}, 0));
                        g.drawLine((int) (xp * bufferedImage.getWidth()), (int) (yp * bufferedImage.getHeight()),
                                (int) (xp1 * bufferedImage.getWidth()), (int) (yp1 * bufferedImage.getHeight()));
                        //g.drawLine(e.getX(), e.getY(),xPad, yPad);
                        jPanel.setBufferedImage(bufferedImage);
                        break;
                    case BRUSH:
                        g.setStroke(new BasicStroke(thickness, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL, 0, new float[]{3, 1}, 0));
                        g.drawLine((int) (xp * bufferedImage.getWidth()), (int) (yp * bufferedImage.getHeight()),
                                (int) (xp1 * bufferedImage.getWidth()), (int) (yp1 * bufferedImage.getHeight()));
                        jPanel.setBufferedImage(bufferedImage);
                        break;
                    case ERASER:
                        g.setStroke(new BasicStroke(thickness, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL, 0, new float[]{3, 1}, 0));
                        g.setColor(Color.white);
                        g.drawLine((int) (xp * bufferedImage.getWidth()), (int) (yp * bufferedImage.getHeight()),
                                (int) (xp1 * bufferedImage.getWidth()), (int) (yp1 * bufferedImage.getHeight()));
                        jPanel.setBufferedImage(bufferedImage);
                        break;
                    case NET:
                        g.setStroke(new BasicStroke(1, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL, 0, new float[]{3, 1}, 0));
                        g.drawLine((int) (xp * bufferedImage.getWidth()), (int) (yp * bufferedImage.getHeight()),
                                (int) (xp1 * bufferedImage.getWidth() + (thickness * thickness)),
                                (int) (yp1 * bufferedImage.getHeight() + (thickness * thickness)));
                        jPanel.setBufferedImage(bufferedImage);
                        break;
                    case OVAL:
                        xp1 = (double) (xPad - xf) / jPanel.getWidth();
                        yp1 = (double) (yPad - yf) / jPanel.getHeight();
                        double xt = (double) xf / jPanel.getWidth();
                        double yt = (double) yf / jPanel.getHeight();
                        g.setStroke(new BasicStroke(thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL, 0, new float[]{3, 1}, 0));
                        bf2 = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), bufferedImage.getType());
                        g2 = (Graphics2D) bf2.getGraphics();
                        g2.drawImage(bufferedImage, 0, 0, null);
                        g2.setColor(currentColor);
                        g2.setStroke(new BasicStroke(thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL, 0, new float[]{3, 1}, 0));
                        if (xp1 < 0) xt = (double) xPad / jPanel.getWidth();
                        if (yp1 < 0) yt = (double) yPad / jPanel.getHeight();
                        g2.drawOval((int) (xt * bufferedImage.getWidth()), (int) abs(yt * bufferedImage.getHeight()),
                                (int) abs(xp1 * bufferedImage.getWidth()), (int) abs(yp1 * bufferedImage.getHeight()));
                        jPanel.setBufferedImage(bf2);
                        break;
                    case RECTANGLE:
                        xp1 = (double) (xPad - xf) / jPanel.getWidth();
                        yp1 = (double) (yPad - yf) / jPanel.getHeight();
                        xt = (double) xf / jPanel.getWidth();
                        yt = (double) yf / jPanel.getHeight();
                        g.setStroke(new BasicStroke(thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL, 0, new float[]{3, 1}, 0));
                        bf2 = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), bufferedImage.getType());
                        g2 = (Graphics2D) bf2.getGraphics();
                        g2.drawImage(bufferedImage, 0, 0, null);
                        g2.setColor(currentColor);
                        g2.setStroke(new BasicStroke(thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL, 0, new float[]{3, 1}, 0));
                        if (xp1 < 0) xt = (double) xPad / jPanel.getWidth();
                        if (yp1 < 0) yt = (double) yPad / jPanel.getHeight();
                        g2.drawRect((int) (xt * bufferedImage.getWidth()), (int) abs(yt * bufferedImage.getHeight()),
                                (int) abs(xp1 * bufferedImage.getWidth()), (int) abs(yp1 * bufferedImage.getHeight()));
                        jPanel.setBufferedImage(bf2);
                        break;
                }

                xPad = e.getX();
                yPad = e.getY();
                jPanel.updateUI();
            }


        });
        jPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                jPanel.setImageEdit(ie);
                if (mode == Brushes.OVAL || mode == Brushes.RECTANGLE) {
                    double xp1 = (double) (-xf + e.getX()) / jPanel.getWidth();
                    double yp1 = (double) (-yf + e.getY()) / jPanel.getHeight();
                    int xt = (int) xf;
                    int yt = (int) yf;
                    if (xp1 < 0) xt = e.getX();
                    if (yp1 < 0) yt = e.getY();
                    double xp = (double) xt / jPanel.getWidth();
                    double yp = (double) yt / jPanel.getHeight();
                    if (mode == Brushes.OVAL)
                        g.drawOval((int) (xp * bufferedImage.getWidth()), (int) abs(yp * bufferedImage.getHeight()),
                                (int) abs(xp1 * bufferedImage.getWidth()), (int) abs(yp1 * bufferedImage.getHeight()));
                    if (mode == Brushes.RECTANGLE)
                        g.drawRect((int) (xp * bufferedImage.getWidth()), (int) abs(yp * bufferedImage.getHeight()),
                                (int) abs(xp1 * bufferedImage.getWidth()), (int) abs(yp1 * bufferedImage.getHeight()));
                    jPanel.setBufferedImage(bufferedImage);
                    jPanel.updateUI();
                }

            }

            final BufferedImage clone(BufferedImage image) {
                BufferedImage clone = new BufferedImage(image.getWidth(),
                        image.getHeight(), image.getType());
                Graphics2D g2d = clone.createGraphics();
                g2d.drawImage(image, 0, 0, null);
                g2d.dispose();
                return clone;
            }

            @Override
            public void mousePressed(MouseEvent e) {
                //g.drawImage(bufferedImage, 0, 0, bufferedImage.getWidth(),
                //        bufferedImage.getHeight(), null);
                xPad = e.getX();
                yPad = e.getY();
                xf = xPad;
                yf = yPad;
                redoStack.push(clone(bufferedImage));
                if (mode == Brushes.TEXT) {
                    g = (Graphics2D) bufferedImage.getGraphics();
                    String text = TextSelect.run(xPad, yPad, jPanel, g, bufferedImage);
                    if (text == null) {
                        jPanel.setBufferedImage(bufferedImage);
                        jPanel.updateUI();
                        return;
                    }
                    AffineTransform orig = g.getTransform();
                    g.setColor(TextSelect.color);
                    g.setFont(new Font("Arial", TextSelect.mode, TextSelect.size));
                    g.rotate(Math.toRadians(TextSelect.angle));
                    g.drawString(text, xPad, yPad);
                    g.setTransform(orig);
                    //redoStack.push(clone(bufferedImage));
                    jPanel.setBufferedImage(bufferedImage);
                    jPanel.updateUI();
                }
                if (mode == Brushes.FILL) {
                    fillPoints(bufferedImage);
                    jPanel.setBufferedImage(bufferedImage);
                    jPanel.updateUI();
                }
                if (mode == Brushes.PLUS) {
                    //System.out.println(bufferedImage.getWidth());
                    /*AffineTransform tx = new AffineTransform();
                    tx.scale(2, 2);

                    AffineTransformOp op = new AffineTransformOp(tx,
                            AffineTransformOp.TYPE_BILINEAR);
                    bufferedImage = op.filter(bufferedImage, null);
                    jPanel.setPreferredSize(new Dimension(bufferedImage.getWidth(), bufferedImage.getHeight()));
                    g.drawImage(bufferedImage, 0, 0, bufferedImage.getWidth(),
                            bufferedImage.getHeight(), 0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), null);
                    jPanel.setBufferedImage(bufferedImage);
                    jPanel.updateUI();
                    //System.out.println(bufferedImage.getWidth());*/
                    System.out.println(jPanel.getWidth()+" "+bufferedImage.getWidth()+
                            " "+jPanel.getHeight()+" "+ bufferedImage.getHeight());
                    jPanel.setPreferredSize(new Dimension((int)(bufferedImage.getWidth()*2),
                            (int)(bufferedImage.getHeight()*2)));
                    g = (Graphics2D) bufferedImage.getGraphics();
                    g.drawImage(bufferedImage, 0, 0, (int)(bufferedImage.getWidth()),
                            (int)(bufferedImage.getHeight()), null);
                    jPanel.setBufferedImage(bufferedImage);
                    jPanel.updateUI();

                    System.out.println(jPanel.getWidth()+" "+bufferedImage.getWidth()+
                            " "+jPanel.getHeight()+" "+ bufferedImage.getHeight());
                }
                if (mode == Brushes.COLOR) {
                    int[] pixels = ((DataBufferInt) bufferedImage.getRaster().getDataBuffer()).getData();
                    System.out.println(pixels.length+" "+(e.getY() * jPanel.getWidth() + e.getX()));
                    currentColor = new Color(pixels[e.getY() * jPanel.getWidth() + e.getX()]);
                }
                if(mode==Brushes.MINUS){
                    //g = (Graphics2D) bufferedImage.getGraphics();
                    /*jPanel.setPreferredSize(new Dimension((int)(jPanel.getWidth()),
                            (int)(jPanel.getHeight())));
                    bf2=clone(bufferedImage);
                    g=(Graphics2D) bufferedImage.getGraphics();
                    g.setColor(Color.white);
                    g.fillRect(0,0,
                            bufferedImage.getWidth(),bufferedImage.getHeight());
                    g.drawImage(bf2, 0, 0, (int)(bufferedImage.getWidth()*0.5),
                            (int)(bufferedImage.getHeight()*0.5), null);
                    jPanel.setBufferedImage(bufferedImage);
                    jPanel.updateUI();
                    System.out.println(jPanel.getWidth()+" "+bufferedImage.getWidth()+
                            " "+jPanel.getHeight()+" "+ bufferedImage.getHeight());*/
                    jPanel.setPreferredSize(new Dimension((int)(bufferedImage.getWidth()),
                            (int)(bufferedImage.getHeight())));
                    f.setSize((int)(jPanel.getWidth()*0.5),(int)(jPanel.getHeight()*0.5));
                }
            }
        });
    }

    private static BufferedImage scale1(BufferedImage before, double scale) {
        int w = before.getWidth();
        int h = before.getHeight();
        // Create a new image of the proper size
        int w2 = (int) (w * scale);
        int h2 = (int) (h * scale);
        BufferedImage after = new BufferedImage(w2, h2, BufferedImage.TYPE_INT_ARGB);
        AffineTransform scaleInstance = AffineTransform.getScaleInstance(scale, scale);
        AffineTransformOp scaleOp
                = new AffineTransformOp(scaleInstance, AffineTransformOp.TYPE_BILINEAR);

        Graphics2D g2 = (Graphics2D) after.getGraphics();
        // Here, you may draw anything you want into the new image, but we're
        // drawing a scaled version of the original image.
        g2.drawImage(before, scaleOp, 0, 0);
        g2.dispose();
        return after;
    }


    private BufferedImage clone(BufferedImage image) {
        BufferedImage clone = new BufferedImage(image.getWidth(),
                image.getHeight(), image.getType());
        Graphics2D g2d = clone.createGraphics();
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();
        return clone;
    }

    private void setMenu() {
        JMenuBar options = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        JMenuItem background = new JMenuItem("New");
        menu.add(background);
        options.add(menu);
        newPicture = new JColorChooser();
        background.addActionListener(actionEvent -> {
            JDialog colorDialog = new JDialog(f, "Choose background");
            colorDialog.add(newPicture);
            colorDialog.setSize(200, 200);
            colorDialog.setVisible(true);
        });

        newPicture.getSelectionModel().addChangeListener(changeEvent -> {
            redoStack.push(clone(bufferedImage));
            bufferedImage = new BufferedImage(jPanel.getWidth(), jPanel.getHeight(), BufferedImage.TYPE_INT_RGB);
            g = (Graphics2D) bufferedImage.getGraphics();
            g.setColor(newPicture.getColor());
            g.fillRect(0, 0, jPanel.getWidth(), jPanel.getHeight());
            //currentColor=newPicture.getColor();
            jPanel.setBufferedImage(bufferedImage);
            jPanel.updateUI();
        });

        JMenuItem load = new JMenuItem("Load");
        menu.add(load);
        load.addActionListener(actionEvent -> {
            openLoadDialog();
        });

        JMenuItem saveAs = new JMenuItem("Save as");
        menu.add(saveAs);
        saveAs.addActionListener(actionEvent -> {
            openSaveDialog();
        });
        JColorChooser newBackgroundChooser = new JColorChooser();
        JMenuItem newBackground = new JMenuItem("new background");
        menu.add(newBackground);
        newBackground.addActionListener(actionEvent -> {
            JDialog colorDialog = new JDialog(f, "Choose background");
            colorDialog.add(newBackgroundChooser);
            colorDialog.setSize(200, 200);
            colorDialog.setVisible(true);
        });
        newBackgroundChooser.getSelectionModel().addChangeListener(changeEvent -> {
            updateBackground(bufferedImage, newBackgroundChooser.getColor());
            jPanel.setBufferedImage(bufferedImage);
            jPanel.updateUI();
        });
        ////redo/undo
        JToolBar redo_undo = new JToolBar("toolbar", JToolBar.HORIZONTAL);

        JButton redo = new JButton(new ImageIcon(new ImageIcon("/home/katier/Рабочий стол/12/Grapher/src/com/company/redo.jpeg").
                getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT)));

        redo.addActionListener((ActionEvent actionEvent) -> {
            if (!redoStack.empty()) {
                //if(wasUndo==false) {
                undoStack.push(clone(bufferedImage));
                //}
                BufferedImage b = redoStack.pop();
                bufferedImage = b;
                g = (Graphics2D) bufferedImage.getGraphics();
                g.drawImage(bufferedImage, 0, 0, null);
                jPanel.setBufferedImage(bufferedImage);
                jPanel.updateUI();

            }


        });


        JButton undo = new JButton(new ImageIcon(new ImageIcon("/home/katier/Рабочий стол/12/Grapher/src/com/company/undo.jpg").
                getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT)));
        undo.addActionListener(actionEvent -> {
            if (undoStack.empty()) return;
            BufferedImage b = undoStack.pop();
            redoStack.push(clone(b));
            bufferedImage = b;
            g = (Graphics2D) bufferedImage.getGraphics();
            g.drawImage(bufferedImage, 0, 0, null);
            jPanel.setBufferedImage(bufferedImage);
            jPanel.updateUI();
        });


        redo_undo.add(undo);
        redo_undo.add(redo);

        for (int i = 0; i < redo_undo.getComponentCount(); i++) {
            Component comp = redo_undo.getComponent(i);
            if (comp instanceof JButton) {
                ((JButton) comp).setFocusable(false);
            }

            /// f.add(redo_undo, BorderLayout.WEST);
        }

        options.add(redo_undo, BorderLayout.BEFORE_FIRST_LINE);
        f.setJMenuBar(options);

    }

    void openLoadDialog() {
        JFileChooser loadChooser = new JFileChooser();
        int res = loadChooser.showDialog(null, "Choose File");
        if (res == JFileChooser.APPROVE_OPTION) {
            String fileName = loadChooser.getSelectedFile().getAbsolutePath();
            loadChooser.addChoosableFileFilter(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    if (file.isDirectory()) return true;
                    return (file.getName().endsWith(".png") || file.getName().endsWith(".jpeg"));
                }

                @Override
                public String getDescription() {
                    return null;
                }
            });
            try {
                BufferedImage tmp = ImageIO.read(new File(fileName));
                bufferedImage = new BufferedImage(tmp.getWidth(), tmp.getHeight(), BufferedImage.TYPE_INT_RGB);
                g = (Graphics2D) bufferedImage.getGraphics();
                jPanel.setPreferredSize(new Dimension(bufferedImage.getWidth(), bufferedImage.getHeight()));
                g.drawImage(tmp, 0, 0, tmp.getWidth(),
                        tmp.getHeight(), 0, 0, tmp.getWidth(), tmp.getHeight(), null);
                //   g.drawImage(bufferedImage, 0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), null);
                jPanel.setBufferedImage(bufferedImage);
                jPanel.updateUI();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    void openSaveDialog() {
        JFileChooser saveChooser = new JFileChooser();
        String fileName = null;
        saveChooser.addChoosableFileFilter(new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (file.isDirectory()) return true;
                return (file.getName().endsWith(".png") || file.getName().endsWith(".jpeg"));
            }

            @Override
            public String getDescription() {
                return null;
            }
        });
        int res = saveChooser.showDialog(null, "Choose File");
        if (res == JFileChooser.APPROVE_OPTION) {
            fileName = saveChooser.getSelectedFile().getAbsolutePath();
        }
        if (fileName == null) return;
        try {
            if (fileName.endsWith(".png")) ImageIO.write(bufferedImage, "png", new File(fileName));
            if (fileName.endsWith(".jpeg")) ImageIO.write(bufferedImage, "jpeg", new File(fileName));
            if (!fileName.contains(".")) ImageIO.write(bufferedImage, "jpeg", new File(fileName + ".jpeg"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fillPoints(BufferedImage image) {
        //сделать заливку

        int width = image.getWidth();
        int height = image.getHeight();

        //pixels = new int[width * height];

        //PixelGrabber pgb = new PixelGrabber(image, 0, 0, width, height, pixels, 0, width);
        //pgb.grabPixels();

        int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        int[][] pixels2D = new int[width][height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pixels2D[x][y] = pixels[y * width + x];
            }
        }
        //System.out.println(Arrays.deepToString(pixels2D));
        int curColor = image.getRGB(xPad, yPad);
        int colorToFill = currentColor.getRGB();
        fillPoints(pixels2D, xPad, yPad, curColor, colorToFill);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pixels[y * width + x] = pixels2D[x][y];
            }

        }
    }

    private void fillPoints(int[][] arr, int x, int y, int initialColor, int colorToFill) {
        //System.out.println(x+" "+y+" "+initialColor+" "+arr[x][y]);
        /*if ((x >= bufferedImage.getWidth() || y >= bufferedImage.getHeight() || x < 0 || y < 0)) return;
        if (arr[x][y] == colorToFill) return;
        if (arr[x][y] != initialColor) return;
        ;
        if (arr[x][y] == initialColor) arr[x][y] = colorToFill;
        if (x < bufferedImage.getWidth() - 1 && arr[x + 1][y] != colorToFill)
            fillPoints(arr, x + 1, y, initialColor, colorToFill);
        if (x > 0 && arr[x - 1][y] != colorToFill) fillPoints(arr, x - 1, y, initialColor, colorToFill);
        if (y < bufferedImage.getHeight() - 1 && arr[x][y + 1] != colorToFill)
            fillPoints(arr, x, y + 1, initialColor, colorToFill);
        if (y > 0 && arr[x][y - 1] != colorToFill) fillPoints(arr, x, y - 1, initialColor, colorToFill);*/
        Queue<int[]> q = new ArrayDeque();
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        q.add(new int[]{x, y});
        while (!q.isEmpty()) {
            int t[] = q.poll();
            x = t[0];
            y = t[1];
            if (arr[x][y] == colorToFill) continue;
            arr[x][y] = colorToFill;
            if (x < width - 1 && y < height && arr[x + 1][y] == initialColor) q.add(new int[]{x + 1, y});
            if (x > 0 && y < height && arr[x - 1][y] == initialColor) q.add(new int[]{x - 1, y});
            if (x < width && y < height - 1 && arr[x][y + 1] == initialColor) q.add(new int[]{x, y + 1});
            if (x < width && y > 0 && arr[x][y - 1] == initialColor) q.add(new int[]{x, y - 1});
        }
    }

    private void updateBackground(BufferedImage image, Color color) {
        int width = image.getWidth();
        int height = image.getHeight();

        //pixels = new int[width * height];

        //PixelGrabber pgb = new PixelGrabber(image, 0, 0, width, height, pixels, 0, width);
        //pgb.grabPixels();

        int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        int[][] pixels2D = new int[width][height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pixels2D[x][y] = pixels[y * width + x];
            }
        }
        //System.out.println(Arrays.deepToString(pixels2D));
        int curColor = image.getRGB(xPad, yPad);
        int colorToFill = color.getRGB();
        updateBackground(pixels2D, colorToFill);
        backgroundColor = colorToFill;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pixels[y * width + x] = pixels2D[x][y];
            }

        }
    }

    private void updateBackground(int[][] arr, int colorToFill) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                if (arr[i][j] == backgroundColor) arr[i][j] = colorToFill;
    }
}

class MyPanel extends JPanel {
    BufferedImage bufferedImage;
    ImageEdit imageEdit;

    MyPanel() {
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent componentEvent) {
                /*if (bufferedImage == null) return;
                //bufferedImage=imageEdit.bufferedImage;
                AffineTransform tx = new AffineTransform();
                tx.scale(1.0*getWidth() / bufferedImage.getWidth(),
                        1.0*getHeight() / bufferedImage.getHeight());
                //if (tx.getScaleX() <= 0 || tx.getScaleY() <= 0) return;
                AffineTransformOp op = new AffineTransformOp(tx,
                        AffineTransformOp.TYPE_BILINEAR);
                bufferedImage = op.filter(bufferedImage, null);
                //Graphics graphics = bufferedImage.getGraphics();
                //setPreferredSize(new Dimension(bufferedImage.getWidth(), bufferedImage.getHeight()));
                //graphics.drawImage(bufferedImage, 0, 0, bufferedImage.getWidth(),
               //         bufferedImage.getHeight(), 0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), null);
                imageEdit.bufferedImage=bufferedImage;*/
            }
        });
    }

    void setBufferedImage(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
    }
    void setImageEdit(ImageEdit imageEdit){
        this.imageEdit=imageEdit;
    }

    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);
        // if(ImageEdit.loaded == false)
        graphics.drawImage(bufferedImage, 0, 0, getWidth(), getHeight(), null);
        // else if(bufferedImage.getWidth()>=getWidth()&&bufferedImage.getHeight()>=getHeight())
        //     graphics.drawImage(bufferedImage, 0, 0, getWidth(),getHeight(), null);
        // else graphics.drawImage(bufferedImage, 0, 0, bufferedImage.getWidth(),bufferedImage.getHeight(), null);
    }
}

enum Brushes {
    PEN,
    BRUSH,
    ERASER,
    NET,
    OVAL,
    RECTANGLE,
    TEXT,
    FILL,
    PLUS,
    MINUS,
    COLOR
}
