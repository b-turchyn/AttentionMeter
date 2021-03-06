/*
 * SunSpotApplication.java
 *
 * Created on Nov 22, 2011 10:26:24 AM;
 */


package org.sunspotworld;

import ca.umanitoba.cs.comp4720.NetRequests;
import ca.umanitoba.cs.comp4720.Actions;
import ca.umanitoba.cs.comp4720.AttentionLevel;
import ca.umanitoba.cs.comp4720.LEDControl;
import com.sun.spot.core.resources.Resources;
import com.sun.spot.core.resources.transducers.IAccelerometer3D;
import com.sun.spot.core.resources.transducers.ISwitch;
import com.sun.spot.core.resources.transducers.ISwitchListener;
import com.sun.spot.core.resources.transducers.SwitchEvent;
import com.sun.spot.core.util.Utils;
import com.sun.spot.espot.service.BootloaderListenerService;
import com.sun.spot.ieee_802_15_4_radio.IEEE802_15_4Environment;
import com.sun.spot.ieee_802_15_4_radio.util.IEEEAddress;

import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import java.io.IOException;
import java.util.Random;


/**
 * The startApp method of this class is called by the VM to start the
 * application.
 * 
 * The manifest specifies this class as MIDlet-1, which means it will
 * be selected for execution.
 */
public class SunSpotApplication extends MIDlet implements ISwitchListener {

    private ISwitch sw1, sw2;      // Variables to hold the two switches.
    IAccelerometer3D acc = (IAccelerometer3D)Resources.lookup(IAccelerometer3D.class);
    private Actions actions;
    private NetRequests req;
    private LEDControl ledControl;
    private boolean baseStation;

    
    public boolean isBaseStation()
    {
        return baseStation;
    }
    
    private void monitorSwitches() throws IOException
    {
        sw1 = (ISwitch) Resources.lookup(ISwitch.class, "SW1");
        sw2 = (ISwitch) Resources.lookup(ISwitch.class, "SW2");
        
        //if we have a null switch, we can assume we are a base station.
        if (sw1 == null || sw2 == null)
        {
            throw new IOException();
        }
        else
        {
            sw1.addISwitchListener(this);       // enable automatic notification of switches
            sw2.addISwitchListener(this);     
        }
        
    }
    
    protected void startApp() throws MIDletStateChangeException {
        System.out.println("Hello, world");
        BootloaderListenerService.getInstance().start();   // monitor the USB (if connected) and recognize commands from host

        long ourAddr = IEEE802_15_4Environment.getIEEEAddress();
        System.out.println("Our radio address = " + IEEEAddress.toDottedHex(ourAddr));
        
        actions = new Actions(this);
        req = new NetRequests(actions);
        
        baseStation = false;
        
        try
        {
            monitorSwitches();
            ledControl = new LEDControl();
            //accelerometerBomb();
            req.startReceiverThread();
            req.startSenderThread();
        }
        catch(IOException p)
        {
            baseStation = true;
        }
        
        if (isBaseStation())
        {
            System.out.println("Hello, I'll be your base station today.");
            req.startReceiverThread();
            while(true)
            {
                //To Infinity....
                Utils.sleep(5000);
                System.out.println("Average is "+actions.getAverage());
            }
        }
        else
        {
            System.out.println("Hello, I'll be your sun spot client today.");
            while (true) 
            {   
                Utils.sleep(4500);

                //Turn them off
                ledControl.off();

                //Set the average
                ledControl.red();
                ledControl.setOn(actions.getAverage());

                Utils.sleep(500);

                ledControl.off();

                //Set the average
                ledControl.green();
                ledControl.setOn(actions.getAttention());

            }
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
            //leds.getLED(actions.getAttention()-1).setRGB(0,25,0); //dim green
            //leds.getLED(actions.getAttention()-1).setOn();
        }
        else
        {
            actions.decrementAtt();
            //leds.getLED(actions.getAttention()).setOff();
        }
        
        ledControl.green();
        ledControl.setOn(actions.getAttention());
        
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
    
    public NetRequests getNetRequests() {
        return req;
    }
    
    public void accelerometerBomb() {
        new Thread() {
                public void run() {
                    try {
                        while (true) {
                            double ax = acc.getAccelY();
                            if (ax >= 2) {
                                System.out.println("Bombing!");
                                AttentionLevel clone;
                                Random rand = new Random();
                                ledControl.pulse(2, 255);
                                for ( int i = 0; i < 100; i++ ) {
                                    clone = actions.getAttentionLevel().clone();
                                    clone.setStationID(rand.nextLong());
                                    actions.parseAttentionLevel(clone);
                                    Utils.sleep(10);
                                }
                                ledControl.pulse(2, 255);
                                Utils.sleep(5000);
                            }
                            Utils.sleep(10);  // check every 1/4 second
                        }
                    } catch (IOException e) {
                        
                    }
                }
        }.start();
    }
}
