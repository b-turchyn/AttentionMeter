/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.umanitoba.cs.comp4720;

import com.sun.spot.ieee_802_15_4_radio.IEEE802_15_4Environment;
import javax.microedition.io.Datagram;
import org.sunspotworld.SunSpotApplication;

/**
 *
 * @author kylekaufmann
 */
public class Actions 
{
    private int average;
    private int attention;
    private AttentionHistory history;
    private AttentionLevel thisLevel;
    private SunSpotApplication parent;
    
    public Actions( SunSpotApplication parent )
    {
        // TODO: Get actual ID
        this.history = new AttentionHistory();
        thisLevel = new AttentionLevel(IEEE802_15_4Environment.getIEEEAddress(), System.currentTimeMillis(), 0 );
        average = 0;
        this.parent = parent;
    }
    
    public void incrementAtt()
    {
        thisLevel.incrementAttentionLevel();
        this.attention = thisLevel.getAttentionLevel();
    }
    
    public void decrementAtt()
    {
        thisLevel.decrementAttentionLevel();
        this.attention = thisLevel.getAttentionLevel();
    }
    
    public int getAttention()
    {
        return thisLevel.getAttentionLevel();
    }
    
    public AttentionLevel getAttentionLevel() {
        return thisLevel;
    }
    
    public int getAverage()
    {
        //updateAverage();
        return average;
    }
    
    public void parseAttentionLevel ( AttentionLevel att ) {
        System.out.println("Parsing obj from ID: " + att.getStationID());
        if ( this.history.doAttention(att) ) 
        {
            updateAverage();
            //Do not forward packets if you are a base station.
            if (!parent.isBaseStation())
                this.parent.getNetRequests().sendAttentionLevel(att);
        }
    }
    
    public void updateAverage() {
        this.average = history.getAverageAttention();
    }
}
