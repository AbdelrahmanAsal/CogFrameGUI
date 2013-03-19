import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;


public class TerminalTableCellRenderer  extends DefaultTableCellRenderer {

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			Component rendererComp = super.getTableCellRendererComponent(table,
					value, isSelected, hasFocus, row, column);
			if(isSelected){
				rendererComp.setBackground(Color.BLACK);
				return rendererComp;
			}
			TerminalOutputTable tableModel = (TerminalOutputTable) table.getModel();
			String[] commands = {"discard", "channel_switching", "network_creation", "protocol", "packet_received", "primary_user_active", "packet_sent", "packet_dest", "network_ip", "primary_user_inactive"};
			Color[] colors = {Color.LIGHT_GRAY, Color.CYAN, Color.BLUE, Color.ORANGE, Color.GREEN, Color.RED, Color.GREEN, Color.YELLOW, Color.PINK, Color.MAGENTA};
			boolean set = false;
			for(int i = 0; i < commands.length; ++i) {
				if (tableModel.current.get(row).line.toLowerCase().contains(commands[i])){
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
