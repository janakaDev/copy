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

public class RegBranch extends Agent{
   LinkedList<String> linkedlist=new LinkedList<String>();
   LinkedList<String> reqSubject=new LinkedList<String>();
   
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
        System.out.println("RegBranch sent ACK...\n");
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
                    System.out.println("RegBranch Got the details(s)..\n");
                    System.out.println("The sender is "+msg.getSender().getLocalName()+" the receiver is "+getLocalName()+"\nand the msg is "+msg.getContent());
                    String m[]=msg.getContent().split(",");
                    
                    if(Integer.parseInt(m[1])>18)
                    {
                        
                        linkedlist.add(msg.getSender().getLocalName()+","+msg.getContent());
                        sendMsg(msg.getSender().getLocalName(),"Registered",ACLMessage.INFORM);
                       // linkedlist.removeFirst();
                        //System.out.println("aaa"+linkedlist.get(1)+"aaaa");
                    }
                    else
                    {
                        sendMsg(msg.getSender().getLocalName(),"Not Registered",ACLMessage.INFORM);
                        
                    }
                  
                }
                
                
                
                if(msg.getPerformative()==ACLMessage.REQUEST)
                {
                   System.out.println("RegBranch Got the details(D)..\n");
                   System.out.println("The sender is "+msg.getSender().getLocalName()+" the receiver is "+getLocalName()+"\nand the msg is "+msg.getContent());
                   String details=msg.getContent();
                   
			for(int i=0;i<linkedlist.size();i++)
                        {
                            String e=linkedlist.get(i);
                            String m[]=e.split(",");
                            String subject=m[3];
                            //reqSubject.add(m[3]);
                            //System.out.println("grddjytfkuygk"+reqSubject);
                            if(details.equals(subject))
                            {
                                reqSubject.add(m[1]);
                                System.out.println("Yawaanna puluwan");
                            }
                            else
                            {
                                System.out.println("Yawaanna be");
                            }
			}
			sendMsg(msg.getSender().getLocalName(),reqSubject.get(0).toString(),ACLMessage.INFORM);
				}
                
                
                
            }
        }
    }
    
}
