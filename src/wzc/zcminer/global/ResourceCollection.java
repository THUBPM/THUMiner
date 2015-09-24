package wzc.zcminer.global;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

//resource集合类
public class ResourceCollection {
    
    private class ComparatorResourceFrequency implements Comparator<Resource>{
        public int compare(Resource resource1, Resource resource2) {
            if(resource1.getFrequency() < resource2.getFrequency()) {return 1;}
            else if (resource1.getFrequency() > resource2.getFrequency()) {return -1;}
            else {return 0;}
        }
    }
    
    private class ComparatorResourceMedianDuration implements Comparator<Resource>{
        public int compare(Resource resource1, Resource resource2) {
            if(resource1.getMedianDuration() < resource2.getMedianDuration()) {return 1;}
            else if (resource1.getMedianDuration() > resource2.getMedianDuration()) {return -1;}
            else {return 0;}
        }
    }
    
    private class ComparatorResourceMeanDuration implements Comparator<Resource>{
        public int compare(Resource resource1, Resource resource2) {
            if(resource1.getMeanDuration() < resource2.getMeanDuration()) {return 1;}
            else if (resource1.getMeanDuration() > resource2.getMeanDuration()) {return -1;}
            else {return 0;}
        }
    }
    
    private class ComparatorResourceDurationRange implements Comparator<Resource>{
        public int compare(Resource resource1, Resource resource2) {
            if(resource1.getDurationRange() < resource2.getDurationRange()) {return 1;}
            else if (resource1.getDurationRange() > resource2.getDurationRange()) {return -1;}
            else {return 0;}
        }
    }
    
    private class ComparatorResourceAggregateDuration implements Comparator<Resource>{
        public int compare(Resource resource1, Resource resource2) {
            if(resource1.getAggregateDuration() < resource2.getAggregateDuration()) {return 1;}
            else if (resource1.getAggregateDuration() > resource2.getAggregateDuration()) {return -1;}
            else {return 0;}
        }
    }
    
	List<Resource> resources;
	int first;
	int last;
	int size;

	public ResourceCollection() {
		// TODO Auto-generated constructor stub
	    resources = new ArrayList<Resource>();
	    first = 0;
	    last = 0;
	    size = 0;
	}

	public void addResource(Resource a) {
	    resources.add(a);
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
        return getResource(getSize() - 1).getFrequency();
    }
    
    public int getMaximalFrequency(){
        return getResource(0).getFrequency();
    }
    
    public double getMeanFrequency(){
        int totalFrequency = 0;
        for(int i = 0; i < getSize(); i++)
        {
            totalFrequency += getResource(i).getFrequency();
        }
        return totalFrequency * 1.0 / getSize();
    }
    
    public double getMedianFrequency(){
        double result;
        if(getSize() % 2 == 1)
        {
            result = getResource(getSize() / 2).getFrequency();
        }
        else
        {
            result = (getResource(getSize() / 2).getFrequency() + getResource(getSize() / 2 - 1).getFrequency()) * 1.0 / 2;
        }
        return result;
    }
	
    public double getFrequencyStdDeviation(){
        double meanFrequency = getMeanFrequency();
        double total = 0;
        for(int i = 0; i < getSize(); i++)
        {
            total += Math.pow(getResource(i).getFrequency() - meanFrequency, 2);
        }
        return Math.sqrt(total / (getSize() - 1));
    }
    
	public Resource getResource(int pos) {
		return resources.get(pos);
	}
	
	public Resource getResource(String resource) {
	    for(int i = 0; i < getSize(); i++)
	    {
	        if(resources.get(i).getResource().equals(resource))
	        {
	            return resources.get(i);
	        }
	    }
	    return null;
	}
	
    public void sortAndMerge() {
        sortByFrequency();
        
        for(int i = 0; i < getSize(); i++)
        {
            Resource a = getResource(i);
            if(a.getFirst())
                first++;
            if(a.getLast())
                last++;
        }
    }

    public void sortByFrequency() {
        ComparatorResourceFrequency c = new ComparatorResourceFrequency();
        resources.sort(c);
    }
    
    public void sortByMedianDuration() {
        ComparatorResourceMedianDuration c = new ComparatorResourceMedianDuration();
        resources.sort(c);
    }
    
    public void sortByMeanDuration() {
        ComparatorResourceMeanDuration c = new ComparatorResourceMeanDuration();
        resources.sort(c);
    }
    
    public void sortByDurationRange() {
        ComparatorResourceDurationRange c = new ComparatorResourceDurationRange();
        resources.sort(c);
    }
    
    public void sortByAggregateDuration() {
        ComparatorResourceAggregateDuration c = new ComparatorResourceAggregateDuration();
        resources.sort(c);
    }
}
