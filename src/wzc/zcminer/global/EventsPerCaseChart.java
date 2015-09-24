package wzc.zcminer.global;

import java.util.ArrayList;
import java.util.List;

//EventsPerCaseChart图表类
public class EventsPerCaseChart {
    
    private class EventsPerCase {
        int caseCount;
        int eventCount;

        public EventsPerCase() {
            // TODO Auto-generated constructor stub
            caseCount = 0;
            eventCount = 0;
        }
        
        public int getCaseCount(){
            return caseCount;
        }
        
        public void setCaseCount(int caseCount){
            this.caseCount = caseCount;
        }
        
        public void addCaseCount(){
            caseCount++;
        }
        
        public int getEventCount(){
            return eventCount;
        }
        
        public void setEventCount(int eventCount){
            this.eventCount = eventCount;
        }
    }
    
    List<EventsPerCase> data;
    int size;

    public EventsPerCaseChart() {
        // TODO Auto-generated constructor stub
        data = new ArrayList<EventsPerCase>();
        size = 0;
    }

    public void addCases(Case mycase) {
        while(mycase.getSize() > data.size())
        {
            EventsPerCase e = new EventsPerCase();
            e.setEventCount(data.size() + 1);
            data.add(e);
            size++;
        }
        data.get(mycase.getSize() - 1).addCaseCount();
    }
    
    public int getSize(){
        return size;
    }
    
    public int getCaseCount(int index){
        return data.get(index).getCaseCount();
    }
    
    public int getEventCount(int index){
        return data.get(index).getEventCount();
    }
    
}