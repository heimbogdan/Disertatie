package ro.helator.dia.util;

import java.util.UUID;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueReceiver;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQSession;
import org.apache.activemq.broker.Broker;
import org.apache.activemq.command.ActiveMQBlobMessage;
import org.apache.activemq.command.ActiveMQBytesMessage;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.apache.log4j.Logger;
import ro.helator.dia.server.Server;

public class BrokerConnector {

	private static final Logger log = Logger.getLogger(Broker.class);
	
	private static MessageProducer brokerProducer_RouteTemplates;
	private static Queue ROUTE_TEMPLATES_OUT;
	
	private Server server;
	
	private ActiveMQConnectionFactory mqConnectionFactory;
	private ActiveMQConnection connection;
	private ActiveMQSession session;

	public BrokerConnector(Server server) {
		
		this.server = server;
		if (log.isTraceEnabled()) {
			log.trace("Create ActiveMQ connection factory...");
		}
		mqConnectionFactory = new ActiveMQConnectionFactory("tcp://" + this.server.getIp() + ":" + this.server.getPort());
		if (log.isTraceEnabled()) {
			log.trace("ActiveMQ connection factory created for [IP:Port] : " + this.server.getIp() + ":" + this.server.getPort());
		}
		createConnection();

	}
	
	
	public void initQueues() throws JMSException {
		createSession();
		brokerProducer_RouteTemplates = session.createProducer(getDestination("SYSTEM.TEMPLATE.LIST.IN", DestinationType.QUEUE));

		ROUTE_TEMPLATES_OUT = session.createQueue("SYSTEM.TEMPLATE.LIST.OUT");

	}
	
	private void createConnection() {
		try {
			if (log.isTraceEnabled()) {
				log.trace("Create ActiveMQ connection...");
			}
			connection = (ActiveMQConnection) mqConnectionFactory.createConnection();
			if (log.isTraceEnabled()) {
				log.trace("ActiveMQ connection created.");
			}
		} catch (JMSException e) {
			log.error(e);
		}
	}
	
	public void startConnection() {
		if (this.connection != null && !isConnectionStarted()) {
			try {
				if (log.isTraceEnabled()) {
					log.trace("Set ClientID for current session...");
				}
				String clientId = UUID.randomUUID().toString();
				connection.setClientID(clientId);
				if (log.isTraceEnabled()) {
					log.trace("ClientID was set to [" + clientId + "]");
				}
			} catch (JMSException e1) {
				log.error(e1);
			}
			try {
				if (log.isTraceEnabled()) {
					log.trace("Start connection to ActiveMQ...");
				}
				connection.start();
				if (log.isTraceEnabled()) {
					log.trace("Connection started!");
				}
			} catch (JMSException e) {
				log.error(e);
				if (log.isTraceEnabled()) {
					log.trace("Retry to connect...");
				}
				try {
					connection.start();
					if (log.isTraceEnabled()) {
						log.trace("Connection started!");
					}
				} catch (JMSException e1) {
					log.error(e1);
				}
			}
		} else {
			if (log.isTraceEnabled()) {
				log.trace("Connection already started!");
			}
		}
	}
	
	private void createSession() {
		if (this.connection == null) {
			createConnection();
		}
		if (!isConnectionStarted()) {
			startConnection();
		}
		try {
			if (log.isTraceEnabled()) {
				log.trace("Create session...");
			}
			session = (ActiveMQSession) connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			if (log.isTraceEnabled()) {
				log.trace("Session created!");
			}
		} catch (JMSException e) {
			log.error(e);
		}
	}
	
	private Destination createDestination(String destinationName, DestinationType destinationType) {
		if (this.session == null) {
			createSession();
		}
		Destination destination = null;
		switch (destinationType) {
		case QUEUE:
			try {
				destination = session.createQueue(destinationName);
				if (log.isTraceEnabled()) {
					log.trace("Destination Queue created for [" + destinationName + "]");
				}
			} catch (JMSException e) {
				log.error(e);
			}
			break;
		case TOPIC:
			try {
				destination = session.createTopic(destinationName);
				if (log.isTraceEnabled()) {
					log.trace("Destination Topic created for [" + destinationName + "]");
				}
			} catch (JMSException e) {
				log.error(e);
			}
			break;
		default:
			break;
		}
		return destination;
	}
	
	public Connection getConnection() {
		return this.connection;
	}

	public ActiveMQSession getSession() {
		if (this.session == null) {
			createSession();
		}
		return this.session;
	}

	public Destination getDestination(String destinationName, DestinationType destinationType) {
		if (connection == null) {
			createConnection();
		}
		if (session == null) {
			createSession();
		}
		return createDestination(destinationName, destinationType);
	}

	public boolean isConnectionStarted() {
		return this.connection.isStarted();
	}

	public void closeSession() {
		try {
			this.session.close();
			this.session = null;
		} catch (JMSException e) {
			log.error(e);
		}
	}

	public void closeConnection() {
		if (this.session != null && !this.session.isClosed()) {
			closeSession();
		}
		try {
			this.connection.close();
			this.connection = null;
		} catch (JMSException e) {
			log.error(e);
		}
	}
	
	public String request(String request, RequestType type) throws JMSException {
		String response = null;
		Long waitTime = 120000L;
		TextMessage message = session.createTextMessage(request);
		String corelId = UUID.randomUUID().toString();
		message.setJMSCorrelationID(corelId);
		String selector = "JMSCorrelationID='" + corelId + "'";
		switch (type) {
		case ROUTE_TEMPLATE_LIST:
			brokerProducer_RouteTemplates.send(message);
			response = getMessageAsString(ROUTE_TEMPLATES_OUT, selector,waitTime);
			break;
		default:
			break;
		}
		return response;
	}
	
	private String getMessageAsString(Queue queue, String selector, Long wait) throws JMSException {
		QueueReceiver q = session.createReceiver(queue, selector);
		Message msg = q.receive(wait);
		if (msg != null) {
			if (msg instanceof ActiveMQBytesMessage) {
				ActiveMQBytesMessage byteMsg = (ActiveMQBytesMessage) msg;
				return new String(byteMsg.getContent().data);
			} else if (msg instanceof ActiveMQTextMessage) {
				ActiveMQTextMessage textMsg = (ActiveMQTextMessage) msg;
				try {
					return textMsg.getText();
				} catch (JMSException e) {
					return null;
				}
			} else if (msg instanceof ActiveMQBlobMessage) {
				ActiveMQBlobMessage blobMsg = (ActiveMQBlobMessage) msg;
				return new String(blobMsg.getContent().data);
			}
		}
		return null;
	}
	
}
