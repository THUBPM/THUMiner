package wzc.zcminer.global;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

//event集合类
public class BigEventCollection {
	public void rightRotateEventID(String rootID) {
		BigEvent tempFile = new BigEvent(rootID);
		
		BigEvent leftChild = null;
		BigEvent parent = null;
		if(!tempFile.getLeftChildEventID().equals("")){
			leftChild = new BigEvent(tempFile.getLeftChildEventID());
		}
		if(!tempFile.getParentEventID().equals("")){
			parent = new BigEvent(tempFile.getParentEventID());
		}
		
		BigEvent leftRightChild = null;
		int leftRightHeight = 0;
		if(!leftChild.getRightChildEventID().equals("")){
			leftRightChild = new BigEvent(leftChild.getRightChildEventID());
			leftRightHeight = leftRightChild.getHeightEventID();
		}
		
		leftChild.setParentEventID(tempFile.getParentEventID());
		leftChild.setWhichChildEventID(tempFile.getWhichChildEventID());
		if(tempFile.getParentEventID().equals(""))
			rootEventID = tempFile.getLeftChildEventID();
		else{
			if(tempFile.getWhichChildEventID() == 0)
				parent.setLeftChildEventID(tempFile.getLeftChildEventID());
			else
				parent.setRightChildEventID(tempFile.getLeftChildEventID());
			parent.save(tempFile.getParentEventID());
		}
		tempFile.setParentEventID(tempFile.getLeftChildEventID());
		tempFile.setWhichChildEventID(1);
		tempFile.setLeftChildEventID(leftChild.getRightChildEventID());
		tempFile.setHeightEventID(leftRightHeight + 1);
		if(leftRightChild != null){
			leftRightChild.setParentEventID(rootID);
			leftRightChild.setWhichChildEventID(0);
			leftRightChild.save(tempFile.getLeftChildEventID());
		}
		leftChild.setRightChildEventID(rootID);
		leftChild.setHeightEventID(leftRightHeight + 2);
		
		tempFile.save(rootID);
		leftChild.save(tempFile.getParentEventID());
	}
	
	public void leftRotateEventID(String rootID) {
		BigEvent tempFile = new BigEvent(rootID);
		
		BigEvent rightChild = null;
		BigEvent parent = null;
		if(!tempFile.getRightChildEventID().equals("")){
			rightChild = new BigEvent(tempFile.getRightChildEventID());
		}
		if(!tempFile.getParentEventID().equals("")){
			parent = new BigEvent(tempFile.getParentEventID());
		}
		
		BigEvent rightLeftChild = null;
		int rightLeftHeight = 0;
		if(!rightChild.getLeftChildEventID().equals("")){
			rightLeftChild = new BigEvent(rightChild.getLeftChildEventID());
			rightLeftHeight = rightLeftChild.getHeightEventID();
		}

		rightChild.setParentEventID(tempFile.getParentEventID());
		rightChild.setWhichChildEventID(tempFile.getWhichChildEventID());
		if(tempFile.getParentEventID().equals(""))
			rootEventID = tempFile.getRightChildEventID();
		else{
			if(tempFile.getWhichChildEventID() == 0)
				parent.setLeftChildEventID(tempFile.getRightChildEventID());
			else
				parent.setRightChildEventID(tempFile.getRightChildEventID());
			parent.save(tempFile.getParentEventID());
		}
		tempFile.setParentEventID(tempFile.getRightChildEventID());
		tempFile.setWhichChildEventID(0);
		tempFile.setRightChildEventID(rightChild.getLeftChildEventID());
		tempFile.setHeightEventID(rightLeftHeight + 1);
		if(rightLeftChild != null){
			rightLeftChild.setParentEventID(rootID);
			rightLeftChild.setWhichChildEventID(1);
			rightLeftChild.save(tempFile.getRightChildEventID());
		}
		rightChild.setLeftChildEventID(rootID);
		rightChild.setHeightEventID(rightLeftHeight + 2);
		
		tempFile.save(rootID);
		rightChild.save(tempFile.getParentEventID());
	}
	
	public void rightRotateActivity(String rootID) {
		BigEvent tempFile = new BigEvent(rootID);
		
		BigEvent leftChild = null;
		BigEvent parent = null;
		if(!tempFile.getLeftChildActivity().equals("")){
			leftChild = new BigEvent(tempFile.getLeftChildActivity());
		}
		if(!tempFile.getParentActivity().equals("")){
			parent = new BigEvent(tempFile.getParentActivity());
		}
		
		BigEvent leftRightChild = null;
		int leftRightHeight = 0;
		if(!leftChild.getRightChildActivity().equals("")){
			leftRightChild = new BigEvent(leftChild.getRightChildActivity());
			leftRightHeight = leftRightChild.getHeightActivity();
		}
		
		leftChild.setParentActivity(tempFile.getParentActivity());
		leftChild.setWhichChildActivity(tempFile.getWhichChildActivity());
		if(tempFile.getParentActivity().equals(""))
			rootActivity = tempFile.getLeftChildActivity();
		else{
			if(tempFile.getWhichChildActivity() == 0)
				parent.setLeftChildActivity(tempFile.getLeftChildActivity());
			else
				parent.setRightChildActivity(tempFile.getLeftChildActivity());
			parent.save(tempFile.getParentActivity());
		}
		tempFile.setParentActivity(tempFile.getLeftChildActivity());
		tempFile.setWhichChildActivity(1);
		tempFile.setLeftChildActivity(leftChild.getRightChildActivity());
		tempFile.setHeightActivity(leftRightHeight + 1);
		if(leftRightChild != null){
			leftRightChild.setParentActivity(rootID);
			leftRightChild.setWhichChildActivity(0);
			leftRightChild.save(tempFile.getLeftChildActivity());
		}
		leftChild.setRightChildActivity(rootID);
		leftChild.setHeightActivity(leftRightHeight + 2);
		
		tempFile.save(rootID);
		leftChild.save(tempFile.getParentActivity());
	}
	
	public void leftRotateActivity(String rootID) {
		BigEvent tempFile = new BigEvent(rootID);
		
		BigEvent rightChild = null;
		BigEvent parent = null;
		if(!tempFile.getRightChildActivity().equals("")){
			rightChild = new BigEvent(tempFile.getRightChildActivity());
		}
		if(!tempFile.getParentActivity().equals("")){
			parent = new BigEvent(tempFile.getParentActivity());
		}
		
		BigEvent rightLeftChild = null;
		int rightLeftHeight = 0;
		if(!rightChild.getLeftChildActivity().equals("")){
			rightLeftChild = new BigEvent(rightChild.getLeftChildActivity());
			rightLeftHeight = rightLeftChild.getHeightActivity();
		}

		rightChild.setParentActivity(tempFile.getParentActivity());
		rightChild.setWhichChildActivity(tempFile.getWhichChildActivity());
		if(tempFile.getParentActivity().equals(""))
			rootActivity = tempFile.getRightChildActivity();
		else{
			if(tempFile.getWhichChildActivity() == 0)
				parent.setLeftChildActivity(tempFile.getRightChildActivity());
			else
				parent.setRightChildActivity(tempFile.getRightChildActivity());
			parent.save(tempFile.getParentActivity());
		}
		tempFile.setParentActivity(tempFile.getRightChildActivity());
		tempFile.setWhichChildActivity(0);
		tempFile.setRightChildActivity(rightChild.getLeftChildActivity());
		tempFile.setHeightActivity(rightLeftHeight + 1);
		if(rightLeftChild != null){
			rightLeftChild.setParentActivity(rootID);
			rightLeftChild.setWhichChildActivity(1);
			rightLeftChild.save(tempFile.getRightChildActivity());
		}
		rightChild.setLeftChildActivity(rootID);
		rightChild.setHeightActivity(rightLeftHeight + 2);
		
		tempFile.save(rootID);
		rightChild.save(tempFile.getParentActivity());
	}
	
	public void rightRotateResource(String rootID) {
		BigEvent tempFile = new BigEvent(rootID);
		
		BigEvent leftChild = null;
		BigEvent parent = null;
		if(!tempFile.getLeftChildResource().equals("")){
			leftChild = new BigEvent(tempFile.getLeftChildResource());
		}
		if(!tempFile.getParentResource().equals("")){
			parent = new BigEvent(tempFile.getParentResource());
		}
		
		BigEvent leftRightChild = null;
		int leftRightHeight = 0;
		if(!leftChild.getRightChildResource().equals("")){
			leftRightChild = new BigEvent(leftChild.getRightChildResource());
			leftRightHeight = leftRightChild.getHeightResource();
		}
		
		leftChild.setParentResource(tempFile.getParentResource());
		leftChild.setWhichChildResource(tempFile.getWhichChildResource());
		if(tempFile.getParentResource().equals(""))
			rootResource = tempFile.getLeftChildResource();
		else{
			if(tempFile.getWhichChildResource() == 0)
				parent.setLeftChildResource(tempFile.getLeftChildResource());
			else
				parent.setRightChildResource(tempFile.getLeftChildResource());
			parent.save(tempFile.getParentResource());
		}
		tempFile.setParentResource(tempFile.getLeftChildResource());
		tempFile.setWhichChildResource(1);
		tempFile.setLeftChildResource(leftChild.getRightChildResource());
		tempFile.setHeightResource(leftRightHeight + 1);
		if(leftRightChild != null){
			leftRightChild.setParentResource(rootID);
			leftRightChild.setWhichChildResource(0);
			leftRightChild.save(tempFile.getLeftChildResource());
		}
		leftChild.setRightChildResource(rootID);
		leftChild.setHeightResource(leftRightHeight + 2);
		
		tempFile.save(rootID);
		leftChild.save(tempFile.getParentResource());
	}
	
	public void leftRotateResource(String rootID) {
		BigEvent tempFile = new BigEvent(rootID);
		
		BigEvent rightChild = null;
		BigEvent parent = null;
		if(!tempFile.getRightChildResource().equals("")){
			rightChild = new BigEvent(tempFile.getRightChildResource());
		}
		if(!tempFile.getParentResource().equals("")){
			parent = new BigEvent(tempFile.getParentResource());
		}
		
		BigEvent rightLeftChild = null;
		int rightLeftHeight = 0;
		if(!rightChild.getLeftChildResource().equals("")){
			rightLeftChild = new BigEvent(rightChild.getLeftChildResource());
			rightLeftHeight = rightLeftChild.getHeightResource();
		}

		rightChild.setParentResource(tempFile.getParentResource());
		rightChild.setWhichChildResource(tempFile.getWhichChildResource());
		if(tempFile.getParentResource().equals(""))
			rootResource = tempFile.getRightChildResource();
		else{
			if(tempFile.getWhichChildResource() == 0)
				parent.setLeftChildResource(tempFile.getRightChildResource());
			else
				parent.setRightChildResource(tempFile.getRightChildResource());
			parent.save(tempFile.getParentResource());
		}
		tempFile.setParentResource(tempFile.getRightChildResource());
		tempFile.setWhichChildResource(0);
		tempFile.setRightChildResource(rightChild.getLeftChildResource());
		tempFile.setHeightResource(rightLeftHeight + 1);
		if(rightLeftChild != null){
			rightLeftChild.setParentResource(rootID);
			rightLeftChild.setWhichChildResource(1);
			rightLeftChild.save(tempFile.getRightChildResource());
		}
		rightChild.setLeftChildResource(rootID);
		rightChild.setHeightResource(rightLeftHeight + 2);
		
		tempFile.save(rootID);
		rightChild.save(tempFile.getParentResource());
	}
	
    public int compareActivity(BigEvent event1, BigEvent event2) {
        if(event1.getActivity().compareTo(event2.getActivity()) > 0) {return 1;}
        else if (event1.getActivity().compareTo(event2.getActivity()) < 0) {return -1;}
        else {return 0;}
    }

    public int compareResource(BigEvent event1, BigEvent event2) {
        if(event1.getResource().compareTo(event2.getResource()) > 0) {return 1;}
        else if (event1.getResource().compareTo(event2.getResource()) < 0) {return -1;}
        else {return 0;}
    }
    
    public int compareEventID(BigEvent event1, BigEvent event2) {
        if(event1.getCase().compareTo(event2.getCase()) > 0) {return 1;}
        else if (event1.getCase().compareTo(event2.getCase()) < 0) {return -1;}
        else 
        {
            if(event1.getStartDate().before(event2.getStartDate())) {return -1;}
            else if (event1.getStartDate().after(event2.getStartDate())) {return 1;}
            else
            {
            	if(event1.getEndDate().before(event2.getEndDate()))
            		return -1;
            	else if(event1.getEndDate().after(event2.getEndDate()))
            		return 1;
            	else
            		return 0;
            }
        }
    }
    
    String rootEventID;
    String rootActivity;
    String rootResource;
	int size;

	public BigEventCollection() {
		// TODO Auto-generated constructor stub
		size = 0;
	    rootEventID = null;
	    rootActivity = null;
	    rootResource = null;
	}

	public void addEvent(BigEvent c) {
		UUID uuid = UUID.randomUUID();
		String fileName = uuid.toString();
		
		if(rootEventID == null && rootActivity == null && rootResource == null) {
			rootEventID = fileName;
			c.setWhichChildEventID(-1);
			
			rootActivity = fileName;
			c.setWhichChildActivity(-1);
			
			rootResource = fileName;
			c.setWhichChildResource(-1);
			c.save(fileName);
		}else{
			String tempIDEventID = rootEventID;
			BigEvent tempFileEventID = new BigEvent(tempIDEventID);
			while((compareEventID(c, tempFileEventID) < 0 && !tempFileEventID.getLeftChildEventID().equals("")) || (compareEventID(c, tempFileEventID) >= 0 && !tempFileEventID.getRightChildEventID().equals(""))){
				if(compareEventID(c, tempFileEventID) < 0){
					tempIDEventID = tempFileEventID.getLeftChildEventID();
					tempFileEventID = new BigEvent(tempIDEventID);
				}else{
					tempIDEventID = tempFileEventID.getRightChildEventID();
					tempFileEventID = new BigEvent(tempIDEventID);
				}
			}
			
			if(compareEventID(c, tempFileEventID) < 0){
				tempFileEventID.setHeightEventID(2);
				tempFileEventID.setLeftChildEventID(fileName);
				tempFileEventID.save(tempIDEventID);
				c.setParentEventID(tempIDEventID);
				c.setWhichChildEventID(0);
			}else{
				tempFileEventID.setHeightEventID(2);
				tempFileEventID.setRightChildEventID(fileName);
				tempFileEventID.save(tempIDEventID);
				c.setParentEventID(tempIDEventID);
				c.setWhichChildEventID(1);
			}
			
			String tempIDActivity = rootActivity;
			BigEvent tempFileActivity = new BigEvent(tempIDActivity);
			while((compareActivity(c, tempFileActivity) < 0 && !tempFileActivity.getLeftChildActivity().equals("")) || (compareActivity(c, tempFileActivity) >= 0 && !tempFileActivity.getRightChildActivity().equals(""))){
				if(compareActivity(c, tempFileActivity) < 0){
					tempIDActivity = tempFileActivity.getLeftChildActivity();
					tempFileActivity = new BigEvent(tempIDActivity);
				}else{
					tempIDActivity = tempFileActivity.getRightChildActivity();
					tempFileActivity = new BigEvent(tempIDActivity);
				}
			}
			
			if(compareActivity(c, tempFileActivity) < 0){
				tempFileActivity.setHeightActivity(2);
				tempFileActivity.setLeftChildActivity(fileName);
				tempFileActivity.save(tempIDActivity);
				c.setParentActivity(tempIDActivity);
				c.setWhichChildActivity(0);
			}else{
				tempFileActivity.setHeightActivity(2);
				tempFileActivity.setRightChildActivity(fileName);
				tempFileActivity.save(tempIDActivity);
				c.setParentActivity(tempIDActivity);
				c.setWhichChildActivity(1);
			}
			
			String tempIDResource = rootResource;
			BigEvent tempFileResource = new BigEvent(tempIDResource);
			while((compareResource(c, tempFileResource) < 0 && !tempFileResource.getLeftChildResource().equals("")) || (compareResource(c, tempFileResource) >= 0 && !tempFileResource.getRightChildResource().equals(""))){
				if(compareResource(c, tempFileResource) < 0){
					tempIDResource = tempFileResource.getLeftChildResource();
					tempFileResource = new BigEvent(tempIDResource);
				}else{
					tempIDResource = tempFileResource.getRightChildResource();
					tempFileResource = new BigEvent(tempIDResource);
				}
			}
			
			if(compareResource(c, tempFileResource) < 0){
				tempFileResource.setHeightResource(2);
				tempFileResource.setLeftChildResource(fileName);
				tempFileResource.save(tempIDResource);
				c.setParentResource(tempIDResource);
				c.setWhichChildResource(0);
			}else{
				tempFileResource.setHeightResource(2);
				tempFileResource.setRightChildResource(fileName);
				tempFileResource.save(tempIDResource);
				c.setParentResource(tempIDResource);
				c.setWhichChildResource(1);
			}
		
			c.save(fileName);
			
			while(!tempFileEventID.getParentEventID().equals("")){
				tempIDEventID = tempFileEventID.getParentEventID();
				tempFileEventID = new BigEvent(tempIDEventID);
				
				BigEvent leftChild = null;
				BigEvent rightChild = null;
				int leftHeight = 0;
				int rightHeight = 0;
				if(!tempFileEventID.getLeftChildEventID().equals("")){
					leftChild = new BigEvent(tempFileEventID.getLeftChildEventID());
					leftHeight = leftChild.getHeightEventID();
				}
				if(!tempFileEventID.getRightChildEventID().equals("")){
					rightChild = new BigEvent(tempFileEventID.getRightChildEventID());
					rightHeight = rightChild.getHeightEventID();
				}
				
				if(leftHeight - rightHeight > 1){
					BigEvent leftLeftChild = null;
					BigEvent leftRightChild = null;
					int leftLeftHeight = 0;
					int leftRightHeight = 0;
					if(!leftChild.getLeftChildEventID().equals("")){
						leftLeftChild = new BigEvent(leftChild.getLeftChildEventID());
						leftLeftHeight = leftLeftChild.getHeightEventID();
					}
					if(!leftChild.getRightChildEventID().equals("")){
						leftRightChild = new BigEvent(leftChild.getRightChildEventID());
						leftRightHeight = leftRightChild.getHeightEventID();
					}
					
					if(leftLeftHeight > leftRightHeight){
						rightRotateEventID(tempIDEventID);
					}else{
						leftRotateEventID(tempFileEventID.getLeftChildEventID());
						rightRotateEventID(tempIDEventID);
					}
				}else if(leftHeight - rightHeight < -1){
					BigEvent rightLeftChild = null;
					BigEvent rightRightChild = null;
					int rightLeftHeight = 0;
					int rightRightHeight = 0;
					if(!rightChild.getLeftChildEventID().equals("")){
						rightLeftChild = new BigEvent(rightChild.getLeftChildEventID());
						rightLeftHeight = rightLeftChild.getHeightEventID();
					}
					if(!rightChild.getRightChildEventID().equals("")){
						rightRightChild = new BigEvent(rightChild.getRightChildEventID());
						rightRightHeight = rightRightChild.getHeightEventID();
					}
					
					if(rightLeftHeight < rightRightHeight){
						leftRotateEventID(tempIDEventID);
					}else{
						rightRotateEventID(tempFileEventID.getRightChildEventID());
						leftRotateEventID(tempIDEventID);
					}
				}else{
					if(leftHeight < rightHeight){
						tempFileEventID.setHeightEventID(rightHeight + 1);
					}else{
						tempFileEventID.setHeightEventID(leftHeight + 1);
					}
					tempFileEventID.save(tempIDEventID);
				}
			}
		
			while(!tempFileActivity.getParentActivity().equals("")){
				tempIDActivity = tempFileActivity.getParentActivity();
				tempFileActivity = new BigEvent(tempIDActivity);
				
				BigEvent leftChild = null;
				BigEvent rightChild = null;
				int leftHeight = 0;
				int rightHeight = 0;
				if(!tempFileActivity.getLeftChildActivity().equals("")){
					leftChild = new BigEvent(tempFileActivity.getLeftChildActivity());
					leftHeight = leftChild.getHeightActivity();
				}
				if(!tempFileActivity.getRightChildActivity().equals("")){
					rightChild = new BigEvent(tempFileActivity.getRightChildActivity());
					rightHeight = rightChild.getHeightActivity();
				}
				
				if(leftHeight - rightHeight > 1){
					BigEvent leftLeftChild = null;
					BigEvent leftRightChild = null;
					int leftLeftHeight = 0;
					int leftRightHeight = 0;
					if(!leftChild.getLeftChildActivity().equals("")){
						leftLeftChild = new BigEvent(leftChild.getLeftChildActivity());
						leftLeftHeight = leftLeftChild.getHeightActivity();
					}
					if(!leftChild.getRightChildActivity().equals("")){
						leftRightChild = new BigEvent(leftChild.getRightChildActivity());
						leftRightHeight = leftRightChild.getHeightActivity();
					}
					
					if(leftLeftHeight > leftRightHeight){
						rightRotateActivity(tempIDActivity);
					}else{
						leftRotateActivity(tempFileActivity.getLeftChildActivity());
						rightRotateActivity(tempIDActivity);
					}
				}else if(leftHeight - rightHeight < -1){
					BigEvent rightLeftChild = null;
					BigEvent rightRightChild = null;
					int rightLeftHeight = 0;
					int rightRightHeight = 0;
					if(!rightChild.getLeftChildActivity().equals("")){
						rightLeftChild = new BigEvent(rightChild.getLeftChildActivity());
						rightLeftHeight = rightLeftChild.getHeightActivity();
					}
					if(!rightChild.getRightChildActivity().equals("")){
						rightRightChild = new BigEvent(rightChild.getRightChildActivity());
						rightRightHeight = rightRightChild.getHeightActivity();
					}
					
					if(rightLeftHeight < rightRightHeight){
						leftRotateActivity(tempIDActivity);
					}else{
						rightRotateActivity(tempFileActivity.getRightChildActivity());
						leftRotateActivity(tempIDActivity);
					}
				}else{
					if(leftHeight < rightHeight){
						tempFileActivity.setHeightActivity(rightHeight + 1);
					}else{
						tempFileActivity.setHeightActivity(leftHeight + 1);
					}
					tempFileActivity.save(tempIDActivity);
				}
			}
		
			while(!tempFileResource.getParentResource().equals("")){
				tempIDResource = tempFileResource.getParentResource();
				tempFileResource = new BigEvent(tempIDResource);
				
				BigEvent leftChild = null;
				BigEvent rightChild = null;
				int leftHeight = 0;
				int rightHeight = 0;
				if(!tempFileResource.getLeftChildResource().equals("")){
					leftChild = new BigEvent(tempFileResource.getLeftChildResource());
					leftHeight = leftChild.getHeightResource();
				}
				if(!tempFileResource.getRightChildResource().equals("")){
					rightChild = new BigEvent(tempFileResource.getRightChildResource());
					rightHeight = rightChild.getHeightResource();
				}
				
				if(leftHeight - rightHeight > 1){
					BigEvent leftLeftChild = null;
					BigEvent leftRightChild = null;
					int leftLeftHeight = 0;
					int leftRightHeight = 0;
					if(!leftChild.getLeftChildResource().equals("")){
						leftLeftChild = new BigEvent(leftChild.getLeftChildResource());
						leftLeftHeight = leftLeftChild.getHeightResource();
					}
					if(!leftChild.getRightChildResource().equals("")){
						leftRightChild = new BigEvent(leftChild.getRightChildResource());
						leftRightHeight = leftRightChild.getHeightResource();
					}
					
					if(leftLeftHeight > leftRightHeight){
						rightRotateResource(tempIDResource);
					}else{
						leftRotateResource(tempFileResource.getLeftChildResource());
						rightRotateResource(tempIDResource);
					}
				}else if(leftHeight - rightHeight < -1){
					BigEvent rightLeftChild = null;
					BigEvent rightRightChild = null;
					int rightLeftHeight = 0;
					int rightRightHeight = 0;
					if(!rightChild.getLeftChildResource().equals("")){
						rightLeftChild = new BigEvent(rightChild.getLeftChildResource());
						rightLeftHeight = rightLeftChild.getHeightResource();
					}
					if(!rightChild.getRightChildResource().equals("")){
						rightRightChild = new BigEvent(rightChild.getRightChildResource());
						rightRightHeight = rightRightChild.getHeightResource();
					}
					
					if(rightLeftHeight < rightRightHeight){
						leftRotateResource(tempIDResource);
					}else{
						rightRotateResource(tempFileResource.getRightChildResource());
						leftRotateResource(tempIDResource);
					}
				}else{
					if(leftHeight < rightHeight){
						tempFileResource.setHeightResource(rightHeight + 1);
					}else{
						tempFileResource.setHeightResource(leftHeight + 1);
					}
					tempFileResource.save(tempIDResource);
				}
			}
		}
        
	    size++;
	}
	
	public int getSize(){
		return size;
	}
	
	public BigEvent getFirstEventID(){
		if(rootEventID == null)
			return null;

		BigEvent tempFileEventID = new BigEvent(rootEventID);
		while(!tempFileEventID.getLeftChildEventID().equals("")){
			tempFileEventID = new BigEvent(tempFileEventID.getLeftChildEventID());
		}
		
		return tempFileEventID;
	}
	
	public BigEvent getNextEventID(BigEvent e){
		if(!e.getRightChildEventID().equals("")){
			BigEvent tempFileEventID = new BigEvent(e.getRightChildEventID());
			while(!tempFileEventID.getLeftChildEventID().equals("")){
				tempFileEventID = new BigEvent(tempFileEventID.getLeftChildEventID());
			}
			return tempFileEventID;
		}
		
		BigEvent tempFileEventID = e;
		while(tempFileEventID.getWhichChildEventID() == 1){
			tempFileEventID = new BigEvent(tempFileEventID.getParentEventID());
		}
		
		if(tempFileEventID.getWhichChildEventID() == 0){
			tempFileEventID = new BigEvent(tempFileEventID.getParentEventID());
			return tempFileEventID;
		}
		
		return null;
	}
	
	public BigEvent getFirstActivity(){
		if(rootActivity == null)
			return null;

		BigEvent tempFileActivity = new BigEvent(rootActivity);
		while(!tempFileActivity.getLeftChildActivity().equals("")){
			tempFileActivity = new BigEvent(tempFileActivity.getLeftChildActivity());
		}
		
		return tempFileActivity;
	}
	
	public BigEvent getNextActivity(BigEvent e){
		if(!e.getRightChildActivity().equals("")){
			BigEvent tempFileActivity = new BigEvent(e.getRightChildActivity());
			while(!tempFileActivity.getLeftChildActivity().equals("")){
				tempFileActivity = new BigEvent(tempFileActivity.getLeftChildActivity());
			}
			return tempFileActivity;
		}
		
		BigEvent tempFileActivity = e;
		while(tempFileActivity.getWhichChildActivity() == 1){
			tempFileActivity = new BigEvent(tempFileActivity.getParentActivity());
		}
		
		if(tempFileActivity.getWhichChildActivity() == 0){
			tempFileActivity = new BigEvent(tempFileActivity.getParentActivity());
			return tempFileActivity;
		}
		
		return null;
	}
	
	public BigEvent getFirstResource(){
		if(rootResource == null)
			return null;

		BigEvent tempFileResource = new BigEvent(rootResource);
		while(!tempFileResource.getLeftChildResource().equals("")){
			tempFileResource = new BigEvent(tempFileResource.getLeftChildResource());
		}
		
		return tempFileResource;
	}
	
	public BigEvent getNextResource(BigEvent e){
		if(!e.getRightChildResource().equals("")){
			BigEvent tempFileResource = new BigEvent(e.getRightChildResource());
			while(!tempFileResource.getLeftChildResource().equals("")){
				tempFileResource = new BigEvent(tempFileResource.getLeftChildResource());
			}
			return tempFileResource;
		}
		
		BigEvent tempFileResource = e;
		while(tempFileResource.getWhichChildResource() == 1){
			tempFileResource = new BigEvent(tempFileResource.getParentResource());
		}
		
		if(tempFileResource.getWhichChildResource() == 0){
			tempFileResource = new BigEvent(tempFileResource.getParentResource());
			return tempFileResource;
		}
		
		return null;
	}
}
