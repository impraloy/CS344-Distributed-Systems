

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote{
	/**
	 * Sends message to everyone in the Chatroom.
	 * 
	 * @param message - the message to broadcast.
	 */
	void broadcastToAll(String message) throws RemoteException;	
	
	/**
	 * Prompts for client's name, broadcasts to all clients, and adds
	 *  the clieny to the list when a client enters the Chatroom.
	 *  
	 * @param clientName - name of the client.
	 */
	void welcome(String clientName) throws RemoteException, NotBoundException;
	
	/**
	 * Broadcasts to the message to everyone else in the Chatroom when
	 *  a client enters a message.
	 *  
	 * @param message - message entered by a client.
	 */
	void clientToall(String message) throws RemoteException;
	
	/**
	 * Broadcasts to all clients when client exits the chatroom, and
	 *  removes the client from the list.
	 *  
	 * @param client - the client to remove.
	 */
	void removeClient(String client) throws RemoteException, NotBoundException;
	
	/**
	 * Returns true if a client with given name is in server's list of clients.
	 *  Otherwise returns false;
	 * 
	 * @param clientName - name of client
	 * @return true if client with given name is registered, or false otherwise.
	 * @throws RemoteException
	 */
	public boolean hasClient(String clientName) throws RemoteException;


}
