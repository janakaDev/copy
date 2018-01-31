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

public class PCMiddle extends Agent{
    
    protected void setup()
    {
        System.out.println("I'm the Middle PC and my name is "+getLocalName());
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
    
    public void sendMsg(String rec,String msg,int p)
    {
        ACLMessage acl=new ACLMessage();
        AID r=new AID(rec,AID.ISLOCALNAME);
        acl.addReceiver(r);
        acl.setContent(msg);
        acl.setPerformative(p);
        send(acl);
        System.out.println("Key Sent..\n");
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
               if(msg.getPerformative()==ACLMessage.REQUEST)
               {
                   System.out.println("I'm "+getLocalName());
                   System.out.println(msg.getSender().getLocalName()+" is send me the IP: "+msg.getContent());
                   
                   String []pc1=msg.getSender().getLocalName().split("\\.");
                  
                   String []pc2=msg.getContent().split("\\.");
                   
                   int sum=Integer.parseInt(pc1[0])+Integer.parseInt(pc1[1])+Integer.parseInt(pc1[2])+Integer.parseInt(pc1[3])+
                           Integer.parseInt(pc2[0])+Integer.parseInt(pc2[1])+Integer.parseInt(pc2[2])+Integer.parseInt(pc2[3]);                  
                   
                   String sum1=Integer.toString(sum);
                   char key=sum1.charAt(0);
                   sendMsg(msg.getSender().getLocalName(),Character.toString(key),ACLMessage.INFORM);
                   sendMsg(msg.getContent(),Character.toString(key),ACLMessage.INFORM);
               }
           }
       }
   }
    
}
