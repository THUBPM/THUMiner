package wzc.zcminer.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Arrays;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import wzc.zcminer.global.BigAnimationDerby.Frame;
import wzc.zcminer.global.Event;

import com.mxgraph.layout.mxParallelEdgeLayout;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxGraph;

//map面板
public class BigMapPanelDerby extends JPanel implements ComponentListener {
	//参数滑块
	static JSlider pathSlider;   
	static JSlider activitySlider;
	//动画滑块
	static JSlider speedSlider;
	static JSlider animationSlider;
	//类型选择
	static JComboBox<String> modelType;
	static JPanel countSliderPanel;
	static JPanel countLabelPanel;
	static JPanel countPanel;
	static JPanel animationPanel;
	static JPanel speedSliderPanel;
	static JPanel animationSliderPanel;
	static JPanel animationButtonPanel;
	static JPanel controlPanel;
	static JButton animationButton;
	static JButton pauseButton;
	//切换页面
	static ButtonPanel buttonPanel;
	
	//jgraph面板
	mxGraphComponent graphComponent;
	mxGraph graph;
	Object parent;
	Timer timer;
	TimerTask timerTask;
	int animationFlag;
	int pauseFlag;
	long speed;
	long currentTime;
	Frame currentFrame;
	//图像节点
	Object[] v;
	
	public BigMapPanelDerby() {
		// TODO Auto-generated constructor stub

		setLayout(new BorderLayout());

		controlPanel = new JPanel();
		controlPanel.setLayout(new BorderLayout());
		
		countPanel = new JPanel();
		countPanel.setLayout(new BorderLayout());
		
		animationPanel = new JPanel(new BorderLayout());
		
		countSliderPanel = new JPanel();
		countSliderPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		countLabelPanel = new JPanel();
		countLabelPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
			countLabelPanel.add(new JLabel("Path"));
			countLabelPanel.add(new JLabel("Activity"));
		}
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
			countLabelPanel.add(new JLabel("路径数"));
			countLabelPanel.add(new JLabel("活动数"));
	    }
		
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
		countSliderPanel.add(pathSlider);
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
		countSliderPanel.add(activitySlider);

		countPanel.add(countLabelPanel, BorderLayout.NORTH);
		countPanel.add(countSliderPanel, BorderLayout.CENTER);
		
		//动画设置
		speed = 1;
		speedSlider = new JSlider();
		speedSlider.setMinimum(1);
		long period =MainFrame.graphNet.endTime- MainFrame.graphNet.beginTime;
		speedSlider.setMaximum((int) (period / 50));
		speedSlider.setValue(1);
		speedSlider.setExtent(1);
		speedSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (speedSlider.getValueIsAdjusting() != true) {
					speed = speedSlider.getValue();
				}
			}
		});
		
		animationFlag = 0;
		pauseFlag = 0;
		if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
			animationButton = new JButton("Animation");
		}
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
			animationButton = new JButton("播放动画");
	    }
		if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
			pauseButton = new JButton("Pause");
		}
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
			pauseButton = new JButton("暂停播放");
	    }
		
		animationSlider = new JSlider();
		animationSlider.setMinimum(0);
		animationSlider.setMaximum(100);
		animationSlider.setValue(0);
		animationSlider.setExtent(1);
		animationSlider.setPreferredSize(new Dimension(MainFrame.mainFrame.getWidth() - 100, 40));
		animationSlider.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				if(animationFlag == 1)
				{
					if(pauseFlag == 0)
						timer.cancel();
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if(animationFlag == 1)
				{
					currentTime = MainFrame.graphNet.beginTime + (int) ((MainFrame.graphNet.endTime - MainFrame.graphNet.beginTime) / 100 * animationSlider.getValue());
					currentFrame = MainFrame.bigAnimationDerby.getAnimationFrame(animationSlider.getValue());
					if(pauseFlag == 0)
						startAnimation();
					else
						paintAnimation();
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		animationSlider.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseDragged(MouseEvent e) {
				currentTime = MainFrame.graphNet.beginTime + (int) ((MainFrame.graphNet.endTime - MainFrame.graphNet.beginTime) / 100 * animationSlider.getValue());
				currentFrame = MainFrame.bigAnimationDerby.getAnimationFrame(animationSlider.getValue());
				paintAnimation();
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		pauseButton.setEnabled(false);
		speedSlider.setEnabled(false);
		animationSlider.setEnabled(false);
		
		animationButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (animationFlag == 0){
					animationFlag = 1;
					pauseFlag = 0;
					currentTime = MainFrame.graphNet.beginTime;
					currentFrame = MainFrame.bigAnimationDerby.getAnimationFrame(0);
					if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
					{
						animationButton.setText("Stop");
					}
					else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
					{
						animationButton.setText("结束播放");
				    }
					modelType.setEnabled(false);
					speedSlider.setEnabled(true);
					animationSlider.setEnabled(true);
					animationSlider.setValue(0);
					pathSlider.setEnabled(false);
					activitySlider.setEnabled(false);
					if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
					{
						pauseButton.setText("Pause");
					}
					else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
					{
						pauseButton.setText("暂停播放");
				    }
					pauseButton.setEnabled(true);
					startAnimation();
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
					speedSlider.setEnabled(false);
					animationSlider.setEnabled(false);
					animationSlider.setValue(0);
					pathSlider.setEnabled(true);
					activitySlider.setEnabled(true);
					if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
					{
						pauseButton.setText("Pause");
					}
					else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
					{
						pauseButton.setText("暂停播放");
				    }
					pauseButton.setEnabled(false);
					animationFlag = 0;
					pauseFlag = 0;
					currentTime = MainFrame.graphNet.beginTime;
					currentFrame = MainFrame.bigAnimationDerby.getAnimationFrame(0);
					timer.cancel();
					paintGraph();
					
				}
			}
		});
		pauseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (pauseFlag == 0){
					pauseFlag = 1;
					if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
					{
						pauseButton.setText("Continue");
					}
					else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
					{
						pauseButton.setText("继续播放");
				    }
					timer.cancel();
				} else{
					pauseFlag = 0;
					if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
					{
						pauseButton.setText("Pause");
					}
					else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
					{
						pauseButton.setText("暂停播放");
					}
					startAnimation();
				}
			}
		});
		//面板布局
		animationButtonPanel = new JPanel(new FlowLayout());
		speedSliderPanel = new JPanel(new FlowLayout());
		
		animationButtonPanel.add(animationButton);
		animationButtonPanel.add(pauseButton);
		if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
			speedSliderPanel.add(new JLabel("Speed"));
		}
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
			speedSliderPanel.add(new JLabel("播放速度"));
	    }
		speedSliderPanel.add(speedSlider);
		animationPanel.add(animationButtonPanel,BorderLayout.NORTH);
		animationPanel.add(speedSliderPanel, BorderLayout.CENTER);
		
		
		controlPanel.add(countPanel, BorderLayout.NORTH);
		controlPanel.add(animationPanel, BorderLayout.CENTER);
		controlPanel.add(modelType, BorderLayout.SOUTH);
		
		graph = new mxGraph();
		parent = graph.getDefaultParent();
		graphComponent = new mxGraphComponent(graph);
        
		animationSliderPanel = new JPanel(new FlowLayout());
		if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
		{
			animationSliderPanel.add(new JLabel("SeekBar"));
		}
		else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
		{
			animationSliderPanel.add(new JLabel("播放进度"));
	    }
		animationSliderPanel.add(animationSlider);
		
		buttonPanel = new ButtonPanel();
		buttonPanel.setMap();
        
        add(buttonPanel, BorderLayout.NORTH);
        add(graphComponent, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.EAST);
        add(animationSliderPanel, BorderLayout.SOUTH);

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

	
    public void setTimerTask(){
		timerTask = new TimerTask() {  
            public void run() {
            	currentTime=currentTime + speed;
            	if (currentTime >= MainFrame.graphNet.endTime){
					if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
					{
						animationButton.setText("Animation");
					}
					else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
					{
						animationButton.setText("播放动画");
				    }
					modelType.setEnabled(true);
					speedSlider.setEnabled(false);
					animationSlider.setEnabled(false);
					animationSlider.setValue(0);
					pathSlider.setEnabled(true);
					activitySlider.setEnabled(true);
					if(MainFrame.properties.getProperty("language", "zhCN").equals("enUS"))
					{
						pauseButton.setText("Pause");
					}
					else if(MainFrame.properties.getProperty("language", "zhCN").equals("zhCN"))
					{
						pauseButton.setText("暂停播放");
				    }
					pauseButton.setEnabled(false);
					animationFlag = 0;
					currentTime = MainFrame.graphNet.beginTime;
					currentFrame = MainFrame.bigAnimationDerby.getAnimationFrame(0);
					timer.cancel();
					paintGraph();
					return;
            	}else{
            		Frame nextFrame = MainFrame.bigAnimationDerby.getNextFrame(currentFrame);
            		while (nextFrame.getFrame() < currentTime * (1000*60*60)){
            			currentFrame = nextFrame;
            			nextFrame = MainFrame.bigAnimationDerby.getNextFrame(currentFrame);
            		}
            	}
            	animationSlider.setValue((int) ((currentTime - MainFrame.graphNet.beginTime) * 100 / (MainFrame.graphNet.endTime - MainFrame.graphNet.beginTime)));
            	paintAnimation();
            }
		};
    }
    
    public void paintAnimation(){
    	int[] activityEvent = new int[MainFrame.graphNet.activityCount];
    	int[][] activityEventEdge = new int[MainFrame.graphNet.activityCount][MainFrame.graphNet.activityCount];
    	
    	for(int i = 0; i < MainFrame.graphNet.activityCount; i++){
    		activityEvent[i] = currentFrame.getActivityFre(i);
        }
    	for(int i = 0; i < MainFrame.graphNet.activityCount; i++)
        	for(int j = 0; j < MainFrame.graphNet.activityCount; j++){
        		activityEventEdge[i][j] = currentFrame.getActivityQueFre(i, j);
        	}
    	
    	int[] temp = MainFrame.graphNet.activityFre.clone();
		Arrays.sort(temp);
    	
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
    	if (i < 2 || (MainFrame.graphNet.activityFre[i] >= temp[activitySlider
			               								.getValue()])){
    		for (int j = 0; j< MainFrame.graphNet.activityCount; j++){
    			if (j < 2 || (MainFrame.graphNet.activityFre[j] >= temp[activitySlider
    			               								.getValue()]
    			               								&& MainFrame.graphNet.activityQueFre[i][j] >= MainFrame.graphNet.activityQueFreSort.
    			               								get(pathSlider.getValue()) && MainFrame.graphNet.activityQueFre[i][j] >= 0)){ 
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
    
	//动画模拟函数
	public void startAnimation(){
		timer = new Timer();
		setTimerTask();
        timer.scheduleAtFixedRate(timerTask, 0, 500);
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
		animationSlider.setPreferredSize(new Dimension(MainFrame.mainFrame.getWidth() - 100, 40));
	}

	
	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	
	@Override
	public void componentResized(ComponentEvent e) {
		// TODO Auto-generated method stub
		ZoomtoFit();
		animationSlider.setPreferredSize(new Dimension(400, 40));
		animationSlider.setPreferredSize(new Dimension(MainFrame.mainFrame.getWidth() - 100, 40));
	}

	
}
