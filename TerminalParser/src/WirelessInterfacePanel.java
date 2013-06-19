import java.awt.Font;

import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;



public class WirelessInterfacePanel extends JPanel {
	public WirelessInterfacePanel(WirelessInterface inter) {
		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);
		Font defaultFont = new Font("MS Mincho", Font.BOLD, 15);

		JLabel channel = new JLabel("Channel:");
		channel.setFont(defaultFont);
		
		inter.channelVal.setFont(defaultFont);
		
		JLabel network = new JLabel("Network:");
		network.setFont(defaultFont);
		
		inter.networkVal.setFont(defaultFont);
		
		JLabel networkIP = new JLabel("IP:");
		networkIP.setFont(defaultFont);
		
		inter.networkIPVal.setFont(defaultFont);
		
		JLabel noOfSwitchesLabel = new JLabel("No. Of Switches:");
		noOfSwitchesLabel.setFont(defaultFont);
		
		inter.noOfSwitches.setFont(defaultFont);
		
		JLabel switchingTimeLabel = new JLabel("Average Swithcing Time:");
		switchingTimeLabel.setFont(defaultFont);
		
		inter.averageSwitchingTime.setFont(defaultFont);
		
		layout.setHorizontalGroup(
				   layout.createSequentialGroup()
				   .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				    		.addComponent(channel)
				    		.addComponent(network)
				    		.addComponent(networkIP)
				    		.addComponent(noOfSwitchesLabel)
				    		.addComponent(switchingTimeLabel))
				   .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						   .addComponent(inter.channelVal)
						   .addComponent(inter.networkVal)
						   .addComponent(inter.networkIPVal)
						   .addComponent(inter.noOfSwitches)
						   .addComponent(inter.averageSwitchingTime))
	    );
		layout.setVerticalGroup(
				   layout.createSequentialGroup()
				   .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						   .addComponent(channel)
						   .addComponent(inter.channelVal)
						  )
				    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						   .addComponent(network)
						   .addComponent(inter.networkVal)
						  )
				    	 .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						   .addComponent(networkIP)
						   .addComponent(inter.networkIPVal)
						  )
					 .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
					 	   .addComponent(noOfSwitchesLabel)
						   .addComponent(inter.noOfSwitches)
						  )
				     .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
					 	   .addComponent(switchingTimeLabel)
						   .addComponent(inter.averageSwitchingTime)
						  )
		);
		layout.setAutoCreateGaps(true) ;
		layout.setAutoCreateContainerGaps(true);
	}
}
