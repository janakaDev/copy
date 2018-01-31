/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bookBuy;

import jade.core.AID;
import jade.core.Agent;
import static jade.core.Agent.D_MIN;
import static jade.core.behaviours.Behaviour.STATE_READY;
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
public class Broker extends Agent{
    
    int sellerCount = 3;
    String currentBook;
    String currentBuyer;
    
//    int[] curPriceTable = new int[sellerCount];
    
    String cheapestSellerName;
    int cheapestSellerPrice;
    
    int receivedSellerPricesCount =0;
    
    protected void setup()
	{
		System.out.println("Broker Agent '"+getLocalName()+"' created.");
		
		DFAgentDescription dfd = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		
		sd.setName(getLocalName());
		sd.setType("First");
		dfd.setName(getAID());
		dfd.addServices(sd);
		
		try
		{
			DFService.register(this,dfd);
			
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
                            if(msg.getPerformative()==ACLMessage.REQUEST)//buyer sending broker
                            {                                        
                                System.out.println("I m "+getLocalName()+"Message is : "+msg.getContent()+"Message received from : "+msg.getSender().getLocalName());
                                currentBook = msg.getContent();
                                currentBuyer = msg.getSender().getLocalName();

                                for (int i = 0; i < sellerCount; i++) {

                                    String curseller = "seller"+(i+1);
                                    sendMessage(curseller, currentBook, ACLMessage.REQUEST);
                                }

                            }

                            else if(msg.getPerformative()==ACLMessage.INFORM)// seller sending price
                            {
                                receivedSellerPricesCount++;
                                
                                String[] priceDetails = msg.getContent().split("&");
                                
                                int sellerPrice = Integer.parseInt(priceDetails[1]);
                                System.out.println("at broker seller price = "+sellerPrice);
                                
                                if(sellerPrice<cheapestSellerPrice || cheapestSellerPrice==0)
                                {
                                    cheapestSellerPrice = sellerPrice;
                                    cheapestSellerName = msg.getSender().getLocalName();
                                }
                                
                                
                                if(receivedSellerPricesCount==sellerCount)
                                {
                                    sendMessage(currentBuyer, "Cheapest price is Rs."+cheapestSellerPrice+" from seller '"+cheapestSellerName+"'.", ACLMessage.INFORM);
                                }
//                                if(msg.getSender().getLocalName()=="seller1")
//                                {
//                                    curPriceTable[0] = Integer.parseInt(msg.getContent());
//                                }
                                
//                                else if(msg.getSender().getLocalName()=="seller2")
//                                {
//                                    curPriceTable[1] = Integer.parseInt(msg.getContent());
//                                }
//                                else if(msg.getSender().getLocalName()=="seller3")
//                                {
//                                    curPriceTable[3] = Integer.parseInt(msg.getContent());
//                                }
//                                                         
//                                if()
                            }
			}
		}
	}
    
}
