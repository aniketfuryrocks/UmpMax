package org.umpmax;

import javafx.stage.DirectoryChooser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DirectoryHandle {

    final static String location;
    static ArrayList<File> e = new ArrayList<File>();
    static File f = null;

    static {
        String AppData = System.getenv("LOCALAPPDATA");
        if (AppData == null)
            location = "./UmpMax";
        else
            location = AppData + "/UmpMax";
    }

    public static void WriteToDirectory() throws Exception {
        File file = new File(location + "/Directories");
        int number = file.listFiles().length;
        File DirectoryLocations = new File(location + "/Directories/Location" + number + ".ump");
        DirectoryLocations.createNewFile();
        FileWriter fw = new FileWriter(DirectoryLocations);
        System.out.println("Appending");
        fw.write(f.toPath().toString());
        fw.close();
    }

    public static void RemoveDirectory(String filepath) throws Exception {
        File file = new File(location + "/Directories");
        File[] AllFiles = file.listFiles();
        for (File allFile : AllFiles) {
            if (allFile.getName().endsWith(".ump")) {
                FileReader fr = new FileReader(allFile);
                BufferedReader br = new BufferedReader(fr);
                String read = br.readLine();
                read.trim();
                filepath.trim();
                br.close();
                fr.close();
                if (read.equals(filepath)) {
                    allFile.delete();
                    break;
                }
            }
        }
    }

    public static void ClearLocation() {
        File SystemLocation = new File(location);
        if (!SystemLocation.exists())
            SystemLocation.mkdir();

        File DirectoryFolder = new File(location + "/Directories");
        if (!DirectoryFolder.exists())
            DirectoryFolder.mkdir();

        File file = new File(location + "/Directories");
        File[] All = file.listFiles();
        String ss = "nope";
        for (File value : All) {
            for (int yy = 0; yy < All.length; yy++) {
                String NAME = value.getName().substring(value.getName().length() - 3);
                if (NAME.equals("ump")) {
                    System.out.println(value);
                    System.out.println(value.getName());
                    e.add(value);
                    ss = "nope";
                    break;
                } else {
                    System.out.println("deleting");
                    ss = "delete";
                }
            }
            if (ss.equals("delete")) {
                value.delete();
            }
        }
    }

    public static void CheckData() throws Exception {
        File file = new File(location + "/Directories");
        File[] fileList = file.listFiles();
        for (File value : fileList) {
            try {
                FileReader fr = new FileReader(value);
                BufferedReader br = new BufferedReader(fr);
                String s = br.readLine();
                File e = new File(s);
                if (e.exists()) {
                    br.close();
                    fr.close();
                } else {
                    br.close();
                    fr.close();
                    System.out.println("Deleting");
                    value.delete();
                }
            } catch (Exception ex) {
                throw ex;
            }
        }
    }

    public static boolean checkDuplicate() throws Exception {
        File file = new File(location + "/Directories");
        File[] fileList = file.listFiles();
        ArrayList<File> lll = new ArrayList<File>();
        Collections.addAll(lll, fileList);
        boolean hi = false;
        for (int i = 0; i < lll.size(); i++) {
            try {
                FileReader fr = new FileReader(lll.get(i));
                BufferedReader br = new BufferedReader(fr);
                String s = br.readLine();
                br.close();
                fr.close();
                for (int y = 0; y < lll.size(); y++) {

                    FileReader fra = new FileReader(fileList[y]);
                    BufferedReader bra = new BufferedReader(fra);
                    String sa = bra.readLine();
                    bra.close();
                    fra.close();
                    if (s.equals(sa) && !fileList[i].getName().equals(fileList[y].getName())) {
                        System.out.println(fileList[i].getName() + "&" + fileList[y].getName() + " are duplicate in data");
                        System.out.println(fileList[i] + "is being deleted");
                        lll.get(y).delete();
                        lll.remove(y);
                        hi = true;
                        break;
                    }
                }
            } catch (Exception ex) {
                throw ex;
            }
        }
        return hi;
    }

    public void ChooseDirectory() throws Exception {
        File SystemLocation = new File(location);
        if (!SystemLocation.exists())
            SystemLocation.mkdir();

        File DirectoryFolder = new File(location + "/Directories");
        if (!DirectoryFolder.exists())
            DirectoryFolder.mkdir();

        DirectoryChooser Fc = new DirectoryChooser();
        Fc.setTitle("Choose Directory");
        f = Fc.showDialog(null);
        System.out.println(f.toString());
        WriteToDirectory();
        System.out.println("done");
    }

    public void getDirectories(List<File> Directories) throws Exception {
        File file = new File(location + "/Directories");
        //file.setReadOnly();
        //file.setWritable(false);
        File[] fileList = file.listFiles();
        Directories.clear();
        for (File value : fileList) {
            FileReader fr = new FileReader(value);
            BufferedReader br = new BufferedReader(fr);
            String s = br.readLine();
            File location = new File(s);
            Directories.add(location);
            br.close();
            fr.close();
        }
    }

    public void getStreams(List<File> Songs, List<File> Videos) throws Exception {
        ArrayList<File> files = new ArrayList<File>();
        getDirectories(files);
        ArrayList<File> d = new ArrayList<File>();
        for (File file : files) {
            File[] a = file.listFiles();
            Collections.addAll(d, a);
        }
        File[] AllFiles = new File[d.size()];
        for (int i = 0; i < d.size(); i++) {
            AllFiles[i] = d.get(i);
        }
        for (File allFile : AllFiles) {
            if (!allFile.isDirectory()) {
                String type = Files.probeContentType(allFile.toPath());
                System.out.println(type);
                if (type != null) {
                    if (type.substring(0, type.indexOf('/')).equals("audio") || type.equals("application/vnd.apple.mpegurl")) {
                        System.out.println(type + "\t : " + allFile.getName());
                        Songs.add(allFile);
                    }
                    if (type.substring(0, type.indexOf('/')).equals("video") || type.equals("application/vnd.apple.mpegurl")) {
                        System.out.println(type + "\t : " + allFile.getName());
                        Videos.add(allFile);
                    }
                }
            }
        }
    }
}