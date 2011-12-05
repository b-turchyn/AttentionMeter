/***
 * Stores a single logged attention level from a single base station
 * @author Brian Turchyn
 ***/
package ca.umanitoba.cs.comp4720;
import java.io.IOException;
import javax.microedition.io.Datagram;

/**
 *
 * @author brian
 */
public class AttentionLevel {
    public static final String NEWLINE = "\n";
    public static final int MAX_VALUE = 8;
    public static final int MIN_VALUE = 0;
    
    private long stationID;
    private long timestamp;
    private int attentionLevel;
    
    public boolean parsePacket ( Datagram dg ) {
        //replaced by
        
        long stationID = 0;
        int attentionLevel = 0;
        long timestamp = 0;
        
        boolean valid = true;
        
        try {
            //checks for incorrect data
            stationID = dg.readLong();
            attentionLevel = dg.readInt();
            timestamp = dg.readLong();
        }
        catch (IOException e) 
        {
        }
        
        //Check if payload is valid
        //System.out.println("ID:  "+stationID);
        if (stationID < 0 || stationID > Long.MAX_VALUE)
            valid = false;
       
        //System.out.println("ATT: "+attentionLevel);
        if (attentionLevel < 0 || attentionLevel > 8)
            valid = false;
        
        //System.out.println("TIM: "+timestamp);
        if (timestamp < 0 || timestamp > Long.MAX_VALUE)
            valid = false;
        
        if (valid) //Add the data
        {
            this.stationID = stationID;
            this.attentionLevel = attentionLevel;
            this.timestamp = timestamp;
        }
        
        return valid;
    }
    
    public AttentionLevel() 
    {
        //Create new Attention level, but packet must be validated.
    }
    
    
    public AttentionLevel(long stationID, long timestamp, int attentionLevel) {
        this.stationID = stationID;
        this.timestamp = timestamp;
        this.attentionLevel = attentionLevel;
    }
    
    public long getStationID() {
        return this.stationID;
    }
    
    public long getTimestamp() {
        return this.timestamp;
    }
    
    public int getAttentionLevel() {
        return this.attentionLevel;
    }
    
    public boolean equals( AttentionLevel other ) {
        boolean result = true;
        
        //System.out.println(other.getTimestamp() + " vs " + getTimestamp());
        if ( other.getTimestamp() != getTimestamp() ) result = false;
        //System.out.println(other.getAttentionLevel() + " vs " + getAttentionLevel());
        if ( other.getAttentionLevel() != getAttentionLevel() ) result = false;
        //System.out.println(other.getStationID() + " vs " + getStationID());
        if ( other.getStationID() != getStationID() ) result = false;
        
        return result;
    }
    
    public boolean equalsStationID( AttentionLevel other ) {
        boolean result = true;
         
        if ( other.getStationID() != getStationID() ) result = false;
        
        return result;
    }
    
    public String toString() {
        return getStationID() + "\n" + getAttentionLevel() + "\n" + getTimestamp();
    }
    
    public Datagram toSend(Datagram dg) {
        try {            
            dg.writeLong(getStationID());
            dg.writeInt(getAttentionLevel());
            dg.writeLong(getTimestamp());
        
            return dg;
        }
        catch (IOException e) {
            //All aboard the failboat.
            return null;
        }
    }
    
    public void setAttentionLevel( int attention ) {
        System.out.println("Updating the value to " + attention);
        this.attentionLevel = attention;
        this.timestamp = System.currentTimeMillis();
    }
    
    public void setStationID( long stationID ) {
        this.stationID = stationID;
        this.timestamp = System.currentTimeMillis();
    }
    
    public void incrementAttentionLevel() {
        System.out.println("In increment; currValue = " + this.attentionLevel);
        if ( this.attentionLevel < MAX_VALUE ) {
            System.out.println("In increment");
            setAttentionLevel( ++this.attentionLevel );
        }
    }
    
    public void decrementAttentionLevel() {
        System.out.println("In decrement; currValue = " + this.attentionLevel);
        if ( this.attentionLevel > MIN_VALUE ) {
            System.out.println("In increment");
            setAttentionLevel( --this.attentionLevel );
        }
    }
    
    public void updateTimestamp() {
        long time = System.currentTimeMillis();
        System.out.println("Updating timestamp from " + this.timestamp + " to " + time);
        this.timestamp = time;
    }
    
    public AttentionLevel clone() {
        AttentionLevel result = new AttentionLevel(stationID,
                                                   timestamp,
                                                   attentionLevel);
        return result;
    }
}
