/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.umanitoba.cs.comp4720;


import ca.umanitoba.cs.comp4720.Actions;
import com.sun.spot.core.util.Utils;
import javax.microedition.io.Connector;
import javax.microedition.io.Datagram;
import javax.microedition.io.DatagramConnection;
import com.sun.spot.multihop.io.j2me.radiogram.RadiogramConnection;
import java.io.IOException;


/**
 *
 * @author kylekaufmann
 */
public class NetRequests 
{
        private Actions actions;
        
        public NetRequests(Actions actions)
        {
            this.actions = actions;
        }
                
        public void startReceiverThread() 
        {
        new Thread() {
            public void run() {
                String tmp = null;
                RadiogramConnection dgConnection = null;
                Datagram dg = null;
                
                try {
                    dgConnection = (RadiogramConnection) Connector.open("radiogram://:42");
                    // Then, we ask for a datagram with the maximum size allowed
                    dg = dgConnection.newDatagram(dgConnection.getMaximumLength());
                } catch (IOException e) {
                    System.out.println("Could not open radiogram receiver connection");
                    e.printStackTrace();
                    return;
                }
                
                while(true){
                    try {
                        dg.reset();
                        dgConnection.receive(dg);
                        tmp = dg.readUTF();
                        System.out.println("Received: " + tmp + " from " + dg.getAddress());
                    } catch (IOException e) {
                        System.out.println("Nothing received");
                    }
                }
            }
        }.start();
    }
    
    /**
     * The sender thread sends a string each 5000 ms
     */
    synchronized public void startSenderThread() 
    {
        new Thread() {
            public void run() {
                // We create a DatagramConnection
                DatagramConnection dgConnection = null;
                Datagram dg = null;
                try {
                    // The Connection is a broadcast so we specify it in the creation string
                    dgConnection = (DatagramConnection) Connector.open("radiogram://broadcast:42");
                    // Then, we ask for a datagram with the maximum size allowed
                    dg = dgConnection.newDatagram(dgConnection.getMaximumLength());
                } catch (IOException ex) {
                    System.out.println("Could not open radiogram broadcast connection");
                    ex.printStackTrace();
                    return;
                }
                
                while(true){
                    try {
                        // We send the message (UTF encoded)
                        dg.reset();
                        dg.writeUTF("Atention level: "+actions.getAttention());
                        dgConnection.send(dg);
                        System.out.println("Broadcasting Attention: "+actions.getAttention());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    Utils.sleep(5000);
                }
            }
        }.start();
    }
}
