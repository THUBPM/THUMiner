package wzc.zcminer.frame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import sun.applet.Main;
import wzc.zcminer.global.ActiveCasesOverTimeChart;
import wzc.zcminer.global.Activity;
import wzc.zcminer.global.ActivityCollection;
import wzc.zcminer.global.CaseDurationChart;
import wzc.zcminer.global.CaseUtilizationChart;
import wzc.zcminer.global.ColumnSelectableJTable;
import wzc.zcminer.global.EventCollection;
import wzc.zcminer.global.Event;
import wzc.zcminer.global.CaseCollection;
import wzc.zcminer.global.Case;
import wzc.zcminer.global.EventsOverTimeChart;
import wzc.zcminer.global.EventsPerCaseChart;
import wzc.zcminer.global.GraphNet;
import wzc.zcminer.global.MeanActivityDurationChart;
import wzc.zcminer.global.MeanWaitingTimeChart;
import wzc.zcminer.global.Resource;
import wzc.zcminer.global.ResourceCollection;
import wzc.zcminer.global.VariantCollection;
import wzc.zcminer.global.Variant;

import com.mxgraph.util.mxRectangle;
import com.opencsv.CSVReader;
//数据导入面板
public class ImportPanel extends JPanel {
	static ColumnSelectableJTable table;
	static JPanel radioPanel;
	static JRadioButton removeButton;
	static JRadioButton caseButton;
	static JRadioButton activityButton;
	static JRadioButton timeButton;
	static JRadioButton resourseButton;
	static JRadioButton otherButton;
	static ButtonGroup buttonGroup;
	static JButton fileButton;
	static JTextField separatorText;
	static JCheckBox tableHead;
	static JTextField timeText; //时间模式输入
	static JButton okButton;
	final static int MAXHEADINDEX = 20;
	int[] headIndex;
	String[][] rowData;
	List<String[]> myEntries;
	
	int maxLine;

	public ImportPanel(String dataName, char separator, boolean hasTableHead) {
		// TODO Auto-generated constructor stub
		try {
			headIndex = new int[MAXHEADINDEX];
			//按钮组的设置
			setLayout(new BorderLayout());
			radioPanel = new JPanel();
			radioPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

			if(MainFrame.properties.getProperty("language").equals("enUS"))
			{
				removeButton = new JRadioButton("Remove");
			}
			else if(MainFrame.properties.getProperty("language").equals("zhCN"))
			{
				removeButton = new JRadioButton("移除");
			}
			removeButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					headIndex[table.column] = 1;
				}
			});

			if(MainFrame.properties.getProperty("language").equals("enUS"))
			{
				caseButton = new JRadioButton("Case");
			}
			else if(MainFrame.properties.getProperty("language").equals("zhCN"))
			{
				caseButton = new JRadioButton("实例");
			}
			caseButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					headIndex[table.column] = 2;
				}
			});

			if(MainFrame.properties.getProperty("language").equals("enUS"))
			{
				activityButton = new JRadioButton("Activity");
			}
			else if(MainFrame.properties.getProperty("language").equals("zhCN"))
			{
				activityButton = new JRadioButton("活动");
			}
			activityButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					headIndex[table.column] = 3;
				}
			});

			if(MainFrame.properties.getProperty("language").equals("enUS"))
			{
				timeButton = new JRadioButton("Timestamp");
			}
			else if(MainFrame.properties.getProperty("language").equals("zhCN"))
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
			timeText.setText(MainFrame.properties.getProperty("timestamp"));
			//timeText.setText("dd.MM.yy HH:mm");
			//timeText.setText("dd/MM/yyyy HH:mm:ss");
			//timeText.setText("yyyy-MM-dd HH:mm:ss");
			
			if(MainFrame.properties.getProperty("language").equals("enUS"))
			{
				resourseButton = new JRadioButton("Resourse");
			}
			else if(MainFrame.properties.getProperty("language").equals("zhCN"))
			{
				resourseButton = new JRadioButton("资源");
			}
			resourseButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					headIndex[table.column] = 5;
				}
			});

			if(MainFrame.properties.getProperty("language").equals("enUS"))
			{
				otherButton = new JRadioButton("Other");
			}
			else if(MainFrame.properties.getProperty("language").equals("zhCN"))
			{
				otherButton = new JRadioButton("其他");
			}
			otherButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					headIndex[table.column] = 6;
				}
			});

			if(MainFrame.properties.getProperty("language").equals("enUS"))
			{
				fileButton = new JButton("Select csv");
			}
			else if(MainFrame.properties.getProperty("language").equals("zhCN"))
			{
				fileButton = new JButton("选择csv文件");
			}
			//导入数据文件
			fileButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JFileChooser fd = new JFileChooser();
					fd.setCurrentDirectory(new File("."));
					fd.setAcceptAllFileFilterUsed(false);
					final String[][] fileENames;
					fileENames = new String[1][2];
					if(MainFrame.properties.getProperty("language").equals("enUS"))
					{
						fileENames[0][0] = ".csv";
						fileENames[0][1] = "Csv files(*.csv)";
					}
					else if(MainFrame.properties.getProperty("language").equals("zhCN"))
					{
						fileENames[0][0] = ".csv";
						fileENames[0][1] = "Csv文件(*.csv)";
					}
					fd.addChoosableFileFilter(new FileFilter() {
						public boolean accept(File file) {
							return true;
						}

						public String getDescription() {
							if(MainFrame.properties.getProperty("language").equals("enUS"))
							{
								return "All files(*.*)";
							}
							else if(MainFrame.properties.getProperty("language").equals("zhCN"))
							{
								return "所有文件(*.*)";
							}
							return "";
						}
					});
	                //文件操作
	                for (final String[] fileEName : fileENames) {

	                    fd.setFileFilter(new javax.swing.filechooser.FileFilter() {

	                        public boolean accept(File file) {

	                            if (file.getName().endsWith(fileEName[0])
	                                    || file.isDirectory()) {

	                                return true;
	                            }

	                            return false;
	                        }

	                        public String getDescription() {

	                            return fileEName[1];
	                        }

	                    });
	                }
	                fd.showOpenDialog(new JLabel());
	                File file = fd.getSelectedFile();
	                if (file!=null){
	                    MainFrame.eventCollection = new EventCollection();
	                    MainFrame.graphNet = new GraphNet();
	                    MainFrame.variantCollection = new VariantCollection();
	                    MainFrame.caseCollection = new CaseCollection();
	                    MainFrame.activeCasesOverTimeChart = new ActiveCasesOverTimeChart();
	                    MainFrame.eventsOverTimeChart = new EventsOverTimeChart();
	                    MainFrame.eventsPerCaseChart = new EventsPerCaseChart();
	                    MainFrame.caseDurationChart = new CaseDurationChart();
	                    MainFrame.caseUtilizationChart = new CaseUtilizationChart();
	                    MainFrame.meanActivityDurationChart = new MeanActivityDurationChart();
	                    MainFrame.meanWaitingTimeChart = new MeanWaitingTimeChart();
	                    MainFrame.activityCollection = new ActivityCollection();
	                    MainFrame.resourceCollection = new ResourceCollection();
	                    
	                    MainFrame.mainFrame.getContentPane().removeAll();
	                    System.gc();
						ImportPanel importPanel = new ImportPanel(file.getAbsolutePath(), separatorText.getText().charAt(0), tableHead.isSelected());
	                    MainFrame.mainFrame.setContentPane(importPanel);
	                    MainFrame.mainFrame.setVisible(true);
	                    System.gc();
	                }

	            }
	        });
			
			separatorText = new JTextField(2);
			separatorText.setText(MainFrame.properties.getProperty("separator"));
			
			if(MainFrame.properties.getProperty("language").equals("enUS"))
			{
				tableHead = new JCheckBox("Header");
			}
			else if(MainFrame.properties.getProperty("language").equals("zhCN"))
			{
				tableHead = new JCheckBox("表头");
			}
			if(MainFrame.properties.getProperty("header").equals("true"))
			{
				tableHead.setSelected(true);
			}
			else if(MainFrame.properties.getProperty("header").equals("false"))
			{
				tableHead.setSelected(false);
			}
	        
	        radioPanel.add(fileButton);
			if(MainFrame.properties.getProperty("language").equals("enUS"))
			{
				radioPanel.add(new JLabel("Csv seperator: "));
			}
			else if(MainFrame.properties.getProperty("language").equals("zhCN"))
			{
				radioPanel.add(new JLabel("Csv分隔符："));
			}
	        radioPanel.add(separatorText);
	        radioPanel.add(tableHead);
			radioPanel.add(removeButton);
			radioPanel.add(otherButton);
			radioPanel.add(caseButton);
			radioPanel.add(activityButton);
			radioPanel.add(resourseButton);
			radioPanel.add(timeButton);
			if(MainFrame.properties.getProperty("language").equals("enUS"))
			{
				radioPanel.add(new JLabel("Timestamp pattern: "));
			}
			else if(MainFrame.properties.getProperty("language").equals("zhCN"))
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
			//csv数据导入
			DataInputStream input = new DataInputStream(new FileInputStream(new File(dataName)));
			CSVReader reader = new CSVReader(new InputStreamReader(input, "GBK"), separator);
			myEntries = reader.readAll();
			String[] headlines;
			if(hasTableHead)
			{
				headlines = myEntries.remove(0);
			}
			else
			{
				headlines = new String[myEntries.get(0).length];
				for(int i = 0; i < headlines.length; i++)
				{
					if(MainFrame.properties.getProperty("language").equals("enUS"))
					{
						headlines[i] = "Column" + (i + 1);
					}
					else if(MainFrame.properties.getProperty("language").equals("zhCN"))
					{
						headlines[i] = "第" + (i + 1) + "列";
					}
				}
			}
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
			reader.close();
			//逻辑分析代码，得出所有信息
			if(MainFrame.properties.getProperty("language").equals("enUS"))
			{
				okButton = new JButton("Start import");
			}
			else if(MainFrame.properties.getProperty("language").equals("zhCN"))
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

		} catch (Exception ex) {
			ex.printStackTrace();
		}
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
		if(MainFrame.properties.getProperty("language").equals("enUS"))
		{
	        MainFrame.graphNet.activityNames[0] = "begin";
	        MainFrame.graphNet.activityNames[1] = "end";
		}
		else if(MainFrame.properties.getProperty("language").equals("zhCN"))
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
