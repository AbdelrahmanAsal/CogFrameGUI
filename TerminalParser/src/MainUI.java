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
	MyTableModel t;
	JTable table;
	JScrollPane ps;
	int sorting;
	boolean isSortAsc;
	final int[] colWidth = {60, 200, 520};
	String fileName;
	String query = "";
	JTabbedPane tabbedPane;
	HashMap<String,WirelessInterface> wifiCards;
	int defaultWidth = 220, defaultHeight = 40;
	Font defaultFont = new Font("MS Mincho", Font.BOLD, 15);
	int sentPackets, receivedPackets;
	JLabel packetsSentVal, packetsReceivedVal, primaryUserStateVal;
	
	public MainUI(String file) {
//		String file="";
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
//				if(k.getKeyCode() == KeyEvent.VK_ENTER)
				{
					query = searchField.getText();
					t.current = new ArrayList<RowEntry>();
					for(RowEntry r: t.all)
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
		getContentPane().add(searchField);
		
		
		JButton applyButton = new JButton("Apply");
		applyButton.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				query = searchField.getText();
				t.current = new ArrayList<RowEntry>();
				for(RowEntry r: t.all)
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
					t.current = new ArrayList<RowEntry>();
					for(RowEntry r: t.all)
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
				t.current = new ArrayList<RowEntry>();
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
		
		t = new MyTableModel();
		table = new JTable();
		table.setModel(t);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		for (int i = 0; i < colWidth.length; i++) {
			TableColumn tcol = table.getColumnModel().getColumn(i);
			tcol.setPreferredWidth(colWidth[i]);
			tcol.setCellRenderer(new CustomCellRenderer());
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
		
		JLabel primaryUserState = new JLabel("Primary User State:");
		primaryUserState.setBounds((int)(ps.getX() + ps.getWidth() + 10), (int)(packetsSent.getY() + packetsSent.getHeight() + 10), defaultWidth, defaultHeight);
		primaryUserState.setFont(defaultFont);
		getContentPane().add(primaryUserState);
		
		primaryUserStateVal = new JLabel(emptyLabelVal);
		primaryUserStateVal.setBounds((int)(primaryUserState.getX() + primaryUserState.getWidth() + 10), primaryUserState.getY(), defaultWidth, defaultHeight);
		primaryUserStateVal.setFont(defaultFont);
		getContentPane().add(primaryUserStateVal);
		
		ProgramExecutor exec = new ProgramExecutor(fileName);
		exec.start();
	}
	class WirelessInterface{
		JLabel channelVal;
		JLabel networkVal;
		
		public WirelessInterface(){
			channelVal = new JLabel("---");
			networkVal = new JLabel("---");
		}
	}
	
	JPanel createPane(String s, WirelessInterface inter) {
		JPanel p = new JPanel();
		p.setLayout(null);
		int defaultWidth = 220, defaultHeight = 40;
		Font defaultFont = new Font("MS Mincho", Font.BOLD, 15);

		String emptyLabelVal = "---";
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
		
		return p;
	}
	 
	public static void main(String[] args) {
		MainUI ui = new MainUI(args[0]);
//		MainUI ui = new MainUI();
		ui.setVisible(true);
	}

	class CustomCellRenderer extends DefaultTableCellRenderer {

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			Component rendererComp = super.getTableCellRendererComponent(table,
					value, isSelected, hasFocus, row, column);
			if(isSelected){
				rendererComp.setBackground(Color.BLACK);
				return rendererComp;
			}
			String[] commands = {"discard", "channel_switching", "network_creation", "protocol", "packet_received", "primary_user_appeared", "packet_sent", "packet_dest"};
			Color[] colors = {Color.LIGHT_GRAY, Color.CYAN, Color.BLUE, Color.ORANGE, Color.GREEN, Color.RED, Color.GREEN, Color.YELLOW};
			boolean set = false;
			for(int i = 0; i < commands.length; ++i) {
				if (t.current.get(row).line.toLowerCase().contains(commands[i])){
					rendererComp.setBackground(colors[i]);
					set = true;
					break;
				}
			}
			if(!set)
				rendererComp.setBackground(Color.WHITE);
			return rendererComp;
		}
	}

	class MyTableModel extends AbstractTableModel {
		String[] colNames = { "ID", "Timestamp", "Log" };
		ArrayList<RowEntry> current, all;
		protected int columnsCount = colNames.length;

		public MyTableModel() {
			current = new ArrayList<RowEntry>();
			all = new ArrayList<RowEntry>();
		}

		public int getRowCount() {
			return current == null ? 0 : current.size();
		}

		public int getColumnCount() {
			return columnsCount;
		}

		public String getColumnName(int column) {
			return colNames[column];
		}

		public boolean isCellEditable(int nRow, int nCol) {
			return false;
		}

		public Object getValueAt(int nRow, int nCol) {
			if (nRow < 0 || nRow >= current.size())
				return "";
			if (nCol == 0)
				return current.get(nRow).identifer;
			if (nCol == 1)
				return current.get(nRow).time.toString();
			else if (nCol == 2)
				return current.get(nRow).line;
			return "";
		}

		public String getTitle() {
			return "Data";
		}

		class ColumnListener extends MouseAdapter {
			protected JTable table;

			public ColumnListener(JTable t) {
				table = t;
			}

			public void mouseClicked(MouseEvent e) {
				TableColumnModel colModel = table.getColumnModel();
				int columnModelIndex = colModel.getColumnIndexAtX(e.getX());
				int modelIndex = colModel.getColumn(columnModelIndex)
						.getModelIndex();

				if (modelIndex < 0)
					return;
				sorting = modelIndex;
				isSortAsc = !isSortAsc;
				for (int i = 0; i < columnsCount; i++) {
					TableColumn column = colModel.getColumn(i);
					column.setHeaderValue(getColumnName(column.getModelIndex()));
				}
				table.getTableHeader().repaint();
				Collections.sort(current);
				table.tableChanged(new TableModelEvent(MyTableModel.this));
				table.repaint();
			}
		}
	}

	class ProgramExecutor extends Thread {
		String fileName;
		int id = 1;
		
		public ProgramExecutor(String f) {
			fileName = f;
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
				while (line != null) {
					System.out.println("Stdout : " + line);
					Date date = new java.util.Date();
					RowEntry r = new RowEntry(id++,
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
			} catch (Exception e) {

			}
		}
		public void parseCommand(String str){
			if(str.toLowerCase().startsWith("channel_switching")) {
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
			} else if(str.toLowerCase().startsWith("packet_sent")){
				sentPackets++;
				packetsSentVal.setText(sentPackets+"");
			} else if(str.toLowerCase().startsWith("packet_received")){
				receivedPackets++;
				packetsReceivedVal.setText(receivedPackets+"");
			} else if(str.toLowerCase().startsWith("network_creation")){
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
			}
			
					
		}
	}

	class RowEntry implements Comparable<RowEntry> {
		public Timestamp time;
		public String line;
		public int identifer;

		public RowEntry(int id, Timestamp t, String l) {
			identifer = id;
			time = t;
			line = l;
		}

		public int compareTo(RowEntry r2) {
			if (sorting == 0)
				return (isSortAsc ? 1 : -1) * (identifer - r2.identifer);
			if (sorting == 1) {
				if (time.compareTo(r2.time) == 0)
					return (isSortAsc ? 1 : -1) * (identifer - r2.identifer);
				return (isSortAsc ? 1 : -1) * time.compareTo(r2.time);
			}
			return (isSortAsc ? 1 : -1) * line.compareTo(r2.line);
		}
	}
}
