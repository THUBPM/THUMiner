package wzc.zcminer.global;

import java.util.ArrayList;
import java.util.List;

//MeanActivityDurationChart图表类
public class MeanActivityDurationChart {
    
    private class MeanActivityDuration {
        int caseCount;
        double duration;

        public MeanActivityDuration() {
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
        
        public double getDuration(){
            return duration;
        }
        
        public void setDuration(double duration){
            this.duration = duration;
        }
    }
    
    List<MeanActivityDuration> data;
    double duration;

    public MeanActivityDurationChart() {
        // TODO Auto-generated constructor stub
        data = new ArrayList<MeanActivityDuration>();
        duration = 0;
    }

    public void setDuration(double duration) {
        this.duration = duration;
        data = new ArrayList<MeanActivityDuration>();
        for(int i = 1; i <= 100; i++)
        {
            MeanActivityDuration c = new MeanActivityDuration();
            c.setDuration(i * duration);
            c.setCaseCount(0);
            data.add(c);
        }
    }
    
    public void addCases(Case mycase) {
        int i = ((int) Math.ceil(mycase.getMeanActiveTime() * 1.0 / duration)) - 1;
        if(i < 0)
            i = 0;
        if(i > 99)
            i = 99;
        data.get(i).addCaseCount();
    }
    
    public void addCases(BigCase mycase) {
        int i = ((int) Math.ceil(mycase.getMeanActiveTime() * 1.0 / duration)) - 1;
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
    
    public double getDuration(int index){
        return data.get(index).getDuration();
    }
    
}