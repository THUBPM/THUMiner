package wzc.zcminer.global;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

//variant 类
public class BigVariant {
    
    private class ComparatorCaseDuration implements Comparator<Case>{
        public int compare(Case case1, Case case2) {
            if(case1.getDuration() < case2.getDuration()) {return 1;}
            else if (case1.getDuration() > case2.getDuration()) {return -1;}
            else {return 0;}
        }
    }
    
    private class ComparatorCaseID implements Comparator<Case>{
        public int compare(Case s1, Case s2) {
            int i1 = 0;
            int i2 = 0;
            while(i1 < s1.getCase().length() && i2 < s2.getCase().length())
            {
                if(s1.getCase().charAt(i1) >= '0' && s1.getCase().charAt(i1) <= '9' && s2.getCase().charAt(i2) >= '0' && s2.getCase().charAt(i2) <= '9')
                {
                    int j1 = i1;
                    int j2 = i2;
                    while(s1.getCase().charAt(j1) >= '0' && s1.getCase().charAt(j1) <= '9')
                    {
                        j1++;
                        if(j1 == s1.getCase().length())
                            break;
                    }
                    while(s2.getCase().charAt(j2) >= '0' && s2.getCase().charAt(j2) <= '9')
                    {
                        j2++;
                        if(j2 == s2.getCase().length())
                            break;
                    }
                    double n1 = Double.parseDouble(s1.getCase().substring(i1, j1));
                    double n2 = Double.parseDouble(s2.getCase().substring(i2, j2));
                    if(n1 > n2)
                        return 1;
                    else if(n1 < n2)
                        return -1;
                    else
                    {
                        i1 = j1; i2 = j2;
                    }
                }
                else
                {
                    if(s1.getCase().charAt(i1) > s2.getCase().charAt(i2))
                        return 1;
                    else if(s1.getCase().charAt(i1) < s2.getCase().charAt(i2))
                        return -1;
                    else
                    {
                        i1++; i2++;
                    }
                }
            }
            if(i1 == s1.getCase().length() && i2 == s2.getCase().length())
                return 0;
            else if(i1 == s1.getCase().length())
                return -1;
            else
                return 1;
        }
    }
    
	//variant属性
	String variantID;
	List<String> activities;
	CaseCollection caseCollection;
	long totalDuration;

	public BigVariant() {
		// TODO Auto-generated constructor stub
	    variantID = "";
	    activities = new ArrayList<String>();
		caseCollection = new CaseCollection();
		totalDuration = 0;
	}
	
	public void setVariant(String variantID) {
		this.variantID = variantID;
	}

	public String getVariant() {
		return variantID;
	}

	public void addCase(Case c) {
	    caseCollection.addCase(c);
	    totalDuration += c.getDuration(); 
	}
	
	public int getSize(){
		return caseCollection.getSize();
	}
	
	public Case getCase(int pos) {
		return caseCollection.getCase(pos);
	}

    public void addActivity(String c) {
        activities.add(c);
    }
    
    public int getActivitiesSize(){
        return activities.size();
    }
    
    public String getActivity(int pos) {
        return activities.get(pos);
    }
    
    public List<String> getActivities(){
        return activities;
    }
    
    public void setActivities(List<String> activities){
        this.activities.addAll(activities);
    }
    
    public long getMeanDuration() {
        return totalDuration / getSize();
    }
    
    public void sortByDuration(){
        ComparatorCaseDuration c = new ComparatorCaseDuration();
        caseCollection.cases.sort(c);
    }
    
    public void sortByID(){
        ComparatorCaseID c = new ComparatorCaseID();
        caseCollection.cases.sort(c);
    }
    
    public long getMedianDuration() {
        sortByDuration();
        int size = getSize();
        long result;
        if(size % 2 == 1)
        {
            result = getCase(size / 2).getDuration();
        }
        else
        {
            result = (getCase(size / 2).getDuration() + getCase(size / 2 - 1).getDuration()) / 2;
        }
        sortByID();
        return result;
    }
}
