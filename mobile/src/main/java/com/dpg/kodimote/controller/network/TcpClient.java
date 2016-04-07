package com.dpg.kodimote.controller.network;

import android.util.Log;

import com.dpg.kodimote.models.Utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Project Kodimote created by enriquedelpozogomez on 20/02/16.
 */
public class TcpClient {
    private static final String TAG = TcpClient.class.getSimpleName();

    public static final String SERVER_IP = Utils.getInstance().getCurrentHostIP();
    public static final int SERVER_PORT = 9090;
    // message to send to the server
    private String mServerMessage;
    // sends message received notifications
    private OnMessageReceived mMessageListener = null;
    // while this is true, the server will continue running
    private boolean mRun = false;
    // used to send messages
    private PrintWriter mBufferOut;
    // used to read messages from the server
    private BufferedReader mBufferIn;


    /**
     * Constructor of the class. OnMessagedReceived listens for the messages received from server
     */
    public TcpClient(OnMessageReceived listener) {
        mMessageListener = listener;
    }

    /**
     * Sends the message entered by client to the server
     *
     * @param message text entered by client
     */
    public void sendMessage(String message) {
        if (mBufferOut != null && !mBufferOut.checkError()) {
            mBufferOut.println(message);
            mBufferOut.flush();
        }
    }

    /**
     * Close the connection and release the members
     */
    public void stopClient() {
        Log.d(TAG, "stopClient");

        // send mesage that we are closing the connection
        //sendMessage(Constants.CLOSED_CONNECTION + "Kazy");

        mRun = false;

        if (mBufferOut != null) {
            mBufferOut.flush();
            mBufferOut.close();
        }

        mMessageListener = null;
        mBufferIn = null;
        mBufferOut = null;
        mServerMessage = null;
    }

    public void run() {

        mRun = true;

        try {
            //here you must put your computer's IP address.
            InetAddress serverAddr = InetAddress.getByName(SERVER_IP);

            Log.d(TAG, "C: Connecting...");

            //create a socket to make the connection with the server
            Socket mSocket = new Socket(Utils.getInstance().getCurrentHostIP(), SERVER_PORT);


            try {
                Log.d(TAG, "inside try catch");
                //sends the message to the server
                mBufferOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(mSocket.getOutputStream())), true);

                //receives the message which the server sends back
                mBufferIn = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
                // send login name
                //sendMessage(Constants.LOGIN_NAME + PreferencesManager.getInstance().getUserName());
                sendMessage("Hi");
                //in this while the client listens for the messages sent by the server
                while (mRun) {
                    char[] buf = new char[1024];

                    if (mBufferIn.ready()) {
                        mBufferIn.read(buf);
                        mServerMessage = String.copyValueOf(buf).replace("\u0000", "");

                        if (mServerMessage != null && mMessageListener != null) {
                            //call the method messageReceived from MyActivity class
                            mMessageListener.messageReceived(mServerMessage);
                            Log.d(TAG, mServerMessage);
                            mServerMessage = null;
                        }
                    }
                }
                Log.d(TAG, "S: Received Message: '" + mServerMessage + "'");

            } catch (Exception e) {

                Log.d(TAG, "S: Error", e);

            } finally {
                //the socket must be closed. It is not possible to reconnect to this socket
                // after it is closed, which means a new socket instance has to be created.
                try {
                    mSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (ConnectException e) {

            Log.d(TAG, "Ce: Error"+ e.getLocalizedMessage());
        } catch (UnknownHostException e) {
            Log.d(TAG, "Ce2: Error"+ e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Ce3: Error"+ e.getMessage());
        }

    }

    //Declare the interface. The method messageReceived(String message) will must be implemented in the MyActivity
    //class at on asynckTask doInBackground
    public interface OnMessageReceived {
        public void messageReceived(String message);
    }
}