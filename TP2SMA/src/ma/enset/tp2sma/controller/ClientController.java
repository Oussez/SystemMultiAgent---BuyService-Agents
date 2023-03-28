package ma.enset.tp2sma.controller;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
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
import ma.enset.tp2sma.agents.ClientAgent;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ClientController implements Initializable {

    @FXML
    private Button buyBtn;
    @FXML
    private  Button detailsBtn;
    @FXML
    private TextField nameService;

    @FXML
    private ObservableList<String> observableList= FXCollections.observableArrayList();
    @FXML
    private ListView<String> listServices = new ListView<>(observableList);
    @FXML
    private ObservableList<String> observableDetails= FXCollections.observableArrayList();
    @FXML
    private ListView<String> listDetails = new ListView<>(observableDetails);

    private ClientAgent clientAgent;
    private SingletonControllers singleton = SingletonControllers.getInstance();
    protected VendeurController vendeurController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        singleton.setClientCtr(this);  //initialisation de singelton.clientCtr par this
        vendeurController = singleton.getVendeurCtr(); //partie initialisation de vendeurController via la classe SIngeleton
        vendeurController.clientController = this; //initalisation de clientController pour l'objet VendeurController
        /////////////////////////////
        try {
            startContainer();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        listServices.setItems(observableList);
        listDetails.setItems(observableDetails);


    }

    private void startContainer() throws Exception {
        Runtime runtime=Runtime.instance();
        ProfileImpl profile=new ProfileImpl();
        profile.setParameter(ProfileImpl.MAIN_HOST,"localhost");
        AgentContainer container=runtime.createAgentContainer(profile);
        AgentController agentClient1=container.createNewAgent("client","ma.enset.tp2sma.agents.ClientAgent",new Object[]{this});
        agentClient1.start();
    }

    public void showMessage(DFAgentDescription description) {
        Platform.runLater(() -> {
            if(!observableList.contains(description.getName().getLocalName())) { //pour ne pas redonder le nom des agent qui proposent leurs services
                observableList.add(description.getName().getLocalName());
                clientAgent.addAgentDesc(description);
            }
        });
    }

    public void showDetails(){
       String AgentLocalName = listServices.getSelectionModel().getSelectedItems().toString();
        if (AgentLocalName != null){
            observableDetails.clear();
            GuiEvent guiEvent=new GuiEvent(this,1);
            guiEvent.addParameter(AgentLocalName);
            clientAgent.onGuiEvent(guiEvent);
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);

            alert.setTitle("ERROR !!");

            alert.setHeaderText("ERROR : there is no selected service from the list below.");

            alert.setContentText("Solution: Please try to select a service and then press the button delete ");

            alert.showAndWait(); // result of pressed button = OK / CANCEL / CONFIRM ...etc

        }


    }
    public void setAgent(ClientAgent agent){
        this.clientAgent = agent;
    }

    public void deleteService(String LocalNameAgent, String serviceName){
        GuiEvent guiEvent = new GuiEvent(this,3);
        guiEvent.addParameter(LocalNameAgent);
        guiEvent.addParameter(serviceName);
        clientAgent.onGuiEvent(guiEvent);

    }

    public void setDetailsArea(String serviceDetails){
        observableDetails.add(serviceDetails);
    }

    public void buyService(){
       String selectedService = listDetails.getSelectionModel().getSelectedItems().toString();
       if(!selectedService.equals("[]") ){
            String service;
            int start = selectedService.indexOf("[NAME]: ") + "[NAME]: ".length();
            int end = selectedService.indexOf("\t\t|", start);
            service = selectedService.substring(start, end).trim();

            start = selectedService.indexOf("'") +1;
            end = selectedService.indexOf("'", start);
            String localNameAgent = selectedService.substring(start, end).trim();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("CONFIRMATION OF PURCHASE");
            alert.setHeaderText("CONFIRMATION : Please confirm your purchase of service : "+service);

            Optional<ButtonType> result = alert.showAndWait();

            if(result.get()==ButtonType.OK) {
                GuiEvent guiEvent = new GuiEvent(this, 2);
                guiEvent.addParameter(localNameAgent);
                guiEvent.addParameter(service);
                clientAgent.onGuiEvent(guiEvent);

            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);

            alert.setTitle("ERROR !!");

            alert.setHeaderText("ERROR : there is no selected service from the list below.");

            alert.setContentText("Solution: Please try to select an agent s then press Detail button.Now choose your desired service");

            alert.showAndWait(); // result of pressed button = OK / CANCEL / CONFIRM ...etc

        }


    }

    public void clearListDetails(){
        observableDetails.clear();
    }

    public String getPrice(String serviceName){
        return vendeurController.vendeurAgent.getPriceService(serviceName);
    }

    public void notificationWindow(String message) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("NOTIFICATION");
                alert.setHeaderText(message);
                alert.showAndWait();
            
            }
        });
    }

}
