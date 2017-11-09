/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package second;
import jade.core.*;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.*;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.*;
import java.util.*;

public class Department extends Agent{
   
    protected void setup()
    {
        System.out.println("I'm RegBranch and my nick name is "+getLocalName());
        DFAgentDescription dfd=new DFAgentDescription();
        ServiceDescription sd=new ServiceDescription();
        sd.setName(getLocalName());
        sd.setType("First");
        dfd.setName(getAID());
        dfd.addServices(sd);
        
        try
        {
            DFService.register(this, dfd);
            sendMsg("reg","ICT",ACLMessage.REQUEST);            
            addBehaviour(new myBehaviour(this));
        
        }
        catch(FIPAException e)
        {
            System.out.println("Error: "+e);
        }
    }
    
     public void sendMsg(String rec,String msg, int p)
    {
        ACLMessage acl=new ACLMessage();
        AID r=new AID(rec,AID.ISLOCALNAME);
        acl.addReceiver(r);
        acl.setContent(msg);
        acl.setPerformative(p);
        send(acl);
        System.out.println("Dept. sent Request...\n");
    }
    
    class myBehaviour extends CyclicBehaviour
    {

        public myBehaviour(Agent a) {
            super(a);
        }
        
        public void action()
        {
            ACLMessage msg=myAgent.receive();
            if(msg!=null)
            {
                if(msg.getPerformative()==ACLMessage.INFORM)
                {
                    System.out.println("Dept. Got the details..\n");
                    System.out.println("The sender is "+msg.getSender().getLocalName()+" the receiver is "+getLocalName()+"\nand the msg is "+msg.getContent());
                    String stu=msg.getContent();
                    System.out.println(stu);
                    sendMsg(stu, "You are selected to ICT",ACLMessage.REQUEST);
                }   
            }
        }
    }
    
}
