package wzc.zcminer.global;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

//variant 类
public class BigVariant {
    public static boolean createDir(String destDirName) {
    	File dir = new File(destDirName);
    	if (dir.exists()) {
    		return false;
    	}
    	if (!destDirName.endsWith(File.separator)) {
    		destDirName = destDirName + File.separator;
    	}
    	if (dir.mkdirs()) {
    		return true;
    	} else {
    		return false;
    	}
    }
    
    public static boolean createFile(String destFileName) {
    	File file = new File(destFileName);
    	if(file.exists()) {
    		return false;
    	}
    	if (destFileName.endsWith(File.separator)) {
    		return false;
    	}
    	if(!file.getParentFile().exists()) {
    		if(!file.getParentFile().mkdirs()) {
    			return false;
    		}
    	}
    	try {
    		if (file.createNewFile()) {
    			return true;
    		} else {
    			return false;
    		}
    	} catch (IOException e) {
    		e.printStackTrace();
    		return false;
    	}
	}
    
	//variant属性
	int variantID;
	long totalDuration;
	long medianDuration;
	int size;
	int page;
	List<String> cases;
	List<String> caseFiles;
	List<String> activities;

	public BigVariant() {
		// TODO Auto-generated constructor stub
	    variantID = 0;
	    cases = new ArrayList<String>();
	    caseFiles = new ArrayList<String>();
	    activities = new ArrayList<String>();
		totalDuration = 0;
		medianDuration = 0;
		size = 0;
		page = 1;
	}
	
	public BigVariant(int fileName) {
		// TODO Auto-generated constructor stub
    	try {
			File file = new File("data_tmp//variant//" + fileName);
			InputStreamReader read = null;
			BufferedReader reader = null;
			try{
				read = new InputStreamReader(new FileInputStream(file));
				reader = new BufferedReader(read);
				String tempString = null;

				tempString = reader.readLine();
				variantID = Integer.parseInt(tempString);
				tempString = reader.readLine();
				totalDuration = Long.parseLong(tempString);
				tempString = reader.readLine();
				medianDuration = Long.parseLong(tempString);
				tempString = reader.readLine();
				size = Integer.parseInt(tempString);
				tempString = reader.readLine();
				if(tempString != null){
					activities = Arrays.asList(tempString.split("\t"));
				}else{
				    activities = new ArrayList<String>();
				}
				page = -1;
				
				reader.close();
				read.close();
			}catch(IOException e){
				e.printStackTrace();
			}finally{
				if(reader!=null)
				{
					try{
						reader.close();
						read.close();
					}catch(IOException e)
					{
						e.printStackTrace();
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void save() {
	    try{
	        String path = "data_tmp\\variant\\" + variantID;
	        createFile(path);
	        FileWriter fw = new FileWriter(path);
	        String str = variantID + "\n" + totalDuration + "\n" + medianDuration + "\n" + size + "\n" + String.join("\t", activities.toArray(new String[]{}));
	        fw.write(str);
	        fw.close();
	    }catch(IOException ex){
	        System.out.println(ex.getStackTrace());
	    }
	}
	
	public void saveCases() {
	    try{
	        String path = "data_tmp\\variant\\" + variantID + "case";
	        createDir(path);
	        path = "data_tmp\\variant\\" + variantID + "case\\" + page;
	        createFile(path);
	        FileWriter fw = new FileWriter(path);
	        String str = String.join("\t", cases.toArray(new String[]{})) + "\n" + String.join("\t", caseFiles.toArray(new String[]{}));
	        fw.write(str);
	        fw.close();
	    }catch(IOException ex){
	        System.out.println(ex.getStackTrace());
	    }
	}
	
	public void openCases(int page) {
    	try {
			File file = new File("data_tmp\\variant\\" + variantID + "case\\" + page);
			InputStreamReader read = null;
			BufferedReader reader = null;
			try{
				read = new InputStreamReader(new FileInputStream(file));
				reader = new BufferedReader(read);
				String tempString = null;

				tempString = reader.readLine();
				cases = Arrays.asList(tempString.split("\t"));
				tempString = reader.readLine();
				caseFiles = Arrays.asList(tempString.split("\t"));
				
				reader.close();
				read.close();
			}catch(IOException e){
				e.printStackTrace();
			}finally{
				if(reader!=null)
				{
					try{
						reader.close();
						read.close();
					}catch(IOException e)
					{
						e.printStackTrace();
					}
				}
			}
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

	public void addCase(BigCase c) {
		if(cases.size() == 1000){
			saveCases();
			page++;
			cases.clear();
			caseFiles.clear();
		}
		cases.add(c.getCase());
		caseFiles.add(c.getFileName());
	    totalDuration += c.getDuration();
	    size++;
	}
	
	public int getSize(){
		return size;
	}
	
	public BigCase getCase(int pos) {
		if(page != pos / 1000 + 1){
			page = pos / 1000 + 1;
			openCases(page);
		}
		return new BigCase(caseFiles.get(pos % 1000));
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
