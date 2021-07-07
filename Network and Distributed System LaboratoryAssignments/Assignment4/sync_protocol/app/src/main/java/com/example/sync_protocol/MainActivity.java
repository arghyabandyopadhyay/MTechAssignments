//MainActivity.java
package com.example.sync_protocol;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.FileInputStream;

import javax.activation.MimetypesFileTypeMap;

import fi.iki.elonen.NanoHTTPD;

import static com.example.sync_protocol.Ip.ipadd;

public class MainActivity extends AppCompatActivity {
    private WebServer server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String i = "";
        i = ipadd();
        TextView deviceIp = findViewById(R.id.deviceIp);
        TextView serv_ip = findViewById(R.id.ip_add);
        deviceIp.setText("Device IP " + i);

        final Button button;
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    System.out.println(InetAddress.getLocalHost().getHostAddress());
                    server = new WebServer();
                    try {
                        server.start();
                    } catch(IOException ioe) {
                        Log.w("Httpd", "The Server could not start.");
                    }
                    Log.w("Httpd", "Web Server Initialized");
                    Main.invoke(serv_ip.getText().toString());
                    Toast.makeText(MainActivity.this,"Transfer Compelete", Toast.LENGTH_LONG);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(server != null) {
            server.stop();
        }
    }

    public class WebServer extends NanoHTTPD {
        public WebServer() {
            super(8080);
        }

        @Override
        public Response serve(String uri, Method method, Map<String, String> header,
                              Map<String, String> parameters, Map<String, String> files) {

            File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/test/");

            Map<Integer, List<String>> prio = new HashMap<>();
            List<String> list1 = new ArrayList<>();
            List<String> list2 = new ArrayList<>();
            List<String> list3 = new ArrayList<>();
            List<String> list4 = new ArrayList<>();
            List<String> list5 = new ArrayList<>();

            for(File file: folder.listFiles()) {
                if(GetFileExtension.getFileExtension(file).equals("txt")) {
                    list1.add(file.getName());
                    prio.put(new Integer(1), list1);
                }
                if(GetFileExtension.getFileExtension(file).equals("pdf")) {
                    list2.add(file.getName());
                    prio.put(new Integer(2), list2);
                }
                if(GetFileExtension.getFileExtension(file).equals("jpg") || GetFileExtension.getFileExtension(file).equals("png")) {
                    list3.add(file.getName());
                    prio.put(new Integer(3), list3);
                }
                if(GetFileExtension.getFileExtension(file).equals("mp3")) {
                    list4.add(file.getName());
                    prio.put(new Integer(4), list4);
                }
                if(GetFileExtension.getFileExtension(file).equals("mp4")) {
                    list5.add(file.getName());
                    prio.put(new Integer(5), list5);
                }
            }

            String fileName = "";

            if(uri.equals("/")) {
                System.out.println(uri);

                String st = "";
                String x = "";

                for(Map.Entry<Integer, List<String>> en : prio.entrySet()) {
                    for(String obj : en.getValue()) {
                        x = obj;
                        st = st + "<a href=\"/get?name=" + x + "\">" + x + "</a>";
                        st = st + "<br>";
                    }
                }

                return newFixedLengthResponse(Response.Status.OK, MIME_HTML, st);
            } else if(uri.equals("/get")) {
                FileInputStream fis = null;
                File f = null;
                try {
                    f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/test/" + parameters.get("name"));
                    System.out.println("GET : " + f);
                    fis = new FileInputStream(f);
                    System.out.println("GET FIS : " + fis);
                } catch(FileNotFoundException e) {
                    e.printStackTrace();
                }

                MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();

                String mimeType = mimeTypesMap.getContentType(fileName);
                return newChunkedResponse(Response.Status.OK,mimeType, fis);
            } else {
                return newFixedLengthResponse("404 File Not Found");
            }
        }
    }
}