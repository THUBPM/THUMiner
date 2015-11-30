package wzc.zcminer.frame;

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
import javax.swing.filechooser.FileFilter;

import wzc.zcminer.global.ActiveCasesOverTimeChart;
import wzc.zcminer.global.ActivityCollection;
import wzc.zcminer.global.CaseCollection;
import wzc.zcminer.global.CaseDurationChart;
import wzc.zcminer.global.CaseUtilizationChart;
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
	static JPanel databaseSelectPanel;
	static JPanel urlLabelPanel;
	static JTextField urlText;
	static JPanel tableLabelPanel;
	static JTextField tableText;
	static JPanel userLabelPanel;
	static JTextField userText;
	static JPanel passwordLabelPanel;
	static JPasswordField passwordText;
	static JPanel buttonPanel;
	static JButton okButton;
	static JButton cancleButton;
	
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
				
				urlText = new JTextField(30);
				urlText.setText("jdbc:oracle:thin:@//127.0.0.1:1521/ORCL");
				tableText = new JTextField(30);
				tableText.setText("THUMiner_test");
				userText = new JTextField(30);
				userText.setText("c##a");
				passwordText = new JPasswordField(30);
				passwordText.setText("abc");
				
				if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
				{
					okButton = new JButton("Open");
				}
				else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
				{
					okButton = new JButton("打开");
				}
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
		                String driver = "oracle.jdbc.driver.OracleDriver";
		                String url = urlText.getText();
		                String table = tableText.getText();
		                String username = userText.getText();
		                String password = passwordText.getText();

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
					cancleButton = new JButton("Cancle");
				}
				else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
				{
					cancleButton = new JButton("取消");
				}
				cancleButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						jd.dispose();
					}
				});
				
				urlLabelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 10));
				if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
				{
					urlLabelPanel.add(new JLabel("Url: "));
				}
				else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
				{
					urlLabelPanel.add(new JLabel("数据库地址："));
				}
				urlLabelPanel.add(urlText);
				
				tableLabelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 10));
				if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
				{
					tableLabelPanel.add(new JLabel("Table: "));
				}
				else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
				{
					tableLabelPanel.add(new JLabel("表名："));
				}
				tableLabelPanel.add(tableText);
				
				userLabelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 10));
				if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
				{
					userLabelPanel.add(new JLabel("User Name: "));
				}
				else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
				{
					userLabelPanel.add(new JLabel("用户名："));
				}
				userLabelPanel.add(userText);
				
				passwordLabelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 10));
				if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
				{
					passwordLabelPanel.add(new JLabel("Password: "));
				}
				else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
				{
					passwordLabelPanel.add(new JLabel("密码："));
				}
				passwordLabelPanel.add(passwordText);
				
				buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 10));
				buttonPanel.add(okButton);
				buttonPanel.add(cancleButton);
				
				databaseSelectPanel = new JPanel(new GridLayout(0, 1));
				databaseSelectPanel.add(urlLabelPanel);
				databaseSelectPanel.add(tableLabelPanel);
				databaseSelectPanel.add(userLabelPanel);
				databaseSelectPanel.add(passwordLabelPanel);
				databaseSelectPanel.add(buttonPanel);
				jd.add(databaseSelectPanel);

        		jd.setModal(true);
        		jd.setSize(new Dimension(510, 240));
                jd.setPreferredSize(new Dimension(510, 240));
                int x = (Toolkit.getDefaultToolkit().getScreenSize().width - jd.getSize().width)/2;
                int y = (Toolkit.getDefaultToolkit().getScreenSize().height - jd.getSize().height)/2;
                jd.setLocation(x, y); 
                jd.setVisible(true);
			}
		});
	}
	
}
