/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exam;
import jade.core.*;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.*;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.*;

public class SenderWithAck extends Agent{
    
    protected void setup()
    {
        System.out.println("I'm the Sender and my nick name is "+getLocalName());
        DFAgentDescription dfd=new DFAgentDescription();
        ServiceDescription sd=new ServiceDescription();
        sd.setName(getLocalName());
        sd.setType("First");
        dfd.setName(getAID());
        dfd.addServices(sd);
        
        try
        {
            DFService.register(this, dfd);
            sendMsg("rec","Good Morning",ACLMessage.REQUEST);
            addBehaviour(new myBehaviour(this));
        }
        catch(FIPAException e)
        {
            System.out.println("Error: "+e);
        }
    }
    
    public void sendMsg(String rec,String msg,int p)
    {
        ACLMessage acl=new ACLMessage();
        AID r=new AID(rec,AID.ISLOCALNAME);
        acl.addReceiver(r);
        acl.setContent(msg);
        acl.setPerformative(p);
        send(acl);
        System.out.println("Message is sent..\n");
    }
    
    class myBehaviour extends CyclicBehaviour
    {
        myBehaviour(Agent a)
        {
            super(a);
        }
        
        public void action()
        {
            ACLMessage msg=myAgent.receive();
            if(msg!=null)
            {
                if(msg.getPerformative()==ACLMessage.INFORM)
                {
                    System.out.println("Got the ack..\n");
                    System.out.println("The Receiver is "+msg.getSender().getLocalName()+" and ACK is "+msg.getContent());
                }
            }
        }
    }
    
}
