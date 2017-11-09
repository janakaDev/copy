package ass2.studreg;

import jade.core.*;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.*;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.*;

public class StudentAgent extends Agent
{
	protected void setup()
	{
		System.out.println("I am a Student Agent.My Name is "+getLocalName());
		
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
			String msg="";
			if(args!=null && args.length>0)
			{
				msg = (String)args[0];
			}
						
			sendMessage("StudentBranchAgent",msg,ACLMessage.REQUEST);
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
				if(msg.getPerformative()==ACLMessage.REQUEST)
				{
					System.out.println("I m "+getLocalName()+". Got message is : "+msg.getContent()+". Message received from : "+msg.getSender().getLocalName());
				}
			}
		}
	}
}