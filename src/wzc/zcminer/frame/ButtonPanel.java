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

//切换界面
public class ButtonPanel extends JPanel{
	static FileButton fileButton;
	static JButton mapButton;
	static JButton staticsButton;
	static JButton casesButton;

	public ButtonPanel() {
		super();
		
		fileButton = new FileButton();

		if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
	        mapButton = new JButton("Map");
		}
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
	        mapButton = new JButton("示意图");
		}
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

		add(fileButton);
        add(mapButton);
        add(staticsButton);
        add(casesButton);
	}
	
	public void setMap() {
		mapButton.setEnabled(false);
		staticsButton.setEnabled(true);
		casesButton.setEnabled(true);
	}
	
	public void setStatics() {
		mapButton.setEnabled(true);
		staticsButton.setEnabled(false);
		casesButton.setEnabled(true);
	}
	
	public void setCases() {
		mapButton.setEnabled(true);
		staticsButton.setEnabled(true);
		casesButton.setEnabled(false);
	}
	
}
