/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bookBuy;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

/**
 *
 * @author lahir
 */
public class Seller extends Agent{
    
    
    String bookName = "abc";
    int bookPrice;
    
    protected void setup()
	{
		System.out.println("I am a seller.My name is "+getLocalName());
		
		DFAgentDescription dfd = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		
		sd.setName(getLocalName());
		sd.setType("First");
		dfd.setName(getAID());
		dfd.addServices(sd);
		
		try
		{
			DFService.register(this,dfd);
                        
                        Object[] args = getArguments();
                        if(args!=null && args.length>0)
                        {
                            bookPrice = Integer.parseInt((String)args[0]);
                        }
                        
			addBehaviour(new MyBehaviour(this));
		}
		catch(FIPAException e)
		{
			System.out.println(e);
		}
	}
	
	class MyBehaviour extends CyclicBehaviour
	{
		MyBehaviour(Agent a)
		{
			super(a);
		}
		
		public void action()
		{
			ACLMessage msg = myAgent.receive();
			if(msg!=null)
			{
				if(msg.getPerformative()==ACLMessage.REQUEST)
				{
					//System.out.println("Message received.");
					System.out.println("I m "+getLocalName()+"Book price request received for  : "+msg.getContent()+"Message received from : "+msg.getSender().getLocalName());
				
					sendMessage(msg.getSender().getLocalName(),bookName+"&"+bookPrice,ACLMessage.INFORM);
				}
			}
		}
	}
	//to send ack
	protected void sendMessage(String receiver, String msg, int p)
	{
		ACLMessage acl = new ACLMessage();
		AID r = new AID(receiver,AID.ISLOCALNAME);
		
		acl.addReceiver(r);
		acl.setContent(msg);
		acl.setPerformative(p);
		
		send(acl);
		
		System.out.println("Seller Message Sent : "+msg+".\n\n");
	}
    
}
