import java.sql.Timestamp;



public class TerminalTableRowEntry implements Comparable<TerminalTableRowEntry> {
		public Timestamp time;
		public String line;
		public int identifer;
		static int sorting;
		static boolean isSortAsc;
		

		public TerminalTableRowEntry(int id, Timestamp t, String l) {
			identifer = id;
			time = t;
			line = l;
		}

		public int compareTo(TerminalTableRowEntry r2) {
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
