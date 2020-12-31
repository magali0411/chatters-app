package main.java.util;

import javafx.scene.control.Alert;

public class Message {

    private Message() { throw new IllegalStateException("Utility class");}

    public static Alert showPopupAlert(String header, String texte) {
        Alert popup = new Alert(Alert.AlertType.ERROR);
        popup.setContentText(texte);
        popup.setHeaderText(header);
        return popup;
    }

    public static Alert showPopupInfo(String header, String texte) {
        Alert popup = new Alert(Alert.AlertType.INFORMATION);
        popup.setContentText(texte);
        popup.setHeaderText(header);
        return popup;
    }


}
