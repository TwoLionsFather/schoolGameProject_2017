package com.JanTlk.BesseresHearthstone.secondTry;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class FileController
{
    private static HashMap<String, File> ALL_IMPORTED_FILES = new HashMap<String, File>(15);
    private static ArrayList<String> FILEPATHS = new ArrayList<String>();

    private FileController() {	}

    public static void addFileImport(String filename, File file)
    {
        ALL_IMPORTED_FILES.put(filename, file);
    }

    public static ArrayList<String> getFileNames()
    {
        return new ArrayList<String>(ALL_IMPORTED_FILES.keySet());
    }

    public static File getFile(String filename)
    {
        return ALL_IMPORTED_FILES.get(filename);
    }

    public static void addFilePath(String path)
    {
        FILEPATHS.add(path);
    }

    public static ArrayList<String> getFilePathList()
    {
        return FILEPATHS;
    }
}
