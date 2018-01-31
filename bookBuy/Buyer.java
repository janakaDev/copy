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
public class Buyer extends Agent{
    
    protected void setup()
	{
		System.out.println("Buyer Agent '"+getLocalName()+"' created.");
		
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
			String bookName="";
                        
			if(args!=null && args.length>0)
			{
				bookName = (String)args[0];
				
			}
			
			sendMessage("BookBroker",bookName,ACLMessage.REQUEST);
			addBehaviour(new MyBehaviour(this));
		}
		catch(FIPAException e)
		{
			System.out.println(e);
		}
	}
	
	protected void sendMessage(String receiver, String msg, int p)
	{
		ACLMessage acl = new ACLMessage();
		AID r = new AID(receiver,AID.ISLOCALNAME);
		
		acl.addReceiver(r);
		acl.setContent(msg);
		acl.setPerformative(p);
		
		send(acl);
		
		System.out.println("Message Sent : "+msg+".\n\n");
	}
	//to receive the ack
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
				if(msg.getPerformative()==ACLMessage.INFORM)
				{
					//System.out.println("Message received.");
					System.out.println("I m "+getLocalName()+"Message is : "+msg.getContent()+"Message received from : "+msg.getSender().getLocalName());
				}
			}
		}
	}
    
}
