package client;

import serveur.Receiver;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConnectionImpl extends UnicastRemoteObject implements Connection {

    public HashMap<String,Receiver> dicoProxy = new HashMap<String,Receiver>();

    public static final String port = "1099";

    public ConnectionImpl() throws RemoteException {
        super();
    }


    @Override
    public Emitter connect(String name, Receiver re) throws RemoteException, MalformedURLException, NotBoundException {
        Emitter emitter = new EmitterImpl();
        emitter.setName(name);

        dicoProxy.put(name,re);

        re.initClient(getAllusername());
        System.out.println(name + " s'est connecté au chat");

        return emitter;
    }

    @Override
    public void disconnect(String pseudo) throws RemoteException {

    }

    public List<Receiver> getAllReceiver(){
        List<Receiver> allR = new ArrayList<Receiver>();
        for (Receiver r : dicoProxy.values()) {
            allR.add(r);
        }
        return allR;
    }

    public List<String> getAllusername(){
        List<String> allU = new ArrayList<String>();
        for (String username : dicoProxy.keySet()) {
            allU.add(username);
        }
        return allU;
    }

    @Override
    public Receiver getReceiver(String name){
        Receiver re = null;
        for (String reName : dicoProxy.keySet()) {
            if ( reName.equals(name) )
                re = dicoProxy.get(reName);
        }
        return re;
    }
}
