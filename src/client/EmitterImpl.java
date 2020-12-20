package client;

import serveur.Receiver;
import serveur.ReceiverImpl;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmitterImpl extends UnicastRemoteObject implements Emitter {

    //public Chat client=null;
    public String clientAssocie;
    public List<String> messages = new ArrayList<String>();

    Map<String, String> persons = new HashMap<String, String>();

    // Now add observability by wrapping it with ObservableList.

    public EmitterImpl() throws RemoteException {
    }

    public String getName() throws RemoteException {
        return clientAssocie;
    }

    @Override
    public void setName(String name) throws RemoteException {
        this.clientAssocie = name;
    }

    @Override
    public Emitter getClient() {
        return this;
    }

    public void sendMessages(String s) throws RemoteException, MalformedURLException, NotBoundException {
        if (!s.isEmpty())
            messages.add(s);
        System.out.println("[" + this.getName() + "] "+ s);

        //re.receive(this.getName(), s);
        Receiver re = (Receiver) Naming.lookup("rmi://localhost:" + 1099 +"/serveur");
        re.receive(this,s);

        //persons.put(to, s);
    }



}
