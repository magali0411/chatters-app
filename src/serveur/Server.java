package serveur;

import client.EmitterImpl;
import client.ConnectionImpl;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;


public class Server {

    public static void main(String[] args) throws RemoteException, MalformedURLException, NotBoundException {
        // registry creation
        int port = 1099;
        LocateRegistry.createRegistry(port);

//	    	Scanner s=new Scanner(System.in);
//	    	System.out.println("Bonjour, veuillez entrer votre nom :");
//	    	String name=s.nextLine().trim();
//
        // component instanciation
        EmitterImpl chat = new EmitterImpl();
        ConnectionImpl connection = new ConnectionImpl();
        ReceiverImpl serveur = new ReceiverImpl();

        // component instanciation and implicit activation

        //HelloImpl myComponent = new HelloImpl();
        //DialogueImpl test = new DialogueImpl();

        //publication of component reference in the registry
        //Naming.rebind("Hello", myComponent);
        //Naming.rebind("Dialogue", test);
        Naming.rebind("chat", chat);
        Naming.rebind("connection", connection);
        Naming.rebind("serveur", serveur);


        //Receiver serveur = (Receiver) Naming.lookup("rmi://localhost:" + port +"/receiver");
        //Receiver serveur = (Receiver) Naming.lookup("rmi://localhost:" + port +"/receiver");

        System.out.println("Serveur actif");

//			while(true){
//	    		String msg=s.nextLine().trim();
//	    		if (server.getClient()!=null){
//	    			Chat client=server.getClient();
//	    			msg="["+server.getName()+"] "+msg;
//	    			client.send(msg);
//	    		}
//	    	}

    }

}
