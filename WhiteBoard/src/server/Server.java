/** 
 * name: Yuqing Chang
 * student number: 1044862
 * username: yuqchang
 */
package server;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import remote.IRemoteBoard;

public class Server {
	public static void main(String args[]) {
		try {
			IRemoteBoard wb = new RemoteDraw();
			int port = Integer.parseInt(args[0]);
            Registry registry = LocateRegistry.createRegistry(port);
            registry.bind("Whiteboard", wb);            
            System.out.println("the port: "+ port + " \nserver ready");
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AlreadyBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
