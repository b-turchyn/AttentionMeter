/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.umanitoba.cs.comp4720;

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
        private DatagramConnection dgConnection = null;
        private boolean sending = false;
        
        public NetRequests(Actions actions)
        {
            this.actions = actions;
        }
                
        public void startReceiverThread() 
        {
        new Thread() {
            public void run() {
                RadiogramConnection dgConnection = null;
                Datagram dg = null;
                
                try {
                    dgConnection = (RadiogramConnection) Connector.open("radiogram://:42");
                    // Then, we ask for a datagram with the maximum size allowed
                    dg = dgConnection.newDatagram(dgConnection.getMaximumLength());
                } catch (IOException e) {
                    System.out.println("Could not open radiogram receiver connection");
                    return;
                }
                
                while(true){
                    try {
                        dg.reset();
                        dgConnection.receive(dg);
                        AttentionLevel tmp = new AttentionLevel(dg.readUTF());
                        System.out.println("Received: " + tmp + " from " + dg.getAddress());
                        actions.parseAttentionLevel(tmp);
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
                Datagram dg = null;
                AttentionLevel broadcast;
                try {
                    // The Connection is a broadcast so we specify it in the creation string
                    dgConnection = (DatagramConnection) Connector.open("radiogram://broadcast:42");
                    // Then, we ask for a datagram with the maximum size allowed
                    dg = dgConnection.newDatagram(dgConnection.getMaximumLength());
                } catch (IOException ex) {
                    System.out.println("Could not open radiogram broadcast connection");
                    return;
                }
                
                while(true){
                    System.out.println("About to send regular broadcast");
                    actions.getAttentionLevel().updateTimestamp();
                    actions.parseAttentionLevel(actions.getAttentionLevel());
                    Utils.sleep(5000);
                }
            }
        }.start();
    }
    
    synchronized public void sendAttentionLevel ( AttentionLevel att ) {
        // Make sure we're not sending
        while ( sending ) {
            Utils.sleep(10);
            System.out.println("Sleeping");
        }
        sending = true;
        System.out.println("Sending");
        
        try {
            Datagram dg = dgConnection.newDatagram(dgConnection.getMaximumLength());
            dg.writeUTF(att.toString());
            dgConnection.send(dg);
            System.out.println("Broadcasting Attention: "+actions.getAttention());
        } catch ( IOException e ) {
            // FAILBUS EXPRESS!
        }
        
        sending = false;
    }
}
