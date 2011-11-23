/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sunspotworld;

import java.util.Vector;

/**
 *
 * @author brian
 */
public class AttentionHistory {
    private Vector history;
    
    public AttentionHistory() {
        history = new Vector();
    }
    
    public int attentionExists ( AttentionLevel att ) {
        AttentionLevel hID;
        int result = -1;
        
        for ( int i = 0; i < this.history.size(); i++ ) {
            try {
                hID = (AttentionLevel)this.history.elementAt(i);
                if ( hID.equals(att) ) {
                    result = i;
                    i = this.history.size();
                }
            } catch (ClassCastException e) {
                // HA, caught it!
            }
        }
        
        return result;
    }
    
    public boolean updateAttention ( AttentionLevel newAtt, int index ) {
        boolean result = true;
        
        this.history.setElementAt(newAtt, index);
        
        return result;
    }
    
    public int getAverageAttention() {
        AttentionLevel hID;
        int result = 0;
        
        for ( int i = 0; i < this.history.size(); i++ ) {
            try {
                hID = (AttentionLevel)this.history.elementAt(i);
                result += hID.getAttentionLevel();
            } catch (ClassCastException e) {
                System.out.print("On average calculation, got an object other than AttentionLevel");
            } catch (NullPointerException e) {
                System.out.print("On average calculation, got a null AttentionLevel");
            }
        }
        
        return result / this.history.size();
    }
}
