package main.java.client;


import main.java.serveur.Receiver;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Emitter extends Remote {

    String getName() throws RemoteException;

    Emitter getClient() throws RemoteException;

    void sendMessages(Receiver to, String message) throws RemoteException, MalformedURLException, NotBoundException;

    void setName(String name) throws RemoteException;
}
