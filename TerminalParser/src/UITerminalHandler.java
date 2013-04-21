import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;

public class UITerminalHandler {
	HashMap<String, WirelessInterface> wifiCards;
	PrimaryUserTable puTableModel;
	JTabbedPane tabbedPane;
	JTable table, putable;
	TerminalOutputTable t;
	JLabel packetsSentVal, packetsReceivedVal;
	public UITerminalHandler(HashMap<String, WirelessInterface> wifiCards, PrimaryUserTable puTableModel, JTabbedPane tabbedPane, JTable table, JTable putable, TerminalOutputTable t,JLabel packetsSentVal, JLabel packetsReceivedVal) {
		this.wifiCards = wifiCards;
		this.puTableModel = puTableModel;
		this.tabbedPane = tabbedPane;
		this.table = table;
		this.putable = putable;
		this.t = t;
		this.packetsSentVal = packetsSentVal;
		this.packetsReceivedVal = packetsReceivedVal;
	}
	public void parseCommand(int id, String str) {
		Date date = new java.util.Date();
		TerminalTableRowEntry r = new TerminalTableRowEntry(id,
				new Timestamp(date.getTime()), str);
		t.all.add(r);
		// Need to handle the search query , adding real time lines
//		if (line.toLowerCase().contains(query.toLowerCase()))
//			t.current.add(r);
		table.getTableHeader().repaint();
		table.tableChanged(new TableModelEvent(t));
		table.repaint();
		str = str.toLowerCase();
		if (str.startsWith("channel_switching")) {
			String[] split = str.split("[ ]+");
			String interfaceName = split[1];
			int channel = new Integer(split[2]);
			if (wifiCards.containsKey(interfaceName)) {
				WirelessInterface inter = wifiCards.get(interfaceName);
				inter.channelVal.setText(channel + "");
			} else {
				WirelessInterface inter = new WirelessInterface();
				wifiCards.put(interfaceName, inter);
				tabbedPane.add(interfaceName, new WirelessInterfacePanel(
						inter));
				inter.channelVal.setText(channel + "");
			}
			WirelessInterface inter = wifiCards.get(interfaceName);
			inter.switches++;
			inter.noOfSwitches.setText(inter.switches + "");
		} else if (str.startsWith("packet_sent")) {
			packetsSentVal.setText((new Integer(packetsSentVal.getText())+1)+"");
		} else if (str.startsWith("packet_received")) {
			packetsReceivedVal.setText((new Integer(packetsReceivedVal.getText())+1)+"");
		} else if (str.startsWith("network_creation")) {
			String[] split = str.split("[ ]+");
			String interfaceName = split[1];
			String networkID = split[2];
			if (wifiCards.containsKey(interfaceName)) {
				WirelessInterface inter = wifiCards.get(interfaceName);
				inter.networkVal.setText(networkID);
			} else {
				WirelessInterface inter = new WirelessInterface();
				wifiCards.put(interfaceName, inter);
				tabbedPane.add(interfaceName, new WirelessInterfacePanel(
						inter));
				inter.networkVal.setText(networkID);
			}
		} else if (str.startsWith("network_ip")) {
			String[] split = str.split("[ ]+");
			String interfaceName = split[1];
			String networkIP = split[2];
			if (wifiCards.containsKey(interfaceName)) {
				WirelessInterface inter = wifiCards.get(interfaceName);
				inter.networkIPVal.setText(networkIP);
			} else {
				WirelessInterface inter = new WirelessInterface();
				wifiCards.put(interfaceName, inter);
				tabbedPane.add(interfaceName, new WirelessInterfacePanel(
						inter));
				inter.networkIPVal.setText(networkIP);
			}
		} else if (str.startsWith("primary_user_active")) {
			String[] split = str.split("[ ]+");
			int channel = new Integer(split[1]);
			puTableModel.setPU(channel, true);
			putable.tableChanged(new TableModelEvent(puTableModel));
			putable.repaint();
		} else if (str.startsWith("primary_user_inactive")) {
			String[] split = str.split("[ ]+");
			int channel = new Integer(split[1]);
			puTableModel.setPU(channel, false);
			putable.tableChanged(new TableModelEvent(puTableModel));
			putable.repaint();
		} else if (str.startsWith("average_switching_time")) {
			String[] split = str.split("[ ]+");
			String interfaceName = split[1];
			String switchingTime = split[2];
			if (wifiCards.containsKey(interfaceName)) {
				WirelessInterface inter = wifiCards.get(interfaceName);
				inter.averageSwitchingTime.setText(switchingTime);
			} else {
				WirelessInterface inter = new WirelessInterface();
				wifiCards.put(interfaceName, inter);
				tabbedPane.add(interfaceName, new WirelessInterfacePanel(
						inter));
				inter.averageSwitchingTime.setText(switchingTime);
			}
		}
	}
}
