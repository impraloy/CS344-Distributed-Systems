import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote{
	
	/**
	 * Sends message to the serrver.
	 * 
	 * @param message the message to send.
	 * @throws RemoteException
	 */
	void sendMessage(String message) throws RemoteException;
	
	/**
	 * Recieves message from the server.
	 * 
	 * @param message the message from the server.
	 * @throws RemoteException
	 */
	void receiveMessage(String message) throws RemoteException;

	/**
	 * Receives portname and hostname, obtains a server stub copy from the 
	 *  registry and assigns the copy to this client's serverStub.
	 *  
	 * @param hostname - the hostname to which this client registers.
	 * @param portname - the portname to which this client registers.
	 * @throws RemoteException when a RMI exception occurs.
	 */  	
//	void obtainServerStub(String hostname, int portname) throws RemoteException, NotBoundException;
  	 
	/**
	 * Assigns the value of the parameter name to this client's name attribute.
	 * 
	 * @param name the name to assign to this client.
	 * @throws RemoteException
	 */
	void setName(String name) throws RemoteException;
	
	/**
	 * Return this client's name.
	 * 
	 * @return this client's name.
	 * @throws RemoteException
	 */
	String getName() throws RemoteException;
	
	/**
	 * Assigns the value of the parameter id to this client's id attribute.
	 * 
	 * @param id the id to assign to this client.
	 * @throws RemoteException
	 */
	void setID(int id) throws RemoteException;
	
	/**
	 * Return this client's id.
	 * 
	 * @return this client's id
	 * @throws RemoteException
	 */
	int getID() throws RemoteException;

}
