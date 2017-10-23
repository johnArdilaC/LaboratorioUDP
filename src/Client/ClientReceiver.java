package Client;

import javax.swing.*;

import Client.Object;
import Server.ClientInformation;
import Server.Server;

import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class ClientReceiver {

	private int port;
	private DatagramSocket datagramSocket;
	private ArrayList<String>[] infoTemporal;
	private static int ID_GENERATOR = 0;

	public ClientReceiver(int port) {
		this.port = port;
		this.infoTemporal = new ArrayList[100];
	}

	public void start() {

		try {
			DatagramSocket datagramSocket = new DatagramSocket(port);
			datagramSocket.setSendBufferSize(128000);
			byte[] buffer = new byte[200];
			DatagramPacket datagramReceived = new DatagramPacket(buffer, buffer.length);
			int n = 0;

			CompletableFuture.runAsync(() -> {
				while (true) {
					try {
						Thread.sleep(11000);
						writeRecords();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			while (true) {
				n++;
				datagramSocket.receive(datagramReceived);
				byte[] data = datagramReceived.getData();
				String message = new String(data, 0, datagramReceived.getLength());
				ByteArrayInputStream bais = new ByteArrayInputStream(data);
				ObjectInputStream ois = new ObjectInputStream(bais);
				Object object = (Object) ois.readObject();
				ZonedDateTime timeObject = ZonedDateTime.now(ZoneId.of("GMT"));
				int time = ZonedDateTime.parse(object.getTimeMark(), DateTimeFormatter.RFC_1123_DATE_TIME).getNano();
				insertRecord(timeObject, object, -1);

			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void insertRecord(ZonedDateTime arrivalTime, Object object, int idClient) throws IOException {
		String fileName = "client" + idClient + ".txt";
		int time = ZonedDateTime.parse(object.getTimeMark(), DateTimeFormatter.RFC_1123_DATE_TIME).getNano();
		int timeDifference = (arrivalTime.getNano() - time) / 1000000;
		String t = object.getSequenceNumer() + ": " + timeDifference + "ms\n";
		infoTemporal[idClient].add(t);
	}

	public void writeRecords() throws IOException {
		for (int i = 0; i < 100; i++) {
			if (infoTemporal[i] == null)
				return;
			String fileName = "client" + i + ".txt";
			FileWriter fileWriter = new FileWriter(fileName, true);
			for (int j = 0; j < infoTemporal[i].size(); j++) {
				fileWriter.write(infoTemporal[i].get(j));
			}
			infoTemporal[i].clear();
			fileWriter.close();
		}
	}

}
