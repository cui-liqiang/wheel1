package util;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ClassPathUtil {
    public static List<String> getClassNamesInPackage(String packageName) throws Exception {
        String packagePath = packageName.replace(".", "/");
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource(packagePath);

        if(url == null) throw new Exception("package name \"" + packageName + "\" doesn't seem to be exist");

        String path = url.getPath();
        String baseClassPath = path.substring(0, path.indexOf(packagePath));

        return getClassNameInDir(new File(path), baseClassPath);
    }

    private static List<String> getClassNameInDir(File dir, String baseClassPath) {
        List<String> names = new ArrayList<String>();
        for (File file  : dir.listFiles()) {
            if(file.isDirectory()) {
                names.addAll(getClassNameInDir(file, baseClassPath));
            } else {
                String filePath = file.getAbsolutePath();
                names.add(filePath.substring(0, filePath.indexOf(".class")).substring(baseClassPath.length()).replace("/", "."));
            }
        }
        return names;
    }
}
