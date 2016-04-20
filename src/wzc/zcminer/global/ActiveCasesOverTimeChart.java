package wzc.zcminer.global;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

//ActiveCasesOverTime图表类
public class ActiveCasesOverTimeChart {
    
    private class ActiveCasesOverTime {
        int caseCount;
        Date date;

        public ActiveCasesOverTime() {
            // TODO Auto-generated constructor stub
            caseCount = 0;
            date = null;
        }
        
        public int getCaseCount(){
            return caseCount;
        }
        
        public void setCaseCount(int caseCount){
            this.caseCount = caseCount;
        }
        
        public void incCaseCount(){
            caseCount++;
        }
        
        public void decCaseCount(){
            caseCount--;
        }
        
        public Date getDate(){
            return date;
        }
        
        public void setDate(Date date){
            this.date = date;
        }
    }
    
    List<ActiveCasesOverTime> data;
    int size;
    long start;
    long end;
    long duration;

    public ActiveCasesOverTimeChart() {
        // TODO Auto-generated constructor stub
        data = null;
        size = 0;
        start = 0;
        end = 0;
        duration = 0;
    }

    public void addActiveCasesOverTime(int caseCount, Date date) {
        if(caseCount == 1)
        {
            data.get((int)Math.floor((date.getTime() - start) * 1.0 / duration)).incCaseCount();
        }
        else if(caseCount == -1)
        {
        	data.get((int)Math.ceil((date.getTime() - start) * 1.0 / duration)).decCaseCount();
        }
    }
    
    public int getSize(){
        return size;
    }
    
    public int getCaseCount(int pos) {
        return data.get(pos).getCaseCount();
    }
    
    public Date getDate(int pos) {
        return data.get(pos).getDate();
    }

    public void init(long start, long end){
    	data = new ArrayList<ActiveCasesOverTime>();
    	this.start = start;
    	this.end = end;
        duration = (end - start) / 1000;
        
        for(int i = 0; i <= 1000; i++)
        {
        	ActiveCasesOverTime a = new ActiveCasesOverTime();
            a.setCaseCount(0);
            a.setDate(new Date(duration * i + start));
            data.add(a);
        }
        
        size = 1001;
    }    
}