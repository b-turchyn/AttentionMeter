/*
 * SunSpotApplication.java
 *
 * Created on Nov 22, 2011 10:26:24 AM;
 */

package org.sunspotworld;

import com.sun.spot.core.resources.Resources;
import com.sun.spot.core.resources.transducers.ISwitch;
import com.sun.spot.core.resources.transducers.ISwitchListener;
import com.sun.spot.core.resources.transducers.SwitchEvent;
import com.sun.spot.core.resources.transducers.ITriColorLED;
import com.sun.spot.core.resources.transducers.ITriColorLEDArray;
import com.sun.spot.core.resources.transducers.LEDColor;
import com.sun.spot.espot.service.BootloaderListenerService;
import com.sun.spot.ieee_802_15_4_radio.IEEE802_15_4Environment;
import com.sun.spot.ieee_802_15_4_radio.util.IEEEAddress;

import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import java.io.IOException;


/**
 * The startApp method of this class is called by the VM to start the
 * application.
 * 
 * The manifest specifies this class as MIDlet-1, which means it will
 * be selected for execution.
 */
public class SunSpotApplication extends MIDlet implements ISwitchListener {

    private ITriColorLEDArray leds = (ITriColorLEDArray) Resources.lookup(ITriColorLEDArray.class);
    private ISwitch sw1, sw2;      // Variables to hold the two switches.
    private Actions actions;
    private NetRequests req;

    private void monitorSwitches() throws IOException
    {
        sw1 = (ISwitch) Resources.lookup(ISwitch.class, "SW1");
        sw2 = (ISwitch) Resources.lookup(ISwitch.class, "SW2");

        sw1.addISwitchListener(this);       // enable automatic notification of switches
        sw2.addISwitchListener(this);       
        
    }
    
    protected void startApp() throws MIDletStateChangeException {
        System.out.println("Hello, world");
        BootloaderListenerService.getInstance().start();   // monitor the USB (if connected) and recognize commands from host

        long ourAddr = IEEE802_15_4Environment.getIEEEAddress();
        System.out.println("Our radio address = " + IEEEAddress.toDottedHex(ourAddr));
        
        actions = new Actions();
        req = new NetRequests(actions);
        
        try
        {
           monitorSwitches();
           req.startReceiverThread();
           req.startSenderThread();
        }
        catch(IOException p)
        {
        }
        
        ISwitch sw1 = (ISwitch) Resources.lookup(ISwitch.class, "SW1");
        while (true) {                  // done when switch is pressed
                      
        }
        //notifyDestroyed();                      // cause the MIDlet to exit
    }

        /**
     * These methods are the "call backs" that are invoked whenever the
     * switch is pressed or released. They are run in a new thread.
     *
     * @param sw the switch that was pressed/released.
     */
    public void switchPressed(SwitchEvent evt) {
        int switchNum = (evt.getSwitch() == sw1) ? 1 : 2;
        System.out.println("Switch " + switchNum + " pressed.");
        
        if (switchNum == 2) //Right side
        {
            actions.incrementAtt();
            leds.getLED(actions.getAttention()-1).setRGB(0,25,0); //dim green
            leds.getLED(actions.getAttention()-1).setOn();
        }
        else
        {
            actions.decrementAtt();
            leds.getLED(actions.getAttention()).setOff();
        }
        
        System.out.println("Attention Level: "+ actions.getAttention());
        
    }
    
    public void switchReleased(SwitchEvent evt) {
    }
    
    protected void pauseApp() {
        // This is not currently called by the Squawk VM
    }

    /**
     * Called if the MIDlet is terminated by the system.
     * It is not called if MIDlet.notifyDestroyed() was called.
     *
     * @param unconditional If true the MIDlet must cleanup and release all resources.
     */
    protected void destroyApp(boolean unconditional) throws MIDletStateChangeException {
    }
}
