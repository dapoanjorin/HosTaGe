package de.tudarmstadt.informatik.hostage.protocol;

import com.mbed.coap.client.CoapClient;
import com.mbed.coap.client.CoapClientBuilder;
import com.mbed.coap.server.CoapServer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.List;

import de.tudarmstadt.informatik.hostage.protocol.coapUtils.COAPHandler;
import de.tudarmstadt.informatik.hostage.wrapper.Packet;

public class COAP implements Protocol {
    private final static String defaultAddress="192.168.1.5";
    private static InetAddress address=null;
    static {
        try {
            address = InetAddress.getByName(defaultAddress);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    private final static int defaultPort = 5683;
    private int port=5683;
    private static boolean serverStarted = false; //prevents the server from starting multiple times from the threads
    private final static CoapServer server = CoapServer.builder().transport(address,defaultPort).build();


    public COAP() throws IOException {
        if(!serverStarted)
            startServer();
    }

    /**
     * Returns the port on which the protocol is running.
     *
     * @return The port used by the protocol (1-65535)
     */
    @Override
    public int getPort() {
        return port;
    }

    /**
     * Sets the port on the protocol.
     *
     * @param port The new port
     */
    @Override
    public void setPort(int port) {
        this.port=port;
    }

    /**
     * Returns whether the communication is ended and the connection should be
     * closed or not.
     *
     * @return true if the connection should be closed, false otherwise.
     */
    @Override
    public boolean isClosed() {
        return false;
    }

    /**
     * Returns whether the protocol uses a secure connection or not.
     *
     * @return true if SSL/TLS is used, false otherwise.
     */
    @Override
    public boolean isSecure() {
        return false;
    }

    /**
     * Determines the next response.
     *
     * @param requestPacket Last message received from the client.
     * @return Message to be sent to the client.
     */
    @Override
    public List<Packet> processMessage(Packet requestPacket) {
        return null;
    }

    /**
     * Returns the name of the protocol.
     *
     * @return String representation of the protocol.
     */
    @Override
    public String toString() {
        return "COAP";
    }

    /**
     * Specifies who starts the communication once the connection is
     * established.
     *
     * @return A value in TALK_FIRST.
     */
    @Override
    public TALK_FIRST whoTalksFirst() {
        return TALK_FIRST.SERVER;
    }

    /**
     * Starts the CoAP server.
     * @throws IOException
     */
    public CoapServer startServer() throws IOException {
        server.addRequestHandler("/*",new COAPHandler());
        server.start();
        serverStarted=true;

        return server;
    }

    /**
     * Returns a simple CoAP client.
     * @param serverAddress the server address with the port.
     * @param server the server instance.
     */
    public CoapClient getSimpleClient(InetSocketAddress serverAddress, CoapServer server){
        CoapClient client = CoapClientBuilder.clientFor(serverAddress, server);

        return client;
    }

    /**
     * Stops the broker and closes the port
     */

    public static void serverStop(){
        server.stop();
    }

}
