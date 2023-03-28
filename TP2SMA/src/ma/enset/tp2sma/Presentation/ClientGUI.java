package ma.enset.tp2sma.Presentation;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import ma.enset.tp2sma.agents.ClientAgent;
import ma.enset.tp2sma.controller.VendeurController;

public class ClientGUI extends Application {
    private ObservableList<String> data = FXCollections.observableArrayList();
    public static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
//    public void start(Stage primaryStage) throws Exception {
//        BorderPane root=new BorderPane();
//        ListView<String> listView=new ListView<>(data);
//        root.setCenter(listView);
//        Scene scene=new Scene(root,400,300);
//        primaryStage.setScene(scene);
//        primaryStage.show();
//    }
    public void start(Stage stage) throws Exception {


        BorderPane root = FXMLLoader.load(getClass().getResource("ClientGUI.fxml"));
        Scene scene = new Scene(root); //new Scene(root,X,Y)
        //Set styleSheet below :
        //scene.getStylesheets().add("com.OussezMVC/Presentation/Vue/StyleCSS.css");
        stage.setScene(scene);
        stage.show();
        stage.setTitle("                                                                    Client GUI | v1.0");
    }
}
