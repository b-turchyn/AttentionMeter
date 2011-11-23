/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sunspotworld;

/**
 *
 * @author kylekaufmann
 */
public class Actions 
{
    private int attention;
    
    public Actions()
    {
        attention = 0;
    }
    
    public void incrementAtt()
    {
        if (attention < 8)
            attention++;
    }
    
    public void decrementAtt()
    {
        if (attention > 0)
            attention--;
    }
    
    public int getAttention()
    {
        return attention;
    }
}
