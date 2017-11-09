package example.EmployeeRegistration;
import jade.core.*;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.*;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.*;

public class Employee extends Agent
{
	String details="";
	protected void setup()
	{
		 
		System.out.println("\nMy nick name is "+getLocalName());  
		DFAgentDescription dfd=new DFAgentDescription();
		ServiceDescription sd=new ServiceDescription();
		sd.setName(getLocalName());
		sd.setType("First");
		dfd.setName(getAID());
		dfd.addServices(sd);
		
		try
		{
			DFService.register(this,dfd);
			Object [] args=getArguments();
			if(args!=null && args.length > 0)
			{
				details=(String) args[0];
			}
				
			sendMsg("regis",details,ACLMessage.INFORM);
			addBehaviour(new myBehaviour(this));

		}
		catch(FIPAException e)
		{
			System.out.println("Error ! : "+e);
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
			
			if (msg!=null)
			{
				if (msg.getPerformative()==ACLMessage.INFORM)
				{
					System.out.println("Got the msg.... \n");
					System.out.println("The sender is "+msg.getSender().getLocalName()+". The reciever is "+getLocalName()+" Msg is "+msg.getContent()+"\n");
				}
			}
		}
	}
}
	
	