/** 
 * name: Yuqing Chang
 * student number: 1044862
 * username: yuqchang
 */
package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * RMI Remote interface - must be shared between client and server.
 * All methods must throw RemoteException.
 * Any object that is a remote object must implement this interface.
 * Only those methods specified in a "remote interface" are available remotely. 
 * It defines main functions to manage clients in white board project.
 */
public interface IRemoteUser extends Remote {
    /**
     * Get the manager name.
     * @return whiteboard manager's name
     */
	public String getManagerName() throws RemoteException;

    /**
     * Set the manager name.
     * @param managerName whiteboard's manager's name
     */
	public void setManagerName(String managerName) throws RemoteException;

	/**
	 * Get a list of current user names.
	 * @return list of users' names
	 */
	public List<String> getUserNames() throws RemoteException;
	
	/**
	 * Add a new user name in current user names' list.
	 * @param userName
	 * @throws RemoteException
	 */
	public void addUser(String userName) throws RemoteException;
	/**
	 * Add a list of user names in current user names' list.
	 * @param userNames
	 * @throws RemoteException
	 */
	public void addUsers(String[] userNames) throws RemoteException;
	
	/**
	 * Show message on the GUI.
	 * @param msg
	 * @throws RemoteException
	 */
	public void recieveMsg(String msg) throws RemoteException;
	/**
	 * When an error occurs, an error alert box pops up to interact with the user.
	 * @param str
	 * @throws RemoteException
	 */
	public void errorGUI(String str) throws RemoteException;
	 /**
	 * Transfer and load the previous canvas to all users.
	 * @param i
	 * @throws RemoteException
	 */
	public void transfer(byte[] i) throws RemoteException;
}
