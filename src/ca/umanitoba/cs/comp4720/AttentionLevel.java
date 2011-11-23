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
        
        if ( other.getStationID() != getStationID() ) result = false;
        if ( other.getTimestamp() != getTimestamp() ) result = false;
        if ( other.getAttentionLevel() != getAttentionLevel() ) result = false;
        
        return result;
    }
    
    public String toString() {
        return getStationID() + "\n" + getAttentionLevel() + "\n" + getTimestamp();
    }
}
