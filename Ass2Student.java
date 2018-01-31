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

public class Ass2Student extends Agent{
    
    protected void setup()
    {
        System.out.println("I'm a Student and my name is "+getLocalName());
        DFAgentDescription dfd=new DFAgentDescription();
        ServiceDescription sd=new ServiceDescription();
        sd.setName(getLocalName());
        sd.setType("First");
        dfd.setName(getAID());
        dfd.addServices(sd);
        
        try
        {
            DFService.register(this, dfd);
            Object[] args=getArguments();
            sendMsg("SBranch",args[0].toString()+","+args[1].toString()+","+args[2].toString()+","+args[3].toString(),ACLMessage.INFORM);
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
        System.out.println("Application is sent..");
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
                    System.out.println("I'm "+getLocalName());
                    System.out.println("I got ACK from "+msg.getSender().getLocalName()+" and ACK is "+msg.getContent()+"\n");
                }
                if(msg.getPerformative()==ACLMessage.CONFIRM)
                {
                    
                    System.out.println("I'm "+getLocalName());
                    System.out.println("I got Information from "+msg.getSender().getLocalName()+" and Message is "+msg.getContent()+"\n"); 
                }
            }
        }
    }
    
}
