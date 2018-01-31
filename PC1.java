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

public class PC1 extends Agent{
    
    protected void setup()
    {
        System.out.println("I'm PC1 and my IP is "+getLocalName());
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
            sendMsg("PCMiddle",args[0].toString(),ACLMessage.REQUEST);   
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
        System.out.println("Msg sent\n");
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
                    
                    int key=Integer.parseInt(msg.getContent());
                    
                    int encrypted=mod((5+key),26);
                    String enMsg=Integer.toString(encrypted);
                    
                    sendMsg("192.168.4.6",enMsg,ACLMessage.REQUEST);
                }
            }
        }
    }
    
}
