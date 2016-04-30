package wzc.zcminer.frame;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import javax.swing.*;

import com.mongodb.MongoClient;

import wzc.zcminer.global.ActiveCasesOverTimeChart;
import wzc.zcminer.global.ActivityCollection;
import wzc.zcminer.global.BigAnimation;
import wzc.zcminer.global.BigCaseCollection;
import wzc.zcminer.global.BigEventCollection;
import wzc.zcminer.global.BigVariantCollection;
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

//主框架
public class MainFrame {
	static final int WIDTH = 1200;
	static final int HEIGHT = 720;
	static EventCollection eventCollection;
	static GraphNet graphNet;
	static VariantCollection variantCollection;
	static CaseCollection caseCollection;
	static ActiveCasesOverTimeChart activeCasesOverTimeChart;
	static EventsOverTimeChart eventsOverTimeChart;
	static EventsPerCaseChart eventsPerCaseChart;
	static CaseDurationChart caseDurationChart;
	static CaseUtilizationChart caseUtilizationChart;
	static MeanActivityDurationChart meanActivityDurationChart;
	static MeanWaitingTimeChart meanWaitingTimeChart;
	static ActivityCollection activityCollection;
	static ResourceCollection resourceCollection;
	static BigEventCollection bigEventCollection;
	static BigAnimation bigAnimation;
	static BigCaseCollection bigCaseCollection;
	static BigVariantCollection bigVariantCollection;
	static int dataSource = 0; //0:file 1:database 2:big file
	static Connection oracleConnection = null;
	static PreparedStatement oracleStatement = null;
	static ResultSet oracleResult = null;
	static MongoClient mongoClient = null;

	static JFrame mainFrame;
	static FileButton fileButton;
	static DatabaseButton databaseButton;
	static BigFileButton bigFileButton;
	
	static Properties properties;
	static String filename;
	
	public MainFrame() {
		filename = "THUMiner.properties";
		readPropertiesFile();
		
		mainFrame = new JFrame();
		mainFrame.setTitle("THUMiner");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setSize(WIDTH, HEIGHT);

		JPanel startPanel = new JPanel();

		mainFrame.setContentPane(startPanel);

		fileButton = new FileButton();
		databaseButton = new DatabaseButton();
		bigFileButton = new BigFileButton();

		startPanel.setLayout(new BorderLayout());

		JPanel radioPanel = new JPanel();
		radioPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
		if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
			radioPanel.add(new JLabel("Start Importing: "));
		}
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
			radioPanel.add(new JLabel("开始导入："));
		}
		radioPanel.add(fileButton);
		radioPanel.add(databaseButton);
		radioPanel.add(bigFileButton);
		startPanel.add(radioPanel, BorderLayout.CENTER);
		
		mainFrame.setVisible(true);
	}
	
	
	public static void readPropertiesFile()
	{
		properties = new Properties();
		try
		{
			InputStream inputStream = new FileInputStream(filename);
			properties.load(inputStream);
			inputStream.close(); //关闭流
		}
		catch (IOException e)
		{
			properties = new Properties();
			try
			{
			    OutputStream outputStream = new FileOutputStream(filename);
			    properties.setProperty("language", "zhCN");
			    properties.setProperty("separator", ",");
			    properties.setProperty("timestamp", "dd.MM.yy HH:mm");
			    properties.setProperty("header", "true");
			    properties.setProperty("encoding", "GBK");
			    properties.store(outputStream, "Error: File not exist");
			    outputStream.close();
			}
			catch (IOException ioe)
			{
			    e.printStackTrace();
			}
		}
	}

	
	public static void main(String[] args) {
		new MainFrame();
	}
}
