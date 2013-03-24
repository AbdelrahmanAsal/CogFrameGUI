import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;


public class WirelessInterfacePanel extends JPanel {
	public WirelessInterfacePanel(WirelessInterface inter) {
		setLayout(null);
		int defaultWidth = 220, defaultHeight = 40;
		Font defaultFont = new Font("MS Mincho", Font.BOLD, 15);

		JLabel channel = new JLabel("Channel:");
		channel.setBounds(10, 10, defaultWidth, defaultHeight);
		channel.setFont(defaultFont);
		add(channel);
		
		inter.channelVal.setBounds((int)(channel.getX() + channel.getWidth() + 10), channel.getY(), defaultWidth, defaultHeight);
		inter.channelVal.setFont(defaultFont);
		add(inter.channelVal);
		
		JLabel network = new JLabel("Network:");
		network.setBounds(10, (int)(channel.getY() + channel.getHeight() + 10), defaultWidth, defaultHeight);
		network.setFont(defaultFont);
		add(network);
		
		inter.networkVal.setBounds((int)(network.getX() + network.getWidth() + 10), network.getY(), defaultWidth, defaultHeight);
		inter.networkVal.setFont(defaultFont);
		add(inter.networkVal);
		
		JLabel networkIP = new JLabel("IP:");
		networkIP.setBounds(10, (int)(network.getY() + network.getHeight() + 10), defaultWidth, defaultHeight);
		networkIP.setFont(defaultFont);
		add(networkIP);
		
		inter.networkIPVal.setBounds((int)(networkIP.getX() + networkIP.getWidth() + 10), networkIP.getY(), defaultWidth, defaultHeight);
		inter.networkIPVal.setFont(defaultFont);
		add(inter.networkIPVal);
		
		JLabel noOfSwitchesLabel = new JLabel("No. Of Switches:");
		noOfSwitchesLabel.setBounds(10, (int)(networkIP.getY() + networkIP.getHeight() + 10), defaultWidth, defaultHeight);
		noOfSwitchesLabel.setFont(defaultFont);
		add(noOfSwitchesLabel);
		
		inter.noOfSwitches.setBounds((int)(noOfSwitchesLabel.getX() + noOfSwitchesLabel.getWidth() + 10), noOfSwitchesLabel.getY(), defaultWidth, defaultHeight);
		inter.noOfSwitches.setFont(defaultFont);
		add(inter.noOfSwitches);
		
		JLabel switchingTimeLabel = new JLabel("Average Swithcing Time:");
		switchingTimeLabel.setBounds(10, (int)(noOfSwitchesLabel.getY() + noOfSwitchesLabel.getHeight() + 10), defaultWidth, defaultHeight);
		switchingTimeLabel.setFont(defaultFont);
		add(switchingTimeLabel);
		
		inter.averageSwitchingTime.setBounds((int)(switchingTimeLabel.getX() + switchingTimeLabel.getWidth() + 10), switchingTimeLabel.getY(), defaultWidth, defaultHeight);
		inter.averageSwitchingTime.setFont(defaultFont);
		add(inter.averageSwitchingTime);
	}
}
