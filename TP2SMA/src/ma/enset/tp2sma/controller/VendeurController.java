package ma.enset.tp2sma.controller;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.gui.GuiEvent;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import ma.enset.tp2sma.Presentation.SingletonControllers;
import ma.enset.tp2sma.agents.VendeurAgent;

import java.net.URL;
import java.util.ResourceBundle;

public class VendeurController implements Initializable {

    @FXML
    private Button addBtn;
    @FXML
    private  Button deleteBtn;
    @FXML
    private TextField typeServices;
    @FXML
    private TextField nameService;

    @FXML
    private ObservableList<String> observableList= FXCollections.observableArrayList();
    @FXML
    private ListView<String> listServices = new ListView<>(observableList);
    @FXML
    private TextField priceService;

    protected VendeurAgent vendeurAgent;
    private SingletonControllers singleton = SingletonControllers.getInstance();
    protected ClientController clientController;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        singleton.setVendeurCtr(this); //initiation de singelton.vendeurCtr par this

        try {
            startContainer();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        listServices.setItems(observableList);

    }

    private void startContainer() throws Exception {
        Runtime runtime=Runtime.instance();
        ProfileImpl profile=new ProfileImpl();
        profile.setParameter(ProfileImpl.MAIN_HOST,"localhost");
        AgentContainer container=runtime.createAgentContainer(profile);
        AgentController agent=container.createNewAgent("server","ma.enset.tp2sma.agents.VendeurAgent",new Object[]{this});
        agent.start();


    }

    public void showMessage(String message, DFAgentDescription dfAgentDescription) {
        Platform.runLater(() -> {
            observableList.add(message);
            clientController.showMessage(dfAgentDescription);
        });
    }

    public void addService(){
        String serviceName = nameService.getText();
        String serviceType = typeServices.getText();
        String servicePrice = priceService.getText();
        if(!serviceName.isEmpty() && !serviceType.isEmpty()){
            GuiEvent guiEvent=new GuiEvent(this,1);
            guiEvent.addParameter(serviceName);
            guiEvent.addParameter(serviceType);
            guiEvent.addParameter(servicePrice);
            vendeurAgent.onGuiEvent(guiEvent);

            nameService.clear();
            typeServices.clear();
            priceService.clear();
        }

    }

    public void setAgent(VendeurAgent agent){
        this.vendeurAgent = agent;
    }

    public ObservableList<String> getAvailableServices(){
        ObservableList<String> availableServices = listServices.getItems();
        System.out.println(">> Available services: " + availableServices);
        return availableServices;
    }


    public void deleteService() {
        String selectedItem = listServices.getSelectionModel().getSelectedItem();
        String[] sentences = selectedItem.split("<<\\|>>");
        String NameService = sentences[0].trim();

        if (selectedItem != null){
            clientController.deleteService(this.vendeurAgent.getLocalName(),NameService);
            observableList.remove(selectedItem);
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);

            alert.setTitle("ERROR !!");

            alert.setHeaderText("ERROR : there is no selected service from the list below.");

            alert.setContentText("Solution: Please try to select a service and then press the button delete ");

            alert.showAndWait(); // result of pressed button = OK / CANCEL / CONFIRM ...etc

        }
    }

}
