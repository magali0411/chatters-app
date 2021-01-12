package client;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
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
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import serveur.Receiver;
import serveur.ReceiverImpl;
import util.Message;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ClientApp extends Application {

    //constantes
    static final String DEFAULT_HOST = "localhost";
    static final String DEFAULT_PORT = "1099";

    //Font
    static final Font FONTBOLD = Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 12);
    static final Font FONTITALIC = Font.font("verdana", FontWeight.EXTRA_LIGHT, FontPosture.ITALIC, 9);

    //Logger
    static final Logger logger = Logger.getLogger("Client-app");


    // Inititalisation des éléments RMI
    Emitter emI;
    Receiver reI;
    Connection connection;


    // Liste dynamique des messages et des clients du chat
    private final ObservableList<String> listeMessages = FXCollections.observableArrayList();
    private final ObservableList<String> listeClients = FXCollections.observableArrayList();


    @Override
    public void start(Stage primaryStage) throws Exception {

        Alert popupClose = Message.showPopupAlert("Vous quittez l'application de chat", "");

        Group group = new Group();
        Scene scene = new Scene(group, 600, 400);
        scene.setFill(Color.WHITE);
        primaryStage.setTitle("Chat-app");
        primaryStage.setScene(chatScene(primaryStage));
        primaryStage.show();

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                logger.log(Level.INFO, "Stage is closing");
                popupClose.show();
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
        TextField hostNameField = new TextField(DEFAULT_HOST);
        TextField portNumberField = new TextField(DEFAULT_PORT);

        Label pseudoLabel = new Label("Pseudo ");
        Label hostNameLabel = new Label("Host Name");
        Label portNumberLabel = new Label("Port Number");
        Label errorLabel = new Label();


        Button submitBtn = new Button("Connection");
        submitBtn.onMouseClickedProperty().set((MouseEvent t) -> {
            {
                if (!pseudoField.getText().isEmpty() && !portNumberField.getText().isEmpty() && !hostNameField.getText().isEmpty()) {

                    int port = Integer.parseInt(portNumberField.getText());
                    String name = pseudoField.getText();
                    logger.log(Level.INFO, "name : {0}", name);


                    try {

                        // Instanciation de la connection au serveur
                        connection = (Connection) Naming.lookup("rmi://localhost:" + port + "/connection");

                        // instanciation du receiver
                        this.reI = new ReceiverImpl();

                        //Instanciation de l'emitteur
                        this.emI = connection.connect(name, reI);


                        // Toast
                        Alert popupConnect = Message.showPopupInfo("Connection réussie", "Vous êtes connecté(e) au serveur de chat en tant que " + name);
                        popupConnect.show();

                    } catch (MalformedURLException | NotBoundException | RemoteException e) {
                        Alert popupPort = Message.showPopupAlert("Connection fail", "Le PORT " + port + " du serveur " + hostNameField.getText() + " n'est pas accessible ");
                        popupPort.show();
                    }


                    if (emI != null && reI != null && connection != null) {
                        try {
                            primaryStage.setScene(clientScene(emI, reI, connection));
                        } catch (RemoteException e) {
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


        TextField chatTextField = new TextField();
        TextField destTextField = new TextField();
        Label destLabel = new Label("Destinaire");
        Label userLabel = new Label("Bonjour " + emI.getName());
        Label msgLabel = new Label("Messages");
        ListView chatBox = new ListView(listeMessages);
        Label bandeauClient = new Label("");
        Label instructionLabel = new Label("Appuyer sur entrer pour envoyer un message");

        chatBox.setPrefHeight(250);
        chatBox.setDisable(true);
        chatBox.setMouseTransparent(true);

        userLabel.setFont(FONTBOLD);
        instructionLabel.setFont(FONTITALIC);

        Button clearBtn = new Button("Supprimer les messages");
        Button disconnectBtn = new Button("Deconnection");


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
            public void handle(ActionEvent e) {

                if (!chatTextField.getText().isEmpty() && !destTextField.getText().isEmpty()) {
                    try {
                        listeMessages.addAll(reI.getMsg());
                        Receiver reDest = co.getReceiver(destTextField.getText());
                        if (reDest == null) {
                            Alert popupAlert = Message.showPopupAlert("Impossible d'envoyer le message", "L'utilisateur " + destTextField.getText() + " n'est pas connecté.");
                            popupAlert.show();
                        } else {
                            emI.sendMessages(reDest, chatTextField.getText());
                            reI.receive(emI.getName(), chatTextField.getText()); // self reception
                        }

                    } catch (RemoteException | MalformedURLException | NotBoundException e1) {
                        e1.printStackTrace();
                    }

                    chatTextField.clear();
                    destTextField.clear();

                } else {
                    Alert alertNoMsg = Message.showPopupAlert("Impossible d'envoyer le message", "Veuillez saisir un message et un destinaire");
                    alertNoMsg.show();
                    logger.warning("Null message sent");
                }

            }
        };

        chatTextField.setOnAction(event);
        destTextField.setOnAction(event);

        // Maj du conteenu dynamique


        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), ev -> {
            try {
                listeMessages.clear();
                listeMessages.addAll(reI.getMsg());
                chatBox.setItems(listeMessages);


                co.synchronise();
                listeClients.clear();
                listeClients.addAll(reI.getClients());
                bandeauClient.setText("Clients connectés : " + listeClients);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }));
        // Play infini
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        // clean du chat
        clearBtn.onMouseClickedProperty().set((MouseEvent me) -> {
            {
                try {
                    reI.clearMsg();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                listeMessages.clear();
                chatBox.setItems(listeMessages);
            }
        });

        disconnectBtn.onMouseClickedProperty().set((MouseEvent mee) -> {
            {
                try {
                    co.disconnect(emI.getName());
                    logger.log(Level.INFO, "Client deconnecte {0}", emI.getName());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                Platform.exit();
            }
        });


        rootPane.add(userLabel, 0, 0);
        rootPane.add(bandeauClient, 0, 1);
        rootPane.add(chatBox, 0, 2);
        rootPane.add(msgLabel, 0, 3);
        rootPane.add(destLabel, 1, 3);
        rootPane.add(chatTextField, 0, 4);
        rootPane.add(destTextField, 1, 4);
        rootPane.add(clearBtn, 0, 5);
        rootPane.add(disconnectBtn, 1, 5);
        rootPane.add(instructionLabel, 0, 6);


        return new Scene(rootPane, 400, 400);

    }

    public static void main(String[] args) {
        launch(args);
    }

}

