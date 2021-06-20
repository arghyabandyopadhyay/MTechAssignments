//Sender class code
import java.net.*;
import java.io.*;

public class SenderClass extends Thread {

    private DatagramSocket datagramSocket;

    int server_port;
    InetAddress clientAddress = InetAddress.getLocalHost();

    String inputFile;

    public SenderClass(int port, String fileName, int serverPort) throws IOException {
        datagramSocket = new DatagramSocket(port);
        datagramSocket.setSoTimeout(8000);
        server_port = serverPort;

        inputFile = fileName;
    }

    public void run() {
        System.out.println("\nSender thread started.");

        byte buf[] = null;

        int byteRead;
        int cnt = 0;
        buf = new byte[65000];

        try {
            sleep(3000);
            int packets_sent = 0;
            int ack_recv = 0;
            int packets_req = 0;
            int failed_tries = 0;

            // Calculate file name and file size
            File f = new File(inputFile);
            long fileSize = f.length();
            String fileInfo = f.getName() + "," + fileSize;

            DatagramPacket DpSend;
            System.out.println("File Info: " + fileInfo);
            packets_req = packets_req + 1;
            while (true) {
                // Send fileInfo
                System.out.println("Sending info...");
                buf = fileInfo.getBytes();
                DpSend = new DatagramPacket(buf, buf.length, clientAddress, server_port);
                datagramSocket.send(DpSend);
                packets_sent = packets_sent + 1;

                // sleep(1);

                try {
                    // Receive ACK from Server
                    byte[] receive = new byte[65000];
                    DatagramPacket DpReceive = new DatagramPacket(receive, receive.length);
                    datagramSocket.receive(DpReceive);
                    System.out.println("Got " + data(receive));

                    ack_recv = ack_recv + 1;
                    sleep(10);
                    break;
                } catch (Exception e) {
                    System.out.println("ACK not received. Re-sending...");
                    if (failed_tries == 10) {
                        System.out.println("\nTimeout occurred after 10 reties. Aborting.");
                        break;
                    }
                    failed_tries = failed_tries + 1;
                }

            }
            if (failed_tries == 10)
                return;
            System.out.println("File Info Sent.");

            // Open input file for reading contents
            InputStream inputStream = new FileInputStream(inputFile);

            System.out.println("\nSending file contents...");
            buf = new byte[65000];
            while ((byteRead = inputStream.read()) != -1) {

                buf[cnt % 65000] = (byte) byteRead;
                if ((cnt + 1) % 65000 == 0) {
                    // Send packet until ACK is received or failed tries == 10
                    packets_req = packets_req + 1;
                    failed_tries = 0;
                    while (true) {
                        // Send 65000 bytes to server
                        DpSend = new DatagramPacket(buf, buf.length, clientAddress, server_port);
                        datagramSocket.send(DpSend);
                        packets_sent = packets_sent + 1;

                        // sleep(1);

                        try {
                            // Receive ACK from Server
                            byte[] receive = new byte[65000];
                            DatagramPacket DpReceive = new DatagramPacket(receive, receive.length);
                            datagramSocket.receive(DpReceive);
                            System.out.println("Got " + data(receive));
                            ack_recv = ack_recv + 1;
                            sleep(10);
                            break;
                        } catch (Exception e) {
                            System.out.println("ACK not received. Re-sending...");
                            if (failed_tries == 10) {
                                System.out.println("\nTimeout occurred after 10 reties. Aborting.");
                                break;
                            }
                            failed_tries = failed_tries + 1;
                        }

                    }
                    if (failed_tries == 10) {
                        // System.out.println("\nTimeout occurred after 10 reties. Aborting.");
                        break;
                    }
                    buf = new byte[65000];
                    System.out.println("Bytes Sent: " + cnt);
                }
                cnt = cnt + 1;
            }
            // Send final buffer
            if (cnt != 0) {
                DpSend = new DatagramPacket(buf, (cnt % 65000) + 1, clientAddress, server_port);
                datagramSocket.send(DpSend);
                buf = new byte[cnt + 1];
                sleep(10);
                System.out.println("Final Bytes Sent: " + cnt);
            }
            inputStream.close();

            System.out.println("\nFor this file:");
            System.out.println("No. of packets req to send: " + packets_req);
            System.out.println("No. of packets sent: " + packets_sent);
            System.out.println("No. of ACK received: " + ack_recv);
            System.out.println("No. of packets lost: " + (packets_sent - packets_req));

            // Append packets info to log file
            String logFile = "log_" + f.getName() + ".txt";
            FileWriter f2 = new FileWriter(logFile, true);
            BufferedWriter b2 = new BufferedWriter(f2);
            PrintWriter p2 = new PrintWriter(b2);
            p2.println("\nNo. of packets req to send: " + packets_req);
            p2.println("No. of packets sent: " + packets_sent);
            p2.println("No. of ACK received: " + ack_recv);
            p2.println("No. of packets lost: " + (packets_sent - packets_req));

            p2.close();
            b2.close();
            f2.close();

            datagramSocket.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("\nTimeout occurred after many reties. Aborting.");
        }
        System.out.println("\nSender thread done!");
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