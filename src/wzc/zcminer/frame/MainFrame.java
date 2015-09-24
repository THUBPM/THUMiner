package wzc.zcminer.frame;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

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

	static JFrame mainFrame;
	static JButton fileButton;
	static JTextField separatorText;
	static JCheckBox tableHead;
	
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

		if(properties.getProperty("language").equals("enUS"))
		{
			fileButton = new JButton("Select csv");
		}
		else if(properties.getProperty("language").equals("zhCN"))
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
				if(properties.getProperty("language").equals("enUS"))
				{
					fileENames[0][0] = ".csv";
					fileENames[0][1] = "Csv files(*.csv)";
				}
				else if(properties.getProperty("language").equals("zhCN"))
				{
					fileENames[0][0] = ".csv";
					fileENames[0][1] = "Csv文件(*.csv)";
				}
				fd.addChoosableFileFilter(new FileFilter() {
					public boolean accept(File file) {
						return true;
					}

					public String getDescription() {
						if(properties.getProperty("language").equals("enUS"))
						{
							return "All files(*.*)";
						}
						else if(properties.getProperty("language").equals("zhCN"))
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
					mainFrame.getContentPane().removeAll();
					System.gc();
					ImportPanel importPanel = new ImportPanel(file.getAbsolutePath(), separatorText.getText().charAt(0), tableHead.isSelected());
					mainFrame.setContentPane(importPanel);
					mainFrame.setVisible(true);
					System.gc();
				}

			}
		});

		separatorText = new JTextField(2);
		separatorText.setText(properties.getProperty("separator"));
		
		if(properties.getProperty("language").equals("enUS"))
		{
			tableHead = new JCheckBox("Header");
		}
		else if(properties.getProperty("language").equals("zhCN"))
		{
			tableHead = new JCheckBox("表头");
		}
		if(properties.getProperty("header").equals("true"))
		{
			tableHead.setSelected(true);
		}
		else if(properties.getProperty("header").equals("false"))
		{
			tableHead.setSelected(false);
		}
		
		eventCollection = new EventCollection();
		graphNet = new GraphNet();
		variantCollection = new VariantCollection();
		caseCollection = new CaseCollection();
		activeCasesOverTimeChart = new ActiveCasesOverTimeChart();
		eventsOverTimeChart = new EventsOverTimeChart();
		eventsPerCaseChart = new EventsPerCaseChart();
		caseDurationChart = new CaseDurationChart();
		caseUtilizationChart = new CaseUtilizationChart();
		meanActivityDurationChart = new MeanActivityDurationChart();
		meanWaitingTimeChart = new MeanWaitingTimeChart();
		activityCollection = new ActivityCollection();
		resourceCollection = new ResourceCollection();
		
		startPanel.setLayout(new BorderLayout());

		JPanel radioPanel = new JPanel();
		radioPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20 ));
		radioPanel.add(fileButton);
		if(properties.getProperty("language").equals("enUS"))
		{
			radioPanel.add(new JLabel("Csv seperator: "));
		}
		else if(properties.getProperty("language").equals("zhCN"))
		{
			radioPanel.add(new JLabel("Csv分隔符："));
		}
		radioPanel.add(separatorText);
		radioPanel.add(tableHead);
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
			    properties.store(outputStream, "Error: File not exist");
			    outputStream.close();
			}
			catch (IOException ioe)
			{
			    e.printStackTrace();
			}
		}
		try
		{
			if(!(properties.getProperty("header").equals("true") || properties.getProperty("header").equals("false") || properties.getProperty("language").equals("enUS") || properties.getProperty("language").equals("zhCN")))
			{
				properties = new Properties();
				try
				{
				    OutputStream outputStream = new FileOutputStream(filename);
				    properties.setProperty("language", "zhCN");
				    properties.setProperty("separator", ",");
				    properties.setProperty("timestamp", "dd.MM.yy HH:mm");
				    properties.setProperty("header", "true");
				    properties.store(outputStream, "Error: Language or Header value is invalid");
				    outputStream.close();
				}
				catch (IOException e)
				{
				    e.printStackTrace();
				}
			}
		}
		catch (Exception e)
		{
			properties = new Properties();
			try
			{
			    OutputStream outputStream = new FileOutputStream(filename);
			    properties.setProperty("language", "zhCN");
			    properties.setProperty("separator", ",");
			    properties.setProperty("timestamp", "dd.MM.yy HH:mm");
			    properties.setProperty("header", "true");
			    properties.store(outputStream, "Error: Language or Header value not exist");
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
