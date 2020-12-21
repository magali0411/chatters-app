package serveur;

import client.Emitter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class ReceiverImpl extends UnicastRemoteObject implements Receiver {

    public static List<String> listMessages = new ArrayList<>();
    public static List<String> clientList = new ArrayList<>();


    public ReceiverImpl() throws RemoteException {
        super();
        // TODO Auto-generated constructor stub
    }

    @Override
    public void addClient(String pseudo) throws RemoteException {
        System.out.println("Client ajouté : " + pseudo);

        clientList.add(pseudo);
    }

    @Override
    public void removeClient(String pseudo) throws RemoteException {

        //ChatImpl generalChat = new ChatImpl();
        //generalChat.removeClient(pseudo);
        clientList.remove(pseudo);
    }

    @Override
    public void initClient(List<String> clients) throws RemoteException {
        clientList = clients;
    }

    @Override
    public void receive(String sender, String msg) throws RemoteException {
        //System.out.println("J'ai bien reçu le message de "+ sender + " disant que " + msg);
        //for (Receiver re : this.getAllReceiver()) {
        //    re.receive(sender, msg);
        //}
        listMessages.add("[" + sender+"] " + msg + "\n");
        System.out.println("OUI LA VIE" + "[" + sender +"] " + msg + "\n");
        //return ("[" + sender +"] " + msg);
    }

    @Override
    public ArrayList<String> getClients() {
        return (ArrayList<String>) clientList;
    }

    @Override
    public ArrayList<String> getMsg() {
        return (ArrayList<String>) listMessages;
    }


    @Override
    public void clearMsg() throws RemoteException {
        this.listMessages.clear();
    }


}
