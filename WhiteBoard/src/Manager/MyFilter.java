/** 
 * name: Yuqing Chang
 * student number: 1044862
 * username: yuqchang
 */
package Manager;

import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.*;

/**
 * 
 * @author zys91
 *
 */
public class MyFilter extends FileFilter {
	 public final static String jpeg = "jpeg";
    public final static String jpg = "jpg";
    public final static String gif = "gif";
    public final static String tiff = "tiff";
    public final static String tif = "tif";
    public final static String png = "png";

    /**
     * Get the extension of a file.
     * @param f
     * @return
     */
    public static String getExtension(File f) {
        String extension = null;
        String name = f.getName();
        int i = name.lastIndexOf('.');

        if (i > 0 &&  i < name.length() - 1) {
        	extension = name.substring(i+1).toLowerCase();
        }
        return extension;
    }

    /**
     * Files are presented by image icons if path is not empty.
     * @param path
     * @return
     */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = MyFilter.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
    
    /**
     * Define the type of files that can be accepted, such as gif, jpg, tiff, or png files.
     */
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }
        String extension = getExtension(f);
        if (extension != null) {
            if (extension.equals(tiff) || extension.equals(tif) || extension.equals(gif) ||
                extension.equals(jpeg) || extension.equals(jpg) || extension.equals(png)) {
                    return true;
            } else {
                return false;
            }
        }

        return false;
    }

    /**
     * Return description.
     */
    public String getDescription() {
        return "Images";
    }
}

