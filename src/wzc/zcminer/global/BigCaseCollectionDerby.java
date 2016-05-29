package wzc.zcminer.global;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import wzc.zcminer.frame.MainFrame;

//case集合类
public class BigCaseCollectionDerby {
	long totalDuration;
	long medianDuration;
	Date start;
	Date end;
	int size;

	public BigCaseCollectionDerby() {
		// TODO Auto-generated constructor stub
	    totalDuration = 0;
	    medianDuration = 0;
	    start = null;
	    end = null;
	    size = 0;
	}
	
	public void addCase(BigCaseDerby c, PreparedStatement derbyStatement) {
		c.insert(derbyStatement);
        
	    size++;
        totalDuration += c.getDuration();
        if(start == null)
            start = c.getStart();
        else if(start.after(c.getStart()))
            start = c.getStart();
        if(end == null)
            end = c.getEnd();
        else if(end.before(c.getEnd()))
            end = c.getEnd();
	}
	
	public int getSize(){
		return size;
	}
	
	public Date getStart(){
	    return start;
	}
	
	public Date getEnd(){
        return end;
	}
	
    public long getMeanDuration() {
        return totalDuration / getSize();
    }
	
	public void update() {
		int count = 0;
		BigCaseDerby c = getFirstDuration();
        if (c == null) return;
        
        if(getSize() % 2 == 1){
	        while (c != null) {
	        	if(count == getSize() / 2){
	        		medianDuration = c.getDuration();
	        		try {
	        	        if (MainFrame.derbyResult != null)
	        	        	MainFrame.derbyResult.close();
	        	        if (MainFrame.derbyStatement != null)
	        	        	MainFrame.derbyStatement.close();
	        		} catch (Exception ex) {
	        			ex.printStackTrace();
	        		}
	        		return;
	        	}
	        	c = getNextDuration();
	        	count++;
	        }
        }else{
	        while (c != null) {
	        	if(count == getSize() / 2 - 1){
	        		medianDuration = c.getDuration();
	        		c = getNextDuration();
	        		medianDuration = (medianDuration + c.getDuration()) / 2;
	        		try {
	        	        if (MainFrame.derbyResult != null)
	        	        	MainFrame.derbyResult.close();
	        	        if (MainFrame.derbyStatement != null)
	        	        	MainFrame.derbyStatement.close();
	        		} catch (Exception ex) {
	        			ex.printStackTrace();
	        		}
	        		return;
	        	}
	        	c = getNextDuration();
	        	count++;
	        }
        }
	}
    
    public long getMedianDuration() {
        return medianDuration;
    }

	public BigCaseDerby getFirstID(){
		try {
			String sql = "select * from caseCollection order by caseID";
			MainFrame.derbyStatement = MainFrame.derbyConnection.prepareStatement(sql);
			MainFrame.derbyResult = MainFrame.derbyStatement.executeQuery();
            if (MainFrame.derbyResult.next())
            {
            	return new BigCaseDerby(MainFrame.derbyResult);
            }
            else
            {
    	        if (MainFrame.derbyResult != null)
    	        	MainFrame.derbyResult.close();
    	        if (MainFrame.derbyStatement != null)
    	        	MainFrame.derbyStatement.close();
            	return null;
            }
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		try {
	        if (MainFrame.derbyResult != null)
	        	MainFrame.derbyResult.close();
	        if (MainFrame.derbyStatement != null)
	        	MainFrame.derbyStatement.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	public BigCaseDerby getNextID(){
		try {
            if (MainFrame.derbyResult.next())
            {
            	return new BigCaseDerby(MainFrame.derbyResult);
            }
            else
            {
    	        if (MainFrame.derbyResult != null)
    	        	MainFrame.derbyResult.close();
    	        if (MainFrame.derbyStatement != null)
    	        	MainFrame.derbyStatement.close();
            	return null;
            }
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		try {
	        if (MainFrame.derbyResult != null)
	        	MainFrame.derbyResult.close();
	        if (MainFrame.derbyStatement != null)
	        	MainFrame.derbyStatement.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	public BigCaseDerby getFirstDuration(){
		try {
			String sql = "select * from caseCollection order by duration";
			MainFrame.derbyStatement = MainFrame.derbyConnection.prepareStatement(sql);
			MainFrame.derbyResult = MainFrame.derbyStatement.executeQuery();
            if (MainFrame.derbyResult.next())
            {
            	return new BigCaseDerby(MainFrame.derbyResult);
            }
            else
            {
    	        if (MainFrame.derbyResult != null)
    	        	MainFrame.derbyResult.close();
    	        if (MainFrame.derbyStatement != null)
    	        	MainFrame.derbyStatement.close();
            	return null;
            }
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		try {
	        if (MainFrame.derbyResult != null)
	        	MainFrame.derbyResult.close();
	        if (MainFrame.derbyStatement != null)
	        	MainFrame.derbyStatement.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	public BigCaseDerby getNextDuration(){
		try {
            if (MainFrame.derbyResult.next())
            {
            	return new BigCaseDerby(MainFrame.derbyResult);
            }
            else
            {
    	        if (MainFrame.derbyResult != null)
    	        	MainFrame.derbyResult.close();
    	        if (MainFrame.derbyStatement != null)
    	        	MainFrame.derbyStatement.close();
            	return null;
            }
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		try {
	        if (MainFrame.derbyResult != null)
	        	MainFrame.derbyResult.close();
	        if (MainFrame.derbyStatement != null)
	        	MainFrame.derbyStatement.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	public BigCaseDerby getFirstCaseVariant(){
		try {
			String sql = "select * from caseCollection order by activitiesList, duration";
			MainFrame.derbyStatement = MainFrame.derbyConnection.prepareStatement(sql);
			MainFrame.derbyResult = MainFrame.derbyStatement.executeQuery();
            if (MainFrame.derbyResult.next())
            {
            	return new BigCaseDerby(MainFrame.derbyResult);
            }
            else
            {
    	        if (MainFrame.derbyResult != null)
    	        	MainFrame.derbyResult.close();
    	        if (MainFrame.derbyStatement != null)
    	        	MainFrame.derbyStatement.close();
            	return null;
            }
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		try {
	        if (MainFrame.derbyResult != null)
	        	MainFrame.derbyResult.close();
	        if (MainFrame.derbyStatement != null)
	        	MainFrame.derbyStatement.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	public BigCaseDerby getNextCaseVariant(){
		try {
            if (MainFrame.derbyResult.next())
            {
            	return new BigCaseDerby(MainFrame.derbyResult);
            }
            else
            {
    	        if (MainFrame.derbyResult != null)
    	        	MainFrame.derbyResult.close();
    	        if (MainFrame.derbyStatement != null)
    	        	MainFrame.derbyStatement.close();
            	return null;
            }
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		try {
	        if (MainFrame.derbyResult != null)
	        	MainFrame.derbyResult.close();
	        if (MainFrame.derbyStatement != null)
	        	MainFrame.derbyStatement.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	public BigCaseDerby getFirstCaseVariant(PreparedStatement derbyStatement, ResultSet derbyResult){
		try {
            if (derbyResult.next())
            {
            	return new BigCaseDerby(derbyResult);
            }
            else
            {
    	        if (derbyResult != null)
    	        	derbyResult.close();
    	        if (derbyStatement != null)
    	        	derbyStatement.close();
            	return null;
            }
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		try {
	        if (derbyResult != null)
	        	derbyResult.close();
	        if (derbyStatement != null)
	        	derbyStatement.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	public BigCaseDerby getNextCaseVariant(PreparedStatement derbyStatement, ResultSet derbyResult){
		try {
            if (derbyResult.next())
            {
            	return new BigCaseDerby(derbyResult);
            }
            else
            {
    	        if (derbyResult != null)
    	        	derbyResult.close();
    	        if (derbyStatement != null)
    	        	derbyStatement.close();
            	return null;
            }
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		try {
	        if (derbyResult != null)
	        	derbyResult.close();
	        if (derbyStatement != null)
	        	derbyStatement.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
}
