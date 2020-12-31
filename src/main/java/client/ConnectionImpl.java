package main.java.client;

import main.java.serveur.Receiver;

import java.io.Serial;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionImpl extends UnicastRemoteObject implements Connection, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final HashMap<String, Receiver> dicoProxy = new HashMap<>();

    private static final Logger logger = Logger.getLogger("connection");

    public ConnectionImpl() throws RemoteException {
        super();
    }


    @Override
    public Emitter connect(String name, Receiver re) throws RemoteException, MalformedURLException, NotBoundException {


        re.initClient(getAllusername());

        Emitter emitter = new EmitterImpl();
        emitter.setName(name);

        dicoProxy.put(name,re);
        re.addClient(name);


        logger.log(Level.INFO,"'{} s'est connecté au chat",name);

        return emitter;
    }

    @Override
    public void disconnect(String pseudo) throws RemoteException {
        dicoProxy.remove(pseudo);
        Receiver reSuppr = this.getReceiver(pseudo);
        if (reSuppr != null)
            reSuppr.removeClient(pseudo);
        else
            logger.log(Level.WARNING,"Impossible de déconnecter le client {0}", pseudo);

    }

    public List<Receiver> getAllReceiver(){
        List<Receiver> allR = new ArrayList<>();
        for (Receiver r : dicoProxy.values()) {
            allR.add(r);
        }
        return allR;
    }

    public List<String> getAllusername(){
        List<String> allU = new ArrayList<>();
        for (String username : dicoProxy.keySet()) {
            allU.add(username);
        }
        return allU;
    }

    @Override
    public Receiver getReceiver(String name){
        Receiver re = null;
        for (Map.Entry<String, Receiver> entry : dicoProxy.entrySet()) {
            String reName = entry.getKey();
            if ( reName.equals(name) )
                re = dicoProxy.get(reName);
        }
        return re;
    }

    @Override
    public void synchronise() throws RemoteException {
        for (Receiver re : getAllReceiver()) {
            re.initClient(getAllusername());
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ConnectionImpl that = (ConnectionImpl) o;
        return Objects.equals(dicoProxy, that.dicoProxy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), dicoProxy);
    }
}
