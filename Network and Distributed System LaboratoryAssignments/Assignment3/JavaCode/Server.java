//Server class code
import java.io.*;
import java.util.*;

public class Server {

    public static void main(String[] args) {
        int my_port = 56070;
        int client_port = 56060;
        try {
            System.out.println("This is Server.java");

            // Thread for receiving file
            Thread t = new ReceiverClass(my_port, "server_files/", client_port);
            t.start();

            // List of my files
            File curDir = new File("./server_files");
            String fileList = getAllFiles(curDir);
            String files[] = fileList.split(",");

            File curDir2 = new File("./client_files");
            String fileList2 = getAllFiles(curDir2);

            PriorityQueue<String> pq = new PriorityQueue<String>(50, new FileComparator());

            // Prioritizing files
            System.out.println("\nPrioritizing files...\n");
            for (String f : files) {
                if (!fileList2.contains(f)) {
                    System.out.println("Client does not have " + f);
                    pq.add(f);
                } else {
                    if (!f.equalsIgnoreCase(""))
                        System.out.println("Client already has " + f);
                }
            }

            // Sending files
            System.out.println("\nNow begin sending...");
            while (!pq.isEmpty()) {
                String f = pq.poll();
                System.out.println("Sending: " + f);

                Thread t2 = new SenderClass(my_port + 1, "./server_files/" + f, client_port + 1);
                t2.start();
                t2.join();
                t2.interrupt();
            }
            // System.out.println("\nSending files from server done.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getAllFiles(File curDir) {

        String files = "";

        File[] filesList = curDir.listFiles();
        for (File f : filesList) {
            if (f.isDirectory())
                getAllFiles(f);
            if (f.isFile()) {
                files = files + "," + f.getName();
                // System.out.println(f.getName());
            }
        }
        return files;
    }

}