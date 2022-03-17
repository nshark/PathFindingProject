package com.company;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class cell {
    // shared hashmap taking the g.drawline arguments in array form to tell path if it is occupied or not;
    static HashMap<ArrayList<Integer>, Boolean> lines = new HashMap<>();
    // what column
    public int column;
    // what level
    public int level;
    // what is my label
    public String title;
    // parents cell array
    public ArrayList<cell> parents;
    // children cell array
    public ArrayList<cell> children;
    // array list of path arraylists, which are arraylists of arguments for drawing a line.
    public ArrayList<ArrayList<ArrayList<Integer>>> paths;
    cell(String title1, int column1, int level1, ArrayList<cell> Parent, ArrayList<cell> Children){
        // setup
        title = title1;
        column = column1;
        level = level1;
        parents = Parent;
        children = Children;
    }
    public void draw(Graphics2D g, Sketch s){
        // if their are no paths, find me some.
        if(paths == null){
            pathFind(s);
        }
        // draw lines from path array, formatted as [[x1, y1, x2, y2], [x1, y1, x2, y2]], etc]
        for (ArrayList<ArrayList<Integer>> path : paths) {
            for (ArrayList<Integer> pos : path) {
                g.drawLine(pos.get(0), pos.get(1), pos.get(2), pos.get(3));
            }
        }
        // draw the title/label
        g.setFont(Main.f);
        g.drawString(title, this.column * 100 + 50, this.level*375 + 80);
    }
    public void pathFind(Sketch s){
        // remove the old path from lines hashmap
        if (paths != null) {
            for (ArrayList<ArrayList<Integer>> i : paths) {
                for (ArrayList<Integer> x : i) {
                    lines.replace(x, false);
                }
            }
        }
        paths = new ArrayList<>();
        // if I have someone to draw a line too
        if(children != null) {
            // for each child
            for (cell child : children) {
                // t2 and t1 stand for temporary array list:
                ArrayList<ArrayList<Integer>> t2 = new ArrayList<>();
                ArrayList<Integer> t1 = new ArrayList<>();
                // I start pathfindind at x1, y1, end at x2, y2
                int x1 = this.children.indexOf(child) * 25 + 25 + this.column * 100;
                int y1 = this.level * 250 + 125;
                int x2 = child.parents.indexOf(this) * 25 + 25 + child.column * 100;
                int y2 = child.level * 250 + 125;
                // add to the temporary arrays
                t1.add(x1);
                t1.add(y1);
                t1.add(x1);
                t1.add(y1);
                t2.add(t1);
                // call the recursive function.
                paths.add(findPathTo(x2, y2, x1, y1, t2, 5));
            }
        }
    }
    public ArrayList<Integer> t = new ArrayList<>();
    public ArrayList<ArrayList<Integer>> findPathTo(int x1, int y1, int x2, int y2, ArrayList<ArrayList<Integer>> pathCur, int i){
        // t: temporary. This little bit adds this location to the current path(pathCur)
        t = new ArrayList<>(4);
        t.add(pathCur.get(pathCur.size()-1).get(2));
        t.add(pathCur.get(pathCur.size()-1).get(3));
        // x2, y2 are my location
        t.add(x2);
        t.add(y2);
        if(!lines.containsKey(t)){
            // if lines doesn't have this paticular line in it, add it.
            lines.put(t, false);
        }
        if (!lines.get(t)) {
            // if im allowed to be here
            pathCur.add(t);
            // add it to the path
            lines.remove(t);
            lines.put(t, true);
            // and tell everyone else its taken.
            if (x1 == x2 && y1 == y2) {
                // if i am done, return the current path
                return pathCur;
            } else {
                // otherwise,copy the current path, and firgure out using a greedy algorithm where I should go next
                ArrayList<ArrayList<Integer>> pathCurs = new ArrayList<>(pathCur);
                int x3 = 0;
                int y3 = 0;
                // greedy alorithm
                if (x2-x1 != 0){
                    x3 = 5*(x1-x2)/Math.abs(x1-x2);
                }
                if (Math.abs(y2-(y1-i)) > 5 + 5*i){
                    y3 = 5*((y1-i)-y2)/Math.abs((y1-i)-y2);
                }
                else if(x1 - x2 == 0){
                    y3 = 5*(y1-y2)/Math.abs(y1-y2);
                }
                ArrayList<ArrayList<Integer>> a = null;
                int i1 = 0;
                while (a == null){
                    // if it works, do it
                    if(i1 == 0){
                        a = findPathTo(x1, y1, x2 + x3, y2 + y3, pathCurs, i1+i);
                        i1++;
                    }
                    else{
                        // otherwise, try these emergency strategies in order
                        pathCurs = new ArrayList<>(pathCur);
                        if (i1 == 1){
                            a = findPathTo(x1, y1, x2 + x3, y2 + y3, pathCurs, i1+i);
                        }
                        else if (i1 == 2){
                            a = findPathTo(x1, y1, x2 + x3, y2 + y3-5, pathCurs, i1+i);
                        }
                        else if (i1 == 3){
                            a = findPathTo(x1, y1, x2 + x3+5, y2 + y3-5, pathCurs, i1+i);
                        }
                        else{
                            // give up, and tell noah he screwed up
                            break;
                        }
                        i1++;
                    }
                }
                return a;
            }
        }
        else{
            // tell the prevoius call I am not supposed to be here.
            return null;
        }
    }
}
