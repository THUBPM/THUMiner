package wzc.zcminer.global;

import java.util.ArrayList;
import java.util.HashMap;

import wzc.zcminer.frame.MainFrame;

//图完整信息类
public class GraphNet {
	HashMap<String, Integer> activityIDMap;
	public String[] activityNames;
	public int[][] activityQueFre; //边频率
	public int[] activityFre;
	public int[] activityCaseFre; //case频率
	public int[][] activityCaseQueFre; //边的case频率
	public int[] maxActivityRep;  //最大反复
	public long[] activityTime;    //活动时间
	public long[][] activityQueTime; 
	public long[] maxActivityTime;  //最大活动时间
	public long[][] maxActivityQueTime;
	public long[] minActivityTime;
	public long[][] minActivityQueTime;
	public int activityCount;   //活动数
	public int maxActivityFre;
	public int maxActivityQuiFre;
	public long beginTime;   //整个图的开始时间
	public long endTime; 	//整个图的结束时间
	public ArrayList<Integer> activityQueFreSort;  
	

	public GraphNet() {
		// TODO Auto-generated constructor stub
		activityIDMap = new HashMap<String, Integer>();
		activityQueFreSort = new ArrayList<Integer>();
		activityCount = 2;
		maxActivityFre = 0;
		maxActivityQuiFre = 0;
		beginTime = 0;
		endTime = 0;
	}

	//hash判断活动数
	public boolean activityExist(String activityName) {
		return activityIDMap.containsKey(activityName);

	}

	
	//各类活动操作
	public void addActivityFre(int pos) {
		activityFre[pos]++;
		if (activityFre[pos] > maxActivityFre) {
			maxActivityFre = activityFre[pos];
		}
	}

	public void addActivityQueFre(int parent, int children) {
		activityQueFre[parent][children]++;
		if (activityQueFre[parent][children] > maxActivityQuiFre) {
			maxActivityQuiFre = activityQueFre[parent][children];
		}
	}

	public void setActivityName(int id, String name) {
		activityNames[id] = name;
	}

	public int getActivityId(String activityString) {
		return activityIDMap.get(activityString);
	}

	public void addActivity(String activityName) {
		activityIDMap.put(activityName, activityCount);
		activityCount++;
	}

	public void addActivityTime(int id, long time) {
		activityTime[id] += time;
	}
	
	public void addActivityQueTime(int parent, int children, long time) {
		activityQueTime[parent][children] += time;
	}
	
	//分配动态内存
	public void setMemory() {
		activityNames = new String[activityCount];
		activityQueFre = new int[activityCount][activityCount];
		activityFre = new int[activityCount];
		activityCaseFre = new int[activityCount];
		activityCaseQueFre = new int[activityCount][activityCount];
		maxActivityRep = new int[activityCount];
		activityTime = new long[activityCount];
		activityQueTime = new long[activityCount][activityCount];
		maxActivityTime = new long[activityCount];
		maxActivityQueTime = new long[activityCount][activityCount];
		minActivityTime = new long[activityCount];
		minActivityQueTime = new long[activityCount][activityCount];
	}
	
	public void setTime(int id, long time) {
		if (maxActivityTime[id] < time){
			 maxActivityTime[id] = time;
		}
		if (minActivityTime[id] == 0 || minActivityTime[id] > time)
		{
			minActivityTime[id] = time;
		}	
	}
	
	public void setQueTime(int parent, int children, long time) {
		if (maxActivityQueTime[parent][children] < time){
			maxActivityQueTime[parent][children]  = time;
		}
		if (minActivityQueTime[parent][children] == 0 || minActivityQueTime[parent][children] > time){
			minActivityQueTime[parent][children] = time;
		}
	}
	
	public void setBeginTime(long time){
		if (time < beginTime || beginTime == 0 ){
			beginTime = time;
		}
	}
	
	public void setEndTime(long time){
		if (time > endTime){
			endTime = time;
		}
	}
	
	public int getMaxActivityFre() {
		return maxActivityFre;
	}

	public int getMaxActivityQueFre() {
		return maxActivityQuiFre;
	}
	
	

}
