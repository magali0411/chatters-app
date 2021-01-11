package client;


import serveur.Receiver;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Objects;


public class EmitterImpl extends UnicastRemoteObject implements Emitter {

    private String clientAssocie;

    public EmitterImpl() throws RemoteException {
        super();
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

    public void sendMessages(Receiver to, String s) throws RemoteException, MalformedURLException, NotBoundException {
        to.receive(this.getName(), s);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        EmitterImpl emitter = (EmitterImpl) o;
        return Objects.equals(clientAssocie, emitter.clientAssocie);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), clientAssocie);
    }
}
