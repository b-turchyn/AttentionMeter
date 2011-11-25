/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.umanitoba.cs.comp4720;

import com.sun.spot.core.resources.Resources;
import com.sun.spot.core.resources.transducers.ITriColorLEDArray;
import com.sun.spot.core.util.Utils;

/**
 * The LEDControl class is what we use to control the LEDs on the 'Spot. Just
 * throws everything into a nice little wrapper.
 * @author brian
 */
public class LEDControl {
    private ITriColorLEDArray leds = (ITriColorLEDArray) Resources.lookup(ITriColorLEDArray.class);
    
    public static final int MIN_VALUE = 0;
    public static final int MAX_VALUE = 8;
    
    public static final int OFF_VALUE = 0;
    public static final int ON_VALUE = 50;
    
    public LEDControl() {
        pulse(2);
    }
    
    /**
     * Turn off all the LEDs
     */
    public void off() {
        leds.setOff();
    }
    
    /**
     * Turn on all the LEDs
     */
    public void on() {
        leds.setOn();
    }
    
    /**
     * Turn on LEDs from 0 to numberOn
     * @param numberOn the number of LEDs to turn on. 
     */
    public void setOn ( int numberOn ) {
        if ( numberOn >= MIN_VALUE && numberOn <= MAX_VALUE ) {
            off();
            for ( int i = 0; i < numberOn; i++ ) {
                leds.getLED(i).setOn();
            }
        }
    }
    
    /**
     * Sets all the LEDs to red
     */
    public void red() {
        leds.setRGB(ON_VALUE, OFF_VALUE, OFF_VALUE);
    }
    
    /**
     * Sets all the LEDs to green
     */
    public void green() {
        leds.setRGB(OFF_VALUE, ON_VALUE, OFF_VALUE);
    }
    
    /**
     * Sets all the LEDs to blue
     */
    public void blue() {
        leds.setRGB(OFF_VALUE, OFF_VALUE, ON_VALUE);
    }
    
    /**
     * Sets all the LEDs to white
     */
    public void white() {
        leds.setRGB(ON_VALUE, ON_VALUE, ON_VALUE);
    }
    
    public void pulse(int times) {
        for ( int i = 0; i < times; i++ ) {
            off();
            Utils.sleep(100);
            white();
            on();
            Utils.sleep(100);
        }
    }
}
