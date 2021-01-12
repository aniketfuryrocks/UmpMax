/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author TimeTraveller
 */
public class PlayListHandle {
    String location = System.getenv("LOCALAPPDATA") + "/UmpMax";

    public void checkDatabase() {
        try {
            File SystemLocation = new File(location);
            if (!SystemLocation.exists()) {
                SystemLocation.mkdir();

            }
            File PlayListFolder = new File(location + "/PlayLists");
            if (!PlayListFolder.exists()) {
                PlayListFolder.mkdir();
            }
        } catch (Exception ex) {

        }
    }

    public Boolean createNewPlayList(String Name) {
        Boolean Status = true;
        try {
            File PlayListFolder = new File(location + "/PlayLists/" + Name);
            if (!PlayListFolder.exists()) {
                PlayListFolder.mkdir();
            } else {
                Status = false;
            }
        } catch (Exception ex) {

        }
        return Status;

    }

    public String createSubPlayList(String PlayName, String SubName) {
        String Status = null;
        try {
            File PlayListFolder = new File(location + "/PlayLists/" + PlayName);
            if (!PlayListFolder.exists()) {
                Status = "PlayList DoesNot Exists";
            } else {
                Status = "PlayList Exists";
            }
            if (Status.equals("PlayList Exists")) {
                File SubListFolder = new File(location + "/PlayLists/" + PlayName + "/" + SubName);
                if (!SubListFolder.exists()) {
                    Status = "SubPlayList Successfully created";
                    SubListFolder.mkdir();
                } else {
                    Status = "SubList Already Exists";
                }
            }
        } catch (Exception ex) {

        }
        return Status;
    }


    public void WriteToSubPlayList(String PlayName, String SubName, String Song) throws IOException {
        try {
            File PlayListFolder = new File(location + "/PlayLists/" + PlayName + "/" + SubName);
            int i = PlayListFolder.listFiles().length + 1;
            File ToStore = new File(location + "/PlayLists/" + PlayName + "/" + SubName + "/" + "Song" + i + ".ump");
            FileWriter FW = new FileWriter(ToStore);
            FW.write(Song);
            FW.close();
        } catch (Exception ex) {

        }

    }

    public List<String> getPlayLists() {
        List<String> PlayLists = new ArrayList();
        try {
            File PlayListFolder = new File(location + "/PlayLists");
            File[] AllFiles = PlayListFolder.listFiles();


            for (int i = 0; i < AllFiles.length; i++) {

                File file = AllFiles[i];
                if (file.isDirectory()) {
                    PlayLists.add(file.getName());
                }
            }
        } catch (Exception ex) {

        }
        return PlayLists;
    }

    public List<String> getSubPlayLists(String PlayList) {

        List<String> PlayLists = new ArrayList();
        try {
            File PlayListFolder = new File(location + "/PlayLists/" + PlayList);
            File[] AllFiles = PlayListFolder.listFiles();

            for (int i = 0; i < AllFiles.length; i++) {

                File file = AllFiles[i];
                if (file.isDirectory()) {
                    PlayLists.add(file.getName());
                }
            }
        } catch (Exception ex) {

        }
        return PlayLists;
    }

    public List<String> getSubPlayListSongs(String PlayName, String SubName) {
        List<String> Songs = new ArrayList();
        try {
            File PlayListFolder = new File(location + "/PlayLists/" + PlayName + "/" + SubName);
            File[] AllFiles = PlayListFolder.listFiles();

            for (int i = 0; i < AllFiles.length; i++) {


                File file = AllFiles[i];
                String Name = file.getName();
                int Length = Name.length();
                int dot = Length - 3;
                String Exten = Name.substring(dot, Length);
                if (Exten.equals("ump")) {
                    FileReader FR = null;

                    FR = new FileReader(file);
                    BufferedReader BR = new BufferedReader(FR);
                    String Location = BR.readLine();
                    Songs.add(Location);
                    FR.close();
                    BR.close();

                }
            }

        } catch (Exception ex) {
        }
        return Songs;
    }

    public void DeletePlayList(String Play) {


        File ZPlay = new File(location + "/PlayLists/" + Play);
        File[] AllFiles = ZPlay.listFiles();
        if (AllFiles.length > 0) {
            for (int i = 0; i < AllFiles.length; i++) {
                AllFiles[i].delete();
            }
        }
        ZPlay.delete();

    }

    public void DeleteSubPlayList(String Play, String SubPlay) {

        File SubFileFiles = new File(location + "/PlayLists/" + Play + "/" + SubPlay);
        File[] file = SubFileFiles.listFiles();
        if (file == null) {
            SubFileFiles.delete();
        } else {
            for (int i = 0; i < file.length; i++) {
                file[i].delete();
            }
            SubFileFiles.delete();
        }

    }

    public void DeleteSubSong(String Play, String SubPlay, String Song) {


        try {
            File PlayListFolder = new File(location + "/PlayLists/" + Play + "/" + SubPlay);
            File[] AllFiles = PlayListFolder.listFiles();

            for (int i = 0; i < AllFiles.length; i++) {


                File file = AllFiles[i];
                String Name = file.getName();
                int Length = Name.length();
                int dot = Length - 3;
                String Exten = Name.substring(dot, Length);
                if (Exten.equals("ump")) {
                    FileReader FR = new FileReader(file);
                    BufferedReader BR = new BufferedReader(FR);
                    String Location = BR.readLine();
                    FR.close();
                    BR.close();
                    if (Location.equals(Song)) {
                        file.delete();
                    }

                }
            }

        } catch (Exception ex) {
        }

    }

    public List<String> getPlayListSongs(String PlayName) {
        List<String> Songs = new ArrayList();
        try {
            File PlayListFolder = new File(location + "/PlayLists/" + PlayName);
            File[] AllFiles = PlayListFolder.listFiles();

            for (int i = 0; i < AllFiles.length; i++) {
                File file = AllFiles[i];
                String Name = file.getName();
                int Length = Name.length();
                int dot = Length - 3;
                String Exten = Name.substring(dot, Length);
                if (Exten.equals("ump")) {
                    FileReader FR = null;

                    FR = new FileReader(file);
                    BufferedReader BR = new BufferedReader(FR);
                    String Location = BR.readLine();
                    Songs.add(Location);
                    FR.close();
                    BR.close();

                }
            }

        } catch (Exception ex) {
        }
        return Songs;
    }

    public void WriteToPlayList(String PlayName, String Song) throws IOException {
        try {
            File PlayListFolder = new File(location + "/PlayLists/" + PlayName);
            int i = PlayListFolder.listFiles().length + 1;
            File ToStore = new File(location + "/PlayLists/" + PlayName + "/" + "Song" + i + ".ump");
            FileWriter FW = new FileWriter(ToStore);
            FW.write(Song);
            FW.close();
        } catch (Exception ex) {

        }

    }

    public void DeletePlayListSong(String Play, String Song) {


        try {
            File PlayListFolder = new File(location + "/PlayLists/" + Play);
            File[] AllFiles = PlayListFolder.listFiles();

            for (int i = 0; i < AllFiles.length; i++) {
                File file = AllFiles[i];
                String Name = file.getName();
                int Length = Name.length();
                int dot = Length - 3;
                String Exten = Name.substring(dot, Length);
                if (Exten.equals("ump")) {
                    FileReader FR = new FileReader(file);
                    BufferedReader BR = new BufferedReader(FR);
                    String Location = BR.readLine();
                    FR.close();
                    BR.close();
                    if (Location.equals(Song)) {
                        file.delete();
                    }

                }
            }

        } catch (Exception ex) {
            System.out.println("ex " + ex);
        }

    }


}
