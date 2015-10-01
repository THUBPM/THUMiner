package wzc.zcminer.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

import wzc.zcminer.global.ActiveCasesOverTimeChart;
import wzc.zcminer.global.ActivityCollection;
import wzc.zcminer.global.CaseCollection;
import wzc.zcminer.global.CaseDurationChart;
import wzc.zcminer.global.CaseUtilizationChart;
import wzc.zcminer.global.EventCollection;
import wzc.zcminer.global.Event;
import wzc.zcminer.global.EventsOverTimeChart;
import wzc.zcminer.global.EventsPerCaseChart;
import wzc.zcminer.global.GraphNet;
import wzc.zcminer.global.MeanActivityDurationChart;
import wzc.zcminer.global.MeanWaitingTimeChart;
import wzc.zcminer.global.ResourceCollection;
import wzc.zcminer.global.VariantCollection;
import jdk.internal.org.objectweb.asm.tree.analysis.Value;

import com.mxgraph.layout.mxParallelEdgeLayout;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.layout.orthogonal.mxOrthogonalLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxGraph;
import com.sun.media.jfxmedia.events.NewFrameEvent;
//map面板
public class MapPanel extends JPanel implements ComponentListener {
	//参数滑块
	static JSlider pathSlider;   
	static JSlider activitySlider;
	//动画速度滑块
	static JSlider animationSlider;
	//类型选择
	static JComboBox<String> modelType;
	static JPanel sliderJPanel;
	static JPanel labelPanel;
	static JPanel headPanel;
	static JPanel centerPanel;
	static JPanel animationPanel;
	static JPanel controlPanel;
	static JButton animationButton;
	//切换页面
	static JPanel buttonPanel;
	static JButton fileButton;
	static JButton mapButton;
	static JButton staticsButton;
	static JButton casesButton;
	static JTextField separatorText;
	static JCheckBox tableHead;
	static JComboBox<String> encoding;
	
	//jgraph面板
	mxGraphComponent graphComponent;
	mxGraph graph;
	Object parent;
	Timer timer;
	int timeFlag;
	long speed;
	long currentTime;
	//图像节点
	Object[]  v;
	
	public MapPanel() {
		// TODO Auto-generated constructor stub

		setLayout(new BorderLayout());

		controlPanel = new JPanel();
		controlPanel.setLayout(new BorderLayout());
		
		headPanel = new JPanel();
		headPanel.setLayout(new BorderLayout());
		
		centerPanel = new JPanel(new BorderLayout());
		
		sliderJPanel = new JPanel();
		sliderJPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		labelPanel = new JPanel();
		labelPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
			labelPanel.add(new JLabel("Path"));
			labelPanel.add(new JLabel("Activity"));
		}
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
			labelPanel.add(new JLabel("路径数"));
			labelPanel.add(new JLabel("活动数"));
	    }
		
		animationPanel = new JPanel(new FlowLayout());
		
		//选择模块初始
		if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
			String[] boxString = {"Absolute frequence", "Total duration", "Mean duration", "Max duration"
					, "Min duration", "Case frequence", "Max repetition"};
			modelType = new JComboBox<String>(boxString);
			modelType.setSelectedItem("Absolute frequence");
		}
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
			String[] boxString = {"事件频度", "总时间", "平均时间", "最大时间"
					, "最小时间", "实例频度", "重复频度"};
			modelType = new JComboBox<String>(boxString);
			modelType.setSelectedItem("事件频度");
	    }
		modelType.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent evt) {
				if(evt.getStateChange() == ItemEvent.SELECTED){
					paintGraph();
				} 
			}
		});

		//路径参数滑块
		pathSlider = new JSlider(JSlider.VERTICAL);
		pathSlider.setMinimum(0);
		int activityCount = MainFrame.graphNet.activityCount - 1;
		pathSlider.setMaximum(activityCount * activityCount);
		pathSlider.setValue(activityCount * activityCount);
		pathSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (pathSlider.getValueIsAdjusting() != true) {
					paintGraph();
				}
			}
		});
		sliderJPanel.add(pathSlider);
		//活动参数滑块
		activitySlider = new JSlider(JSlider.VERTICAL);
		activitySlider.setMinimum(0);
		activitySlider.setMaximum(MainFrame.graphNet.activityCount-1);
		activitySlider.setValue(0);
		//activitySlider.setExtent(1);
		activitySlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				if (activitySlider.getValueIsAdjusting() != true) {
					paintGraph();
				}
			}
		});
		sliderJPanel.add(activitySlider);

		headPanel.add(labelPanel, BorderLayout.NORTH);
		headPanel.add(sliderJPanel, BorderLayout.CENTER);
	//	centerPanel.add(sliderJPanel,BorderLayout.CENTER);
		//动画设置
		speed = 1;
		animationSlider = new JSlider();
		animationSlider.setMinimum(1);
		long period =MainFrame.graphNet.endTime- MainFrame.graphNet.beginTime;
		animationSlider.setMaximum((int) (period / 50));
		animationSlider.setValue(1);
		animationSlider.setExtent(1);
		animationSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				if (animationSlider.getValueIsAdjusting() != true) {
					speed = animationSlider.getValue();
				}
			}
		});
		timeFlag = 0;
		if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
			animationButton = new JButton("Animation");
		}
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
			animationButton = new JButton("播放动画");
	    }
		animationSlider.setEnabled(false);
		animationButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (timeFlag == 0){
					timeFlag = 1;
					if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
					{
						animationButton.setText("Stop");
					}
					else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
					{
						animationButton.setText("结束播放");
				    }
					modelType.setEnabled(false);
					animationSlider.setEnabled(true);
					pathSlider.setEnabled(false);
					activitySlider.setEnabled(false);
					paintAnimation();
				} else{
					if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
					{
						animationButton.setText("Animation");
					}
					else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
					{
						animationButton.setText("播放动画");
				    }
					modelType.setEnabled(true);
					animationSlider.setEnabled(false);
					pathSlider.setEnabled(true);
					activitySlider.setEnabled(true);
					timeFlag = 0;
					timer.cancel();
					paintGraph();
					
				}
			}
		});
		//面板布局
		if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
			animationPanel.add(new JLabel("Speed"));
		}
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
			animationPanel.add(new JLabel("播放速度"));
	    }
		animationPanel.add(animationSlider);
		centerPanel.add(animationButton,BorderLayout.NORTH);
		
		centerPanel.add(animationPanel, BorderLayout.CENTER);
		
		
		controlPanel.add(headPanel, BorderLayout.NORTH);
		controlPanel.add(centerPanel, BorderLayout.CENTER);
		controlPanel.add(modelType, BorderLayout.SOUTH);
		
		graph = new mxGraph();
		parent = graph.getDefaultParent();
		graphComponent = new mxGraphComponent(graph);
        
		if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
			fileButton = new JButton("Select csv");
		}
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
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
                    ImportPanel importPanel = new ImportPanel(file.getAbsolutePath(), separatorText.getText().charAt(0), tableHead.isSelected(), encoding.getItemAt(encoding.getSelectedIndex()));
                    MainFrame.mainFrame.setContentPane(importPanel);
                    MainFrame.mainFrame.setVisible(true);
                    System.gc();
                }

            }
        });

		if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
	        mapButton = new JButton("Map");
		}
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
	        mapButton = new JButton("示意图");
		}
        mapButton.setEnabled(false);
        mapButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MainFrame.mainFrame.getContentPane().removeAll();
                System.gc();
                MapPanel mapPanel = new MapPanel();
                MainFrame.mainFrame.setContentPane(mapPanel);
                MainFrame.mainFrame.setVisible(true);
                System.gc();
            }
        });
      
		if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
	        staticsButton = new JButton("Statics");
		}
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
	        staticsButton = new JButton("统计");
	    }
        staticsButton.setEnabled(true);
        staticsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MainFrame.mainFrame.getContentPane().removeAll();
                System.gc();
                StaticsPanel staticsPanel = new StaticsPanel();
                MainFrame.mainFrame.setContentPane(staticsPanel);
                MainFrame.mainFrame.setVisible(true);
                System.gc();
            }
        });
        
		if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
	        casesButton = new JButton("Cases");
		}
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
	        casesButton = new JButton("实例");
	    }
        casesButton.setEnabled(true);
        casesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MainFrame.mainFrame.getContentPane().removeAll();
                System.gc();
                CasesPanel casesPanel = new CasesPanel();
                MainFrame.mainFrame.setContentPane(casesPanel);
                MainFrame.mainFrame.setVisible(true);
                System.gc();
            }
        });
        
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
        
		buttonPanel = new JPanel();
		buttonPanel.add(fileButton);
		if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
			buttonPanel.add(new JLabel("Encoding: "));
		}
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
			buttonPanel.add(new JLabel("文件编码："));
		}
		buttonPanel.add(encoding);
		if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
			buttonPanel.add(new JLabel("Csv seperator: "));
		}
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
			buttonPanel.add(new JLabel("Csv分隔符："));
		}
        buttonPanel.add(separatorText);
        buttonPanel.add(tableHead);
        buttonPanel.add(mapButton);
        buttonPanel.add(staticsButton);
        buttonPanel.add(casesButton);
        
        add(buttonPanel, BorderLayout.NORTH);
        /*
         * GridBagConstraints grapgBagConstraints = new GridBagConstraints();
         * grapgBagConstraints.gridwidth = 3; grapgBagConstraints.gridheight =
         * 1; grapgBagConstraints.weightx = 0.7; grapgBagConstraints.weighty =
         * 1; grapgBagConstraints.fill = GridBagConstraints.NONE;
         */
        add(graphComponent, BorderLayout.CENTER);

        /*
         * GridBagConstraints sliderBagConstraints = new GridBagConstraints();
         * sliderBagConstraints.gridwidth = 1; sliderBagConstraints.gridheight =
         * 1; sliderBagConstraints.weightx = 0.3; sliderBagConstraints.weighty =
         * 1; sliderBagConstraints.fill = GridBagConstraints.BOTH;
         */
        add(controlPanel, BorderLayout.EAST);

		graphComponent.addComponentListener(this);
		
	    paintGraph();
	}
	
	
	//rgb转16进制
    public String toHexEncoding(Color color) {
        String R, G, B;
        StringBuffer sb = new StringBuffer();
 
        R = Integer.toHexString(color.getRed());
        G = Integer.toHexString(color.getGreen());
        B = Integer.toHexString(color.getBlue());
 
        R = R.length() == 1 ? "0" + R : R;
        G = G.length() == 1 ? "0" + G : G;
        B = B.length() == 1 ? "0" + B : B;
 
        sb.append("0x");
        sb.append(R);
        sb.append(G);
        sb.append(B);
 
        return sb.toString();
    }

	
	//动画模拟函数
	public void paintAnimation(){
		timer = new Timer();  
		currentTime = MainFrame.graphNet.beginTime;
		
        timer.scheduleAtFixedRate(new TimerTask() {  
            public void run() {  
            	currentTime=currentTime + speed;
            	if (currentTime > MainFrame.graphNet.endTime){
					if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
					{
						animationButton.setText("Animation");
					}
					else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
					{
						animationButton.setText("播放动画");
				    }
					modelType.setEnabled(true);
					animationSlider.setEnabled(false);
					pathSlider.setEnabled(true);
					activitySlider.setEnabled(true);
					timeFlag = 0;
					paintGraph();
					timer.cancel();
            	}
            	int[] activityEvent = new int[MainFrame.graphNet.activityCount];
            	int[][] activityEventEdge = new int[MainFrame.graphNet.activityCount][MainFrame.graphNet.activityCount];
            	
            	int lastActivityId = -1;
            	String lastCase = "";
            	Date lastDate = new Date();
            	int[] temp = MainFrame.graphNet.activityFre.clone();
    			Arrays.sort(temp);
    			
            	for (int i = 0 ; i < MainFrame.eventCollection.getSize(); i++){
            		
            		Event event = MainFrame.eventCollection
							.getEvent(i);
            		String caseName = event.getCase();
            		String activityName = event.getActivity();
            		int activityId = MainFrame.graphNet
							.getActivityId(activityName);
        			if (MainFrame.graphNet.activityFre[activityId] < temp[activitySlider
        			                      						.getValue()]) {
        				continue;
        			}
            		if (event.getStartDate().getTime()/ (1000*60*60) <= currentTime && 
            		        event.getEndDate().getTime()/ (1000*60*60) >= currentTime){
            			//graph.setCellStyles(mxConstants.STYLE_FILLCOLOR, "yellow", new Object[]{v[activityId]}); 
            			activityEvent[activityId]++;
            		}
            		
            		if (caseName.equals(lastCase)){
            			if (event.getStartDate().getTime()/ (1000*60*60) >= currentTime && 
                				lastDate.getTime()/ (1000*60*60) <= currentTime){
            				activityEventEdge[lastActivityId][activityId]++;
            			}
            		}
            		
            		lastCase = caseName;
            		lastDate = event.getEndDate();
            		lastActivityId = activityId;
            	}
            	
            	for (int i = 2; i< MainFrame.graphNet.activityCount; i++)
            		if (MainFrame.graphNet.activityFre[i] >= temp[activitySlider
            		                      						.getValue()])
            	{
            		graph.cellLabelChanged(v[i], MainFrame.graphNet.activityNames[i] + "\n"+activityEvent[i], false);
            		if (activityEvent[i] > 0){
            			int g = activityEvent[i];
            		//	System.out.println(g);
            			if (g > 100){
            				g = 0; 
            			} else{
            				g = 200-g*2;
            			}
            			graph.setCellStyles(mxConstants.STYLE_FILLCOLOR, toHexEncoding(new Color(255,g,0)), new Object[]{v[i]}); 
            		} else{
            			graph.setCellStyles(mxConstants.STYLE_FILLCOLOR, "#BFEFFF",new Object[]{v[i]}); 
            		}
            	}
	            	
            	for (int i = 0; i< MainFrame.graphNet.activityCount;i++)
            	if (MainFrame.graphNet.activityFre[i] >= temp[activitySlider
     			               								.getValue()]){
            		for (int j = 0; j< MainFrame.graphNet.activityCount; j++){
            			if ( MainFrame.graphNet.activityFre[j] >= temp[activitySlider
            			               								.getValue()]
            			               								&& MainFrame.graphNet.activityQueFre[i][j] >= MainFrame.graphNet.activityQueFreSort.
            			               								get(pathSlider.getValue()) && MainFrame.graphNet.activityQueFre[i][j] > 0){ 
            				for (Object edge : graph.getEdgesBetween(v[i], v[j])){
            					graph.cellLabelChanged(edge, activityEventEdge[i][j], false);
            				}
            				
            				if(   activityEventEdge[i][j] > 0 ){
            					int g = activityEventEdge[i][j];
                    			if (g > 40){
                    				g = 0; 
                    			} else{
                    				g = 200-g*5;
                    			}
            					graph.setCellStyles(mxConstants.STYLE_STROKECOLOR, toHexEncoding(new Color(255,g,0)),graph.getEdgesBetween(v[i], v[j]));
            			
            				} else{
            					graph.setCellStyles(mxConstants.STYLE_STROKECOLOR, "#515151",graph.getEdgesBetween(v[i], v[j]));
            				}
            			}
            		}
            	}
            	

        		graphComponent.refresh();
            }
        }, 0, 500);
	}
	
	
	//画图函数
	public void paintGraph() { 
		graph.getModel().beginUpdate();
		try {
			int[] temp = MainFrame.graphNet.activityFre.clone();
			Arrays.sort(temp);
			
			graph.selectAll();
			graph.removeCells();
			v = new Object[MainFrame.graphNet.activityCount];
			v[0] = graph.insertVertex(parent, null,
					MainFrame.graphNet.activityNames[0], 400, 400, 50, 20,"fillColor=#BFEFFF");
			v[1] = graph.insertVertex(parent, null,
					MainFrame.graphNet.activityNames[1], 400, 400, 50, 20,"fillColor=#BFEFFF");
			for (int i = 2; i < MainFrame.graphNet.activityCount; i++)
				if (MainFrame.graphNet.activityFre[i] >= temp[activitySlider
						.getValue()]) {
					
					String value = MainFrame.graphNet.activityNames[i] + "\n"+
					MainFrame.graphNet.activityFre[i];
					
					switch (modelType.getSelectedIndex()) {
					case 1:
						if (MainFrame.graphNet.activityTime[i] != 0)
						{
							value = MainFrame.graphNet.activityNames[i] + "\n"+
									(MainFrame.graphNet.activityTime[i] / (1000*60*60)) + "hours";
						}else{
							value = MainFrame.graphNet.activityNames[i];
						}
						break;
					case 2:
						if (MainFrame.graphNet.activityTime[i] != 0)
						{
							value = MainFrame.graphNet.activityNames[i] + "\n"+
									(MainFrame.graphNet.activityTime[i] / 
											(1000*60*MainFrame.graphNet.activityFre[i])) + "mins";
						}else{
							value = MainFrame.graphNet.activityNames[i];
						}
						break;
					case 3:
						if (MainFrame.graphNet.maxActivityTime[i] != 0)
						{
							value = MainFrame.graphNet.activityNames[i] + "\n"+
									(MainFrame.graphNet.maxActivityTime[i] / 
											(1000*60*60)) + "hours";
						}else{
							value = MainFrame.graphNet.activityNames[i];
						}
						break;
					case 4:
						if (MainFrame.graphNet.minActivityTime[i] != 0)
						{
							value = MainFrame.graphNet.activityNames[i] + "\n"+
									(MainFrame.graphNet.minActivityTime[i] / 
											(1000*60)) + "mins";
						}else{
							value = MainFrame.graphNet.activityNames[i];
						}
						break;
					case 5:
						value = MainFrame.graphNet.activityNames[i] + "\n" +
								MainFrame.graphNet.activityCaseFre[i];
						break;
					case 6:
						value = MainFrame.graphNet.activityNames[i] + "\n" +
								MainFrame.graphNet.maxActivityRep[i];
						break;	
					default:
						break;
					}
							
					v[i] = graph.insertVertex(parent, null,
							value, 400, 400, 80,
							40, "fillColor=#BFEFFF");
				}

			for (int i = 0; i < MainFrame.graphNet.activityCount; i++)
				if (i < 2
						|| MainFrame.graphNet.activityFre[i] >= temp[activitySlider
								.getValue()])
					for (int j = 0; j < MainFrame.graphNet.activityCount; j++)
						if ((j < 2 || MainFrame.graphNet.activityFre[j] >= temp[activitySlider
								.getValue()])
								&& MainFrame.graphNet.activityQueFre[i][j] >= MainFrame.graphNet.activityQueFreSort.
								get(pathSlider.getValue())&& MainFrame.graphNet.activityQueFre[i][j] > 0 ) {
							
							String value = MainFrame.graphNet.activityQueFre[i][j]+"";
							
							switch (modelType.getSelectedIndex()) {
							case 1:
								if (MainFrame.graphNet.activityQueTime[i][j] != 0)
								{
									value =  (MainFrame.graphNet.activityQueTime[i][j] / (1000*60*60) )+" hours";
								} else
								{
									value = "";
								}
								break;
							case 2:
								if (MainFrame.graphNet.activityQueTime[i][j] != 0)
								{
									value =  (MainFrame.graphNet.activityQueTime[i][j] /
											(1000*60*MainFrame.graphNet.activityQueFre[i][j]) )+" mins";
								} else
								{
									value = "";
								}
								break;
							case 3:
								if (MainFrame.graphNet.maxActivityQueTime[i][j] != 0)
								{
									value =  (MainFrame.graphNet.maxActivityQueTime[i][j] / (1000*60*60) )+" hours";
								} else
								{
									value = "";
								}
								break;
							case 4:
								if (MainFrame.graphNet.minActivityQueTime[i][j] != 0)
								{
									value =  (MainFrame.graphNet.minActivityQueTime[i][j] / (1000*60) )+" mins";
								} else
								{
									value = "";
								}
								break;
							case 5:
								value = MainFrame.graphNet.activityCaseQueFre[i][j]+"";
								break;
							case 6:
								value = "";
							default:
								break;
							}
							
							graph.insertEdge(parent, null,
									value,
									v[i], v[j], "strokeColor=#515151");
						}

			new mxHierarchicalLayout(graph).execute(graph.getDefaultParent());
			new mxParallelEdgeLayout(graph).execute(graph.getDefaultParent());
		} finally {
			graph.getModel().endUpdate();
		}
		ZoomtoFit();
		graphComponent.validateGraph();
	}

	
	//放缩
	public void ZoomtoFit() {
		
		graphComponent.zoomTo(1, true);
		
	    double newScale = 1;
		mxRectangle graphSize = graph.getView().getGraphBounds();
		Dimension viewPortSize = graphComponent.getSize();

		int gw = (int) graphSize.getWidth();
		int gh = (int) graphSize.getHeight();

		boolean xCenter = false;
		boolean yCenter = false;
		
		if (gw > 0 && gh > 0) {
			int w = (int) viewPortSize.getWidth();
			int h = (int) viewPortSize.getHeight();

			newScale = Math.min((double) w * 0.9 / gw, (double) h * 0.9 / gh);
			
			if((double) w * 0.9 / gw > 1)
				xCenter = true;
			
			if((double) h * 0.9 / gh > 1)
				yCenter = true;
		}
		if (newScale > 1) {
			graphComponent.zoomTo(newScale, true);
		}
		else
		{
		    graphComponent.zoomTo(1, true);
		}
		ZoomtoCenter(xCenter, yCenter);
	}

	
	//居中
	public void ZoomtoCenter(boolean xCenter, boolean yCenter) {
		mxRectangle graphSize = graph.getView().getGraphBounds();
		Dimension viewPortSize = graphComponent.getSize();
		double s = graphComponent.getGraph().getView().getScale();
		double x = (viewPortSize.getWidth() / 2 - graphSize.getWidth() / 2) / s;
		double y = (viewPortSize.getHeight() / 2 - graphSize.getHeight() / 2) / s;
		if(!xCenter)
			x = 10;
		if(!yCenter)
			y = 10;
		graphComponent.getGraph().getView().setTranslate(
				new mxPoint(x, y));
	}

	
	@Override
	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	
	@Override
	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub
		ZoomtoFit();
	}

	
	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	
	@Override
	public void componentResized(ComponentEvent e) {
		// TODO Auto-generated method stub
		ZoomtoFit();
	}

	
}
