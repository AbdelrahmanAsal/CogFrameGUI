import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.Timestamp;
import java.util.Date;

import javax.swing.event.TableModelEvent;

public class ProgramExecutor extends Thread {
		String fileName;
		int id = 1;
		boolean finished;
		UITerminalHandler terminalHandler;

		public ProgramExecutor(String f, UITerminalHandler terminalHandler) {
			fileName = f;
			finished = false;
			this.terminalHandler = terminalHandler;
		}

		public void stopThread() {
			finished = true;
		}

		public void run() {
			try {
				ProcessBuilder builder = new ProcessBuilder("/bin/bash");
				builder.redirectErrorStream(true);
				Process process = builder.start();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(process.getInputStream()));
				BufferedWriter writer = new BufferedWriter(
						new OutputStreamWriter(process.getOutputStream()));

				writer.write("sudo /usr/local/click/bin/click " + fileName
						+ "\n");
				writer.flush();
				String line = reader.readLine();
				while (!finished && line != null) {
					System.out.println("Stdout : " + line);
					terminalHandler.parseCommand(id++, line);
					line = reader.readLine();
				}
				writer.close();
				reader.close();
				process.destroy();
			} catch (Exception e) {

			}
		}
	}
