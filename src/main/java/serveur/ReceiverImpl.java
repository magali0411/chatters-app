package main.java.serveur;

import java.io.Serial;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReceiverImpl extends UnicastRemoteObject implements Receiver, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final List<String> listMessages = new ArrayList<>();
    private List<String> clientList = new ArrayList<>();

    private static final Logger logger = Logger.getLogger("Serveur-app");


    public ReceiverImpl() throws RemoteException {
        super();
    }

    @Override
    public void addClient(String pseudo) throws RemoteException {

        logger.log(Level.INFO,"Client ajouté : {0}", pseudo);
        clientList.add(pseudo);
    }

    @Override
    public void removeClient(String pseudo) throws RemoteException {

        clientList.remove(pseudo);
    }

    @Override
    public void initClient(List<String> clients) throws RemoteException {
        clientList = clients;
    }

    @Override
    public void receive(String sender, String msg) throws RemoteException {

        listMessages.add("[" + sender+"] " + msg + "\n");

    }

    @Override
    public ArrayList<String> getClients() throws RemoteException {
        return (ArrayList<String>) clientList;
    }

    @Override
    public ArrayList<String> getMsg() {
        return (ArrayList<String>) listMessages;
    }


    @Override
    public void clearMsg() throws RemoteException {
        listMessages.clear();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ReceiverImpl receiver = (ReceiverImpl) o;
        return Objects.equals(listMessages, receiver.listMessages) && Objects.equals(clientList, receiver.clientList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), listMessages, clientList);
    }
}
