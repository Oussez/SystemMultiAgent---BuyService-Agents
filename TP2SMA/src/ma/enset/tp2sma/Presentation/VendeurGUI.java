package ma.enset.tp2sma.Presentation;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class VendeurGUI extends Application {
    public SingletonControllers Singleton = SingletonControllers.getInstance();

    private ObservableList<String> data= FXCollections.observableArrayList();
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
    public void start(Stage stage) throws Exception{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("VendeurGUI.fxml"));
        BorderPane root = loader.load();

        Scene scene = new Scene(root); //new Scene(root,X,Y)
        //Set styleSheet below :
        //scene.getStylesheets().add("com.OussezMVC/Presentation/Vue/StyleCSS.css");
        stage.setScene(scene);
        stage.show();
        stage.setTitle("                                                                    Vendeur GUI | v1.0");
    }





}
