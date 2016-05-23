package wzc.zcminer.frame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

//切换界面
public class ButtonPanel extends JPanel{
	static FileButton fileButton;
	static DatabaseButton databaseButton;
	static BigFileButtonDerby bigFileButton;
	static JButton mapButton;
	static JButton staticsButton;
	static JButton casesButton;

	public ButtonPanel() {
		super();
		
		fileButton = new FileButton();
		databaseButton = new DatabaseButton();
		bigFileButton = new BigFileButtonDerby();

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
                if(MainFrame.dataSource == 0 || MainFrame.dataSource == 1){
                	MapPanel mapPanel = new MapPanel();
                	MainFrame.mainFrame.setContentPane(mapPanel);
                }else if(MainFrame.dataSource == 2){
                	BigMapPanelDerby mapPanel = new BigMapPanelDerby();
                	MainFrame.mainFrame.setContentPane(mapPanel);
                }
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
                if(MainFrame.dataSource == 0 || MainFrame.dataSource == 1){
                    StaticsPanel staticsPanel = new StaticsPanel();
                    MainFrame.mainFrame.setContentPane(staticsPanel);
                }else if(MainFrame.dataSource == 2){
                    BigStaticsPanelDerby staticsPanel = new BigStaticsPanelDerby();
                    MainFrame.mainFrame.setContentPane(staticsPanel);
                }
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
                if(MainFrame.dataSource == 0 || MainFrame.dataSource == 1){
                    CasesPanel casesPanel = new CasesPanel();
                    MainFrame.mainFrame.setContentPane(casesPanel);
                }else if(MainFrame.dataSource == 2){
                    BigCasesPanelDerby casesPanel = new BigCasesPanelDerby();
                    MainFrame.mainFrame.setContentPane(casesPanel);
                }
                MainFrame.mainFrame.setVisible(true);
                System.gc();
            }
        });

		add(fileButton);
		add(databaseButton);
		add(bigFileButton);
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
