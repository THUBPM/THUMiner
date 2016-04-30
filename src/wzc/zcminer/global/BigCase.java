package wzc.zcminer.global;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

//case 类
public class BigCase {
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
    
	//case属性
    String fileNameUUID;
	String caseID;
	int events;
	Date start;
	Date end;
	long duration;
	long active;
	List<String> activities;
	List<String> eventList;
	int heightDuration;
	String leftChildID;
	String rightChildID;
	String parentID;
	int whichChildID; //0:left 1:right -1:root
	int heightID;
	String leftChildDuration;
	String rightChildDuration;
	String parentDuration;
	int whichChildDuration; //0:left 1:right -1:root
	String leftChildCaseVariant;
	String rightChildCaseVariant;
	String parentCaseVariant;
	int whichChildCaseVariant; //0:left 1:right -1:root
	int heightCaseVariant;

	public BigCase() {
		// TODO Auto-generated constructor stub
		fileNameUUID = "";
		caseID = "";
		events = 0;
		start = null;
		end = null;
		duration = 0;
		active = 0;
		activities = new ArrayList<String>();
		eventList = new ArrayList<String>();
		leftChildID = "";
		rightChildID = "";
		parentID = "";
		whichChildID = -2;
		heightID = 1;
		leftChildDuration = "";
		rightChildDuration = "";
		parentDuration = "";
		whichChildDuration = -2;
		heightDuration = 1;
		leftChildCaseVariant = "";
		rightChildCaseVariant = "";
		parentCaseVariant = "";
		whichChildCaseVariant = -2;
		heightCaseVariant = 1;
	}
	
	public BigCase(String fileName) {
		// TODO Auto-generated constructor stub
    	try {
			File file = new File("data_tmp//case//" + fileName);
			InputStreamReader read = null;
			BufferedReader reader = null;
			try{
				read = new InputStreamReader(new FileInputStream(file));
				reader = new BufferedReader(read);
				String tempString = null;

				tempString = reader.readLine();
				fileNameUUID = tempString;
				tempString = reader.readLine();
				caseID = tempString;
				tempString = reader.readLine();
				events = Integer.parseInt(tempString);
				tempString = reader.readLine();
				start = new Date(Long.parseLong(tempString));
				tempString = reader.readLine();
				end = new Date(Long.parseLong(tempString));
				tempString = reader.readLine();
				duration = Long.parseLong(tempString);
				tempString = reader.readLine();
				active = Long.parseLong(tempString);
				tempString = reader.readLine();
				activities = Arrays.asList(tempString.split("\t"));
				tempString = reader.readLine();
				eventList = Arrays.asList(tempString.split("\t"));
				tempString = reader.readLine();
				leftChildID = tempString;
				tempString = reader.readLine();
				rightChildID = tempString;
				tempString = reader.readLine();
				parentID = tempString;
				tempString = reader.readLine();
				whichChildID = Integer.parseInt(tempString);
				tempString = reader.readLine();
				heightID = Integer.parseInt(tempString);
				tempString = reader.readLine();
				leftChildDuration = tempString;
				tempString = reader.readLine();
				rightChildDuration = tempString;
				tempString = reader.readLine();
				parentDuration = tempString;
				tempString = reader.readLine();
				whichChildDuration = Integer.parseInt(tempString);
				tempString = reader.readLine();
				heightDuration = Integer.parseInt(tempString);
				tempString = reader.readLine();
				leftChildCaseVariant = tempString;
				tempString = reader.readLine();
				rightChildCaseVariant = tempString;
				tempString = reader.readLine();
				parentCaseVariant = tempString;
				tempString = reader.readLine();
				whichChildCaseVariant = Integer.parseInt(tempString);
				tempString = reader.readLine();
				heightCaseVariant = Integer.parseInt(tempString);
				
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
	        String path = "data_tmp\\case\\" + fileName;
	        createFile(path);
	        FileWriter fw = new FileWriter(path);
	        setFileName(fileName);
	        String str = getFileName() + "\n" + getCase() + "\n" + getSize() + "\n" + getStart().getTime() + "\n" + getEnd().getTime() + "\n" + getDuration() + "\n" + getActive()
	        		+ "\n" + String.join("\t", activities.toArray(new String[]{})) + "\n" + String.join("\t", eventList.toArray(new String[]{}))
	        		+ "\n" + getLeftChildID() + "\n" + getRightChildID() + "\n" + getParentID() + "\n" + getWhichChildID() + "\n" + getHeightID()
	        		+ "\n" + getLeftChildDuration() + "\n" + getRightChildDuration() + "\n" + getParentDuration() + "\n" + getWhichChildDuration() + "\n" + getHeightDuration()
	        		+ "\n" + getLeftChildCaseVariant() + "\n" + getRightChildCaseVariant() + "\n" + getParentCaseVariant() + "\n" + getWhichChildCaseVariant() + "\n" + getHeightCaseVariant();
	        fw.write(str);
	        fw.close();
	    }catch(IOException ex){
	        System.out.println(ex.getStackTrace());
	    }
	}
	
    public void setFileName(String fileNameUUID) {
    	this.fileNameUUID = fileNameUUID;
    }
    
    public String getFileName() {
        return fileNameUUID;
    }
	
    public void setHeightID(int heightID) {
    	this.heightID = heightID;
    }
    
    public int getHeightID() {
        return heightID;
    }
    
    public void setHeightDuration(int heightDuration) {
    	this.heightDuration = heightDuration;
    }
    
    public int getHeightDuration() {
        return heightDuration;
    }
    
    public void setHeightCaseVariant(int heightCaseVariant) {
    	this.heightCaseVariant = heightCaseVariant;
    }
    
    public int getHeightCaseVariant() {
        return heightCaseVariant;
    }
	
    public void setLeftChildID(String leftChildID) {
    	this.leftChildID = leftChildID;
    }
    
    public String getLeftChildID() {
        return leftChildID;
    }
	
    public void setRightChildID(String rightChildID) {
    	this.rightChildID = rightChildID;
    }
    
    public String getRightChildID() {
        return rightChildID;
    }
	
    public void setParentID(String parentID) {
    	this.parentID = parentID;
    }
    
    public String getParentID() {
        return parentID;
    }

    public void setWhichChildID(int whichChildID) {
    	this.whichChildID = whichChildID;
    }
    
    public int getWhichChildID() {
        return whichChildID;
    }
    
    public void setLeftChildDuration(String leftChildDuration) {
    	this.leftChildDuration = leftChildDuration;
    }
    
    public String getLeftChildDuration() {
        return leftChildDuration;
    }
	
    public void setRightChildDuration(String rightChildDuration) {
    	this.rightChildDuration = rightChildDuration;
    }
    
    public String getRightChildDuration() {
        return rightChildDuration;
    }
	
    public void setParentDuration(String parentDuration) {
    	this.parentDuration = parentDuration;
    }
    
    public String getParentDuration() {
        return parentDuration;
    }

    public void setWhichChildDuration(int whichChildDuration) {
    	this.whichChildDuration = whichChildDuration;
    }
    
    public int getWhichChildDuration() {
        return whichChildDuration;
    }
    
    public void setLeftChildCaseVariant(String leftChildCaseVariant) {
    	this.leftChildCaseVariant = leftChildCaseVariant;
    }
    
    public String getLeftChildCaseVariant() {
        return leftChildCaseVariant;
    }
	
    public void setRightChildCaseVariant(String rightChildCaseVariant) {
    	this.rightChildCaseVariant = rightChildCaseVariant;
    }
    
    public String getRightChildCaseVariant() {
        return rightChildCaseVariant;
    }
	
    public void setParentCaseVariant(String parentCaseVariant) {
    	this.parentCaseVariant = parentCaseVariant;
    }
    
    public String getParentCaseVariant() {
        return parentCaseVariant;
    }

    public void setWhichChildCaseVariant(int whichChildCaseVariant) {
    	this.whichChildCaseVariant = whichChildCaseVariant;
    }
    
    public int getWhichChildCaseVariant() {
        return whichChildCaseVariant;
    }
	
	public void setCase(String caseID) {
		this.caseID = caseID;
	}

	public String getCase() {
		return caseID;
	}

	public void addEvent(BigEvent c) {
	    events++;
	    if(events == 1)
	    {
	        start = c.getStartDate();
	    	end = c.getEndDate();
	    	active += c.getTime();
	    }
	    else if(c.getEndDate().after(end))
	    {
	    	if(c.getStartDate().before(end))
	    		active += (c.getEndDate().getTime() - end.getTime());
	    	else
	    		active += c.getTime();
	    	end = c.getEndDate();
	    }
	    duration = end.getTime() - start.getTime();
	    eventList.add(c.getFileName());
	}
	
    public Date getStart() {
        return start;
    }
    
    public Date getEnd() {
        return end;
    }
    
    public long getDuration() {
        return duration;
    }
    
    public long getActive() {
        return active;
    }
    
    public double getActive(boolean zeroIsZero) {
        if(duration > 0)
            return active * 1.0 / duration;
        if(zeroIsZero)
            return 0;
        return 1;
    }
    
    public double getMeanActiveTime() {
        return active * 1.0 / events;
    }
    
    public double getMeanWaitingTime() {
        if(events > 1)
            return (duration - active) * 1.0 / (events - 1);
        return 0;
    }
	
	public int getSize(){
		return events;
	}
	
	public BigEvent getEvent(int pos) {
		return new BigEvent(eventList.get(pos));
	}

    public void addActivity(String c) {
        activities.add(c);
    }
    
    public int getActivitiesSize(){
        return activities.size();
    }
    
    public String getActivity(int pos) {
        return activities.get(pos);
    }
    
    public List<String> getActivities(){
        return activities;
    }
}
