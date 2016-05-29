package wzc.zcminer.global;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import wzc.zcminer.frame.MainFrame;

//event 类
public class BigEventDerby {
	//event属性
    String uuid;
	String caseID;
	String activity;
	String resource;
    Date startDate;
	Date endDate;
	boolean first;
	boolean last;

	public BigEventDerby() {
		// TODO Auto-generated constructor stub
		uuid = "";
	    caseID = "";
	    activity = "";
	    resource = "";
		//startDateString = "";
		//endDateString = "";
		startDate = null;
		endDate = null;
		first = false;
		last = false;
	}
	
	public BigEventDerby(ResultSet resultSet) {
    	try {
			uuid = resultSet.getString("UUID");
		    caseID = resultSet.getString("caseID");
		    activity = resultSet.getString("activity");
			resource = resultSet.getString("resource");
			startDate = new Date(resultSet.getLong("startDate"));
			endDate = new Date(resultSet.getLong("endDate"));
			first = resultSet.getBoolean("firstInCase");
			last = resultSet.getBoolean("lastInCase");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public BigEventDerby(String uuid) {
		try {
			String sql = "select * from eventCollection where UUID = '" + uuid + "'" + " OFFSET 0 ROWS FETCH NEXT 1 ROWS ONLY";
			PreparedStatement derbyStatement = MainFrame.derbyConnection.prepareStatement(sql);
         	ResultSet derbyResult = derbyStatement.executeQuery();
			
            if (derbyResult.next())
            {
    			uuid = derbyResult.getString("UUID");
    		    caseID = derbyResult.getString("caseID");
    		    activity = derbyResult.getString("activity");
    			resource = derbyResult.getString("resource");
    			startDate = new Date(derbyResult.getLong("startDate"));
    			endDate = new Date(derbyResult.getLong("endDate"));
    			first = derbyResult.getBoolean("firstInCase");
    			last = derbyResult.getBoolean("lastInCase");
            }
         	
	        if (derbyResult != null)
	        	derbyResult.close();
	        if (derbyStatement != null)
	        	derbyStatement.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void insert(PreparedStatement derbyStatement){
		try {
//			String sql = "insert into eventCollection values('" + uuid + "', '" + caseID + "', '" + activity + "', '" + resource + "', " + startDate.getTime() + ", "+ endDate.getTime() + ", false, false)";
//			PreparedStatement derbyStatement = MainFrame.derbyConnection.prepareStatement(sql);
//         	derbyStatement.executeUpdate();
//	        if (derbyStatement != null)
//	        	derbyStatement.close();
			derbyStatement.setString(1, uuid);
			derbyStatement.setString(2, caseID);
			derbyStatement.setString(3, activity);
			derbyStatement.setString(4, resource);
			derbyStatement.setLong(5, startDate.getTime());
			derbyStatement.setLong(6, endDate.getTime());
			derbyStatement.addBatch();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
    public void setUUID(String uuid) {
    	this.uuid = uuid;
    }
    
    public String getUUID() {
        return uuid;
    }
    
    public void setFirst(PreparedStatement derbyStatement) {
        first = true;
		try {
//			String sql = "update eventCollection set firstInCase = true where UUID = '" + uuid + "'";
//			PreparedStatement derbyStatement = MainFrame.derbyConnection.prepareStatement(sql);
//         	derbyStatement.executeUpdate();
//	        if (derbyStatement != null)
//	        	derbyStatement.close();
			derbyStatement.setString(1, uuid);
			derbyStatement.addBatch();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
    }
    
    public void setLast(PreparedStatement derbyStatement) {
        last = true;
		try {
//			String sql = "update eventCollection set lastInCase = true where UUID = '" + uuid + "'";
//			PreparedStatement derbyStatement = MainFrame.derbyConnection.prepareStatement(sql);
//         	derbyStatement.executeUpdate();
//	        if (derbyStatement != null)
//	        	derbyStatement.close();
			derbyStatement.setString(1, uuid);
			derbyStatement.addBatch();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
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
		this.resource = resource;
	}

	public String getResource() {
		return resource;
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
