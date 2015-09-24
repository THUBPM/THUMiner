package wzc.zcminer.global;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

//case集合类
public class CaseCollection {
    
    private class ComparatorCaseDuration implements Comparator<Case>{
        public int compare(Case case1, Case case2) {
            if(case1.getDuration() < case2.getDuration()) {return 1;}
            else if (case1.getDuration() > case2.getDuration()) {return -1;}
            else {return 0;}
        }
    }
    
    private class ComparatorCaseVariant implements Comparator<Case>{
        public int compare(Case case1, Case case2) {
            if(case1.getSize() > case2.getSize()) {return 1;}
            else if (case1.getSize() < case2.getSize()) {return -1;}
            else
            {
                if(case1.getActivities().toString().compareTo(case2.getActivities().toString()) > 0)
                {
                    return 1;
                }
                else if(case1.getActivities().toString().compareTo(case2.getActivities().toString()) < 0)
                {
                    return -1;
                }
                else
                {
                    return 0;
                }
            }
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
    
	List<Case> cases;
	long totalDuration;
//	Date start;
//	Date end;

	public CaseCollection() {
		// TODO Auto-generated constructor stub
	    cases = new ArrayList<Case>();
	    totalDuration = 0;
//	    start = null;
//	    end = null;
	}

	public void addCase(Case c) {
	    cases.add(c);
	}
	
	public void update() {
	    sortByID();
	    for(int i = 0; i < getSize(); i++)
	    {
	        Case c = getCase(i);
	        totalDuration += c.getDuration();
//	        if(start == null)
//	            start = c.getStart();
//	        else if(start.after(c.getStart()))
//	            start = c.getStart();
//	        if(end == null)
//	            end = c.getEnd();
//	        else if(end.before(c.getEnd()))
//	            end = c.getEnd();
	    }
	}
	
	public int getSize(){
		return cases.size();
	}
	
	public Date getStart(){
	    Date start = getCase(0).getStart();
	    for(int i = 1; i < getSize(); i++)
        {
            Case c = getCase(i);
            if(start.after(c.getStart()))
                start = c.getStart();
        }
	    return start;
	}
	
	public Date getEnd(){
        Date end = getCase(0).getEnd();
        for(int i = 1; i < getSize(); i++)
        {
            Case c = getCase(i);
            if(end.before(c.getEnd()))
                end = c.getEnd();
        }
        return end;
	}
	
    public long getMeanDuration() {
        return totalDuration / getSize();
    }
	
	public Case getCase(int pos) {
		return cases.get(pos);
	}
	
	public void setCase(int pos, Case c) {
	    cases.set(pos, c);
	}
	
	public void sortByDuration(){
        ComparatorCaseDuration c = new ComparatorCaseDuration();
        cases.sort(c);
    }
    
    public void sortByID(){
        ComparatorCaseID c = new ComparatorCaseID();
        cases.sort(c);
    }
    
    public void sortByVariant(){
        ComparatorCaseVariant c = new ComparatorCaseVariant();
        cases.sort(c);
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
