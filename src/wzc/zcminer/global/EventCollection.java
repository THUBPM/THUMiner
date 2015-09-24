package wzc.zcminer.global;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

//event集合类
public class EventCollection {
    
    private class ComparatorEventActivity implements Comparator<Event>{
        public int compare(Event event1, Event event2) {
            if(event1.getActivity().compareTo(event2.getActivity()) > 0) {return 1;}
            else if (event1.getActivity().compareTo(event2.getActivity()) < 0) {return -1;}
            else {return 0;}
        }
    }
    
    private class ComparatorEventResource implements Comparator<Event>{
        public int compare(Event event1, Event event2) {
            if(event1.getResource().compareTo(event2.getResource()) > 0) {return 1;}
            else if (event1.getResource().compareTo(event2.getResource()) < 0) {return -1;}
            else {return 0;}
        }
    }
    
    private class ComparatorEventID implements Comparator<Event>{
        public int compare(Event event1, Event event2) {
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
    }
    
	List<Event> events;
	int size;

	public EventCollection() {
		// TODO Auto-generated constructor stub
	    events = new ArrayList<Event>();
	}

	public void addEvent(Event c) {
	    events.add(c);
	    size++;
	}
	
	public int getSize(){
		return size;
	}
	
	public Event getEvent(int pos) {
		return events.get(pos);
	}
	
	public void sortByActivity()
	{
	    ComparatorEventActivity c = new ComparatorEventActivity();
	    events.sort(c);
	}
	
    public void sortByResource()
    {
        ComparatorEventResource c = new ComparatorEventResource();
        events.sort(c);
    }
	
    
    public void sortByID()
    {
        ComparatorEventID c = new ComparatorEventID();
        events.sort(c);
    }
    
}
