import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class MainUI extends JFrame {
	TerminalOutputTable t;
	JTable table, putable;
	JScrollPane ps;
	final int[] colWidth = {60, 200, 520};
	String fileName;
	String query = "";
	JTabbedPane tabbedPane;
	HashMap<String,WirelessInterface> wifiCards;
	int defaultWidth = 220, defaultHeight = 40;
	Font defaultFont = new Font("MS Mincho", Font.BOLD, 15);
	int sentPackets, receivedPackets;
	JLabel packetsSentVal, packetsReceivedVal, primaryUserStateVal;
	ProgramExecutor exec;
	PrimaryUserTable puTableModel;
	
	public MainUI(String file) {
		wifiCards = new HashMap<String,WirelessInterface>();
		fileName = file;
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setDefaultLookAndFeelDecorated(true);
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				// Nimbus or GTK+
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
//			UIManager.setLookAndFeel("seaglasslookandfeel.SeaGlassLookAndFeel");
		} catch (Exception e) {
			
		}
		getContentPane().setLayout(null);

		JLabel filterLabel = new JLabel("Filter:");
		filterLabel.setBounds(20, 10, 50, 30);
		filterLabel.setFont(defaultFont);
		getContentPane().add(filterLabel);

		final JTextField searchField = new JTextField();
		searchField.setBounds((int)(filterLabel.getX() + filterLabel.getWidth() + 10), 10, 250, 30);
		searchField.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent k) {
					query = searchField.getText();
					t.current.clear();
					for(TerminalTableRowEntry r: t.all)
						if(r.line.toLowerCase().contains(query.toLowerCase()))
							t.current.add(r);
					repaint();
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		getContentPane().add(searchField);
		
		
		JButton applyButton = new JButton("Apply");
		applyButton.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				query = searchField.getText();
				t.current.clear();
				for(TerminalTableRowEntry r: t.all)
					if(r.line.toLowerCase().contains(query.toLowerCase()))
						t.current.add(r);
				repaint();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}
			
		});
		
		applyButton.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent k) {
				if(k.getKeyCode() == KeyEvent.VK_ENTER) {
					query = searchField.getText();
					t.current.clear();
					for(TerminalTableRowEntry r: t.all)
						if(r.line.toLowerCase().contains(query.toLowerCase()))
							t.current.add(r);
					repaint();
				}
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		applyButton.setBounds((int)(searchField.getX() + searchField.getWidth() + 10), 10, 80, 30);
		getContentPane().add(applyButton);
		
		JButton clearButton = new JButton("Clear");
		clearButton.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				query = "";
				searchField.setText("");
				t.current.clear();
				t.current.addAll(t.all);
				repaint();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		clearButton.setBounds((int)(applyButton.getX() + applyButton.getWidth() + 10), 10, 80, 30);
		getContentPane().add(clearButton);
		
		JButton stopButton = new JButton("Stop");
		stopButton.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				exec.stopThread();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
			}
		});
		stopButton.setBounds((int)(clearButton.getX() + clearButton.getWidth() + 10), 10, 80, 30);
		getContentPane().add(stopButton);
		
		t = new TerminalOutputTable();
		table = new JTable();
		table.setModel(t);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		for (int i = 0; i < colWidth.length; i++) {
			TableColumn tcol = table.getColumnModel().getColumn(i);
			tcol.setPreferredWidth(colWidth[i]);
			tcol.setCellRenderer(new TerminalTableCellRenderer());
		}

		JTableHeader header = table.getTableHeader();
		header.setUpdateTableInRealTime(true);
		header.addMouseListener(t.new ColumnListener(table));
		header.setReorderingAllowed(true);
		ps = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		ps.setBounds(10, 50, 800, 800);
		
//		JScrollBar sb = ps.getVerticalScrollBar();
//		boolean onBottom = (sb.getValue() == sb.getMaximum());
//		if(onBottom)
//			sb.setValue(sb.getMaximum());

		ps.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {  
	        public void adjustmentValueChanged(AdjustmentEvent e) {
	        	boolean onBottom = (e.getAdjustable().getValue() == e.getAdjustable().getMaximum());
	    		if(onBottom)
	    			e.getAdjustable().setValue(e.getAdjustable().getMaximum());  
	        }
	    });
		
		getContentPane().add(ps);
		
		tabbedPane = new JTabbedPane();
		tabbedPane.setBounds((int)(ps.getX() + ps.getWidth() + 10), 10, 680, 400);
		add(tabbedPane);


		String emptyLabelVal = "--";
		JLabel packetsReceived = new JLabel("No. of received packets:");
		packetsReceived.setBounds((int)(ps.getX() + ps.getWidth() + 10), (int)(tabbedPane.getY() + tabbedPane.getHeight() + 10), defaultWidth, defaultHeight);
		packetsReceived.setFont(defaultFont);
		getContentPane().add(packetsReceived);
		
		packetsReceivedVal = new JLabel(emptyLabelVal);
		packetsReceivedVal.setBounds((int)(packetsReceived.getX() + packetsReceived.getWidth() + 10), (int)(tabbedPane.getY() + tabbedPane.getHeight() + 10), defaultWidth, defaultHeight);
		packetsReceivedVal.setFont(defaultFont);
		getContentPane().add(packetsReceivedVal);
		
		JLabel packetsSent = new JLabel("No. of Sent packets:");
		packetsSent.setBounds((int)(ps.getX() + ps.getWidth() + 10), (int)(packetsReceived.getY() + packetsReceived.getHeight() + 10), defaultWidth, defaultHeight);
		packetsSent.setFont(defaultFont);
		getContentPane().add(packetsSent);
		
		packetsSentVal = new JLabel(emptyLabelVal);
		packetsSentVal.setBounds((int)(packetsSent.getX() + packetsSent.getWidth() + 10), packetsSent.getY(), defaultWidth, defaultHeight);
		packetsSentVal.setFont(defaultFont);
		getContentPane().add(packetsSentVal);
		
		JLabel primaryUserState = new JLabel("Primary User State");
		primaryUserState.setBounds((int)(ps.getX() + ps.getWidth() + 10), (int)(packetsSent.getY() + packetsSent.getHeight() + 10), defaultWidth, defaultHeight);
		primaryUserState.setFont(defaultFont);
		getContentPane().add(primaryUserState);
		
		puTableModel = new PrimaryUserTable();
		putable = new JTable();
		putable.setModel(puTableModel);
		putable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		for (int i = 0; i < 2; i++) {
			TableColumn tcol = putable.getColumnModel().getColumn(i);
			tcol.setPreferredWidth(150);
			tcol.setCellRenderer(new PrimaryUserTableCellRenderer());
		}

		JScrollPane puScrollPane = new JScrollPane(putable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		puScrollPane.setBounds((int)(ps.getX() + ps.getWidth() + 10),(int)(primaryUserState.getY()+primaryUserState.getHeight()), 300, 200);
		getContentPane().add(puScrollPane);
		
		exec = new ProgramExecutor(fileName);
		exec.start();
	}
	
	JPanel createPane(String s, WirelessInterface inter) {
		JPanel p = new JPanel();
		p.setLayout(null);
		int defaultWidth = 220, defaultHeight = 40;
		Font defaultFont = new Font("MS Mincho", Font.BOLD, 15);

		JLabel channel = new JLabel("Channel:");
		channel.setBounds(10, 10, defaultWidth, defaultHeight);
		channel.setFont(defaultFont);
		p.add(channel);
		
		inter.channelVal.setBounds((int)(channel.getX() + channel.getWidth() + 10), channel.getY(), defaultWidth, defaultHeight);
		inter.channelVal.setFont(defaultFont);
		p.add(inter.channelVal);
		
		JLabel network = new JLabel("Network:");
		network.setBounds(10, (int)(channel.getY() + channel.getHeight() + 10), defaultWidth, defaultHeight);
		network.setFont(defaultFont);
		p.add(network);
		
		inter.networkVal.setBounds((int)(network.getX() + network.getWidth() + 10), network.getY(), defaultWidth, defaultHeight);
		inter.networkVal.setFont(defaultFont);
		p.add(inter.networkVal);
		
		JLabel networkIP = new JLabel("IP:");
		networkIP.setBounds(10, (int)(network.getY() + network.getHeight() + 10), defaultWidth, defaultHeight);
		networkIP.setFont(defaultFont);
		p.add(networkIP);
		
		inter.networkIPVal.setBounds((int)(networkIP.getX() + networkIP.getWidth() + 10), networkIP.getY(), defaultWidth, defaultHeight);
		inter.networkIPVal.setFont(defaultFont);
		p.add(inter.networkIPVal);
		
		return p;
	}
	 
	public static void main(String[] args) {
		MainUI ui = new MainUI(args[0]);
//		MainUI ui = new MainUI("");
		ui.setVisible(true);
	}


	

	class ProgramExecutor extends Thread {
		String fileName;
		int id = 1;
		boolean finished;
		
		public ProgramExecutor(String f) {
			fileName = f;
			finished = false;
		}
		
		public void stopThread(){
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
				writer.write("sudo /usr/local/click/bin/click " + fileName 	+ "\n");
				writer.flush();
				String line = reader.readLine();
				while (!finished && line != null) {
					System.out.println("Stdout : " + line);
					Date date = new java.util.Date();
					TerminalTableRowEntry r = new TerminalTableRowEntry(id++,
							new Timestamp(date.getTime()), line);
					t.all.add(r);
					if(line.toLowerCase().contains(query.toLowerCase()))
						t.current.add(r);
					parseCommand(line);
					table.getTableHeader().repaint();
					table.tableChanged(new TableModelEvent(t));
					table.repaint();
					line = reader.readLine();
				}
				writer.close();
				reader.close();
				process.destroy();
			} catch (Exception e) {

			}
		}
		public void parseCommand(String str){
			str = str.toLowerCase();
			if(str.startsWith("channel_switching")) {
				String[] split = str.split("[ ]+");
				String interfaceName = split[1];
				int channel = new Integer(split[2]);
				if (wifiCards.containsKey(interfaceName)) {
					WirelessInterface inter = wifiCards.get(interfaceName);
					inter.channelVal.setText(channel+"");
				} else {
					WirelessInterface inter = new WirelessInterface();
					wifiCards.put(interfaceName,inter);
					tabbedPane.add(interfaceName, createPane(interfaceName,inter));
					inter.channelVal.setText(channel+"");
				}
			} else if(str.startsWith("packet_sent")){
				sentPackets++;
				packetsSentVal.setText(sentPackets+"");
			} else if(str.startsWith("packet_received")){
				receivedPackets++;
				packetsReceivedVal.setText(receivedPackets+"");
			} else if(str.startsWith("network_creation")){
				String[] split = str.split("[ ]+");
				String interfaceName = split[1];
				String networkID = split[2];
				if (wifiCards.containsKey(interfaceName)) {
					WirelessInterface inter = wifiCards.get(interfaceName);
					inter.networkVal.setText(networkID);
				} else {
					WirelessInterface inter = new WirelessInterface();
					wifiCards.put(interfaceName,inter);
					tabbedPane.add(interfaceName, createPane(interfaceName,inter));
					inter.networkVal.setText(networkID);
				}
			} else if(str.startsWith("network_ip")){
				String[] split = str.split("[ ]+");
				String interfaceName = split[1];
				String networkIP = split[2];
				if (wifiCards.containsKey(interfaceName)) {
					WirelessInterface inter = wifiCards.get(interfaceName);
					inter.networkIPVal.setText(networkIP);
				} else {
					WirelessInterface inter = new WirelessInterface();
					wifiCards.put(interfaceName,inter);
					tabbedPane.add(interfaceName, createPane(interfaceName,inter));
					inter.networkIPVal.setText(networkIP);
				}
			} else if(str.startsWith("primary_user_active")) {
				String[] split = str.split("[ ]+");
				int channel = new Integer(split[1]);
				puTableModel.setPU(channel, true);
				putable.tableChanged(new TableModelEvent(puTableModel));
				putable.repaint();
			} else if(str.startsWith("primary_user_inactive")) {
				String[] split = str.split("[ ]+");
				int channel = new Integer(split[1]);
				puTableModel.setPU(channel, false);
				putable.tableChanged(new TableModelEvent(puTableModel));
				putable.repaint();
			}
		}
	}
}
