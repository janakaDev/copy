package ass2.studreg;

import jade.core.*;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.*;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.*;

public class StudentBranchAgent extends Agent
{
	protected void setup()
	{
		System.out.println("I am the Student Branch Agent.My name is "+getLocalName());
		
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
					System.out.println("I m "+getLocalName()+"New Student request to register is : "+msg.getContent()+"Request received from student agent: "+msg.getSender().getLocalName());
					System.out.println("Forwarding student to department.");
					sendMessage("DepartmentAgent",msg.getContent()+"&"+msg.getSender().getLocalName(),ACLMessage.REQUEST);
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
		
		System.out.println("Message Sent : "+msg+".\n\n");
	}
	
}