package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.ArrayList;
import java.util.Objects;

import static java.lang.Math.*;

public class Sketch extends Frame implements KeyListener, MouseListener, MouseMotionListener {
    // graphics
    public JPanel panel;
    // graphics
    public Canvas canvas;
    // if a mouse press is waiting to be handled
    public boolean mp = false;
    // mouse x
    private float mx = 0;
    // mouse y
    private float my = 0;
    // width of screen
    public int width = 500;
    // height of screen
    public int height = 500;
    // what cell is currently picked up
    public cell picked = null;
    // list of all the cells
    public ArrayList<cell> cells = new ArrayList<>();
    Sketch(){
        // more setup
        new JFrame("world");
        this.setUpGraphics();
        new Thread(this::run).start();
    }
    public void setUpGraphics(){
        // sets up graphics, can be ignored
        panel = new JPanel();
        canvas = new Canvas();
        canvas.setBounds(0,0, width, height);
        add(panel);
        panel.setLayout(null);
        panel.add(canvas);
        setPreferredSize(new Dimension(width, height));
        panel.setPreferredSize(new Dimension(width, height));
        panel.setBounds(0,0,width,height);
        pack();
        canvas.requestFocus();
        canvas.setIgnoreRepaint(true);
        canvas.createBufferStrategy(2);
        canvas.addKeyListener(this);
        canvas.addMouseListener(this);
        canvas.addMouseMotionListener(this);
        setVisible(true);
        setResizable(false);
        //End of graphics setup
        // setup basic cells: can add more or less. Copy a for kid cell, b for parent
        cell a = new cell("a", 2, 1, new ArrayList<>(), null);
        cell b = new cell("b", 3, 0, null, new ArrayList<>());
        cell c = new cell( "c", 4, 0, null, new ArrayList<>());
        cell d = new cell("d", 4, 1, new ArrayList<>(), null);
        // tell the cells their parents, and add them to main cells list
        a.parents.add(b);
        b.children.add(a);
        c.children.add(a);
        a.parents.add(c);
        d.parents.add(b);
        d.parents.add(c);
        b.children.add(d);
        c.children.add(d);
        cells.add(a);
        cells.add(b);
        cells.add(c);
        cells.add(d);
    }
    @SuppressWarnings("LoopConditionNotUpdatedInsideLoop")
    public void run(){
        boolean done = false;
        while(!done){
            // while true loop: main render loop
            this.render();
        }
    }
    public void render(){
        // here
        Graphics2D g = (Graphics2D) canvas.getBufferStrategy().getDrawGraphics().create();
        g.clearRect(0,0,width,height);
        g.setColor(Color.BLACK);
        // to here is boring graphics stuff: below the horizontal lines:
        g.drawLine(0,125, 500, 125);
        g.drawLine(0, 375,500,375);
        // handles a mouse press
        if (mp){
            mp = false;
            if (my < 125){
                int col = 0;
                for (int i = 0; i <= 500; i+=100) {
                    if (mx < i){
                        col = i/100 - 1;
                        for (cell c : cells){
                            if(c.column == col && c.level == 0){
                                picked = c;
                            }
                        }
                        if(picked != null){
                            if(picked.level == 0) {
                                picked.column = col;
                                for (cell cell : cells){
                                    cell.pathFind(this);
                                }
                            }
                        }
                        break;
                    }
                }
            }
            else if(my > 375) {
                int col = 0;
                for (int i = 0; i <= 500; i += 100) {
                    if (mx < i) {
                        col = i / 100 - 1;
                        for (cell c : cells) {
                            if (c.column == col && c.level == 1) {
                                picked = c;
                            }
                        }
                        if (picked != null) {
                            if (picked.level == 1) {
                                picked.column = col;
                                for (cell cell : cells){
                                    cell.pathFind(this);
                                }
                            }
                        }
                        break;
                    }
                }
            }
        }
        // vertical lines
        for (int i = 0; i < 4; i++) {
            g.drawLine(100 * i + 100, 0, 100 * i + 100, 500);
        }
        // cells should draw themselves
        for (cell c : cells){
            c.draw(g,this);
        }
        canvas.getBufferStrategy().show();
        g.dispose();
    }
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        mp = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }
    @Override
    public void mouseMoved(MouseEvent e) {
        mx = e.getX();
        my = e.getY();
    }
}