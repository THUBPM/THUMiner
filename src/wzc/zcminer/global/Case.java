package wzc.zcminer.global;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//case 类
public class Case {
	//case属性
	String caseID;
	int events;
	Date start;
	Date end;
	long duration;
	long active;	
	List<String> activities;
	EventCollection eventCollection;

	public Case() {
		// TODO Auto-generated constructor stub
		caseID = "";
		events = 0;
		start = null;
		duration = 0;
		active = 0;
		activities = new ArrayList<String>();
		eventCollection = new EventCollection();
	}
	
	public void setCase(String caseID) {
		this.caseID = caseID;
	}

	public String getCase() {
		return caseID;
	}

	public void addEvent(Event c) {
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
	    eventCollection.addEvent(c);
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
	
	public Event getEvent(int pos) {
		return eventCollection.getEvent(pos);
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
