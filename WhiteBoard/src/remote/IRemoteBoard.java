/** 
 * name: Yuqing Chang
 * student number: 1044862
 * username: yuqchang
 */
package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import server.Users;

/**
 * RMI Remote interface - must be shared between client and server.
 * All methods must throw RemoteException.
 * Any object that is a remote object must implement this interface.
 * Only those methods specified in a "remote interface" are available remotely. 
 * 
 * It defines main functions to manage white board, which includes drawing shapes, monitoring state of the board,
 * interacting with GUI.
 */
public interface IRemoteBoard extends Remote {
	/**
	 * Get the current board.
	 * @return
	 * @throws RemoteException
	 */
	public ShapeImage getBoard() throws RemoteException;
	/**
	 * @param image image for canvas
	 */
	public void setImage(ShapeImage image) throws RemoteException;
	
	/*
	 * These functions define the basic drawing operations.
	 */
	public void line(int x1, int y1, int x2, int y2) throws RemoteException;
	
	public void circle(int x, int y, int rectwidth, int rectheight) throws RemoteException;
	
	public void text(String msg, int x, int y) throws RemoteException;
	
	public void rectangle(int x, int y, int rectwidth, int rectheight) throws RemoteException;
	
	public void draw(byte[] image) throws RemoteException;
	/**
	 * Remove all points on the board.
	 * @throws RemoteException
	 */
	public void removeAll() throws RemoteException;
	
	/**
	 * Check if current user list is empty.
	 * @return
	 * @throws RemoteException
	 */
	public boolean isEmpty() throws RemoteException;
	
	/**
	 * Get the list of users (custom class). Each user contain name and associated IRemoteUser object.
	 * @return
	 * @throws RemoteException
	 */
	public ArrayList<Users> getUserList() throws RemoteException;
	/**
	 * Check if the user name is already existed in the white board.
	 * @param info
	 * @throws RemoteException
	 */
	public void checkUsername(String[] info) throws RemoteException;
	/**
	 * Update the current users.
	 * @throws RemoteException
	 */
	public void updateUser() throws RemoteException;
	/**
	 * Remove a certain user.
	 * @param username
	 * @param isManager
	 * @throws RemoteException
	 */
	public void removeUser(String username, boolean isManager) throws RemoteException;
	/**
	 * Send a message to all current users in chat room.
	 * @param string
	 * @throws RemoteException
	 */
	public void sendMessageToUsers(String string) throws RemoteException;
	
	/**
	 * Seek approvals from manager and give permissions to chat.
	 * @param info
	 * @throws RemoteException
	 */
	public void enterBoard(String[] info) throws RemoteException;
	/**
	 * Check if the IP, port have already been occupied by others.
	 * @param details
	 * @throws RemoteException
	 */
	public void checkEmpty(String[] details) throws RemoteException;
	/**
	 * Let a user exit the current board by manager.
	 * @param username
	 * @throws RemoteException
	 */
	public void exit(String username) throws RemoteException;
	public void close() throws RemoteException;
	
	/**
	 * Seek the boolean approval about user's request to enter. 
	 * @param userName
	 * @return
	 * @throws RemoteException
	 */
	public boolean seekApproval(String userName) throws RemoteException;
	/**
	 * Get the boolean approval about user's request to enter. 
	 * @return
	 * @throws RemoteException
	 */
	public boolean getApproval() throws RemoteException;
	/**
	 * Set the boolean approval about user's request to enter. 
	 * @param b
	 * @throws RemoteException
	 */
	public void setApproval(boolean b) throws RemoteException;
	/**
	 * Get the boolean to indicate current user if has entered.
	 * @return
	 * @throws RemoteException
	 */
	public boolean getCheckUser() throws RemoteException;
	/**
	 * Set the boolean isExit to indicate current system if has exited. 
	 * @param b
	 * @throws RemoteException
	 */
	public void setExit(boolean b)throws RemoteException;
	/**
	 * Get the boolean isExit to indicate current system if has exited. 
	 * @param b
	 * @throws RemoteException
	 */
	public boolean getExit()throws RemoteException;
}