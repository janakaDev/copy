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

public class ReceiveFromOne extends Agent {
    
    protected void setup()
    {
        System.out.println("I'm a receiver and my nick name is "+getLocalName());
        DFAgentDescription dfd=new DFAgentDescription();
        ServiceDescription sd=new ServiceDescription();
        sd.setName(getLocalName());
        sd.setType("First");
        dfd.setName(getAID());
        dfd.addServices(sd);
        
        try
        {
            DFService.register(this, dfd);
            addBehaviour(new myBehaviour(this));
        }
        catch(FIPAException e)
        {
            System.out.println("Error: "+e);
        }       
        
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
                   System.out.println("Got the message..\n");
                   System.out.println("The sender is "+msg.getSender().getLocalName()+" and msg is "+msg.getContent());
               }
           }
        }
    }
    

}
    

