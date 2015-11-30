package wzc.zcminer.frame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

//切换界面
public class ButtonPanel extends JPanel{
	static FileButton fileButton;
	static DatabaseButton databaseButton;
	static JButton mapButton;
	static JButton staticsButton;
	static JButton casesButton;

	public ButtonPanel() {
		super();
		
		fileButton = new FileButton();
		databaseButton = new DatabaseButton();

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
		add(databaseButton);
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
