/** 
 * name: Yuqing Chang
 * student number: 1044862
 * username: yuqchang
 */
package client;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.rmi.RemoteException;

import remote.IRemoteBoard;
import remote.ShapeImage;
/**
 * Implement user's request to paint on the board
 *
 */
public class DrawEvents {
	private Point begin;
	private Point end;
	private static IRemoteBoard remoteBoard;
	private String operateObjectName = "";
	public DrawEvents() {}
	
	/**
	 * Clear all points.
	 */
	public void clear() {
	    begin = null;
	    end = null;
	}
	
	/**
	 * Set the board
	 * @param remoteBoard
	 */
	public void setRemoteCanvas(IRemoteBoard remoteBoard) {
	    DrawEvents.remoteBoard = remoteBoard;
	}
	
	/**
	 * Get the board
	 * @return
	 */
	public IRemoteBoard getCanvas() {
		return remoteBoard;
	}
	
	/**
	 * Get the name of operation, such as "Line".
	 * @return
	 */
    public String getObjectName() {
		return operateObjectName;
    }
	
    /**
     * Set the name of operation, such as "Line".
     * @param operateObjectName
     */
    public void setObjectName(String operateObjectName) {
		this.operateObjectName = operateObjectName;
    }
    
    /**
     * Clear all board.
     */
	public void clearCanvas() {
    	try {
    		remoteBoard.removeAll();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	        
	 }
	 
	/**
	 * Set the BufferedImage to be transferred.
	 * @param image
	 */
	public void setImage(BufferedImage image) {
        try {
        	remoteBoard.setImage(new ShapeImage(image));
        } catch (RemoteException e) {
        	e.printStackTrace();
        }
        clear();
    }
	
	/**
	 * Use basic drawing operation on RMI agency to draw lines.
	 * @param newPoint1
	 * @param newPoint2
	 */
	public void drawLine(Point newPoint1, Point newPoint2){
		begin = newPoint1;
		end = newPoint2;
		try {
			remoteBoard.line(begin.x,begin.y,end.x,end.y);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	/**
	 * Use basic drawing operation on RMI agency to draw circles.
	 * @param newPoint1
	 * @param newPoint2
	 */
	public void drawCircle(Point newPoint1, Point newPoint2){
		begin = newPoint1;
		end = newPoint2;
		int radius = (int) Math.round(begin.distance(end));
		Point center = new Point((begin.x-end.x)/2, (begin.y-end.y)/2);
		try {
			remoteBoard.circle(center.x, center.y, 2*radius, 2*radius);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.clear();
	}
	
	/**
	 * Use basic drawing operation on RMI agency to input texts.
	 * @param msg
	 * @param p
	 */
	public void drawText(String msg, Point p){
		try {
			remoteBoard.text(msg, p.x, p.y);
        } catch (RemoteException e) {
            System.out.println("Error to draw to remote.");
        }
	}
	
	/**
	 * Use basic drawing operation on RMI agency to draw rectangles.
	 * @param newPoint1
	 * @param newPoint2
	 */
	public void rectangle(Point newPoint1, Point newPoint2){
		begin = newPoint1;
		end = newPoint2;
		Dimension rect = null;
		rect = new Dimension();
		int height = Math.abs(end.y - begin.y);
        int width = Math.abs(end.x - begin.x);
        int x = Math.min(begin.x, end.x);
        int y = Math.min(begin.y, end.y);
		try {
			remoteBoard.rectangle(x, y, width, height);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
} 
	
