package ma.enset.tp2sma.agents;

import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import ma.enset.tp2sma.Presentation.ClientGUI;
import ma.enset.tp2sma.controller.ClientController;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ClientAgent extends GuiAgent {
    private ClientController clientController;
    private List<DFAgentDescription> agentDescriptions=new ArrayList<>(); //Créer une liste des descriptions des agents qui fournissent leurs servicesDescription.
    @Override
    protected void setup() {
        clientController=(ClientController) getArguments()[0];
        clientController.setAgent(this);
        ParallelBehaviour parallelBehaviour=new ParallelBehaviour();


        parallelBehaviour.addSubBehaviour(new CyclicBehaviour() {
            private List<Float> prices=new ArrayList<>();
            @Override
            public void action() {
                ACLMessage message=receive();
                if(message!=null){
                    ACLMessage responseMSG=message.createReply();
                    //partie de compraison des prix
//                     if(message.getPerformative()==ACLMessage.PROPOSE){
//                         System.out.println("price: "+message.getContent()+" $");
//                         prices.add(Float.parseFloat(message.getContent()));
//                         if (prices.size()==2){
//                             float prixMin=Math.min(prices.get(0),prices.get(1));
//                             ACLMessage message1=message.createReply();
//                             message1.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
//                             message1.setContent("I want this price : "+prixMin);
//                             send(message1);
//                         }}
                     if(message.getPerformative()==ACLMessage.CONFIRM){
                         clientController.notificationWindow(message.getContent());
                     }
                }else {
                    block();
                }
            }
        });
/**                                                                         **/
//        parallelBehaviour.addSubBehaviour(new OneShotBehaviour() {
//            @Override
//            public void action() {
//                DFAgentDescription dfAgentDescription=new DFAgentDescription();
//                ServiceDescription service=new ServiceDescription();
//                service.setType("gaming");
//                dfAgentDescription.addServices(service);
//                try {
//                    agentDescriptions= DFService.search(getAgent(),dfAgentDescription);
//
//                } catch (FIPAException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
        addBehaviour(parallelBehaviour);
    }

    @Override
    protected void takeDown() {

    }

    @Override
    protected void beforeMove() {

    }

    @Override
    protected void afterMove() {

    }

    @Override
    public void onGuiEvent(GuiEvent guiEvent) {
        String agentLocalName = (String) guiEvent.getParameter(0); //structure de nom : [nomAgent]
        agentLocalName = agentLocalName.replace("[", ""); //supprimer le caractère [
        agentLocalName = agentLocalName.replace("]", ""); //supprimer le caractère ]

        DFAgentDescription vendeurAgentDesc = null;
        Iterator<ServiceDescription> serviceDescription = null;
        for (DFAgentDescription dfAgent : agentDescriptions) {
            if (dfAgent.getName().getLocalName().equals(agentLocalName)) {
                vendeurAgentDesc = dfAgent; //charger la desc de vendeur agent pointé
                serviceDescription = vendeurAgentDesc.getAllServices(); //charger toutes les services de vendeur agent
            }
        }

        if (vendeurAgentDesc != null && serviceDescription != null) {
                if (guiEvent.getType() == 1) { //***************partie de Show details of services
                    int index = 1;
                    String msg = "";
                    while (serviceDescription.hasNext()) {
                        ServiceDescription description = serviceDescription.next();
                        String price = clientController.getPrice(description.getName());
                        msg = "* '"+vendeurAgentDesc.getName().getLocalName()+"' Service No." + index + " >\t  [NAME]: " + description.getName() + " \t\t|\t\t [TYPE]: " + description.getType() + " \t\t|\t\t [PRICE]: " +price+"$";
                        index++;
                        clientController.setDetailsArea(msg);

                    }

                }
                if (guiEvent.getType() == 2) { //***********partie de buy service
                    while (serviceDescription.hasNext()) {
                        ServiceDescription description = serviceDescription.next();
                        if(description.getName().equals(guiEvent.getParameter(1))) { //getParameter(1) contient le nom de service
                            //Partie de communication avec l'agent vendeur
                            ACLMessage message = new ACLMessage(ACLMessage.CONFIRM);
                            message.addReceiver(vendeurAgentDesc.getName());
                            message.setContent(description.getName());
                            send(message);
                            System.out.println(">> Client has pressed the button buy to the service : " + message.getContent());
                            break;
                        }
                    }
                }
                if (guiEvent.getType() == 3) {//***********patie de suppression d'un service une fois l'agent vendeur le supprime.
                    String serviceName = (String) guiEvent.getParameter(1);
                    while (serviceDescription.hasNext()) {
                        ServiceDescription description = serviceDescription.next();
                        if (description.getName().equals(serviceName)) {
                            vendeurAgentDesc.removeServices(description);
                            clientController.clearListDetails();
                            break;
                        }
                    }
                }
            }
    }


    public void addAgentDesc(DFAgentDescription description){
        this.agentDescriptions.add(description);
    }
}
