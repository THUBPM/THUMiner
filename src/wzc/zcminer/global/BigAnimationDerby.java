package wzc.zcminer.global;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import wzc.zcminer.frame.MainFrame;

public class BigAnimationDerby {
	HashMap<String, Integer> activityIDMap;
	public String[] activityNames;
	public int activityCount;   //活动数
	public long beginTime;   //整个图的开始时间
	public long endTime; 	//整个图的结束时间
	public int size;
	public long[] animationFrame;

	public BigAnimationDerby() {
		// TODO Auto-generated constructor stub
		activityIDMap = new HashMap<String, Integer>();
		activityCount = 2;
		beginTime = 0;
		endTime = 0;
	    size = 0;
	    animationFrame = new long[101];
	}
	
	public int getSize(){
		return size;
	}
	
	//hash判断活动数
	public boolean activityExist(String activityName) {
		return activityIDMap.containsKey(activityName);
	}
	
	//各类活动操作
	public void setActivityName(int id, String name) {
		activityNames[id] = name;
	}

	public int getActivityId(String activityString) {
		return activityIDMap.get(activityString);
	}

	public void addActivity(String activityName) {
		activityIDMap.put(activityName, activityCount);
		activityCount++;
	}
	
	//分配动态内存
	public void setMemory() {
		activityNames = new String[activityCount];
	}
	
	public void setBeginTime(long time){
		if (time < beginTime || beginTime == 0 ){
			beginTime = time;
		}
	}
	
	public void setEndTime(long time){
		if (time > endTime){
			endTime = time;
		}
	}
	
	public boolean exists(long frame) {
		try {			
			String sql = "select * from animation where frame = " + frame + " OFFSET 0 ROWS FETCH NEXT 1 ROWS ONLY";
			PreparedStatement derbyStatement = MainFrame.derbyConnection.prepareStatement(sql);
         	ResultSet derbyResult = derbyStatement.executeQuery();
         	
            if (derbyResult.next())
            {
            	return true;
            }
         	
	        if (derbyResult != null)
	        	derbyResult.close();
	        if (derbyStatement != null)
	        	derbyStatement.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}
	
	public Frame newFrame() {
		return new Frame();
	}
	
	public Frame newFrame(long frame) {
		return new Frame(frame);
	}
	
	public class Frame {
		public int[] activityFre; //case频率
		public int[][] activityQueFre; //边的case频率
		long frame;
		long nextFrame;
		boolean insert = false;
		
		public Frame() {
			// TODO Auto-generated constructor stub
			activityQueFre = new int[activityCount][activityCount];
			activityFre = new int[activityCount];
			insert = true;
			frame = -1;
			nextFrame = -1;
		}
		
		public Frame(long frame) {
			// TODO Auto-generated constructor stub
			try {
				String sql = "select * from animation where frame = " + frame + " OFFSET 0 ROWS FETCH NEXT 1 ROWS ONLY";
				PreparedStatement derbyStatement = MainFrame.derbyConnection.prepareStatement(sql);
	         	ResultSet derbyResult = derbyStatement.executeQuery();
	         	
				activityQueFre = new int[activityCount][activityCount];
				activityFre = new int[activityCount];
				
	            if (derbyResult.next())
	            {
	            	this.frame = derbyResult.getLong("frame");
	            	nextFrame = derbyResult.getLong("nextFrame");
	            	
					String[] activityFreString = derbyResult.getString("activityFre").split(",");
					for(int i = 0; i < activityCount; i++){
						activityFre[i] = Integer.parseInt(activityFreString[i]);
					}
					
					String[] activityQueFreString = derbyResult.getString("activityQueFre").split(";");
					String[][] activityQueFreStringArray = new String[activityCount][activityCount];
					for(int i = 0; i < activityCount; i++){
						activityQueFreStringArray[i] = activityQueFreString[i].split(",");
					}
					for(int i = 0; i < activityCount; i++)
						for(int j = 0; j < activityCount; j++){
							activityQueFre[i][j] = Integer.parseInt(activityQueFreStringArray[i][j]);
						}
	            }
	         	
    	        if (derbyResult != null)
    	        	derbyResult.close();
		        if (derbyStatement != null)
		        	derbyStatement.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		public Frame(ResultSet resultSet) {
			// TODO Auto-generated constructor stub
			activityQueFre = new int[activityCount][activityCount];
			activityFre = new int[activityCount];
			
	    	try {
	    		frame = resultSet.getLong("frame");
            	nextFrame = resultSet.getLong("nextFrame");
			    
				String[] activityFreString = resultSet.getString("activityFre").split(",");
				for(int i = 0; i < activityCount; i++){
					activityFre[i] = Integer.parseInt(activityFreString[i]);
				}
				
				String[] activityQueFreString = resultSet.getString("activityQueFre").split(";");
				String[][] activityQueFreStringArray = new String[activityCount][activityCount];
				for(int i = 0; i < activityCount; i++){
					activityQueFreStringArray[i] = activityQueFreString[i].split(",");
				}
				for(int i = 0; i < activityCount; i++)
					for(int j = 0; j < activityCount; j++){
						activityQueFre[i][j] = Integer.parseInt(activityQueFreStringArray[i][j]);
					}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		public void insert(Statement derbyStatement){
			try {
				String activityFreString = "" + activityFre[0];
				for(int i = 1; i < activityCount; i++){
					activityFreString += "," + activityFre[i];
				}
				String activityQueFreString = "" + activityQueFre[0][0];
				for(int j = 1; j < activityCount; j++){
					activityQueFreString += "," + activityQueFre[0][j];
				}
				for(int i = 1; i < activityCount; i++){
					activityQueFreString += ";" + activityQueFre[i][0];
					for(int j = 1; j < activityCount; j++){
						activityQueFreString += "," + activityQueFre[i][j];
					}
				}
				
				String sql = "insert into animation values(" + frame + ", " + nextFrame + ", '" + activityFreString + "', '" + activityQueFreString + "')";
//				PreparedStatement derbyStatement = MainFrame.derbyConnection.prepareStatement(sql);
//	         	derbyStatement.executeUpdate();
//		        if (derbyStatement != null)
//		        	derbyStatement.close();
				derbyStatement.addBatch(sql);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		public void update(Statement derbyStatement){
			try {
				String activityFreString = "" + activityFre[0];
				for(int i = 1; i < activityCount; i++){
					activityFreString += "," + activityFre[i];
				}
				String activityQueFreString = "" + activityQueFre[0][0];
				for(int j = 1; j < activityCount; j++){
					activityQueFreString += "," + activityQueFre[0][j];
				}
				for(int i = 1; i < activityCount; i++){
					activityQueFreString += ";" + activityQueFre[i][0];
					for(int j = 1; j < activityCount; j++){
						activityQueFreString += "," + activityQueFre[i][j];
					}
				}
				
				String sql = "update animation set nextFrame = " + nextFrame + ", activityFre = '" + activityFreString + "', activityQueFre = '" + activityQueFreString + "' where frame = " + frame;
//				PreparedStatement derbyStatement = MainFrame.derbyConnection.prepareStatement(sql);
//	         	derbyStatement.executeUpdate();
//		        if (derbyStatement != null)
//		        	derbyStatement.close();
		        derbyStatement.addBatch(sql);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		public void update(PreparedStatement derbyStatement){
			try {
				String activityFreString = "" + activityFre[0];
				for(int i = 1; i < activityCount; i++){
					activityFreString += "," + activityFre[i];
				}
				String activityQueFreString = "" + activityQueFre[0][0];
				for(int j = 1; j < activityCount; j++){
					activityQueFreString += "," + activityQueFre[0][j];
				}
				for(int i = 1; i < activityCount; i++){
					activityQueFreString += ";" + activityQueFre[i][0];
					for(int j = 1; j < activityCount; j++){
						activityQueFreString += "," + activityQueFre[i][j];
					}
				}
				
//				String sql = "update animation set nextFrame = " + nextFrame + ", activityFre = '" + activityFreString + "', activityQueFre = '" + activityQueFreString + "' where frame = " + frame;
//				PreparedStatement derbyStatement = MainFrame.derbyConnection.prepareStatement(sql);
//	         	derbyStatement.executeUpdate();
//		        if (derbyStatement != null)
//		        	derbyStatement.close();
				derbyStatement.setLong(1, nextFrame);
				derbyStatement.setString(2, activityFreString);
				derbyStatement.setString(3, activityQueFreString);
				derbyStatement.setLong(4, frame);
				derbyStatement.addBatch();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		public void addFrame(Frame frame) {
			for(int i = 0; i < activityCount; i++){
				setActivityFre(i, frame.getActivityFre(i) + getActivityFre(i));
			}
			for(int i = 0; i < activityCount; i++)
				for(int j = 0; j < activityCount; j++){
					setActivityQueFre(i, j, frame.getActivityQueFre(i, j) + getActivityQueFre(i, j));
				}
		}
		
		public void setFrame(long num){
			frame = num;
		}
		
		public long getFrame(){
			return frame;
		}
		
		public void setNextFrame(long num){
			nextFrame = num;
		}
		
		public long getNextFrame(){
			return nextFrame;
		}
		
		public void setActivityFre(int pos, int num) {
			activityFre[pos] = num;
		}

		public void setActivityQueFre(int parent, int children, int num) {
			activityQueFre[parent][children] = num;
		}
		
		public void incActivityFre(int pos) {
			activityFre[pos]++;
		}

		public void incActivityQueFre(int parent, int children) {
			activityQueFre[parent][children]++;
		}
		
		public void decActivityFre(int pos) {
			activityFre[pos]--;
		}

		public void decActivityQueFre(int parent, int children) {
			activityQueFre[parent][children]--;
		}
		
		public int getActivityFre(int pos) {
			return activityFre[pos];
		}

		public int getActivityQueFre(int parent, int children) {
			return activityQueFre[parent][children];
		}
	}

	public void addFrame(Frame c, Statement derbyStatement) {
		if(c.insert)	
			c.insert(derbyStatement);
		else
			c.update(derbyStatement);
	    size++;
	}
	
	public Frame getFirst(){
		try {
			String sql = "select * from animation order by frame";
			MainFrame.derbyStatement = MainFrame.derbyConnection.prepareStatement(sql);
			MainFrame.derbyResult = MainFrame.derbyStatement.executeQuery();
            if (MainFrame.derbyResult.next())
            {
            	return new Frame(MainFrame.derbyResult);
            }
            else
            {
    	        if (MainFrame.derbyResult != null)
    	        	MainFrame.derbyResult.close();
    	        if (MainFrame.derbyStatement != null)
    	        	MainFrame.derbyStatement.close();
            	return null;
            }
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		try {
	        if (MainFrame.derbyResult != null)
	        	MainFrame.derbyResult.close();
	        if (MainFrame.derbyStatement != null)
	        	MainFrame.derbyStatement.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	public Frame getNext(){
		try {
            if (MainFrame.derbyResult.next())
            {
            	return new Frame(MainFrame.derbyResult);
            }
            else
            {
    	        if (MainFrame.derbyResult != null)
    	        	MainFrame.derbyResult.close();
    	        if (MainFrame.derbyStatement != null)
    	        	MainFrame.derbyStatement.close();
            	return null;
            }
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		try {
	        if (MainFrame.derbyResult != null)
	        	MainFrame.derbyResult.close();
	        if (MainFrame.derbyStatement != null)
	        	MainFrame.derbyStatement.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	public Frame getNextFrame(Frame currentFrame){
		if(currentFrame.getNextFrame() != -1)
			return newFrame(currentFrame.getNextFrame());
		return null;
	}
	
	public void merge(){
		try{
    		int icount = 0;
    		long now;
    		MainFrame.derbyConnection.setAutoCommit(false);
    		String sql = "update animation set nextFrame = ?, activityFre = ?, activityQueFre = ? where frame = ?";
			PreparedStatement derbyStatement = MainFrame.derbyConnection.prepareStatement(sql);
			
	    	animationFrame[0] = beginTime;
	    	
	        Frame frame = getFirst();
	        Frame nextFrame = getNext();
	        Frame nextNextFrame = null;
	        
	        if(nextFrame != null){
	        	frame.setNextFrame(nextFrame.getFrame());
	        	frame.update(derbyStatement);
	        	nextNextFrame = getNext();
	        	
	            while (nextNextFrame != null) {
	            	icount++;
					if(icount % 10000 == 0){
						derbyStatement.executeBatch();
						MainFrame.derbyConnection.commit();
						derbyStatement.clearBatch();
						now=System.currentTimeMillis();
						System.out.println("animationMerge"+(icount) +"："+(now)+"毫秒");
					}
	            	animationFrame[((int) Math.ceil((nextFrame.getFrame() - beginTime) * 100 / (endTime - beginTime)))] = nextFrame.getFrame();
	            	nextFrame.addFrame(frame);
	            	nextFrame.setNextFrame(nextNextFrame.getFrame());
	            	nextFrame.update(derbyStatement);
	            	
	            	frame = nextFrame;
	            	nextFrame = nextNextFrame;
	            	nextNextFrame = getNext();
	            }
	            
	        	animationFrame[((int) Math.ceil((nextFrame.getFrame() - beginTime) * 100 / (endTime - beginTime)))] = nextFrame.getFrame();
	        	nextFrame.addFrame(frame);
	        	nextFrame.update(derbyStatement);
	        }
	        
	        for(int i = 1; i < 101; i++){
	        	if(animationFrame[i] == 0)
	        		animationFrame[i] = animationFrame[i - 1];
	        }
	        
			derbyStatement.executeBatch();
			MainFrame.derbyConnection.commit();
	        if (derbyStatement != null)
	        	derbyStatement.close();
    		MainFrame.derbyConnection.setAutoCommit(true);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public Frame getAnimationFrame(int i){
		if(i < 0 || i > 100)
			return null;
		return newFrame(animationFrame[i]);
	}
}
