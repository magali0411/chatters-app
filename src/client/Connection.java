package client;

import serveur.Receiver;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Connection extends Remote {

    Emitter connect(String pseudo, Receiver re) throws RemoteException, MalformedURLException, NotBoundException;
    void disconnect(String pseudo) throws RemoteException;

    Receiver getReceiver(String text) throws RemoteException;
}
