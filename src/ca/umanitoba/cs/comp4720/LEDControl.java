/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.umanitoba.cs.comp4720;

import com.sun.spot.core.resources.Resources;
import com.sun.spot.core.resources.transducers.ITriColorLEDArray;

/**
 *
 * @author brian
 */
public class LEDControl {
    private ITriColorLEDArray leds = (ITriColorLEDArray) Resources.lookup(ITriColorLEDArray.class);
    
    public static final int MIN_VALUE = 0;
    public static final int MAX_VALUE = 8;
    
    public static final int OFF_VALUE = 0;
    public static final int ON_VALUE = 25;
    
    public LEDControl() {
        
    }
    
    public void off() {
        leds.setOff();
    }
    
    public void on() {
        leds.setOn();
    }
    
    public void setOn ( int numberOn ) {
        System.out.println("Got a numberOn value of " + numberOn);
        if ( numberOn >= MIN_VALUE && numberOn <= MAX_VALUE ) {
            System.out.println("Got a numberOn value of " + numberOn);
            off();
            for ( int i = 0; i < numberOn; i++ ) {
                leds.getLED(i).setOn();
            }
        }
    }
    
    public void red() {
        leds.setRGB(ON_VALUE, OFF_VALUE, OFF_VALUE);
    }
    
    public void green() {
        leds.setRGB(OFF_VALUE, ON_VALUE, OFF_VALUE);
    }
    
    public void blue() {
        leds.setRGB(OFF_VALUE, OFF_VALUE, ON_VALUE);
    }
}
