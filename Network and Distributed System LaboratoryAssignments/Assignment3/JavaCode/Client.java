//Client class code
import java.io.*;
import java.util.*;

public class Client {

    public static void main(String[] args) throws Exception {
        int my_port = 56060;
        int server_port = 56070;
        try {
            System.out.println("This is Client.java");

            // List of my files
            File curDir = new File("./client_files");
            String fileList = getAllFiles(curDir);
            String files[] = fileList.split(",");

            File curDir2 = new File("./server_files");
            String fileList2 = getAllFiles(curDir2);

            PriorityQueue<String> pq = new PriorityQueue<String>(50, new FileComparator());

            // Prioritizing files
            System.out.println("\nPrioritizing files...\n");
            for (String f : files) {
                if (!fileList2.contains(f)) {
                    System.out.println("Server does not have " + f);
                    pq.add(f);
                } else {
                    if (!f.equalsIgnoreCase(""))
                        System.out.println("Server already has " + f);
                }
            }

            // Sending files
            System.out.println("\nNow begin sending...");
            while (!pq.isEmpty()) {
                String f = pq.poll();
                System.out.println("Sending: " + f);

                Thread t = new SenderClass(my_port, "./client_files/" + f, server_port);
                t.start();
                t.join();
                t.interrupt();
            }
            System.out.println("");
            // System.out.println("\nSending files from client done.");

            // Thread for receiving file
            Thread t2 = new ReceiverClass(my_port + 1, "client_files/", server_port + 1);
            t2.start();
        } catch (IOException e) {
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