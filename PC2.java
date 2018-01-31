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
import static sun.util.calendar.CalendarUtils.mod;


public class PC2 extends Agent{
    
    int key=0;
    protected void setup()
    {
        System.out.println("I'm PC2 and my IP is "+getLocalName());
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
                    System.out.println("I'm "+getLocalName());
                    System.out.println("The key is \n"+msg.getContent());
                    
                    key=Integer.parseInt(msg.getContent());
                }
                
                if(msg.getPerformative()==ACLMessage.REQUEST)
                {
                    System.out.println("I'm "+getLocalName());
                    System.out.println("The Encrypted Msg is "+msg.getContent()+"\n");
                    
                    int got=Integer.parseInt(msg.getContent());
                    
                    int decrypted=mod((got-key),26);
                    System.out.println("The Msg is "+decrypted+"\n");
                }
            }
        }
    }
    
}
