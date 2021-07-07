package com.example.sync_protocol;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

public class Main {
    public static void invoke(String ip) throws IOException {
        //String ip = "10.0.2.16";
        String urlString = "http://"+ip+":8080";

        //Collection A = new ArrayList();
        //Collection B = new ArrayList();

        ArrayList<String> A = new ArrayList();
        ArrayList<String> B = new ArrayList();

        String fileName = "";
        String fileURL;
        String saveDir;
        String name = Environment.getExternalStorageDirectory().getAbsolutePath() + "/test/";
        File folder = new File(name);
        System.out.println(name);

        for(File file : Objects.requireNonNull(folder.listFiles())) {
            B.add(file.getName());
        }

        System.out.println("Files in My System\n"+B);

        URL url = new URL(urlString);
        Log.println(Log.INFO, String.valueOf(Log.INFO), String.valueOf(url) );

        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        Log.println(Log.INFO, String.valueOf(Log.INFO), String.valueOf(reader) );

        String line;
        line = reader.readLine();
        Log.println(Log.INFO, String.valueOf(Log.INFO), String.valueOf(line) );

        int i;
        for(i = 0; i < line.length(); i++) {
            if(line.charAt(i) =='e' && line.charAt(i+1) == '=') {
                i = i + 2;
                while(line.charAt(i) != '"') {
                    fileName += line.charAt(i);
                    i++;
                }
                A.add(fileName);
                fileName = "";
            }
        }
        System.out.println("Files in Server\n"+A);

        ArrayList<String> diff = new ArrayList(A);
        diff.removeAll(B);
        System.out.println("File To Be Taken from Server\n"+diff);
        Iterator it = diff.iterator();
        while(it.hasNext()) {
            String x = (String)it.next();
            fileURL = "http://"+ip+":8080/get?name="+x;
            saveDir = Environment.getExternalStorageDirectory().getAbsolutePath()+"/test/"+x;
            File out = new File(saveDir);
            new Thread(new Download(fileURL, out)).start();
        }
        reader.close();
    }
}