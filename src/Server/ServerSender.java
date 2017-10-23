package Server;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import Client.Object;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

import javax.swing.JOptionPane;

public class ServerSender {

	private int port;
	private String ipNumber;
	private int portNumber;
	private int bufferSize;
	private int objectsNumber;
	private DatagramSocket socket;
	private Object object;

	public ServerSender(int port, String ipNumber, int portNumber, int bufferSize, int objectsNumber) {
		this.port = port;
		this.ipNumber = ipNumber;
		this.portNumber = portNumber;
		this.bufferSize = bufferSize;
		this.objectsNumber = objectsNumber;
	}

	public void start() {

		try {
			socket = new DatagramSocket(port);
			socket.setSendBufferSize(bufferSize);
			InetAddress ipServer = InetAddress.getByName(ipNumber);

			for (int i = 0; i < objectsNumber; i++) {
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream(6400);
				ObjectOutputStream oos = new ObjectOutputStream(outputStream);

				object = new Object(i, LocalDateTime.now().atZone(ZoneId.of("GMT")).format(DateTimeFormatter.RFC_1123_DATE_TIME),
									objectsNumber);
				oos.writeObject(object);

				byte[] dataByte = outputStream.toByteArray();

				DatagramPacket datagram = new DatagramPacket(dataByte, dataByte.length, ipServer, portNumber);
				socket.send(datagram);

				try {
					if (i % 600 == 0) {
						Thread.sleep(80);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			JOptionPane.showMessageDialog(null, "the objects have been sended", "OK ", JOptionPane.INFORMATION_MESSAGE);

		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
