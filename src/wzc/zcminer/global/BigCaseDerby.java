package wzc.zcminer.global;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import wzc.zcminer.frame.MainFrame;

//case 类
public class BigCaseDerby {
	//case属性
	String caseID;
	int events;
	Date start;
	Date end;
	long duration;
	long active;
	List<String> activities;
	List<String> eventList;
	int variantID;

	public BigCaseDerby() {
		// TODO Auto-generated constructor stub
		caseID = "";
		events = 0;
		start = null;
		end = null;
		duration = 0;
		active = 0;
		activities = new ArrayList<String>();
		eventList = new ArrayList<String>();
		variantID = 0;
	}
	
	public BigCaseDerby(ResultSet resultSet) {
		// TODO Auto-generated constructor stub
    	try {
		    caseID = resultSet.getString("caseID");
		    events = resultSet.getInt("eventsCount");
		    start = new Date(resultSet.getLong("startDate"));
		    end = new Date(resultSet.getLong("endDate"));
		    duration = resultSet.getLong("duration");
		    active = resultSet.getLong("active");
			activities = Arrays.asList(resultSet.getString("activitiesList").split("\t"));
			eventList = Arrays.asList(resultSet.getString("eventList").split("\t"));
			variantID = resultSet.getInt("variantID");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public BigCaseDerby(String id) {
		// TODO Auto-generated constructor stub
		try {
			String sql = "select * from caseCollection where caseID = '" + id + "'" + " OFFSET 0 ROWS FETCH NEXT 1 ROWS ONLY";
			PreparedStatement derbyStatement = MainFrame.derbyConnection.prepareStatement(sql);
         	ResultSet derbyResult = derbyStatement.executeQuery();
			
            if (derbyResult.next())
            {
    		    caseID = derbyResult.getString("caseID");
    		    events = derbyResult.getInt("eventsCount");
    		    start = new Date(derbyResult.getLong("startDate"));
    		    end = new Date(derbyResult.getLong("endDate"));
    		    duration = derbyResult.getLong("duration");
    		    active = derbyResult.getLong("active");
    			activities = Arrays.asList(derbyResult.getString("activitiesList").split("\t"));
    			eventList = Arrays.asList(derbyResult.getString("eventList").split("\t"));
    			variantID = derbyResult.getInt("variantID");
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
//			String sql = "insert into caseCollection values('" + caseID + "', " + events + ", " + start.getTime() + ", "+ end.getTime() + ", " + duration + ", " + active + ", '" + String.join("\t", activities.toArray(new String[]{})) + "', '" + String.join("\t", eventList.toArray(new String[]{})) + "', 0)";
//			PreparedStatement derbyStatement = MainFrame.derbyConnection.prepareStatement(sql);
//         	derbyStatement.executeUpdate();
//	        if (derbyStatement != null)
//	        	derbyStatement.close();
			derbyStatement.setString(1, caseID);
			derbyStatement.setLong(2, events);
			derbyStatement.setLong(3, start.getTime());
			derbyStatement.setLong(4, end.getTime());
			derbyStatement.setLong(5, duration);
			derbyStatement.setLong(6, active);
			derbyStatement.setString(7, String.join("\t", activities.toArray(new String[]{})));
			derbyStatement.setString(8, String.join("\t", eventList.toArray(new String[]{}))); 
			derbyStatement.addBatch();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void update(PreparedStatement derbyStatement){
		try {
//			String sql = "update caseCollection set variantID = " + variantID + " where caseID = '" + caseID + "'";
//			PreparedStatement derbyStatement = MainFrame.derbyConnection.prepareStatement(sql);
//         	derbyStatement.executeUpdate();
//	        if (derbyStatement != null)
//	        	derbyStatement.close();
			derbyStatement.setInt(1, variantID);
			derbyStatement.setString(2, caseID);
			derbyStatement.addBatch();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void setVariantID(int variantID) {
		this.variantID = variantID;
	}

	public int getVariantID() {
		return variantID;
	}
	
	public void setCase(String caseID) {
		this.caseID = caseID;
	}

	public String getCase() {
		return caseID;
	}

	public void addEvent(BigEventDerby c) {
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
	    eventList.add(c.getUUID());
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
	
	public BigEventDerby getEvent(int pos) {
		return new BigEventDerby(eventList.get(pos));
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
