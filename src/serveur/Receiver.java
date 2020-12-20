package serveur;

import client.Emitter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public interface Receiver extends Remote {

	//ObservableList<String> listMessages = FXCollections.observableArrayList();



    void addClient(String pseudo) throws RemoteException;
	void removeClient(String pseudo) throws RemoteException;
	void initClient(List<String> clients) throws RemoteException;
	void receive(Emitter sender, String msg) throws RemoteException;
	ArrayList<String> getClients() throws RemoteException;
	ArrayList<String> getMsg() throws RemoteException;

	ArrayList<Receiver> getAllReceiver() throws RemoteException;

	void clearMsg() throws RemoteException;


}
