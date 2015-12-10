package wzc.zcminer.global;

import javax.swing.JTable;
//列选中表
public class RowSelectableJTable extends JTable {
	public int column;
	
	public RowSelectableJTable() {
		super();
		setColumnSelectionAllowed(false);
		setRowSelectionAllowed(true);
	}

	public RowSelectableJTable(Object[][] items, Object[] headers) {
		super(items, headers);
		setColumnSelectionAllowed(false);
		setRowSelectionAllowed(true);
	}
	
	@Override
	public boolean isCellEditable(int row,int column){
		return false;
	}

}