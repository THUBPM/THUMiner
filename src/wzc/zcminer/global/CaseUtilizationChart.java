package wzc.zcminer.global;

import java.util.ArrayList;
import java.util.List;

//CaseUtilizationChart图表类
public class CaseUtilizationChart {
    
    private class CaseUtilization {
        int caseCount;
        int percentage;

        public CaseUtilization() {
            // TODO Auto-generated constructor stub
            caseCount = 0;
            percentage = 0;
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
        
        public int getPercentage(){
            return percentage;
        }
        
        public void setPercentage(int percentage){
            this.percentage = percentage;
        }
    }
    
    List<CaseUtilization> data;

    public CaseUtilizationChart() {
        // TODO Auto-generated constructor stub
        data = new ArrayList<CaseUtilization>();
        for(int i = 0; i <= 100; i++)
        {
            CaseUtilization c = new CaseUtilization();
            c.setPercentage(i);
            c.setCaseCount(0);
            data.add(c);
        }
    }
    
    public void addCases(Case mycase) {
        int i = (int) Math.floor(mycase.getActive(true) * 100);
        if(i > 100)
        	i = 100;
        data.get(i).addCaseCount();
    }
    
    public int getSize(){
        return data.size();
    }
    
    public int getCaseCount(int index){
        return data.get(index).getCaseCount();
    }
    
    public int getPercentage(int index){
        return data.get(index).getPercentage();
    }
    
}