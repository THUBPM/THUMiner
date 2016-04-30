package wzc.zcminer.global;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import wzc.zcminer.frame.MainFrame;

//case集合类
public class BigCaseCollection {
	public void rightRotateID(String rootID) {
		BigCase tempFile = new BigCase(rootID);
		
		BigCase leftChild = null;
		BigCase parent = null;
		if(!tempFile.getLeftChildID().equals("")){
			leftChild = new BigCase(tempFile.getLeftChildID());
		}
		if(!tempFile.getParentID().equals("")){
			parent = new BigCase(tempFile.getParentID());
		}
		
		BigCase leftRightChild = null;
		int leftRightHeight = 0;
		if(!leftChild.getRightChildID().equals("")){
			leftRightChild = new BigCase(leftChild.getRightChildID());
			leftRightHeight = leftRightChild.getHeightID();
		}
		
		leftChild.setParentID(tempFile.getParentID());
		leftChild.setWhichChildID(tempFile.getWhichChildID());
		if(tempFile.getParentID().equals(""))
			rootCaseID = tempFile.getLeftChildID();
		else{
			if(tempFile.getWhichChildID() == 0)
				parent.setLeftChildID(tempFile.getLeftChildID());
			else
				parent.setRightChildID(tempFile.getLeftChildID());
			parent.save(tempFile.getParentID());
		}
		tempFile.setParentID(tempFile.getLeftChildID());
		tempFile.setWhichChildID(1);
		tempFile.setLeftChildID(leftChild.getRightChildID());
		tempFile.setHeightID(leftRightHeight + 1);
		if(leftRightChild != null){
			leftRightChild.setParentID(rootID);
			leftRightChild.setWhichChildID(0);
			leftRightChild.save(tempFile.getLeftChildID());
		}
		leftChild.setRightChildID(rootID);
		leftChild.setHeightID(leftRightHeight + 2);
		
		tempFile.save(rootID);
		leftChild.save(tempFile.getParentID());
	}
	
	public void leftRotateID(String rootID) {
		BigCase tempFile = new BigCase(rootID);
		
		BigCase rightChild = null;
		BigCase parent = null;
		if(!tempFile.getRightChildID().equals("")){
			rightChild = new BigCase(tempFile.getRightChildID());
		}
		if(!tempFile.getParentID().equals("")){
			parent = new BigCase(tempFile.getParentID());
		}
		
		BigCase rightLeftChild = null;
		int rightLeftHeight = 0;
		if(!rightChild.getLeftChildID().equals("")){
			rightLeftChild = new BigCase(rightChild.getLeftChildID());
			rightLeftHeight = rightLeftChild.getHeightID();
		}

		rightChild.setParentID(tempFile.getParentID());
		rightChild.setWhichChildID(tempFile.getWhichChildID());
		if(tempFile.getParentID().equals(""))
			rootCaseID = tempFile.getRightChildID();
		else{
			if(tempFile.getWhichChildID() == 0)
				parent.setLeftChildID(tempFile.getRightChildID());
			else
				parent.setRightChildID(tempFile.getRightChildID());
			parent.save(tempFile.getParentID());
		}
		tempFile.setParentID(tempFile.getRightChildID());
		tempFile.setWhichChildID(0);
		tempFile.setRightChildID(rightChild.getLeftChildID());
		tempFile.setHeightID(rightLeftHeight + 1);
		if(rightLeftChild != null){
			rightLeftChild.setParentID(rootID);
			rightLeftChild.setWhichChildID(1);
			rightLeftChild.save(tempFile.getRightChildID());
		}
		rightChild.setLeftChildID(rootID);
		rightChild.setHeightID(rightLeftHeight + 2);
		
		tempFile.save(rootID);
		rightChild.save(tempFile.getParentID());
	}
	
	public void rightRotateDuration(String rootID) {
		BigCase tempFile = new BigCase(rootID);
		
		BigCase leftChild = null;
		BigCase parent = null;
		if(!tempFile.getLeftChildDuration().equals("")){
			leftChild = new BigCase(tempFile.getLeftChildDuration());
		}
		if(!tempFile.getParentDuration().equals("")){
			parent = new BigCase(tempFile.getParentDuration());
		}
		
		BigCase leftRightChild = null;
		int leftRightHeight = 0;
		if(!leftChild.getRightChildDuration().equals("")){
			leftRightChild = new BigCase(leftChild.getRightChildDuration());
			leftRightHeight = leftRightChild.getHeightDuration();
		}
		
		leftChild.setParentDuration(tempFile.getParentDuration());
		leftChild.setWhichChildDuration(tempFile.getWhichChildDuration());
		if(tempFile.getParentDuration().equals(""))
			rootDuration = tempFile.getLeftChildDuration();
		else{
			if(tempFile.getWhichChildDuration() == 0)
				parent.setLeftChildDuration(tempFile.getLeftChildDuration());
			else
				parent.setRightChildDuration(tempFile.getLeftChildDuration());
			parent.save(tempFile.getParentDuration());
		}
		tempFile.setParentDuration(tempFile.getLeftChildDuration());
		tempFile.setWhichChildDuration(1);
		tempFile.setLeftChildDuration(leftChild.getRightChildDuration());
		tempFile.setHeightDuration(leftRightHeight + 1);
		if(leftRightChild != null){
			leftRightChild.setParentDuration(rootID);
			leftRightChild.setWhichChildDuration(0);
			leftRightChild.save(tempFile.getLeftChildDuration());
		}
		leftChild.setRightChildDuration(rootID);
		leftChild.setHeightDuration(leftRightHeight + 2);
		
		tempFile.save(rootID);
		leftChild.save(tempFile.getParentDuration());
	}
	
	public void leftRotateDuration(String rootID) {
		BigCase tempFile = new BigCase(rootID);
		
		BigCase rightChild = null;
		BigCase parent = null;
		if(!tempFile.getRightChildDuration().equals("")){
			rightChild = new BigCase(tempFile.getRightChildDuration());
		}
		if(!tempFile.getParentDuration().equals("")){
			parent = new BigCase(tempFile.getParentDuration());
		}
		
		BigCase rightLeftChild = null;
		int rightLeftHeight = 0;
		if(!rightChild.getLeftChildDuration().equals("")){
			rightLeftChild = new BigCase(rightChild.getLeftChildDuration());
			rightLeftHeight = rightLeftChild.getHeightDuration();
		}

		rightChild.setParentDuration(tempFile.getParentDuration());
		rightChild.setWhichChildDuration(tempFile.getWhichChildDuration());
		if(tempFile.getParentDuration().equals(""))
			rootDuration = tempFile.getRightChildDuration();
		else{
			if(tempFile.getWhichChildDuration() == 0)
				parent.setLeftChildDuration(tempFile.getRightChildDuration());
			else
				parent.setRightChildDuration(tempFile.getRightChildDuration());
			parent.save(tempFile.getParentDuration());
		}
		tempFile.setParentDuration(tempFile.getRightChildDuration());
		tempFile.setWhichChildDuration(0);
		tempFile.setRightChildDuration(rightChild.getLeftChildDuration());
		tempFile.setHeightDuration(rightLeftHeight + 1);
		if(rightLeftChild != null){
			rightLeftChild.setParentDuration(rootID);
			rightLeftChild.setWhichChildDuration(1);
			rightLeftChild.save(tempFile.getRightChildDuration());
		}
		rightChild.setLeftChildDuration(rootID);
		rightChild.setHeightDuration(rightLeftHeight + 2);
		
		tempFile.save(rootID);
		rightChild.save(tempFile.getParentDuration());
	}
	
	public void rightRotateCaseVariant(String rootID) {
		BigCase tempFile = new BigCase(rootID);
		
		BigCase leftChild = null;
		BigCase parent = null;
		if(!tempFile.getLeftChildCaseVariant().equals("")){
			leftChild = new BigCase(tempFile.getLeftChildCaseVariant());
		}
		if(!tempFile.getParentCaseVariant().equals("")){
			parent = new BigCase(tempFile.getParentCaseVariant());
		}
		
		BigCase leftRightChild = null;
		int leftRightHeight = 0;
		if(!leftChild.getRightChildCaseVariant().equals("")){
			leftRightChild = new BigCase(leftChild.getRightChildCaseVariant());
			leftRightHeight = leftRightChild.getHeightCaseVariant();
		}
		
		leftChild.setParentCaseVariant(tempFile.getParentCaseVariant());
		leftChild.setWhichChildCaseVariant(tempFile.getWhichChildCaseVariant());
		if(tempFile.getParentCaseVariant().equals(""))
			rootCaseVariant = tempFile.getLeftChildCaseVariant();
		else{
			if(tempFile.getWhichChildCaseVariant() == 0)
				parent.setLeftChildCaseVariant(tempFile.getLeftChildCaseVariant());
			else
				parent.setRightChildCaseVariant(tempFile.getLeftChildCaseVariant());
			parent.save(tempFile.getParentCaseVariant());
		}
		tempFile.setParentCaseVariant(tempFile.getLeftChildCaseVariant());
		tempFile.setWhichChildCaseVariant(1);
		tempFile.setLeftChildCaseVariant(leftChild.getRightChildCaseVariant());
		tempFile.setHeightCaseVariant(leftRightHeight + 1);
		if(leftRightChild != null){
			leftRightChild.setParentCaseVariant(rootID);
			leftRightChild.setWhichChildCaseVariant(0);
			leftRightChild.save(tempFile.getLeftChildCaseVariant());
		}
		leftChild.setRightChildCaseVariant(rootID);
		leftChild.setHeightCaseVariant(leftRightHeight + 2);
		
		tempFile.save(rootID);
		leftChild.save(tempFile.getParentCaseVariant());
	}
	
	public void leftRotateCaseVariant(String rootID) {
		BigCase tempFile = new BigCase(rootID);
		
		BigCase rightChild = null;
		BigCase parent = null;
		if(!tempFile.getRightChildCaseVariant().equals("")){
			rightChild = new BigCase(tempFile.getRightChildCaseVariant());
		}
		if(!tempFile.getParentCaseVariant().equals("")){
			parent = new BigCase(tempFile.getParentCaseVariant());
		}
		
		BigCase rightLeftChild = null;
		int rightLeftHeight = 0;
		if(!rightChild.getLeftChildCaseVariant().equals("")){
			rightLeftChild = new BigCase(rightChild.getLeftChildCaseVariant());
			rightLeftHeight = rightLeftChild.getHeightCaseVariant();
		}

		rightChild.setParentCaseVariant(tempFile.getParentCaseVariant());
		rightChild.setWhichChildCaseVariant(tempFile.getWhichChildCaseVariant());
		if(tempFile.getParentCaseVariant().equals(""))
			rootCaseVariant = tempFile.getRightChildCaseVariant();
		else{
			if(tempFile.getWhichChildCaseVariant() == 0)
				parent.setLeftChildCaseVariant(tempFile.getRightChildCaseVariant());
			else
				parent.setRightChildCaseVariant(tempFile.getRightChildCaseVariant());
			parent.save(tempFile.getParentCaseVariant());
		}
		tempFile.setParentCaseVariant(tempFile.getRightChildCaseVariant());
		tempFile.setWhichChildCaseVariant(0);
		tempFile.setRightChildCaseVariant(rightChild.getLeftChildCaseVariant());
		tempFile.setHeightCaseVariant(rightLeftHeight + 1);
		if(rightLeftChild != null){
			rightLeftChild.setParentCaseVariant(rootID);
			rightLeftChild.setWhichChildCaseVariant(1);
			rightLeftChild.save(tempFile.getRightChildCaseVariant());
		}
		rightChild.setLeftChildCaseVariant(rootID);
		rightChild.setHeightCaseVariant(rightLeftHeight + 2);
		
		tempFile.save(rootID);
		rightChild.save(tempFile.getParentCaseVariant());
	}	
	
    public int compareDuration(BigCase case1, BigCase case2) {
        if(case1.getDuration() < case2.getDuration()) {return 1;}
        else if (case1.getDuration() > case2.getDuration()) {return -1;}
        else {return 0;}
    }
    
    public int compareCaseVariant(BigCase case1, BigCase case2) {
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
                if(case1.getDuration() < case2.getDuration()) {return 1;}
                else if (case1.getDuration() > case2.getDuration()) {return -1;}
                else {return 0;}
            }
        }
    }
    
    public int compareID(BigCase s1, BigCase s2) {
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
    
    String rootCaseID;
    String rootDuration;
    String rootCaseVariant;
	long totalDuration;
	long medianDuration;
	Date start;
	Date end;
	int size;

	public BigCaseCollection() {
		// TODO Auto-generated constructor stub
	    totalDuration = 0;
	    medianDuration = 0;
	    rootCaseID = null;
	    rootDuration = null;
	    rootCaseVariant = null;
	    start = null;
	    end = null;
	    size = 0;
	}
	
	public void addCase(BigCase c) {
		UUID uuid = UUID.randomUUID();
		String fileName = uuid.toString();
		
		if(rootCaseID == null && rootDuration == null && rootCaseVariant == null) {
			rootCaseID = fileName;
			c.setWhichChildID(-1);
			
			rootDuration = fileName;
			c.setWhichChildDuration(-1);
			
			rootCaseVariant = fileName;
			c.setWhichChildCaseVariant(-1);
			c.save(fileName);
		}else{
			String tempIDID = rootCaseID;
			BigCase tempFileID = new BigCase(tempIDID);
			while((compareID(c, tempFileID) < 0 && !tempFileID.getLeftChildID().equals("")) || (compareID(c, tempFileID) >= 0 && !tempFileID.getRightChildID().equals(""))){
				if(compareID(c, tempFileID) < 0){
					tempIDID = tempFileID.getLeftChildID();
					tempFileID = new BigCase(tempIDID);
				}else{
					tempIDID = tempFileID.getRightChildID();
					tempFileID = new BigCase(tempIDID);
				}
			}
			
			if(compareID(c, tempFileID) < 0){
				tempFileID.setHeightID(2);
				tempFileID.setLeftChildID(fileName);
				tempFileID.save(tempIDID);
				c.setParentID(tempIDID);
				c.setWhichChildID(0);
			}else{
				tempFileID.setHeightID(2);
				tempFileID.setRightChildID(fileName);
				tempFileID.save(tempIDID);
				c.setParentID(tempIDID);
				c.setWhichChildID(1);
			}
			
			String tempIDDuration = rootDuration;
			BigCase tempFileDuration = new BigCase(tempIDDuration);
			while((compareDuration(c, tempFileDuration) < 0 && !tempFileDuration.getLeftChildDuration().equals("")) || (compareDuration(c, tempFileDuration) >= 0 && !tempFileDuration.getRightChildDuration().equals(""))){
				if(compareDuration(c, tempFileDuration) < 0){
					tempIDDuration = tempFileDuration.getLeftChildDuration();
					tempFileDuration = new BigCase(tempIDDuration);
				}else{
					tempIDDuration = tempFileDuration.getRightChildDuration();
					tempFileDuration = new BigCase(tempIDDuration);
				}
			}
			
			if(compareDuration(c, tempFileDuration) < 0){
				tempFileDuration.setHeightDuration(2);
				tempFileDuration.setLeftChildDuration(fileName);
				tempFileDuration.save(tempIDDuration);
				c.setParentDuration(tempIDDuration);
				c.setWhichChildDuration(0);
			}else{
				tempFileDuration.setHeightDuration(2);
				tempFileDuration.setRightChildDuration(fileName);
				tempFileDuration.save(tempIDDuration);
				c.setParentDuration(tempIDDuration);
				c.setWhichChildDuration(1);
			}
			
			String tempIDCaseVariant = rootCaseVariant;
			BigCase tempFileCaseVariant = new BigCase(tempIDCaseVariant);
			while((compareCaseVariant(c, tempFileCaseVariant) < 0 && !tempFileCaseVariant.getLeftChildCaseVariant().equals("")) || (compareCaseVariant(c, tempFileCaseVariant) >= 0 && !tempFileCaseVariant.getRightChildCaseVariant().equals(""))){
				if(compareCaseVariant(c, tempFileCaseVariant) < 0){
					tempIDCaseVariant = tempFileCaseVariant.getLeftChildCaseVariant();
					tempFileCaseVariant = new BigCase(tempIDCaseVariant);
				}else{
					tempIDCaseVariant = tempFileCaseVariant.getRightChildCaseVariant();
					tempFileCaseVariant = new BigCase(tempIDCaseVariant);
				}
			}
			
			if(compareCaseVariant(c, tempFileCaseVariant) < 0){
				tempFileCaseVariant.setHeightCaseVariant(2);
				tempFileCaseVariant.setLeftChildCaseVariant(fileName);
				tempFileCaseVariant.save(tempIDCaseVariant);
				c.setParentCaseVariant(tempIDCaseVariant);
				c.setWhichChildCaseVariant(0);
			}else{
				tempFileCaseVariant.setHeightCaseVariant(2);
				tempFileCaseVariant.setRightChildCaseVariant(fileName);
				tempFileCaseVariant.save(tempIDCaseVariant);
				c.setParentCaseVariant(tempIDCaseVariant);
				c.setWhichChildCaseVariant(1);
			}
		
			c.save(fileName);
			
			while(!tempFileID.getParentID().equals("")){
				tempIDID = tempFileID.getParentID();
				tempFileID = new BigCase(tempIDID);
				
				BigCase leftChild = null;
				BigCase rightChild = null;
				int leftHeight = 0;
				int rightHeight = 0;
				if(!tempFileID.getLeftChildID().equals("")){
					leftChild = new BigCase(tempFileID.getLeftChildID());
					leftHeight = leftChild.getHeightID();
				}
				if(!tempFileID.getRightChildID().equals("")){
					rightChild = new BigCase(tempFileID.getRightChildID());
					rightHeight = rightChild.getHeightID();
				}
				
				if(leftHeight - rightHeight > 1){
					BigCase leftLeftChild = null;
					BigCase leftRightChild = null;
					int leftLeftHeight = 0;
					int leftRightHeight = 0;
					if(!leftChild.getLeftChildID().equals("")){
						leftLeftChild = new BigCase(leftChild.getLeftChildID());
						leftLeftHeight = leftLeftChild.getHeightID();
					}
					if(!leftChild.getRightChildID().equals("")){
						leftRightChild = new BigCase(leftChild.getRightChildID());
						leftRightHeight = leftRightChild.getHeightID();
					}
					
					if(leftLeftHeight > leftRightHeight){
						rightRotateID(tempIDID);
					}else{
						leftRotateID(tempFileID.getLeftChildID());
						rightRotateID(tempIDID);
					}
				}else if(leftHeight - rightHeight < -1){
					BigCase rightLeftChild = null;
					BigCase rightRightChild = null;
					int rightLeftHeight = 0;
					int rightRightHeight = 0;
					if(!rightChild.getLeftChildID().equals("")){
						rightLeftChild = new BigCase(rightChild.getLeftChildID());
						rightLeftHeight = rightLeftChild.getHeightID();
					}
					if(!rightChild.getRightChildID().equals("")){
						rightRightChild = new BigCase(rightChild.getRightChildID());
						rightRightHeight = rightRightChild.getHeightID();
					}
					
					if(rightLeftHeight < rightRightHeight){
						leftRotateID(tempIDID);
					}else{
						rightRotateID(tempFileID.getRightChildID());
						leftRotateID(tempIDID);
					}
				}else{
					if(leftHeight < rightHeight){
						tempFileID.setHeightID(rightHeight + 1);
					}else{
						tempFileID.setHeightID(leftHeight + 1);
					}
					tempFileID.save(tempIDID);
				}
			}
		
			while(!tempFileDuration.getParentDuration().equals("")){
				tempIDDuration = tempFileDuration.getParentDuration();
				tempFileDuration = new BigCase(tempIDDuration);
				
				BigCase leftChild = null;
				BigCase rightChild = null;
				int leftHeight = 0;
				int rightHeight = 0;
				if(!tempFileDuration.getLeftChildDuration().equals("")){
					leftChild = new BigCase(tempFileDuration.getLeftChildDuration());
					leftHeight = leftChild.getHeightDuration();
				}
				if(!tempFileDuration.getRightChildDuration().equals("")){
					rightChild = new BigCase(tempFileDuration.getRightChildDuration());
					rightHeight = rightChild.getHeightDuration();
				}
				
				if(leftHeight - rightHeight > 1){
					BigCase leftLeftChild = null;
					BigCase leftRightChild = null;
					int leftLeftHeight = 0;
					int leftRightHeight = 0;
					if(!leftChild.getLeftChildDuration().equals("")){
						leftLeftChild = new BigCase(leftChild.getLeftChildDuration());
						leftLeftHeight = leftLeftChild.getHeightDuration();
					}
					if(!leftChild.getRightChildDuration().equals("")){
						leftRightChild = new BigCase(leftChild.getRightChildDuration());
						leftRightHeight = leftRightChild.getHeightDuration();
					}
					
					if(leftLeftHeight > leftRightHeight){
						rightRotateDuration(tempIDDuration);
					}else{
						leftRotateDuration(tempFileDuration.getLeftChildDuration());
						rightRotateDuration(tempIDDuration);
					}
				}else if(leftHeight - rightHeight < -1){
					BigCase rightLeftChild = null;
					BigCase rightRightChild = null;
					int rightLeftHeight = 0;
					int rightRightHeight = 0;
					if(!rightChild.getLeftChildDuration().equals("")){
						rightLeftChild = new BigCase(rightChild.getLeftChildDuration());
						rightLeftHeight = rightLeftChild.getHeightDuration();
					}
					if(!rightChild.getRightChildDuration().equals("")){
						rightRightChild = new BigCase(rightChild.getRightChildDuration());
						rightRightHeight = rightRightChild.getHeightDuration();
					}
					
					if(rightLeftHeight < rightRightHeight){
						leftRotateDuration(tempIDDuration);
					}else{
						rightRotateDuration(tempFileDuration.getRightChildDuration());
						leftRotateDuration(tempIDDuration);
					}
				}else{
					if(leftHeight < rightHeight){
						tempFileDuration.setHeightDuration(rightHeight + 1);
					}else{
						tempFileDuration.setHeightDuration(leftHeight + 1);
					}
					tempFileDuration.save(tempIDDuration);
				}
			}
		
			while(!tempFileCaseVariant.getParentCaseVariant().equals("")){
				tempIDCaseVariant = tempFileCaseVariant.getParentCaseVariant();
				tempFileCaseVariant = new BigCase(tempIDCaseVariant);
				
				BigCase leftChild = null;
				BigCase rightChild = null;
				int leftHeight = 0;
				int rightHeight = 0;
				if(!tempFileCaseVariant.getLeftChildCaseVariant().equals("")){
					leftChild = new BigCase(tempFileCaseVariant.getLeftChildCaseVariant());
					leftHeight = leftChild.getHeightCaseVariant();
				}
				if(!tempFileCaseVariant.getRightChildCaseVariant().equals("")){
					rightChild = new BigCase(tempFileCaseVariant.getRightChildCaseVariant());
					rightHeight = rightChild.getHeightCaseVariant();
				}
				
				if(leftHeight - rightHeight > 1){
					BigCase leftLeftChild = null;
					BigCase leftRightChild = null;
					int leftLeftHeight = 0;
					int leftRightHeight = 0;
					if(!leftChild.getLeftChildCaseVariant().equals("")){
						leftLeftChild = new BigCase(leftChild.getLeftChildCaseVariant());
						leftLeftHeight = leftLeftChild.getHeightCaseVariant();
					}
					if(!leftChild.getRightChildCaseVariant().equals("")){
						leftRightChild = new BigCase(leftChild.getRightChildCaseVariant());
						leftRightHeight = leftRightChild.getHeightCaseVariant();
					}
					
					if(leftLeftHeight > leftRightHeight){
						rightRotateCaseVariant(tempIDCaseVariant);
					}else{
						leftRotateCaseVariant(tempFileCaseVariant.getLeftChildCaseVariant());
						rightRotateCaseVariant(tempIDCaseVariant);
					}
				}else if(leftHeight - rightHeight < -1){
					BigCase rightLeftChild = null;
					BigCase rightRightChild = null;
					int rightLeftHeight = 0;
					int rightRightHeight = 0;
					if(!rightChild.getLeftChildCaseVariant().equals("")){
						rightLeftChild = new BigCase(rightChild.getLeftChildCaseVariant());
						rightLeftHeight = rightLeftChild.getHeightCaseVariant();
					}
					if(!rightChild.getRightChildCaseVariant().equals("")){
						rightRightChild = new BigCase(rightChild.getRightChildCaseVariant());
						rightRightHeight = rightRightChild.getHeightCaseVariant();
					}
					
					if(rightLeftHeight < rightRightHeight){
						leftRotateCaseVariant(tempIDCaseVariant);
					}else{
						rightRotateCaseVariant(tempFileCaseVariant.getRightChildCaseVariant());
						leftRotateCaseVariant(tempIDCaseVariant);
					}
				}else{
					if(leftHeight < rightHeight){
						tempFileCaseVariant.setHeightCaseVariant(rightHeight + 1);
					}else{
						tempFileCaseVariant.setHeightCaseVariant(leftHeight + 1);
					}
					tempFileCaseVariant.save(tempIDCaseVariant);
				}
			}
		}
        
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
	
	public void sortByDuration(){
    }
    
    public void sortByID(){
    }
    
    public void sortByVariant(){
    }
    
	public void update() {
		int count = 0;
        BigCase c = getFirstDuration();
        if (c == null) return;
        
        if(getSize() % 2 == 1){
	        while (c != null) {
	        	if(count == getSize() / 2){
	        		medianDuration = c.getDuration();
	        		return;
	        	}
	        	c = getNextDuration(c);
	        	count++;
	        }
        }else{
	        while (c != null) {
	        	if(count == getSize() / 2 - 1){
	        		medianDuration = c.getDuration();
	        		c = getNextDuration(c);
	        		medianDuration = (medianDuration + c.getDuration()) / 2;
	        		return;
	        	}
	        	c = getNextDuration(c);
	        	count++;
	        }
        }
	}
    
    public long getMedianDuration() {
        return medianDuration;
    }

	public BigCase getFirstID(){
		if(rootCaseID == null)
			return null;

		BigCase tempFileID = new BigCase(rootCaseID);
		while(!tempFileID.getLeftChildID().equals("")){
			tempFileID = new BigCase(tempFileID.getLeftChildID());
		}
		
		return tempFileID;
	}
	
	public BigCase getNextID(BigCase e){
		if(!e.getRightChildID().equals("")){
			BigCase tempFileID = new BigCase(e.getRightChildID());
			while(!tempFileID.getLeftChildID().equals("")){
				tempFileID = new BigCase(tempFileID.getLeftChildID());
			}
			return tempFileID;
		}
		
		BigCase tempFileID = e;
		while(tempFileID.getWhichChildID() == 1){
			tempFileID = new BigCase(tempFileID.getParentID());
		}
		
		if(tempFileID.getWhichChildID() == 0){
			tempFileID = new BigCase(tempFileID.getParentID());
			return tempFileID;
		}
		
		return null;
	}
	
	public BigCase getFirstDuration(){
		if(rootDuration == null)
			return null;

		BigCase tempFileDuration = new BigCase(rootDuration);
		while(!tempFileDuration.getLeftChildDuration().equals("")){
			tempFileDuration = new BigCase(tempFileDuration.getLeftChildDuration());
		}
		
		return tempFileDuration;
	}
	
	public BigCase getNextDuration(BigCase e){
		if(!e.getRightChildDuration().equals("")){
			BigCase tempFileDuration = new BigCase(e.getRightChildDuration());
			while(!tempFileDuration.getLeftChildDuration().equals("")){
				tempFileDuration = new BigCase(tempFileDuration.getLeftChildDuration());
			}
			return tempFileDuration;
		}
		
		BigCase tempFileDuration = e;
		while(tempFileDuration.getWhichChildDuration() == 1){
			tempFileDuration = new BigCase(tempFileDuration.getParentDuration());
		}
		
		if(tempFileDuration.getWhichChildDuration() == 0){
			tempFileDuration = new BigCase(tempFileDuration.getParentDuration());
			return tempFileDuration;
		}
		
		return null;
	}
	
	public BigCase getFirstCaseVariant(){
		if(rootCaseVariant == null)
			return null;

		BigCase tempFileCaseVariant = new BigCase(rootCaseVariant);
		while(!tempFileCaseVariant.getLeftChildCaseVariant().equals("")){
			tempFileCaseVariant = new BigCase(tempFileCaseVariant.getLeftChildCaseVariant());
		}
		
		return tempFileCaseVariant;
	}
	
	public BigCase getNextCaseVariant(BigCase e){
		if(!e.getRightChildCaseVariant().equals("")){
			BigCase tempFileCaseVariant = new BigCase(e.getRightChildCaseVariant());
			while(!tempFileCaseVariant.getLeftChildCaseVariant().equals("")){
				tempFileCaseVariant = new BigCase(tempFileCaseVariant.getLeftChildCaseVariant());
			}
			return tempFileCaseVariant;
		}
		
		BigCase tempFileCaseVariant = e;
		while(tempFileCaseVariant.getWhichChildCaseVariant() == 1){
			tempFileCaseVariant = new BigCase(tempFileCaseVariant.getParentCaseVariant());
		}
		
		if(tempFileCaseVariant.getWhichChildCaseVariant() == 0){
			tempFileCaseVariant = new BigCase(tempFileCaseVariant.getParentCaseVariant());
			return tempFileCaseVariant;
		}
		
		return null;
	}
}
