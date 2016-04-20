package wzc.zcminer.global;

import java.util.ArrayList;
import java.util.List;

//CaseDurationChart图表类
public class CaseDurationChart {
    
    private class CaseDuration {
        int caseCount;
        long duration;

        public CaseDuration() {
            // TODO Auto-generated constructor stub
            caseCount = 0;
            duration = 0;
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
        
        public long getDuration(){
            return duration;
        }
        
        public void setDuration(long duration){
            this.duration = duration;
        }
    }
    
    List<CaseDuration> data;
    long duration;

    public CaseDurationChart() {
        // TODO Auto-generated constructor stub
        data = new ArrayList<CaseDuration>();
        duration = 0;
    }

    public void setDuration(long duration) {
        this.duration = duration;
        data = new ArrayList<CaseDuration>();
        for(int i = 1; i <= 100; i++)
        {
            CaseDuration c = new CaseDuration();
            c.setDuration(i * duration);
            c.setCaseCount(0);
            data.add(c);
        }
    }
    
    public void addCases(Case mycase) {
        int i = ((int) Math.ceil(mycase.getDuration() * 1.0 / duration)) - 1;
        if(i < 0)
            i = 0;
        if(i > 99)
            i = 99;
        data.get(i).addCaseCount();
    }
    
    public void addCases(BigCase mycase) {
        int i = ((int) Math.ceil(mycase.getDuration() * 1.0 / duration)) - 1;
        if(i < 0)
            i = 0;
        if(i > 99)
            i = 99;
        data.get(i).addCaseCount();
    }
    
    public int getSize(){
        return data.size();
    }
    
    public int getCaseCount(int index){
        return data.get(index).getCaseCount();
    }
    
    public long getDuration(int index){
        return data.get(index).getDuration();
    }
    
}