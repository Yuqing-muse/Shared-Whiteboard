/** 
 * name: Yuqing Chang
 * student number: 1044862
 * username: yuqchang
 */
package Manager;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import client.DrawEvents;
import remote.IRemoteBoard;

public class Drawing extends JPanel implements MouseListener, MouseMotionListener{

	private static final long serialVersionUID = 1L;
	private DrawEvents drawEvent;
	private Point begin;
	private Point end;
	private IRemoteBoard board;
	private BufferedImage image;
	private Color selectColor = Color.BLACK;
	//private Stroke stroke = new BasicStroke(1.0f);
    private static ArrayList<Point> points = new ArrayList<Point>();
    // Store all shapes of current canvas, which is used for repainting
    private static ArrayList<Shape> shapelist = new ArrayList<Shape>();
    
	/**
	 * Create the application.
	 */
	public Drawing(DrawEvents drawEvent) {
		this.drawEvent = drawEvent;
		setBackground(Color.WHITE);
        addMouseListener(this);
        addMouseMotionListener(this);
	}
	
	/**
	 * Set the board
	 * @param board
	 */
	public void setboard(IRemoteBoard board) {
    	this.board = board;
    }
	
	/**
	 * Get DrawEvents Object
	 * @return
	 */
	public DrawEvents getDrawEvent() {
		return this.drawEvent;
	}
	
	/**
	 * Set current color
	 * @param color
	 */
	public void setColor(Color color){
		this.selectColor = color;
	}
	
	/**
	 * Clear all shapes on the canvas
	 */
	public void clear() {
	    shapelist = new ArrayList<Shape>();
	    image = null;
	}
	
	/**
	 * Save the image of board
	 * @return
	 */
    public BufferedImage save() {    	
    	Dimension imagesize = this.getSize();
		BufferedImage image = new BufferedImage(imagesize.width,imagesize.height,BufferedImage.TYPE_INT_BGR);	
		Graphics2D graphics = image.createGraphics();
        this.paint(graphics);
        graphics.dispose();
        return image;
    }
    
    /**
     * Load current board image
     * @param image
     */
    public void load(BufferedImage image) {
    	clear();
    	repaint();
    	this.image = image;
    }
    
    /**
     * Define the basic function of painting
     */
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        if(image != null) {
        	g.drawImage(image, 0, 0, this);
        }
        for (int i = 0; i < shapelist.size(); i++) {
            if (shapelist.get(i) == null) {break;}
                shapelist.get(i).repaint(g);
        }
        this.repaint();
    }
			       
	 
    /**
     * Draw shapes based on the instruction.
     * Add current shape in the shape list
     * @param begin
     * @param end
     * @param type
     */
    public void draw(Point begin, Point end,String instruction) {
        Graphics2D g = (Graphics2D)getGraphics();
        System.out.println(instruction);
        g.setColor(selectColor);
        if(instruction.equals("Line")) {
            shapelist.add(new Shape(g,begin,end,instruction,selectColor));
            g.drawLine(begin.x,begin.y, end.x, end.y);
        }
        else {
            switch (instruction) {
            case "Circle":
            	shapelist.add(new Shape(g,begin,end,instruction,selectColor));
            	circle(begin,end,g);
            	break;
            case "Rectangle":
            	shapelist.add(new Shape(g,begin,end,instruction,selectColor));
            	rectangle(begin,end,g);
            	break;
            case "Pen":
            	ArrayList<Point> p = new ArrayList<Point>(1000);
            	p.addAll(points);
                shapelist.add(new Shape(g,p,instruction,selectColor));
                break;
            case "Eraser":
            	ArrayList<Point> p1 = new ArrayList<Point>(1000);
                p1.addAll(points);
                g.setStroke(new BasicStroke(2.0f));
                shapelist.add(new Shape(g,p1,instruction,Color.white));
                break;
            case "Oval":
            	shapelist.add(new Shape(g,begin,end,instruction,selectColor));
                oval(begin,end,g);
                break;
            }
            // Clear points list for draw continue shape
            points.clear();
        }
    }

    /**
     * Updata current board to all users
     */
    public void updateBoard() {
		try {	        
			BufferedImage image = save();
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			ImageIO.write(image,"png", output);
			byte[] transfer = output.toByteArray();
			board.draw(transfer);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "There is a Remote Exception", "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			System.exit(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}        
	}
	

	@Override
	public void mouseDragged(MouseEvent e) {
		if(board != null) {
			String clickedName = drawEvent.getObjectName();
			Point p = e.getPoint();
            Point current = null;
            Graphics2D g = (Graphics2D)getGraphics();
            g.setColor(selectColor);
            if(clickedName.equals("Pen")){
            	/*
            	 * Set end point
            	 */
                if(points.size()!=0)
                	current = points.get(points.size()-1);
                else
                	current = begin;
                g.drawLine(current.x,current.y,p.x,p.y);
                points.add(p);
            }
            else if(clickedName.equals("Eraser")){
            	g.setStroke(new BasicStroke(2.0f));
                if(points.size()!=0)
                	current = points.get(points.size()-1);
                else
                    current = begin;
                
                /*
                 * Set the pen color to white
                 */
                Color c = new Color(selectColor.getRGB());
                g.setColor(Color.WHITE);
                g.drawLine(current.x,current.y,p.x,p.y);
                points.add(p);
                g.setColor(c);
            }
            else {
                if (clickedName.equals("Line")) {
                    g.drawLine(begin.x, begin.y, p.x,p.y);
                }
                else {
                    if (clickedName.equals("Rectangle")) {
                    	rectangle(begin,p,g);
                    }
                    if (clickedName.equals("Circle")) {
                    	circle(begin,end,g);
                    }
                    if(clickedName.equals("Oval")) {
                    	oval(begin,end,g);
                    }
                }
                repaint();
            }
		}
    }

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		end = e.getPoint();
	}

	
	@Override
	public void mouseClicked(MouseEvent e){
		// TODO Auto-generated method stub
	}

	/**
	 * Get a position and input a text
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		begin = e.getPoint();
		// Get the name of operation
		String clickedName = drawEvent.getObjectName();
		if(board != null && clickedName.equals("Text")){
			Graphics2D g = (Graphics2D)getGraphics();
			String input;
			input = JOptionPane.showInputDialog("Please input your text!");
			if(input != null) {
				g.setColor(selectColor);
				g.drawString(input,begin.x,begin.y);
				shapelist.add(new Shape(g,begin,input,clickedName,selectColor));
				updateBoard();
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
            String clickedName = drawEvent.getObjectName();
            if(board != null) {
            	Point p = e.getPoint();
                draw(begin, p, clickedName);
                updateBoard();
        	}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Draw a rectangle
	 * @param newPoint1
	 * @param newPoint2
	 * @param g
	 */
	public void rectangle(Point newPoint1, Point newPoint2,Graphics2D g){
		begin = newPoint1;
		end = newPoint2;
		Dimension rect = null;
		rect = new Dimension();
		int height = Math.abs(end.y - begin.y);
        int width = Math.abs(end.x - begin.x);
        int x = Math.min(begin.x, end.x);
        int y = Math.min(begin.y, end.y);
		g.drawRect(x,y, width, height);
	}
	
	/**
	 * Draw a circle
	 * @param newPoint1
	 * @param newPoint2
	 * @param g
	 */
	public void circle(Point newPoint1, Point newPoint2,Graphics2D g){
		int height = Math.abs(end.y - begin.y);
        int width = Math.abs(end.x - begin.x);
		int radius = Math.max(width, height);
		Point center = new Point((begin.x-end.x)/2, (begin.y-end.y)/2);
        g.drawOval(center.x, center.y, radius, radius);
	}	
	
	/**
	 * Draw oval
	 * @param newPoint1
	 * @param newPoint2
	 * @param g
	 */
	public void oval(Point newPoint1, Point newPoint2,Graphics2D g){
		int height = Math.abs(end.y - begin.y);
        int width = Math.abs(end.x - begin.x);
        g.drawOval(Math.min(begin.x, end.x),Math.min(begin.y, end.y), width, height);
	}
}
