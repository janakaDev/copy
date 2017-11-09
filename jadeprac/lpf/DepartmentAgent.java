package ass2.studreg;

import jade.core.*;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.*;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.*;

public class DepartmentAgent extends Agent
{
	protected void setup()
	{
		System.out.println("I am the Department Agent.My name is "+getLocalName());
		
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
					System.out.println("I m "+getLocalName()+"New Student was forwarded is : "+msg.getContent()+"Message received from : "+msg.getSender().getLocalName());
												
					String newStudDetails = msg.getContent();
					String[] personalDetails = newStudDetails.split("&");
								
					String name = personalDetails[0];
					String gender=personalDetails[1];
					String course = personalDetails[2];
					
					String studAgent = personalDetails[3];
					
					System.out.println("Student Name :"+name);
					System.out.println("Student gender :"+gender);
					System.out.println("Student course :"+course);
					
					System.out.println("New Student Registered Successfully.Informing the student now");					
					
					sendMessage(studAgent,"Hello !"+name+".You are a student of our department now. Welcome!!!",ACLMessage.REQUEST);
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