/***
 * Stores a single logged attention level from a single base station
 * @author Brian Turchyn
 ***/
package ca.umanitoba.cs.comp4720;

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
    
    public AttentionLevel ( String packetInput ) {
        String[] input = new String[3];
        
        // Parse out the 3 parts of the String
        int index = packetInput.indexOf(NEWLINE);
        int index2 = packetInput.indexOf(NEWLINE, index + 1);
        
        if ( index >= 0 ) {
            input[0] = packetInput.substring(0, index);
        }
        
        if ( index2 >= 0 ) {
            input[1] = packetInput.substring(index + 1, index2);
            input[2] = packetInput.substring(index2 + 1);
        }
        
        // Populate the variables
        this.stationID = Long.parseLong(input[0]);
        this.attentionLevel = Integer.valueOf(input[1]).intValue();
        this.timestamp = Long.parseLong(input[2]);
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
        
        System.out.println(other.getTimestamp() + " vs " + getTimestamp());
        if ( other.getTimestamp() != getTimestamp() ) result = false;
        System.out.println(other.getAttentionLevel() + " vs " + getAttentionLevel());
        if ( other.getAttentionLevel() != getAttentionLevel() ) result = false;
        System.out.println(other.getStationID() + " vs " + getStationID());
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
    
    public void setAttentionLevel( int attention ) {
        System.out.println("Updating the value to " + attention);
        this.attentionLevel = attention;
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
