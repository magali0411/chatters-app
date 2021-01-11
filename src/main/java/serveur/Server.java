package serveur;


import client.ConnectionImpl;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Server {

    public static void main(String[] args) throws RemoteException, MalformedURLException {

        Logger logger = Logger.getLogger("serveur-app");
        final int PORT = 1099;

        // registry creation
        LocateRegistry.createRegistry(PORT);

        // component instanciation
        ConnectionImpl connection = new ConnectionImpl();
        Naming.rebind("connection", connection);


        logger.log(Level.INFO, "Serveur actif");


    }

}
