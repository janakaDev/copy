package examples.IT8017;
import jade.core.*;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.*;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.*;
public class department extends Agent
{
	protected void setup()
	{
		System.out.println("Hi! I'm "+getLocalName());
		DFAgentDescription dfad=new DFAgentDescription();
		ServiceDescription sd=new ServiceDescription();
		sd.setName(getLocalName());
		sd.setType("First");
		dfad.setName(getAID());
		dfad.addServices(sd);

		try
		{
			DFService.register(this,dfad);
			addBehaviour(new myBehaviour(this));
		}
		catch(FIPAException e)
		{
			System.out.println(e);
		}
	}

	public void sendmsg(String rec, String msg, int p)
	{
		ACLMessage acl=new ACLMessage();
		AID r=new AID(rec,AID.ISLOCALNAME);
		acl.addReceiver(r);
		acl.setContent(msg);
		acl.setPerformative(p);
		send(acl);
		System.out.println(getLocalName()+" Message sent to "+rec);
	}

	public class myBehaviour extends CyclicBehaviour
	{
		myBehaviour(Agent a)
		{
			super(a);
		}

		public void action()
		{
			ACLMessage msg=myAgent.receive();
			if (msg!=null) {
				if (msg.getPerformative()==ACLMessage.REQUEST) {
					System.out.println(msg.getContent());
					sendmsg("student1","You have registered Successfuy",ACLMessage.REQUEST);
				}
			}
		}
	}
}