/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.umanitoba.cs.comp4720;

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
    
    /**
     * Handles all of the Attention management
     * @param att
     * @return <b>true</b> on should send again, <b>false</b> on shouldn't.
     */
    public boolean doAttention ( AttentionLevel att ) {
        boolean result = false;
        int index[] = attentionExists ( att );
        
        // Was it found?
        if ( index[0] >= 0 ) {
            // Was it inexact (needs updating)?
            if ( index[1] == 0 ) {
                System.out.println("Will be sending out");
                updateAttention ( att, index[0] );
                result = true;
            } else {
                System.out.println("Exact match. Not resending.");
            }
        } else { // Was not found; insert and send.
            insertAttention ( att );
            result = true;
            System.out.println("Will be sending out");
        }
        
        return result;
    }
    
    /**
     * Checks if an AttentionLevel exists in the history
     * @param att
     * @return an int array of the following:<ul>
     * <li>The index that it was found at; -1 if not found</li>
     * <li>1 if it was an exact match; 0 if it was not</li>
     * </ul>
     */
    public int[] attentionExists ( AttentionLevel att ) {
        AttentionLevel hID;
        int[] result = {-1, 0};
        
        for ( int i = 0; i < this.history.size(); i++ ) {
            try {
                hID = (AttentionLevel)this.history.elementAt(i);
                // Do we have a station ID match?
                if ( hID.equalsStationID(att))  {
                    result[0] = i;
                    i = this.history.size();
                    if ( hID.equals( att ) ) {
                        result[1] = 1;
                    } else {
                        result[1] = 0;
                    }
                }
            } catch (ClassCastException e) {
                // HA, caught it!
            }
        }
        
        return result;
    }
    
    public boolean updateAttention ( AttentionLevel newAtt, int index ) {
        boolean result = true;
        
        this.history.setElementAt(newAtt.clone(), index);
        
        return result;
    }
    
    public boolean insertAttention ( AttentionLevel newAtt ) {
        boolean result = true;
        
        this.history.addElement(newAtt.clone());
        
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
