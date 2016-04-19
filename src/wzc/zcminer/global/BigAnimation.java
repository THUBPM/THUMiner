package wzc.zcminer.global;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import wzc.zcminer.frame.MainFrame;

public class BigAnimation {
    public int compare(Frame frame1, Frame frame2) {
        if(frame1.getFrame() - frame2.getFrame() > 0) {return 1;}
        else if (frame1.getFrame() - frame2.getFrame() < 0) {return -1;}
        else {return 0;}
    }
	
    public static boolean createFile(String destFileName) {
    	File file = new File(destFileName);
    	if(file.exists()) {
    		return false;
    	}
    	if (destFileName.endsWith(File.separator)) {
    		return false;
    	}
    	if(!file.getParentFile().exists()) {
    		if(!file.getParentFile().mkdirs()) {
    			return false;
    		}
    	}
    	try {
    		if (file.createNewFile()) {
    			return true;
    		} else {
    			return false;
    		}
    	} catch (IOException e) {
    		e.printStackTrace();
    		return false;
    	}
	}
	
	public void rightRotate(String rootID) {
		Frame tempFile = new Frame(rootID);
		
		Frame leftChild = null;
		Frame parent = null;
		if(!tempFile.getLeftChild().equals("")){
			leftChild = new Frame(tempFile.getLeftChild());
		}
		if(!tempFile.getParent().equals("")){
			parent = new Frame(tempFile.getParent());
		}
		
		Frame leftRightChild = null;
		int leftRightHeight = 0;
		if(!leftChild.getRightChild().equals("")){
			leftRightChild = new Frame(leftChild.getRightChild());
			leftRightHeight = leftRightChild.getHeight();
		}
		
		leftChild.setParent(tempFile.getParent());
		leftChild.setWhichChild(tempFile.getWhichChild());
		if(tempFile.getParent().equals(""))
			root = tempFile.getLeftChild();
		else{
			if(tempFile.getWhichChild() == 0)
				parent.setLeftChild(tempFile.getLeftChild());
			else
				parent.setRightChild(tempFile.getLeftChild());
			parent.save(tempFile.getParent());
		}
		tempFile.setParent(tempFile.getLeftChild());
		tempFile.setWhichChild(1);
		tempFile.setLeftChild(leftChild.getRightChild());
		tempFile.setHeight(leftRightHeight + 1);
		if(leftRightChild != null){
			leftRightChild.setParent(rootID);
			leftRightChild.setWhichChild(0);
			leftRightChild.save(tempFile.getLeftChild());
		}
		leftChild.setRightChild(rootID);
		leftChild.setHeight(leftRightHeight + 2);
		
		tempFile.save(rootID);
		leftChild.save(tempFile.getParent());
	}
	
	public void leftRotate(String rootID) {
		Frame tempFile = new Frame(rootID);
		
		Frame rightChild = null;
		Frame parent = null;
		if(!tempFile.getRightChild().equals("")){
			rightChild = new Frame(tempFile.getRightChild());
		}
		if(!tempFile.getParent().equals("")){
			parent = new Frame(tempFile.getParent());
		}
		
		Frame rightLeftChild = null;
		int rightLeftHeight = 0;
		if(!rightChild.getLeftChild().equals("")){
			rightLeftChild = new Frame(rightChild.getLeftChild());
			rightLeftHeight = rightLeftChild.getHeight();
		}

		rightChild.setParent(tempFile.getParent());
		rightChild.setWhichChild(tempFile.getWhichChild());
		if(tempFile.getParent().equals(""))
			root = tempFile.getRightChild();
		else{
			if(tempFile.getWhichChild() == 0)
				parent.setLeftChild(tempFile.getRightChild());
			else
				parent.setRightChild(tempFile.getRightChild());
			parent.save(tempFile.getParent());
		}
		tempFile.setParent(tempFile.getRightChild());
		tempFile.setWhichChild(0);
		tempFile.setRightChild(rightChild.getLeftChild());
		tempFile.setHeight(rightLeftHeight + 1);
		if(rightLeftChild != null){
			rightLeftChild.setParent(rootID);
			rightLeftChild.setWhichChild(1);
			rightLeftChild.save(tempFile.getRightChild());
		}
		rightChild.setLeftChild(rootID);
		rightChild.setHeight(rightLeftHeight + 2);
		
		tempFile.save(rootID);
		rightChild.save(tempFile.getParent());
	}
    
	HashMap<String, Integer> activityIDMap;
	public String[] activityNames;
	public int activityCount;   //活动数
	public long beginTime;   //整个图的开始时间
	public long endTime; 	//整个图的结束时间
	public String root; 	//整个图的结束时间
	public int size;
	public long[] animationFrame;

	public BigAnimation() {
		// TODO Auto-generated constructor stub
		activityIDMap = new HashMap<String, Integer>();
		activityCount = 2;
		beginTime = 0;
		endTime = 0;
	    root = null;
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
	
	public Frame newFrame() {
		return new Frame();
	}
	
	public Frame newFrame(String fileName) {
		return new Frame(fileName);
	}
	
	public Frame newFrame(long fileName) {
		return new Frame(fileName + "");
	}
	
	public class Frame {
		public int[] activityFre; //case频率
		public int[][] activityQueFre; //边的case频率
		String leftChild;
		String rightChild;
		String parent;
		int whichChild; //0:left 1:right -1:root
		int height;
		long fileFrame;
		
		public Frame() {
			// TODO Auto-generated constructor stub
			activityQueFre = new int[activityCount][activityCount];
			activityFre = new int[activityCount];
			
			fileFrame = -1;
			leftChild = "";
			rightChild = "";
			parent = "";
			whichChild = -2;
			height = 1;
		}
		
		public Frame(String fileName) {
			// TODO Auto-generated constructor stub
			activityQueFre = new int[activityCount][activityCount];
			activityFre = new int[activityCount];
	    	try {
				File file = new File("data_tmp//animation//" + fileName);
				InputStreamReader read = null;
				BufferedReader reader = null;
				try{
					read = new InputStreamReader(new FileInputStream(file));
					reader = new BufferedReader(read);
					String tempString = null;

					tempString = reader.readLine();
					fileFrame = Long.parseLong(tempString);
					tempString = reader.readLine();
					leftChild = tempString;
					tempString = reader.readLine();
					rightChild = tempString;
					tempString = reader.readLine();
					parent = tempString;
					tempString = reader.readLine();
					whichChild = Integer.parseInt(tempString);
					tempString = reader.readLine();
					height = Integer.parseInt(tempString);
					for(int i = 0; i < activityCount; i++){
						tempString = reader.readLine();
						activityFre[i] = Integer.parseInt(tempString);
					}
					for(int i = 0; i < activityCount; i++)
						for(int j = 0; j < activityCount; j++){
							tempString = reader.readLine();
							activityQueFre[i][j] = Integer.parseInt(tempString);
						}
					
					reader.close();
					read.close();
				}catch(IOException e){
					e.printStackTrace();
				}finally{
					if(reader!=null)
					{
						try{
							reader.close();
							read.close();
						}catch(IOException e)
						{
							e.printStackTrace();
						}
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		public void save(String fileName) {
		    try{
		        String path = "data_tmp\\animation\\" + fileName;
		        createFile(path);
		        FileWriter fw = new FileWriter(path);
		        
		        String str = getFrame() + "\n" + getLeftChild() + "\n" + getRightChild() + "\n" + getParent() + "\n" + getWhichChild() + "\n" + getHeight();
				for(int i = 0; i < activityCount; i++){
					str += ("\n" + getActivityFre(i));
				}
				for(int i = 0; i < activityCount; i++)
					for(int j = 0; j < activityCount; j++){
						str += ("\n" + getActivityQueFre(i, j));
					}
				
		        fw.write(str);
		        fw.close();
		    }catch(IOException ex){
		        System.out.println(ex.getStackTrace());
		    }
		}
		
		public void save(long fileName) {
		    try{
		        String path = "data_tmp\\animation\\" + fileName;
		        createFile(path);
		        FileWriter fw = new FileWriter(path);
		        
		        String str = getFrame() + "\n" + getLeftChild() + "\n" + getRightChild() + "\n" + getParent() + "\n" + getWhichChild() + "\n" + getHeight();
				for(int i = 0; i < activityCount; i++){
					str += ("\n" + getActivityFre(i));
				}
				for(int i = 0; i < activityCount; i++)
					for(int j = 0; j < activityCount; j++){
						str += ("\n" + getActivityQueFre(i, j));
					}
				
		        fw.write(str);
		        fw.close();
		    }catch(IOException ex){
		        System.out.println(ex.getStackTrace());
		    }
		}
		
		public void save() {
		    try{
		        String path = "data_tmp\\animation\\" + fileFrame;
		        createFile(path);
		        FileWriter fw = new FileWriter(path);
		        
		        String str = getFrame() + "\n" + getLeftChild() + "\n" + getRightChild() + "\n" + getParent() + "\n" + getWhichChild() + "\n" + getHeight();
				for(int i = 0; i < activityCount; i++){
					str += ("\n" + getActivityFre(i));
				}
				for(int i = 0; i < activityCount; i++)
					for(int j = 0; j < activityCount; j++){
						str += ("\n" + getActivityQueFre(i, j));
					}
				
		        fw.write(str);
		        fw.close();
		    }catch(IOException ex){
		        System.out.println(ex.getStackTrace());
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
			fileFrame = num;
		}
		
		public long getFrame(){
			return fileFrame;
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
		
	    public void setHeight(int height) {
	    	this.height = height;
	    }
	    
	    public int getHeight() {
	        return height;
	    }
	    
	    public void setLeftChild(String leftChild) {
	    	this.leftChild = leftChild;
	    }
	    
	    public String getLeftChild() {
	        return leftChild;
	    }
		
	    public void setRightChild(String rightChild) {
	    	this.rightChild = rightChild;
	    }
	    
	    public String getRightChild() {
	        return rightChild;
	    }
		
	    public void setParent(String parent) {
	    	this.parent = parent;
	    }
	    
	    public String getParent() {
	        return parent;
	    }

	    public void setWhichChild(int whichChild) {
	    	this.whichChild = whichChild;
	    }
	    
	    public int getWhichChild() {
	        return whichChild;
	    }
	}

	public void addFrame(Frame c) {
		String fileName = c.getFrame() + "";
		
		if(root == null) {
			root = fileName;
			c.setWhichChild(-1);
			c.save(fileName);
		}else{
			String tempID = root;
			Frame tempFile = new Frame(tempID);
			while((compare(c, tempFile) < 0 && !tempFile.getLeftChild().equals("")) || (compare(c, tempFile) >= 0 && !tempFile.getRightChild().equals(""))){
				if(compare(c, tempFile) < 0){
					tempID = tempFile.getLeftChild();
					tempFile = new Frame(tempID);
				}else{
					tempID = tempFile.getRightChild();
					tempFile = new Frame(tempID);
				}
			}
			
			if(compare(c, tempFile) < 0){
				tempFile.setHeight(2);
				tempFile.setLeftChild(fileName);
				tempFile.save(tempID);
				c.setParent(tempID);
				c.setWhichChild(0);
			}else{
				tempFile.setHeight(2);
				tempFile.setRightChild(fileName);
				tempFile.save(tempID);
				c.setParent(tempID);
				c.setWhichChild(1);
			}
		
			c.save(fileName);
			
			while(!tempFile.getParent().equals("")){
				tempID = tempFile.getParent();
				tempFile = new Frame(tempID);
				
				Frame leftChild = null;
				Frame rightChild = null;
				Frame parent = null;
				int leftHeight = 0;
				int rightHeight = 0;
				if(!tempFile.getLeftChild().equals("")){
					leftChild = new Frame(tempFile.getLeftChild());
					leftHeight = leftChild.getHeight();
				}
				if(!tempFile.getRightChild().equals("")){
					rightChild = new Frame(tempFile.getRightChild());
					rightHeight = rightChild.getHeight();
				}
				if(!tempFile.getParent().equals("")){
					parent = new Frame(tempFile.getParent());
				}
				
				if(leftHeight - rightHeight > 1){
					Frame leftLeftChild = null;
					Frame leftRightChild = null;
					int leftLeftHeight = 0;
					int leftRightHeight = 0;
					if(!leftChild.getLeftChild().equals("")){
						leftLeftChild = new Frame(leftChild.getLeftChild());
						leftLeftHeight = leftLeftChild.getHeight();
					}
					if(!leftChild.getRightChild().equals("")){
						leftRightChild = new Frame(leftChild.getRightChild());
						leftRightHeight = leftRightChild.getHeight();
					}
					
					if(leftLeftHeight > leftRightHeight){
						rightRotate(tempID);
					}else{
						leftRotate(tempFile.getLeftChild());
						rightRotate(tempID);
					}
				}else if(leftHeight - rightHeight < -1){
					Frame rightLeftChild = null;
					Frame rightRightChild = null;
					int rightLeftHeight = 0;
					int rightRightHeight = 0;
					if(!rightChild.getLeftChild().equals("")){
						rightLeftChild = new Frame(rightChild.getLeftChild());
						rightLeftHeight = rightLeftChild.getHeight();
					}
					if(!rightChild.getRightChild().equals("")){
						rightRightChild = new Frame(rightChild.getRightChild());
						rightRightHeight = rightRightChild.getHeight();
					}
					
					if(rightLeftHeight < rightRightHeight){
						leftRotate(tempID);
					}else{
						rightRotate(tempFile.getRightChild());
						leftRotate(tempID);
					}
				}else{
					if(leftHeight < rightHeight){
						tempFile.setHeight(rightHeight + 1);
					}else{
						tempFile.setHeight(leftHeight + 1);
					}
					tempFile.save(tempID);
				}
			}
		}
        
	    size++;
	}
	
	public Frame getFirst(){
		if(root == null)
			return null;

		Frame tempFile = new Frame(root);
		while(!tempFile.getLeftChild().equals("")){
			tempFile = new Frame(tempFile.getLeftChild());
		}
		
		return tempFile;
	}
	
	public Frame getNext(Frame e){
		if(!e.getRightChild().equals("")){
			Frame tempFile = new Frame(e.getRightChild());
			while(!tempFile.getLeftChild().equals("")){
				tempFile = new Frame(tempFile.getLeftChild());
			}
			return tempFile;
		}
		
		Frame tempFile = e;
		while(tempFile.getWhichChild() == 1){
			tempFile = new Frame(tempFile.getParent());
		}
		
		if(tempFile.getWhichChild() == 0){
			tempFile = new Frame(tempFile.getParent());
			return tempFile;
		}
		
		return null;
	}
	
	public void merge(){
    	animationFrame[0] = beginTime;
    	
        Frame frame = getFirst();
        Frame nextFrame = getNext(frame);
        while (nextFrame != null) {
        	animationFrame[((int) Math.ceil((nextFrame.getFrame() - beginTime) * 100 / (endTime - beginTime)))] = nextFrame.getFrame();
        	
        	nextFrame.addFrame(frame);
        	nextFrame.save();
        	
        	frame = nextFrame;
        	nextFrame = getNext(frame);
        }
        
        for(int i = 1; i < 101; i++){
        	if(animationFrame[i] == 0)
        		animationFrame[i] = animationFrame[i - 1];
        }
	}
	
	public Frame getAnimationFrame(int i){
		if(i < 0 || i > 100)
			return null;
		return newFrame(animationFrame[i]);
	}
}
