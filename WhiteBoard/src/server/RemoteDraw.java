/** 
 * name: Yuqing Chang
 * student number: 1044862
 * username: yuqchang
 */
package server;

import java.awt.*;
import java.awt.Graphics;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import remote.IRemoteBoard;
import remote.IRemoteUser;
import remote.ShapeImage;
/**
 * Server side implementation of the remote interface.
 * Must extend UnicastRemoteObject, to allow the JVM to create a 
 * remote proxy/stub.
 *
 */
public class RemoteDraw extends UnicastRemoteObject implements IRemoteBoard  {
	
	private static final long serialVersionUID = 1L;
	private ShapeImage board;
	// Final variable of the size of the board
	private static final int CANVAS_WIDTH = 600;
	private static final int CANVAS_HEIGHT = 600;
	private ArrayList<Users> users;
	private byte[] transferImage;								// The object to transfer between client and server
	public boolean isExit = false;								// Boolean to indicate current system if has exited. 
	public boolean approval;									// Boolean to indicate approval about user's request to enter. 
	private boolean checkUser = true;							// Boolean to indicate current user if has entered.
    
	/**
	 * Constructor of RemoteDraw class
	 * @throws RemoteException
	 */
	public RemoteDraw() throws RemoteException {
    	super();
    	BufferedImage image = new BufferedImage(CANVAS_WIDTH, CANVAS_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        board = new ShapeImage(image);
        users = new ArrayList<Users>();
    }
    
    /**
     * Get the current board.
     */
    public ShapeImage getBoard() throws RemoteException {
        return board;
    }
    
    
    /**
     * @param image image for canvas
     */
    public void setImage(ShapeImage image) throws RemoteException {
        board = image;
    }
    
    @Override
	public void draw(byte[] image) throws RemoteException {
		this.transferImage = image;
		for(Users c : users){
			try {
				c.getClient().transfer(image);
			} 
			catch (RemoteException e) {
				e.printStackTrace();
			}
		}	
	}
    
	@Override
    public void line(int x1, int y1, int x2, int y2) throws RemoteException {
        Graphics g = board.getImage().getGraphics();
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLACK);
        g2.draw(new Line2D.Double(x1, y1, x2, y2));
    }
	@Override
	public void circle(int x, int y, int rectwidth, int rectheight) throws RemoteException{
		Graphics g = board.getImage().getGraphics();
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLACK);
        g2.drawOval(x, y, rectwidth, rectheight);
	}
	@Override
	public void text(String msg, int x, int y) throws RemoteException {
		Graphics g = board.getImage().getGraphics();
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Purisa", Font.PLAIN, 13));
        g2.drawString(msg, x, y);
	}
	@Override
	public void rectangle(int x, int y, int rectwidth, int rectheight) throws RemoteException {
		Graphics g = board.getImage().getGraphics();
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLACK);
        g2.draw(new Rectangle2D.Double(x, y,
                rectwidth,
                rectheight));
	}
	
	/**
	 * Remove all points on the board.
	 */
    public void removeAll() throws RemoteException {
        board.remove();
    }
	
	/**
	 * Check if current user list is empty.
	 */
	public boolean isEmpty() throws RemoteException{
		if(users.size() == 0) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
     * Get the list of users (custom class). Each user contain name and associated IRemoteUser object.
     */
    public ArrayList<Users> getUserList() throws RemoteException{
    	return this.users;
    }
    
    /**
	 * Check if the user name is already existed in the white board.
	 * @param info
	 * @throws RemoteException
	 */
	public void checkUsername(String[] info) throws RemoteException{
		try {
			IRemoteUser user = (IRemoteUser)Naming.lookup("rmi://" + info[1] + "/" + info[2]);
			for(int i =0;i < users.size();i++) {
				if (users.get(i).getName().equals(info[0])) {
					user.errorGUI("Your username is already existed. Please try another.\n");
					this.checkUser = false;
					
				}
			}
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Update the current users.
	 * @throws RemoteException
	 */
	public void updateUser() throws RemoteException {
		String[] updateUsers = new String[users.size()];
		
		for(int i = 0; i< updateUsers.length; i++){
			updateUsers[i] = users.get(i).getName();
		}
		for(Users u : users){
			try {
				u.getClient().addUsers(updateUsers);
			} 
			catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Remove a certain user.
	 * @param username
	 * @param isManager
	 * @throws RemoteException
	 */
	@Override
	public void removeUser(String username, boolean isManager) throws RemoteException {
		int index = 0;
		for(int i = 0;i < users.size();i++) {
			if(users.get(i).getName().equals(username)) {
				index = i;
			}
		}
		if (index == 0)
			JOptionPane.showMessageDialog(null, "Sorry, we can't find this user", "Information", JOptionPane.INFORMATION_MESSAGE);
		else {
			IRemoteUser client = users.get(index).getClient();
			users.remove(index);
			sendMessageToUsers("Server says :" + username + " has left the draw board.\n");
			updateUser();
			if(isManager == true) {
				// Only manager have the right to remove user
				// Create a new thread to avoid influence main thread
				Thread t = new Thread(()->{
					try {
						client.errorGUI("Sorry, the manager kick you out the board.\n");
					} catch (RemoteException e) {
						e.printStackTrace();
					}
					});
					t.start();
			}
		}
	}
	
	/**
	 * Check if the IP, port have already been occupied by others.
	 * @param details
	 * @throws RemoteException
	 */
	@Override
	public void checkEmpty(String[] info) throws RemoteException {
		if(users.size() > 0) {
			try {
				IRemoteUser nextClient = (IRemoteUser)Naming.lookup("rmi://" + info[1] + "/" + info[2]); //serverIPAdress,port
				nextClient.errorGUI("This room has already been created. Please change another room.\n");
				//System.exit(0);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NotBoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	
	
	/**
	 * Let a user exit the current board by manager.
	 * @param username
	 * @throws RemoteException
	 */
	@Override
	public void exit(String username) throws RemoteException{
		for(int i =0;i < users.size();i++) {
			if (users.get(i).getName().equals(username)) {
				users.remove(i);
			}
		}
		sendMessageToUsers("Server says :" + username + " has left the draw board.\n");
		updateUser();
	}
	
	/**
	 * Send a message to all current users in chat room.
	 */
	@Override
	public void sendMessageToUsers(String str) throws RemoteException {
		for(Users u : users){
			   try {
			    u.getClient().recieveMsg(str + "\n");
			   } 
			   catch (RemoteException e) {
			    e.printStackTrace();
			   }
		  } 
	}

	
	/**
	 * Seek approvals from manager and give permissions to chat.
	 * @param info
	 * @throws RemoteException
	 */
	@Override
	public synchronized void enterBoard(String[] info) throws RemoteException {
		try {
			IRemoteUser nextClient = (IRemoteUser)Naming.lookup("rmi://" + info[1] + "/" + info[2]);
			
			// Manager don't approve the user's entering
			if(users.size() >= 1 && (seekApproval(info[0])==false)) {
				isExit = true;
				nextClient.errorGUI("Sorry, manager don't give your approval. You cannot enter.\n");
				return;
			}
			users.add(new Users(info[0], nextClient));
			if(transferImage != null) {//synchronize the last board with new user
				nextClient.transfer(transferImage);
			}
			nextClient.recieveMsg("Server says: Hello " + info[0] + " , please type in this chat room.\n");			
			sendMessageToUsers("Server says: " + info[0] + " has entered.\n");			
			updateUser();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void close() throws RemoteException {
		// TODO Auto-generated method stub
		System.exit(0);
	}
	
	/**
	 * Set the boolean isExit to indicate current system if has exited. 
	 * @param b
	 * @throws RemoteException
	 */
	public void setExit(boolean b) throws RemoteException{
		isExit = b;
	}

	/**
	 * Get the boolean isExit to indicate current system if has exited. 
	 * @param b
	 * @throws RemoteException
	 */
	public boolean getExit() throws RemoteException {
		return this.isExit;
	}
	
	/**
	 * Seek the boolean approval about user's request to enter. 
	 * @param userName
	 * @return
	 * @throws RemoteException
	 */
	public boolean seekApproval(String userName) throws RemoteException {
		int app = JOptionPane.showConfirmDialog(null,"Hi, manager. "+ userName + " want to enter this board. Would you agree?","Approval", JOptionPane.YES_NO_OPTION);
		if (app == 0) {
			approval = true;
			
		}
		else
			approval = false;
		return approval;
	}
	
	/**
	 * Get the boolean approval about user's request to enter. 
	 * @return
	 * @throws RemoteException
	 */
	public boolean getApproval() throws RemoteException {
		return this.approval;
	}
	
	/**
	 * Set the boolean approval about user's request to enter. 
	 * @param b
	 * @throws RemoteException
	 */
	public void setApproval(boolean b) throws RemoteException {
		this.approval = b;
	}
	
	/**
	 * Get the boolean to indicate current user if has entered.
	 * @return
	 * @throws RemoteException
	 */
	public boolean getCheckUser() throws RemoteException {
		return this.checkUser;
	}

}
