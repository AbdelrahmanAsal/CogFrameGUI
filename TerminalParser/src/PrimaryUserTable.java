import java.util.TreeMap;
import java.util.Map.Entry;

import javax.swing.table.DefaultTableModel;

public class PrimaryUserTable extends DefaultTableModel {

	TreeMap<Integer, Boolean> pu;
	public PrimaryUserTable(){
		pu = new TreeMap<Integer, Boolean>();
	}
	String[] colNames = {"Channel", "State"};
	@Override
	public int getRowCount() {
		return pu == null ? 0 : pu.size();
	}

	@Override
	public int getColumnCount() {
		return 2;
	}
	public String getColumnName(int column) {
		return colNames[column];
	}

	@Override
	public Object getValueAt(int row, int column) {
		int index = 0;
		for (Entry<Integer, Boolean> e : pu.entrySet()) {
			if (row == index) {
				if (column == 0)
					return e.getKey();
				else
					return e.getValue() ? "Active" : "Inactive";
			}
			index++;
		}
		return "";
	}
	public void setPU(int channel,boolean state){
		pu.put(channel, state);
	}
}
