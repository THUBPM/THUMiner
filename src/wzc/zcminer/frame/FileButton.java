package wzc.zcminer.frame;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.charset.Charset;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedMap;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;

import wzc.zcminer.global.ActiveCasesOverTimeChart;
import wzc.zcminer.global.ActivityCollection;
import wzc.zcminer.global.BigCaseCollection;
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

//选择文件按钮
public class FileButton extends JButton{
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
    
	static JTextField separatorText;
	static JCheckBox tableHead;
	static JComboBox<String> encoding;

	public FileButton() {
		super();
		
		if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
			setText("Select file");
		}
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
			setText("选择文件");
		}
		//导入数据文件
		addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
				{
					UIManager.put("FileChooser.cancelButtonText", "Cancel");
				}
				else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
				{
					UIManager.put("FileChooser.cancelButtonText", "取消");
				}
				if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
				{
					UIManager.put("FileChooser.openButtonText", "Open");
				}
				else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
				{
					UIManager.put("FileChooser.openButtonText", "打开");
				}
				
				JFileChooser fd = new JFileChooser();
				fd.setCurrentDirectory(new File("."));
				fd.setAcceptAllFileFilterUsed(false);
				if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
				{
					fd.setDialogTitle("Select file");
				}
				else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
				{
					fd.setDialogTitle("选择文件");
				}
				final String[][] fileENames;
				fileENames = new String[4][2];
				if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
				{
					fileENames[0][0] = ".csv";
					fileENames[0][1] = "Csv files(*.csv)";
					fileENames[1][0] = ".xls";
					fileENames[1][1] = "Xls files(*.xls)";
					fileENames[2][0] = ".xlsx";
					fileENames[2][1] = "Xlsx files(*.xlsx)";
					fileENames[3][0] = ".txt";
					fileENames[3][1] = "Txt files(*.txt)";
				}
				else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
				{
					fileENames[0][0] = ".csv";
					fileENames[0][1] = "Csv文件(*.csv)";
					fileENames[1][0] = ".xls";
					fileENames[1][1] = "Xls文件(*.xls)";
					fileENames[2][0] = ".xlsx";
					fileENames[2][1] = "Xlsx文件(*.xlsx)";
					fileENames[3][0] = ".txt";
					fileENames[3][1] = "Txt文件(*.txt)";
				}
				fd.addChoosableFileFilter(new FileFilter() {
					public boolean accept(File file) {
						return true;
					}

					public String getDescription() {
						if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
						{
							return "All files(*.*)";
						}
						else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
						{
							return "所有文件(*.*)";
						}
						return "";
					}
				});				
				//文件操作
				for (final String[] fileEName : fileENames) {

					fd.addChoosableFileFilter(new javax.swing.filechooser.FileFilter() {

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
				fd.setFileFilter(fd.getChoosableFileFilters()[1]);

				ArrayList<String> arrayList = new ArrayList<String>();
		        SortedMap<String, Charset> map = Charset.availableCharsets();  
		        Set<String> set = map.keySet();  
		        Iterator<String> ite = set.iterator();  
		        while (ite.hasNext()) {  
		        	String key = ite.next();  
		        	arrayList.add(key);
		        }  

				String[] boxString = arrayList.toArray(new String[0]);
				encoding = new JComboBox<String>(boxString);
				encoding.setSelectedItem(MainFrame.properties.getProperty("encoding", "GBK"));
				
				separatorText = new JTextField(2);
				separatorText.setText(MainFrame.properties.getProperty("separator", ","));
				
        		if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
        		{
        			tableHead = new JCheckBox("Header");
        		}
        		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
        		{
        			tableHead = new JCheckBox("表头");
        		}
        		if(MainFrame.properties.getProperty("header", "true").equals("true"))
        		{
        			tableHead.setSelected(true);
        		}
        		else if(MainFrame.properties.getProperty("header", "true").equals("false"))
        		{
        			tableHead.setSelected(false);
        		}
        		
        		fd.setLayout(new FlowLayout(FlowLayout.CENTER));
        		JPanel filePropoties = new JPanel();
        		if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
        		{
        			filePropoties.add(new JLabel("Encoding: "));
        		}
        		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
        		{
        			filePropoties.add(new JLabel("文件编码："));
        		}
        		filePropoties.add(encoding);
        		if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
        		{
        			filePropoties.add(new JLabel("Seperator: "));
        		}
        		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
        		{
        			filePropoties.add(new JLabel("分隔符："));
        		}
        		filePropoties.add(separatorText);
        		filePropoties.add(tableHead);
        		fd.add(filePropoties, 3);
        		
        		fd.setPreferredSize(new Dimension(510, 340));
                int result = fd.showOpenDialog(new JLabel());
                if(result == JFileChooser.CANCEL_OPTION)
                {
                	return;
                }
                if(result == JFileChooser.APPROVE_OPTION)
                {
                    File file = fd.getSelectedFile();
                    if (file!=null){
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
    					ImportPanel importPanel = new ImportPanel(file.getAbsolutePath(), separatorText.getText().charAt(0), tableHead.isSelected(), encoding.getItemAt(encoding.getSelectedIndex()));
    					MainFrame.mainFrame.setContentPane(importPanel);
    					MainFrame.mainFrame.setVisible(true);
    					System.gc();
    					
    					MainFrame.dataSource = 0;
                    }
                }
			}
		});
	}
	
}
