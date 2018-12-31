package FileFilters;

import javax.swing.filechooser.FileFilter;
import java.io.File;

// 文件选择器的图片文件过滤器
public class ImageFileFilter extends FileFilter {

    @Override
    public boolean accept(File f) {
        if(f.isDirectory())
        {
            return true;
        }
        if(f.getName() != null)
        {
            return (f.getName().endsWith(".jpg"))
                    ||(f.getName().endsWith(".png"))
                    ||(f.getName().endsWith(".jpeg"))
                    ||(f.getName().endsWith(".gif"));
        }
        return false;
    }

    @Override
    public String getDescription() {
        return "图片文件(*.jpg,*.jpeg,*.gif,*.png)";
    }

}
