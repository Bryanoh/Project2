/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.cbse.common.data;

/**
 *
 * @author Bryan
 */
public class Square {
    
    public Position pos;
    private int gCost; // cost to move from start to this.
    private int hCost; // cost to move from this to end.
    private int fCost; // total score/cost
    
    public Square(Position pos) {
        this.pos = pos;
        gCost = 0;
        hCost = 0;
        fCost = 0;
    }
    
    public void setGCost(int gCost) {
        this.gCost = gCost;
    }
    
    public void setTotalCost(int g, int h) {
        gCost = g;
        hCost = h;
        fCost = g+h;
    }
    
    public int getGCost() {
        return gCost;
    }
    
    public int getHCost() {
        return hCost;
    }
    
    public int getCost() {
        return fCost;
    }
    
}
