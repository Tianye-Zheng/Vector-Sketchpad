package FileFilters;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class OffFileFilter extends FileFilter {

    @Override
    public boolean accept(File f) {
        if(f.isDirectory())
        {
            return true;
        }
        if(f.getName() != null)
        {
            return (f.getName().endsWith(".off"))
                    ||(f.getName().endsWith(".OFF"));
        }
        return false;
    }

    @Override
    public String getDescription() {
        return "off格式文件(*.off,*.OFF)";
    }
}
