import java.awt.Font;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

public class MainUI extends JFrame {
	TerminalOutputTable t;
	JTable table, putable;
	JScrollPane ps;
	final int[] colWidth = { 60, 200, 520 };
	String query = "";
	JTabbedPane tabbedPane;
	HashMap<String, WirelessInterface> wifiCards;
	int defaultWidth = 220, defaultHeight = 40;
	Font defaultFont = new Font("MS Mincho", Font.BOLD, 15);
	int sentPackets, receivedPackets;
	JLabel packetsSentVal, packetsReceivedVal, primaryUserStateVal;
	ProgramExecutor exec;
	PrimaryUserTable puTableModel;
	Server server;
	UITerminalHandler terminalHandler;

	public MainUI(String modulePath) {
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		wifiCards = new HashMap<String, WirelessInterface>();
		setDefaultLookAndFeelDecorated(true);
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				// Nimbus or GTK+
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
			// UIManager.setLookAndFeel("seaglasslookandfeel.SeaGlassLookAndFeel");
		} catch (Exception e) {

		}

		JLabel filterLabel = new JLabel("Filter:");
		filterLabel.setFont(defaultFont);

		final JTextField searchField = new JTextField();
		searchField.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent k) {
				query = searchField.getText();
				terminalHandler.query = query;
				t.current.clear();
				for (TerminalTableRowEntry r : t.all)
					if (r.line.toLowerCase().contains(query.toLowerCase()))
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

		JButton applyButton = new JButton("Apply");
		applyButton.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				query = searchField.getText();
				terminalHandler.query = query;
				t.current.clear();
				for (TerminalTableRowEntry r : t.all)
					if (r.line.toLowerCase().contains(query.toLowerCase()))
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

		applyButton.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent k) {
				if (k.getKeyCode() == KeyEvent.VK_ENTER) {
					query = searchField.getText();
					t.current.clear();
					for (TerminalTableRowEntry r : t.all)
						if (r.line.toLowerCase().contains(query.toLowerCase()))
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

		JButton clearButton = new JButton("Clear");
		clearButton.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				query = "";
				terminalHandler.query = query;
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

		JButton stopButton = new JButton("Stop");
		stopButton.addMouseListener(new MouseListener() {

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

		final JTextField nodeNameField = new JTextField();

		JButton setNodeNameButton = new JButton("Set node name");
		setNodeNameButton.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				String nodeNameStr = nodeNameField.getText();
				server.nodeName = nodeNameStr;
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

		// JScrollBar sb = ps.getVerticalScrollBar();
		// boolean onBottom = (sb.getValue() == sb.getMaximum());
		// if(onBottom)
		// sb.setValue(sb.getMaximum());

		ps.getVerticalScrollBar().addAdjustmentListener(
				new AdjustmentListener() {
					public void adjustmentValueChanged(AdjustmentEvent e) {
						boolean onBottom = (e.getAdjustable().getValue() == e
								.getAdjustable().getMaximum());
						if (onBottom)
							e.getAdjustable().setValue(
									e.getAdjustable().getMaximum());
					}
				});

		tabbedPane = new JTabbedPane();

		String emptyLabelVal = "0";
		JLabel packetsReceived = new JLabel("No. of received packets:");
		packetsReceived.setFont(defaultFont);

		packetsReceivedVal = new JLabel(emptyLabelVal);
		packetsReceivedVal.setFont(defaultFont);

		JLabel packetsSent = new JLabel("No. of Sent packets:");
		packetsSent.setFont(defaultFont);

		packetsSentVal = new JLabel(emptyLabelVal);
		packetsSentVal.setFont(defaultFont);

		JLabel primaryUserState = new JLabel("Primary User State");
		primaryUserState.setFont(defaultFont);
		primaryUserStateVal = new JLabel("Test");

		puTableModel = new PrimaryUserTable();
		putable = new JTable();
		putable.setModel(puTableModel);
//		putable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		for (int i = 0; i < 2; i++) {
			TableColumn tcol = putable.getColumnModel().getColumn(i);
			tcol.setCellRenderer(new PrimaryUserTableCellRenderer());
		}

		JScrollPane puScrollPane = new JScrollPane(putable,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		terminalHandler = new UITerminalHandler(wifiCards,puTableModel, tabbedPane,table, putable, t, packetsSentVal, packetsReceivedVal);
		exec = new ProgramExecutor("Configuration.txt", terminalHandler);
		server = new Server();
		server.modulePath = modulePath;
		server.exec = exec;
		server.start();
		layout.setHorizontalGroup(
				   layout.createSequentialGroup()
				      .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				           .addGroup(layout.createSequentialGroup()
				        		   .addComponent(filterLabel)
				        		   .addComponent(searchField)
				        		   .addComponent(applyButton)
				        		   .addComponent(clearButton)
				        		   .addComponent(stopButton)
				        		   .addComponent(nodeNameField)
				        		   .addComponent(setNodeNameButton)
				        		   )
				            .addComponent(ps))
				    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				    		.addComponent(tabbedPane)
				    		 .addGroup(layout.createSequentialGroup()
					        		   .addComponent(packetsReceived)
					        		   .addComponent(packetsReceivedVal))
			        	     .addGroup(layout.createSequentialGroup()
						       		   .addComponent(packetsSent)
						       		   .addComponent(packetsSentVal))
						       .addGroup(layout.createSequentialGroup()
						       		   .addComponent(primaryUserState)
						       		   .addComponent(primaryUserStateVal))
				            .addComponent(puScrollPane)
				    		)
				);
				layout.setVerticalGroup(
				   layout.createSequentialGroup()
				      .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				    		  .addComponent(filterLabel)
			        		   .addComponent(searchField)
			        		   .addComponent(applyButton)
			        		   .addComponent(clearButton)
			        		   .addComponent(stopButton)
			        		   .addComponent(nodeNameField)
			        		   .addComponent(setNodeNameButton)
			        		   )
				      .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				    		  .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				    				  .addComponent(ps)
				    		  .addGroup(layout.createSequentialGroup()
				    				  .addComponent(tabbedPane)
				    				   .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				    						   .addComponent(packetsReceived)
				    						   .addComponent(packetsReceivedVal))
				    				 .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				    						   .addComponent(packetsSent)
				    						   .addComponent(packetsSentVal))
				    				   .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				    						   .addComponent(primaryUserState)
				    						   .addComponent(primaryUserStateVal))
				    				   .addComponent(puScrollPane)
				    				  ))
				      	)
				);
				layout.setAutoCreateGaps(true) ;
				layout.setAutoCreateContainerGaps(true);
	}
}
