//Receiver Class Code
import java.net.*;
import java.io.*;
import java.util.*;
public class ReceiverClass extends Thread {
    private DatagramSocket datagramSocket;
    private byte[] receive = new byte[65000];
    private DatagramPacket DpReceive = null;
    private String myDir;
    private int client_port;

    public ReceiverClass(int port, String my_dir, int c_port) throws IOException {
        datagramSocket = new DatagramSocket(port);
        datagramSocket.setSoTimeout(20000);
        myDir = my_dir;
        client_port = c_port;
    }

    public void run() {
        System.out.println("\nReceiver thread listening...");

        String outputFile;
        String logFile;
        String[] fileInfo;
        String startTime, endTime;
        int len;

        try {
            while (true) {
                int packets_recv = 0;
                int ack_sent = 0;

                // Receive fileInfo
                DpReceive = new DatagramPacket(receive, receive.length);
                datagramSocket.receive(DpReceive);
                fileInfo = data(receive).toString().split(",");
                receive = new byte[65000];
                packets_recv = packets_recv + 1;
                sleep(10);
                System.out.println("File Info Recv.");
                System.out.println("File Info: " + fileInfo[0] + "," + fileInfo[1]);

                // Send ACK to client
                InetAddress clientAddress = InetAddress.getLocalHost();
                byte[] buf = ("--ACK--").getBytes();
                DatagramPacket DpSend = new DatagramPacket(buf, buf.length, clientAddress, client_port);
                datagramSocket.send(DpSend);
                ack_sent = ack_sent + 1;
                sleep(2);

                startTime = java.time.LocalDateTime.now().toString();

                // Create log file stream
                logFile = "log_" + fileInfo[0] + ".txt";
                OutputStream logStream = new FileOutputStream(logFile);

                // Create output file stream
                outputFile = myDir + fileInfo[0];
                OutputStream outputStream = new FileOutputStream(outputFile);

                int cnt = 0;
                len = Integer.parseInt(fileInfo[1]);

                logStream.write("\n------------".getBytes());
                logStream.write(("\n" + outputFile).getBytes());
                logStream.write(("\nPriority: " + FileComparator.getPriority(outputFile)).getBytes());
                logStream.write(("\nStart Time: " + startTime).getBytes());
                logStream.write(("\n" + fileInfo[0]).getBytes());

                System.out.println("\nReceiving file contents...");
                double perc = 0.0;
                perc = (double) (cnt / len) * 100.0;
                System.out.println("Progress: " + perc);
                while (cnt <= len) {
                    // Receive 65000 bytes from client
                    DpReceive = new DatagramPacket(receive, receive.length);
                    datagramSocket.receive(DpReceive);
                    packets_recv = packets_recv + 1;

                    // Write to output file
                    outputStream.write(receive);
                    receive = new byte[65000];
                    sleep(2);

                    cnt = cnt + 65000;

                    // Send ACK to client
                    buf = ("ACK#" + (int) (cnt / 65000)).getBytes();
                    DpSend = new DatagramPacket(buf, buf.length, clientAddress, client_port);
                    datagramSocket.send(DpSend);
                    ack_sent = ack_sent + 1;

                    // Display progress
                    perc = (double) ((cnt * 100) / len);
                    if (perc > 100)
                        perc = 100;
                    System.out.println("Progress: " + perc + " Packet #" + (int) (cnt / 65000));

                    // Write to log file
                    logStream.write(("\nProgress: " + perc + " Packet #" + (int) (cnt / 65000)).getBytes());
                }

                endTime = java.time.LocalDateTime.now().toString();
                logStream.write(("\nEnd Time: " + endTime).getBytes());
                logStream.write(("\nNo. of packets recv: " + packets_recv).getBytes());
                logStream.write(("\nNo. of ACK sent: " + ack_sent).getBytes());
                logStream.write(("\n*Note: 1 packet is for file info.").getBytes());
                logStream.write("\n------------".getBytes());

                logStream.close();
                outputStream.close();

                System.out.println("\nFile: " + fileInfo[0] + " received.");
                System.out.println("\nFor this file:");
                System.out.println("No. of packets recv: " + packets_recv);
                System.out.println("No. of ACK sent: " + ack_sent);
            }

        } catch (Exception e) {
            // e.printStackTrace();
            System.out.println("\nClosing receiver due to in-activity.");
        }
        System.out.println("\nReceiver done.\n");
    }

    public static StringBuilder data(byte[] a) {
        if (a == null)
            return null;
        StringBuilder ret = new StringBuilder();
        int i = 0;
        while (a[i] != 0) {
            ret.append((char) a[i]);
            i++;
        }
        return ret;
    }
}