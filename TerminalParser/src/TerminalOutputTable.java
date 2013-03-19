import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;


public class TerminalOutputTable extends AbstractTableModel {
	String[] colNames = { "ID", "Timestamp", "Log" };
	ArrayList<TerminalTableRowEntry> current, all;
	protected int columnsCount = colNames.length;

	public TerminalOutputTable() {
		current = new ArrayList<TerminalTableRowEntry>();
		all = new ArrayList<TerminalTableRowEntry>();
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
			TerminalTableRowEntry.sorting = modelIndex;
			TerminalTableRowEntry.isSortAsc = !TerminalTableRowEntry.isSortAsc;
			for (int i = 0; i < columnsCount; i++) {
				TableColumn column = colModel.getColumn(i);
				column.setHeaderValue(getColumnName(column.getModelIndex()));
			}
			table.getTableHeader().repaint();
			Collections.sort(current);
			table.tableChanged(new TableModelEvent(TerminalOutputTable.this));
			table.repaint();
		}
	}
}
