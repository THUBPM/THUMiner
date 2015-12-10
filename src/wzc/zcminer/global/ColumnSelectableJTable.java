package wzc.zcminer.global;

import javax.swing.JTable;
//列选中表
public class ColumnSelectableJTable extends JTable {
	public int column;

	public ColumnSelectableJTable() {
		super();
		setColumnSelectionAllowed(true);
		setRowSelectionAllowed(false);
	}
	
	public ColumnSelectableJTable(Object[][] items, Object[] headers) {
		super(items, headers);
		setColumnSelectionAllowed(true);
		setRowSelectionAllowed(false);
	}
	
	@Override
	public boolean isCellEditable(int row,int column){
		return false;
	}

}