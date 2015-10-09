package wzc.zcminer.frame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
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

//选择文件按钮
public class FileButton extends JButton{
	static JTextField separatorText;
	static JCheckBox tableHead;
	static JComboBox<String> encoding;

	public FileButton() {
		super();
		
		if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
			setText("Select csv");
		}
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
			setText("选择csv文件");
		}
		//导入数据文件
		addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fd = new JFileChooser();
				fd.setCurrentDirectory(new File("."));
				fd.setAcceptAllFileFilterUsed(false);
				final String[][] fileENames;
				fileENames = new String[1][2];
				if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
				{
					fileENames[0][0] = ".csv";
					fileENames[0][1] = "Csv files(*.csv)";
				}
				else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
				{
					fileENames[0][0] = ".csv";
					fileENames[0][1] = "Csv文件(*.csv)";
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

				String[] boxString = {"Big5", "Big5-HKSCS", "CESU-8", "EUC-JP", "EUC-KR", "GB18030", "GB2312",
						"GBK", "IBM-Thai", "IBM00858", "IBM01140", "IBM01141", "IBM01142", "IBM01143", "IBM01144",
						"IBM01145", "IBM01146", "IBM01147", "IBM01148", "IBM01149", "IBM037", "IBM1026", "IBM1047",
						"IBM273", "IBM277", "IBM278", "IBM280", "IBM284", "IBM285", "IBM290", "IBM297", "IBM420",
						"IBM424", "IBM437", "IBM500", "IBM775", "IBM850", "IBM852", "IBM855", "IBM857", "IBM860",
						"IBM861", "IBM862", "IBM863", "IBM864", "IBM865", "IBM866", "IBM868", "IBM869", "IBM870",
						"IBM871", "IBM918", "ISO-2022-CN", "ISO-2022-JP", "ISO-2022-JP-2", "ISO-2022-KR",
						"ISO-8859-1", "ISO-8859-13", "ISO-8859-15", "ISO-8859-2", "ISO-8859-3", "ISO-8859-4",
						"ISO-8859-5", "ISO-8859-6", "ISO-8859-7", "ISO-8859-8", "ISO-8859-9", "JIS_X0201",
						"JIS_X0212-1990", "KOI8-R", "KOI8-U", "Shift_JIS", "TIS-620", "US-ASCII", "UTF-16",
						"UTF-16BE", "UTF-16LE", "UTF-32", "UTF-32BE", "UTF-32LE", "UTF-8", "windows-1250",
						"windows-1251", "windows-1252", "windows-1253", "windows-1254", "windows-1255",
						"windows-1256", "windows-1257", "windows-1258", "windows-31j", "x-Big5-HKSCS-2001",
						"x-Big5-Solaris", "x-euc-jp-linux", "x-EUC-TW", "x-eucJP-Open", "x-IBM1006", "x-IBM1025",
						"x-IBM1046", "x-IBM1097", "x-IBM1098", "x-IBM1112", "x-IBM1122", "x-IBM1123", "x-IBM1124",
						"x-IBM1364", "x-IBM1381", "x-IBM1383", "x-IBM300", "x-IBM33722", "x-IBM737", "x-IBM833",
						"x-IBM834", "x-IBM856", "x-IBM874", "x-IBM875", "x-IBM921", "x-IBM922", "x-IBM930",
						"x-IBM933", "x-IBM935", "x-IBM937", "x-IBM939", "x-IBM942", "x-IBM942C", "x-IBM943",
						"x-IBM943C", "x-IBM948", "x-IBM949", "x-IBM949C", "x-IBM950", "x-IBM964", "x-IBM970",
						"x-ISCII91", "x-ISO-2022-CN-CNS", "x-ISO-2022-CN-GB", "x-iso-8859-11", "x-JIS0208",
						"x-JISAutoDetect", "x-Johab", "x-MacArabic", "x-MacCentralEurope", "x-MacCroatian",
						"x-MacCyrillic", "x-MacDingbat", "x-MacGreek", "x-MacHebrew", "x-MacIceland", "x-MacRoman",
						"x-MacRomania", "x-MacSymbol", "x-MacThai", "x-MacTurkish", "x-MacUkraine", "x-MS932_0213",
						"x-MS950-HKSCS", "x-MS950-HKSCS-XP", "x-mswin-936", "x-PCK", "x-SJIS_0213", "x-UTF-16LE-BOM",
						"X-UTF-32BE-BOM", "X-UTF-32LE-BOM", "x-windows-50220", "x-windows-50221", "x-windows-874",
						"x-windows-949", "x-windows-950", "x-windows-iso2022jp"};
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
        			filePropoties.add(new JLabel("Csv seperator: "));
        		}
        		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
        		{
        			filePropoties.add(new JLabel("Csv分隔符："));
        		}
        		filePropoties.add(separatorText);
        		filePropoties.add(tableHead);
        		fd.add(filePropoties, 3);
        		
				if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
				{
					fd.setApproveButtonText("Open");
				}
				else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
				{
					fd.setApproveButtonText("打开");
				}
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
                    }
                }
			}
		});
	}
	
}
