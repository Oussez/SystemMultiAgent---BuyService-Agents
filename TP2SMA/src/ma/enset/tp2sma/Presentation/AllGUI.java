package ma.enset.tp2sma.Presentation;

import javafx.application.Application;
import javafx.stage.Stage;
import ma.enset.tp2sma.containers.MainContainer;

public class AllGUI extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        //TODO First: start the class from package 'containers'--> MainContainer
        // Lancer la fenêtre de la classe VendeurGUI sur un autre objet Stage distinct
        Stage vendeurStage = new Stage();
        VendeurGUI vendeurGUI = new VendeurGUI();
        vendeurGUI.start(vendeurStage);
        // Lancer la fenêtre de la classe ClientGUI sur un objet Stage distinct
        Stage clientStage = new Stage();
        ClientGUI clientGUI = new ClientGUI();
        clientGUI.start(clientStage);

    }

}
