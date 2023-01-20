package chat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.nio.Buffer;
import java.util.Scanner;

public class GroupChat {

    public volatile static boolean isFinished = false;

    public static void main(String[] args) throws IOException {
        int port = 5727;
        InetAddress inetAddress = InetAddress.getByName("224.0.0.1");
        MulticastSocket multicastSocket = new MulticastSocket(port);
        multicastSocket.setTimeToLive(0);
        multicastSocket.joinGroup(inetAddress);

        Thread thread = new Thread(new ReadThread(multicastSocket,inetAddress,port));
        thread.start();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String message = scanner.nextLine();

            if (message.equals("exit")) {
                multicastSocket.leaveGroup(inetAddress);
                multicastSocket.close();
                isFinished = true;
                break;
            }
            byte[] buffer = message.getBytes();
            DatagramPacket datagramPacket = new DatagramPacket(buffer,buffer.length,inetAddress,port);
            multicastSocket.send(datagramPacket);
        }
    }
}
