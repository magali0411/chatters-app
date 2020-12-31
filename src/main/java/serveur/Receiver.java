package main.java.serveur;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public interface Receiver extends Remote {

    void addClient(String pseudo) throws RemoteException;
	void removeClient(String pseudo) throws RemoteException;
	void initClient(List<String> clients) throws RemoteException;
	void receive(String sender, String msg) throws RemoteException;
	ArrayList<String> getClients() throws RemoteException;
	ArrayList<String> getMsg() throws RemoteException;

	void clearMsg() throws RemoteException;


}
