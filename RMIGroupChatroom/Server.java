

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;

public class Server extends UnicastRemoteObject implements ServerInterface{
	private int currentId;
	private int numClients;
	private List<ClientInterface> clients;
	private Registry registry; //RMI registry.
	
	public Server(String hostname, int portname) throws RemoteException{
		super();
		this.currentId  = 0;
		this.numClients = 0;
		this.clients = new ArrayList<ClientInterface>();
		
		//Create RMI registry with the portname.		
		registry = LocateRegistry.createRegistry(portname);
		
		//Bind this server stub to the registry.
		registry.rebind("rmi://" + hostname + ":" + portname + "/server", this);
		
		//Display message
		System.out.println("Chatroom is open.");	
	}

	@Override
	public void broadcastToAll(String message) throws RemoteException{
		for (ClientInterface client : clients) {
			client.receiveMessage("ChatMaster: " + message);
		}
	}

	@Override
	public void welcome(String clientName) throws RemoteException, NotBoundException{
		String message = clientName + " just joined the Chatroom.";
		
		ClientInterface newClient = (ClientInterface)registry.lookup(clientName);
		newClient.setID(++currentId);
				
		System.out.println(message);	
		System.out.println(clientName + "'s assigned ID = " + newClient.getID());
		
		broadcastToAll(message);
	
		newClient.receiveMessage("Welcome to the ChatRoom, " + clientName + "!");
	    if (currentId == 1){
	    	newClient.receiveMessage("It seems you are the first person in this ChatRoom. :)");
	    }else {
	    	message = "You can chat with:";
	    	for (ClientInterface client : clients) {
	    		message += " " + client.getName();
    		}
	    	newClient.receiveMessage(message);
	    }    
	    newClient.receiveMessage("\nYou can send messages now, or just enter \"exit\" to leave ChatRoom.");
		
	    clients.add(newClient);
	    numClients++;
	}

	@Override
	public void clientToall(String message) throws RemoteException {
		String clientName = message.substring(0,message.indexOf(':'));
		String output = clientName + "'s message has been sent to ";
		System.out.println(message);
		
		for (int i=0; i < clients.size(); i++) {
			ClientInterface currentClient = clients.get(i);
			if(!clientName.equalsIgnoreCase(currentClient.getName())) {
				currentClient.receiveMessage(message);
				output += currentClient.getName();
				if (i < clients.size()-1) {
					output += ", ";
				}
			}
		}
		
		if (numClients > 1) {
			System.out.println(output);
		}
	}

	@Override
	public void removeClient(String client) throws RemoteException, NotBoundException {
		ClientInterface clientToRemove = null;		
		Iterator<ClientInterface> iter = clients.iterator();
        while(iter.hasNext()) {
        	clientToRemove = iter.next();
        	if(client.equalsIgnoreCase(clientToRemove.getName())) {
        		break;
        	}
        }
        if (clientToRemove != null) {
        	System.out.println("removing..." + clientToRemove.getName()
        					+ " (" + clientToRemove.getID() + ")");
        	
        	String message = clientToRemove.getName() + " left the ChatRoom."; 
        	System.out.println(message);  
        	broadcastToAll(message);
        	
    		registry.unbind(clientToRemove.getName());
    		
    		iter.remove();
    		
            System.out.println("Successfully removed client " + clientToRemove.getName());
        }
        numClients--;
	}
	
	public boolean hasClient(String clientName) throws RemoteException{
		for (ClientInterface client : clients) {
			if (clientName.equalsIgnoreCase(client.getName())) {
				return true;
			}
		}
		return false;
	}	
	
	public static void main(String args[]) {		
		int port = 5000;
		String host = "localhost";
		
		 //Retrieve hostname and hostname from command line.
		if (args.length != 2) {
			System.out.println("ERROR: specify hostname and portname.\n Ex. 'Java Server localhost 5000'.");			
			System.exit(1);			
		}

		if(host.isEmpty()) {
			System.out.println("ERROR: Invalid hostname.");		
		}
		
		host = args[0];
	
		try {
			port = Integer.parseInt(args[1]);
		}catch(Exception e) {
			System.out.println("ERROR: Invalid portname.");
			System.exit(1);			
		}
					
		try {
			//Create a Chatroom stub (remote object)
			new Server(host, port);
			
			Scanner input = new Scanner(System.in);
			String message = input.nextLine();
			if (message.equalsIgnoreCase("exit")) {
				System.exit(0);
			}
		}catch(Exception e) {
			System.out.println("ERROR: " + e.getMessage());
			System.exit(1);
		}
	}

}
