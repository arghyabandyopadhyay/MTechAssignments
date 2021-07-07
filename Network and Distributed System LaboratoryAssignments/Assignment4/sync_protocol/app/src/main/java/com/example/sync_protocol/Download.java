// Download.java
package com.example.sync_protocol;

import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class Download implements Runnable {
    String link;
    File out;

    public Download(String link, File out) {
        this.link = link;
        this.out = out;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override

    public void run() {
        try {
            URL url = new URL(link);
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            BufferedInputStream in = new BufferedInputStream(http.getInputStream());
            FileOutputStream fos;

            if(out.exists()) {
                fos = new FileOutputStream(out, true);
            } else {
                fos = new FileOutputStream(out);
            }

            in.skip(out.length());

            BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
            byte[] buffer = new byte[1024];
            double downloaded = 0.0;
            int read = 0;
            double percentDownloaded = 0.0;
            while((read = in.read(buffer, 0, 1024)) >= 0) {
                bout.write(buffer, 0, read);
                downloaded += read;
            }

            bout.close();
            in.close();
            System.out.println("Work Done");

        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }
}
