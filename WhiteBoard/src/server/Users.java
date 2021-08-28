/** 
 * name: Yuqing Chang
 * student number: 1044862
 * username: yuqchang
 */
package server;
import remote.IRemoteUser;
/**
 * Define a custom structure "Users"
 * It contains user name and remote client object.
 * @author zys91
 *
 */
public class Users {
	public String name;
	public IRemoteUser client;
	
	/**
	 * Constructor of Users class
	 * @param name
	 * @param client
	 */
	public Users(String name, IRemoteUser client){
		this.name = name;
		this.client = client;
	}	
	
	/**
	 * Get name of the user.
	 * @return
	 */
	public String getName(){
		return name;
	}
	/**
	 * Set name of the user.
	 * @return
	 */
	public IRemoteUser getClient(){
		return client;
	}
}
