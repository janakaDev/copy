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
import java.util.Date;

public class Ass2Department extends Agent{
    
    protected void setup()
    {
        System.out.println("I'm a Department and my name is "+getLocalName());
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
                    System.out.println("I got reply from "+msg.getSender().getLocalName()+" and MSG is "+msg.getContent()+"\n");
                
                    String []m=msg.getContent().split(",");                  
                    
                    Date d=new Date();
                    d.getYear();
                    String []dt=d.toString().split(" ");
                    String year=dt[5];
                    
                    String RegNo=year+"/"+getLocalName()+"/"+m[0];         
                
                    sendMsg(m[1],"Congratulations "+m[1]+". You are registered for the Course "+getLocalName()+" with the "
                            + "registration number "+RegNo,ACLMessage.CONFIRM);
                
                
                }
            }
        }
    }
 }
    
