package wzc.zcminer.frame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import wzc.zcminer.global.Activity;
import wzc.zcminer.global.ColumnSelectableJTable;
import wzc.zcminer.global.Event;
import wzc.zcminer.global.Case;
import wzc.zcminer.global.Resource;
import wzc.zcminer.global.Variant;

import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.opencsv.CSVReader;

//数据导入面板
public class ImportPanel extends JPanel{
	static ColumnSelectableJTable table;
	static JPanel radioPanel;
	static JRadioButton removeButton;
	static JRadioButton caseButton;
	static JRadioButton activityButton;
	static JRadioButton timeButton;
	static JRadioButton resourseButton;
	static JRadioButton otherButton;
	static ButtonGroup buttonGroup;
	static FileButton fileButton;
	static DatabaseButton databaseButton;
	static JTextField timeText; //时间模式输入
	static JButton okButton;
	final static int MAXHEADINDEX = 20;
	int[] headIndex;
	String[][] rowData;
	List<String[]> myEntries;
	String[] headlines;
	
	int maxLine;

	public ImportPanel(String fileName, char separator, boolean hasTableHead, String encodingText) {
		// TODO Auto-generated constructor stub
		try {
			if(fileName.endsWith(".xls") || fileName.endsWith(".xlsx"))
			{
			    String fileType = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
			    InputStream stream = new FileInputStream(fileName);
			    Workbook wb = null;
			    if(fileType.equals("xls")) {
			    	wb = new HSSFWorkbook(stream);
			    }
			    else if(fileType.equals("xlsx"))
			    {
			    	wb = new XSSFWorkbook(stream);
			    }
			    Sheet sheet1 = wb.getSheetAt(0);
			    myEntries = new ArrayList<String[]>();
			    for(Row row : sheet1)
			    {
			    	String[] rowData = new String[row.getPhysicalNumberOfCells()];
			    	for(Cell cell : row)
			    	{
			    		cell.setCellType(Cell.CELL_TYPE_STRING);
			    		rowData[cell.getColumnIndex()] = cell.getStringCellValue();
			    	}
			    	myEntries.add(rowData);
			    }
			}
			else
			{
				//csv数据导入
				DataInputStream input = new DataInputStream(new FileInputStream(new File(fileName)));
				CSVReader reader = new CSVReader(new InputStreamReader(input, encodingText), separator);
				myEntries = reader.readAll();
				reader.close();
			}
			
			if(hasTableHead)
			{
				headlines = myEntries.remove(0);
			}
			else
			{
				headlines = new String[myEntries.get(0).length];
				for(int i = 0; i < headlines.length; i++)
				{
					if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
					{
						headlines[i] = "Column" + (i + 1);
					}
					else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
					{
						headlines[i] = "第" + (i + 1) + "列";
					}
				}
			}
			setPanel("file_timestamp");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	
	public ImportPanel(String db_table) {
		// TODO Auto-generated constructor stub
		try {
			myEntries = new ArrayList<String[]>();
         	String count_sql = "select count(column_name) from user_tab_columns where table_name='" + db_table + "'";
         	MainFrame.oracleStatement = MainFrame.oracleConnection.prepareStatement(count_sql);
            MainFrame.oracleResult = MainFrame.oracleStatement.executeQuery();
            int column_count = 0;
            while (MainFrame.oracleResult.next())
            {
            	column_count = MainFrame.oracleResult.getInt(1);
            }
	        if (MainFrame.oracleResult != null)
	        	MainFrame.oracleResult.close();
	        if (MainFrame.oracleStatement != null)
	        	MainFrame.oracleStatement.close();
		    
         	String header_sql = "select column_name from user_tab_columns where table_name='" + db_table + "'";
         	MainFrame.oracleStatement = MainFrame.oracleConnection.prepareStatement(header_sql);
            MainFrame.oracleResult = MainFrame.oracleStatement.executeQuery();
            String[] temp = new String[column_count];
            int count = 0;
            while (MainFrame.oracleResult.next())
            {
                temp[count] = MainFrame.oracleResult.getString(1);
                count++;
            }
            myEntries.add(temp);
	        if (MainFrame.oracleResult != null)
	        	MainFrame.oracleResult.close();
	        if (MainFrame.oracleStatement != null)
	        	MainFrame.oracleStatement.close();
		        
         	String data_sql = "select * from " + db_table;
         	MainFrame.oracleStatement = MainFrame.oracleConnection.prepareStatement(data_sql);
            MainFrame.oracleResult = MainFrame.oracleStatement.executeQuery();
            while (MainFrame.oracleResult.next())
            {
                temp = new String[column_count];
                for(int i = 0; i < column_count; i++)
                	temp[i] = MainFrame.oracleResult.getString(myEntries.get(0)[i]);
                myEntries.add(temp);
            }
	        if (MainFrame.oracleResult != null)
	        	MainFrame.oracleResult.close();
	        if (MainFrame.oracleStatement != null)
	        	MainFrame.oracleStatement.close();
			if (MainFrame.oracleConnection != null)
				MainFrame.oracleConnection.close();
            
			headlines = myEntries.remove(0);
			setPanel("oracle_database_timestamp");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	
	public ImportPanel(String[][] tableRelation) {
		// TODO Auto-generated constructor stub
		try {
			String[] tableList = new String[tableRelation.length + 1];
			for(int i = 0; i < tableList.length; i++)
			{
				tableList[i] = "";
			}
			int tableCount = 0;
			for(int i = 0; i < tableRelation.length; i++)
			{
				String table1 = tableRelation[i][0].toUpperCase();
				if(table1 == null) table1 = "";
				if(!table1.equals(""))
				{
					boolean flag = false;
					for(int j = 0; j < tableCount; j++)
					{
						if(tableList[j].equals(table1))
						{
							flag = true;
						}
					}
					if(!flag)
					{
						tableList[tableCount] = table1;
						tableCount++;
					}
				}
				String table2 = tableRelation[i][2].toUpperCase();
				if(table2 == null) table2 = "";
				if(!table2.equals(""))
				{
					boolean flag = false;
					for(int j = 0; j < tableCount; j++)
					{
						if(tableList[j].equals(table2))
						{
							flag = true;
						}
					}
					if(!flag)
					{
						tableList[tableCount] = table2;
						tableCount++;
					}
				}
			}
			
			myEntries = new ArrayList<String[]>();
			int columnCount = 0;
			for(int i = 0; i < tableList.length; i++)
			{
				String table = tableList[i].toUpperCase();
				if(table == null) table = "";
				if(!table.equals(""))
				{
		         	String count_sql = "select count(column_name) from user_tab_columns where table_name='" + table + "'";
		         	MainFrame.oracleStatement = MainFrame.oracleConnection.prepareStatement(count_sql);
		            MainFrame.oracleResult = MainFrame.oracleStatement.executeQuery();
		            while (MainFrame.oracleResult.next())
		            {
		            	columnCount += (MainFrame.oracleResult.getInt(1));
		            }
			        if (MainFrame.oracleResult != null)
			        	MainFrame.oracleResult.close();
			        if (MainFrame.oracleStatement != null)
			        	MainFrame.oracleStatement.close();
	            }
		    }

            String[] temp = new String[columnCount];
            String[][] tableHeader = new String[tableList.length][columnCount];
            int count = 0;
			for(int i = 0; i < tableList.length; i++)
			{
				String table = tableList[i].toUpperCase();
				if(table == null) table = "";
				if(!table.equals(""))
				{
		         	String header_sql = "select column_name from user_tab_columns where table_name='" + table + "'";
		         	MainFrame.oracleStatement = MainFrame.oracleConnection.prepareStatement(header_sql);
		            MainFrame.oracleResult = MainFrame.oracleStatement.executeQuery();
		            int tableColumnCount = 0;
		            while (MainFrame.oracleResult.next())
		            {
		                String columnName = MainFrame.oracleResult.getString(1);
	                	temp[count] = columnName;
	                	tableHeader[i][tableColumnCount] = columnName;
	                	tableColumnCount++;
	                	count++;
		            }
			        if (MainFrame.oracleResult != null)
			        	MainFrame.oracleResult.close();
			        if (MainFrame.oracleStatement != null)
			        	MainFrame.oracleStatement.close();
		       	}
		    }
		    myEntries.add(temp);
		    
		    String data_sql_table = "";
		    String data_sql_condition = "";
		    for(int i = 0; i < tableList.length; i++)
			{
				String table = tableList[i].toUpperCase();
				if(table == null) table = "";
				if(!table.equals(""))
				{
		         	data_sql_table += (table + ",");
				}
		    }
		    for(int i = 0; i < tableRelation.length; i++)
			{
				String table1 = tableRelation[i][0].toUpperCase();
				String tableEventid1 = tableRelation[i][1].toUpperCase();
				String table2 = tableRelation[i][2].toUpperCase();
				String tableEventid2 = tableRelation[i][3].toUpperCase();
				if(table1 == null) table1 = "";
				if(tableEventid1 == null) tableEventid1 = "";
				if(table2 == null) table2 = "";
				if(tableEventid2 == null) tableEventid2 = "";
				if(!table1.equals("") && !tableEventid1.equals("") && !table2.equals("") && !tableEventid2.equals(""))
				{
		         	data_sql_condition += (table1 + "." + tableEventid1 + "=" + table2 + "." + tableEventid2 + " and ");
				}
		    }
		    data_sql_table = data_sql_table.substring(0, data_sql_table.length() - 1);
		    data_sql_condition = data_sql_condition.substring(0, data_sql_condition.length() - 5);
         	String data_sql = "select * from " + data_sql_table + " where " + data_sql_condition;
         	MainFrame.oracleStatement = MainFrame.oracleConnection.prepareStatement(data_sql);
            MainFrame.oracleResult = MainFrame.oracleStatement.executeQuery();
            while (MainFrame.oracleResult.next())
            {
                temp = new String[columnCount];
                for(int j = 0; j < columnCount; j++)
                	temp[j] = MainFrame.oracleResult.getString(myEntries.get(0)[j]);
                myEntries.add(temp);
            }
	        if (MainFrame.oracleResult != null)
	        	MainFrame.oracleResult.close();
	        if (MainFrame.oracleStatement != null)
	        	MainFrame.oracleStatement.close();
			if (MainFrame.oracleConnection != null)
				MainFrame.oracleConnection.close();
            
			headlines = myEntries.remove(0);
			setPanel("oracle_database_timestamp");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public ImportPanel(DBCollection coll) {
		// TODO Auto-generated constructor stub
		try {
			myEntries = new ArrayList<String[]>();
			
			DBObject header_document = coll.findOne();
			int column_count = header_document.keySet().size();
			String[] temp = new String[column_count];
            for(int i = 0; i < column_count; i++)
            	temp[i] = header_document.keySet().toArray()[i].toString();
            myEntries.add(temp);
            
			DBCursor cursor = coll.find();
			while (cursor.hasNext())
			{
				DBObject document = cursor.next();
                temp = new String[column_count];
                for(int i = 0; i < column_count; i++)
                	temp[i] = (String) document.get(myEntries.get(0)[i]).toString();
                myEntries.add(temp);
			}
            
			if (MainFrame.mongoClient != null)
				MainFrame.mongoClient.close();
			headlines = myEntries.remove(0);
			setPanel("mongo_database_timestamp");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	
	public void setPanel(String timestamp) {
		headIndex = new int[MAXHEADINDEX];
		//按钮组的设置
		setLayout(new BorderLayout());
		radioPanel = new JPanel();
		radioPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
			removeButton = new JRadioButton("Remove");
		}
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
			removeButton = new JRadioButton("移除");
		}
		removeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				headIndex[table.column] = 1;
			}
		});

		if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
			caseButton = new JRadioButton("Case");
		}
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
			caseButton = new JRadioButton("实例");
		}
		caseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				headIndex[table.column] = 2;
			}
		});

		if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
			activityButton = new JRadioButton("Activity");
		}
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
			activityButton = new JRadioButton("活动");
		}
		activityButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				headIndex[table.column] = 3;
			}
		});

		if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
			timeButton = new JRadioButton("Timestamp");
		}
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
			timeButton = new JRadioButton("时间");
		}
		timeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				headIndex[table.column] = 4;
			}
		});
		
		//时间模板输入
		timeText = new JTextField(12);
		timeText.setText(MainFrame.properties.getProperty(timestamp, "yyyy-MM-dd HH:mm:00.0"));
		//timeText.setText("dd.MM.yy HH:mm");
		//timeText.setText("dd/MM/yyyy HH:mm:ss");
		//timeText.setText("yyyy-MM-dd HH:mm:ss");
		
		if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
			resourseButton = new JRadioButton("Resourse");
		}
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
			resourseButton = new JRadioButton("资源");
		}
		resourseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				headIndex[table.column] = 5;
			}
		});

		if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
			otherButton = new JRadioButton("Other");
		}
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
			otherButton = new JRadioButton("其他");
		}
		otherButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				headIndex[table.column] = 6;
			}
		});

		fileButton = new FileButton();
		databaseButton = new DatabaseButton();

        radioPanel.add(fileButton);
        radioPanel.add(databaseButton);
		radioPanel.add(removeButton);
		radioPanel.add(otherButton);
		radioPanel.add(caseButton);
		radioPanel.add(activityButton);
		radioPanel.add(resourseButton);
		radioPanel.add(timeButton);
		if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
			radioPanel.add(new JLabel("Timestamp pattern: "));
		}
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
			radioPanel.add(new JLabel("时间格式："));
		}
		radioPanel.add(timeText);

		buttonGroup = new ButtonGroup();
		buttonGroup.add(removeButton);
		buttonGroup.add(caseButton);
		buttonGroup.add(activityButton);
		buttonGroup.add(timeButton);
		buttonGroup.add(resourseButton);
		buttonGroup.add(otherButton);
		add(radioPanel, BorderLayout.PAGE_START);
		
		//rowData = myEntries.toArray(new String[0][]);
		maxLine = 1000;
		if (myEntries.size() < maxLine){
			maxLine = myEntries.size();
		}
		String [][] tableData = myEntries.subList(0, maxLine).toArray(new String[0][]);
        maxLine = 2500000;
        if (myEntries.size() < maxLine){
            maxLine = myEntries.size();
        }
		rowData = myEntries.subList(0, maxLine).toArray(new String[0][]);
		
		myEntries = null;
		
		table = new ColumnSelectableJTable(tableData, headlines);
		table.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				int column = table.getSelectedColumn();
				table.column = column;
				switch (headIndex[column]) {
				case 1:
					buttonGroup.setSelected(removeButton.getModel(), true);
					break;
				case 2:
					buttonGroup.setSelected(caseButton.getModel(), true);
					break;
				case 3:
					buttonGroup.setSelected(activityButton.getModel(), true);
					break;
				case 4:
					buttonGroup.setSelected(timeButton.getModel(), true);
					break;
				case 5:
					buttonGroup.setSelected(resourseButton.getModel(), true);
					break;
				case 6:
					buttonGroup.setSelected(otherButton.getModel(), true);
					break;
				default:
					buttonGroup.clearSelection();
					break;
				}
			}
		});

		JScrollPane dataPanel = new JScrollPane(table);
		add(dataPanel, BorderLayout.CENTER);
		//逻辑分析代码，得出所有信息
		if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
			okButton = new JButton("Start import");
		}
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
			okButton = new JButton("开始导入");
		}
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			    setEventCollection();
			    mergeEventCollection();
			    
			    MainFrame.mainFrame.getContentPane().removeAll();
			    rowData = null;
			    headIndex = null;
                System.gc();
			    
			    setGraphNet();
			    setCaseCollection();
			    setVariantCollection();
			    setEventsOverTimeChart();
			    setActiveCasesOverTimeChart();
			    setEventsPerCaseChart();
			    setCaseDurationChart();
			    setCaseUtilizationChart();
			    setMeanActivityDurationChart();
			    setMeanWaitingTimeChart();
			    setActivityCollection();
			    setResourceCollection();
				
				MainFrame.mainFrame.getContentPane().removeAll();
				System.gc();
				MapPanel mapPanel = new MapPanel();
				MainFrame.mainFrame.setContentPane(mapPanel);
				MainFrame.mainFrame.setVisible(true);
				System.gc();
			}
		});

		add(okButton, BorderLayout.PAGE_END);
		
        addComponentListener(new ComponentAdapter() {
        	public void componentMoved(ComponentEvent e) {}
        	
        	public void componentResized(ComponentEvent e) {
        		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        		if(table.getColumnCount() * 75 < dataPanel.getWidth() - 18)
        		{
        			table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        		}
        	}
        	
        	public void componentShown(ComponentEvent e) {
        		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        		if(table.getColumnCount() * 75 < dataPanel.getWidth() - 18)
        		{
        			table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        		}
        	}
        	
        	public void componentHidden(ComponentEvent e) {}
        });
	}


	//更新event集
    public void setEventCollection() {

        for (int i = 0; i < maxLine; i++) {
            Event event = new Event();
            for (int j = 0; j < rowData[i].length; j++) {
                switch (headIndex[j]) {
                case 2:
                    event.setCase(rowData[i][j]);
                    break;
                case 3:
                    event.setActivity(rowData[i][j]);
                    if (!MainFrame.graphNet
                            .activityExist(rowData[i][j])) {
                        MainFrame.graphNet
                                .addActivity(rowData[i][j]);
                    }
                    break;
                case 4:
                    event.setDate(rowData[i][j], timeText.getText());
                    break;
                case 5:
                    event.setResource(rowData[i][j]);
                    break;
                default:
                    break;
                }
            }
            MainFrame.eventCollection.addEvent(event);
        }
        
    }
   
    
    public void mergeEventCollection() {
        MainFrame.eventCollection.sortByActivity();
        for (int i = 1; i < MainFrame.eventCollection.getSize(); i++) {
            Event event1 = MainFrame.eventCollection.getEvent(i - 1);
            Event event2 = MainFrame.eventCollection.getEvent(i);
            if(event2.getActivity().equals(event1.getActivity()))
            {
                event2.setActivity(event1.getActivity());
            }
        }
        System.gc();
        
        MainFrame.eventCollection.sortByResource();
        for (int i = 1; i < MainFrame.eventCollection.getSize(); i++) {
            Event event1 = MainFrame.eventCollection.getEvent(i - 1);
            Event event2 = MainFrame.eventCollection.getEvent(i);
            if(event2.getResource().equals(event1.getResource()))
            {
                event2.setResource(event1.getResource());
            }
        }
        System.gc();
        
        MainFrame.eventCollection.sortByID();
        for (int i = 1; i < MainFrame.eventCollection.getSize(); i++) {
            Event event1 = MainFrame.eventCollection.getEvent(i - 1);
            Event event2 = MainFrame.eventCollection.getEvent(i);
            if(event2.getCase().equals(event1.getCase()))
            {
                event2.setCase(event1.getCase());
            }
        }
        System.gc();
    }
   
    
    //更新graph
    public void setGraphNet() {

        MainFrame.graphNet.setMemory();
		if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
	        MainFrame.graphNet.activityNames[0] = "begin";
	        MainFrame.graphNet.activityNames[1] = "end";
		}
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
	        MainFrame.graphNet.activityNames[0] = "开始";
	        MainFrame.graphNet.activityNames[1] = "结束";
	    }
        int[] temp = new int[MainFrame.graphNet.activityCount]; 
        int[][] tempQue = new int[MainFrame.graphNet.activityCount][MainFrame.graphNet.activityCount]; 
        int lastActivityId = -1;
        String lastCase = "";
        Date lastDate = new Date();
        //按顺序遍历得出case，并运算各个参数
        for (int i = 0; i < MainFrame.eventCollection.getSize(); i++) {
            Event event = MainFrame.eventCollection.getEvent(i);
            String activityName = event.getActivity();
            String caseName = event.getCase();
            long time = event.getTime();
            int activityId = MainFrame.graphNet
                    .getActivityId(activityName);
            MainFrame.graphNet.setActivityName(activityId,
                    activityName);
            MainFrame.graphNet.addActivityTime(activityId, time);
            MainFrame.graphNet.setTime(activityId, time);
            MainFrame.graphNet.setBeginTime(event.getStartDate().getTime());
            MainFrame.graphNet.setEndTime(event.getEndDate().getTime());
            
            if (caseName.equals(lastCase)) {
                MainFrame.graphNet.addActivityFre(activityId);
                MainFrame.graphNet.addActivityQueFre(
                        lastActivityId, activityId);
                long queTime = (event.getStartDate().getTime()-lastDate.getTime());
                MainFrame.graphNet.addActivityQueTime(lastActivityId, activityId, queTime);
                MainFrame.graphNet.setQueTime(lastActivityId, activityId, queTime);
                temp[activityId]++;
                tempQue[lastActivityId][activityId]++;
                lastActivityId = activityId;
                lastDate = event.getEndDate();
            } else {
                if (lastActivityId != -1) {
                    MainFrame.graphNet.addActivityQueFre(
                            lastActivityId, 1);
                    MainFrame.graphNet.addActivityQueFre(0,
                            activityId);
                    tempQue[lastActivityId][1]++;
                } else{
                    MainFrame.graphNet.addActivityQueFre(0,
                            activityId);
                }
                for (int j=0; j< MainFrame.graphNet.activityCount; j++){
                    if (temp[j] > MainFrame.graphNet.maxActivityRep[j]){
                        MainFrame.graphNet.maxActivityRep[j] = temp[j];
                    }
                    if (temp[j] > 0){
                        MainFrame.graphNet.activityCaseFre[j]++;
                    }
                    temp[j] = 0;
                    for (int k = 0; k< MainFrame.graphNet.activityCount; k++){
                        if (tempQue[j][k] >0){
                            MainFrame.graphNet.activityCaseQueFre[j][k]++;
                        }
                        tempQue[j][k] = 0;
                    }
                }
                tempQue[0][activityId]++;
                temp[activityId]++;
                lastCase = caseName;
                lastActivityId = activityId;
                lastDate = event.getEndDate();
                MainFrame.graphNet.addActivityFre(lastActivityId);
            }
        }
        tempQue[lastActivityId][1]++;
        for (int j=0; j< MainFrame.graphNet.activityCount; j++){
            if (temp[j] > MainFrame.graphNet.maxActivityRep[j]){
                MainFrame.graphNet.maxActivityRep[j] = temp[j];
            }
            if (temp[j] > 0){
                MainFrame.graphNet.activityCaseFre[j]++;
            }
            for (int k = 0; k< MainFrame.graphNet.activityCount; k++){
                if (tempQue[j][k] >0){
                    MainFrame.graphNet.activityCaseQueFre[j][k]++;
                }
            }
        }
        MainFrame.graphNet.addActivityQueFre(
                lastActivityId, 1);

        MainFrame.graphNet.beginTime /= 1000*60*60;
        MainFrame.graphNet.endTime /= 1000*60*60;
        
        for (int i = 0; i < MainFrame.graphNet.activityCount; i++)
            for (int j = 0; j < MainFrame.graphNet.activityCount; j++)
            {
                MainFrame.graphNet.activityQueFreSort.add(MainFrame.graphNet.activityQueFre[i][j]);
            }
        Collections.sort(MainFrame.graphNet.activityQueFreSort);
    }
  
    
    //更新case集
    public void setCaseCollection() {
        for (int i = 0; i < MainFrame.eventCollection.getSize(); i++) {
            Event event = MainFrame.eventCollection.getEvent(i);
            event.setFirst();
            Case newcase = new Case();
            newcase.setCase(event.getCase());
            newcase.addEvent(event);
            newcase.addActivity(event.getActivity());
            i++;
            while(true)
            {
                if(i == MainFrame.eventCollection.getSize())
                {
                    i--;
                    break;
                }
                event = MainFrame.eventCollection.getEvent(i);
                if(event.getCase().equals(newcase.getCase()))
                {
                    newcase.addEvent(event);
                    newcase.addActivity(event.getActivity());
                    i++;
                }
                else
                {
                    i--;
                    break;
                }
            }
            event = MainFrame.eventCollection.getEvent(i);
            event.setLast();
            MainFrame.caseCollection.addCase(newcase);
        }
        MainFrame.caseCollection.update();
    }
   
    
    //更新variant集
    public void setVariantCollection() {
        MainFrame.caseCollection.sortByVariant();
        for (int i = 0; i < MainFrame.caseCollection.getSize(); i++) {
            Case mycase = MainFrame.caseCollection.getCase(i);
            Variant variant = new Variant();
            variant.setVariant((MainFrame.variantCollection.getSize()+1)+"");
            variant.addCase(mycase);
            variant.setActivities(mycase.getActivities());
            i++;
            while(true)
            {
                if(i == MainFrame.caseCollection.getSize())
                {
                    i--;
                    break;
                }
                mycase = MainFrame.caseCollection.getCase(i);
                if(variant.getActivities().equals(mycase.getActivities()))
                {
                    variant.addCase(mycase);
                    i++;
                }
                else
                {
                    i--;
                    break;
                }
            }
            MainFrame.variantCollection.addVariant(variant);
        }
        MainFrame.caseCollection.sortByID();
        MainFrame.variantCollection.sortAndMerge();
    }
	
    
    //更新ActiveCasesOverTimeChart图表
    public void setActiveCasesOverTimeChart() {
        for (int i = 0; i < MainFrame.caseCollection.getSize(); i++) {
            Case mycase = MainFrame.caseCollection.getCase(i);
            MainFrame.activeCasesOverTimeChart.addActiveCasesOverTime(1, mycase.getStart());
            MainFrame.activeCasesOverTimeChart.addActiveCasesOverTime(-1, mycase.getEnd());
        }
        MainFrame.activeCasesOverTimeChart.sort();
        MainFrame.activeCasesOverTimeChart.merge();
    }
  
    
    //更新EventsOverTimeChart图表
    public void setEventsOverTimeChart() {
        for (int i = 0; i < MainFrame.eventCollection.getSize(); i++) {
            Event event = MainFrame.eventCollection.getEvent(i);
            MainFrame.eventsOverTimeChart.addEventsOverTime(1, event.getStartDate());
            MainFrame.eventsOverTimeChart.addEventsOverTime(-1, event.getEndDate());
        }
        MainFrame.eventsOverTimeChart.sort();
        MainFrame.eventsOverTimeChart.merge();
    }
   
    
    //更新EventsPerCaseChart图表
    public void setEventsPerCaseChart() {
        for (int i = 0; i < MainFrame.caseCollection.getSize(); i++) {
            Case mycase = MainFrame.caseCollection.getCase(i);
            MainFrame.eventsPerCaseChart.addCases(mycase);
        }
    }
  
    
    //更新CaseDurationChart图表
    public void setCaseDurationChart() {
        long max = 0;
        for (int i = 0; i < MainFrame.caseCollection.getSize(); i++) {
            Case mycase = MainFrame.caseCollection.getCase(i);
            if(max < mycase.getDuration())
                max = mycase.getDuration();
        }
        MainFrame.caseDurationChart.setDuration(max / 100);
        for (int i = 0; i < MainFrame.caseCollection.getSize(); i++) {
            Case mycase = MainFrame.caseCollection.getCase(i);
            MainFrame.caseDurationChart.addCases(mycase);
        }
    }
  
    
    //更新CaseUtilizationChart图表
    public void setCaseUtilizationChart() {
        for (int i = 0; i < MainFrame.caseCollection.getSize(); i++) {
            Case mycase = MainFrame.caseCollection.getCase(i);
            MainFrame.caseUtilizationChart.addCases(mycase);
        }
    }
  
    
    //更新MeanActivityDurationChart图表
    public void setMeanActivityDurationChart() {
        double max = 0;
        for (int i = 0; i < MainFrame.caseCollection.getSize(); i++) {
            Case mycase = MainFrame.caseCollection.getCase(i);
            if(max < mycase.getMeanActiveTime())
                max = mycase.getMeanActiveTime();
        }
        MainFrame.meanActivityDurationChart.setDuration(max / 100);
        for (int i = 0; i < MainFrame.caseCollection.getSize(); i++) {
            Case mycase = MainFrame.caseCollection.getCase(i);
            MainFrame.meanActivityDurationChart.addCases(mycase);
        }
    }
 
    
    //更新MeanWaitingTimeChart图表
    public void setMeanWaitingTimeChart() {
        double max = 0;
        for (int i = 0; i < MainFrame.caseCollection.getSize(); i++) {
            Case mycase = MainFrame.caseCollection.getCase(i);
            if(max < mycase.getMeanWaitingTime())
                max = mycase.getMeanWaitingTime();
        }
        MainFrame.meanWaitingTimeChart.setDuration(max / 100);
        for (int i = 0; i < MainFrame.caseCollection.getSize(); i++) {
            Case mycase = MainFrame.caseCollection.getCase(i);
            MainFrame.meanWaitingTimeChart.addCases(mycase);
        }
    }
  
    
    //更新activity集
    public void setActivityCollection() {
        MainFrame.eventCollection.sortByActivity();
        for (int i = 0; i < MainFrame.eventCollection.getSize(); i++) {
            Event event = MainFrame.eventCollection.getEvent(i);
            Activity activity = new Activity();
            activity.setActivity(event.getActivity());
            if(event.getFirst())
            {
                activity.setFirst();
            }
            if(event.getLast())
            {
                activity.setLast();
            }
            activity.addFrequency();
            activity.addDuration(event.getTime());
            i++;
            while(true)
            {
                if(i == MainFrame.eventCollection.getSize())
                {
                    i--;
                    break;
                }
                event = MainFrame.eventCollection.getEvent(i);
                if(event.getActivity().equals(activity.getActivity()))
                {
                    if(event.getFirst())
                    {
                        activity.setFirst();
                    }
                    if(event.getLast())
                    {
                        activity.setLast();
                    }
                    activity.addFrequency();
                    activity.addDuration(event.getTime());
                    i++;
                }
                else
                {
                    i--;
                    break;
                }
            }
            MainFrame.activityCollection.addActivity(activity);
        }
        for(int i = 0; i < MainFrame.activityCollection.getSize(); i++)
        {
            Activity activity = MainFrame.activityCollection.getActivity(i);
            activity.sort();
        }
        MainFrame.activityCollection.sortAndMerge();
    }

    
    //更新resource集
    public void setResourceCollection() {
        MainFrame.eventCollection.sortByResource();
        for (int i = 0; i < MainFrame.eventCollection.getSize(); i++) {
            Event event = MainFrame.eventCollection.getEvent(i);
            Resource resource = new Resource();
            resource.setResource(event.getResource());
            if(event.getFirst())
            {
                resource.setFirst();
            }
            if(event.getLast())
            {
                resource.setLast();
            }
            resource.addFrequency();
            resource.addDuration(event.getTime());
            i++;
            while(true)
            {
                if(i == MainFrame.eventCollection.getSize())
                {
                    i--;
                    break;
                }
                event = MainFrame.eventCollection.getEvent(i);
                if(event.getResource().equals(resource.getResource()))
                {
                    if(event.getFirst())
                    {
                        resource.setFirst();
                    }
                    if(event.getLast())
                    {
                        resource.setLast();
                    }
                    resource.addFrequency();
                    resource.addDuration(event.getTime());
                    i++;
                }
                else
                {
                    i--;
                    break;
                }
            }
            MainFrame.resourceCollection.addResource(resource);
        }
        for(int i = 0; i < MainFrame.resourceCollection.getSize(); i++)
        {
            Resource resource = MainFrame.resourceCollection.getResource(i);
            resource.sort();
        }
        MainFrame.resourceCollection.sortAndMerge();
    }

}
