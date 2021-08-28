/** 
 * name: Yuqing Chang
 * student number: 1044862
 * username: yuqchang
 */
package Manager;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;

public class Shape {
    private Point begin = null;
    private Point end = null;
    private String operationName;								//  Declare an operation name
    private Color c;
    private ArrayList<Point> points;
    private String input;
    //private Stroke stroke = new BasicStroke(1.0f);
    /*
     * Define a shape, which needs begin and end points, color and graphic.
     * This constructor is designed for basic shape
     */
    public Shape(Graphics g, Point begin, Point end, String instruction, Color c)
    {
        this.begin = begin;
        this.end = end;
        this.operationName = instruction;
        this.c = c;
    }

    /*
     * Define a shape, which needs begin and end points, color and graphic.
     * This constructor is designed for pen, eraser
     */
    public Shape(Graphics g, ArrayList<Point> points, String type, Color c)
    {
        this.points = points;
        this.operationName = type;
        this.c = c;
    }

    /*
     * Define a shape, which needs begin and end points, color and graphic.
     * This constructor is designed for pen, eraser
     */
    public Shape(Graphics g, Point begin, String in, String type, Color color)
    {
    	this.begin = begin;
        this.input = in;
        this.operationName = type;
        this.c =color;
    }

    public void repaint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        if(operationName.equals("Text")){
        	g2.setColor(c);
            g2.drawString(input,begin.x,begin.y);
        }
        else {
            g2.setColor(c);
            switch (operationName) {
	            case "Line":
	            	g2.draw(new Line2D.Double(begin.x, begin.y, end.x, end.y));
	            	break;
	            case "Circle":
	            	int radius = (int) Math.round(begin.distance(end));
					Point center = new Point(Math.min(begin.x, end.x), Math.min(begin.y, end.y));
	            	g.drawOval(center.x, center.y, 2*radius, 2*radius);
	            	break;
	            case "Rectangle":
	            	g.drawRect(Math.min(begin.x, end.x), Math.min(begin.y, end.y), Math.abs(begin.x - end.x), Math.abs(begin.y - end.y));
	            	break;
	            case "Pen":
	            	int i = 0;
			        while (i < points.size() - 1) {
			            Point currentPoint = points.get(i);
			            Point nextPoint = points.get(i + 1);
		
			            if (nextPoint.x != -1 && nextPoint.y != -1) {
			                RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
			                        RenderingHints.VALUE_ANTIALIAS_ON);
			                g2.setRenderingHints(rh);
			                g2.drawLine(currentPoint.x, currentPoint.y, nextPoint.x, nextPoint.y);
			                i++;
			            } else {
			                i += 2;
			            }
			        }
	            	break;
	            	
	            case "Eraser":
	            	g2.setStroke(new BasicStroke(2.0f));
	            	for (int i1 = 1; i1 < points.size(); i1++) {
	                    g.drawLine(points.get(i1 - 1).x, points.get(i1 - 1).y, points.get(i1).x, points.get(i1).y);
	                }
	            	break;
	            case "Oval":
	            	g.drawOval(Math.min(begin.x,end.x), Math.min(begin.y, end.y), Math.abs(begin.x-end.x), Math.abs(begin.y - end.y));
	            	break;
            }
        }
    }

    /**
     * Get the operation name
     * @return
     */
    public String getType(){
        return operationName;
    }
}