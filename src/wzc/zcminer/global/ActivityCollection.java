package wzc.zcminer.global;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

//activty集合类
public class ActivityCollection {
    
    private class ComparatorActivityFrequency implements Comparator<Activity>{
        public int compare(Activity activity1, Activity activity2) {
            if(activity1.getFrequency() < activity2.getFrequency()) {return 1;}
            else if (activity1.getFrequency() > activity2.getFrequency()) {return -1;}
            else {return 0;}
        }
    }
    
    private class ComparatorActivityMedianDuration implements Comparator<Activity>{
        public int compare(Activity activity1, Activity activity2) {
            if(activity1.getMedianDuration() < activity2.getMedianDuration()) {return 1;}
            else if (activity1.getMedianDuration() > activity2.getMedianDuration()) {return -1;}
            else {return 0;}
        }
    }
    
    private class ComparatorActivityMeanDuration implements Comparator<Activity>{
        public int compare(Activity activity1, Activity activity2) {
            if(activity1.getMeanDuration() < activity2.getMeanDuration()) {return 1;}
            else if (activity1.getMeanDuration() > activity2.getMeanDuration()) {return -1;}
            else {return 0;}
        }
    }
    
    private class ComparatorActivityDurationRange implements Comparator<Activity>{
        public int compare(Activity activity1, Activity activity2) {
            if(activity1.getDurationRange() < activity2.getDurationRange()) {return 1;}
            else if (activity1.getDurationRange() > activity2.getDurationRange()) {return -1;}
            else {return 0;}
        }
    }
    
    private class ComparatorActivityAggregateDuration implements Comparator<Activity>{
        public int compare(Activity activity1, Activity activity2) {
            if(activity1.getAggregateDuration() < activity2.getAggregateDuration()) {return 1;}
            else if (activity1.getAggregateDuration() > activity2.getAggregateDuration()) {return -1;}
            else {return 0;}
        }
    }
    
	List<Activity> activities;
	int first;
	int last;
	int size;

	public ActivityCollection() {
		// TODO Auto-generated constructor stub
	    activities = new ArrayList<Activity>();
	    first = 0;
	    last = 0;
	    size = 0;
	}

	public void addActivity(Activity a) {
	    activities.add(a);
	    size++;
	}
	
	public int getSize(){
		return size;
	}
	
    public int getFirst(){
        return first;
    }
   
    public int getLast(){
        return last;
    }
    
    public int getMinimalFrequency(){
        return getActivity(getSize() - 1).getFrequency();
    }
    
    public int getMaximalFrequency(){
        return getActivity(0).getFrequency();
    }
    
    public double getMeanFrequency(){
        int totalFrequency = 0;
        for(int i = 0; i < getSize(); i++)
        {
            totalFrequency += getActivity(i).getFrequency();
        }
        return totalFrequency * 1.0 / getSize();
    }
    
    public double getMedianFrequency(){
        double result;
        if(getSize() % 2 == 1)
        {
            result = getActivity(getSize() / 2).getFrequency();
        }
        else
        {
            result = (getActivity(getSize() / 2).getFrequency() + getActivity(getSize() / 2 - 1).getFrequency()) * 1.0 / 2;
        }
        return result;
    }
	
    public double getFrequencyStdDeviation(){
        double meanFrequency = getMeanFrequency();
        double total = 0;
        for(int i = 0; i < getSize(); i++)
        {
            total += Math.pow(getActivity(i).getFrequency() - meanFrequency, 2);
        }
        return Math.sqrt(total / (getSize() - 1));
    }
    
	public Activity getActivity(int pos) {
		return activities.get(pos);
	}
	
	public Activity getActivity(String activity) {
	    for(int i = 0; i < getSize(); i++)
	    {
	        if(activities.get(i).getActivity().equals(activity))
	        {
	            return activities.get(i);
	        }
	    }
	    return null;
	}
	
    public void sortAndMerge() {
        sortByFrequency();
        
        for(int i = 0; i < getSize(); i++)
        {
            Activity a = getActivity(i);
            if(a.getFirst())
                first++;
            if(a.getLast())
                last++;
        }
    }

    public void sortByFrequency() {
        ComparatorActivityFrequency c = new ComparatorActivityFrequency();
        activities.sort(c);
    }
    
    public void sortByMedianDuration() {
        ComparatorActivityMedianDuration c = new ComparatorActivityMedianDuration();
        activities.sort(c);
    }
    
    public void sortByMeanDuration() {
        ComparatorActivityMeanDuration c = new ComparatorActivityMeanDuration();
        activities.sort(c);
    }
    
    public void sortByDurationRange() {
        ComparatorActivityDurationRange c = new ComparatorActivityDurationRange();
        activities.sort(c);
    }
    
    public void sortByAggregateDuration() {
        ComparatorActivityAggregateDuration c = new ComparatorActivityAggregateDuration();
        activities.sort(c);
    }
}
