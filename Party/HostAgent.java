
package examples.party;

import jade.core.AID;
import jade.core.Agent;
import jade.core.ProfileImpl;
import jade.core.Profile;

import jade.wrapper.PlatformController;
import jade.wrapper.AgentController;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;

import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.DFService;
import jade.domain.FIPAException;

import javax.swing.*;
import java.util.*;
import java.text.NumberFormat;


public class HostAgent
    extends Agent
{
   
    public final static String HELLO = "HELLO";
    public final static String ANSWER = "ANSWER";
    public final static String THANKS = "THANKS";
    public final static String GOODBYE = "GOODBYE";
    public final static String INTRODUCE = "INTRODUCE";
    public final static String RUMOUR = "RUMOUR";

	protected JFrame m_frame = null;
    protected Vector m_guestList = new Vector();    
    protected int m_guestCount = 0;                 
    protected int m_rumourCount = 0;
    protected int m_introductionCount = 0;
    protected boolean m_partyOver = false;
    protected NumberFormat m_avgFormat = NumberFormat.getInstance();
    protected long m_startTime = 0L;


    
    public HostAgent() {
        m_avgFormat.setMaximumFractionDigits( 2 );
        m_avgFormat.setMinimumFractionDigits( 2 );
    }



    
    protected void setup() {
        try {
            System.out.println( getLocalName() + " setting up");

            
            DFAgentDescription dfd = new DFAgentDescription();
            dfd.setName( getAID() );
            DFService.register( this, dfd );

          
            setupUI();

            
            addBehaviour( new CyclicBehaviour( this ) {
                            public void action() {
                                ACLMessage msg = receive();

                                if (msg != null) {
                                    if (HELLO.equals( msg.getContent() )) {
                                        
                                        m_guestCount++;
                                        setPartyState( "Inviting guests (" + m_guestCount + " have arrived)" );

                                        if (m_guestCount == m_guestList.size()) {
                                            System.out.println( "All guests have arrived, starting conversation" );
                                            
                                            beginConversation();
                                        }
                                    }
                                    else if (RUMOUR.equals( msg.getContent() )) {
                                        
                                        incrementRumourCount();
                                    }
                                    else if (msg.getPerformative() == ACLMessage.REQUEST  &&  INTRODUCE.equals( msg.getContent() )) {
                                        
                                        doIntroduction( msg.getSender() );
                                    }
                                }
                                else {
                                   
                                    block();
                                }
                            }
                        } );
        }
        catch (Exception e) {
            System.out.println( "Saw exception in HostAgent: " + e );
            e.printStackTrace();
        }

    }


   
    private void setupUI() {
        m_frame = new HostUIFrame( this );

        m_frame.setSize( 400, 200 );
        m_frame.setLocation( 400, 400 );
        m_frame.setVisible( true );
        m_frame.validate();
    }


   
    protected void inviteGuests( int nGuests ) {
        
        m_guestList.clear();
        m_guestCount = 0;
        m_rumourCount = 0;
        m_introductionCount = 0;
        m_partyOver = false;
        ((HostUIFrame) m_frame).lbl_numIntroductions.setText( "0" );
        ((HostUIFrame) m_frame).prog_rumourCount.setValue( 0 );
        ((HostUIFrame) m_frame).lbl_rumourAvg.setText( "0.0" );

        
        m_startTime = System.currentTimeMillis();

        setPartyState( "Inviting guests" );

	PlatformController container = getContainerController(); 
        
        try {
            for (int i = 0;  i < nGuests;  i++) {
                
		String localName = "guest_"+i;
		AgentController guest = container.createNewAgent(localName, "examples.party.GuestAgent", null);
		guest.start();
                
                m_guestList.add( new AID(localName, AID.ISLOCALNAME) );
            }
        }
        catch (Exception e) {
            System.err.println( "Exception while adding guests: " + e );
            e.printStackTrace();
        }
    }


    protected void endParty() {
        setPartyState( "Party over" );
        m_partyOver = true;

        
        System.out.println( "Simulation run complete. NGuests = " + m_guestCount + ", time taken = " +
                            m_avgFormat.format( ((double) System.currentTimeMillis() - m_startTime) / 1000.0 ) + "s" );

        
        for (Iterator i = m_guestList.iterator();  i.hasNext();  ) {
            ACLMessage msg = new ACLMessage( ACLMessage.INFORM );
            msg.setContent( GOODBYE );

            msg.addReceiver( (AID) i.next() );

            send(msg);
        }

        m_guestList.clear();
    }


    
    protected void terminateHost() {
        try {
            if (!m_guestList.isEmpty()) {
                endParty();
            }

            DFService.deregister( this );
            m_frame.dispose();
            doDelete();
        }
        catch (Exception e) {
            System.err.println( "Saw FIPAException while terminating: " + e );
            e.printStackTrace();
        }
    }


   
    protected void beginConversation() {
        
        ACLMessage rumour = new ACLMessage( ACLMessage.INFORM );
        rumour.setContent( RUMOUR );
        rumour.addReceiver( randomGuest( null ) );
        send( rumour );

        
        doIntroduction( randomGuest( null ) );
        setPartyState( "Swinging" );
    }


    
    protected void doIntroduction( AID guest0 ) {
        if (!m_partyOver) {
            AID guest1 = randomGuest( guest0 );

            
            ACLMessage m = new ACLMessage( ACLMessage.INFORM );
            m.setContent( INTRODUCE + " " + guest0.getName() );
            m.addReceiver( guest1 );
            send( m );

            
            m_introductionCount++;
            SwingUtilities.invokeLater( new Runnable() {
                                            public void run() {
                                                ((HostUIFrame) m_frame).lbl_numIntroductions.setText( Integer.toString( m_introductionCount ));
                                            }
                                        } );
            updateRumourAvg();
        }
    }


    protected void incrementRumourCount() {
        m_rumourCount++;
        SwingUtilities.invokeLater( new Runnable() {
                                        public void run() {
                                            ((HostUIFrame) m_frame).prog_rumourCount.setValue( Math.round( 100 * m_rumourCount / m_guestCount ) );
                                        }
                                    } );
        updateRumourAvg();

        
        if (m_rumourCount == m_guestCount) {
            
            try {
                SwingUtilities.invokeAndWait( new Runnable() {
                                                public void run() {
                                                    ((HostUIFrame) m_frame).btn_stop_actionPerformed( null );
                                                }
                                            } );
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


  
    protected void setPartyState( final String state ) {
        SwingUtilities.invokeLater( new Runnable() {
                                        public void run() {
                                            ((HostUIFrame) m_frame).lbl_partyState.setText( state );
                                        }
                                    } );
    }


    
    protected void updateRumourAvg() {
        SwingUtilities.invokeLater( new Runnable() {
                                        public void run() {
                                            ((HostUIFrame) m_frame).lbl_rumourAvg.setText( m_avgFormat.format( ((double) m_introductionCount) / m_rumourCount ) );
                                        }
                                    } );
    }


   
    protected AID randomGuest( AID aGuest ) {
        AID g = null;

        do {
            int i = (int) Math.round( Math.random() * (m_guestList.size() - 1) );
            g = (AID) m_guestList.get( i );
        } while ((g!=null) && g.equals(aGuest));

        return g;
    }



}


