

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class Client extends UnicastRemoteObject implements ClientInterface {
	private static final long serialVersionUID = 1L;
	
	//An id assigned to this client by the server.
	private int id;
	//Name of this client.
	private String name;
	//The port to which this client registers.
	private int portname;
	//The host to which this client registers.
	private String hostname;
	//The server stub that listens to this client.
	private ServerInterface serverStub;
	
	/**
	 * Constructor: accepts a name, portname and hostname then obtains a server
	 *  stub copy from the registry and assigns the copy to this client's serverStub.
	 * 
	 * @param name - name for this client;
	 * @param hostname - the hostname to which this client registers.
	 * @param portname - the portname to which this client registers.
	 * @throws RemoteException when a RMI exception occurs.
	 * @throws NotBoundException 
	 * @throws AlreadyBoundException 
	 */
	public Client(String hostname, int portname) throws RemoteException, NotBoundException {	
		String url = "rmi://" + hostname + ":" + portname + "/server";
	    Registry registry = LocateRegistry.getRegistry(portname);
	    serverStub = (ServerInterface)registry.lookup(url);
	    
	    System.out.println("Welcome to the Chatroom.");

		//For input from the keyboard.
		Scanner input = new Scanner(System.in);
		//Sentinel for exiting while loop.
		boolean valid = false;

		System.out.print("Please enter your name: ");
		while(!valid) {
			name = input.nextLine();				
			if(name.isEmpty()) {
				System.out.print("Invalid name. Re-enter: ");						
			}
			else if(serverStub.hasClient(name)) {
				System.out.print("Name is not available. Re-enter: ");					
			}else {
				valid = true;
			}
		}
		
		//Bind the client stub to the registry.
		registry.rebind(name, this);
		//Call server's welcome method
		serverStub.welcome(name);
		
		boolean done = false;
		while(!done) {
			String message = input.nextLine();
			if(!message.isEmpty()) {
				//If message is exit, remove client from Chatroom.
				if (message.equalsIgnoreCase("exit")) {
					done = true;
					serverStub.removeClient(name);
				}else {
					sendMessage(message);
				}
			}
		}
		input.close();
	}

	/**
	 * Sends message to the serrver.
	 * 
	 * @param message the message to send.
	 * @throws RemoteException
	 */
	@Override
	public void sendMessage(String message) throws RemoteException {
		serverStub.clientToall(name + ": " + message);
	}

	/**
	 * Recieves message from the server.
	 * 
	 * @param message the message from the server.
	 * @throws RemoteException
	 */
	@Override
	public void receiveMessage(String message) {
		System.out.println(message);
	}
	
	/**
	 * Assigns the value of the parameter name to this client's name attribute.
	 * 
	 * @param name the name to assign to this client.
	 * @throws RemoteException
	 */
	@Override
	public void setName(String name) throws RemoteException{
		this.name = name;		
	}

	/**
	 * Return this client's name.
	 * 
	 * @return this client's name.
	 * @throws RemoteException
	 */
	@Override
	public String getName() throws RemoteException {
		return name;
	}
  
	/**
	 * Assigns the value of the parameter id to this client's id attribute.
	 * 
	 * @param id the id to assign to this client.
	 * @throws RemoteException
	 */
	@Override
	public void setID(int id) throws RemoteException {
		this.id = id;		
	}

	/**
	 * Return this client's id.
	 * 
	 * @return this client's id
	 * @throws RemoteException
	 */
	@Override
	public int getID() throws RemoteException {
		return id;
	}


   /**
    * The main method. Entry point for this Client. 
    * 
    *  @param args an array of the command line arguments.
    */
	public static void main(String[] args) {
		int portname = 5000;
		String hostname = "localhost";
		
		 //Exit if hostname and hostname are not provided.
		if (args.length != 2) {
			System.out.println("ERROR: Specify hostname and portname.\n Ex. 'Java Client localhost 5000'.");			
			System.exit(1);			
		}
		
		if(hostname.isEmpty()) {
			System.out.println("ERROR: Invalid hostname.");		
		}

		hostname = args[0];
	
		try {
			portname = Integer.parseInt(args[1]);
		}catch(Exception e) {
			System.out.println("ERROR: Invalid portname.");
			System.exit(1);			
		}
		
		
		try {
			new Client(hostname, portname);
			System.exit(0);
		}catch(Exception e) {
			System.out.println("ERROR: " + e.getMessage());
		}
	}

}
