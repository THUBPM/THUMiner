package wzc.zcminer.global;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

//EventsOverTimeChart图表类
public class EventsOverTimeChart {
    
    private class EventsOverTime {
        int eventCount;
        Date date;

        public EventsOverTime() {
            // TODO Auto-generated constructor stub
            eventCount = 0;
            date = null;
        }
        
        public int getEventCount(){
            return eventCount;
        }
        
        public void setEventCount(int eventCount){
            this.eventCount = eventCount;
        }
        
        public void incEventCount(){
            eventCount++;
        }
        
        public void decEventCount(){
            eventCount--;
        }
        
        public Date getDate(){
            return date;
        }
        
        public void setDate(Date date){
            this.date = date;
        }
    }
    
    List<EventsOverTime> data;
    int size;
    long start;
    long end;
    long duration;

    public EventsOverTimeChart() {
        // TODO Auto-generated constructor stub
        data = null;
        size = 0;
        start = 0;
        end = 0;
        duration = 0;
    }

    public void addEventsOverTime(int eventCount, Date date) {
        if(eventCount == 1)
        {
            data.get((int)Math.floor((date.getTime() - start) * 1.0 / duration)).incEventCount();
        }
        else if(eventCount == -1)
        {
        	data.get((int)Math.ceil((date.getTime() - start) * 1.0 / duration)).decEventCount();
        }
    }
    
    public int getSize(){
        return size;
    }
    
    public int getEventCount(int pos) {
        return data.get(pos).getEventCount();
    }
    
    public Date getDate(int pos) {
        return data.get(pos).getDate();
    }
    
    public void init(long start, long end){
    	data = new ArrayList<EventsOverTime>();
    	this.start = start;
    	this.end = end;
        duration = (end - start) / 1000;
        
        for(int i = 0; i <= 1000; i++)
        {
            EventsOverTime a = new EventsOverTime();
            a.setEventCount(0);
            a.setDate(new Date(duration * i + start));
            data.add(a);
        }
        
        size = 1001;
    }
}