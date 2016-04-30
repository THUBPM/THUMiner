package wzc.zcminer.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import wzc.zcminer.global.BigCase;
import wzc.zcminer.global.BigEvent;
import wzc.zcminer.global.BigVariant;
import wzc.zcminer.global.Case;
import wzc.zcminer.global.Event;
import wzc.zcminer.global.RowSelectableJTable;
import wzc.zcminer.global.Variant;

import com.mxgraph.layout.mxParallelEdgeLayout;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxGraph;

//cases面板
public class BigCasesPanel extends JPanel implements ComponentListener {

    //选择板面
    static JPanel selectPanel;
    //variant选择版面
    static JPanel variantsPanel;
    static JPanel variantsLabelPanel;
    static JPanel variantsListPanel;
    static JList<String> variantsList;
    static DefaultListModel<String> variantsListModel;
    static JScrollPane variantsListScroller;
    //case选择版面
    static JComboBox<String> caseComboBox;
    static JPanel casesPanel;
    static JPanel casesLabelPanel;
    static JPanel casesListPanel;
    static JList<String> casesList;
    static JScrollPane casesListScroller;
    static DefaultListModel<String> casesListModel;
    //显示版面
    static JPanel showPanel;
    //显示统计图表版面
    static ChartPanel chartPanel;
    static JFreeChart chart;
    static TimeSeriesCollection dataset;
    int max;
    //显示统计数据版面
    static JPanel staticPanel;
    static JPanel labelPanel;
    static JTextPane eventCountLabel;
    static JTextPane startTimeLabel;
    static JTextPane durationLabel;
    static JTextPane activeTimeLabel;
    static JPanel titlePanel;
    static JLabel titleLabel;
    //显示case版面
    static JTabbedPane tabbedPanel;
    //表格显示
    static JComponent tablePanel;
    static RowSelectableJTable table;
    static DefaultTableModel tableModel;
    //图形显示
    mxGraphComponent graphPanel;
    mxGraph graph;
    Object parent;
    //图像节点
    Object[] v;
	//切换页面
	static ButtonPanel buttonPanel;
	static boolean flag;

	public BigCasesPanel() {
		// TODO Auto-generated constructor stub
		flag = false;

		setLayout(new BorderLayout());
		
	    variantsLabelPanel = new JPanel();
	    variantsLabelPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
		    variantsLabelPanel.add(new JLabel("Variants(" + MainFrame.bigVariantCollection.getSize() + ")"));
		}
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
		    variantsLabelPanel.add(new JLabel("实例种类(" + MainFrame.bigVariantCollection.getSize() + ")"));
		}
		
		variantsListModel = new DefaultListModel<String>();
		if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
			variantsListModel.addElement("Complete Log(" + MainFrame.bigCaseCollection.getSize() + " cases)");
		}
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
			variantsListModel.addElement("所有实例(" + MainFrame.bigCaseCollection.getSize() + "实例)");
		}
		for(int i = 0; i < MainFrame.bigVariantCollection.getSize(); i++)
		{
		    BigVariant variant = MainFrame.bigVariantCollection.getVariant(i);
			if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
			{
			    variantsListModel.addElement("Variant " + variant.getVariant() + " (" + variant.getSize() + "实例(" +
			            (new DecimalFormat("#0.00").format(1.0 * variant.getSize() / MainFrame.bigCaseCollection.getSize() * 100)) + "%))");
			}
			else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
			{
			    variantsListModel.addElement("实例种类" + variant.getVariant() + " (" + variant.getSize() + "实例(" +
			            (new DecimalFormat("#0.00").format(1.0 * variant.getSize() / MainFrame.bigCaseCollection.getSize() * 100)) + "%))");
			}
		}
		
		variantsList = new JList<String>(variantsListModel);
		variantsList.setSelectedIndex(0);
		variantsList.addListSelectionListener(new ListSelectionListener(){
		    public void valueChanged(ListSelectionEvent e) {
		        if (e.getValueIsAdjusting() == false) {
		        	flag = true;
		            int selectedValue = variantsList.getSelectedIndex();
    	            if (selectedValue == -1)
    	            {
                        casesListModel = new DefaultListModel<String>();
                        casesList.setModel(casesListModel);
    	            }
    	            else if(selectedValue == 0)
    	            {
    	                casesListModel = new DefaultListModel<String>();
	                	BigVariant variant = MainFrame.bigVariantCollection.getVariant(-1);
	                	caseComboBox.removeAllItems();
    	                for(int i = 0; i <= (variant.getSize() - 1) / 1000; i++){
    	                	caseComboBox.addItem("第" + (i + 1) + "页");
    	                }
    	                int pageSelectedValue = caseComboBox.getSelectedIndex();
    	                for(int i = pageSelectedValue * 1000; i < (pageSelectedValue + 1) * 1000; i++)
    	                {
    	                	if(i >= variant.getSize()) break;
    	                    BigCase mycase = variant.getCase(i);
    	        			if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
    	        			{
        	        			casesListModel.addElement(mycase.getCase() + " (" + mycase.getSize() + " events)");
    	        			}
    	        			else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
    	        			{
        	        			casesListModel.addElement(mycase.getCase() + " (" + mycase.getSize() + "事件)");
        	        		}
    	                }
    	                casesList.setModel(casesListModel);
    	                casesList.setSelectedIndex(0);
    	            }
    	            else
    	            {
    	                casesListModel = new DefaultListModel<String>();
    	                BigVariant variant = MainFrame.bigVariantCollection.getVariant(selectedValue - 1);
    	                caseComboBox.removeAllItems();
    	                for(int i = 0; i <= (variant.getSize() - 1) / 1000; i++){
    	                	caseComboBox.addItem("第" + (i + 1) + "页");
    	                }
    	                int pageSelectedValue = caseComboBox.getSelectedIndex();
    	                for(int i = pageSelectedValue * 1000; i < (pageSelectedValue + 1) * 1000; i++)
    	                {
    	                	if(i >= variant.getSize()) break;
                            BigCase mycase = variant.getCase(i);
    	        			if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
    	        			{
                                casesListModel.addElement(mycase.getCase() + " (" + mycase.getSize() + " events)");
    	        			}
    	        			else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
    	        			{
                                casesListModel.addElement(mycase.getCase() + " (" + mycase.getSize() + "事件)");
        	        		}
                        }
    	                casesList.setModel(casesListModel);
    	                casesList.setSelectedIndex(0);
    	            }
                    refresh();
		        	flag = false;
		        }
		    }
	    });
		
		variantsListScroller = new JScrollPane(variantsList);
		variantsListScroller.setBorder(BorderFactory.createLineBorder(Color.black));
		variantsListScroller.setPreferredSize(new Dimension(150, variantsListScroller.getHeight()));
		
		variantsListPanel = new JPanel();
        variantsListPanel.setLayout(new BorderLayout(5, 5));
        variantsListPanel.add(variantsListScroller);
        
		variantsPanel = new JPanel(new BorderLayout());
		variantsPanel.add(variantsLabelPanel, BorderLayout.PAGE_START);
		variantsPanel.add(variantsListPanel, BorderLayout.CENTER);
		
		casesLabelPanel = new JPanel();
        casesLabelPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
	        casesLabelPanel.add(new JLabel("Cases(" + MainFrame.bigCaseCollection.getSize() + ")"));
		}
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
	        casesLabelPanel.add(new JLabel("实例(" + MainFrame.bigCaseCollection.getSize() + ")"));
		}
        
        casesListModel = new DefaultListModel<String>();
        BigVariant variant = MainFrame.bigVariantCollection.getVariant(-1);
        caseComboBox = new JComboBox<String>();
        for(int i = 0; i <= (variant.getSize() - 1) / 1000; i++){
        	caseComboBox.addItem("第" + (i + 1) + "页");
        }
        int pageSelectedValue = caseComboBox.getSelectedIndex();
        for(int i = pageSelectedValue * 1000; i < (pageSelectedValue + 1) * 1000; i++)
        {
        	if(i >= variant.getSize()) break;
            BigCase mycase = variant.getCase(i);
    		if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
    		{
                casesListModel.addElement(mycase.getCase() + " (" + mycase.getSize() + " events)");
    		}
    		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
    		{
                casesListModel.addElement(mycase.getCase() + " (" + mycase.getSize() + "事件)");
    		}
        }
        
        casesList = new JList<String>(casesListModel);
        casesList.setSelectedIndex(0);
        casesList.addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting() == false) {
                    refresh();
                }
            }
        });

        casesListScroller = new JScrollPane(casesList);
        casesListScroller.setBorder(BorderFactory.createLineBorder(Color.black));
        casesListScroller.setPreferredSize(new Dimension(150, variantsListScroller.getHeight()));
        
        casesListPanel = new JPanel();
        casesListPanel.setLayout(new BorderLayout(5, 5));
        casesListPanel.add(casesListScroller);
        
        caseComboBox.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
                if(e.getStateChange() == ItemEvent.SELECTED)
                {
                	if(flag) return;
		            int selectedValue = variantsList.getSelectedIndex();
    	            if (selectedValue == -1)
    	            {
                        casesListModel = new DefaultListModel<String>();
                        casesList.setModel(casesListModel);
    	            }
    	            else if(selectedValue == 0)
    	            {
    	                casesListModel = new DefaultListModel<String>();
	                	BigVariant variant = MainFrame.bigVariantCollection.getVariant(-1);
    	                int pageSelectedValue = caseComboBox.getSelectedIndex();
    	                for(int i = pageSelectedValue * 1000; i < (pageSelectedValue + 1) * 1000; i++)
    	                {
    	                	if(i >= variant.getSize()) break;
    	                    BigCase mycase = variant.getCase(i);
    	        			if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
    	        			{
        	        			casesListModel.addElement(mycase.getCase() + " (" + mycase.getSize() + " events)");
    	        			}
    	        			else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
    	        			{
        	        			casesListModel.addElement(mycase.getCase() + " (" + mycase.getSize() + "事件)");
        	        		}
    	                }
    	                casesList.setModel(casesListModel);
    	                casesList.setSelectedIndex(0);
    	            }
    	            else
    	            {
    	                casesListModel = new DefaultListModel<String>();
    	                BigVariant variant = MainFrame.bigVariantCollection.getVariant(selectedValue - 1);
    	                int pageSelectedValue = caseComboBox.getSelectedIndex();
    	                for(int i = pageSelectedValue * 1000; i < (pageSelectedValue + 1) * 1000; i++)
    	                {
    	                	if(i >= variant.getSize()) break;
                            BigCase mycase = variant.getCase(i);
    	        			if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
    	        			{
                                casesListModel.addElement(mycase.getCase() + " (" + mycase.getSize() + " events)");
    	        			}
    	        			else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
    	        			{
                                casesListModel.addElement(mycase.getCase() + " (" + mycase.getSize() + "事件)");
        	        		}
                        }
    	                casesList.setModel(casesListModel);
    	                casesList.setSelectedIndex(0);
    	            }
                    refresh();
                }

			}
        });
        
        casesPanel = new JPanel(new BorderLayout());
        casesPanel.add(casesLabelPanel, BorderLayout.PAGE_START);
        casesPanel.add(caseComboBox, BorderLayout.SOUTH);
        casesPanel.add(casesListPanel, BorderLayout.CENTER);
		
        selectPanel = new JPanel(new BorderLayout());
        selectPanel.add(variantsPanel, BorderLayout.WEST);
        selectPanel.add(casesPanel, BorderLayout.EAST);
        
        table = new RowSelectableJTable();
        
        tablePanel = new JScrollPane(table);
        
        graph = new mxGraph();
        parent = graph.getDefaultParent();
        graphPanel = new mxGraphComponent(graph);
        
        tabbedPanel = new JTabbedPane();
		if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
	        tabbedPanel.addTab("Table", null, tablePanel, "Show case in table");
	        tabbedPanel.addTab("Graph", null, graphPanel, "Show case in graph");
		}
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
	        tabbedPanel.addTab("表格", null, tablePanel, "以表格显示实例");
	        tabbedPanel.addTab("示意图", null, graphPanel, "以示意图显示实例");
		}
        tabbedPanel.addChangeListener(new ChangeListener(){
            public void stateChanged(ChangeEvent e) {
                int selectedIndex = tabbedPanel.getSelectedIndex();
                if(selectedIndex == 0)
                {
                    refresh();
                }
                else if(selectedIndex == 1)
                {
                    refresh();
                }
            }
        });
      
        titleLabel = new JLabel();
        titlePanel = new JPanel();
        titlePanel.add(titleLabel);
        
        eventCountLabel = new JTextPane();
        eventCountLabel.setEditable(false);
        eventCountLabel.setBackground(null);
        startTimeLabel = new JTextPane();
        startTimeLabel.setEditable(false);
        startTimeLabel.setBackground(null);
        durationLabel = new JTextPane();
        durationLabel.setEditable(false);
        durationLabel.setBackground(null);
        activeTimeLabel = new JTextPane();
        activeTimeLabel.setEditable(false);
        activeTimeLabel.setBackground(null);
        labelPanel = new JPanel(new GridLayout(0, 1));
        labelPanel.add(eventCountLabel);
        labelPanel.add(startTimeLabel);
        labelPanel.add(durationLabel);
        labelPanel.add(activeTimeLabel);
        
        chartPanel = new ChartPanel(null);
        chartPanel.setPreferredSize(new Dimension(chartPanel.getWidth(), 200));
        
        staticPanel = new JPanel(new BorderLayout());
        staticPanel.add(labelPanel, BorderLayout.EAST);
        staticPanel.add(chartPanel, BorderLayout.CENTER);
        staticPanel.add(titlePanel, BorderLayout.NORTH);
        
        showPanel = new JPanel(new BorderLayout());
        showPanel.add(tabbedPanel, BorderLayout.CENTER);
        showPanel.add(staticPanel, BorderLayout.NORTH);
        
		buttonPanel = new ButtonPanel();
		buttonPanel.setCases();
        
        add(selectPanel, BorderLayout.WEST);
        add(showPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.NORTH);

        creatChart(MainFrame.bigCaseCollection.getFirstID());
        refresh();
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
    
    
    public void refresh()
    {
        int caseSelectedValue = casesList.getSelectedIndex();
        int variantSelectedValue = variantsList.getSelectedIndex();
        int pageSelectedValue = caseComboBox.getSelectedIndex();
        if (caseSelectedValue == -1 || variantSelectedValue == -1){}
        else if(variantSelectedValue == 0)
        {
        	BigVariant variant = MainFrame.bigVariantCollection.getVariant(-1);
            BigCase mycase = variant.getCase(pageSelectedValue * 1000 + caseSelectedValue);
            updateTable(mycase);
            updateGraph(mycase);
            updateLabel(mycase);
            updateChart(mycase);
        }
        else
        {
            BigVariant variant = MainFrame.bigVariantCollection.getVariant(variantSelectedValue - 1);
            BigCase mycase = variant.getCase(pageSelectedValue * 1000 + caseSelectedValue);
            updateTable(mycase);
            updateGraph(mycase);
            updateLabel(mycase);
            updateChart(mycase);
        }
    }
    
    
    public void creatChart(BigCase mycase)
    {
        TimeSeries casesSeries = new TimeSeries("");
        int count = 0;
        max = 0;
        for(int i = 0; i < MainFrame.activeCasesOverTimeChart.getSize(); i++)
        {
            Date time = MainFrame.activeCasesOverTimeChart.getDate(i);
            int minute = time.getMinutes();
            int hour = time.getHours();
            int day = time.getDate();
            int month = time.getMonth() + 1;
            int year = time.getYear() + 1900;
            Minute timeChart = new Minute(minute, hour, day, month, year);
            count += MainFrame.activeCasesOverTimeChart.getCaseCount(i);
            casesSeries.addOrUpdate(timeChart, count);
            if(count > max)
                max = count;
        }
        
        TimeSeries caseSeries = new TimeSeries("");
        
        Date startTime = mycase.getStart();
        int startMinute = startTime.getMinutes();
        int startHour = startTime.getHours();
        int startDay = startTime.getDate();
        int startMonth = startTime.getMonth() + 1;
        int startYear = startTime.getYear() + 1900;
        Minute startTimeChart = new Minute(startMinute, startHour, startDay, startMonth, startYear);
        
        Date endTime = mycase.getEnd();
        int endMinute = endTime.getMinutes();
        int endHour = endTime.getHours();
        int endDay = endTime.getDate();
        int endMonth = endTime.getMonth() + 1;
        int endYear = endTime.getYear() + 1900;
        Minute endTimeChart = new Minute(endMinute, endHour, endDay, endMonth, endYear);
        
        caseSeries.add(startTimeChart, max);
        caseSeries.addOrUpdate(endTimeChart, max);
        
        dataset = new TimeSeriesCollection();
        dataset.addSeries(caseSeries);
        dataset.addSeries(casesSeries);
        
        chart = ChartFactory.createXYAreaChart(
                null,                     // chart title
                null,                     // domain axis label
                null,                     // range axis label
                dataset,                  // data
                PlotOrientation.VERTICAL, // orientation
                false,                    // include legend
                false,                    // tooltips
                false                     // urls
            );
        
        chart.setBackgroundPaint(null);
        XYPlot plot = chart.getXYPlot();
        DateAxis domain = new DateAxis();
        domain.setTickLabelsVisible(false);
        domain.setAxisLineVisible(false);
        domain.setTickMarksVisible(false);
        plot.setDomainAxis(domain);
        NumberAxis range = new NumberAxis();
        range.setTickLabelsVisible(false);
        range.setAxisLineVisible(false);
        range.setTickMarksVisible(false);
        plot.setRangeAxis(range);
        
        chartPanel.setChart(chart);
    }
    
    
    public void updateChart(BigCase mycase)
    {
        TimeSeries caseSeries = dataset.getSeries(0);
        
        Date startTime = mycase.getStart();
        int startMinute = startTime.getMinutes();
        int startHour = startTime.getHours();
        int startDay = startTime.getDate();
        int startMonth = startTime.getMonth() + 1;
        int startYear = startTime.getYear() + 1900;
        Minute startTimeChart = new Minute(startMinute, startHour, startDay, startMonth, startYear);
        
        Date endTime = mycase.getEnd();
        int endMinute = endTime.getMinutes();
        int endHour = endTime.getHours();
        int endDay = endTime.getDate();
        int endMonth = endTime.getMonth() + 1;
        int endYear = endTime.getYear() + 1900;
        Minute endTimeChart = new Minute(endMinute, endHour, endDay, endMonth, endYear);
        
        caseSeries.clear();
        caseSeries.add(startTimeChart, max);
        caseSeries.addOrUpdate(endTimeChart, max);
    }
    
    
    public void updateLabel(BigCase mycase)
    {
		if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
	        titleLabel.setText(mycase.getCase() + " with " + mycase.getSize() + " events");
	        eventCountLabel.setText("Events : " + mycase.getSize());
	        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        startTimeLabel.setText("Start : " + formatter.format(mycase.getStart()));
	        long duration = mycase.getDuration();
	        long durationMinutes = duration / (60 * 1000) % 60;
	        long durationHours = duration / (60 * 60 * 1000) % 24;
	        long durationDays = duration / (24 * 60 * 60 * 1000);
	        durationLabel.setText("Duration : " + durationDays + " days " + durationHours + " hours " + durationMinutes + " mins");
	        activeTimeLabel.setText("Active time : " + (new DecimalFormat("#0.00").format(mycase.getActive(false) * 100)) + "%");
		}
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
	        titleLabel.setText(mycase.getCase() + "含" + mycase.getSize() + "事件");
	        eventCountLabel.setText("事件数：" + mycase.getSize());
	        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        startTimeLabel.setText("开始时间：" + formatter.format(mycase.getStart()));
	        long duration = mycase.getDuration();
	        long durationMinutes = duration / (60 * 1000) % 60;
	        long durationHours = duration / (60 * 60 * 1000) % 24;
	        long durationDays = duration / (24 * 60 * 60 * 1000);
	        durationLabel.setText("持续时间：" + durationDays + "天" + durationHours + "小时" + durationMinutes + "分");
	        activeTimeLabel.setText("活动时间百分比：" + (new DecimalFormat("#0.00").format(mycase.getActive(false) * 100)) + "%");
		}
    }
    
    
    public void updateTable(BigCase mycase)
    {
        String[][] tableData = new String[mycase.getSize()][5];
        for(int i = 0; i < mycase.getSize(); i++)
        {
            BigEvent event = mycase.getEvent(i);
            tableData[i][0] = event.getActivity();
            tableData[i][1] = event.getResource();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            tableData[i][2] = formatter.format(event.getStartDate());
            tableData[i][3] = formatter.format(event.getEndDate());
            long diff = event.getTime();
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);
    		if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
    		{
                tableData[i][4] = diffDays + " days " + diffHours + " hours " + diffMinutes + " mins";
            }
    		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
    		{
                tableData[i][4] = diffDays + "天" + diffHours + "小时" + diffMinutes + "分";
    		}
        }
		if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
	        String[] headlines = {"Activity","Resource","Start","End","Duration"};
	        tableModel = new DefaultTableModel(tableData, headlines);
        }
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
	        String[] headlines = {"活动","资源","开始时间","结束时间","持续时间"};
	        tableModel = new DefaultTableModel(tableData, headlines);
		}
        table.setModel(tableModel);
    }
    
    
    public void updateGraph(BigCase mycase)
    {
        graph.getModel().beginUpdate();
        
        graph.selectAll();
        graph.removeCells();
        v = new Object[mycase.getSize()];
        
        for(int i = 0; i < mycase.getSize(); i++)
        {
            BigEvent event = mycase.getEvent(i);
            long diff = event.getTime();
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);
            String value;
    		if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
    		{
    			value = event.getActivity() + "\nstarted at " + event.getStartDate().toString()
                        + " by " + event.getResource() + "\ncompleted after "
                        + diffDays + " days " + diffHours + " hours " + diffMinutes + " mins";
            }
    		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
    		{
    			value = event.getActivity() + "\n" + "由" + event.getResource()
    					+ "始于" + event.getStartDate().toString() + "\n在"
                        + diffDays + "天" + diffHours + "小时" + diffMinutes + "分后结束";
    		}
    		else
    		{
    			value = "";
    		}
            v[i] = graph.insertVertex(parent, null,
                    value, 400, 400, 300, 50,"fillColor=#BFEFFF");
        }
        
        for(int i = 1; i < mycase.getSize(); i++)
        {
            long diff = mycase.getEvent(i).getStartDate().getTime() - mycase.getEvent(i - 1).getEndDate().getTime();
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);
            String value;
    		if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
    		{
                value = diffDays + " days " + diffHours + " hours " + diffMinutes + " mins";
            }
    		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
    		{
                value = diffDays + "天" + diffHours + "小时" + diffMinutes + "分";
    		}
    		else
    		{
    			value = "";
    		}
            graph.insertEdge(parent, null, value, v[i - 1], v[i], "strokeColor=#515151");
        }
        
        new mxHierarchicalLayout(graph).execute(graph.getDefaultParent());
        new mxParallelEdgeLayout(graph).execute(graph.getDefaultParent());
        
        graph.getModel().endUpdate();
        ZoomtoFit();
        ZoomtoCenter();
        graphPanel.validateGraph();
    }
    
    
    //放缩
    public void ZoomtoFit() {

        double newScale = 1;
        mxRectangle graphSize = graph.getView().getGraphBounds();
        Dimension viewPortSize = graphPanel.getSize();

        int gw = (int) graphSize.getWidth();

        if (gw > 0) {
            int w = (int) viewPortSize.getWidth();

            newScale = (double) w * 0.9 / gw;
        }
        if (newScale != 0 && newScale > 0.2) {
            graphPanel.zoom(newScale);
        }
    }
    
    
    //居中
    public void ZoomtoCenter() {
        mxRectangle graphSize = graph.getView().getGraphBounds();
        Dimension viewPortSize = graphPanel.getSize();
        double s = graphPanel.getGraph().getView().getScale();
        double x = (viewPortSize.getWidth() / 2 - graphSize.getWidth() / 2) / s;
        graphPanel.getGraph().getView().setTranslate(
                new mxPoint(x, 5));
        graphPanel.getVerticalScrollBar().setValue(0);
    }
    
    
    @Override
    public void componentMoved(ComponentEvent e) {
        // TODO Auto-generated method stub

    }

    
    @Override
    public void componentShown(ComponentEvent e) {
        // TODO Auto-generated method stub
        ZoomtoFit();
        ZoomtoCenter();
    }

    
    @Override
    public void componentHidden(ComponentEvent e) {
        // TODO Auto-generated method stub

    }

    
    @Override
    public void componentResized(ComponentEvent e) {
        // TODO Auto-generated method stub
        ZoomtoFit();
        ZoomtoCenter();
    }

    
}
