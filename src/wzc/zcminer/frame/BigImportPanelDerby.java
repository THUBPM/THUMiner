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
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import wzc.zcminer.global.BigAnimationDerby.Frame;
import wzc.zcminer.global.BigCase;
import wzc.zcminer.global.BigCaseDerby;
import wzc.zcminer.global.BigEvent;
import wzc.zcminer.global.BigEventDerby;
import wzc.zcminer.global.BigVariant;
import wzc.zcminer.global.BigVariantDerby;
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
public class BigImportPanelDerby extends JPanel{    
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
	static BigFileButtonDerby bigFileButton;
	static JTextField timeText; //时间模式输入
	static JButton okButton;
	final static int MAXHEADINDEX = 20;
	int columnCount;
	int[] headIndex;
	String[] headlines;
	String [][] tableData;
	
	int maxLine;

	public BigImportPanelDerby(String fileName, String separator, boolean hasTableHead, String encodingText) {
		// TODO Auto-generated constructor stub
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
			MainFrame.derbyConnection = DriverManager.getConnection("jdbc:derby:" + "data_tmp" + ";create=true");
			
			try {
				String sql = "create table eventCollection(UUID varchar(255), caseID varchar(255), activity varchar(255), resource varchar(255), startDate bigint, endDate bigint, firstInCase boolean, lastInCase boolean)";
	         	MainFrame.derbyStatement = MainFrame.derbyConnection.prepareStatement(sql);
	         	MainFrame.derbyStatement.executeUpdate();
		        if (MainFrame.derbyStatement != null)
		        	MainFrame.derbyStatement.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			
			try {
				String sql = "create table caseCollection(caseID varchar(255), eventsCount int, startDate bigint, endDate bigint, duration bigint, active bigint, activitiesList varchar(32672), eventList varchar(32672), variantID int)";
	         	MainFrame.derbyStatement = MainFrame.derbyConnection.prepareStatement(sql);
	         	MainFrame.derbyStatement.executeUpdate();
		        if (MainFrame.derbyStatement != null)
		        	MainFrame.derbyStatement.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			
			try {
				String sql = "create table variantCollection(variantID int, totalDuration bigint, medianDuration bigint, size bigint, activitiesList varchar(32672))";
	         	MainFrame.derbyStatement = MainFrame.derbyConnection.prepareStatement(sql);
	         	MainFrame.derbyStatement.executeUpdate();
		        if (MainFrame.derbyStatement != null)
		        	MainFrame.derbyStatement.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			
			try {
				String sql = "create table animation(frame bigint, nextFrame bigint, activityFre varchar(32672), activityQueFre varchar(32672))";
	         	MainFrame.derbyStatement = MainFrame.derbyConnection.prepareStatement(sql);
	         	MainFrame.derbyStatement.executeUpdate();
		        if (MainFrame.derbyStatement != null)
		        	MainFrame.derbyStatement.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			
			try {
				String sql = "create index eventCollection_activity on eventCollection(activity)";
	         	MainFrame.derbyStatement = MainFrame.derbyConnection.prepareStatement(sql);
	         	MainFrame.derbyStatement.executeUpdate();
		        if (MainFrame.derbyStatement != null)
		        	MainFrame.derbyStatement.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			
			try {
				String sql = "create index eventCollection_resource on eventCollection(resource)";
	         	MainFrame.derbyStatement = MainFrame.derbyConnection.prepareStatement(sql);
	         	MainFrame.derbyStatement.executeUpdate();
		        if (MainFrame.derbyStatement != null)
		        	MainFrame.derbyStatement.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			
			try {
				String sql = "create index eventCollection_caseID on eventCollection(caseID, startDate, endDate)";
	         	MainFrame.derbyStatement = MainFrame.derbyConnection.prepareStatement(sql);
	         	MainFrame.derbyStatement.executeUpdate();
		        if (MainFrame.derbyStatement != null)
		        	MainFrame.derbyStatement.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			
			try {
				String sql = "create unique index eventCollection_uuid on eventCollection(UUID)";
	         	MainFrame.derbyStatement = MainFrame.derbyConnection.prepareStatement(sql);
	         	MainFrame.derbyStatement.executeUpdate();
		        if (MainFrame.derbyStatement != null)
		        	MainFrame.derbyStatement.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			
			try {
				String sql = "create unique index caseCollection_caseID on caseCollection(caseID)";
	         	MainFrame.derbyStatement = MainFrame.derbyConnection.prepareStatement(sql);
	         	MainFrame.derbyStatement.executeUpdate();
		        if (MainFrame.derbyStatement != null)
		        	MainFrame.derbyStatement.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			
			try {
				String sql = "create index caseCollection_duration on caseCollection(duration)";
	         	MainFrame.derbyStatement = MainFrame.derbyConnection.prepareStatement(sql);
	         	MainFrame.derbyStatement.executeUpdate();
		        if (MainFrame.derbyStatement != null)
		        	MainFrame.derbyStatement.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			
			try {
				String sql = "create index caseCollection_activitiesList on caseCollection(activitiesList, duration)";
	         	MainFrame.derbyStatement = MainFrame.derbyConnection.prepareStatement(sql);
	         	MainFrame.derbyStatement.executeUpdate();
		        if (MainFrame.derbyStatement != null)
		        	MainFrame.derbyStatement.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			
			try {
				String sql = "create unique index variantCollection_variantID on variantCollection(variantID)";
	         	MainFrame.derbyStatement = MainFrame.derbyConnection.prepareStatement(sql);
	         	MainFrame.derbyStatement.executeUpdate();
		        if (MainFrame.derbyStatement != null)
		        	MainFrame.derbyStatement.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			
			try {
				String sql = "create unique index animation_frame on animation(frame)";
	         	MainFrame.derbyStatement = MainFrame.derbyConnection.prepareStatement(sql);
	         	MainFrame.derbyStatement.executeUpdate();
		        if (MainFrame.derbyStatement != null)
		        	MainFrame.derbyStatement.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			
			//csv, txt数据导入
			File file = new File(fileName);
			InputStreamReader read = null;
			BufferedReader reader = null;
			try{
				read = new InputStreamReader(new FileInputStream(file), encodingText);
				reader = new BufferedReader(read);
				String tempString = null;
				
				tempString = reader.readLine();
				columnCount = tempString.split(separator).length;
				if(reader!=null)
				{
					try{
						reader.close();
						read.close();
					}catch(IOException e)
					{
						e.printStackTrace();
					}
				}
				read = new InputStreamReader(new FileInputStream(file), encodingText);
				reader = new BufferedReader(read, 5*1024*1024);
				
				if(hasTableHead)
				{
					tempString = reader.readLine();
					headlines = tempString.split(separator);
				}
				else
				{
					headlines = new String[columnCount];
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
				
				tableData = new String[1000][columnCount];
				for(int i = 0; i < 1000 && (tempString = reader.readLine()) != null; i++)
				{
					tableData[i] = tempString.split(separator);
				}
				reader.close();
				read.close();
			}catch(IOException e){
				e.printStackTrace();
			}finally{
				if(reader!=null)
				{
					try{
						reader.close();
						read.close();
					}catch(IOException e)
					{
						e.printStackTrace();
					}
				}
			}

			setPanel("file_timestamp", fileName, separator, hasTableHead, encodingText);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void setPanel(String timestamp, String fileName, String separator, boolean hasTableHead, String encodingText) {
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
		bigFileButton = new BigFileButtonDerby();

        radioPanel.add(fileButton);
        radioPanel.add(databaseButton);
		radioPanel.add(bigFileButton);
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
				long start=System.currentTimeMillis();// 当前时间对应的毫秒数
				
				long now=System.currentTimeMillis();
				System.out.println("now："+(now)+"毫秒");
			    setEventCollection(fileName, separator, hasTableHead, encodingText);
			    now=System.currentTimeMillis();
				System.out.println("eventCollection："+(now)+"毫秒");
			    setGraphNet();
			    now=System.currentTimeMillis();
				System.out.println("GraphNet"+(now)+"毫秒");
			    setAnimation();
			    now=System.currentTimeMillis();
				System.out.println("Animation"+(now)+"毫秒");
			    setCaseCollection();
			    now=System.currentTimeMillis();
				System.out.println("caseCollection："+(now)+"毫秒");
			    setVariantCollection();
			    now=System.currentTimeMillis();
				System.out.println("variantCollection："+(now)+"毫秒");
			    setEventsOverTimeChart();
			    now=System.currentTimeMillis();
				System.out.println("setEventsOverTimeChart："+(now)+"毫秒");
			    setActiveCasesOverTimeChart();
			    now=System.currentTimeMillis();
				System.out.println("setActiveCasesOverTimeChart："+(now)+"毫秒");
			    setEventsPerCaseChart();
			    now=System.currentTimeMillis();
				System.out.println("setEventsPerCaseChart："+(now)+"毫秒");
			    setCaseDurationChart();
			    now=System.currentTimeMillis();
				System.out.println("setCaseDurationChart："+(now)+"毫秒");
			    setCaseUtilizationChart();
			    now=System.currentTimeMillis();
				System.out.println("setCaseUtilizationChart："+(now)+"毫秒");
			    setMeanActivityDurationChart();
			    now=System.currentTimeMillis();
				System.out.println("setMeanActivityDurationChart："+(now)+"毫秒");
			    setMeanWaitingTimeChart();
			    now=System.currentTimeMillis();
				System.out.println("setMeanWaitingTimeChart："+(now)+"毫秒");
			    setActivityCollection();
			    now=System.currentTimeMillis();
				System.out.println("setActivityCollection："+(now)+"毫秒");
			    setResourceCollection();
			    now=System.currentTimeMillis();
				System.out.println("setResourceCollection："+(now)+"毫秒");
				
				MainFrame.mainFrame.getContentPane().removeAll();
				System.gc();
				BigMapPanelDerby mapPanel = new BigMapPanelDerby();
				MainFrame.mainFrame.setContentPane(mapPanel);
				MainFrame.mainFrame.setVisible(true);
				System.gc();
				long end=System.currentTimeMillis();
				System.out.println("all 总耗时为："+(end-start)+"毫秒");
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
    public void setEventCollection(String fileName, String separator, boolean hasTableHead, String encodingText) {
    	try {
    		int icount = 0;
    		long now;
    		MainFrame.derbyConnection.setAutoCommit(false);
    		String sql = "insert into eventCollection values(?, ?, ?, ?, ?, ?, false, false)";
			PreparedStatement derbyStatement = MainFrame.derbyConnection.prepareStatement(sql);
			File file = new File(fileName);
			InputStreamReader read = null;
			BufferedReader reader = null;
			try{
				read = new InputStreamReader(new FileInputStream(file), encodingText);
				reader = new BufferedReader(read);
				String tempString = null;
				
				if(hasTableHead)
				{
					tempString = reader.readLine();
				}
				
				String[] tempStringList;
				while((tempString = reader.readLine()) != null)
				{
					icount++;
					if(icount % 10000 == 0){
						derbyStatement.executeBatch();
						MainFrame.derbyConnection.commit();
						derbyStatement.clearBatch();
						now=System.currentTimeMillis();
						System.out.println("eventCollection"+(icount) +"："+(now)+"毫秒");
					}
					tempStringList = tempString.split(separator);
					BigEventDerby event = new BigEventDerby();
					for (int j = 0; j < headIndex.length; j++) {
		                switch (headIndex[j]) {
		                case 2:
		                    event.setCase(tempStringList[j]);
		                    break;
		                case 3:
		                    event.setActivity(tempStringList[j]);
		                    if (!MainFrame.graphNet
		                            .activityExist(tempStringList[j])) {
		                        MainFrame.graphNet
		                                .addActivity(tempStringList[j]);
		                    }
		                    if (!MainFrame.bigAnimationDerby
		                            .activityExist(tempStringList[j])) {
		                        MainFrame.bigAnimationDerby
		                                .addActivity(tempStringList[j]);
		                    }
		                    break;
		                case 4:
		                    event.setDate(tempStringList[j], timeText.getText());
		                    break;
		                case 5:
		                    event.setResource(tempStringList[j]);
		                    break;
		                default:
		                    break;
		                }
		            }
					MainFrame.bigEventCollectionDerby.addEvent(event, derbyStatement);
				}
				reader.close();
				read.close();
			}catch(IOException e){
				e.printStackTrace();
			}finally{
				if(reader!=null)
				{
					try{
						reader.close();
						read.close();
					}catch(IOException e)
					{
						e.printStackTrace();
					}
				}
			}
			derbyStatement.executeBatch();
			MainFrame.derbyConnection.commit();
	        if (derbyStatement != null)
	        	derbyStatement.close();
    		MainFrame.derbyConnection.setAutoCommit(true);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
    }   
    
    //更新graph
    public void setGraphNet() {
		int icount = 0;
		long now;
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
        BigEventDerby event = MainFrame.bigEventCollectionDerby.getFirstEventID();
        while (event != null) {
			icount++;
			if(icount % 10000 == 0){
				now=System.currentTimeMillis();
				System.out.println("setGraphNet"+(icount) +"："+(now)+"毫秒");
			}
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
            
            event = MainFrame.bigEventCollectionDerby.getNextEventID();
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
  
    public void setAnimation() {
    	int icount = 0;
		long now;
		try{
			MainFrame.derbyConnection.setAutoCommit(false);
			Statement derbyStatement = MainFrame.derbyConnection.createStatement();
			HashMap<Long, Frame> hashMap = new HashMap<Long, Frame>();;
	        MainFrame.bigAnimationDerby.setMemory();
			if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
			{
		        MainFrame.bigAnimationDerby.activityNames[0] = "begin";
		        MainFrame.bigAnimationDerby.activityNames[1] = "end";
			}
			else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
			{
		        MainFrame.bigAnimationDerby.activityNames[0] = "开始";
		        MainFrame.bigAnimationDerby.activityNames[1] = "结束";
		    }
	        int lastActivityId = -1;
	        Frame newFrame = null;
	        //按顺序遍历得出case，并运算各个参数
	        BigEventDerby event = MainFrame.bigEventCollectionDerby.getFirstEventID();
	        BigEventDerby nextEvent = null;
	        while (event != null) {
				icount++;
				if(icount % 1000 == 0){
					Iterator<Entry<Long, Frame>>  iter = hashMap.entrySet().iterator();
					while (iter.hasNext()) {
						Entry<Long, Frame> entry = (Entry<Long, Frame>) iter.next();
						long key = entry.getKey();
						Frame val = entry.getValue();
						if(key != val.getFrame())
							System.out.println(key);
						MainFrame.bigAnimationDerby.addFrame(val, derbyStatement);
					}
					derbyStatement.executeBatch();
					MainFrame.derbyConnection.commit();
					derbyStatement.clearBatch();
					hashMap.clear();
					now=System.currentTimeMillis();
					System.out.println("setAnimation"+(icount) +"："+(now)+"毫秒");
				}
	            String activityName = event.getActivity();
	            int activityId = MainFrame.bigAnimationDerby.getActivityId(activityName);
	            MainFrame.bigAnimationDerby.setActivityName(activityId, activityName);
	            MainFrame.bigAnimationDerby.setBeginTime(event.getStartDate().getTime());
	            MainFrame.bigAnimationDerby.setEndTime(event.getEndDate().getTime());
	            
	        	if(lastActivityId == -1){
	        		newFrame = MainFrame.bigAnimationDerby.newFrame();
	        		newFrame.setFrame(event.getStartDate().getTime());
	            	newFrame.incActivityFre(activityId);
	            	hashMap.put(event.getStartDate().getTime(), newFrame);
	        		//MainFrame.bigAnimationDerby.addFrame(newFrame);
	        		lastActivityId = 0;
	        	}
	            
	        	nextEvent = MainFrame.bigEventCollectionDerby.getNextEventID();
	        	
	        	if(nextEvent == null){
	        		if(hashMap.containsKey(event.getEndDate().getTime())){
	        			newFrame = hashMap.get(event.getEndDate().getTime());
	                	newFrame.decActivityFre(activityId);
	        		}else if(MainFrame.bigAnimationDerby.exists(event.getEndDate().getTime())){
	            		newFrame = MainFrame.bigAnimationDerby.newFrame(event.getEndDate().getTime());
	                	newFrame.decActivityFre(activityId);
	                	hashMap.put(event.getEndDate().getTime(), newFrame);
	//                	newFrame.update();
	            	}else{
	            		newFrame = MainFrame.bigAnimationDerby.newFrame();
	            		newFrame.setFrame(event.getEndDate().getTime());
	                	newFrame.decActivityFre(activityId);
	                	hashMap.put(event.getEndDate().getTime(), newFrame);
	//            		MainFrame.bigAnimationDerby.addFrame(newFrame);
	            	}
	            	break;
	        	}
	        	
	            String nextActivityName = nextEvent.getActivity();
	            int nextActivityId = MainFrame.bigAnimationDerby.getActivityId(nextActivityName);
	            MainFrame.bigAnimationDerby.setActivityName(nextActivityId, nextActivityName);
	            MainFrame.bigAnimationDerby.setBeginTime(nextEvent.getStartDate().getTime());
	            MainFrame.bigAnimationDerby.setEndTime(nextEvent.getEndDate().getTime());
	        	
	        	if(nextEvent.getCase().equals(event.getCase())){
	        		if(hashMap.containsKey(event.getEndDate().getTime())){
	        			newFrame = hashMap.get(event.getEndDate().getTime());
	                	newFrame.decActivityFre(activityId);
	                	newFrame.incActivityQueFre(activityId, nextActivityId);
	        		}else if(MainFrame.bigAnimationDerby.exists(event.getEndDate().getTime())){
	            		newFrame = MainFrame.bigAnimationDerby.newFrame(event.getEndDate().getTime());
	                	newFrame.decActivityFre(activityId);
	                	newFrame.incActivityQueFre(activityId, nextActivityId);
	                	hashMap.put(event.getEndDate().getTime(), newFrame);
	//                	newFrame.update();
	            	}else{
	            		newFrame = MainFrame.bigAnimationDerby.newFrame();
	            		newFrame.setFrame(event.getEndDate().getTime());
	                	newFrame.decActivityFre(activityId);
	                	newFrame.incActivityQueFre(activityId, nextActivityId);
	                	hashMap.put(event.getEndDate().getTime(), newFrame);
	//            		MainFrame.bigAnimationDerby.addFrame(newFrame);
	            	}
	            	
	        		if(hashMap.containsKey(nextEvent.getStartDate().getTime())){
	        			newFrame = hashMap.get(nextEvent.getStartDate().getTime());
	                	newFrame.incActivityFre(nextActivityId);
	                	newFrame.decActivityQueFre(activityId, nextActivityId);
	        		}else if(MainFrame.bigAnimationDerby.exists(nextEvent.getStartDate().getTime())){
	            		newFrame = MainFrame.bigAnimationDerby.newFrame(nextEvent.getStartDate().getTime());
	                	newFrame.incActivityFre(nextActivityId);
	                	newFrame.decActivityQueFre(activityId, nextActivityId);
	                	hashMap.put(nextEvent.getStartDate().getTime(), newFrame);
	//                	newFrame.update();
	            	}else{
	            		newFrame = MainFrame.bigAnimationDerby.newFrame();
	            		newFrame.setFrame(nextEvent.getStartDate().getTime());
	                	newFrame.incActivityFre(nextActivityId);
	                	newFrame.decActivityQueFre(activityId, nextActivityId);
	                	hashMap.put(nextEvent.getStartDate().getTime(), newFrame);
	//            		MainFrame.bigAnimationDerby.addFrame(newFrame);
	            	}
	        	}else{
	        		if(hashMap.containsKey(event.getEndDate().getTime())){
	        			newFrame = hashMap.get(event.getEndDate().getTime());
	                	newFrame.decActivityFre(activityId);
	        		}else if(MainFrame.bigAnimationDerby.exists(event.getEndDate().getTime())){
	            		newFrame = MainFrame.bigAnimationDerby.newFrame(event.getEndDate().getTime());
	                	newFrame.decActivityFre(activityId);
	                	hashMap.put(event.getEndDate().getTime(), newFrame);
	//                	newFrame.update();
	            	}else{
	            		newFrame = MainFrame.bigAnimationDerby.newFrame();
	            		newFrame.setFrame(event.getEndDate().getTime());
	                	newFrame.decActivityFre(activityId);
	                	hashMap.put(event.getEndDate().getTime(), newFrame);
	//            		MainFrame.bigAnimationDerby.addFrame(newFrame);
	            	}
	            	
	        		if(hashMap.containsKey(nextEvent.getStartDate().getTime())){
	        			newFrame = hashMap.get(nextEvent.getStartDate().getTime());
	                	newFrame.incActivityFre(nextActivityId);
	        		}else if(MainFrame.bigAnimationDerby.exists(nextEvent.getStartDate().getTime())){
	            		newFrame = MainFrame.bigAnimationDerby.newFrame(nextEvent.getStartDate().getTime());
	                	newFrame.incActivityFre(nextActivityId);
	                	hashMap.put(nextEvent.getStartDate().getTime(), newFrame);
	//                	newFrame.update();
	            	}else{
	            		newFrame = MainFrame.bigAnimationDerby.newFrame();
	            		newFrame.setFrame(nextEvent.getStartDate().getTime());
	                	newFrame.incActivityFre(nextActivityId);
	                	hashMap.put(nextEvent.getStartDate().getTime(), newFrame);
	//            		MainFrame.bigAnimationDerby.addFrame(newFrame);
	            	}
	        	}
	    		
		        event = nextEvent;
		    }
			Iterator<Entry<Long, Frame>>  iter = hashMap.entrySet().iterator();
			while (iter.hasNext()) {
				Entry<Long, Frame> entry = (Entry<Long, Frame>) iter.next();
//				long key = entry.getKey();
				Frame val = entry.getValue();
				MainFrame.bigAnimationDerby.addFrame(val, derbyStatement);
			}
			derbyStatement.executeBatch();
			MainFrame.derbyConnection.commit();
			hashMap.clear();
	        if (derbyStatement != null)
	        	derbyStatement.close();
    		MainFrame.derbyConnection.setAutoCommit(true);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

        MainFrame.bigAnimationDerby.merge();
    }
    
    //更新case集
    public void setCaseCollection() {
		int icount = 0;
		long now;
		try{
			MainFrame.derbyConnection.setAutoCommit(false);
			String sql = "insert into caseCollection values(?, ?, ?, ?, ?, ?, ?, ?, 0)";
			PreparedStatement derbyStatement = MainFrame.derbyConnection.prepareStatement(sql);
			String fisrtSql = "update eventCollection set firstInCase = true where UUID = ?";
			PreparedStatement fisrtDerbyStatement = MainFrame.derbyConnection.prepareStatement(fisrtSql);
			String lastSql = "update eventCollection set lastInCase = true where UUID = ?";
			PreparedStatement lastDerbyStatement = MainFrame.derbyConnection.prepareStatement(lastSql);
	        BigEventDerby event = MainFrame.bigEventCollectionDerby.getFirstEventID();
	        if (event == null) return;
	        BigEventDerby nextEvent = MainFrame.bigEventCollectionDerby.getNextEventID();
	        while (event != null) {
				icount++;
				if(icount % 10000 == 0){
					derbyStatement.executeBatch();
					fisrtDerbyStatement.executeBatch();
					lastDerbyStatement.executeBatch();
					MainFrame.derbyConnection.commit();
					derbyStatement.clearBatch();
					fisrtDerbyStatement.clearBatch();
					lastDerbyStatement.clearBatch();
					now=System.currentTimeMillis();
					System.out.println("setCaseCollection"+(icount) +"："+(now)+"毫秒");
				}
	            event.setFirst(fisrtDerbyStatement);
	            BigCaseDerby newcase = new BigCaseDerby();
	            newcase.setCase(event.getCase());
	            newcase.addEvent(event);
	            newcase.addActivity(event.getActivity());
	            while(true){
	            	if(nextEvent == null){
	                    event.setLast(lastDerbyStatement);
	                    MainFrame.bigCaseCollectionDerby.addCase(newcase, derbyStatement);
	        			derbyStatement.executeBatch();
	        			fisrtDerbyStatement.executeBatch();
	        			lastDerbyStatement.executeBatch();
	        			MainFrame.derbyConnection.commit();
	        	        if (derbyStatement != null)
	        	        	derbyStatement.close();
	        	        if (fisrtDerbyStatement != null)
	        	        	fisrtDerbyStatement.close();
	        	        if (lastDerbyStatement != null)
	        	        	lastDerbyStatement.close();
	            		MainFrame.derbyConnection.setAutoCommit(true);
	                    
	                    MainFrame.bigCaseCollectionDerby.update();
	                    return;
	            	}
	                if(nextEvent.getCase().equals(newcase.getCase())){
	        			icount++;
	        			if(icount % 10000 == 0){
	    					derbyStatement.executeBatch();
	    					fisrtDerbyStatement.executeBatch();
	    					lastDerbyStatement.executeBatch();
	    					MainFrame.derbyConnection.commit();
	    					derbyStatement.clearBatch();
	    					fisrtDerbyStatement.clearBatch();
	    					lastDerbyStatement.clearBatch();
	        				now=System.currentTimeMillis();
	        				System.out.println("setCaseCollection"+(icount) +"："+(now)+"毫秒");
	        			}
	                	event = nextEvent;
	                	nextEvent = MainFrame.bigEventCollectionDerby.getNextEventID();
	                    newcase.addEvent(event);
	                    newcase.addActivity(event.getActivity());
	                }else{
	                	break;
	                }
	            }
	            event.setLast(lastDerbyStatement);
	            MainFrame.bigCaseCollectionDerby.addCase(newcase, derbyStatement);
	        	event = nextEvent;
	        	nextEvent = MainFrame.bigEventCollectionDerby.getNextEventID();
	        }
		} catch (Exception ex) {
			ex.printStackTrace();
		}
    }
       
    //更新variant集
    public void setVariantCollection() {
		int icount = 0;
		long now;
    	try {
    		MainFrame.derbyConnection.setAutoCommit(false);
    		String sql = "insert into variantCollection values(?, ?, ?, ?, ?)";
    		PreparedStatement derbyStatement = MainFrame.derbyConnection.prepareStatement(sql);
    		String caseSql = "update caseCollection set variantID = ? where caseID = ?";
    		PreparedStatement caseDerbyStatement = MainFrame.derbyConnection.prepareStatement(caseSql);
    		BigCaseDerby mycase = MainFrame.bigCaseCollectionDerby.getFirstCaseVariant();
	        if (mycase == null) return;
	        BigCaseDerby nextCase = MainFrame.bigCaseCollectionDerby.getNextCaseVariant();
	        PreparedStatement preparedStatement = null;
	        ResultSet resultSet = null;
			String medianSql = "select * from caseCollection order by activitiesList, duration";
			preparedStatement = MainFrame.derbyConnection.prepareStatement(medianSql);
			resultSet = preparedStatement.executeQuery();
	        BigCaseDerby medianCase = MainFrame.bigCaseCollectionDerby.getFirstCaseVariant(preparedStatement, resultSet);
	        int medianCount = 0;
	        int caseCount = 0;
	        while(mycase != null){
				icount++;
				if(icount % 10000 == 0){
					derbyStatement.executeBatch();
					caseDerbyStatement.executeBatch();
					MainFrame.derbyConnection.commit();
					derbyStatement.clearBatch();
					caseDerbyStatement.clearBatch();
					now=System.currentTimeMillis();
					System.out.println("setVariantCollection"+(icount) +"："+(now)+"毫秒");
				}
	            BigVariantDerby variant = new BigVariantDerby();
	            variant.setVariant(MainFrame.bigVariantCollectionDerby.getSize()+1);
	            variant.addCase(mycase, caseDerbyStatement);
	            variant.setActivities(mycase.getActivities());
	            int count = 1;
	            while(true)
	            {
	            	if(nextCase == null){
	                	if(variant.getSize() % 2 == 0){
	                		variant.setMedianDuration(medianCase.getDuration());
	                		medianCase = MainFrame.bigCaseCollectionDerby.getNextCaseVariant(preparedStatement, resultSet);
	                		variant.setMedianDuration((variant.getMedianDuration() + medianCase.getDuration()) / 2);
	                	}else{
	                		variant.setMedianDuration(medianCase.getDuration());
	                	}
	                    MainFrame.bigVariantCollectionDerby.addVariant(variant, derbyStatement);
	                    while(medianCase != null)
	                    	medianCase = MainFrame.bigCaseCollectionDerby.getNextCaseVariant(preparedStatement, resultSet);
	        			derbyStatement.executeBatch();
	        			caseDerbyStatement.executeBatch();
	        			MainFrame.derbyConnection.commit();
	        	        if (derbyStatement != null)
	        	        	derbyStatement.close();
	        	        if (caseDerbyStatement != null)
	        	        	caseDerbyStatement.close();
	            		MainFrame.derbyConnection.setAutoCommit(true);
	                    return;
	            	}
	                if(variant.getActivities().equals(nextCase.getActivities())){
	    				icount++;
	    				if(icount % 10000 == 0){
	    					derbyStatement.executeBatch();
	    					caseDerbyStatement.executeBatch();
	    					MainFrame.derbyConnection.commit();
	    					derbyStatement.clearBatch();
	    					caseDerbyStatement.clearBatch();
	    					now=System.currentTimeMillis();
	    					System.out.println("setVariantCollection"+(icount) +"："+(now)+"毫秒");
	    				}
	                	mycase = nextCase;
	                	nextCase = MainFrame.bigCaseCollectionDerby.getNextCaseVariant();
	                	caseCount++;
	                	variant.addCase(mycase, caseDerbyStatement);
	                	if(variant.getSize() > count * 2){
	                		medianCase = MainFrame.bigCaseCollectionDerby.getNextCaseVariant(preparedStatement, resultSet);
	                		medianCount++;
	                		count++;
	                	}
	                }else{
	                	break;
	                }
	            }
	        	if(variant.getSize() % 2 == 0){
	        		variant.setMedianDuration(medianCase.getDuration());
	        		medianCase = MainFrame.bigCaseCollectionDerby.getNextCaseVariant(preparedStatement, resultSet);
	        		medianCount++;
	        		variant.setMedianDuration((variant.getMedianDuration() + medianCase.getDuration()) / 2);
	        	}else{
	        		variant.setMedianDuration(medianCase.getDuration());
	        	}
	            MainFrame.bigVariantCollectionDerby.addVariant(variant, derbyStatement);
	        	mycase = nextCase;
	        	nextCase = MainFrame.bigCaseCollectionDerby.getNextCaseVariant();
	        	caseCount++;
	            while(caseCount > medianCount){
	            	medianCase = MainFrame.bigCaseCollectionDerby.getNextCaseVariant(preparedStatement, resultSet);
	            	medianCount++;
	            }
	        }
    	} catch (Exception ex) {
			ex.printStackTrace();
		}
    }
	
    
    //更新ActiveCasesOverTimeChart图表
    public void setActiveCasesOverTimeChart() {
		int icount = 0;
		long now;
        long start = 0;
        long end = 0;
        BigCaseDerby mycase = MainFrame.bigCaseCollectionDerby.getFirstID();
        while(mycase != null){
			icount++;
			if(icount % 10000 == 0){
				now=System.currentTimeMillis();
				System.out.println("setActiveCasesOverTimeChart"+(icount) +"："+(now)+"毫秒");
			}
            if(start > mycase.getStart().getTime() || start == 0)
            	start = mycase.getStart().getTime();
            if(end < mycase.getEnd().getTime())
            	end = mycase.getEnd().getTime();
        	mycase = MainFrame.bigCaseCollectionDerby.getNextID();
        }
        MainFrame.activeCasesOverTimeChart.init(start, end);
        mycase = MainFrame.bigCaseCollectionDerby.getFirstID();
        icount = 0;
        while(mycase != null){
			icount++;
			if(icount % 10000 == 0){
				now=System.currentTimeMillis();
				System.out.println("2setActiveCasesOverTimeChart"+(icount) +"："+(now)+"毫秒");
			}
            MainFrame.activeCasesOverTimeChart.addActiveCasesOverTime(1, mycase.getStart());
            MainFrame.activeCasesOverTimeChart.addActiveCasesOverTime(-1, mycase.getEnd());
        	mycase = MainFrame.bigCaseCollectionDerby.getNextID();
        }
    }
  
    
    //更新EventsOverTimeChart图表
    public void setEventsOverTimeChart() {
		int icount = 0;
		long now;
        long start = 0;
        long end = 0;
        BigEventDerby event = MainFrame.bigEventCollectionDerby.getFirst();
        while(event != null){
			icount++;
			if(icount % 10000 == 0){
				now=System.currentTimeMillis();
				System.out.println("setEventsOverTimeChart"+(icount) +"："+(now)+"毫秒");
			}
            if(start > event.getStartDate().getTime() || start == 0)
            	start = event.getStartDate().getTime();
            if(end < event.getEndDate().getTime())
            	end = event.getEndDate().getTime();
        	event = MainFrame.bigEventCollectionDerby.getNext();
        }
        MainFrame.eventsOverTimeChart.init(start, end);
        event = MainFrame.bigEventCollectionDerby.getFirst();
        icount=0;
        while(event != null){
			icount++;
			if(icount % 10000 == 0){
				now=System.currentTimeMillis();
				System.out.println("2setEventsOverTimeChart"+(icount) +"："+(now)+"毫秒");
			}
            MainFrame.eventsOverTimeChart.addEventsOverTime(1, event.getStartDate());
            MainFrame.eventsOverTimeChart.addEventsOverTime(-1, event.getEndDate());
        	event = MainFrame.bigEventCollectionDerby.getNext();
        }
    }
   
    
    //更新EventsPerCaseChart图表
    public void setEventsPerCaseChart() {
		int icount = 0;
		long now;
        BigCaseDerby mycase = MainFrame.bigCaseCollectionDerby.getFirstID();
        while(mycase != null){
			icount++;
			if(icount % 10000 == 0){
				now=System.currentTimeMillis();
				System.out.println("setEventsPerCaseChart"+(icount) +"："+(now)+"毫秒");
			}
            MainFrame.eventsPerCaseChart.addCases(mycase);
        	mycase = MainFrame.bigCaseCollectionDerby.getNextID();
        }
    }
  
    
    //更新CaseDurationChart图表
    public void setCaseDurationChart() {
		int icount = 0;
		long now;
        long max = 0;
        BigCaseDerby mycase = MainFrame.bigCaseCollectionDerby.getFirstID();
        while(mycase != null){
			icount++;
			if(icount % 10000 == 0){
				now=System.currentTimeMillis();
				System.out.println("setCaseDurationChart"+(icount) +"："+(now)+"毫秒");
			}
            if(max < mycase.getDuration())
                max = mycase.getDuration();
        	mycase = MainFrame.bigCaseCollectionDerby.getNextID();
        }
        MainFrame.caseDurationChart.setDuration(max / 100);
        mycase = MainFrame.bigCaseCollectionDerby.getFirstID();
        icount=0;
        while(mycase != null){
			icount++;
			if(icount % 10000 == 0){
				now=System.currentTimeMillis();
				System.out.println("2setCaseDurationChart"+(icount) +"："+(now)+"毫秒");
			}
            MainFrame.caseDurationChart.addCases(mycase);
        	mycase = MainFrame.bigCaseCollectionDerby.getNextID();
        }
    }
  
    
    //更新CaseUtilizationChart图表
    public void setCaseUtilizationChart() {
		int icount = 0;
		long now;
        BigCaseDerby mycase = MainFrame.bigCaseCollectionDerby.getFirstID();
        while(mycase != null){
			icount++;
			if(icount % 10000 == 0){
				now=System.currentTimeMillis();
				System.out.println("setCaseUtilizationChart"+(icount) +"："+(now)+"毫秒");
			}
            MainFrame.caseUtilizationChart.addCases(mycase);
           	mycase = MainFrame.bigCaseCollectionDerby.getNextID();
        }
    }
  
    
    //更新MeanActivityDurationChart图表
    public void setMeanActivityDurationChart() {
		int icount = 0;
		long now;
        double max = 0;
        BigCaseDerby mycase = MainFrame.bigCaseCollectionDerby.getFirstID();
        while(mycase != null){
			icount++;
			if(icount % 10000 == 0){
				now=System.currentTimeMillis();
				System.out.println("setMeanActivityDurationChart"+(icount) +"："+(now)+"毫秒");
			}
            if(max < mycase.getMeanActiveTime())
                max = mycase.getMeanActiveTime();
           	mycase = MainFrame.bigCaseCollectionDerby.getNextID();
        }
        MainFrame.meanActivityDurationChart.setDuration(max / 100);
        mycase = MainFrame.bigCaseCollectionDerby.getFirstID();
        icount = 0;
        while(mycase != null){
			icount++;
			if(icount % 10000 == 0){
				now=System.currentTimeMillis();
				System.out.println("2setMeanActivityDurationChart"+(icount) +"："+(now)+"毫秒");
			}
            MainFrame.meanActivityDurationChart.addCases(mycase);
           	mycase = MainFrame.bigCaseCollectionDerby.getNextID();
        }
    }
 
    
    //更新MeanWaitingTimeChart图表
    public void setMeanWaitingTimeChart() {
		int icount = 0;
		long now;
        double max = 0;
        BigCaseDerby mycase = MainFrame.bigCaseCollectionDerby.getFirstID();
        while(mycase != null){
			icount++;
			if(icount % 10000 == 0){
				now=System.currentTimeMillis();
				System.out.println("setMeanWaitingTimeChart"+(icount) +"："+(now)+"毫秒");
			}
            if(max < mycase.getMeanWaitingTime())
                max = mycase.getMeanWaitingTime();
           	mycase = MainFrame.bigCaseCollectionDerby.getNextID();
        }
        MainFrame.meanWaitingTimeChart.setDuration(max / 100);
        mycase = MainFrame.bigCaseCollectionDerby.getFirstID();
        icount = 0;
        while(mycase != null){
			icount++;
			if(icount % 10000 == 0){
				now=System.currentTimeMillis();
				System.out.println("2setMeanWaitingTimeChart"+(icount) +"："+(now)+"毫秒");
			}
            MainFrame.meanWaitingTimeChart.addCases(mycase);
           	mycase = MainFrame.bigCaseCollectionDerby.getNextID();
        }
    }
  
    
    //更新activity集
    public void setActivityCollection() {
		int icount = 0;
		long now;
        BigEventDerby event = MainFrame.bigEventCollectionDerby.getFirstActivity();
        BigEventDerby nextEvent = MainFrame.bigEventCollectionDerby.getNextEventID();
        while (event != null) {
			icount++;
			if(icount % 10000 == 0){
				now=System.currentTimeMillis();
				System.out.println("setActivityCollection"+(icount) +"："+(now)+"毫秒");
			}
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
            while(true)
            {
                if(nextEvent == null)
                    break;
                if(nextEvent.getActivity().equals(activity.getActivity()))
                {
        			icount++;
        			if(icount % 10000 == 0){
        				now=System.currentTimeMillis();
        				System.out.println("setActivityCollection"+(icount) +"："+(now)+"毫秒");
        			}
                    if(nextEvent.getFirst())
                    {
                        activity.setFirst();
                    }
                    if(nextEvent.getLast())
                    {
                        activity.setLast();
                    }
                    activity.addFrequency();
                    activity.addDuration(nextEvent.getTime());
                    event = nextEvent;
                    nextEvent = MainFrame.bigEventCollectionDerby.getNextEventID();
                }
                else
                {
                    break;
                }
            }
            MainFrame.activityCollection.addActivity(activity);
            event = nextEvent;
            if(event == null)
            	break;
            nextEvent = MainFrame.bigEventCollectionDerby.getNextEventID();
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
		int icount = 0;
		long now;
        BigEventDerby event = MainFrame.bigEventCollectionDerby.getFirstResource();
        BigEventDerby nextEvent = MainFrame.bigEventCollectionDerby.getNextEventID();
        while (event != null) {
			icount++;
			if(icount % 10000 == 0){
				now=System.currentTimeMillis();
				System.out.println("setResourceCollection"+(icount) +"："+(now)+"毫秒");
			}
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
            while(true)
            {
                if(nextEvent == null)
                    break;
                if(nextEvent.getResource().equals(resource.getResource()))
                {
        			icount++;
        			if(icount % 10000 == 0){
        				now=System.currentTimeMillis();
        				System.out.println("setResourceCollection"+(icount) +"："+(now)+"毫秒");
        			}
                    if(nextEvent.getFirst())
                    {
                        resource.setFirst();
                    }
                    if(nextEvent.getLast())
                    {
                        resource.setLast();
                    }
                    resource.addFrequency();
                    resource.addDuration(nextEvent.getTime());
                    event = nextEvent;
                    nextEvent = MainFrame.bigEventCollectionDerby.getNextEventID();
                }
                else
                {
                    break;
                }
            }
            MainFrame.resourceCollection.addResource(resource);
            event = nextEvent;
            if(event == null)
            	break;
            nextEvent = MainFrame.bigEventCollectionDerby.getNextEventID();
        }
        for(int i = 0; i < MainFrame.resourceCollection.getSize(); i++)
        {
            Resource resource = MainFrame.resourceCollection.getResource(i);
            resource.sort();
        }
        MainFrame.resourceCollection.sortAndMerge();
    }

}
