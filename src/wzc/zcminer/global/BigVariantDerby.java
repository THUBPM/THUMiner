package wzc.zcminer.global;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import wzc.zcminer.frame.MainFrame;

//variant 类
public class BigVariantDerby {
	//variant属性
	int variantID;
	long totalDuration;
	long medianDuration;
	int size;
	int page;
	List<String> activities;
	List<String> cases;

	public BigVariantDerby() {
		// TODO Auto-generated constructor stub
	    variantID = 0;
	    activities = new ArrayList<String>();
		totalDuration = 0;
		medianDuration = 0;
		size = 0;
		page = 1;
	    cases = new ArrayList<String>();
	}
	
	public BigVariantDerby(ResultSet resultSet) {
		// TODO Auto-generated constructor stub
    	try {
		    variantID = resultSet.getInt("variantID");
		    totalDuration = resultSet.getLong("totalDuration");
		    medianDuration = resultSet.getLong("medianDuration");
			size = resultSet.getInt("size");
		    activities = Arrays.asList(resultSet.getString("activitiesList").split("\t"));
			page = -1;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public BigVariantDerby(int id) {
		// TODO Auto-generated constructor stub
		try {
			String sql = "select * from variantCollection where variantID = " + id;
			PreparedStatement derbyStatement = MainFrame.derbyConnection.prepareStatement(sql);
         	ResultSet derbyResult = derbyStatement.executeQuery();
			
            if (derbyResult.next())
            {
    		    variantID = derbyResult.getInt("variantID");
    		    totalDuration = derbyResult.getLong("totalDuration");
    		    medianDuration = derbyResult.getLong("medianDuration");
    			size = derbyResult.getInt("size");
    		    activities = Arrays.asList(derbyResult.getString("activitiesList").split("\t"));
    			page = -1;
            }
         	
	        if (derbyResult != null)
	        	derbyResult.close();
	        if (derbyStatement != null)
	        	derbyStatement.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void insert() {
		try {
			String sql = "insert into variantCollection values(" + variantID + ", " + totalDuration + ", " + medianDuration + ", "+ size + ", '" + String.join("\t", activities.toArray(new String[]{})) + "')";
			PreparedStatement derbyStatement = MainFrame.derbyConnection.prepareStatement(sql);
         	derbyStatement.executeUpdate();
	        if (derbyStatement != null)
	        	derbyStatement.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void setVariant(int variantID) {
		this.variantID = variantID;
	}

	public int getVariant() {
		return variantID;
	}

	public void addCase(BigCaseDerby c) {
		c.setVariantID(variantID);
		c.update();
	    totalDuration += c.getDuration();
	    size++;
	}
	
	public int getSize(){
		return size;
	}
	
	public BigCaseDerby getCase(int pos) {		
		try {
			String sql = "select * from caseCollection where variantID = " + variantID;
			PreparedStatement derbyStatement = MainFrame.derbyConnection.prepareStatement(sql);
			derbyStatement.setMaxRows(pos + 2);
         	ResultSet derbyResult = derbyStatement.executeQuery();
         	BigCaseDerby c = null;
         	int i = 0;

            while (derbyResult.next())
            {
            	if (i == pos)
            		c = new BigCaseDerby(derbyResult);
            	i++;
            }
         	
	        if (derbyResult != null)
	        	derbyResult.close();
	        if (derbyStatement != null)
	        	derbyStatement.close();
	        
	        return c;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
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
    
    public void setMedianDuration(long medianDuration) {
    	this.medianDuration = medianDuration;
    }
    
    public long getMedianDuration() {
    	return medianDuration;
//        if(size % 2 == 1)
//        {
//        	medianDuration = getCase(size / 2).getDuration();
//        }
//        else
//        {
//        	medianDuration = (getCase(size / 2).getDuration() + getCase(size / 2 - 1).getDuration()) / 2;
//        }
    }
}
