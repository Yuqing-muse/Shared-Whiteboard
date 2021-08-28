/** 
 * name: Yuqing Chang
 * student number: 1044862
 * username: yuqchang
 */
package remote;
import javax.imageio.ImageIO;
import javax.imageio.stream.MemoryCacheImageInputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;

public class ShapeImage implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final int CANVAS_WIDTH = 600;
	private static final int CANVAS_HEIGHT = 600;
	private BufferedImage image = null;

    public ShapeImage() {
        super();
    }

    public ShapeImage(BufferedImage bufferedImage) {
    	super();
        this.image = bufferedImage;
    }
    
    public BufferedImage getImage() {
        return image;
    }

    
    public void setImage(BufferedImage image) {
        this.image = image;
    }
    
    
    public void remove() {
    	System.out.println("canvas is removed");
    	image = new BufferedImage(CANVAS_WIDTH, CANVAS_HEIGHT, BufferedImage.TYPE_INT_ARGB);
    }
}
