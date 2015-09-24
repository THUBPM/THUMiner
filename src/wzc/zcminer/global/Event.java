package wzc.zcminer.global;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

//event 类
public class Event {
	//event属性
	String caseID;
	String activity;
	//String startDateString;
	//String endDateString;
	String resourceString;
    Date startDate;
	Date endDate;
	boolean first;
	boolean last;

	public Event() {
		// TODO Auto-generated constructor stub
	    caseID = "";
	    activity = "";
	    resourceString = "";
		//startDateString = "";
		//endDateString = "";
		startDate = null;
		endDate = null;
		first = false;
		last = false;
	}
	
    public void setFirst() {
        first = true;
    }
    
    public void setLast() {
        last = true;
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
			try {
				startDate = sdf.parse(dateString);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			//this.endDateString = dateString;
			try {
				endDate = sdf.parse(dateString);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
