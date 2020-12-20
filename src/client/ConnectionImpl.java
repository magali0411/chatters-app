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

public class ConnectionImpl extends UnicastRemoteObject implements Connection {

    ArrayList<String> listClient = new ArrayList<String>();
    public static final String port = "1099";

    public ConnectionImpl() throws RemoteException {
        super();
    }


    @Override
    public Emitter connect(String name, Receiver re) throws RemoteException, MalformedURLException, NotBoundException {

        Registry registry = LocateRegistry.getRegistry(port);

        Emitter emitter = (Emitter) Naming.lookup("rmi://localhost:" + port +"/chat");
        emitter.setName(name);

        listClient.add(name);

        re.initClient(listClient);
        //System.out.println("Liste de client connectés : " + this.listClient.toString());

        return emitter;
    }

    @Override
    public void disconnect(String pseudo) throws RemoteException {

    }
}
