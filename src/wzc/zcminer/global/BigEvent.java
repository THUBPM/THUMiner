package wzc.zcminer.global;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import wzc.zcminer.frame.MainFrame;

//event 类
public class BigEvent {
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
	
	//event属性
    String fileNameUUID;
	String caseID;
	String activity;
	//String startDateString;
	//String endDateString;
	String resourceString;
    Date startDate;
	Date endDate;
	boolean first;
	boolean last;
	String leftChildEventID;
	String rightChildEventID;
	String parentEventID;
	int whichChildEventID; //0:left 1:right -1:root
	int heightEventID;
	String leftChildActivity;
	String rightChildActivity;
	String parentActivity;
	int whichChildActivity; //0:left 1:right -1:root
	int heightActivity;
	String leftChildResource;
	String rightChildResource;
	String parentResource;
	int whichChildResource; //0:left 1:right -1:root
	int heightResource;

	public BigEvent() {
		// TODO Auto-generated constructor stub
		fileNameUUID = "";
	    caseID = "";
	    activity = "";
	    resourceString = "";
		//startDateString = "";
		//endDateString = "";
		startDate = null;
		endDate = null;
		first = false;
		last = false;
		leftChildEventID = "";
		rightChildEventID = "";
		parentEventID = "";
		whichChildEventID = -2;
		heightEventID = 1;
		leftChildActivity = "";
		rightChildActivity = "";
		parentActivity = "";
		whichChildActivity = -2;
		heightActivity = 1;
		leftChildResource = "";
		rightChildResource = "";
		parentResource = "";
		whichChildResource = -2;
		heightResource = 1;
	}
	
	public BigEvent(String fileName) {
		// TODO Auto-generated constructor stub
    	try {
			File file = new File("data_tmp//event//" + fileName);
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
			    activity = tempString;
				tempString = reader.readLine();
			    resourceString = tempString;
				//startDateString = "";
				//endDateString = "";
				tempString = reader.readLine();
				startDate = new Date(Long.parseLong(tempString));
				tempString = reader.readLine();
				endDate = new Date(Long.parseLong(tempString));
				tempString = reader.readLine();
				first = Boolean.parseBoolean(tempString);
				tempString = reader.readLine();
				last = Boolean.parseBoolean(tempString);
				tempString = reader.readLine();
				leftChildEventID = tempString;
				tempString = reader.readLine();
				rightChildEventID = tempString;
				tempString = reader.readLine();
				parentEventID = tempString;
				tempString = reader.readLine();
				whichChildEventID = Integer.parseInt(tempString);
				tempString = reader.readLine();
				heightEventID = Integer.parseInt(tempString);
				tempString = reader.readLine();
				leftChildActivity = tempString;
				tempString = reader.readLine();
				rightChildActivity = tempString;
				tempString = reader.readLine();
				parentActivity = tempString;
				tempString = reader.readLine();
				whichChildActivity = Integer.parseInt(tempString);
				tempString = reader.readLine();
				heightActivity = Integer.parseInt(tempString);
				tempString = reader.readLine();
				leftChildResource = tempString;
				tempString = reader.readLine();
				rightChildResource = tempString;
				tempString = reader.readLine();
				parentResource = tempString;
				tempString = reader.readLine();
				whichChildResource = Integer.parseInt(tempString);
				tempString = reader.readLine();
				heightResource = Integer.parseInt(tempString);
				
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
	        String path = "data_tmp\\event\\" + fileName;
	        createFile(path);
	        FileWriter fw = new FileWriter(path);
	        setFileName(fileName);
	        String str = getFileName() + "\n" + getCase() + "\n" + getActivity() + "\n" + getResource() + "\n" + getStartDate().getTime() + "\n" + getEndDate().getTime() + "\n" + getFirst() + "\n" + getLast()
	        		+ "\n" + getLeftChildEventID() + "\n" + getRightChildEventID() + "\n" + getParentEventID() + "\n" + getWhichChildEventID() + "\n" + getHeightEventID()
	        		+ "\n" + getLeftChildActivity() + "\n" + getRightChildActivity() + "\n" + getParentActivity() + "\n" + getWhichChildActivity() + "\n" + getHeightActivity()
	        		+ "\n" + getLeftChildResource() + "\n" + getRightChildResource() + "\n" + getParentResource() + "\n" + getWhichChildResource() + "\n" + getHeightResource();
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
	
    public void setHeightEventID(int heightEventID) {
    	this.heightEventID = heightEventID;
    }
    
    public int getHeightEventID() {
        return heightEventID;
    }
    
    public void setHeightActivity(int heightActivity) {
    	this.heightActivity = heightActivity;
    }
    
    public int getHeightActivity() {
        return heightActivity;
    }
    
    public void setHeightResource(int heightResource) {
    	this.heightResource = heightResource;
    }
    
    public int getHeightResource() {
        return heightResource;
    }
	
    public void setLeftChildEventID(String leftChildEventID) {
    	this.leftChildEventID = leftChildEventID;
    }
    
    public String getLeftChildEventID() {
        return leftChildEventID;
    }
	
    public void setRightChildEventID(String rightChildEventID) {
    	this.rightChildEventID = rightChildEventID;
    }
    
    public String getRightChildEventID() {
        return rightChildEventID;
    }
	
    public void setParentEventID(String parentEventID) {
    	this.parentEventID = parentEventID;
    }
    
    public String getParentEventID() {
        return parentEventID;
    }

    public void setWhichChildEventID(int whichChildEventID) {
    	this.whichChildEventID = whichChildEventID;
    }
    
    public int getWhichChildEventID() {
        return whichChildEventID;
    }
    
    public void setLeftChildActivity(String leftChildActivity) {
    	this.leftChildActivity = leftChildActivity;
    }
    
    public String getLeftChildActivity() {
        return leftChildActivity;
    }
	
    public void setRightChildActivity(String rightChildActivity) {
    	this.rightChildActivity = rightChildActivity;
    }
    
    public String getRightChildActivity() {
        return rightChildActivity;
    }
	
    public void setParentActivity(String parentActivity) {
    	this.parentActivity = parentActivity;
    }
    
    public String getParentActivity() {
        return parentActivity;
    }

    public void setWhichChildActivity(int whichChildActivity) {
    	this.whichChildActivity = whichChildActivity;
    }
    
    public int getWhichChildActivity() {
        return whichChildActivity;
    }
    
    public void setLeftChildResource(String leftChildResource) {
    	this.leftChildResource = leftChildResource;
    }
    
    public String getLeftChildResource() {
        return leftChildResource;
    }
	
    public void setRightChildResource(String rightChildResource) {
    	this.rightChildResource = rightChildResource;
    }
    
    public String getRightChildResource() {
        return rightChildResource;
    }
	
    public void setParentResource(String parentResource) {
    	this.parentResource = parentResource;
    }
    
    public String getParentResource() {
        return parentResource;
    }

    public void setWhichChildResource(int whichChildResource) {
    	this.whichChildResource = whichChildResource;
    }
    
    public int getWhichChildResource() {
        return whichChildResource;
    }
    
    public void setFirst() {
        first = true;
    	save(fileNameUUID);
    }
    
    public void setLast() {
        last = true;
    	save(fileNameUUID);
    }
    
    public boolean getFirst() {
        return first;
    }
    
    public boolean getLast() {
        return last;
    }
	
	//活动时长
	public long getTime() {
		long hours = 0;
		
		if (endDate == null)
		{
			hours = 0;
		} else
		{
			hours = (endDate.getTime() - startDate.getTime());
		}
		return hours;
	}

	public Date getStartDate() {
		return startDate;
	}
	
	public Date getEndDate() {
		if (endDate == null)
		{
			return startDate;
		}else
		{
			return endDate;
		}
	}
	public void setCase(String caseID) {
		this.caseID = caseID;
	}

	public String getCase() {
		return caseID;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public String getActivity() {
		return activity;
	}

	public void setResource(String resource) {
		this.resourceString = resource;
	}

	public String getResource() {
		return resourceString;
	}
	//设置时间，考虑单个或多个时间的情况
	public void setDate(String dateString, String timeString) {
		SimpleDateFormat sdf = new SimpleDateFormat(timeString);  
		if (startDate == null) {
			//this.startDateString = dateString;
			//this.endDateString = dateString;
			try {
				startDate = sdf.parse(dateString);
				endDate = sdf.parse(dateString);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			//this.endDateString = dateString;
			try {
				endDate = sdf.parse(dateString);
				if(endDate.before(startDate))
				{
					Date temp = startDate;
					startDate = endDate;
					endDate = temp;
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
