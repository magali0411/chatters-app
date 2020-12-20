package client;

import java.util.ArrayList;

public class User {

    public String name;
    public ClientUtil client;
    public ArrayList<String> messages = new ArrayList();


    //constructeur
    public User(String name, ClientUtil client) {
        this.name = name;
        this.client = client;
    }

    public User(String name) {
        this.name = name;
    }


    //getters and setters
    public String getName() {
        return name;
    }

    public ClientUtil getClient() {
        return client;
    }

    public ArrayList<String> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<String> messages) {
        this.messages = messages;
    }

    public void clear() {
        this.messages.clear();
    }

}
