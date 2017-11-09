package example.EmployeeRegistration;
import jade.core.*;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.*;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.*;

public class RegCenter extends Agent
{
	String [][] empList=new String [100][3];
	int i,j=0;
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
				System.out.println("Got the msg.... \n");
				System.out.println("The sender is "+msg.getSender().getLocalName()+". The reciever is "+getLocalName()+" Msg is "+msg.getContent()+"\n");
				if (msg.getPerformative()==ACLMessage.INFORM)
				{
					String det[]=msg.getContent().split("&");
					if(Integer.parseInt(det[1])>25)
					{
						for(int j=0;j<det.length;j++)
						{
							empList[i][j]=det[j];
						}
						sendMsg(msg.getSender().getLocalName(),"registered",ACLMessage.INFORM);
						i++;
					}
					else
					{
						sendMsg(msg.getSender().getLocalName(),"not registered",ACLMessage.INFORM);
					}
					
				}
				if (msg.getPerformative()==ACLMessage.REQUEST)
				{
					String salary=msg.getContent();
					System.out.println(salary);
					for(int i=0;i<100;i++)
					{
						if(empList[i][2]!=null && Integer.parseInt(empList[i][2])<Integer.parseInt(salary))
						{
							sendMsg(msg.getSender().getLocalName(),empList[i][0],ACLMessage.INFORM);
						}
					}
				}
			}
		}
	}
}
	
	