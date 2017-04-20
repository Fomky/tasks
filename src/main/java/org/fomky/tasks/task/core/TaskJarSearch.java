package org.fomky.tasks.task.core;

import org.apache.log4j.Logger;
import org.fomky.tasks.task.entity.JarFiles;
import org.springframework.stereotype.Service;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Created by Fomky on 2017/4/1309:52.
 */
@Service
public class TaskJarSearch {
    private Logger logger = Logger.getLogger(TaskJarSearch.class);
    //缓存已经记载到ClassPath的 jar 文件
    public Set<String> cache = new HashSet<>();
    private Method addURL = initAddMethod();
    private static URLClassLoader classloader = (URLClassLoader) ClassLoader.getSystemClassLoader();

    private Method initAddMethod() {
        try {
            Method add = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            add.setAccessible(true);
            return add;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<JarFiles> files(String dir_path) {
        List<JarFiles> filesList = new ArrayList<>();
        File dir = new File(dir_path);
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles(pathname -> pathname.getName().endsWith(".jar"));
            if (files != null) {
                for (File file : files) {
                    JarFiles file_jar = new JarFiles(file);
                    //加载到ClassPath
                    loadJarToClassPath(file);
                    filesList.add(file_jar);
                }
            }
        }
        return filesList;
    }

    public void loadJarToClassPath(File file) {
        try {
            if (!cache.contains(file.getName())){
                addURL.invoke(classloader, file.toURI().toURL());
                cache.add(file.getName());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

}
