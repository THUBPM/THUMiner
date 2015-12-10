package wzc.zcminer.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.CategoryToolTipGenerator;
import org.jfree.chart.labels.CustomXYToolTipGenerator;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYAreaRenderer;

import wzc.zcminer.global.Activity;
import wzc.zcminer.global.Case;
import wzc.zcminer.global.Resource;
import wzc.zcminer.global.RowSelectableJTable;
import wzc.zcminer.global.Variant;

//statics面板
public class StaticsPanel extends JPanel implements ComponentListener {
    
    private class ComparatorRow implements Comparator<String>{
        public int compare(String s1, String s2) {
            int i1 = 0;
            int i2 = 0;
            while(i1 < s1.length() && i2 < s2.length())
            {
                if(s1.charAt(i1) >= '0' && s1.charAt(i1) <= '9' && s2.charAt(i2) >= '0' && s2.charAt(i2) <= '9')
                {
                    int j1 = i1;
                    int j2 = i2;
                    while(s1.charAt(j1) >= '0' && s1.charAt(j1) <= '9')
                    {
                        j1++;
                        if(j1 == s1.length())
                            break;
                    }
                    while(s2.charAt(j2) >= '0' && s2.charAt(j2) <= '9')
                    {
                        j2++;
                        if(j2 == s2.length())
                            break;
                    }
                    double n1 = Double.parseDouble(s1.substring(i1, j1));
                    double n2 = Double.parseDouble(s2.substring(i2, j2));
                    if(n1 > n2)
                        return 1;
                    else if(n1 < n2)
                        return -1;
                    else
                    {
                        i1 = j1; i2 = j2;
                    }
                }
                else
                {
                    if(s1.charAt(i1) > s2.charAt(i2))
                        return 1;
                    else if(s1.charAt(i1) < s2.charAt(i2))
                        return -1;
                    else
                    {
                        i1++; i2++;
                    }
                }
            }
            if(i1 == s1.length() && i2 == s2.length())
                return 0;
            else if(i1 == s1.length())
                return -1;
            else
                return 1;
        }
    }
  
    
    static class CustomToolTipGenerator implements CategoryToolTipGenerator  {
        public String generateToolTip(CategoryDataset dataset, int row, int column)   {
            long duration = dataset.getValue(row, column).longValue();
            long durationMinutes = duration / (60 * 1000) % 60;
            long durationHours = duration / (60 * 60 * 1000) % 24;
            long durationDays = duration / (24 * 60 * 60 * 1000);
    		if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
    		{
                return dataset.getColumnKey(column) + ", " + durationDays + " days " + durationHours + " hours " + durationMinutes + " mins";
    		}
    		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
    		{
                return dataset.getColumnKey(column) + "，" + durationDays + "天" + durationHours + "小时" + durationMinutes + "分";
    		}
    		return "";
        }
    }
    
    
    //显示主版面
    static JTabbedPane tabbedPanel;
    
    //overview
    static JPanel overviewPanel;
    //overview统计版面
    static JPanel overviewStaticPanel;
    //overview图表版面
    static JTabbedPane overviewChartPanel;
    //overview-events over time图表版面
    static ChartPanel overviewEventsOverTimeChartPanel;
    static JFreeChart overviewEventsOverTimeChart;
    static TimeSeriesCollection overviewEventsOverTimeDataset;
    //overview-active cases over time图表版面
    static ChartPanel overviewActiveCasesOverTimeChartPanel;
    static JFreeChart overviewActiveCasesOverTimeChart;
    static TimeSeriesCollection overviewActiveCasesOverTimeDataset;
    //overview-case variants图表版面
    static ChartPanel overviewCaseVariantsChartPanel;
    static JFreeChart overviewCaseVariantsChart;
    static XYSeriesCollection overviewCaseVariantsDataset;
    //overview-events per case图表版面
    static ChartPanel overviewEventsPerCaseChartPanel;
    static JFreeChart overviewEventsPerCaseChart;
    static XYSeriesCollection overviewEventsPerCaseDataset;
    //overview-case duration图表版面
    static ChartPanel overviewCaseDurationChartPanel;
    static JFreeChart overviewCaseDurationChart;
    static XYSeriesCollection overviewCaseDurationDataset;
    //overview-case utilization图表版面
    static ChartPanel overviewCaseUtilizationChartPanel;
    static JFreeChart overviewCaseUtilizationChart;
    static XYSeriesCollection overviewCaseUtilizationDataset;
    //overview-mean activity duration图表版面
    static ChartPanel overviewMeanActivityDurationChartPanel;
    static JFreeChart overviewMeanActivityDurationChart;
    static XYSeriesCollection overviewMeanActivityDurationDataset;
    //overview-mean waiting time图表版面
    static ChartPanel overviewMeanWaitingTimeChartPanel;
    static JFreeChart overviewMeanWaitingTimeChart;
    static XYSeriesCollection overviewMeanWaitingTimeDataset;
    //overview统计数据版面
    static JPanel overviewLabelPanel;
    static JTextPane overviewEventsLabel;
    static JTextPane overviewCasesLabel;
    static JTextPane overviewActivitiesLabel;
    static JTextPane overviewMedianCaseDurationLabel;
    static JTextPane overviewMeanCaseDurationLabel;
    static JTextPane overviewStartLabel;
    static JTextPane overviewEndLabel;
    //overview表格版面
    static JTabbedPane overviewTablePanel;
    //overview-case表格显示
    static JComponent overviewCaseTablePanel;
    static RowSelectableJTable overviewCaseTable;
    static DefaultTableModel overviewCaseTableModel;
    //overview-variant表格显示
    static JComponent overviewVariantTablePanel;
    static RowSelectableJTable overviewVariantTable;
    static DefaultTableModel overviewVariantTableModel;
    
    //activity
    static JPanel activityPanel;
    //activity表格版面
    static JTabbedPane activityTablePanel;
    //activity-all activities表格显示
    static JComponent activityAllActivitiesTablePanel;
    static RowSelectableJTable activityAllActivitiesTable;
    static DefaultTableModel activityAllActivitiesTableModel;
    //activity-first in case表格显示
    static JComponent activityFirstInCaseTablePanel;
    static RowSelectableJTable activityFirstInCaseTable;
    static DefaultTableModel activityFirstInCaseTableModel;
    //activity-last in case表格显示
    static JComponent activityLastInCaseTablePanel;
    static RowSelectableJTable activityLastInCaseTable;
    static DefaultTableModel activityLastInCaseTableModel;
    //activity统计版面
    static JPanel activityStaticPanel;
    //activity图表版面
    static JTabbedPane activityChartPanel;
    //activity-frequency图表版面
    static ChartPanel activityFrequencyChartPanel;
    static JFreeChart activityFrequencyChart;
    static DefaultCategoryDataset activityFrequencyDataset;
    //activity-median duration图表版面
    static ChartPanel activityMedianDurationChartPanel;
    static JFreeChart activityMedianDurationChart;
    static DefaultCategoryDataset activityMedianDurationDataset;
    //activity-mean duration图表版面
    static ChartPanel activityMeanDurationChartPanel;
    static JFreeChart activityMeanDurationChart;
    static DefaultCategoryDataset activityMeanDurationDataset;
    //activity-duration range图表版面
    static ChartPanel activityDurationRangeChartPanel;
    static JFreeChart activityDurationRangeChart;
    static DefaultCategoryDataset activityDurationRangeDataset;
    //activity-aggregate duration图表版面
    static ChartPanel activityAggregateDurationChartPanel;
    static JFreeChart activityAggregateDurationChart;
    static DefaultCategoryDataset activityAggregateDurationDataset;
    //activity统计数据版面
    static JPanel activityLabelPanel;
    static JTextPane activityActivitiesLabel;
    static JTextPane activityMinimalFrequencyLabel;
    static JTextPane activityMedianFrequencyLabel;
    static JTextPane activityMeanFrequencyLabel;
    static JTextPane activityMaximalFrequencyLabel;
    static JTextPane activityFrequencyStdDeviationLabel;
    
    //resource
    static JPanel resourcePanel;
    //resource表格版面
    static JTabbedPane resourceTablePanel;
    //resource-all resources表格显示
    static JComponent resourceAllResourcesTablePanel;
    static RowSelectableJTable resourceAllResourcesTable;
    static DefaultTableModel resourceAllResourcesTableModel;
    //resource-first in case表格显示
    static JComponent resourceFirstInCaseTablePanel;
    static RowSelectableJTable resourceFirstInCaseTable;
    static DefaultTableModel resourceFirstInCaseTableModel;
    //resource-last in case表格显示
    static JComponent resourceLastInCaseTablePanel;
    static RowSelectableJTable resourceLastInCaseTable;
    static DefaultTableModel resourceLastInCaseTableModel;
    //resource统计版面
    static JPanel resourceStaticPanel;
    //resource图表版面
    static JTabbedPane resourceChartPanel;
    //resource-frequency图表版面
    static ChartPanel resourceFrequencyChartPanel;
    static JFreeChart resourceFrequencyChart;
    static DefaultCategoryDataset resourceFrequencyDataset;
    //resource-median duration图表版面
    static ChartPanel resourceMedianDurationChartPanel;
    static JFreeChart resourceMedianDurationChart;
    static DefaultCategoryDataset resourceMedianDurationDataset;
    //resource-mean duration图表版面
    static ChartPanel resourceMeanDurationChartPanel;
    static JFreeChart resourceMeanDurationChart;
    static DefaultCategoryDataset resourceMeanDurationDataset;
    //resource-duration range图表版面
    static ChartPanel resourceDurationRangeChartPanel;
    static JFreeChart resourceDurationRangeChart;
    static DefaultCategoryDataset resourceDurationRangeDataset;
    //resource-aggregate duration图表版面
    static ChartPanel resourceAggregateDurationChartPanel;
    static JFreeChart resourceAggregateDurationChart;
    static DefaultCategoryDataset resourceAggregateDurationDataset;
    //resource统计数据版面
    static JPanel resourceLabelPanel;
    static JTextPane resourceResourcesLabel;
    static JTextPane resourceMinimalFrequencyLabel;
    static JTextPane resourceMedianFrequencyLabel;
    static JTextPane resourceMeanFrequencyLabel;
    static JTextPane resourceMaximalFrequencyLabel;
    static JTextPane resourceFrequencyStdDeviationLabel;
    
	//切换页面
	static ButtonPanel buttonPanel;

	public StaticsPanel() {
		// TODO Auto-generated constructor stub

		setLayout(new BorderLayout());
        
		overviewEventsOverTimeChartPanel = new ChartPanel(null);
		overviewEventsOverTimeChartPanel.setPreferredSize(new Dimension(overviewEventsOverTimeChartPanel.getWidth(), 200));
		overviewEventsOverTimeChartPanel.setDismissDelay(36000000);
		overviewEventsOverTimeChartPanel.setInitialDelay(0);
		overviewEventsOverTimeChartPanel.setReshowDelay(0);
		overviewActiveCasesOverTimeChartPanel = new ChartPanel(null);
		overviewActiveCasesOverTimeChartPanel.setPreferredSize(new Dimension(overviewActiveCasesOverTimeChartPanel.getWidth(), 200));
		overviewActiveCasesOverTimeChartPanel.setDismissDelay(36000000);
		overviewActiveCasesOverTimeChartPanel.setInitialDelay(0);
		overviewActiveCasesOverTimeChartPanel.setReshowDelay(0);
		overviewCaseVariantsChartPanel = new ChartPanel(null);
		overviewCaseVariantsChartPanel.setPreferredSize(new Dimension(overviewCaseVariantsChartPanel.getWidth(), 200));
		overviewCaseVariantsChartPanel.setDismissDelay(36000000);
		overviewCaseVariantsChartPanel.setInitialDelay(0);
		overviewCaseVariantsChartPanel.setReshowDelay(0);
		overviewEventsPerCaseChartPanel = new ChartPanel(null);
		overviewEventsPerCaseChartPanel.setPreferredSize(new Dimension(overviewEventsPerCaseChartPanel.getWidth(), 200));
		overviewEventsPerCaseChartPanel.setDismissDelay(36000000);
		overviewEventsPerCaseChartPanel.setInitialDelay(0);
		overviewEventsPerCaseChartPanel.setReshowDelay(0);
		overviewCaseDurationChartPanel = new ChartPanel(null);
		overviewCaseDurationChartPanel.setPreferredSize(new Dimension(overviewCaseDurationChartPanel.getWidth(), 200));
		overviewCaseDurationChartPanel.setDismissDelay(36000000);
		overviewCaseDurationChartPanel.setInitialDelay(0);
		overviewCaseDurationChartPanel.setReshowDelay(0);
		overviewCaseUtilizationChartPanel = new ChartPanel(null);
		overviewCaseUtilizationChartPanel.setPreferredSize(new Dimension(overviewCaseUtilizationChartPanel.getWidth(), 200));
		overviewCaseUtilizationChartPanel.setDismissDelay(36000000);
		overviewCaseUtilizationChartPanel.setInitialDelay(0);
		overviewCaseUtilizationChartPanel.setReshowDelay(0);
		overviewMeanActivityDurationChartPanel = new ChartPanel(null);
		overviewMeanActivityDurationChartPanel.setPreferredSize(new Dimension(overviewMeanActivityDurationChartPanel.getWidth(), 200));
		overviewMeanActivityDurationChartPanel.setDismissDelay(36000000);
		overviewMeanActivityDurationChartPanel.setInitialDelay(0);
		overviewMeanActivityDurationChartPanel.setReshowDelay(0);
		overviewMeanWaitingTimeChartPanel = new ChartPanel(null);
		overviewMeanWaitingTimeChartPanel.setPreferredSize(new Dimension(overviewMeanWaitingTimeChartPanel.getWidth(), 200));
		overviewMeanWaitingTimeChartPanel.setDismissDelay(36000000);
		overviewMeanWaitingTimeChartPanel.setInitialDelay(0);
        overviewMeanWaitingTimeChartPanel.setReshowDelay(0);
		
        overviewChartPanel = new JTabbedPane();
		if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
	        overviewChartPanel.addTab("Events over time", null, overviewEventsOverTimeChartPanel, "Events over time");
	        overviewChartPanel.addTab("Active cases over time", null, overviewActiveCasesOverTimeChartPanel, "Active cases over time");
	        overviewChartPanel.addTab("Case variants", null, overviewCaseVariantsChartPanel, "Case variants");
	        overviewChartPanel.addTab("Events per case", null, overviewEventsPerCaseChartPanel, "Events per case");
	        overviewChartPanel.addTab("Case duration", null, overviewCaseDurationChartPanel, "Case duration");
	        overviewChartPanel.addTab("Case utilization", null, overviewCaseUtilizationChartPanel, "Case utilization");
	        overviewChartPanel.addTab("Mean activity duration", null, overviewMeanActivityDurationChartPanel, "Mean activity duration");
	        overviewChartPanel.addTab("Mean waiting time", null, overviewMeanWaitingTimeChartPanel, "Mean waiting time");
		}
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
	        overviewChartPanel.addTab("事件分布图", null, overviewEventsOverTimeChartPanel, "事件分布图");
	        overviewChartPanel.addTab("实例分布图", null, overviewActiveCasesOverTimeChartPanel, "实例分布图");
	        overviewChartPanel.addTab("实例种类统计图", null, overviewCaseVariantsChartPanel, "实例种类统计图");
	        overviewChartPanel.addTab("事件数统计图", null, overviewEventsPerCaseChartPanel, "事件数统计图");
	        overviewChartPanel.addTab("总时间图", null, overviewCaseDurationChartPanel, "总时间图");
	        overviewChartPanel.addTab("时间利用率图", null, overviewCaseUtilizationChartPanel, "时间利用率图");
	        overviewChartPanel.addTab("平均活动时间图", null, overviewMeanActivityDurationChartPanel, "平均活动时间图");
	        overviewChartPanel.addTab("平均等待时间图", null, overviewMeanWaitingTimeChartPanel, "平均等待时间图");
		}
        
		overviewEventsLabel = new JTextPane();
		overviewEventsLabel.setEditable(false);
		overviewEventsLabel.setBackground(null);
		overviewCasesLabel = new JTextPane();
		overviewCasesLabel.setEditable(false);
		overviewCasesLabel.setBackground(null);
		overviewActivitiesLabel = new JTextPane();
		overviewActivitiesLabel.setEditable(false);
		overviewActivitiesLabel.setBackground(null);
		overviewMedianCaseDurationLabel = new JTextPane();
		overviewMedianCaseDurationLabel.setEditable(false);
		overviewMedianCaseDurationLabel.setBackground(null);
		overviewMeanCaseDurationLabel = new JTextPane();
		overviewMeanCaseDurationLabel.setEditable(false);
		overviewMeanCaseDurationLabel.setBackground(null);
		overviewStartLabel = new JTextPane();
		overviewStartLabel.setEditable(false);
		overviewStartLabel.setBackground(null);
		overviewEndLabel = new JTextPane();
		overviewEndLabel.setEditable(false);
		overviewEndLabel.setBackground(null);
        overviewLabelPanel = new JPanel(new GridLayout(0, 1));
        overviewLabelPanel.add(overviewEventsLabel);
        overviewLabelPanel.add(overviewCasesLabel);
        overviewLabelPanel.add(overviewActivitiesLabel);
        overviewLabelPanel.add(overviewMedianCaseDurationLabel);
        overviewLabelPanel.add(overviewMeanCaseDurationLabel);
        overviewLabelPanel.add(overviewStartLabel);
        overviewLabelPanel.add(overviewEndLabel);
		
		overviewStaticPanel = new JPanel(new BorderLayout());
		overviewStaticPanel.add(overviewLabelPanel, BorderLayout.EAST);
		overviewStaticPanel.add(overviewChartPanel, BorderLayout.CENTER);
		
		overviewCaseTable = new RowSelectableJTable();
		overviewCaseTablePanel = new JScrollPane(overviewCaseTable);
        overviewVariantTable = new RowSelectableJTable();
		overviewVariantTablePanel = new JScrollPane(overviewVariantTable);
        overviewTablePanel = new JTabbedPane();
		if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
			overviewTablePanel.addTab("Cases (" + MainFrame.caseCollection.getSize() + ")", null, overviewCaseTablePanel, "Cases");
			overviewTablePanel.addTab("Variants (" + MainFrame.variantCollection.getSize() + ")", null, overviewVariantTablePanel, "Variants");
		}
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
			overviewTablePanel.addTab("实例(" + MainFrame.caseCollection.getSize() + ")", null, overviewCaseTablePanel, "实例");
			overviewTablePanel.addTab("实例种类(" + MainFrame.variantCollection.getSize() + ")", null, overviewVariantTablePanel, "实例种类");
		}
		
		overviewPanel = new JPanel(new BorderLayout());
		overviewPanel.add(overviewTablePanel, BorderLayout.CENTER);
		overviewPanel.add(overviewStaticPanel, BorderLayout.NORTH);
		
		activityFrequencyChartPanel = new ChartPanel(null);
		activityFrequencyChartPanel.setPreferredSize(new Dimension(activityFrequencyChartPanel.getWidth(), 200));
		activityFrequencyChartPanel.setDismissDelay(36000000);
		activityFrequencyChartPanel.setInitialDelay(0);
		activityFrequencyChartPanel.setReshowDelay(0);
		activityMedianDurationChartPanel = new ChartPanel(null);
		activityMedianDurationChartPanel.setPreferredSize(new Dimension(activityMedianDurationChartPanel.getWidth(), 200));
		activityMedianDurationChartPanel.setDismissDelay(36000000);
		activityMedianDurationChartPanel.setInitialDelay(0);
		activityMedianDurationChartPanel.setReshowDelay(0);
		activityMeanDurationChartPanel = new ChartPanel(null);
		activityMeanDurationChartPanel.setPreferredSize(new Dimension(activityMeanDurationChartPanel.getWidth(), 200));
		activityMeanDurationChartPanel.setDismissDelay(36000000);
		activityMeanDurationChartPanel.setInitialDelay(0);
		activityMeanDurationChartPanel.setReshowDelay(0);
		activityDurationRangeChartPanel = new ChartPanel(null);
		activityDurationRangeChartPanel.setPreferredSize(new Dimension(activityDurationRangeChartPanel.getWidth(), 200));
		activityDurationRangeChartPanel.setDismissDelay(36000000);
		activityDurationRangeChartPanel.setInitialDelay(0);
		activityDurationRangeChartPanel.setReshowDelay(0);
		activityAggregateDurationChartPanel = new ChartPanel(null);
		activityAggregateDurationChartPanel.setPreferredSize(new Dimension(activityAggregateDurationChartPanel.getWidth(), 200));
		activityAggregateDurationChartPanel.setDismissDelay(36000000);
		activityAggregateDurationChartPanel.setInitialDelay(0);
		activityAggregateDurationChartPanel.setReshowDelay(0);
		
        activityChartPanel = new JTabbedPane();
		if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
	        activityChartPanel.addTab("Frequency", null, activityFrequencyChartPanel, "Frequency");
	        activityChartPanel.addTab("Median duration", null, activityMedianDurationChartPanel, "Median duration");
	        activityChartPanel.addTab("Mean duration", null, activityMeanDurationChartPanel, "Mean duration");
	        activityChartPanel.addTab("Duration range", null, activityDurationRangeChartPanel, "Duration range");
	        activityChartPanel.addTab("Aggregate duration", null, activityAggregateDurationChartPanel, "Aggregate duration");
		}
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
	        activityChartPanel.addTab("活动频度图", null, activityFrequencyChartPanel, "活动频度图");
	        activityChartPanel.addTab("中位时间图", null, activityMedianDurationChartPanel, "中位时间图");
	        activityChartPanel.addTab("平均时间图", null, activityMeanDurationChartPanel, "平均时间图");
	        activityChartPanel.addTab("最大时间图", null, activityDurationRangeChartPanel, "最大时间图");
	        activityChartPanel.addTab("累积时间图", null, activityAggregateDurationChartPanel, "累积时间图");
		}
        
		activityActivitiesLabel = new JTextPane();
		activityActivitiesLabel.setEditable(false);
		activityActivitiesLabel.setBackground(null);
		activityMinimalFrequencyLabel = new JTextPane();
        activityMinimalFrequencyLabel.setEditable(false);
        activityMinimalFrequencyLabel.setBackground(null);
        activityMedianFrequencyLabel = new JTextPane();
        activityMedianFrequencyLabel.setEditable(false);
        activityMedianFrequencyLabel.setBackground(null);
        activityMeanFrequencyLabel = new JTextPane();
        activityMeanFrequencyLabel.setEditable(false);
        activityMeanFrequencyLabel.setBackground(null);
        activityMaximalFrequencyLabel = new JTextPane();
        activityMaximalFrequencyLabel.setEditable(false);
        activityMaximalFrequencyLabel.setBackground(null);
        activityFrequencyStdDeviationLabel = new JTextPane();
        activityFrequencyStdDeviationLabel.setEditable(false);
        activityFrequencyStdDeviationLabel.setBackground(null);
        activityLabelPanel = new JPanel(new GridLayout(0, 1));
        activityLabelPanel.add(activityActivitiesLabel);
        activityLabelPanel.add(activityMinimalFrequencyLabel);
        activityLabelPanel.add(activityMedianFrequencyLabel);
        activityLabelPanel.add(activityMeanFrequencyLabel);
        activityLabelPanel.add(activityMaximalFrequencyLabel);
        activityLabelPanel.add(activityFrequencyStdDeviationLabel);
		
		activityStaticPanel = new JPanel(new BorderLayout());
		activityStaticPanel.add(activityLabelPanel, BorderLayout.EAST);
		activityStaticPanel.add(activityChartPanel, BorderLayout.CENTER);
		
		activityAllActivitiesTable = new RowSelectableJTable();
		activityAllActivitiesTablePanel = new JScrollPane(activityAllActivitiesTable);
		activityFirstInCaseTable = new RowSelectableJTable();
        activityFirstInCaseTablePanel = new JScrollPane(activityFirstInCaseTable);
        activityLastInCaseTable = new RowSelectableJTable();
        activityLastInCaseTablePanel = new JScrollPane(activityLastInCaseTable);
        activityTablePanel = new JTabbedPane();
		if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
	        activityTablePanel.addTab("All activities (" + MainFrame.activityCollection.getSize() + ")", null, activityAllActivitiesTablePanel, "All activities");
	        activityTablePanel.addTab("First in case (" + MainFrame.activityCollection.getFirst() + ")", null, activityFirstInCaseTablePanel, "First in case");
	        activityTablePanel.addTab("Last in case (" + MainFrame.activityCollection.getLast() + ")", null, activityLastInCaseTablePanel, "Last in case");
		}
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
	        activityTablePanel.addTab("所有活动(" + MainFrame.activityCollection.getSize() + ")", null, activityAllActivitiesTablePanel, "所有活动");
	        activityTablePanel.addTab("开始活动(" + MainFrame.activityCollection.getFirst() + ")", null, activityFirstInCaseTablePanel, "开始活动");
	        activityTablePanel.addTab("结束活动(" + MainFrame.activityCollection.getLast() + ")", null, activityLastInCaseTablePanel, "结束活动");
		}
		
		activityPanel = new JPanel(new BorderLayout());
		activityPanel.add(activityTablePanel, BorderLayout.CENTER);
		activityPanel.add(activityStaticPanel, BorderLayout.NORTH);
		
        resourceFrequencyChartPanel = new ChartPanel(null);
        resourceFrequencyChartPanel.setPreferredSize(new Dimension(resourceFrequencyChartPanel.getWidth(), 200));
        resourceFrequencyChartPanel.setDismissDelay(36000000);
        resourceFrequencyChartPanel.setInitialDelay(0);
        resourceFrequencyChartPanel.setReshowDelay(0);
        resourceMedianDurationChartPanel = new ChartPanel(null);
        resourceMedianDurationChartPanel.setPreferredSize(new Dimension(resourceMedianDurationChartPanel.getWidth(), 200));
        resourceMedianDurationChartPanel.setDismissDelay(36000000);
        resourceMedianDurationChartPanel.setInitialDelay(0);
        resourceMedianDurationChartPanel.setReshowDelay(0);
        resourceMeanDurationChartPanel = new ChartPanel(null);
        resourceMeanDurationChartPanel.setPreferredSize(new Dimension(resourceMeanDurationChartPanel.getWidth(), 200));
        resourceMeanDurationChartPanel.setDismissDelay(36000000);
        resourceMeanDurationChartPanel.setInitialDelay(0);
        resourceMeanDurationChartPanel.setReshowDelay(0);
        resourceDurationRangeChartPanel = new ChartPanel(null);
        resourceDurationRangeChartPanel.setPreferredSize(new Dimension(resourceDurationRangeChartPanel.getWidth(), 200));
        resourceDurationRangeChartPanel.setDismissDelay(36000000);
        resourceDurationRangeChartPanel.setInitialDelay(0);
        resourceDurationRangeChartPanel.setReshowDelay(0);
        resourceAggregateDurationChartPanel = new ChartPanel(null);
        resourceAggregateDurationChartPanel.setPreferredSize(new Dimension(resourceAggregateDurationChartPanel.getWidth(), 200));
        resourceAggregateDurationChartPanel.setDismissDelay(36000000);
        resourceAggregateDurationChartPanel.setInitialDelay(0);
        resourceAggregateDurationChartPanel.setReshowDelay(0);
        
        resourceChartPanel = new JTabbedPane();
		if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
	        resourceChartPanel.addTab("Frequency", null, resourceFrequencyChartPanel, "Frequency");
	        resourceChartPanel.addTab("Median duration", null, resourceMedianDurationChartPanel, "Median duration");
	        resourceChartPanel.addTab("Mean duration", null, resourceMeanDurationChartPanel, "Mean duration");
	        resourceChartPanel.addTab("Duration range", null, resourceDurationRangeChartPanel, "Duration range");
	        resourceChartPanel.addTab("Aggregate duration", null, resourceAggregateDurationChartPanel, "Aggregate duration");
		}
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
			resourceChartPanel.addTab("资源频度图", null, resourceFrequencyChartPanel, "资源频度图");
			resourceChartPanel.addTab("中位时间图", null, resourceMedianDurationChartPanel, "中位时间图");
			resourceChartPanel.addTab("平均时间图", null, resourceMeanDurationChartPanel, "平均时间图");
			resourceChartPanel.addTab("最大时间图", null, resourceDurationRangeChartPanel, "最大时间图");
			resourceChartPanel.addTab("累积时间图", null, resourceAggregateDurationChartPanel, "累积时间图");
		}
        
        resourceResourcesLabel = new JTextPane();
        resourceResourcesLabel.setEditable(false);
        resourceResourcesLabel.setBackground(null);
        resourceMinimalFrequencyLabel = new JTextPane();
        resourceMinimalFrequencyLabel.setEditable(false);
        resourceMinimalFrequencyLabel.setBackground(null);
        resourceMedianFrequencyLabel = new JTextPane();
        resourceMedianFrequencyLabel.setEditable(false);
        resourceMedianFrequencyLabel.setBackground(null);
        resourceMeanFrequencyLabel = new JTextPane();
        resourceMeanFrequencyLabel.setEditable(false);
        resourceMeanFrequencyLabel.setBackground(null);
        resourceMaximalFrequencyLabel = new JTextPane();
        resourceMaximalFrequencyLabel.setEditable(false);
        resourceMaximalFrequencyLabel.setBackground(null);
        resourceFrequencyStdDeviationLabel = new JTextPane();
        resourceFrequencyStdDeviationLabel.setEditable(false);
        resourceFrequencyStdDeviationLabel.setBackground(null);
        resourceLabelPanel = new JPanel(new GridLayout(0, 1));
        resourceLabelPanel.add(resourceResourcesLabel);
        resourceLabelPanel.add(resourceMinimalFrequencyLabel);
        resourceLabelPanel.add(resourceMedianFrequencyLabel);
        resourceLabelPanel.add(resourceMeanFrequencyLabel);
        resourceLabelPanel.add(resourceMaximalFrequencyLabel);
        resourceLabelPanel.add(resourceFrequencyStdDeviationLabel);
        
        resourceStaticPanel = new JPanel(new BorderLayout());
        resourceStaticPanel.add(resourceLabelPanel, BorderLayout.EAST);
        resourceStaticPanel.add(resourceChartPanel, BorderLayout.CENTER);

        resourceAllResourcesTable = new RowSelectableJTable();
        resourceAllResourcesTablePanel = new JScrollPane(resourceAllResourcesTable);
        resourceFirstInCaseTable = new RowSelectableJTable();
        resourceFirstInCaseTablePanel = new JScrollPane(resourceFirstInCaseTable);
        resourceLastInCaseTable = new RowSelectableJTable();
        resourceLastInCaseTablePanel = new JScrollPane(resourceLastInCaseTable);
        resourceTablePanel = new JTabbedPane();
		if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
	        resourceTablePanel.addTab("All resources (" + MainFrame.resourceCollection.getSize() + ")", null, resourceAllResourcesTablePanel, "All resources");
	        resourceTablePanel.addTab("First in case (" + MainFrame.resourceCollection.getFirst() + ")", null, resourceFirstInCaseTablePanel, "First in case");
	        resourceTablePanel.addTab("Last in case (" + MainFrame.resourceCollection.getLast() + ")", null, resourceLastInCaseTablePanel, "Last in case");
	 	}
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
	        resourceTablePanel.addTab("所有资源(" + MainFrame.resourceCollection.getSize() + ")", null, resourceAllResourcesTablePanel, "所有资源");
	        resourceTablePanel.addTab("开始资源(" + MainFrame.resourceCollection.getFirst() + ")", null, resourceFirstInCaseTablePanel, "开始资源");
	        resourceTablePanel.addTab("结束资源(" + MainFrame.resourceCollection.getLast() + ")", null, resourceLastInCaseTablePanel, "结束资源");
	 	}
       
        resourcePanel = new JPanel(new BorderLayout());
        resourcePanel.add(resourceTablePanel, BorderLayout.CENTER);
        resourcePanel.add(resourceStaticPanel, BorderLayout.NORTH);
		
//		tabbedPanel = new JTabbedPane(JTabbedPane.LEFT);
//		if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
//		{
//	        tabbedPanel.addTab(null, new VerticalTextIcon("  Overview  ", false), overviewPanel, "Global Statics");
//	        tabbedPanel.addTab(null, new VerticalTextIcon("  Activity  ", false), activityPanel, "Activity Classes");
//	        tabbedPanel.addTab(null, new VerticalTextIcon("  Resource  ", false), resourcePanel, "Resource Classes");
//	 	}
//		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
//		{
//	        tabbedPanel.addTab(null, new VerticalTextIcon("  全局  ", false), overviewPanel, "全局统计");
//	        tabbedPanel.addTab(null, new VerticalTextIcon("  活动  ", false), activityPanel, "活动统计");
//	        tabbedPanel.addTab(null, new VerticalTextIcon("  资源  ", false), resourcePanel, "资源统计");
//	 	}
        tabbedPanel = new JTabbedPane();
		if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
	        tabbedPanel.addTab("Overview", null, overviewPanel, "Global Statics");
	        tabbedPanel.addTab("Activity", null, activityPanel, "Activity Classes");
	        tabbedPanel.addTab("Resource", null, resourcePanel, "Resource Classes");	 	}
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
	        tabbedPanel.addTab("全局", null, overviewPanel, "全局统计");
	        tabbedPanel.addTab("活动", null, activityPanel, "活动统计");
	        tabbedPanel.addTab("资源", null, resourcePanel, "资源统计");
	 	}

		buttonPanel = new ButtonPanel();
		buttonPanel.setStatics();
        
        add(buttonPanel, BorderLayout.NORTH);
        add(tabbedPanel, BorderLayout.CENTER);
        
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
        updateOverviewCaseTable();
        updateOverviewVariantTable();
        updateOverviewLabel();
        updateOverviewActiveCasesOverTimeChart();
        updateOverviewEventsOverTimeChart();
        updateOverviewCaseVariantsChart();
        updateOverviewEventsPerCaseChart();
        updateOverviewCaseDurationChart();
        updateOverviewCaseUtilizationChart();
        updateOverviewMeanActivityDurationChart();
        updateOverviewMeanWaitingTimeChart();
        updateActivityAllActivitiesTable();
        updateActivityFirstInCaseTable();
        updateActivityLastInCaseTable();
        updateActivityLabel();
        updateActivityFrequencyChart();
        updateActivityMedianDurationChart();
        updateActivityMeanDurationChart();
        updateActivityDurationRangeChart();
        updateActivityAggregateDurationChart();
        updateResourceAllResourcesTable();
        updateResourceFirstInCaseTable();
        updateResourceLastInCaseTable();
        updateResourceLabel();
        updateResourceFrequencyChart();
        updateResourceMedianDurationChart();
        updateResourceMeanDurationChart();
        updateResourceDurationRangeChart();
        updateResourceAggregateDurationChart();
    }
    
    
    public void updateOverviewCaseTable()
    {
        int size = MainFrame.caseCollection.getSize();
        if(size > 1000)
            size = 1000;
        String[][] tableData = new String[size][5];
        for(int i = 0; i < size; i++)
        {
            Case mycase = MainFrame.caseCollection.getCase(i);
            tableData[i][0] = mycase.getCase();
            tableData[i][1] = mycase.getSize() + "";
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            tableData[i][2] = formatter.format(mycase.getStart());
            tableData[i][3] = formatter.format(mycase.getEnd());
            long diff = mycase.getDuration();
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
	        String[] headlines = {"Case ID","Events","Started","Finished","Duration"};
	        overviewCaseTableModel = new DefaultTableModel(tableData, headlines);
        }
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
	        String[] headlines = {"实例名","事件数","开始时间","结束时间","持续时间"};
	        overviewCaseTableModel = new DefaultTableModel(tableData, headlines);
	 	}
        overviewCaseTable.setModel(overviewCaseTableModel);
        
        ComparatorRow c = new ComparatorRow();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<DefaultTableModel>(overviewCaseTableModel);
        sorter.setComparator(0, c);
        sorter.setComparator(1, c);
        sorter.setComparator(2, c);
        sorter.setComparator(3, c);
        sorter.setComparator(4, c);
        overviewCaseTable.setRowSorter(sorter);
    }
    
    
    public void updateOverviewVariantTable()
    {
        int size = MainFrame.variantCollection.getSize();
        if(size > 1000)
            size = 1000;
        String[][] tableData = new String[size][5];
        for(int i = 0; i < size; i++)
        {
            Variant variant = MainFrame.variantCollection.getVariant(i);
    		if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
    		{
                tableData[i][0] = "Variant " + variant.getVariant();
            }
    		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
    		{
                tableData[i][0] = "实例种类" + variant.getVariant();
    	 	}
            tableData[i][1] = variant.getSize() + "";
            tableData[i][2] = variant.getActivitiesSize() + "";
            long median = variant.getMedianDuration();
            long medianMinutes = median / (60 * 1000) % 60;
            long medianHours = median / (60 * 60 * 1000) % 24;
            long medianDays = median / (24 * 60 * 60 * 1000);
    		if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
    		{
                tableData[i][3] = medianDays + " days " + medianHours + " hours " + medianMinutes + " mins";
            }
    		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
    		{
                tableData[i][3] = medianDays + "天" + medianHours + "小时" + medianMinutes + "分";
    	 	}
            long mean = variant.getMeanDuration();
            long meanMinutes = mean / (60 * 1000) % 60;
            long meanHours = mean / (60 * 60 * 1000) % 24;
            long meanDays = mean / (24 * 60 * 60 * 1000);
    		if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
    		{
                tableData[i][4] = meanDays + " days " + meanHours + " hours " + meanMinutes + " mins";
            }
    		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
    		{
                tableData[i][4] = meanDays + "天" + meanHours + "小时" + meanMinutes + "分";
    	 	}
        }
		if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
	        String[] headlines = {"Variant","Cases","Events","Median duration","Mean duration"};
	        overviewVariantTableModel = new DefaultTableModel(tableData, headlines);
        }
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
	        String[] headlines = {"实例种类","实例数","事件数","中位时间","平均时间"};
	        overviewVariantTableModel = new DefaultTableModel(tableData, headlines);
	 	}
        overviewVariantTable.setModel(overviewVariantTableModel);
        
        ComparatorRow c = new ComparatorRow();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<DefaultTableModel>(overviewVariantTableModel);
        sorter.setComparator(0, c);
        sorter.setComparator(1, c);
        sorter.setComparator(2, c);
        sorter.setComparator(3, c);
        sorter.setComparator(4, c);
        overviewVariantTable.setRowSorter(sorter);
    }

    
    public void updateOverviewLabel()
    {
		if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
	        overviewEventsLabel.setText("Events : " + MainFrame.eventCollection.getSize());
	        overviewCasesLabel.setText("Cases : " + MainFrame.caseCollection.getSize());
	        overviewActivitiesLabel.setText("Activities : " + (MainFrame.graphNet.activityCount - 2));
	        long median = MainFrame.caseCollection.getMedianDuration();
	        long medianMinutes = median / (60 * 1000) % 60;
	        long medianHours = median / (60 * 60 * 1000) % 24;
	        long medianDays = median / (24 * 60 * 60 * 1000);
	        overviewMedianCaseDurationLabel.setText("Median case duration : " + medianDays + " days " + medianHours + " hours " + medianMinutes + " mins");
	        long mean = MainFrame.caseCollection.getMeanDuration();
	        long meanMinutes = mean / (60 * 1000) % 60;
	        long meanHours = mean / (60 * 60 * 1000) % 24;
	        long meanDays = mean / (24 * 60 * 60 * 1000);
	        overviewMeanCaseDurationLabel.setText("Mean case duration : " + meanDays + " days " + meanHours + " hours " + meanMinutes + " mins");
	        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        overviewStartLabel.setText("Start : " + formatter.format(MainFrame.caseCollection.getStart()));
	        overviewEndLabel.setText("End : " + formatter.format(MainFrame.caseCollection.getEnd()));
        }
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
	        overviewEventsLabel.setText("事件数：" + MainFrame.eventCollection.getSize());
	        overviewCasesLabel.setText("实例数：" + MainFrame.caseCollection.getSize());
	        overviewActivitiesLabel.setText("活动数：" + (MainFrame.graphNet.activityCount - 2));
	        long median = MainFrame.caseCollection.getMedianDuration();
	        long medianMinutes = median / (60 * 1000) % 60;
	        long medianHours = median / (60 * 60 * 1000) % 24;
	        long medianDays = median / (24 * 60 * 60 * 1000);
	        overviewMedianCaseDurationLabel.setText("中位实例持续时间：" + medianDays + "天" + medianHours + "小时" + medianMinutes + "分");
	        long mean = MainFrame.caseCollection.getMeanDuration();
	        long meanMinutes = mean / (60 * 1000) % 60;
	        long meanHours = mean / (60 * 60 * 1000) % 24;
	        long meanDays = mean / (24 * 60 * 60 * 1000);
	        overviewMeanCaseDurationLabel.setText("平均实例持续时间：" + meanDays + "天" + meanHours + "小时" + meanMinutes + "分");
	        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        overviewStartLabel.setText("开始时间：" + formatter.format(MainFrame.caseCollection.getStart()));
	        overviewEndLabel.setText("结束时间：" + formatter.format(MainFrame.caseCollection.getEnd()));
	 	}
    }
    
    
    public void updateOverviewEventsOverTimeChart()
    {
        TimeSeries series = new TimeSeries("");
        int count = 0;
        for(int i = 0; i < MainFrame.eventsOverTimeChart.getSize(); i++)
        {
            Date time = MainFrame.eventsOverTimeChart.getDate(i);
            int minute = time.getMinutes();
            int hour = time.getHours();
            int day = time.getDate();
            int month = time.getMonth() + 1;
            int year = time.getYear() + 1900;
            Minute timeChart = new Minute(minute, hour, day, month, year);
            count += MainFrame.eventsOverTimeChart.getEventCount(i);
            series.addOrUpdate(timeChart, count);
        }
        
        overviewEventsOverTimeDataset = new TimeSeriesCollection();
        overviewEventsOverTimeDataset.addSeries(series);
        
        overviewEventsOverTimeChart = ChartFactory.createXYAreaChart(
                null,                          // chart title
                null,                          // domain axis label
                null,                          // range axis label
                overviewEventsOverTimeDataset, // data
                PlotOrientation.VERTICAL,      // orientation
                false,                         // include legend
                true,                          // tooltips
                false                          // urls
            );
        
        overviewEventsOverTimeChart.setBackgroundPaint(null);
        XYPlot plot = overviewEventsOverTimeChart.getXYPlot();
        DateAxis domain;
        if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
        	domain = new DateAxis("Log timeline");
        }
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
			domain = new DateAxis("时间轴");
	 	}
		else
		{
			domain = new DateAxis("Log timeline");
		}
        domain.setTickLabelsVisible(false);
        domain.setAxisLineVisible(false);
        domain.setTickMarksVisible(false);
        plot.setDomainAxis(domain);
        NumberAxis range;
        if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
        	range = new NumberAxis("Events");
        }
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
			range = new NumberAxis("事件数");
	 	}
		else
		{
			range = new NumberAxis("Events");
		}
        range.setTickLabelsVisible(false);
        range.setAxisLineVisible(false);
        range.setTickMarksVisible(false);
        plot.setRangeAxis(range);
        
        XYAreaRenderer renderer = (XYAreaRenderer) plot.getRenderer();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DecimalFormat numberFormatter = new DecimalFormat("#0");
        if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
            renderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator("{1}, {2} events", dateFormatter, numberFormatter));
        }
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
	        renderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator("{1}，{2}个事件", dateFormatter, numberFormatter));
	 	}
        
        overviewEventsOverTimeChartPanel.setChart(overviewEventsOverTimeChart);
    }

    
    public void updateOverviewActiveCasesOverTimeChart()
    {
        TimeSeries series = new TimeSeries("");
        int count = 0;
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
            series.addOrUpdate(timeChart, count);
        }
        
        overviewActiveCasesOverTimeDataset = new TimeSeriesCollection();
        overviewActiveCasesOverTimeDataset.addSeries(series);
        
        overviewActiveCasesOverTimeChart = ChartFactory.createXYAreaChart(
                null,                               // chart title
                null,                               // domain axis label
                null,                               // range axis label
                overviewActiveCasesOverTimeDataset, // data
                PlotOrientation.VERTICAL,           // orientation
                false,                              // include legend
                true,                               // tooltips
                false                               // urls
            );
        
        overviewActiveCasesOverTimeChart.setBackgroundPaint(null);
        XYPlot plot = overviewActiveCasesOverTimeChart.getXYPlot();
        DateAxis domain;
        if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
        	domain = new DateAxis("Log timeline");
        }
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
			domain = new DateAxis("时间轴");
	 	}
		else
		{
			domain = new DateAxis("Log timeline");
		}
        domain.setTickLabelsVisible(false);
        domain.setAxisLineVisible(false);
        domain.setTickMarksVisible(false);
        plot.setDomainAxis(domain);
        NumberAxis range;
        if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
        	range = new NumberAxis("Active Cases");
        }
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
			range = new NumberAxis("实例数");
	 	}
		else
		{
			range = new NumberAxis("Active Cases");
		}
        range.setTickLabelsVisible(false);
        range.setAxisLineVisible(false);
        range.setTickMarksVisible(false);
        plot.setRangeAxis(range);
        
        XYAreaRenderer renderer = (XYAreaRenderer) plot.getRenderer();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DecimalFormat numberFormatter = new DecimalFormat("#0");
        if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
            renderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator("{1}, {2} active cases", dateFormatter, numberFormatter));
        }
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
	        renderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator("{1}，{2}个实例", dateFormatter, numberFormatter));
	 	}
        
        overviewActiveCasesOverTimeChartPanel.setChart(overviewActiveCasesOverTimeChart);
    }

    
    public void updateOverviewCaseVariantsChart()
    {
        XYSeries series = new XYSeries("");
        for(int i = 0; i < MainFrame.variantCollection.getSize(); i++)
        {
            Variant variant = MainFrame.variantCollection.getVariant(i);
            series.add(i + 1, variant.getSize());
        }
        
        overviewCaseVariantsDataset = new XYSeriesCollection();
        overviewCaseVariantsDataset.addSeries(series);
        
        overviewCaseVariantsChart = ChartFactory.createXYBarChart(
                null,                        // chart title
                null,                        // domain axis label
                false,                       // date chart
                null,                        // range axis label
                overviewCaseVariantsDataset, // data
                PlotOrientation.VERTICAL,    // orientation
                false,                       // include legend
                true,                        // tooltips
                false                        // urls
            );
        
        overviewCaseVariantsChart.setBackgroundPaint(null);
        XYPlot plot = overviewCaseVariantsChart.getXYPlot();
        NumberAxis domain;
        if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
        	domain = new NumberAxis("Variants");
        }
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
			domain = new NumberAxis("实例种类");
	 	}
		else
		{
			domain = new NumberAxis("Variants");
		}
        domain.setTickLabelsVisible(false);
        domain.setAxisLineVisible(false);
        domain.setTickMarksVisible(false);
        plot.setDomainAxis(domain);
        NumberAxis range;
        if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
        	range = new NumberAxis("Number of cases");
        }
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
			range = new NumberAxis("实例数");
	 	}
		else
		{
			range = new NumberAxis("Number of cases");
		}
        range.setTickLabelsVisible(false);
        range.setAxisLineVisible(false);
        range.setTickMarksVisible(false);
        plot.setRangeAxis(range);
        
        XYBarRenderer renderer = (XYBarRenderer) plot.getRenderer();
        DecimalFormat numberFormatter = new DecimalFormat("#0");
        if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
        	renderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator("Variant {1}, {2} cases", numberFormatter, numberFormatter));
        }
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
			renderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator("实例种类{1}，{2}个实例", numberFormatter, numberFormatter));
	 	}
        
        overviewCaseVariantsChartPanel.setChart(overviewCaseVariantsChart);
    }
    
    
    public void updateOverviewEventsPerCaseChart()
    {
        XYSeries series = new XYSeries("");
        for(int i = 0; i < MainFrame.eventsPerCaseChart.getSize(); i++)
        {
            series.add(MainFrame.eventsPerCaseChart.getEventCount(i), MainFrame.eventsPerCaseChart.getCaseCount(i));
        }
        
        overviewEventsPerCaseDataset = new XYSeriesCollection();
        overviewEventsPerCaseDataset.addSeries(series);
        
        overviewEventsPerCaseChart = ChartFactory.createXYBarChart(
                null,                         // chart title
                null,                         // domain axis label
                false,                        // date chart
                null,                         // range axis label
                overviewEventsPerCaseDataset, // data
                PlotOrientation.VERTICAL,     // orientation
                false,                        // include legend
                true,                         // tooltips
                false                         // urls
            );
        
        overviewEventsPerCaseChart.setBackgroundPaint(null);
        XYPlot plot = overviewEventsPerCaseChart.getXYPlot();
        NumberAxis domain;
        if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
        	domain = new NumberAxis("Events per case");
        }
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
			domain = new NumberAxis("事件数");
	 	}
		else
		{
			domain = new NumberAxis("Events per case");
		}
        domain.setTickLabelsVisible(false);
        domain.setAxisLineVisible(false);
        domain.setTickMarksVisible(false);
        plot.setDomainAxis(domain);
        NumberAxis range;
        if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
        	range = new NumberAxis("Number of cases");
        }
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
			range = new NumberAxis("实例数");
	 	}
		else
		{
			range = new NumberAxis("Number of cases");
		}
        range.setTickLabelsVisible(false);
        range.setAxisLineVisible(false);
        range.setTickMarksVisible(false);
        plot.setRangeAxis(range);
        
        XYBarRenderer renderer = (XYBarRenderer) plot.getRenderer();
        DecimalFormat numberFormatter = new DecimalFormat("#0");
        if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
            renderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator("{1} events, {2} cases", numberFormatter, numberFormatter));
        }
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
	        renderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator("{1}个事件，{2}个实例", numberFormatter, numberFormatter));
	 	}
        
        overviewEventsPerCaseChartPanel.setChart(overviewEventsPerCaseChart);
    }
    
    
    public void updateOverviewCaseDurationChart()
    {
        XYSeries series = new XYSeries("");
        ArrayList<String> tips = new ArrayList<String>();
        for(int i = 0; i < MainFrame.caseDurationChart.getSize(); i++)
        {
            long duration = MainFrame.caseDurationChart.getDuration(i);
            int count = MainFrame.caseDurationChart.getCaseCount(i);
            series.add(duration, count);
            long durationMinutes = duration / (60 * 1000) % 60;
            long durationHours = duration / (60 * 60 * 1000) % 24;
            long durationDays = duration / (24 * 60 * 60 * 1000);
            if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
    		{
                tips.add("Up to " + durationDays + " days " + durationHours + " hours " + durationMinutes + " mins, " + count + " cases");
            }
    		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
    		{
                tips.add("不大于" + durationDays + "天" + durationHours + "小时" + durationMinutes + "分，" + count + "个实例");
    	 	}
        }
        
        overviewCaseDurationDataset = new XYSeriesCollection();
        overviewCaseDurationDataset.addSeries(series);
        overviewCaseDurationDataset.setIntervalWidth(MainFrame.caseDurationChart.getDuration(0));
        
        overviewCaseDurationChart = ChartFactory.createXYBarChart(
                null,                        // chart title
                null,                        // domain axis label
                false,                       // date chart
                null,                        // range axis label
                overviewCaseDurationDataset, // data
                PlotOrientation.VERTICAL,    // orientation
                false,                       // include legend
                true,                        // tooltips
                false                        // urls
            );
        
        overviewCaseDurationChart.setBackgroundPaint(null);
        XYPlot plot = overviewCaseDurationChart.getXYPlot();
        NumberAxis domain;
        if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
        	domain = new NumberAxis("Case duration");
        }
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
			domain = new NumberAxis("实例持续时间");
	 	}
		else
		{
			domain = new NumberAxis("Case duration");
		}
        domain.setTickLabelsVisible(false);
        domain.setAxisLineVisible(false);
        domain.setTickMarksVisible(false);
        plot.setDomainAxis(domain);
        NumberAxis range;
        if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
        	range = new NumberAxis("Number of cases");
        }
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
			range = new NumberAxis("实例数");
	 	}
		else
		{
			range = new NumberAxis("Number of cases");
		}
        range.setTickLabelsVisible(false);
        range.setAxisLineVisible(false);
        range.setTickMarksVisible(false);
        plot.setRangeAxis(range);
        
        XYBarRenderer renderer = (XYBarRenderer) plot.getRenderer();
        CustomXYToolTipGenerator myToolTip = new CustomXYToolTipGenerator();
        myToolTip.addToolTipSeries(tips);
        renderer.setBaseToolTipGenerator(myToolTip);
        
        overviewCaseDurationChartPanel.setChart(overviewCaseDurationChart);
    }
    
    
    public void updateOverviewCaseUtilizationChart()
    {
        XYSeries series = new XYSeries("");
        for(int i = 0; i < MainFrame.caseUtilizationChart.getSize(); i++)
        {
            series.add(MainFrame.caseUtilizationChart.getPercentage(i), MainFrame.caseUtilizationChart.getCaseCount(i));
        }
        
        overviewCaseUtilizationDataset = new XYSeriesCollection();
        overviewCaseUtilizationDataset.addSeries(series);
        
        overviewCaseUtilizationChart = ChartFactory.createXYBarChart(
                null,                           // chart title
                null,                           // domain axis label
                false,                          // date chart
                null,                           // range axis label
                overviewCaseUtilizationDataset, // data
                PlotOrientation.VERTICAL,       // orientation
                false,                          // include legend
                true,                           // tooltips
                false                           // urls
            );
        
        overviewCaseUtilizationChart.setBackgroundPaint(null);
        XYPlot plot = overviewCaseUtilizationChart.getXYPlot();
        NumberAxis domain;
        if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
        	domain = new NumberAxis("Relative active time");
        }
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
			domain = new NumberAxis("活动时间百分比");
	 	}
		else
		{
			domain = new NumberAxis("Relative active time");
		}
        domain.setTickLabelsVisible(false);
        domain.setAxisLineVisible(false);
        domain.setTickMarksVisible(false);
        plot.setDomainAxis(domain);
        NumberAxis range;
        if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
        	range = new NumberAxis("Number of cases");
        }
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
			range = new NumberAxis("实例数");
	 	}
		else
		{
			range = new NumberAxis("Number of cases");
		}
        range.setTickLabelsVisible(false);
        range.setAxisLineVisible(false);
        range.setTickMarksVisible(false);
        plot.setRangeAxis(range);
        
        XYBarRenderer renderer = (XYBarRenderer) plot.getRenderer();
        DecimalFormat numberFormatter = new DecimalFormat("#0");
        if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
            renderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator("Up to {1}%, {2} cases", numberFormatter, numberFormatter));
        }
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
	        renderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator("不大于{1}%, {2}个实例", numberFormatter, numberFormatter));
	 	}
        
        overviewCaseUtilizationChartPanel.setChart(overviewCaseUtilizationChart);
    }
  
    
    public void updateOverviewMeanActivityDurationChart()
    {
        XYSeries series = new XYSeries("");
        ArrayList<String> tips = new ArrayList<String>();
        for(int i = 0; i < MainFrame.meanActivityDurationChart.getSize(); i++)
        {
            double duration = MainFrame.meanActivityDurationChart.getDuration(i);
            int count = MainFrame.meanActivityDurationChart.getCaseCount(i);
            series.add(duration, count);
            long durationMinutes = (long) duration / (60 * 1000) % 60;
            long durationHours = (long) duration / (60 * 60 * 1000) % 24;
            long durationDays = (long) duration / (24 * 60 * 60 * 1000);
            if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
    		{
                tips.add("Up to " + durationDays + " days " + durationHours + " hours " + durationMinutes + " mins, " + count + " cases");
            }
    		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
    		{
                tips.add("不大于" + durationDays + "天" + durationHours + "小时" + durationMinutes + "分，" + count + "个实例");
    	 	}
        }
        
        overviewMeanActivityDurationDataset = new XYSeriesCollection();
        overviewMeanActivityDurationDataset.addSeries(series);
        overviewMeanActivityDurationDataset.setIntervalWidth(MainFrame.meanActivityDurationChart.getDuration(0));
        
        overviewMeanActivityDurationChart = ChartFactory.createXYBarChart(
                null,                                // chart title
                null,                                // domain axis label
                false,                               // date chart
                null,                                // range axis label
                overviewMeanActivityDurationDataset, // data
                PlotOrientation.VERTICAL,            // orientation
                false,                               // include legend
                true,                                // tooltips
                false                                // urls
            );
        
        overviewMeanActivityDurationChart.setBackgroundPaint(null);
        XYPlot plot = overviewMeanActivityDurationChart.getXYPlot();
        NumberAxis domain;
        if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
        	domain = new NumberAxis("Mean activity duration");
        }
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
			domain = new NumberAxis("平均活动时间");
	 	}
		else
		{
			domain = new NumberAxis("Mean activity duration");
		}
        domain.setTickLabelsVisible(false);
        domain.setAxisLineVisible(false);
        domain.setTickMarksVisible(false);
        plot.setDomainAxis(domain);
        NumberAxis range;
        if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
        	range = new NumberAxis("Number of cases");
        }
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
			range = new NumberAxis("实例数");
	 	}
		else
		{
			range = new NumberAxis("Number of cases");
		}
        range.setTickLabelsVisible(false);
        range.setAxisLineVisible(false);
        range.setTickMarksVisible(false);
        plot.setRangeAxis(range);
        
        XYBarRenderer renderer = (XYBarRenderer) plot.getRenderer();
        CustomXYToolTipGenerator myToolTip = new CustomXYToolTipGenerator();
        myToolTip.addToolTipSeries(tips);
        renderer.setBaseToolTipGenerator(myToolTip);
        
        overviewMeanActivityDurationChartPanel.setChart(overviewMeanActivityDurationChart);
    }
   
    
    public void updateOverviewMeanWaitingTimeChart()
    {
        XYSeries series = new XYSeries("");
        ArrayList<String> tips = new ArrayList<String>();
        for(int i = 0; i < MainFrame.meanWaitingTimeChart.getSize(); i++)
        {
            double duration = MainFrame.meanWaitingTimeChart.getDuration(i);
            int count = MainFrame.meanWaitingTimeChart.getCaseCount(i);
            series.add(duration, count);
            long durationMinutes = (long) duration / (60 * 1000) % 60;
            long durationHours = (long) duration / (60 * 60 * 1000) % 24;
            long durationDays = (long) duration / (24 * 60 * 60 * 1000);
            if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
    		{
                tips.add("Up to " + durationDays + " days " + durationHours + " hours " + durationMinutes + " mins, " + count + " cases");
            }
    		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
    		{
                tips.add("不大于" + durationDays + "天" + durationHours + "小时" + durationMinutes + "分，" + count + "个实例");
    	 	}
        }
        
        overviewMeanWaitingTimeDataset = new XYSeriesCollection();
        overviewMeanWaitingTimeDataset.addSeries(series);
        overviewMeanWaitingTimeDataset.setIntervalWidth(MainFrame.meanWaitingTimeChart.getDuration(0));
        
        overviewMeanWaitingTimeChart = ChartFactory.createXYBarChart(
                null,                           // chart title
                null,                           // domain axis label
                false,                          // date chart
                null,                           // range axis label
                overviewMeanWaitingTimeDataset, // data
                PlotOrientation.VERTICAL,       // orientation
                false,                          // include legend
                true,                           // tooltips
                false                           // urls
            );
        
        overviewMeanWaitingTimeChart.setBackgroundPaint(null);
        XYPlot plot = overviewMeanWaitingTimeChart.getXYPlot();
        NumberAxis domain = new NumberAxis("Mean waiting time");
        if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
        	domain = new NumberAxis("Mean waiting time");
        }
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
			domain = new NumberAxis("平均等待时间");
	 	}
		else
		{
			domain = new NumberAxis("Mean waiting time");
		}
        domain.setTickLabelsVisible(false);
        domain.setAxisLineVisible(false);
        domain.setTickMarksVisible(false);
        plot.setDomainAxis(domain);
        NumberAxis range;
        if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
        	range = new NumberAxis("Number of cases");
        }
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
			range = new NumberAxis("实例数");
	 	}
		else
		{
			range = new NumberAxis("Number of cases");
		}
        range.setTickLabelsVisible(false);
        range.setAxisLineVisible(false);
        range.setTickMarksVisible(false);
        plot.setRangeAxis(range);
        
        XYBarRenderer renderer = (XYBarRenderer) plot.getRenderer();
        CustomXYToolTipGenerator myToolTip = new CustomXYToolTipGenerator();
        myToolTip.addToolTipSeries(tips);
        renderer.setBaseToolTipGenerator(myToolTip);
        
        overviewMeanWaitingTimeChartPanel.setChart(overviewMeanWaitingTimeChart);
    }
    
    
    public void updateActivityAllActivitiesTable()
    {
        int size = MainFrame.activityCollection.getSize();
        if(size > 1000)
            size = 1000;
        String[][] tableData = new String[size][6];
        for(int i = 0; i < size; i++)
        {
            Activity activity = MainFrame.activityCollection.getActivity(i);
            tableData[i][0] = activity.getActivity();
            tableData[i][1] = activity.getFrequency() + "";
            tableData[i][2] = new DecimalFormat("#0.00").format(activity.getFrequency() * 1.0 / MainFrame.eventCollection.getSize() * 100) + "%";
            if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
    		{
                long median = activity.getMedianDuration();
                long medianMinutes = median / (60 * 1000) % 60;
                long medianHours = median / (60 * 60 * 1000) % 24;
                long medianDays = median / (24 * 60 * 60 * 1000);
                tableData[i][3] = medianDays + " days " + medianHours + " hours " + medianMinutes + " mins";
                long mean = activity.getMeanDuration();
                long meanMinutes = mean / (60 * 1000) % 60;
                long meanHours = mean / (60 * 60 * 1000) % 24;
                long meanDays = mean / (24 * 60 * 60 * 1000);
                tableData[i][4] = meanDays + " days " + meanHours + " hours " + meanMinutes + " mins";
                long max = activity.getDurationRange();
                long maxMinutes = max / (60 * 1000) % 60;
                long maxHours = max / (60 * 60 * 1000) % 24;
                long maxDays = max / (24 * 60 * 60 * 1000);
                tableData[i][5] = maxDays + " days " + maxHours + " hours " + maxMinutes + " mins";
            }
    		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
    		{
                long median = activity.getMedianDuration();
                long medianMinutes = median / (60 * 1000) % 60;
                long medianHours = median / (60 * 60 * 1000) % 24;
                long medianDays = median / (24 * 60 * 60 * 1000);
                tableData[i][3] = medianDays + "天" + medianHours + "小时" + medianMinutes + "分";
                long mean = activity.getMeanDuration();
                long meanMinutes = mean / (60 * 1000) % 60;
                long meanHours = mean / (60 * 60 * 1000) % 24;
                long meanDays = mean / (24 * 60 * 60 * 1000);
                tableData[i][4] = meanDays + "天" + meanHours + "小时" + meanMinutes + "分";
                long max = activity.getDurationRange();
                long maxMinutes = max / (60 * 1000) % 60;
                long maxHours = max / (60 * 60 * 1000) % 24;
                long maxDays = max / (24 * 60 * 60 * 1000);
                tableData[i][5] = maxDays + "天" + maxHours + "小时" + maxMinutes + "分";
            }
        }
        
        if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
            String[] headlines = {"Activity","Frequency","Relative frequency","Median duration","Mean duration","Duration range"};
            activityAllActivitiesTableModel = new DefaultTableModel(tableData, headlines);
        }
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
	        String[] headlines = {"活动","频度","相对频度","中位时间","平均时间","最大时间"};
	        activityAllActivitiesTableModel = new DefaultTableModel(tableData, headlines);
	 	}
        activityAllActivitiesTable.setModel(activityAllActivitiesTableModel);
        
        ComparatorRow c = new ComparatorRow();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<DefaultTableModel>(activityAllActivitiesTableModel);
        sorter.setComparator(0, c);
        sorter.setComparator(1, c);
        sorter.setComparator(2, c);
        sorter.setComparator(3, c);
        sorter.setComparator(4, c);
        activityAllActivitiesTable.setRowSorter(sorter);
    }
    
    
    public void updateActivityFirstInCaseTable()
    {
        int size = MainFrame.activityCollection.getFirst();
        if(size > 1000)
            size = 1000;
        String[][] tableData = new String[size][6];
        int j = 0;
        for(int i = 0; j < size; i++)
        {
            Activity activity = MainFrame.activityCollection.getActivity(i);
            if(activity.getFirst())
            {
                tableData[j][0] = activity.getActivity();
                tableData[j][1] = activity.getFrequency() + "";
                tableData[j][2] = new DecimalFormat("#0.00").format(activity.getFrequency() * 1.0 / MainFrame.eventCollection.getSize() * 100) + "%";
                if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
        		{
                    long median = activity.getMedianDuration();
                    long medianMinutes = median / (60 * 1000) % 60;
                    long medianHours = median / (60 * 60 * 1000) % 24;
                    long medianDays = median / (24 * 60 * 60 * 1000);
                    tableData[j][3] = medianDays + " days " + medianHours + " hours " + medianMinutes + " mins";
                    long mean = activity.getMeanDuration();
                    long meanMinutes = mean / (60 * 1000) % 60;
                    long meanHours = mean / (60 * 60 * 1000) % 24;
                    long meanDays = mean / (24 * 60 * 60 * 1000);
                    tableData[j][4] = meanDays + " days " + meanHours + " hours " + meanMinutes + " mins";
                    long max = activity.getDurationRange();
                    long maxMinutes = max / (60 * 1000) % 60;
                    long maxHours = max / (60 * 60 * 1000) % 24;
                    long maxDays = max / (24 * 60 * 60 * 1000);
                    tableData[j][5] = maxDays + " days " + maxHours + " hours " + maxMinutes + " mins";
                }
        		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
        		{
                    long median = activity.getMedianDuration();
                    long medianMinutes = median / (60 * 1000) % 60;
                    long medianHours = median / (60 * 60 * 1000) % 24;
                    long medianDays = median / (24 * 60 * 60 * 1000);
                    tableData[j][3] = medianDays + "天" + medianHours + "小时" + medianMinutes + "分";
                    long mean = activity.getMeanDuration();
                    long meanMinutes = mean / (60 * 1000) % 60;
                    long meanHours = mean / (60 * 60 * 1000) % 24;
                    long meanDays = mean / (24 * 60 * 60 * 1000);
                    tableData[j][4] = meanDays + "天" + meanHours + "小时" + meanMinutes + "分";
                    long max = activity.getDurationRange();
                    long maxMinutes = max / (60 * 1000) % 60;
                    long maxHours = max / (60 * 60 * 1000) % 24;
                    long maxDays = max / (24 * 60 * 60 * 1000);
                    tableData[j][5] = maxDays + "天" + maxHours + "小时" + maxMinutes + "分";
                }
                j++;
            }
        }
        
        if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
            String[] headlines = {"Activity","Frequency","Relative frequency","Median duration","Mean duration","Duration range"};
            activityFirstInCaseTableModel = new DefaultTableModel(tableData, headlines);
        }
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
	        String[] headlines = {"活动","频度","相对频度","中位时间","平均时间","最大时间"};
	        activityFirstInCaseTableModel = new DefaultTableModel(tableData, headlines);
	 	}
        activityFirstInCaseTable.setModel(activityFirstInCaseTableModel);
        
        ComparatorRow c = new ComparatorRow();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<DefaultTableModel>(activityFirstInCaseTableModel);
        sorter.setComparator(0, c);
        sorter.setComparator(1, c);
        sorter.setComparator(2, c);
        sorter.setComparator(3, c);
        sorter.setComparator(4, c);
        activityFirstInCaseTable.setRowSorter(sorter);
    }
    
    
    public void updateActivityLastInCaseTable()
    {
        int size = MainFrame.activityCollection.getLast();
        if(size > 1000)
            size = 1000;
        String[][] tableData = new String[size][6];
        int j = 0;
        for(int i = 0; j < size; i++)
        {
            Activity activity = MainFrame.activityCollection.getActivity(i);
            if(activity.getLast())
            {
                tableData[j][0] = activity.getActivity();
                tableData[j][1] = activity.getFrequency() + "";
                tableData[j][2] = new DecimalFormat("#0.00").format(activity.getFrequency() * 1.0 / MainFrame.eventCollection.getSize() * 100) + "%";
                if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
        		{
                    long median = activity.getMedianDuration();
                    long medianMinutes = median / (60 * 1000) % 60;
                    long medianHours = median / (60 * 60 * 1000) % 24;
                    long medianDays = median / (24 * 60 * 60 * 1000);
                    tableData[j][3] = medianDays + " days " + medianHours + " hours " + medianMinutes + " mins";
                    long mean = activity.getMeanDuration();
                    long meanMinutes = mean / (60 * 1000) % 60;
                    long meanHours = mean / (60 * 60 * 1000) % 24;
                    long meanDays = mean / (24 * 60 * 60 * 1000);
                    tableData[j][4] = meanDays + " days " + meanHours + " hours " + meanMinutes + " mins";
                    long max = activity.getDurationRange();
                    long maxMinutes = max / (60 * 1000) % 60;
                    long maxHours = max / (60 * 60 * 1000) % 24;
                    long maxDays = max / (24 * 60 * 60 * 1000);
                    tableData[j][5] = maxDays + " days " + maxHours + " hours " + maxMinutes + " mins";
                }
        		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
        		{
                    long median = activity.getMedianDuration();
                    long medianMinutes = median / (60 * 1000) % 60;
                    long medianHours = median / (60 * 60 * 1000) % 24;
                    long medianDays = median / (24 * 60 * 60 * 1000);
                    tableData[j][3] = medianDays + "天" + medianHours + "小时" + medianMinutes + "分";
                    long mean = activity.getMeanDuration();
                    long meanMinutes = mean / (60 * 1000) % 60;
                    long meanHours = mean / (60 * 60 * 1000) % 24;
                    long meanDays = mean / (24 * 60 * 60 * 1000);
                    tableData[j][4] = meanDays + "天" + meanHours + "小时" + meanMinutes + "分";
                    long max = activity.getDurationRange();
                    long maxMinutes = max / (60 * 1000) % 60;
                    long maxHours = max / (60 * 60 * 1000) % 24;
                    long maxDays = max / (24 * 60 * 60 * 1000);
                    tableData[j][5] = maxDays + "天" + maxHours + "小时" + maxMinutes + "分";
                }
                j++;
            }
        }
        
        if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
            String[] headlines = {"Activity","Frequency","Relative frequency","Median duration","Mean duration","Duration range"};
            activityLastInCaseTableModel = new DefaultTableModel(tableData, headlines);
        }
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
	        String[] headlines = {"活动","频度","相对频度","中位时间","平均时间","最大时间"};
	        activityLastInCaseTableModel = new DefaultTableModel(tableData, headlines);
	 	}
        activityLastInCaseTable.setModel(activityLastInCaseTableModel);
        
        ComparatorRow c = new ComparatorRow();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<DefaultTableModel>(activityLastInCaseTableModel);
        sorter.setComparator(0, c);
        sorter.setComparator(1, c);
        sorter.setComparator(2, c);
        sorter.setComparator(3, c);
        sorter.setComparator(4, c);
        activityLastInCaseTable.setRowSorter(sorter);
    }
    
    
    public void updateActivityLabel()
    {
        if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
            activityActivitiesLabel.setText("Activities : " + MainFrame.activityCollection.getSize());
            activityMinimalFrequencyLabel.setText("Minimal frequency : " + MainFrame.activityCollection.getMinimalFrequency());
            activityMedianFrequencyLabel.setText("Median frequency : " + new DecimalFormat("#0.##").format(MainFrame.activityCollection.getMedianFrequency()));
            activityMeanFrequencyLabel.setText("Mean frequency : " + new DecimalFormat("#0.##").format(MainFrame.activityCollection.getMeanFrequency()));
            activityMaximalFrequencyLabel.setText("Maximal frequency : " + MainFrame.activityCollection.getMaximalFrequency());
            activityFrequencyStdDeviationLabel.setText("Frequency std. deviation : " + new DecimalFormat("#0.##").format(MainFrame.activityCollection.getFrequencyStdDeviation()));
        }
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
	        activityActivitiesLabel.setText("活动数：" + MainFrame.activityCollection.getSize());
	        activityMinimalFrequencyLabel.setText("最小频度：" + MainFrame.activityCollection.getMinimalFrequency());
	        activityMedianFrequencyLabel.setText("中位频度：" + new DecimalFormat("#0.##").format(MainFrame.activityCollection.getMedianFrequency()));
	        activityMeanFrequencyLabel.setText("平均频度：" + new DecimalFormat("#0.##").format(MainFrame.activityCollection.getMeanFrequency()));
	        activityMaximalFrequencyLabel.setText("最大频度：" + MainFrame.activityCollection.getMaximalFrequency());
	        activityFrequencyStdDeviationLabel.setText("标准差：" + new DecimalFormat("#0.##").format(MainFrame.activityCollection.getFrequencyStdDeviation()));
        }
    }
    
    
    public void updateActivityFrequencyChart()
    {        
        activityFrequencyDataset = new DefaultCategoryDataset();
        
        for(int i = 0; i < MainFrame.activityCollection.getSize(); i++)
        {
            Activity a = MainFrame.activityCollection.getActivity(i);
            activityFrequencyDataset.addValue(a.getFrequency(), "", a.getActivity());
        }
        
        activityFrequencyChart = ChartFactory.createBarChart(
                null,                     // chart title
                null,                     // domain axis label
                null,                     // range axis label
                activityFrequencyDataset, // data
                PlotOrientation.VERTICAL, // orientation
                false,                    // include legend
                true,                     // tooltips
                false                     // urls
            );
        
        activityFrequencyChart.setBackgroundPaint(null);
        CategoryPlot plot = activityFrequencyChart.getCategoryPlot();
        CategoryAxis domain;
        if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
        	domain = new CategoryAxis("Activities");
        }
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
			domain = new CategoryAxis("活动");
	 	}
		else
		{
			domain = new CategoryAxis("Activities");
		}
        domain.setTickLabelsVisible(false);
        domain.setAxisLineVisible(false);
        domain.setTickMarksVisible(false);
        plot.setDomainAxis(domain);
        NumberAxis range;
        if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
        	range = new NumberAxis("Frequency");
        }
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
			range = new NumberAxis("频度");
	 	}
		else
		{
			range = new NumberAxis("Frequency");
		}
        range.setTickLabelsVisible(false);
        range.setAxisLineVisible(false);
        range.setTickMarksVisible(false);
        plot.setRangeAxis(range);
        
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        DecimalFormat numberFormatter = new DecimalFormat("#0");
        if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
            renderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator("{1}, {2} times", numberFormatter));
        }
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
	        renderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator("{1}，{2}次", numberFormatter));
	 	}
        
        activityFrequencyChartPanel.setChart(activityFrequencyChart);
    }

    
    public void updateActivityMedianDurationChart()
    {        
        activityMedianDurationDataset = new DefaultCategoryDataset();
        
        MainFrame.activityCollection.sortByMedianDuration();
        for(int i = 0; i < MainFrame.activityCollection.getSize(); i++)
        {
            Activity a = MainFrame.activityCollection.getActivity(i);
            activityMedianDurationDataset.addValue(a.getMedianDuration(), "", a.getActivity());
        }
        MainFrame.activityCollection.sortByFrequency();
        
        activityMedianDurationChart = ChartFactory.createBarChart(
                null,                          // chart title
                null,                          // domain axis label
                null,                          // range axis label
                activityMedianDurationDataset, // data
                PlotOrientation.VERTICAL,      // orientation
                false,                         // include legend
                true,                          // tooltips
                false                          // urls
            );
        
        activityMedianDurationChart.setBackgroundPaint(null);
        CategoryPlot plot = activityMedianDurationChart.getCategoryPlot();
        CategoryAxis domain;
        if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
        	domain = new CategoryAxis("Activities");
        }
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
			domain = new CategoryAxis("活动");
	 	}
		else
		{
			domain = new CategoryAxis("Activities");
		}
        domain.setTickLabelsVisible(false);
        domain.setAxisLineVisible(false);
        domain.setTickMarksVisible(false);
        plot.setDomainAxis(domain);
        NumberAxis range;
        if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
        	range = new NumberAxis("Median duration");
        }
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
			range = new NumberAxis("中位时间");
	 	}
		else
		{
			range = new NumberAxis("Median duration");
		}
        range.setTickLabelsVisible(false);
        range.setAxisLineVisible(false);
        range.setTickMarksVisible(false);
        plot.setRangeAxis(range);
        
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setBaseToolTipGenerator(new CustomToolTipGenerator());
        
        activityMedianDurationChartPanel.setChart(activityMedianDurationChart);
    }

    
    public void updateActivityMeanDurationChart()
    {        
        activityMeanDurationDataset = new DefaultCategoryDataset();
        
        MainFrame.activityCollection.sortByMeanDuration();
        for(int i = 0; i < MainFrame.activityCollection.getSize(); i++)
        {
            Activity a = MainFrame.activityCollection.getActivity(i);
            activityMeanDurationDataset.addValue(a.getMeanDuration(), "", a.getActivity());
        }
        MainFrame.activityCollection.sortByFrequency();
        
        activityMeanDurationChart = ChartFactory.createBarChart(
                null,                        // chart title
                null,                        // domain axis label
                null,                        // range axis label
                activityMeanDurationDataset, // data
                PlotOrientation.VERTICAL,    // orientation
                false,                       // include legend
                true,                        // tooltips
                false                        // urls
            );
        
        activityMeanDurationChart.setBackgroundPaint(null);
        CategoryPlot plot = activityMeanDurationChart.getCategoryPlot();
        CategoryAxis domain;
        if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
        	domain = new CategoryAxis("Activities");
        }
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
			domain = new CategoryAxis("活动");
	 	}
		else
		{
			domain = new CategoryAxis("Activities");
		}
        domain.setTickLabelsVisible(false);
        domain.setAxisLineVisible(false);
        domain.setTickMarksVisible(false);
        plot.setDomainAxis(domain);
        NumberAxis range;
        if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
        	range = new NumberAxis("Mean duration");
        }
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
			range = new NumberAxis("平均时间");
	 	}
		else
		{
			range = new NumberAxis("Mean duration");
		}
        range.setTickLabelsVisible(false);
        range.setAxisLineVisible(false);
        range.setTickMarksVisible(false);
        plot.setRangeAxis(range);
        
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setBaseToolTipGenerator(new CustomToolTipGenerator());
        
        activityMeanDurationChartPanel.setChart(activityMeanDurationChart);
    }

    
    public void updateActivityDurationRangeChart()
    {        
        activityDurationRangeDataset = new DefaultCategoryDataset();
        
        MainFrame.activityCollection.sortByDurationRange();
        for(int i = 0; i < MainFrame.activityCollection.getSize(); i++)
        {
            Activity a = MainFrame.activityCollection.getActivity(i);
            activityDurationRangeDataset.addValue(a.getDurationRange(), "", a.getActivity());
        }
        MainFrame.activityCollection.sortByFrequency();
        
        activityDurationRangeChart = ChartFactory.createBarChart(
                null,                         // chart title
                null,                         // domain axis label
                null,                         // range axis label
                activityDurationRangeDataset, // data
                PlotOrientation.VERTICAL,     // orientation
                false,                        // include legend
                true,                         // tooltips
                false                         // urls
            );
        
        activityDurationRangeChart.setBackgroundPaint(null);
        CategoryPlot plot = activityDurationRangeChart.getCategoryPlot();
        CategoryAxis domain;
        if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
        	domain = new CategoryAxis("Activities");
        }
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
			domain = new CategoryAxis("活动");
	 	}
		else
		{
			domain = new CategoryAxis("Activities");
		}
        domain.setTickLabelsVisible(false);
        domain.setAxisLineVisible(false);
        domain.setTickMarksVisible(false);
        plot.setDomainAxis(domain);
        NumberAxis range;
        if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
        	range = new NumberAxis("Duration range");
        }
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
			range = new NumberAxis("最大时间");
	 	}
		else
		{
			range = new NumberAxis("Duration range");
		}
        range.setTickLabelsVisible(false);
        range.setAxisLineVisible(false);
        range.setTickMarksVisible(false);
        plot.setRangeAxis(range);
        
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setBaseToolTipGenerator(new CustomToolTipGenerator());
        
        activityDurationRangeChartPanel.setChart(activityDurationRangeChart);
    }

    
    public void updateActivityAggregateDurationChart()
    {        
        activityAggregateDurationDataset = new DefaultCategoryDataset();
        
        MainFrame.activityCollection.sortByAggregateDuration();
        for(int i = 0; i < MainFrame.activityCollection.getSize(); i++)
        {
            Activity a = MainFrame.activityCollection.getActivity(i);
            activityAggregateDurationDataset.addValue(a.getAggregateDuration(), "", a.getActivity());
        }
        MainFrame.activityCollection.sortByFrequency();
        
        activityAggregateDurationChart = ChartFactory.createBarChart(
                null,                             // chart title
                null,                             // domain axis label
                null,                             // range axis label
                activityAggregateDurationDataset, // data
                PlotOrientation.VERTICAL,         // orientation
                false,                            // include legend
                true,                             // tooltips
                false                             // urls
            );
        
        activityAggregateDurationChart.setBackgroundPaint(null);
        CategoryPlot plot = activityAggregateDurationChart.getCategoryPlot();
        CategoryAxis domain;
        if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
        	domain = new CategoryAxis("Activities");
        }
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
			domain = new CategoryAxis("活动");
	 	}
		else
		{
			domain = new CategoryAxis("Activities");
		}
        domain.setTickLabelsVisible(false);
        domain.setAxisLineVisible(false);
        domain.setTickMarksVisible(false);
        plot.setDomainAxis(domain);
        NumberAxis range;
        if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
        	range = new NumberAxis("Aggregate duration");
        }
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
			range = new NumberAxis("累积时间");
	 	}
		else
		{
			range = new NumberAxis("Aggregate duration");
		}
        range.setTickLabelsVisible(false);
        range.setAxisLineVisible(false);
        range.setTickMarksVisible(false);
        plot.setRangeAxis(range);
        
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setBaseToolTipGenerator(new CustomToolTipGenerator());
        
        activityAggregateDurationChartPanel.setChart(activityAggregateDurationChart);
    }

    
    public void updateResourceAllResourcesTable()
    {
        int size = MainFrame.resourceCollection.getSize();
        if(size > 1000)
            size = 1000;
        String[][] tableData = new String[size][6];
        for(int i = 0; i < size; i++)
        {
            Resource resource = MainFrame.resourceCollection.getResource(i);
            tableData[i][0] = resource.getResource();
            tableData[i][1] = resource.getFrequency() + "";
            tableData[i][2] = new DecimalFormat("#0.00").format(resource.getFrequency() * 1.0 / MainFrame.eventCollection.getSize() * 100) + "%";
            if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
    		{
                long median = resource.getMedianDuration();
                long medianMinutes = median / (60 * 1000) % 60;
                long medianHours = median / (60 * 60 * 1000) % 24;
                long medianDays = median / (24 * 60 * 60 * 1000);
                tableData[i][3] = medianDays + " days " + medianHours + " hours " + medianMinutes + " mins";
                long mean = resource.getMeanDuration();
                long meanMinutes = mean / (60 * 1000) % 60;
                long meanHours = mean / (60 * 60 * 1000) % 24;
                long meanDays = mean / (24 * 60 * 60 * 1000);
                tableData[i][4] = meanDays + " days " + meanHours + " hours " + meanMinutes + " mins";
                long max = resource.getDurationRange();
                long maxMinutes = max / (60 * 1000) % 60;
                long maxHours = max / (60 * 60 * 1000) % 24;
                long maxDays = max / (24 * 60 * 60 * 1000);
                tableData[i][5] = maxDays + " days " + maxHours + " hours " + maxMinutes + " mins";
            }
    		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
    		{
                long median = resource.getMedianDuration();
                long medianMinutes = median / (60 * 1000) % 60;
                long medianHours = median / (60 * 60 * 1000) % 24;
                long medianDays = median / (24 * 60 * 60 * 1000);
                tableData[i][3] = medianDays + "天" + medianHours + "小时" + medianMinutes + "分";
                long mean = resource.getMeanDuration();
                long meanMinutes = mean / (60 * 1000) % 60;
                long meanHours = mean / (60 * 60 * 1000) % 24;
                long meanDays = mean / (24 * 60 * 60 * 1000);
                tableData[i][4] = meanDays + "天" + meanHours + "小时" + meanMinutes + "分";
                long max = resource.getDurationRange();
                long maxMinutes = max / (60 * 1000) % 60;
                long maxHours = max / (60 * 60 * 1000) % 24;
                long maxDays = max / (24 * 60 * 60 * 1000);
                tableData[i][5] = maxDays + "天" + maxHours + "小时" + maxMinutes + "分";
            }
        }
        
        if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
            String[] headlines = {"Resource","Frequency","Relative frequency","Median duration","Mean duration","Duration range"};
            resourceAllResourcesTableModel = new DefaultTableModel(tableData, headlines);
        }
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
	        String[] headlines = {"资源","频度","相对频度","中位时间","平均时间","最大时间"};
	        resourceAllResourcesTableModel = new DefaultTableModel(tableData, headlines);
	 	}
        resourceAllResourcesTable.setModel(resourceAllResourcesTableModel);
        
        ComparatorRow c = new ComparatorRow();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<DefaultTableModel>(resourceAllResourcesTableModel);
        sorter.setComparator(0, c);
        sorter.setComparator(1, c);
        sorter.setComparator(2, c);
        sorter.setComparator(3, c);
        sorter.setComparator(4, c);
        resourceAllResourcesTable.setRowSorter(sorter);
    }
    
    
    public void updateResourceFirstInCaseTable()
    {
        int size = MainFrame.resourceCollection.getFirst();
        if(size > 1000)
            size = 1000;
        String[][] tableData = new String[size][6];
        int j = 0;
        for(int i = 0; j < size; i++)
        {
            Resource resource = MainFrame.resourceCollection.getResource(i);
            if(resource.getFirst())
            {
                tableData[j][0] = resource.getResource();
                tableData[j][1] = resource.getFrequency() + "";
                tableData[j][2] = new DecimalFormat("#0.00").format(resource.getFrequency() * 1.0 / MainFrame.eventCollection.getSize() * 100) + "%";
                if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
        		{
                    long median = resource.getMedianDuration();
                    long medianMinutes = median / (60 * 1000) % 60;
                    long medianHours = median / (60 * 60 * 1000) % 24;
                    long medianDays = median / (24 * 60 * 60 * 1000);
                    tableData[j][3] = medianDays + " days " + medianHours + " hours " + medianMinutes + " mins";
                    long mean = resource.getMeanDuration();
                    long meanMinutes = mean / (60 * 1000) % 60;
                    long meanHours = mean / (60 * 60 * 1000) % 24;
                    long meanDays = mean / (24 * 60 * 60 * 1000);
                    tableData[j][4] = meanDays + " days " + meanHours + " hours " + meanMinutes + " mins";
                    long max = resource.getDurationRange();
                    long maxMinutes = max / (60 * 1000) % 60;
                    long maxHours = max / (60 * 60 * 1000) % 24;
                    long maxDays = max / (24 * 60 * 60 * 1000);
                    tableData[j][5] = maxDays + " days " + maxHours + " hours " + maxMinutes + " mins";
                }
        		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
        		{
                    long median = resource.getMedianDuration();
                    long medianMinutes = median / (60 * 1000) % 60;
                    long medianHours = median / (60 * 60 * 1000) % 24;
                    long medianDays = median / (24 * 60 * 60 * 1000);
                    tableData[j][3] = medianDays + "天" + medianHours + "小时" + medianMinutes + "分";
                    long mean = resource.getMeanDuration();
                    long meanMinutes = mean / (60 * 1000) % 60;
                    long meanHours = mean / (60 * 60 * 1000) % 24;
                    long meanDays = mean / (24 * 60 * 60 * 1000);
                    tableData[j][4] = meanDays + "天" + meanHours + "小时" + meanMinutes + "分";
                    long max = resource.getDurationRange();
                    long maxMinutes = max / (60 * 1000) % 60;
                    long maxHours = max / (60 * 60 * 1000) % 24;
                    long maxDays = max / (24 * 60 * 60 * 1000);
                    tableData[j][5] = maxDays + "天" + maxHours + "小时" + maxMinutes + "分";
                }
                j++;
            }
        }
        
        if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
            String[] headlines = {"Resource","Frequency","Relative frequency","Median duration","Mean duration","Duration range"};
            resourceFirstInCaseTableModel = new DefaultTableModel(tableData, headlines);
        }
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
	        String[] headlines = {"资源","频度","相对频度","中位时间","平均时间","最大时间"};
	        resourceFirstInCaseTableModel = new DefaultTableModel(tableData, headlines);
	 	}
        resourceFirstInCaseTable.setModel(resourceFirstInCaseTableModel);
        
        ComparatorRow c = new ComparatorRow();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<DefaultTableModel>(resourceFirstInCaseTableModel);
        sorter.setComparator(0, c);
        sorter.setComparator(1, c);
        sorter.setComparator(2, c);
        sorter.setComparator(3, c);
        sorter.setComparator(4, c);
        resourceFirstInCaseTable.setRowSorter(sorter);
    }

    
    public void updateResourceLastInCaseTable()
    {
        int size = MainFrame.resourceCollection.getLast();
        if(size > 1000)
            size = 1000;
        String[][] tableData = new String[size][6];
        int j = 0;
        for(int i = 0; j < size; i++)
        {
            Resource resource = MainFrame.resourceCollection.getResource(i);
            if(resource.getLast())
            {
                tableData[j][0] = resource.getResource();
                tableData[j][1] = resource.getFrequency() + "";
                tableData[j][2] = new DecimalFormat("#0.00").format(resource.getFrequency() * 1.0 / MainFrame.eventCollection.getSize() * 100) + "%";
                if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
        		{
                    long median = resource.getMedianDuration();
                    long medianMinutes = median / (60 * 1000) % 60;
                    long medianHours = median / (60 * 60 * 1000) % 24;
                    long medianDays = median / (24 * 60 * 60 * 1000);
                    tableData[j][3] = medianDays + " days " + medianHours + " hours " + medianMinutes + " mins";
                    long mean = resource.getMeanDuration();
                    long meanMinutes = mean / (60 * 1000) % 60;
                    long meanHours = mean / (60 * 60 * 1000) % 24;
                    long meanDays = mean / (24 * 60 * 60 * 1000);
                    tableData[j][4] = meanDays + " days " + meanHours + " hours " + meanMinutes + " mins";
                    long max = resource.getDurationRange();
                    long maxMinutes = max / (60 * 1000) % 60;
                    long maxHours = max / (60 * 60 * 1000) % 24;
                    long maxDays = max / (24 * 60 * 60 * 1000);
                    tableData[j][5] = maxDays + " days " + maxHours + " hours " + maxMinutes + " mins";
                }
        		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
        		{
                    long median = resource.getMedianDuration();
                    long medianMinutes = median / (60 * 1000) % 60;
                    long medianHours = median / (60 * 60 * 1000) % 24;
                    long medianDays = median / (24 * 60 * 60 * 1000);
                    tableData[j][3] = medianDays + "天" + medianHours + "小时" + medianMinutes + "分";
                    long mean = resource.getMeanDuration();
                    long meanMinutes = mean / (60 * 1000) % 60;
                    long meanHours = mean / (60 * 60 * 1000) % 24;
                    long meanDays = mean / (24 * 60 * 60 * 1000);
                    tableData[j][4] = meanDays + "天" + meanHours + "小时" + meanMinutes + "分";
                    long max = resource.getDurationRange();
                    long maxMinutes = max / (60 * 1000) % 60;
                    long maxHours = max / (60 * 60 * 1000) % 24;
                    long maxDays = max / (24 * 60 * 60 * 1000);
                    tableData[j][5] = maxDays + "天" + maxHours + "小时" + maxMinutes + "分";
                }
                j++;
            }
        }
        
        if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
            String[] headlines = {"Resource","Frequency","Relative frequency","Median duration","Mean duration","Duration range"};
            resourceLastInCaseTableModel = new DefaultTableModel(tableData, headlines);
        }
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
	        String[] headlines = {"资源","频度","相对频度","中位时间","平均时间","最大时间"};
	        resourceLastInCaseTableModel = new DefaultTableModel(tableData, headlines);
	 	}
        resourceLastInCaseTable.setModel(resourceLastInCaseTableModel);
        
        ComparatorRow c = new ComparatorRow();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<DefaultTableModel>(resourceLastInCaseTableModel);
        sorter.setComparator(0, c);
        sorter.setComparator(1, c);
        sorter.setComparator(2, c);
        sorter.setComparator(3, c);
        sorter.setComparator(4, c);
        resourceLastInCaseTable.setRowSorter(sorter);
    }
    
    
    public void updateResourceLabel()
    {
        if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
            resourceResourcesLabel.setText("Resources : " + MainFrame.resourceCollection.getSize());
            resourceMinimalFrequencyLabel.setText("Minimal frequency : " + MainFrame.resourceCollection.getMinimalFrequency());
            resourceMedianFrequencyLabel.setText("Median frequency : " + new DecimalFormat("#0.##").format(MainFrame.resourceCollection.getMedianFrequency()));
            resourceMeanFrequencyLabel.setText("Mean frequency : " + new DecimalFormat("#0.##").format(MainFrame.resourceCollection.getMeanFrequency()));
            resourceMaximalFrequencyLabel.setText("Maximal frequency : " + MainFrame.resourceCollection.getMaximalFrequency());
            resourceFrequencyStdDeviationLabel.setText("Frequency std. deviation : " + new DecimalFormat("#0.##").format(MainFrame.resourceCollection.getFrequencyStdDeviation()));
        }
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
	        resourceResourcesLabel.setText("资源数：" + MainFrame.resourceCollection.getSize());
	        resourceMinimalFrequencyLabel.setText("最小频度：" + MainFrame.resourceCollection.getMinimalFrequency());
	        resourceMedianFrequencyLabel.setText("中位频度：" + new DecimalFormat("#0.##").format(MainFrame.resourceCollection.getMedianFrequency()));
	        resourceMeanFrequencyLabel.setText("平均频度：" + new DecimalFormat("#0.##").format(MainFrame.resourceCollection.getMeanFrequency()));
	        resourceMaximalFrequencyLabel.setText("最大频度：" + MainFrame.resourceCollection.getMaximalFrequency());
	        resourceFrequencyStdDeviationLabel.setText("标准差：" + new DecimalFormat("#0.##").format(MainFrame.resourceCollection.getFrequencyStdDeviation()));
	 	}
    }
    
    
    public void updateResourceFrequencyChart()
    {        
        resourceFrequencyDataset = new DefaultCategoryDataset();
        
        for(int i = 0; i < MainFrame.resourceCollection.getSize(); i++)
        {
            Resource a = MainFrame.resourceCollection.getResource(i);
            resourceFrequencyDataset.addValue(a.getFrequency(), "", a.getResource());
        }
        
        resourceFrequencyChart = ChartFactory.createBarChart(
                null,                     // chart title
                null,                     // domain axis label
                null,                     // range axis label
                resourceFrequencyDataset, // data
                PlotOrientation.VERTICAL, // orientation
                false,                    // include legend
                true,                     // tooltips
                false                     // urls
            );
        
        resourceFrequencyChart.setBackgroundPaint(null);
        CategoryPlot plot = resourceFrequencyChart.getCategoryPlot();
        CategoryAxis domain;
        if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
        	domain = new CategoryAxis("Resources");
        }
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
			domain = new CategoryAxis("资源");
	 	}
		else
		{
			domain = new CategoryAxis("Resources");
		}
        domain.setTickLabelsVisible(false);
        domain.setAxisLineVisible(false);
        domain.setTickMarksVisible(false);
        plot.setDomainAxis(domain);
        NumberAxis range;
        if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
        	range = new NumberAxis("Frequency");
        }
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
			range = new NumberAxis("频度");
	 	}
		else
		{
			range = new NumberAxis("Frequency");
		}
        range.setTickLabelsVisible(false);
        range.setAxisLineVisible(false);
        range.setTickMarksVisible(false);
        plot.setRangeAxis(range);
        
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        DecimalFormat numberFormatter = new DecimalFormat("#0");
        if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
            renderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator("{1}, {2} times", numberFormatter));
        }
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
	        renderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator("{1}，{2}次", numberFormatter));
	 	}        
        
        resourceFrequencyChartPanel.setChart(resourceFrequencyChart);
    }

    
    public void updateResourceMedianDurationChart()
    {        
        resourceMedianDurationDataset = new DefaultCategoryDataset();
        
        MainFrame.resourceCollection.sortByMedianDuration();
        for(int i = 0; i < MainFrame.resourceCollection.getSize(); i++)
        {
            Resource a = MainFrame.resourceCollection.getResource(i);
            resourceMedianDurationDataset.addValue(a.getMedianDuration(), "", a.getResource());
        }
        MainFrame.resourceCollection.sortByFrequency();
        
        resourceMedianDurationChart = ChartFactory.createBarChart(
                null,                          // chart title
                null,                          // domain axis label
                null,                          // range axis label
                resourceMedianDurationDataset, // data
                PlotOrientation.VERTICAL,      // orientation
                false,                         // include legend
                true,                          // tooltips
                false                          // urls
            );
        
        resourceMedianDurationChart.setBackgroundPaint(null);
        CategoryPlot plot = resourceMedianDurationChart.getCategoryPlot();
        CategoryAxis domain;
        if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
        	domain = new CategoryAxis("Resources");
        }
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
			domain = new CategoryAxis("资源");
	 	}
		else
		{
			domain = new CategoryAxis("Resources");
		}
        domain.setTickLabelsVisible(false);
        domain.setAxisLineVisible(false);
        domain.setTickMarksVisible(false);
        plot.setDomainAxis(domain);
        NumberAxis range;
        if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
        	range = new NumberAxis("Median duration");
        }
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
			range = new NumberAxis("中位时间");
	 	}
		else
		{
			range = new NumberAxis("Median duration");
		}
        range.setTickLabelsVisible(false);
        range.setAxisLineVisible(false);
        range.setTickMarksVisible(false);
        plot.setRangeAxis(range);
        
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setBaseToolTipGenerator(new CustomToolTipGenerator());
        
        resourceMedianDurationChartPanel.setChart(resourceMedianDurationChart);
    }
    
    
    public void updateResourceMeanDurationChart()
    {        
        resourceMeanDurationDataset = new DefaultCategoryDataset();
        
        MainFrame.resourceCollection.sortByMeanDuration();
        for(int i = 0; i < MainFrame.resourceCollection.getSize(); i++)
        {
            Resource a = MainFrame.resourceCollection.getResource(i);
            resourceMeanDurationDataset.addValue(a.getMeanDuration(), "", a.getResource());
        }
        MainFrame.resourceCollection.sortByFrequency();
        
        resourceMeanDurationChart = ChartFactory.createBarChart(
                null,                        // chart title
                null,                        // domain axis label
                null,                        // range axis label
                resourceMeanDurationDataset, // data
                PlotOrientation.VERTICAL,    // orientation
                false,                       // include legend
                true,                        // tooltips
                false                        // urls
            );
        
        resourceMeanDurationChart.setBackgroundPaint(null);
        CategoryPlot plot = resourceMeanDurationChart.getCategoryPlot();
        CategoryAxis domain;
        if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
        	domain = new CategoryAxis("Resources");
        }
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
			domain = new CategoryAxis("资源");
	 	}
		else
		{
			domain = new CategoryAxis("Resources");
		}
        domain.setTickLabelsVisible(false);
        domain.setAxisLineVisible(false);
        domain.setTickMarksVisible(false);
        plot.setDomainAxis(domain);
        NumberAxis range;
        if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
        	range = new NumberAxis("Mean duration");
        }
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
			range = new NumberAxis("平均时间");
	 	}
		else
		{
			range = new NumberAxis("Mean duration");
		}
        range.setTickLabelsVisible(false);
        range.setAxisLineVisible(false);
        range.setTickMarksVisible(false);
        plot.setRangeAxis(range);
        
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setBaseToolTipGenerator(new CustomToolTipGenerator());
        
        resourceMeanDurationChartPanel.setChart(resourceMeanDurationChart);
    }

    
    public void updateResourceDurationRangeChart()
    {        
        resourceDurationRangeDataset = new DefaultCategoryDataset();
        
        MainFrame.resourceCollection.sortByDurationRange();
        for(int i = 0; i < MainFrame.resourceCollection.getSize(); i++)
        {
            Resource a = MainFrame.resourceCollection.getResource(i);
            resourceDurationRangeDataset.addValue(a.getDurationRange(), "", a.getResource());
        }
        MainFrame.resourceCollection.sortByFrequency();
        
        resourceDurationRangeChart = ChartFactory.createBarChart(
                null,                         // chart title
                null,                         // domain axis label
                null,                         // range axis label
                resourceDurationRangeDataset, // data
                PlotOrientation.VERTICAL,     // orientation
                false,                        // include legend
                true,                         // tooltips
                false                         // urls
            );
        
        resourceDurationRangeChart.setBackgroundPaint(null);
        CategoryPlot plot = resourceDurationRangeChart.getCategoryPlot();
        CategoryAxis domain;
        if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
        	domain = new CategoryAxis("Resources");
        }
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
			domain = new CategoryAxis("资源");
	 	}
		else
		{
			domain = new CategoryAxis("Resources");
		}
        domain.setTickLabelsVisible(false);
        domain.setAxisLineVisible(false);
        domain.setTickMarksVisible(false);
        plot.setDomainAxis(domain);
        NumberAxis range;
        if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
        	range = new NumberAxis("Duration range");
        }
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
			range = new NumberAxis("最大时间");
	 	}
		else
		{
			range = new NumberAxis("Duration range");
		}
        range.setTickLabelsVisible(false);
        range.setAxisLineVisible(false);
        range.setTickMarksVisible(false);
        plot.setRangeAxis(range);
        
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setBaseToolTipGenerator(new CustomToolTipGenerator());
        
        resourceDurationRangeChartPanel.setChart(resourceDurationRangeChart);
    }

    
    public void updateResourceAggregateDurationChart()
    {        
        resourceAggregateDurationDataset = new DefaultCategoryDataset();
        
        MainFrame.resourceCollection.sortByAggregateDuration();
        for(int i = 0; i < MainFrame.resourceCollection.getSize(); i++)
        {
            Resource a = MainFrame.resourceCollection.getResource(i);
            resourceAggregateDurationDataset.addValue(a.getAggregateDuration(), "", a.getResource());
        }
        MainFrame.resourceCollection.sortByFrequency();
        
        resourceAggregateDurationChart = ChartFactory.createBarChart(
                null,                             // chart title
                null,                             // domain axis label
                null,                             // range axis label
                resourceAggregateDurationDataset, // data
                PlotOrientation.VERTICAL,         // orientation
                false,                            // include legend
                true,                             // tooltips
                false                             // urls
            );
        
        resourceAggregateDurationChart.setBackgroundPaint(null);
        CategoryPlot plot = resourceAggregateDurationChart.getCategoryPlot();
        CategoryAxis domain;
        if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
        	domain = new CategoryAxis("Resources");
        }
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
			domain = new CategoryAxis("资源");
	 	}
		else
		{
			domain = new CategoryAxis("Resources");
		}
        domain.setTickLabelsVisible(false);
        domain.setAxisLineVisible(false);
        domain.setTickMarksVisible(false);
        plot.setDomainAxis(domain);
        NumberAxis range;
        if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
        	range = new NumberAxis("Aggregate duration");
        }
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
			range = new NumberAxis("累积时间");
	 	}
		else
		{
			range = new NumberAxis("Aggregate duration");
		}
        range.setTickLabelsVisible(false);
        range.setAxisLineVisible(false);
        range.setTickMarksVisible(false);
        plot.setRangeAxis(range);
        
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setBaseToolTipGenerator(new CustomToolTipGenerator());
        
        resourceAggregateDurationChartPanel.setChart(resourceAggregateDurationChart);
    }
    
    
    @Override
    public void componentMoved(ComponentEvent e) {
        // TODO Auto-generated method stub
    }

    
    @Override
    public void componentShown(ComponentEvent e) {
        // TODO Auto-generated method stub
    }

    
    @Override
    public void componentHidden(ComponentEvent e) {
        // TODO Auto-generated method stub
    }

    
    @Override
    public void componentResized(ComponentEvent e) {
        // TODO Auto-generated method stub
    }

    
}
