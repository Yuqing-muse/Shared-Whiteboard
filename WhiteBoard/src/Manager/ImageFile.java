/** 
 * name: Yuqing Chang
 * student number: 1044862
 * username: yuqchang
 */
package Manager;

import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.*;

public class ImageFile extends FileView {
    ImageIcon jpgIcon = MyFilter.createImageIcon("images/jpgIcon.gif");
    ImageIcon gifIcon = MyFilter.createImageIcon("images/gifIcon.gif");
    ImageIcon tiffIcon = MyFilter.createImageIcon("images/tiffIcon.gif");
    ImageIcon pngIcon = MyFilter.createImageIcon("images/pngIcon.png");

    public String getName(File f) {
        return null; 
    }

    public String getDescription(File f) {
        return null;
    }

    public Boolean isTraversable(File f) {
        return null; 
    }

    public String getTypeDescription(File f) {
        String extension = MyFilter.getExtension(f);
        String type = null;

        if (extension != null) {
            if (extension.equals(MyFilter.jpeg) || extension.equals(MyFilter.jpg)) {
                type = "JPEG Image";
            } else if (extension.equals(MyFilter.gif)){
                type = "GIF Image";
            } else if (extension.equals(MyFilter.tiff) || extension.equals(MyFilter.tif)) {
                type = "TIFF Image";
            } else if (extension.equals(MyFilter.png)){
                type = "PNG Image";
            }
        }
        return type;
    }

    public Icon getIcon(File f) {
        String extension = MyFilter.getExtension(f);
        Icon icon = null;

        if (extension != null) {
            if (extension.equals(MyFilter.jpeg) || extension.equals(MyFilter.jpg)) {
                icon = jpgIcon;
            } else if (extension.equals(MyFilter.gif)) {
                icon = gifIcon;
            } else if (extension.equals(MyFilter.tiff) || extension.equals(MyFilter.tif)) {
                icon = tiffIcon;
            } else if (extension.equals(MyFilter.png)) {
                icon = pngIcon;
            }
        }
        return icon;
    }
}
