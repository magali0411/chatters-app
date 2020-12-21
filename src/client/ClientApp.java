package client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Timer;
import java.util.TimerTask;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import serveur.Receiver;
import serveur.ReceiverImpl;
import util.Message;


public class ClientApp extends Application {

    final static String defaultHost = "localhost";
    final static String defaultPort = "1099";


    // Inititalisation des éléments RMI
    Emitter emI ;
    Receiver reI;
    Connection connection;

    // Liste dynamique des messages du chat
    public ObservableList<String> listeMessages = FXCollections.observableArrayList();


    @Override
    public void start(Stage primaryStage) throws Exception {

        Group group = new Group();
        Scene scene = new Scene(group ,600, 300);
        scene.setFill(Color.WHITE);
        primaryStage.setTitle("Chat-app");
        primaryStage.setScene(chatScene(primaryStage));
        primaryStage.show();

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                System.out.println("Stage is closing");
            }
        });

    }

    public Scene chatScene(Stage primaryStage) {

        GridPane rootPane = new GridPane();
        rootPane.setPadding(new Insets(20));
        rootPane.setVgap(10);
        rootPane.setHgap(10);
        rootPane.setAlignment(Pos.CENTER);

        TextField pseudoField = new TextField();
        TextField hostNameField = new TextField(defaultHost);
        TextField portNumberField = new TextField(defaultPort);

        Label pseudoLabel = new Label("Pseudo ");
        Label hostNameLabel = new Label("Host Name");
        Label portNumberLabel = new Label("Port Number");
        Label errorLabel = new Label();


        Button submitBtn = new Button("Done");
        submitBtn.onMouseClickedProperty().set((MouseEvent t) -> {
            {
                //System.out.println(pseudoField.getText() + " " + portNumberField.getText());
                if (!pseudoField.getText().isEmpty() && !portNumberField.getText().isEmpty() && !hostNameField.getText().isEmpty() ) {

                    int port = Integer.parseInt(portNumberField.getText());
                    String name = pseudoField.getText();
                    System.out.println("name : " + name);


                    try {

                        // Instanciation de la connection au serveur
                        connection = (Connection) Naming.lookup("rmi://localhost:" + port +"/connection");

                        // instanciation du receiver
                        this.reI = new ReceiverImpl();
                        reI.addClient(name);

                        //Instanciation de l'emitteur
                        this.emI = (Emitter) connection.connect(name, reI);


                        // Toast
                        Alert popupConnect = Message.showPopupInfo("Connection réussie", "Vous êtes connecté(e) au serveur de chat en tant que "+ name );
                        popupConnect.show();

                    } catch (MalformedURLException | NotBoundException | RemoteException e) {
                        Alert popupPort = Message.showPopupAlert("Connection fail","Le port " + port + " du serveur " + hostNameField.getText() + " n'est pas accessible ");
                        popupPort.show();
                    }


                    if (emI != null && reI != null && connection != null) {
                        try {
                            primaryStage.setScene(clientScene(emI, reI, connection));
                        } catch (RemoteException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                } else {
                    Alert popupAlert = Message.showPopupAlert("Connection impossible", "Veuillez saisir des informations dans tous les champs");
                    popupAlert.show();
                }

            }
        });


        rootPane.add(pseudoField, 0, 0);
        rootPane.add(pseudoLabel, 1, 0);
        rootPane.add(hostNameField, 0, 1);
        rootPane.add(hostNameLabel, 1, 1);
        rootPane.add(portNumberField, 0, 2);
        rootPane.add(portNumberLabel, 1, 2);
        rootPane.add(submitBtn, 0, 3, 2, 1);
        rootPane.add(errorLabel, 0, 4);

        return new Scene(rootPane, 400, 400);

    }

    public Scene clientScene(Emitter emI, Receiver reI, Connection co) throws RemoteException {
        GridPane rootPane = new GridPane();
        rootPane.setPadding(new Insets(20));
        rootPane.setAlignment(Pos.CENTER);
        rootPane.setHgap(10);
        rootPane.setVgap(10);

//
//		ObservableList<String> observable = (ObservableList) (client.getMessages2("Albert"));
//
//		ListView<String> chatListView = new ListView<String>();
//		chatListView.setItems(observable);

        TextField chatTextField = new TextField();
        TextField destTextField = new TextField();
        Label destLabel = new Label("Destinaire");
        Label userLabel = new Label("Bonjour " + emI.getName());
        Label msgLabel = new Label("Messages");
        ListView chatBox = new ListView();
        Label bandeauClient = new Label();

        chatBox.setPrefHeight(250);
        chatBox.setDisable(true);
        chatBox.setMouseTransparent(true);

        Button clearBtn = new Button("Clear chat");


        // Mise à jour du chat
        EventHandler<ActionEvent> maj = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                try {
                    listeMessages.addAll(reI.getMsg());
                } catch (RemoteException ex) {
                    ex.printStackTrace();
                }
            }
        };

        // action event
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {

                if(!chatTextField.getText().isEmpty() && !destTextField.getText().isEmpty()) {
                    try {
                        listeMessages.addAll(reI.getMsg());
                        Receiver reDest = co.getReceiver(destTextField.getText());
                        if (reDest == null) {
                            Alert popupAlert = Message.showPopupAlert("Impossible d'envoyer le message", "L'utilisateur " + destTextField.getText() + " n'est pas connecté.");
                            popupAlert.show();
                        } else {
                            emI.sendMessages(reDest, chatTextField.getText());
                            reI.receive(emI.getName(),chatTextField.getText()); // self reception
                        }


                    } catch (RemoteException | MalformedURLException | NotBoundException e1) {
                        e1.printStackTrace();
                    }
                    //System.out.println("Envoi du message " + chatTextField.getText());
                    chatTextField.clear();
                    destTextField.clear();
                } else {
                    System.out.println("Null message sent.");
                }

            }
        };

        chatTextField.setOnAction(event);

        // Maj du conteenu dynamique
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), ev -> {
            try {
                listeMessages.clear();
                listeMessages.addAll(reI.getMsg());
                chatBox.setItems(listeMessages);
                bandeauClient.setText("Utilisateurs connectés" + reI.getClients().toString());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }));
        // Play infini
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        // clean du chat
        clearBtn.onMouseClickedProperty().set((MouseEvent me) -> {
            { listeMessages.clear(); }
        });


        //rootPane.add(chatListView, 0, 0);
        rootPane.add(userLabel,0,0);
        rootPane.add(bandeauClient,0,1);
        rootPane.add(chatBox, 0, 2);
        rootPane.add(msgLabel,0,3);
        rootPane.add(destLabel, 1, 3);
        rootPane.add(chatTextField, 0, 4);
        rootPane.add(destTextField, 1, 4);
        rootPane.add(clearBtn,0,5);




        return new Scene(rootPane, 400, 400);

    }

    public static void main(String[] args){
        launch(args);
    }

}