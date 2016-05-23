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
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedMap;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;

import wzc.zcminer.global.ActiveCasesOverTimeChart;
import wzc.zcminer.global.ActivityCollection;
import wzc.zcminer.global.BigCaseCollection;
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
    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }
    
	static JTabbedPane tabbedPanel;
	
	static JTabbedPane oracleTabbedPanel;
	
	static JPanel oracleSingleSelectPanel;
	static JPanel oracleSingleLabelPanel;
	static JPanel oracleSingleUrlLabelPanel;
	static JTextField oracleSingleUrlText;
	static JPanel oracleSingleTableLabelPanel;
	static JTextField oracleSingleTableText;
	static JPanel oracleSingleUserLabelPanel;
	static JTextField oracleSingleUserText;
	static JPanel oracleSinglePasswordLabelPanel;
	static JPasswordField oracleSinglePasswordText;
	static JPanel oracleSingleButtonPanel;
	static JButton oracleSingleOkButton;
	static JButton oracleSingleCancleButton;
	
	static JPanel oracleMultiSelectPanel;
	static JPanel oracleMultiLabelPanel;
	static JPanel oracleMultiUrlLabelPanel;
	static JTextField oracleMultiUrlText;
	static JPanel oracleMultiTableLabelPanel;
	static JTable oracleMultiTable;
	static JScrollPane oracleMultiTablePanel;
	static JPanel oracleMultiUserLabelPanel;
	static JTextField oracleMultiUserText;
	static JPanel oracleMultiPasswordLabelPanel;
	static JPasswordField oracleMultiPasswordText;
	static JPanel oracleMultiButtonPanel;
	static JButton oracleMultiOkButton;
	static JButton oracleMultiCancleButton;
	
	static JTabbedPane mongodbTabbedPanel;
	
	static JPanel mongodbSingleSelectPanel;
	static JPanel mongodbSingleLabelPanel;
	static JPanel mongodbSingleHostLabelPanel;
	static JTextField mongodbSingleHostText;
	static JPanel mongodbSinglePortLabelPanel;
	static JTextField mongodbSinglePortText;
	static JPanel mongodbSingleDBLabelPanel;
	static JTextField mongodbSingleDBText;
	static JPanel mongodbSingleCollectionLabelPanel;
	static JTextField mongodbSingleCollectionText;
	static JPanel mongodbSingleUserLabelPanel;
	static JTextField mongodbSingleUserText;
	static JPanel mongodbSinglePasswordLabelPanel;
	static JPasswordField mongodbSinglePasswordText;
	static JPanel mongodbSingleButtonPanel;
	static JButton mongodbSingleOkButton;
	static JButton mongodbSingleCancleButton;
	
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
				JDialog jd = new JDialog();
				if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
				{
					jd.setTitle("Select database");
				}
				else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
				{
					jd.setTitle("选择数据库");
				}
				
				oracleSingleUrlText = new JTextField(30);
				oracleSingleUrlText.setText(MainFrame.properties.getProperty("oracle_single_sql", "jdbc:oracle:thin:@//127.0.0.1:1521/ORCL"));
				oracleSingleTableText = new JTextField(30);
				oracleSingleTableText.setText(MainFrame.properties.getProperty("oracle_single_table", "THUMiner_test"));
				oracleSingleUserText = new JTextField(30);
				oracleSingleUserText.setText(MainFrame.properties.getProperty("oracle_single_user", "c##a"));
				oracleSinglePasswordText = new JPasswordField(30);
				
				if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
				{
					oracleSingleOkButton = new JButton("Connect");
				}
				else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
				{
					oracleSingleOkButton = new JButton("连接");
				}
				oracleSingleOkButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
		                String driver = "oracle.jdbc.driver.OracleDriver";
		                String url = oracleSingleUrlText.getText();
		                String table = oracleSingleTableText.getText();
		                String username = oracleSingleUserText.getText();
		                String password = oracleSinglePasswordText.getText();

						try {
					        if (MainFrame.oracleResult != null)
					        	MainFrame.oracleResult.close();
					        if (MainFrame.oracleStatement != null)
					        	MainFrame.oracleStatement.close();
							if (MainFrame.oracleConnection != null)
								MainFrame.oracleConnection.close();
							if (MainFrame.mongoClient != null)
								MainFrame.mongoClient.close();
					        if (MainFrame.derbyResult != null)
					        	MainFrame.derbyResult.close();
					        if (MainFrame.derbyStatement != null)
					        	MainFrame.derbyStatement.close();
							if (MainFrame.derbyConnection != null)
								MainFrame.derbyConnection.close();

		                    Class.forName(driver);
		                    MainFrame.oracleConnection = DriverManager.getConnection(url, username, password);
		                    
		                    MainFrame.bigEventCollection = null;
							MainFrame.bigAnimation = null;
							MainFrame.bigCaseCollection = null;
							MainFrame.bigVariantCollection = null;
	                        MainFrame.bigEventCollectionDerby = null;
	                        MainFrame.bigCaseCollectionDerby = null;
	                        MainFrame.bigVariantCollectionDerby = null;
							MainFrame.bigAnimationDerby = null;
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
					oracleSingleCancleButton = new JButton("Cancle");
				}
				else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
				{
					oracleSingleCancleButton = new JButton("取消");
				}
				oracleSingleCancleButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						jd.dispose();
					}
				});
				
				oracleSingleUrlLabelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 20));
				if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
				{
					oracleSingleUrlLabelPanel.add(new JLabel("Url: "));
				}
				else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
				{
					oracleSingleUrlLabelPanel.add(new JLabel("数据库地址："));
				}
				oracleSingleUrlLabelPanel.add(oracleSingleUrlText);
				
				oracleSingleUserLabelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 20));
				if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
				{
					oracleSingleUserLabelPanel.add(new JLabel("User Name: "));
				}
				else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
				{
					oracleSingleUserLabelPanel.add(new JLabel("用户名："));
				}
				oracleSingleUserLabelPanel.add(oracleSingleUserText);
				
				oracleSinglePasswordLabelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 20));
				if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
				{
					oracleSinglePasswordLabelPanel.add(new JLabel("Password: "));
				}
				else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
				{
					oracleSinglePasswordLabelPanel.add(new JLabel("密码："));
				}
				oracleSinglePasswordLabelPanel.add(oracleSinglePasswordText);
				
				oracleSingleTableLabelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 20));
				if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
				{
					oracleSingleTableLabelPanel.add(new JLabel("Table: "));
				}
				else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
				{
					oracleSingleTableLabelPanel.add(new JLabel("表名："));
				}
				oracleSingleTableLabelPanel.add(oracleSingleTableText);
				
				oracleSingleButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 20));
				oracleSingleButtonPanel.add(oracleSingleOkButton);
				oracleSingleButtonPanel.add(oracleSingleCancleButton);
				
				oracleSingleLabelPanel = new JPanel(new GridLayout(0, 1));
				oracleSingleLabelPanel.add(oracleSingleUrlLabelPanel);
				oracleSingleLabelPanel.add(oracleSingleUserLabelPanel);
				oracleSingleLabelPanel.add(oracleSinglePasswordLabelPanel);
				oracleSingleLabelPanel.add(oracleSingleTableLabelPanel);
				
				oracleSingleSelectPanel = new JPanel(new BorderLayout());
				oracleSingleSelectPanel.add(oracleSingleLabelPanel, BorderLayout.CENTER);
				oracleSingleSelectPanel.add(oracleSingleButtonPanel, BorderLayout.SOUTH);

				oracleMultiUrlText = new JTextField(30);
				oracleMultiUrlText.setText(MainFrame.properties.getProperty("oracle_multi_sql", "jdbc:oracle:thin:@//127.0.0.1:1521/ORCL"));
				oracleMultiUserText = new JTextField(30);
				oracleMultiUserText.setText(MainFrame.properties.getProperty("oracle_multi_user", "c##a"));
				oracleMultiPasswordText = new JPasswordField(30);
				
				if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
				{
					oracleMultiOkButton = new JButton("Connect");
				}
				else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
				{
					oracleMultiOkButton = new JButton("连接");
				}
				oracleMultiOkButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
		                String driver = "oracle.jdbc.driver.OracleDriver";
		                String url = oracleMultiUrlText.getText();
		                String username = oracleMultiUserText.getText();
		                String password = oracleMultiPasswordText.getText();
		                String[][] tableRelation = new String[5][4];
		                oracleMultiTable.editingStopped(changeEvent);
		                
						for(int i = 0; i < oracleMultiTable.getRowCount(); i++) 
						{
							String table1 = (String)oracleMultiTable.getValueAt(i, 0);
							String tableEventid1 = (String)oracleMultiTable.getValueAt(i, 1);
							String table2 = (String)oracleMultiTable.getValueAt(i, 2);
							String tableEventid2 = (String)oracleMultiTable.getValueAt(i, 3);
							if(table1 == null) table1 = "";
							if(tableEventid1 == null) tableEventid1 = "";
							if(table2 == null) table2 = "";
							if(tableEventid2 == null) tableEventid2 = "";
							if(!((table1.equals("") && tableEventid1.equals("") && table2.equals("") && tableEventid2.equals(""))
									|| (!table1.equals("") && !tableEventid1.equals("") && !table2.equals("") && !tableEventid2.equals(""))))
							{
								System.out.println("Table or Event Id is missing!");
								return;
							}
							tableRelation[i][0] = table1;
							tableRelation[i][1] = tableEventid1;
							tableRelation[i][2] = table2;
							tableRelation[i][3] = tableEventid2;
						}

						try {
							try {
						        if (MainFrame.oracleResult != null)
						        	MainFrame.oracleResult.close();
						        if (MainFrame.oracleStatement != null)
						        	MainFrame.oracleStatement.close();
								if (MainFrame.oracleConnection != null)
									MainFrame.oracleConnection.close();
								if (MainFrame.mongoClient != null)
									MainFrame.mongoClient.close();
						        if (MainFrame.derbyResult != null)
						        	MainFrame.derbyResult.close();
						        if (MainFrame.derbyStatement != null)
						        	MainFrame.derbyStatement.close();
								if (MainFrame.derbyConnection != null)
									MainFrame.derbyConnection.close();
			                } catch (Exception e1) {
			                    e1.printStackTrace();
			                }
							deleteDir(new File("data_tmp"));

		                    Class.forName(driver);
		                    MainFrame.oracleConnection = DriverManager.getConnection(url, username, password);
		                    
		                    MainFrame.bigEventCollection = null;
							MainFrame.bigAnimation = null;
							MainFrame.bigCaseCollection = null;
							MainFrame.bigVariantCollection = null;
	                        MainFrame.bigEventCollectionDerby = null;
	                        MainFrame.bigCaseCollectionDerby = null;
	                        MainFrame.bigVariantCollectionDerby = null;
							MainFrame.bigAnimationDerby = null;
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
	    					ImportPanel importPanel = new ImportPanel(tableRelation);
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
					oracleMultiCancleButton = new JButton("Cancle");
				}
				else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
				{
					oracleMultiCancleButton = new JButton("取消");
				}
				oracleMultiCancleButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						jd.dispose();
					}
				});
				
				oracleMultiUrlLabelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 20));
				if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
				{
					oracleMultiUrlLabelPanel.add(new JLabel("Url: "));
				}
				else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
				{
					oracleMultiUrlLabelPanel.add(new JLabel("数据库地址："));
				}
				oracleMultiUrlLabelPanel.add(oracleMultiUrlText);
				
				oracleMultiUserLabelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 20));
				if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
				{
					oracleMultiUserLabelPanel.add(new JLabel("User Name: "));
				}
				else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
				{
					oracleMultiUserLabelPanel.add(new JLabel("用户名："));
				}
				oracleMultiUserLabelPanel.add(oracleMultiUserText);
				
				oracleMultiPasswordLabelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 20));
				if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
				{
					oracleMultiPasswordLabelPanel.add(new JLabel("Password: "));
				}
				else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
				{
					oracleMultiPasswordLabelPanel.add(new JLabel("密码："));
				}
				oracleMultiPasswordLabelPanel.add(oracleMultiPasswordText);
				
				oracleMultiTableLabelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 20));
				if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
				{
					oracleMultiTableLabelPanel.add(new JLabel("Please entering table name and event id for joining table (Double click to edit)"), BorderLayout.NORTH);
				}
				else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
				{
					oracleMultiTableLabelPanel.add(new JLabel("请输入表名和用于关联的事件编号（双击进行编辑）"), BorderLayout.NORTH);
				}
				
				oracleMultiButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 20));
				oracleMultiButtonPanel.add(oracleMultiOkButton);
				oracleMultiButtonPanel.add(oracleMultiCancleButton);
				
				oracleMultiLabelPanel = new JPanel(new GridLayout(0, 1));
				oracleMultiLabelPanel.add(oracleMultiUrlLabelPanel);
				oracleMultiLabelPanel.add(oracleMultiUserLabelPanel);
				oracleMultiLabelPanel.add(oracleMultiPasswordLabelPanel);
				oracleMultiLabelPanel.add(oracleMultiTableLabelPanel);
				
				String[] oracleHeadlines = new String[4];
				if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
				{
					oracleHeadlines[0] = "Table 1";
					oracleHeadlines[1] = "Event Id 1";
					oracleHeadlines[2] = "Table 2";
					oracleHeadlines[3] = "Event Id 2";
				}
				else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
				{
					oracleHeadlines[0] = "表名1";
					oracleHeadlines[1] = "事件编号1";
					oracleHeadlines[2] = "表名2";
					oracleHeadlines[3] = "事件编号2";
				}
				
				String oracleMultiTableRelation = MainFrame.properties.getProperty("oracle_multi_table", "THUMiner_join1 EventId THUMiner_join2 EventId,THUMiner_join3 EventId THUMiner_join4 EventId,THUMiner_join4 EventId THUMiner_join2 EventId");
				String[] oracleMultiTableRelationArray = oracleMultiTableRelation.split(",");
				String[][] oracleTableData = new String[5][4];
				for(int i = 0; i < 5; i++) 
				{
					oracleTableData[i][0] = "";
					oracleTableData[i][1] = "";
					oracleTableData[i][2] = "";
					oracleTableData[i][3] = "";
				}
				for(int i = 0; i < oracleMultiTableRelationArray.length; i++) 
				{
					oracleTableData[i] = oracleMultiTableRelationArray[i].split(" ");
				}
				
				oracleMultiTable = new JTable(oracleTableData, oracleHeadlines);
				oracleMultiTablePanel = new JScrollPane(oracleMultiTable);
				
				oracleMultiSelectPanel = new JPanel(new BorderLayout());
				oracleMultiSelectPanel.add(oracleMultiLabelPanel, BorderLayout.NORTH);
				oracleMultiSelectPanel.add(oracleMultiTablePanel, BorderLayout.CENTER);
				oracleMultiSelectPanel.add(oracleMultiButtonPanel, BorderLayout.SOUTH);

				oracleTabbedPanel = new JTabbedPane();
				if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
				{
					oracleTabbedPanel.addTab("Single table", null, oracleSingleSelectPanel, "Single table");
					oracleTabbedPanel.addTab("Multi table", null, oracleMultiSelectPanel, "Multi table");
				}
				else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
				{
					oracleTabbedPanel.addTab("单表", null, oracleSingleSelectPanel, "单表");
					oracleTabbedPanel.addTab("多表", null, oracleMultiSelectPanel, "多表");
				}
				
				oracleTabbedPanel.addChangeListener(new ChangeListener(){
					@Override
					public void stateChanged(ChangeEvent e) {
						// TODO Auto-generated method stub
						if(oracleTabbedPanel.getSelectedIndex() == 0)
						{
			        		jd.setSize(new Dimension(510, 340));
			                jd.setPreferredSize(new Dimension(510, 340));
						}
						else if(oracleTabbedPanel.getSelectedIndex() == 1)
						{
			        		jd.setSize(new Dimension(510, 504));
			                jd.setPreferredSize(new Dimension(510, 504));
						}
		                int x = (Toolkit.getDefaultToolkit().getScreenSize().width - jd.getSize().width)/2;
		                int y = (Toolkit.getDefaultToolkit().getScreenSize().height - jd.getSize().height)/2;
		                jd.setLocation(x, y);
					}
				});
				

				mongodbSingleHostText = new JTextField(30);
				mongodbSingleHostText.setText(MainFrame.properties.getProperty("mongo_single_host", "127.0.0.1"));
				mongodbSinglePortText = new JTextField(30);
				mongodbSinglePortText.setText(MainFrame.properties.getProperty("mongo_single_port", "27017"));
				mongodbSingleDBText = new JTextField(30);
				mongodbSingleDBText.setText(MainFrame.properties.getProperty("mongo_single_db", "thuminer"));
				mongodbSingleCollectionText = new JTextField(30);
				mongodbSingleCollectionText.setText(MainFrame.properties.getProperty("mongo_single_collection", "thuminer_test"));
				mongodbSingleUserText = new JTextField(30);
				mongodbSingleUserText.setText(MainFrame.properties.getProperty("mongodb_single_user", "abc"));
				mongodbSinglePasswordText = new JPasswordField(30);

				if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
				{
					mongodbSingleOkButton = new JButton("Connect");
				}
				else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
				{
					mongodbSingleOkButton = new JButton("连接");
				}
				mongodbSingleOkButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
		                String host = mongodbSingleHostText.getText();
		                String port = mongodbSinglePortText.getText();
		                String dbName = mongodbSingleDBText.getText();
		                String collectionName = mongodbSingleCollectionText.getText();
		                String username = mongodbSingleUserText.getText();
		                String password = mongodbSinglePasswordText.getText();

						try {
					        if (MainFrame.oracleResult != null)
					        	MainFrame.oracleResult.close();
					        if (MainFrame.oracleStatement != null)
					        	MainFrame.oracleStatement.close();
							if (MainFrame.oracleConnection != null)
								MainFrame.oracleConnection.close();
							if (MainFrame.mongoClient != null)
								MainFrame.mongoClient.close();
					        if (MainFrame.derbyResult != null)
					        	MainFrame.derbyResult.close();
					        if (MainFrame.derbyStatement != null)
					        	MainFrame.derbyStatement.close();
							if (MainFrame.derbyConnection != null)
								MainFrame.derbyConnection.close();
							
							if(!username.equals("") && !password.equals(""))
							{
								MongoCredential credential = MongoCredential.createCredential(username, dbName, password.toCharArray()); 
								ServerAddress serverAddress = new ServerAddress(host, Integer.parseInt(port)); 
								MainFrame.mongoClient = new MongoClient(serverAddress, Arrays.asList(credential));
							}
							else
							{
								MainFrame.mongoClient = new MongoClient(host, Integer.parseInt(port));
							}
							DB db = MainFrame.mongoClient.getDB(dbName);
							//boolean auth = db.authenticate(username, password.toCharArray());
							DBCollection coll = db.getCollection(collectionName); 
							
							MainFrame.bigEventCollection = null;
							MainFrame.bigAnimation = null;
							MainFrame.bigCaseCollection = null;
							MainFrame.bigVariantCollection = null;
	                        MainFrame.bigEventCollectionDerby = null;
	                        MainFrame.bigCaseCollectionDerby = null;
	                        MainFrame.bigVariantCollectionDerby = null;
							MainFrame.bigAnimationDerby = null;
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
	    					ImportPanel importPanel = new ImportPanel(coll);
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
					mongodbSingleCancleButton = new JButton("Cancle");
				}
				else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
				{
					mongodbSingleCancleButton = new JButton("取消");
				}
				mongodbSingleCancleButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						jd.dispose();
					}
				});
				
				mongodbSingleHostLabelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 20));
				if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
				{
					mongodbSingleHostLabelPanel.add(new JLabel("Host: "));
				}
				else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
				{
					mongodbSingleHostLabelPanel.add(new JLabel("主机："));
				}
				mongodbSingleHostLabelPanel.add(mongodbSingleHostText);
				
				mongodbSinglePortLabelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 20));
				if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
				{
					mongodbSinglePortLabelPanel.add(new JLabel("Port: "));
				}
				else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
				{
					mongodbSinglePortLabelPanel.add(new JLabel("端口："));
				}
				mongodbSinglePortLabelPanel.add(mongodbSinglePortText);
				
				mongodbSingleDBLabelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 20));
				if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
				{
					mongodbSingleDBLabelPanel.add(new JLabel("Database: "));
				}
				else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
				{
					mongodbSingleDBLabelPanel.add(new JLabel("数据库："));
				}
				mongodbSingleDBLabelPanel.add(mongodbSingleDBText);
				
				mongodbSingleCollectionLabelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 20));
				if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
				{
					mongodbSingleCollectionLabelPanel.add(new JLabel("Collection: "));
				}
				else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
				{
					mongodbSingleCollectionLabelPanel.add(new JLabel("集合："));
				}
				mongodbSingleCollectionLabelPanel.add(mongodbSingleCollectionText);
				
				mongodbSingleUserLabelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 20));
				if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
				{
					mongodbSingleUserLabelPanel.add(new JLabel("User Name: "));
				}
				else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
				{
					mongodbSingleUserLabelPanel.add(new JLabel("用户名："));
				}
				mongodbSingleUserLabelPanel.add(mongodbSingleUserText);
				
				mongodbSinglePasswordLabelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 20));
				if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
				{
					mongodbSinglePasswordLabelPanel.add(new JLabel("Password: "));
				}
				else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
				{
					mongodbSinglePasswordLabelPanel.add(new JLabel("密码："));
				}
				mongodbSinglePasswordLabelPanel.add(mongodbSinglePasswordText);
				
				mongodbSingleButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 20));
				mongodbSingleButtonPanel.add(mongodbSingleOkButton);
				mongodbSingleButtonPanel.add(mongodbSingleCancleButton);
				
				mongodbSingleLabelPanel = new JPanel(new GridLayout(0, 1));
				mongodbSingleLabelPanel.add(mongodbSingleHostLabelPanel);
				mongodbSingleLabelPanel.add(mongodbSinglePortLabelPanel);
				mongodbSingleLabelPanel.add(mongodbSingleDBLabelPanel);
				mongodbSingleLabelPanel.add(mongodbSingleCollectionLabelPanel);
				mongodbSingleLabelPanel.add(mongodbSingleUserLabelPanel);
				mongodbSingleLabelPanel.add(mongodbSinglePasswordLabelPanel);
				
				mongodbSingleSelectPanel = new JPanel(new BorderLayout());
				mongodbSingleSelectPanel.add(mongodbSingleLabelPanel, BorderLayout.CENTER);
				mongodbSingleSelectPanel.add(mongodbSingleButtonPanel, BorderLayout.SOUTH);

				mongodbTabbedPanel = new JTabbedPane();
				if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
				{
					mongodbTabbedPanel.addTab("Single table", null, mongodbSingleSelectPanel, "Single table");
				}
				else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
				{
					mongodbTabbedPanel.addTab("单表", null, mongodbSingleSelectPanel, "单表");
				}
				
				mongodbTabbedPanel.addChangeListener(new ChangeListener(){
					@Override
					public void stateChanged(ChangeEvent e) {
						// TODO Auto-generated method stub
						if(mongodbTabbedPanel.getSelectedIndex() == 0)
						{
			        		jd.setSize(new Dimension(510, 400));
			                jd.setPreferredSize(new Dimension(510, 400));
						}
		                int x = (Toolkit.getDefaultToolkit().getScreenSize().width - jd.getSize().width)/2;
		                int y = (Toolkit.getDefaultToolkit().getScreenSize().height - jd.getSize().height)/2;
		                jd.setLocation(x, y);
					}
				});
				
				
				tabbedPanel = new JTabbedPane();
				tabbedPanel.addTab("Oracle", null, oracleTabbedPanel, "Oracle");
				tabbedPanel.addTab("MongoDB", null, mongodbTabbedPanel, "MongoDB");
				
				tabbedPanel.addChangeListener(new ChangeListener(){
					@Override
					public void stateChanged(ChangeEvent e) {
						// TODO Auto-generated method stub
						if(tabbedPanel.getSelectedIndex() == 0)
						{
							if(oracleTabbedPanel.getSelectedIndex() == 0)
							{
				        		jd.setSize(new Dimension(510, 340));
				                jd.setPreferredSize(new Dimension(510, 340));
							}
							else if(oracleTabbedPanel.getSelectedIndex() == 1)
							{
				        		jd.setSize(new Dimension(510, 504));
				                jd.setPreferredSize(new Dimension(510, 504));
							}
						}
						else if(tabbedPanel.getSelectedIndex() == 1)
						{
							if(mongodbTabbedPanel.getSelectedIndex() == 0)
							{
				        		jd.setSize(new Dimension(510, 400));
				                jd.setPreferredSize(new Dimension(510, 400));
							}
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
