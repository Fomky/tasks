package org.fomky.tasks.task.entity;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.jar.JarFile;

/**
 * @author Created by Fomky on 2017/4/1309:53.
 */
public class JarFiles {
    private String fileName;
    private String path;
    private List<String> class_names;

    public JarFiles() {
    }

    public JarFiles(File file) {
        this.fileName = file.getName();
        this.path = file.getPath();
        try {
            JarFile jar = new JarFile(file);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<String> getClass_names() {
        return class_names;
    }

    public void setClass_names(List<String> class_names) {
        this.class_names = class_names;
    }
}
