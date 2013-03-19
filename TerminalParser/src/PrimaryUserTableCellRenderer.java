import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;


public class PrimaryUserTableCellRenderer extends DefaultTableCellRenderer{

	    @Override
	    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
	    	PrimaryUserTable model = (PrimaryUserTable) table.getModel();
	        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	        String state = (String) model.getValueAt(row, 1);
	        if(state.equals("Active")) 
	        	c.setForeground(Color.GREEN);
	        else
	        	c.setForeground(Color.RED);
	        return c;
	    }
}
