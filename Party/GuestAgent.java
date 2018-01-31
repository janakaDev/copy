
package examples.party;

import jade.core.Agent;
import jade.core.AID;

import jade.domain.FIPAException;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import jade.core.behaviours.CyclicBehaviour;

import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.DFService;




public class GuestAgent
    extends Agent
{

    protected boolean m_knowRumour = false;

    protected void setup() {
        try {
           
            ServiceDescription sd = new ServiceDescription();
            sd.setType( "PartyGuest" );
            sd.setName( "GuestServiceDescription" );
            DFAgentDescription dfd = new DFAgentDescription();
            dfd.setName( getAID() );
            dfd.addServices( sd );

            
            DFService.register( this, dfd );

           
            ACLMessage hello = new ACLMessage( ACLMessage.INFORM );
            hello.setContent( HostAgent.HELLO );
            hello.addReceiver( new AID( "host", AID.ISLOCALNAME ) );
            send( hello );

            
            addBehaviour( new CyclicBehaviour( this ) {
                            public void action() {
                              
                                ACLMessage msg = receive( MessageTemplate.MatchPerformative( ACLMessage.INFORM ) );

                                if (msg != null) {
                                    if (HostAgent.GOODBYE.equals( msg.getContent() )) {
                                        
                                        leaveParty();
                                    }
                                    else if (msg.getContent().startsWith( HostAgent.INTRODUCE )) {
                                        
                                        introducing( msg.getContent().substring( msg.getContent().indexOf( " " ) ) );
                                    }
                                    else if (msg.getContent().startsWith( HostAgent.HELLO )) {
                                        
                                        passRumour( msg.getSender() );
                                    }
                                    else if (msg.getContent().startsWith( HostAgent.RUMOUR )) {
                                       
                                        hearRumour();
                                    }
                                    else {
                                        System.out.println( "Guest received unexpected message: " + msg );
                                    }
                                }
                                else {
                                   
                                    block();
                                }
                            }
                        } );
        }
        catch (Exception e) {
            System.out.println( "Saw exception in GuestAgent: " + e );
            e.printStackTrace();
        }

    }

    protected void leaveParty() {
        try {
            DFService.deregister( this );
            doDelete();
        }
        catch (FIPAException e) {
            System.err.println( "Saw FIPAException while leaving party: " + e );
            e.printStackTrace();
        }
    }


  
    protected void introducing( String agentName ) {
        
        AID aID = new AID( agentName, AID.ISGUID); 

        ACLMessage m = new ACLMessage( ACLMessage.INFORM );
        m.setContent( HostAgent.HELLO );
        m.addReceiver( aID );

        send( m );

       
        ACLMessage m1 = new ACLMessage( ACLMessage.REQUEST );
        m1.setContent( HostAgent.INTRODUCE );
        m1.addReceiver( new AID( "host", AID.ISLOCALNAME ) );
        send( m1 );
    }


  
    protected void passRumour( AID agent ) {
        if (m_knowRumour) {
            ACLMessage m = new ACLMessage( ACLMessage.INFORM );
            m.setContent( HostAgent.RUMOUR );
            m.addReceiver( agent );
            send( m );
        }
    }


   
    protected void hearRumour() {
       
        if (!m_knowRumour) {
            ACLMessage m = new ACLMessage( ACLMessage.INFORM );
            m.setContent( HostAgent.RUMOUR );
            m.addReceiver( new AID( "host", AID.ISLOCALNAME ) );
            send( m );

            m_knowRumour = true;
        }
    }

}

