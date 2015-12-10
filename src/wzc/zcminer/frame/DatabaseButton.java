package wzc.zcminer.frame;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedMap;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

import wzc.zcminer.global.ActiveCasesOverTimeChart;
import wzc.zcminer.global.ActivityCollection;
import wzc.zcminer.global.CaseCollection;
import wzc.zcminer.global.CaseDurationChart;
import wzc.zcminer.global.CaseUtilizationChart;
import wzc.zcminer.global.ColumnSelectableJTable;
import wzc.zcminer.global.EventCollection;
import wzc.zcminer.global.EventsOverTimeChart;
import wzc.zcminer.global.EventsPerCaseChart;
import wzc.zcminer.global.GraphNet;
import wzc.zcminer.global.MeanActivityDurationChart;
import wzc.zcminer.global.MeanWaitingTimeChart;
import wzc.zcminer.global.ResourceCollection;
import wzc.zcminer.global.VariantCollection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//选择数据库按钮
public class DatabaseButton extends JButton{
	
	static JTabbedPane tabbedPanel;
	
	static JPanel singleSelectPanel;
	static JPanel singleLabelPanel;
	static JPanel singleUrlLabelPanel;
	static JTextField singleUrlText;
	static JPanel singleTableLabelPanel;
	static JTextField singleTableText;
	static JPanel singleUserLabelPanel;
	static JTextField singleUserText;
	static JPanel singlePasswordLabelPanel;
	static JPasswordField singlePasswordText;
	static JPanel singleButtonPanel;
	static JButton singleOkButton;
	static JButton singleCancleButton;
	
	static JPanel multiSelectPanel;
	static JPanel multiLabelPanel;
	static JPanel multiUrlLabelPanel;
	static JTextField multiUrlText;
	static JPanel multiTableLabelPanel;
	static JTable multiTable;
	static JScrollPane multiTablePanel;
	static JPanel multiUserLabelPanel;
	static JTextField multiUserText;
	static JPanel multiPasswordLabelPanel;
	static JPasswordField multiPasswordText;
	static JPanel multiButtonPanel;
	static JButton multiOkButton;
	static JButton multiCancleButton;
	
	public DatabaseButton() {
		super();
		
		if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
			setText("Select database");
		}
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
			setText("选择数据库");
		}
		//导入数据库表
		addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JDialog jd = new JDialog() ;
				if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
				{
					jd.setTitle("Select database");
				}
				else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
				{
					jd.setTitle("选择数据库");
				}
				
				singleUrlText = new JTextField(30);
				singleUrlText.setText(MainFrame.properties.getProperty("single_sql", "jdbc:oracle:thin:@//127.0.0.1:1521/ORCL"));
				singleTableText = new JTextField(30);
				singleTableText.setText(MainFrame.properties.getProperty("single_table", "THUMiner_test"));
				singleUserText = new JTextField(30);
				singleUserText.setText(MainFrame.properties.getProperty("single_user", "c##a"));
				singlePasswordText = new JPasswordField(30);
				singlePasswordText.setText(MainFrame.properties.getProperty("single_password", "abc"));
				
				if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
				{
					singleOkButton = new JButton("Connect");
				}
				else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
				{
					singleOkButton = new JButton("连接");
				}
				singleOkButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
		                String driver = "oracle.jdbc.driver.OracleDriver";
		                String url = singleUrlText.getText();
		                String table = singleTableText.getText();
		                String username = singleUserText.getText();
		                String password = singlePasswordText.getText();

						try {
					        if (MainFrame.result != null)
					        	MainFrame.result.close();
					        if (MainFrame.statement != null)
					        	MainFrame.statement.close();
							if (MainFrame.connection != null)
								MainFrame.connection.close();

		                    Class.forName(driver);
		                    MainFrame.connection = DriverManager.getConnection(url, username, password);
		                    
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
	    					ImportPanel importPanel = new ImportPanel(table.toUpperCase());
	    					MainFrame.mainFrame.setContentPane(importPanel);
	    					MainFrame.mainFrame.setVisible(true);
	    					System.gc();
	    					
	    					MainFrame.dataSource = 1;
	    					
							jd.dispose();
						} catch (Exception e1) {
		                    e1.printStackTrace();
		                }
		                
					}
				});
				
				if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
				{
					singleCancleButton = new JButton("Cancle");
				}
				else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
				{
					singleCancleButton = new JButton("取消");
				}
				singleCancleButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						jd.dispose();
					}
				});
				
				singleUrlLabelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 20));
				if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
				{
					singleUrlLabelPanel.add(new JLabel("Url: "));
				}
				else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
				{
					singleUrlLabelPanel.add(new JLabel("数据库地址："));
				}
				singleUrlLabelPanel.add(singleUrlText);
				
				singleUserLabelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 20));
				if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
				{
					singleUserLabelPanel.add(new JLabel("User Name: "));
				}
				else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
				{
					singleUserLabelPanel.add(new JLabel("用户名："));
				}
				singleUserLabelPanel.add(singleUserText);
				
				singlePasswordLabelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 20));
				if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
				{
					singlePasswordLabelPanel.add(new JLabel("Password: "));
				}
				else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
				{
					singlePasswordLabelPanel.add(new JLabel("密码："));
				}
				singlePasswordLabelPanel.add(singlePasswordText);
				
				singleTableLabelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 20));
				if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
				{
					singleTableLabelPanel.add(new JLabel("Table: "));
				}
				else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
				{
					singleTableLabelPanel.add(new JLabel("表名："));
				}
				singleTableLabelPanel.add(singleTableText);
				
				singleButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 20));
				singleButtonPanel.add(singleOkButton);
				singleButtonPanel.add(singleCancleButton);
				
				singleLabelPanel = new JPanel(new GridLayout(0, 1));
				singleLabelPanel.add(singleUrlLabelPanel);
				singleLabelPanel.add(singleUserLabelPanel);
				singleLabelPanel.add(singlePasswordLabelPanel);
				singleLabelPanel.add(singleTableLabelPanel);
				
				singleSelectPanel = new JPanel(new BorderLayout());
				singleSelectPanel.add(singleLabelPanel, BorderLayout.CENTER);
				singleSelectPanel.add(singleButtonPanel, BorderLayout.SOUTH);

				multiUrlText = new JTextField(30);
				multiUrlText.setText(MainFrame.properties.getProperty("multi_sql", "jdbc:oracle:thin:@//127.0.0.1:1521/ORCL"));
				multiUserText = new JTextField(30);
				multiUserText.setText(MainFrame.properties.getProperty("multi_user", "c##a"));
				multiPasswordText = new JPasswordField(30);
				multiPasswordText.setText(MainFrame.properties.getProperty("multi_password", "abc"));
				
				if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
				{
					multiOkButton = new JButton("Connect");
				}
				else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
				{
					multiOkButton = new JButton("连接");
				}
				multiOkButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
		                String driver = "oracle.jdbc.driver.OracleDriver";
		                String url = multiUrlText.getText();
		                String username = multiUserText.getText();
		                String password = multiPasswordText.getText();
		                String[] tableList = new String[5];
		                String[] tableEventidList = new String[5];
		                multiTable.editingStopped(changeEvent);
		                
						for(int i = 0; i < multiTable.getRowCount(); i++) 
						{
							String table = (String)multiTable.getValueAt(i, 0);
							String tableEventid = (String)multiTable.getValueAt(i, 1);
							if(table == null) table = "";
							if(tableEventid == null) table = "";
							if(table.equals("") ^ tableEventid.equals(""))
							{
								System.out.println("Table or Event Id is missing!");
								return;
							}
							tableList[i] = table;
							tableEventidList[i] = tableEventid;
						}

						try {
					        if (MainFrame.result != null)
					        	MainFrame.result.close();
					        if (MainFrame.statement != null)
					        	MainFrame.statement.close();
							if (MainFrame.connection != null)
								MainFrame.connection.close();

		                    Class.forName(driver);
		                    MainFrame.connection = DriverManager.getConnection(url, username, password);
		                    
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
	    					ImportPanel importPanel = new ImportPanel(tableList, tableEventidList);
	    					MainFrame.mainFrame.setContentPane(importPanel);
	    					MainFrame.mainFrame.setVisible(true);
	    					System.gc();
	    					
	    					MainFrame.dataSource = 1;
	    					
							jd.dispose();
						} catch (Exception e1) {
		                    e1.printStackTrace();
		                }
		                
					}
				});
				
				if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
				{
					multiCancleButton = new JButton("Cancle");
				}
				else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
				{
					multiCancleButton = new JButton("取消");
				}
				multiCancleButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						jd.dispose();
					}
				});
				
				multiUrlLabelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 20));
				if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
				{
					multiUrlLabelPanel.add(new JLabel("Url: "));
				}
				else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
				{
					multiUrlLabelPanel.add(new JLabel("数据库地址："));
				}
				multiUrlLabelPanel.add(multiUrlText);
				
				multiUserLabelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 20));
				if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
				{
					multiUserLabelPanel.add(new JLabel("User Name: "));
				}
				else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
				{
					multiUserLabelPanel.add(new JLabel("用户名："));
				}
				multiUserLabelPanel.add(multiUserText);
				
				multiPasswordLabelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 20));
				if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
				{
					multiPasswordLabelPanel.add(new JLabel("Password: "));
				}
				else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
				{
					multiPasswordLabelPanel.add(new JLabel("密码："));
				}
				multiPasswordLabelPanel.add(multiPasswordText);
				
				multiTableLabelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 20));
				if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
				{
					multiTableLabelPanel.add(new JLabel("Please entering table name and event id for joining table (Double click to edit)"), BorderLayout.NORTH);
				}
				else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
				{
					multiTableLabelPanel.add(new JLabel("请输入表名和用于关联的事件编号（双击进行编辑）"), BorderLayout.NORTH);
				}
				
				multiButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 20));
				multiButtonPanel.add(multiOkButton);
				multiButtonPanel.add(multiCancleButton);
				
				multiLabelPanel = new JPanel(new GridLayout(0, 1));
				multiLabelPanel.add(multiUrlLabelPanel);
				multiLabelPanel.add(multiUserLabelPanel);
				multiLabelPanel.add(multiPasswordLabelPanel);
				multiLabelPanel.add(multiTableLabelPanel);
				
				String[] headlines = new String[2];
				if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
				{
					headlines[0] = "Table";
					headlines[1] = "Event Id";
				}
				else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
				{
					headlines[0] = "表名";
					headlines[1] = "事件编号";
				}
				
				String multiTableList = MainFrame.properties.getProperty("multi_table", "THUMiner_join1,THUMiner_join2,THUMiner_join3,THUMiner_join4");
				String multiTableEventidList = MainFrame.properties.getProperty("multi_table_eventid", "EventId,EventId,EventId,EventId");
				String[] multiTableListArray = multiTableList.split(",");
				String[] multiTableEventidListArray = multiTableEventidList.split(",");
				String[][] tableData = new String[5][2];
				for(int i = 0; i < 5; i++) 
				{
					tableData[i][0] = "";
					tableData[i][1] = "";
				}
				for(int i = 0; i < multiTableListArray.length; i++) 
				{
					tableData[i][0] = multiTableListArray[i];
					tableData[i][1] = multiTableEventidListArray[i];
				}
				
				multiTable = new JTable(tableData, headlines);
				multiTablePanel = new JScrollPane(multiTable);
				
				multiSelectPanel = new JPanel(new BorderLayout());
				multiSelectPanel.add(multiLabelPanel, BorderLayout.NORTH);
				multiSelectPanel.add(multiTablePanel, BorderLayout.CENTER);
				multiSelectPanel.add(multiButtonPanel, BorderLayout.SOUTH);

				tabbedPanel = new JTabbedPane();
				if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
				{
					tabbedPanel.addTab("Single table", null, singleSelectPanel, "Single table");
					tabbedPanel.addTab("Multi table", null, multiSelectPanel, "Multi table");
				}
				else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
				{
					tabbedPanel.addTab("单表", null, singleSelectPanel, "单表");
					tabbedPanel.addTab("多表", null, multiSelectPanel, "多表");
				}
				
				tabbedPanel.addChangeListener(new ChangeListener(){
					@Override
					public void stateChanged(ChangeEvent e) {
						// TODO Auto-generated method stub
						if(tabbedPanel.getSelectedIndex() == 0)
						{
			        		jd.setSize(new Dimension(510, 340));
			                jd.setPreferredSize(new Dimension(510, 340));
						}
						else if(tabbedPanel.getSelectedIndex() == 1)
						{
			        		jd.setSize(new Dimension(510, 476));
			                jd.setPreferredSize(new Dimension(510, 476));
						}
		                int x = (Toolkit.getDefaultToolkit().getScreenSize().width - jd.getSize().width)/2;
		                int y = (Toolkit.getDefaultToolkit().getScreenSize().height - jd.getSize().height)/2;
		                jd.setLocation(x, y);
					}
				});
				
				jd.add(tabbedPanel);

        		jd.setModal(true);
        		jd.setSize(new Dimension(510, 340));
                jd.setPreferredSize(new Dimension(510, 340));
                int x = (Toolkit.getDefaultToolkit().getScreenSize().width - jd.getSize().width)/2;
                int y = (Toolkit.getDefaultToolkit().getScreenSize().height - jd.getSize().height)/2;
                jd.setLocation(x, y);
                jd.setVisible(true);
			}
		});
	}
	
}
