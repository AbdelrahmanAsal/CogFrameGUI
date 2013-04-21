import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Enumeration;

public class Server extends Thread {
	ServerSocket serverSocket;
	BufferedReader in;
	PrintWriter out;
	public String nodeName, modulePath;
	ProgramExecutor exec;

	public Server() {
	}

	public void run() {
		while (true) {
			ServerSocket serverSocket = null;
			try {
				serverSocket = new ServerSocket(7);
				Socket socket = null;
				System.out.println("Waiting for connection....");
				socket = serverSocket.accept();

				System.out.println("Connection successful");
				System.out.println("Waiting for input");

				out = new PrintWriter(socket.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(socket
						.getInputStream()));
				socket.getInputStream();
				socket.getInetAddress().getHostAddress();
				String inputLine = in.readLine();
				handleClient(inputLine);
				in.close();
				socket.close();
				serverSocket.close();
			} catch (Exception e) {

			}
		}
	}

	private void handleClient(String command) throws Exception {
		command = command.toLowerCase();
		System.out.println(command);
		if (command.equals("get information")) {
			getCardsInformation();
		} else if (command.equals("get statistics")) {
			getStatsFile();
		} else if (command.equals("post configuration")) {
			postFile("Configuration.txt");
		} else if (command.equals("post module")) {
			postFile(modulePath + "ModuleFile.txt");
		} else if (command.equals("start")) {
			exec.start();
		}
	}

	private void getCardsInformation() throws Exception {
		out.println(nodeName);
		Enumeration<NetworkInterface> interfaces = NetworkInterface
				.getNetworkInterfaces();
		ArrayList<String> wlsMac = new ArrayList<String>();
		while (interfaces.hasMoreElements()) {
			NetworkInterface n = interfaces.nextElement();
			System.out.println(n.getName());
			byte[] mac = n.getHardwareAddress();
			if (mac != null) {
				StringBuffer macAddress = new StringBuffer();
				for (int i = 0; i < mac.length; i++) {
					macAddress.append(String.format("%02X%s", mac[i],
							(i < mac.length - 1) ? ":" : ""));
				}
				System.out.println(macAddress.toString());
				if (n.getName().equals("eth0"))
					out.println(macAddress.toString());
				else
					wlsMac.add(macAddress.toString());
			}
		}
		out.println(wlsMac.size());
		for (String s : wlsMac)
			out.println(s);
		out.close();
	}

	private void getStatsFile() throws Exception {
		BufferedReader r = new BufferedReader(new FileReader("stats_"
				+ nodeName + ".txt"));
		String str;
		while ((str = r.readLine()) != null) {
			out.println(str);
		}
		r.close();
	}

	private void postFile(String fileName) throws Exception {
		PrintWriter fileWriter = new PrintWriter(fileName);
		String str = "";
		while ((str = in.readLine()) != null) {
			fileWriter.println(str);
		}
		fileWriter.close();
	}
}