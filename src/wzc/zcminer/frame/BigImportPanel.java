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
import wzc.zcminer.global.BigAnimation;
import wzc.zcminer.global.BigAnimation.Frame;
import wzc.zcminer.global.BigCase;
import wzc.zcminer.global.BigEvent;
import wzc.zcminer.global.BigVariant;
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
public class BigImportPanel extends JPanel{    
    public static boolean createFile(String destFileName) {
    	File file = new File(destFileName);
    	if(file.exists()) {
    		return false;
    	}
    	if (destFileName.endsWith(File.separator)) {
    		return false;
    	}
    	if(!file.getParentFile().exists()) {
    		if(!file.getParentFile().mkdirs()) {
    			return false;
    		}
    	}
    	try {
    		if (file.createNewFile()) {
    			return true;
    		} else {
    			return false;
    		}
    	} catch (IOException e) {
    		e.printStackTrace();
    		return false;
    	}
	}
	
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
	static BigFileButton bigFileButton;
	static JTextField timeText; //时间模式输入
	static JButton okButton;
	final static int MAXHEADINDEX = 20;
	int columnCount;
	int[] headIndex;
	String[] headlines;
	String [][] tableData;
	
	int maxLine;

	public BigImportPanel(String fileName, String separator, boolean hasTableHead, String encodingText) {
		// TODO Auto-generated constructor stub
		try {
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
		bigFileButton = new BigFileButton();

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
//				long start=System.currentTimeMillis();// 当前时间对应的毫秒数
//				BigEvent a = new BigEvent();
//				a.setDate("9.3.10 8:05", timeText.getText());
//				a.setDate("9.3.10 8:05", timeText.getText());
//				
//				long startMili=System.currentTimeMillis();// 当前时间对应的毫秒数
//				for(int i = 0; i < 100000; i++){
//					a.save("test");
//				}
//				long endMili=System.currentTimeMillis();
//				System.out.println("save 总耗时为："+(endMili-startMili)+"毫秒");
//				
//				startMili=System.currentTimeMillis();// 当前时间对应的毫秒数
//				for(int i = 0; i < 100000; i++){
//					a = new BigEvent("test");
//				}
//				endMili=System.currentTimeMillis();
//				System.out.println("read 总耗时为："+(endMili-startMili)+"毫秒");
				
			    setEventCollection(fileName, separator, hasTableHead, encodingText);
			    setGraphNet();
			    setAnimation();
			    setCaseCollection();
			    setCasePage();
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
				BigMapPanel mapPanel = new BigMapPanel();
				MainFrame.mainFrame.setContentPane(mapPanel);
				MainFrame.mainFrame.setVisible(true);
				System.gc();
//				long end=System.currentTimeMillis();
//				System.out.println("all 总耗时为："+(end-start)+"毫秒");
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
					tempStringList = tempString.split(separator);
					BigEvent event = new BigEvent();
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
		                    if (!MainFrame.bigAnimation
		                            .activityExist(tempStringList[j])) {
		                        MainFrame.bigAnimation
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
					MainFrame.bigEventCollection.addEvent(event);
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
		} catch (Exception ex) {
			ex.printStackTrace();
		}
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
        BigEvent event = MainFrame.bigEventCollection.getFirstEventID();
        while (event != null) {
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
            
            event = MainFrame.bigEventCollection.getNextEventID(event);
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
        MainFrame.bigAnimation.setMemory();
		if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
	        MainFrame.bigAnimation.activityNames[0] = "begin";
	        MainFrame.bigAnimation.activityNames[1] = "end";
		}
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
	        MainFrame.bigAnimation.activityNames[0] = "开始";
	        MainFrame.bigAnimation.activityNames[1] = "结束";
	    }
        int lastActivityId = -1;
        String lastCase = "";
        Frame newFrame = null;
        //按顺序遍历得出case，并运算各个参数
        BigEvent event = MainFrame.bigEventCollection.getFirstEventID();
        while (event != null) {
            String activityName = event.getActivity();
            String caseName = event.getCase();
            int activityId = MainFrame.bigAnimation.getActivityId(activityName);
            MainFrame.bigAnimation.setActivityName(activityId, activityName);
            MainFrame.bigAnimation.setBeginTime(event.getStartDate().getTime());
            MainFrame.bigAnimation.setEndTime(event.getEndDate().getTime());
        	File file = null;
            
            if (caseName.equals(lastCase)) {
            	newFrame.incActivityQueFre(lastActivityId, activityId);
            	newFrame.save();
            	file = new File("data_tmp//animation//" + event.getStartDate().getTime());
            	if(file.exists()){
            		newFrame = MainFrame.bigAnimation.newFrame(event.getStartDate().getTime());
            	}else{
            		newFrame = MainFrame.bigAnimation.newFrame();
            		newFrame.setFrame(event.getStartDate().getTime());
            		MainFrame.bigAnimation.addFrame(newFrame);
            		newFrame = MainFrame.bigAnimation.newFrame(event.getStartDate().getTime());
            	}
            	newFrame.incActivityFre(activityId);
            	newFrame.decActivityQueFre(lastActivityId, activityId);
            	newFrame.save();
            	file = new File("data_tmp//animation//" + event.getEndDate().getTime());
            	if(file.exists()){
            		newFrame = MainFrame.bigAnimation.newFrame(event.getEndDate().getTime());
            	}else{
            		newFrame = MainFrame.bigAnimation.newFrame();
            		newFrame.setFrame(event.getEndDate().getTime());
            		MainFrame.bigAnimation.addFrame(newFrame);
            		newFrame = MainFrame.bigAnimation.newFrame(event.getEndDate().getTime());
            	}
            	newFrame.decActivityFre(activityId);
            } else {
                if (lastActivityId != -1) {
                	newFrame.save();
                	file = new File("data_tmp//animation//" + event.getStartDate().getTime());
                	if(file.exists()){
                		newFrame = MainFrame.bigAnimation.newFrame(event.getStartDate().getTime());
                	}else{
                		newFrame = MainFrame.bigAnimation.newFrame();
                		newFrame.setFrame(event.getStartDate().getTime());
                		MainFrame.bigAnimation.addFrame(newFrame);
                		newFrame = MainFrame.bigAnimation.newFrame(event.getStartDate().getTime());
                	}
                	newFrame.incActivityFre(activityId);
                	newFrame.save();
                	file = new File("data_tmp//animation//" + event.getEndDate().getTime());
                	if(file.exists()){
                		newFrame = MainFrame.bigAnimation.newFrame(event.getEndDate().getTime());
                	}else{
                		newFrame = MainFrame.bigAnimation.newFrame();
                		newFrame.setFrame(event.getEndDate().getTime());
                		MainFrame.bigAnimation.addFrame(newFrame);
                		newFrame = MainFrame.bigAnimation.newFrame(event.getEndDate().getTime());
                	}
                	newFrame.decActivityFre(activityId);
                } else {
                	file = new File("data_tmp//animation//" + event.getStartDate().getTime());
                	if(file.exists()){
                		newFrame = MainFrame.bigAnimation.newFrame(event.getStartDate().getTime());
                	}else{
                		newFrame = MainFrame.bigAnimation.newFrame();
                		newFrame.setFrame(event.getStartDate().getTime());
                		MainFrame.bigAnimation.addFrame(newFrame);
                		newFrame = MainFrame.bigAnimation.newFrame(event.getStartDate().getTime());
                	}
                	newFrame.incActivityFre(activityId);
                	newFrame.save();
                	file = new File("data_tmp//animation//" + event.getEndDate().getTime());
                	if(file.exists()){
                		newFrame = MainFrame.bigAnimation.newFrame(event.getEndDate().getTime());
                	}else{
                		newFrame = MainFrame.bigAnimation.newFrame();
                		newFrame.setFrame(event.getEndDate().getTime());
                		MainFrame.bigAnimation.addFrame(newFrame);
                		newFrame = MainFrame.bigAnimation.newFrame(event.getEndDate().getTime());
                	}
                	newFrame.decActivityFre(activityId);
                }
                lastCase = caseName;
                lastActivityId = activityId;
                //newFrame.incActivityFre(lastActivityId);
            }
            
            event = MainFrame.bigEventCollection.getNextEventID(event);
        }
        newFrame.save();
        MainFrame.bigAnimation.merge();
    }
    
    //更新case集
    public void setCaseCollection() {
        BigEvent event = MainFrame.bigEventCollection.getFirstEventID();
        if (event == null) return;
        BigEvent nextEvent = MainFrame.bigEventCollection.getNextEventID(event);
        while (event != null) {
            event.setFirst();
            BigCase newcase = new BigCase();
            newcase.setCase(event.getCase());
            newcase.addEvent(event);
            newcase.addActivity(event.getActivity());
            while(true){
            	if(nextEvent == null){
                    event.setLast();
                    MainFrame.bigCaseCollection.addCase(newcase);
                    MainFrame.bigCaseCollection.update();
                    return;
            	}
                if(nextEvent.getCase().equals(newcase.getCase())){
                	event = nextEvent;
                	nextEvent = MainFrame.bigEventCollection.getNextEventID(event);
                    newcase.addEvent(event);
                    newcase.addActivity(event.getActivity());
                }else{
                	break;
                }
            }
            event.setLast();
            MainFrame.bigCaseCollection.addCase(newcase);
        	event = nextEvent;
        	nextEvent = MainFrame.bigEventCollection.getNextEventID(event);
        }
    }
   
    
    //更新variant集
    public void setCasePage() {
        BigVariant allVariant = new BigVariant();
        allVariant.setVariant(0);
        BigCase mycase = MainFrame.bigCaseCollection.getFirstID();
        while(mycase != null){
            allVariant.addCase(mycase);
        	mycase = MainFrame.bigCaseCollection.getNextID(mycase);
        }
        allVariant.saveCases();
        allVariant.save();
    }
    
    
    //更新variant集
    public void setVariantCollection() {
        BigCase mycase = MainFrame.bigCaseCollection.getFirstCaseVariant();
        if (mycase == null) return;
        BigCase nextCase = MainFrame.bigCaseCollection.getNextCaseVariant(mycase);
        while(mycase != null){
            BigVariant variant = new BigVariant();
            variant.setVariant(MainFrame.bigVariantCollection.getSize()+1);
            variant.addCase(mycase);
            variant.setActivities(mycase.getActivities());
            BigCase medianCase = mycase;
            int count = 1;
            while(true)
            {
            	if(nextCase == null){
                	if(variant.getSize() % 2 == 0){
                		variant.setMedianDuration(medianCase.getDuration());
                		medianCase = MainFrame.bigCaseCollection.getNextCaseVariant(medianCase);
                		variant.setMedianDuration((variant.getMedianDuration() + medianCase.getDuration()) / 2);
                	}else{
                		variant.setMedianDuration(medianCase.getDuration());
                	}
                    MainFrame.bigVariantCollection.addVariant(variant);
                    return;
            	}
                if(variant.getActivities().equals(nextCase.getActivities())){
                	mycase = nextCase;
                	nextCase = MainFrame.bigCaseCollection.getNextCaseVariant(mycase);
                	variant.addCase(mycase);
                	if(variant.getSize() > count * 2){
                		medianCase = MainFrame.bigCaseCollection.getNextCaseVariant(medianCase);
                		count++;
                	}
                }else{
                	break;
                }
            }
        	if(variant.getSize() % 2 == 0){
        		variant.setMedianDuration(medianCase.getDuration());
        		medianCase = MainFrame.bigCaseCollection.getNextCaseVariant(medianCase);
        		variant.setMedianDuration((variant.getMedianDuration() + medianCase.getDuration()) / 2);
        	}else{
        		variant.setMedianDuration(medianCase.getDuration());
        	}
            MainFrame.bigVariantCollection.addVariant(variant);
        	mycase = nextCase;
        	nextCase = MainFrame.bigCaseCollection.getNextCaseVariant(mycase);
        }
    }
	
    
    //更新ActiveCasesOverTimeChart图表
    public void setActiveCasesOverTimeChart() {
        long start = 0;
        long end = 0;
        BigCase mycase = MainFrame.bigCaseCollection.getFirstID();
        while(mycase != null){
            if(start > mycase.getStart().getTime() || start == 0)
            	start = mycase.getStart().getTime();
            if(end < mycase.getEnd().getTime())
            	end = mycase.getEnd().getTime();
        	mycase = MainFrame.bigCaseCollection.getNextID(mycase);
        }
        MainFrame.activeCasesOverTimeChart.init(start, end);
        mycase = MainFrame.bigCaseCollection.getFirstID();
        while(mycase != null){
            MainFrame.activeCasesOverTimeChart.addActiveCasesOverTime(1, mycase.getStart());
            MainFrame.activeCasesOverTimeChart.addActiveCasesOverTime(-1, mycase.getEnd());
        	mycase = MainFrame.bigCaseCollection.getNextID(mycase);
        }
    }
  
    
    //更新EventsOverTimeChart图表
    public void setEventsOverTimeChart() {
        long start = 0;
        long end = 0;
        BigEvent event = MainFrame.bigEventCollection.getFirstEventID();
        while(event != null){
            if(start > event.getStartDate().getTime() || start == 0)
            	start = event.getStartDate().getTime();
            if(end < event.getEndDate().getTime())
            	end = event.getEndDate().getTime();
        	event = MainFrame.bigEventCollection.getNextEventID(event);
        }
        MainFrame.eventsOverTimeChart.init(start, end);
        event = MainFrame.bigEventCollection.getFirstEventID();
        while(event != null){
            MainFrame.eventsOverTimeChart.addEventsOverTime(1, event.getStartDate());
            MainFrame.eventsOverTimeChart.addEventsOverTime(-1, event.getEndDate());
        	event = MainFrame.bigEventCollection.getNextEventID(event);
        }
    }
   
    
    //更新EventsPerCaseChart图表
    public void setEventsPerCaseChart() {
        BigCase mycase = MainFrame.bigCaseCollection.getFirstID();
        while(mycase != null){
            MainFrame.eventsPerCaseChart.addCases(mycase);
        	mycase = MainFrame.bigCaseCollection.getNextID(mycase);
        }
    }
  
    
    //更新CaseDurationChart图表
    public void setCaseDurationChart() {
        long max = 0;
        BigCase mycase = MainFrame.bigCaseCollection.getFirstID();
        while(mycase != null){
            if(max < mycase.getDuration())
                max = mycase.getDuration();
        	mycase = MainFrame.bigCaseCollection.getNextID(mycase);
        }
        MainFrame.caseDurationChart.setDuration(max / 100);
        mycase = MainFrame.bigCaseCollection.getFirstID();
        while(mycase != null){
            MainFrame.caseDurationChart.addCases(mycase);
        	mycase = MainFrame.bigCaseCollection.getNextID(mycase);
        }
    }
  
    
    //更新CaseUtilizationChart图表
    public void setCaseUtilizationChart() {
        BigCase mycase = MainFrame.bigCaseCollection.getFirstID();
        while(mycase != null){
            MainFrame.caseUtilizationChart.addCases(mycase);
           	mycase = MainFrame.bigCaseCollection.getNextID(mycase);
        }
    }
  
    
    //更新MeanActivityDurationChart图表
    public void setMeanActivityDurationChart() {
        double max = 0;
        BigCase mycase = MainFrame.bigCaseCollection.getFirstID();
        while(mycase != null){
            if(max < mycase.getMeanActiveTime())
                max = mycase.getMeanActiveTime();
           	mycase = MainFrame.bigCaseCollection.getNextID(mycase);
        }
        MainFrame.meanActivityDurationChart.setDuration(max / 100);
        mycase = MainFrame.bigCaseCollection.getFirstID();
        while(mycase != null){
            MainFrame.meanActivityDurationChart.addCases(mycase);
           	mycase = MainFrame.bigCaseCollection.getNextID(mycase);
        }
    }
 
    
    //更新MeanWaitingTimeChart图表
    public void setMeanWaitingTimeChart() {
        double max = 0;
        BigCase mycase = MainFrame.bigCaseCollection.getFirstID();
        while(mycase != null){
            if(max < mycase.getMeanWaitingTime())
                max = mycase.getMeanWaitingTime();
           	mycase = MainFrame.bigCaseCollection.getNextID(mycase);
        }
        MainFrame.meanWaitingTimeChart.setDuration(max / 100);
        mycase = MainFrame.bigCaseCollection.getFirstID();
        while(mycase != null){
            MainFrame.meanWaitingTimeChart.addCases(mycase);
           	mycase = MainFrame.bigCaseCollection.getNextID(mycase);
        }
    }
  
    
    //更新activity集
    public void setActivityCollection() {
        BigEvent event = MainFrame.bigEventCollection.getFirstActivity();
        BigEvent nextEvent = MainFrame.bigEventCollection.getNextEventID(event);
        while (event != null) {
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
                    nextEvent = MainFrame.bigEventCollection.getNextEventID(event);
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
            nextEvent = MainFrame.bigEventCollection.getNextEventID(event);
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
        BigEvent event = MainFrame.bigEventCollection.getFirstResource();
        BigEvent nextEvent = MainFrame.bigEventCollection.getNextEventID(event);
        while (event != null) {
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
                    nextEvent = MainFrame.bigEventCollection.getNextEventID(event);
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
            nextEvent = MainFrame.bigEventCollection.getNextEventID(event);
        }
        for(int i = 0; i < MainFrame.resourceCollection.getSize(); i++)
        {
            Resource resource = MainFrame.resourceCollection.getResource(i);
            resource.sort();
        }
        MainFrame.resourceCollection.sortAndMerge();
    }

}
