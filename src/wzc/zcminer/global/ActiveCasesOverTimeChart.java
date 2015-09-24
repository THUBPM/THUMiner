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
    
    private class ComparatorDate implements Comparator<ActiveCasesOverTime>{
        public int compare(ActiveCasesOverTime date1, ActiveCasesOverTime date2) {
            if(date1.getDate().after(date2.getDate())) {return 1;}
            else if (date1.getDate().before(date2.getDate())){return -1;}
            else {return 0;}
        }
    }
    
    List<ActiveCasesOverTime> data;
    int size;

    public ActiveCasesOverTimeChart() {
        // TODO Auto-generated constructor stub
        data = new ArrayList<ActiveCasesOverTime>();
        size = 0;
    }

    public void addActiveCasesOverTime(int caseCount, Date date) {
        ActiveCasesOverTime a = new ActiveCasesOverTime();
        a.setCaseCount(caseCount);
        a.setDate(date);
        size++;
        data.add(a);
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
    
    public void setCaseCount(int pos, int caseCount) {
        data.get(pos).setCaseCount(caseCount);
    }
    
    public void setDate(int pos, Date date) {
        data.get(pos).setDate(date);
    }
        
    public void sort() {
        ComparatorDate c = new ComparatorDate();
        data.sort(c);
    }

    public void merge() {
        List<ActiveCasesOverTime> temp = new ArrayList<ActiveCasesOverTime>();
        long start = getDate(0).getTime();
        long end = getDate(getSize() - 1).getTime();
        long duration = (end - start) / 1000;
        
        for(int i = 0; i <= 1000; i++)
        {
            ActiveCasesOverTime a = new ActiveCasesOverTime();
            a.setCaseCount(0);
            a.setDate(new Date(duration * i + start));
            temp.add(a);
        }
        
        for(int i = 0; i < getSize(); i++)
        {
            ActiveCasesOverTime a = data.get(i);
            if(a.getCaseCount() == 1)
            {
                temp.get((int)Math.floor((a.getDate().getTime() - start) * 1.0 / duration)).incCaseCount();
            }
            else if(a.getCaseCount() == -1)
            {
                temp.get((int)Math.ceil((a.getDate().getTime() - start) * 1.0 / duration)).decCaseCount();
            }
        }
        
        data = temp;
        size = 1001;
    }
    
}