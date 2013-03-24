import javax.swing.JLabel;


public class WirelessInterface {
	JLabel channelVal;
	JLabel networkVal;
	JLabel networkIPVal;
	JLabel noOfSwitches;
	JLabel averageSwitchingTime;
	int switches;
	
	public WirelessInterface(){
		channelVal = new JLabel("---");
		networkVal = new JLabel("---");
		networkIPVal = new JLabel("---");
		noOfSwitches = new JLabel("---");
		averageSwitchingTime = new JLabel("---");
		switches = 0;
	}
}
