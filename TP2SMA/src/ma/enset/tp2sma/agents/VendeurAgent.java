package ma.enset.tp2sma.agents;

import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import ma.enset.tp2sma.controller.VendeurController;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class VendeurAgent extends GuiAgent {
    private DFAgentDescription dfAgentDescription;
    private VendeurController serverGUI;
    private List<String>listClientAgent = new ArrayList<>();//une fois un client agent achete un service, il sera enregistré sur cette liste
    private List<String> listPrices = new ArrayList<>();

    @Override
    protected void setup() {
        serverGUI=(VendeurController)getArguments()[0];
        serverGUI.setAgent(this);

        dfAgentDescription=new DFAgentDescription();//Créer une description d'agent qui fournira son service.
        try {
            DFService.register(this,dfAgentDescription); //Enregistrer la description de nouveau service d'agent sur les services d'agent DF
        } catch (FIPAException e) {
            throw new RuntimeException(e);
        }
        ParallelBehaviour parallelBehaviour=new ParallelBehaviour();

//        parallelBehaviour.addSubBehaviour(new OneShotBehaviour() {
//            @Override
//            public void action() {
//
//                ServiceDescription service1=new ServiceDescription();  //Créer un service
//                service1.setType("gaming");
//                service1.setName("mac1");
//                ServiceDescription service2=new ServiceDescription(); //Créer un service
//                service2.setType("gaming");
//                service2.setName("mac2");
//
//                dfAgentDescription.addServices(service1); //Ajouter les services à la description d'agent
//                dfAgentDescription.addServices(service2);
//
//                try {
//                    DFService.register(getAgent(),dfAgentDescription); //Enregistrer la description de service d'agent sur les services d'agent DF
//                } catch (FIPAException e) {
//                    e.printStackTrace();
//                }
//            }
//        });

        parallelBehaviour.addSubBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage receivedMSG=receive();
                if(receivedMSG!=null){
                    ACLMessage responseMSG=receivedMSG.createReply();

                    if (receivedMSG.getPerformative()==ACLMessage.CFP){ //CFP : Call For Propose
                          responseMSG.setPerformative(ACLMessage.PROPOSE);
//                          System.out.println(">> receivedMSG: "+receivedMSG);
//                          System.out.println(">> responseMSG: "+responseMSG);
                        for(String serviceInfo : listPrices){
                            if(serviceInfo.contains(receivedMSG.getContent())){
                                responseMSG.setContent(serviceInfo.split(" <-> ")[1]);
                            }
                        }
                        send(responseMSG);
                    }
                   if(receivedMSG.getPerformative()==ACLMessage.ACCEPT_PROPOSAL){
                      responseMSG.setPerformative(ACLMessage.AGREE);
                      responseMSG.setContent(">>Your product desired is ready to be delivered");
                      send(responseMSG);
                  }
                   if(receivedMSG.getPerformative()==ACLMessage.CONFIRM){
                       listClientAgent.add(receivedMSG.getSender().getLocalName()+" --> "+receivedMSG.getContent());
                       System.out.println("*** NOTIFICATION-CASH : Client '"+receivedMSG.getSender().getLocalName()+"' has just bought service : '"+receivedMSG.getContent()+"'");
                       responseMSG.setContent("You have successfully bought the service ["+receivedMSG.getContent()+"]. Our support is always available for you.");
                       responseMSG.setPerformative(ACLMessage.CONFIRM);
                       send(responseMSG);
                   }

                    }else {
                    block();
                }
            }
        });
        addBehaviour(parallelBehaviour);
    }

    @Override
    protected void takeDown() {
        try {
            DFService.deregister(this);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void beforeMove() {

    }

    @Override
    protected void afterMove() {

    }

    @Override
    public void onGuiEvent(GuiEvent guiEvent) {
        Iterator<String> services=guiEvent.getAllParameter();
        while(services.hasNext()){
            ServiceDescription serviceDescription = new ServiceDescription();
            String serviceName = services.next();
            String serviceType = services.next();
            String servicePrice = services.next();
            serviceDescription.setName(serviceName);
            serviceDescription.setType(serviceType);
            listPrices.add(serviceName+" <-> "+servicePrice);

            this.dfAgentDescription.addServices(serviceDescription);

            this.serverGUI.showMessage(serviceName+"    <<|>>    "+serviceType+"    <<|>>    "+servicePrice, dfAgentDescription);
            System.out.println("*** [NEW SERVICE] has been added with following infos >>  Name : "+serviceName+" | Type : "+serviceType+" | Price : "+servicePrice+"$");

        }

    }

    public String getPriceService(String serviceName){
        Iterator<String > Prices = this.listPrices.iterator();
        while(Prices.hasNext()){
            String [] serviceInfo = Prices.next().split(" <-> ");
            if(serviceName.equals(serviceInfo[0]))
                return serviceInfo[1];

        }
        return "0$";
    }
}
