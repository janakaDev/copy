package examples.thanksAgent;

import jade.core.Agent;
import jade.core.AID;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import jade.core.Runtime;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.*;



public class ThanksAgent extends Agent {

	private static boolean IAmTheCreator = true;
	
	private int answersCnt = 0;

	public final static String GREETINGS = "GREETINGS";
	public final static String ANSWER = "ANSWER";
	public final static String THANKS = "THANKS";
	private AgentContainer ac = null;
	private AgentController t1 = null;
	private AID initiator = null;

	protected void setup() {
		System.out.println(getLocalName()+" STARTED");
		Object[] args = getArguments();
		if (args != null && args.length > 0) {
			initiator = new AID((String) args[0], AID.ISLOCALNAME);
		}

		try {
			
			DFAgentDescription dfd = new DFAgentDescription();
			dfd.setName(getAID());
			
			DFService.register(this, dfd);
			System.out.println(getLocalName()+" REGISTERED WITH THE DF");
		} catch (FIPAException e) {
			e.printStackTrace();
		}

		if (IAmTheCreator) {
			IAmTheCreator = false;  

			
			String t1AgentName = getLocalName()+"t1";
			String t2AgentName = getLocalName()+"t2";

			try {
				
				AgentContainer container = (AgentContainer)getContainerController(); 
				t1 = container.createNewAgent(t1AgentName, "examples.thanksAgent.ThanksAgent", null);
				t1.start();
				System.out.println(getLocalName()+" CREATED AND STARTED NEW THANKSAGENT:"+t1AgentName + " ON CONTAINER "+container.getContainerName());
			} catch (Exception any) {
				any.printStackTrace();
			}


			
			Runtime rt = Runtime.instance();
			
			ProfileImpl p = new ProfileImpl(false);

			try {
				
				ac = rt.createAgentContainer(p);
				
				AgentController t2 = ac.createNewAgent(t2AgentName,getClass().getName(),new Object[0]);
				
				t2.start();
				System.out.println(getLocalName()+" CREATED AND STARTED NEW THANKSAGENT:"+t2AgentName + " ON CONTAINER "+ac.getContainerName());
			} catch (Exception e2) {
				e2.printStackTrace();
			}

			
			ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
			msg.setContent(GREETINGS);

			msg.addReceiver(new AID(t1AgentName, AID.ISLOCALNAME));
			msg.addReceiver(new AID(t2AgentName, AID.ISLOCALNAME));

			send(msg);
			System.out.println(getLocalName()+" SENT GREETINGS MESSAGE  TO "+t1AgentName+" AND "+t2AgentName); 
		}  
		
		addBehaviour(new CyclicBehaviour(this) {
			public void action() {
				
				ACLMessage msg = receive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
				if (msg != null) {
					if (GREETINGS.equalsIgnoreCase(msg.getContent())) {
						
						System.out.println(myAgent.getLocalName()+" RECEIVED GREETINGS MESSAGE FROM "+msg.getSender().getLocalName()); 
						ACLMessage reply = msg.createReply();
						reply.setContent(ANSWER);
						myAgent.send(reply);
						System.out.println(myAgent.getLocalName()+" SENT ANSWER MESSAGE");
					} 
					else if (ANSWER.equalsIgnoreCase(msg.getContent())) {
						
						System.out.println(myAgent.getLocalName()+" RECEIVED ANSWER MESSAGE FROM "+msg.getSender().getLocalName()); 
						ACLMessage replyT = msg.createReply();
						replyT.setContent(THANKS);
						myAgent.send(replyT);
						System.out.println(myAgent.getLocalName()+" SENT THANKS MESSAGE"); 
						answersCnt++;
						if (answersCnt == 2) {
							
							try {
								Thread.sleep(1000);
							} 
							catch (InterruptedException ie) {}
							try {
								
								ac.kill();
								
								t1.kill();
							
								IAmTheCreator = true;
								
								if (initiator != null) {
									ACLMessage notification = new ACLMessage(ACLMessage.INFORM);
									notification.addReceiver(initiator);
									send(notification);
								}	
							} 
							catch (StaleProxyException any) {
								any.printStackTrace();
							}
						}
					}
					else if (THANKS.equalsIgnoreCase(msg.getContent())) {
						System.out.println(myAgent.getLocalName()+" RECEIVED THANKS MESSAGE FROM "+msg.getSender().getLocalName()); 
					}
					else {
						System.out.println(myAgent.getLocalName()+" Unexpected message received from "+msg.getSender().getLocalName()); 
					}					
				}
				else {
					
					block();
				}
			}
		});
	}

	protected void takeDown() {
		
		try {
			DFService.deregister(this);
			System.out.println(getLocalName()+" DEREGISTERED WITH THE DF");
		} catch (FIPAException e) {
			e.printStackTrace();
		}
	}
}
