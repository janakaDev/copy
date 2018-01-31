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

public class SendToAll extends Agent{
    
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
            sendToAll("Good Morning",ACLMessage.INFORM);
        }
        catch(FIPAException e)
        {
            System.out.println("Error: "+e);
        }
    }
    
   public void sendToAll(String msg,int p)
   {
       DFAgentDescription temp=new DFAgentDescription();
       
       try
       {
           DFAgentDescription agentList[]=DFService.search(this, temp);
           ACLMessage acl=new ACLMessage();
           
           for(int i=0;i<agentList.length;i++)
           {
             acl.addReceiver(agentList[i].getName());
           }
           acl.setContent(msg);
           acl.setPerformative(p);
           send(acl);
       }
       
       catch(FIPAException e)
       {
           System.out.println("Error: "+e);
       }
   }
    
}
