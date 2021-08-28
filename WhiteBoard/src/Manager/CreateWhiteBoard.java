/** 
 * name: Yuqing Chang
 * student number: 1044862
 * username: yuqchang
 */
package Manager;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import remote.IRemoteBoard;
import remote.IRemoteUser;

public class CreateWhiteBoard extends UnicastRemoteObject implements IRemoteUser{
	 /**
	 * 
	 */
	private static final long serialVersionUID = 3440268422538546113L;
	private String manager = null;					// manager name
	protected BoardGUI GUI;  						// Object of GUI
	private boolean isManager = true;				// Boolean to check if current user is manager
	protected IRemoteBoard canvas;					
	protected String userName;
	protected String hostName;
	protected String projectName;
	protected String identity;						// Declare current user's identity, user or manager
    /**
     * list of users' names
     */
    private List<String> allUserNames;

    /**
     * Main function to create a white board for manager.
     * @param args
     */
    public static void main(String [] args) {
    	CreateWhiteBoard bo;
		try {
			if(args.length == 2) {
				JOptionPane.showMessageDialog(null, "Please ensure you input the user name.", "error", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}
			bo = new CreateWhiteBoard(args[2],args[0],args[1]);
			bo.connect();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
    
    /**
	 * Constructor of CreateWhiteBoard class
	 * @throws RemoteException
	 */
    public CreateWhiteBoard(String username,String IP,String port) throws RemoteException {
    	allUserNames = new ArrayList<>();
    	this.userName = username.trim();
    	this.hostName  = IP + ":" + port; 
		this.projectName = "Whiteboard";
		this.identity = "Manager";
		this.GUI = new BoardGUI(isManager,userName);
    }
    
    /**
	 * Get a list of current user names.
	 * @return list of users' names
	 */
    public List<String> getUserNames() throws RemoteException {
        return allUserNames;
    }

    /**
     * Get the manager name.
     * @param managerName whiteboard's manager's name
     */
    public String getManagerName() throws RemoteException{
        return manager;
    }

    /**
     * Set the manager name.
     * @param managerName whiteboard's manager's name
     */
    public void setManagerName(String managerName) throws RemoteException{
        this.manager = managerName;
    }

    public void connect() {
		try {
			// Parse arguments
			String[] info = new String[3];
			info[0] = userName;
			info[1] = hostName;
			info[2] = identity;
			// Bind to RMI
			Naming.rebind("rmi://" + hostName + "/" + identity, this);
			// Obtain the current canvas in RMI
			canvas = (IRemoteBoard) Naming.lookup("rmi://" + hostName + "/" + projectName);
			canvas.checkEmpty(info);
			canvas.checkUsername(info);
			canvas.enterBoard(info);
			createGUI();
		} catch (RemoteException| NotBoundException e1) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "Fail to connect", "Error", JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
			System.exit(0);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /**
   	 * Transfer and load the previous canvas to all users.
   	 * @param i
   	 * @throws RemoteException
   	 */
    public void transfer(byte[] i) throws RemoteException {
		try {
			ByteArrayInputStream input = new ByteArrayInputStream(i);
			BufferedImage image = ImageIO.read(input);
			GUI.getDraw().load(image);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
    /**
     * Establish GUI interface for common user.
     */
    public void createGUI() {
    	GUI.setBoard(canvas);
		GUI.setUsername(userName);
		GUI.getDraw().setboard(canvas);
    }
    
    /**
	 * Add a new user name in current user names' list.
	 * @param userName
	 * @throws RemoteException
	 */
    public void addUser(String userName) throws RemoteException{
			allUserNames.add(userName);
    }
    
    /**
	 * Add a list of user names in current user names' list.
	 * @param userNames
	 * @throws RemoteException
	 */
    @SuppressWarnings("unchecked")
	public void addUsers(String[] userNames) throws RemoteException{
    	for (int i = 0; i < userNames.length; i++) {
    		allUserNames.add(userNames[i]);
    		
    	}
    	GUI.getJlist().setListData(userNames);
    }
    
    
    /**
	 * Show message on the GUI.
	 * @param msg
	 * @throws RemoteException
	 */
	public void recieveMsg(String msg) throws RemoteException {
		GUI.getMsgGUI().append(msg);
	}

	/**
	 * When an error occurs, an error alert box pops up to interact with the user.
	 * @param str
	 * @throws RemoteException
	 */
	public void errorGUI(String str) throws RemoteException {
		// TODO Auto-generated method stub
		JOptionPane.showMessageDialog(null, str, "A Error!", JOptionPane.ERROR_MESSAGE);
		//System.exit(0);
	}
}
