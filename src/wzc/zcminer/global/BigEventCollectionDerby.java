package wzc.zcminer.global;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import wzc.zcminer.frame.MainFrame;

//event集合类
public class BigEventCollectionDerby {
	int size;

	public BigEventCollectionDerby() {
		// TODO Auto-generated constructor stub
		size = 0;
	}

	public void addEvent(BigEventDerby c) {
//		long startMili=System.currentTimeMillis();// 当前时间对应的毫秒数
		String uuid = UUID.randomUUID().toString();
		c.setUUID(uuid);
		c.insert();
	    size++;
//		long endMili=System.currentTimeMillis();
//		System.out.println("add " + c.getCase() + " 总耗时为："+(endMili-startMili)+"毫秒");
	}
	
	public int getSize(){
		return size;
	}
	
	public BigEventDerby getFirst(){
		try {
			String sql = "select * from eventCollection";
			MainFrame.derbyStatement = MainFrame.derbyConnection.prepareStatement(sql);
			MainFrame.derbyResult = MainFrame.derbyStatement.executeQuery();
            if (MainFrame.derbyResult.next())
            {
            	return new BigEventDerby(MainFrame.derbyResult);
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
	
	public BigEventDerby getNext(){
		try {
            if (MainFrame.derbyResult.next())
            {
            	return new BigEventDerby(MainFrame.derbyResult);
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
	
	public BigEventDerby getFirstEventID(){
		try {
			String sql = "select * from eventCollection order by caseID, startDate, endDate";
			MainFrame.derbyStatement = MainFrame.derbyConnection.prepareStatement(sql);
			MainFrame.derbyResult = MainFrame.derbyStatement.executeQuery();
            if (MainFrame.derbyResult.next())
            {
            	return new BigEventDerby(MainFrame.derbyResult);
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
	
	public BigEventDerby getNextEventID(){
		try {
            if (MainFrame.derbyResult.next())
            {
            	return new BigEventDerby(MainFrame.derbyResult);
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
	
	public BigEventDerby getFirstActivity(){
		try {
			String sql = "select * from eventCollection order by activity";
			MainFrame.derbyStatement = MainFrame.derbyConnection.prepareStatement(sql);
			MainFrame.derbyResult = MainFrame.derbyStatement.executeQuery();
            if (MainFrame.derbyResult.next())
            {
            	return new BigEventDerby(MainFrame.derbyResult);
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
	
	public BigEventDerby getNextActivity(){
		try {
            if (MainFrame.derbyResult.next())
            {
            	return new BigEventDerby(MainFrame.derbyResult);
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
	
	public BigEventDerby getFirstResource(){
		try {
			String sql = "select * from eventCollection order by resource";
			MainFrame.derbyStatement = MainFrame.derbyConnection.prepareStatement(sql);
			MainFrame.derbyResult = MainFrame.derbyStatement.executeQuery();
            if (MainFrame.derbyResult.next())
            {
            	return new BigEventDerby(MainFrame.derbyResult);
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
	
	public BigEventDerby getNextResource(){
		try {
            if (MainFrame.derbyResult.next())
            {
            	return new BigEventDerby(MainFrame.derbyResult);
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
}
