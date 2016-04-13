package com.jeremy.polytech.btmat256v5.BT;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.UUID;

import android.os.Handler;


import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Message;

import com.jeremy.polytech.btmat256v5.Convert;
import com.jeremy.polytech.btmat256v5.UI.Home;


/**
 * Cette classe définie un objet Interface Bluetooth qui va gérer la communication bluetooth.
 * Cette classe n'est pas la plus optimisé mais elle est simple à comprendre et à exploiter.
 */
public class BtInterface {


    /**
     * BluetoothDevice est l'appareil bluetooth avec lequel on communique
     */
    private BluetoothDevice device = null;
    /**
     * BluetoothSocket représente le lien de communication entre nos 2 appareils bluetooth et
     * est déduit créé à partir de BluetoothDevice
     */
    private BluetoothSocket socket = null;

    /**
     * Cet InputStream est issu du BluetoothSocket et reçoit toutes les données entrantes en Bluetooth
     */
    private InputStream receiveStream = null;
    /**
     * Cet OutputStream est issu du BluetoothSocket, on y ecrit les bytes qu'on veut envoyer.
     */
    private OutputStream sendStream = null;


    /**
     * UUID de communication
     * 00001101 pour Com Série BLUETOOTH
     * 8ce255c0 pour Android Bluetooth Chat
     */
    private UUID MY_UUID=UUID.fromString(
            "00001101-0000-1000-8000-00805F9B34FB");
            //"8ce255c0-200a-11e0-ac64-0800200c9a66");


    /**
     * Ce Handler est utilisé pour communiquer avec l'activité qui utilise l'interface Bluetooth
     * afin de réagir au connexion ou échec de connection par exemple.
     */
    Handler handler;

    //Si reception de données
    /*
    private ReceiverThread receiverThread;


    */

    //Constructeur si Réception de données
    /*
    public BtInterface(Handler hstatus, Handler h) {
        Set<BluetoothDevice> setpairedDevices = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
        BluetoothDevice[] pairedDevices = (BluetoothDevice[]) setpairedDevices.toArray(new BluetoothDevice[setpairedDevices.size()]);

        for(int i=0;i<pairedDevices.length;i++) {
            if(pairedDevices[i].getName().contains("ModuleBluetooth")) {
                device = pairedDevices[i];
                try {
                    socket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
                    receiveStream = socket.getInputStream();
                    sendStream = socket.getOutputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }

        handler = hstatus;

        receiverThread = new ReceiverThread(h);
    }
    */

    /**
     * new BtInterface(BluetoothDevice mBluetoothDevice,Handler hstatus)
     * construit une interface bluetooth à partir de l'appareil bluetooth placé en paramètre
     * et communiquera avec la classe qui l'a crée via le Handler placé en paramètre
     * @param mBluetoothDevice
     * @param hstatus
     */
    public BtInterface(BluetoothDevice mBluetoothDevice,Handler hstatus) {

        device = mBluetoothDevice;
        try {
            socket = device.createRfcommSocketToServiceRecord(MY_UUID);
            receiveStream = socket.getInputStream();
            sendStream = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        handler=hstatus;

        //receiverThread = new ReceiverThread(hstatus);
    }

    /**
     * Envoie par Bluetooth la chaine placée en parametre
     * @param data
     */
    public void sendData(String data) {
        sendData(data, false);
    }

    /**
     * Appelée par sendData(String data)
     * convertie la chaine data en byte[] pour l'écrire dans le outputStream
     * @param data
     * @param deleteScheduledData
     */
    public void sendData(String data, boolean deleteScheduledData) {
        try {
            sendStream.write(data.getBytes());
            sendStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ecrit dans le OutputStream le tableau byte[] placé en paramètre
     * @param data
     */
    public void sendBrutData(byte[] data){
        try {
            sendStream.write(data);
            sendStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Cette classe permet d'envoyer un motif fait de 256 '0' et '1' sous forme compressée.
     * On place un caractère de départ 'M' et on convertit la chaine "01010...01"
     * en tableau de 32 bytes brut {01010101,...,01000101}
     * on écrit donc dans le OutputStream le tableau des 33bytes résultants.
     * @param pattern
     */
    public void sendPattern(String pattern){
        //Conversion de la chaine "0101..01" en tableau de byte[] brut la représentant
        byte[] t=Convert.toByteTab(pattern);
        byte[] result=new byte[t.length+1];
        char Code='M';
        result[0]=(byte)Code;
        //Le premier element est le code de motif
        for(int i=1;i<t.length;i++){
            result[i]=t[i-1];
        }
        sendBrutData(result);
    }

    /**
     * connect() permet de se connecter à l'appareil bluetooth en éxécutant socket.connect() qui se connecte
     * à l'appareil bluetooth s'il est dispo.
     * On renvoie les résultat des commandes via le Handler à la classe qui a créé l'objet.
     */
    synchronized public void connect(){

        Thread thread=new Thread() {

                @Override public void run() {

                    try {
                        socket.connect();

                        Message msg = handler.obtainMessage();
                        msg.arg1 = Home.CONNECTED_CODE;
                        handler.sendMessage(msg);


                        //receiverThread.start();

                    } catch (IOException e) {
                        Message msg = handler.obtainMessage();
                        msg.arg1 = Home.CONNECTION_FAILED_CODE;
                        handler.sendMessage(msg);
                    }
                }
        };
        thread.start();
    }

    /**
     * Cette méthode arrète le ferme la connection en éxécutant socket.close().
     * On renvoie les resultats de la commande à la classe qui à créée l'objet via l'Handler.
     */
    public void close() {
        try {
            socket.close();
            Message msg = handler.obtainMessage();
            msg.arg1 = Home.DISCONNECTED_CODE;
            handler.sendMessage(msg);
        } catch (IOException e) {
            Message msg = handler.obtainMessage();
            msg.arg1 = Home.DISCONNECTION_FAILED_CODE;
            handler.sendMessage(msg);
        }
    }

    public BluetoothDevice getDevice() {
        return device;
    }

    //-------------------------------------Reception de données uniquement ---------------------------
    /*
    private class ReceiverThread extends Thread {
        Handler handler;

        ReceiverThread(Handler h) {
            handler = h;
        }

        @Override public void run() {
            while(true) {
                try {
                    if(receiveStream.available() > 0) {

                        byte buffer[] = new byte[100];
                        int k = receiveStream.read(buffer, 0, 100);

                        if(k > 0) {
                            byte rawdata[] = new byte[k];
                            for(int i=0;i<k;i++)
                                rawdata[i] = buffer[i];

                            String data = new String(rawdata);

                            Message msg = handler.obtainMessage();
                            Bundle b = new Bundle();
                            b.putString("receivedData", data);
                            msg.setData(b);
                            handler.sendMessage(msg);
                        }
                    }
                } catch (IOException e) {
                    Message msg = handler.obtainMessage();
                    msg.arg1 = Home.DISCONNECTED_CODE;
                    handler.sendMessage(msg);
                }
            }
        }
    }
    */

}
