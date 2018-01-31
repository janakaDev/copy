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
import java.util.*;


        
public class Ass2SBranch extends Agent{
    
    LinkedList<String> StuListICT=new LinkedList<String>();
    LinkedList<String> StuListBIO=new LinkedList<String>();
    LinkedList<String> StuListPHY=new LinkedList<String>();
    
    int ICTcount=0;
    int BIOcount=0;
    int PHYcount=0;
    
    
    protected void setup()
    {
        System.out.println("I'm the Student Reg. Branch and my name is "+getLocalName());
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
        acl.addReceiver(r);;
        acl.setContent(msg);
        acl.setPerformative(p);
        send(acl);
        System.out.println("ACK Sent..\n");
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
                    System.out.println("\nI'm "+getLocalName());
                    System.out.println("I got your Details");
                    String []m=msg.getContent().split(",");
                    
                    Date d=new Date();
                    d.getYear();
                    String []dt=d.toString().split(" ");
                    
                    int age=Integer.parseInt(m[1]);
                    
                    
                    
                    if(m[3].equals(dt[5]) && age>18)
                    {  
                        sendMsg(msg.getSender().getLocalName(),"You are Registerd",ACLMessage.INFORM);            
                    
                        if(m[2].equals("ICT"))
                        {
                            StuListICT.add(msg.getContent());
                            ICTcount+=1;
                            sendMsg("ICT",ICTcount+","+m[0],ACLMessage.INFORM);            
                        }
                        
                        if(m[2].equals("BIO"))
                        {
                            StuListBIO.add(msg.getContent());
                            BIOcount+=1;
                            sendMsg("BIO",BIOcount+","+m[0],ACLMessage.INFORM);            
                        }
                        
                        if(m[2].equals("PHY"))
                        {
                            StuListPHY.add(msg.getContent());
                            PHYcount+=1;
                            sendMsg("PHY",PHYcount+","+m[0],ACLMessage.INFORM);            
                        }  
                    }
                    else
                    {
                      sendMsg(msg.getSender().getLocalName(),"You are Not Registerd",ACLMessage.INFORM);
                    }
                }
            }
        }
        
    }
    
}
