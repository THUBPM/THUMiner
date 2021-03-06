package wzc.zcminer.global;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

//activity 类
public class Activity {
    
    private class ComparatorLong implements Comparator<Long>{
        public int compare(Long l1, Long l2) {
            if(l1 < l2) {return 1;}
            else if (l1 > l2) {return -1;}
            else {return 0;}
        }
    }
    
	//activity属性
	String activity;
	int frequency;
	List<Long> duration;
	long aggregateDuration;
	boolean first;
	boolean last;

	public Activity() {
		// TODO Auto-generated constructor stub
	    activity = "";
	    frequency = 0;
	    duration = new ArrayList<Long>();
	    aggregateDuration = 0;
	    first = false;
	    last = false;
	}
	
	public void setActivity(String activity) {
		this.activity = activity;
	}

	public String getActivity() {
		return activity;
	}

	public void addFrequency() {
	    frequency++;
	}

	public int getFrequency() {
		return frequency;
	}
	
    public long getAggregateDuration() {
        return aggregateDuration;
    }

	public void addDuration(long duration) {
		this.duration.add(duration);
		aggregateDuration += duration;
	}

	public long getMeanDuration() {
		return aggregateDuration / frequency;
	}
	
	public long getMedianDuration() {
        long result;
        if(frequency % 2 == 1)
        {
            result = duration.get(frequency / 2);
        }
        else
        {
            result = (duration.get(frequency / 2) + duration.get(frequency / 2 - 1)) / 2;
        }
        return result;
    }
	
    public long getDurationRange() {
        return duration.get(0);
    }

    public void sort(){
        ComparatorLong c = new ComparatorLong();
        duration.sort(c);
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
    
}
