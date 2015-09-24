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
    
    private class ComparatorDate implements Comparator<EventsOverTime>{
        public int compare(EventsOverTime date1, EventsOverTime date2) {
            if(date1.getDate().after(date2.getDate())) {return 1;}
            else if (date1.getDate().before(date2.getDate())){return -1;}
            else {return 0;}
        }
    }
    
    List<EventsOverTime> data;
    int size;

    public EventsOverTimeChart() {
        // TODO Auto-generated constructor stub
        data = new ArrayList<EventsOverTime>();
        size = 0;
    }

    public void addEventsOverTime(int eventCount, Date date) {
        EventsOverTime a = new EventsOverTime();
        a.setEventCount(eventCount);
        a.setDate(date);
        size++;
        data.add(a);
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
    
    public void setEventCount(int pos, int eventCount) {
        data.get(pos).setEventCount(eventCount);
    }
    
    public void setDate(int pos, Date date) {
        data.get(pos).setDate(date);
    }
        
    public void sort() {
        ComparatorDate c = new ComparatorDate();
        data.sort(c);
    }

    public void merge() {
        List<EventsOverTime> temp = new ArrayList<EventsOverTime>();
        long start = getDate(0).getTime();
        long end = getDate(getSize() - 1).getTime();
        long duration = (end - start) / 1000;
        
        for(int i = 0; i <= 1000; i++)
        {
            EventsOverTime a = new EventsOverTime();
            a.setEventCount(0);
            a.setDate(new Date(duration * i + start));
            temp.add(a);
        }
        
        for(int i = 0; i < getSize(); i++)
        {
            EventsOverTime a = data.get(i);
            if(a.getEventCount() == 1)
            {
                temp.get((int)Math.floor((a.getDate().getTime() - start) * 1.0 / duration)).incEventCount();
            }
            else if(a.getEventCount() == -1)
            {
                temp.get((int)Math.ceil((a.getDate().getTime() - start) * 1.0 / duration)).decEventCount();
            }
        }
        
        data = temp;
        size = 1001;
    }
    
}