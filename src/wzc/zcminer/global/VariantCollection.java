package wzc.zcminer.global;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

//variant集合类
public class VariantCollection {
    
    private class ComparatorVariant implements Comparator<Variant>{
        public int compare(Variant variant1, Variant variant2) {
            if(variant1.getSize() < variant2.getSize()) {return 1;}
            else if (variant1.getSize() > variant2.getSize()) {return -1;}
            else {return 0;}
        }
    }
    
	List<Variant> variants;

	public VariantCollection() {
		// TODO Auto-generated constructor stub
	    variants = new ArrayList<Variant>();
	}

	public void addVariant(Variant c) {
	    variants.add(c);
	}
	
	public int getSize(){
		return variants.size();
	}
	
	public Variant getVariant(int pos) {
		return variants.get(pos);
	}
	
	public void setVariant(int pos, Variant v) {
	    variants.set(pos, v);
	}
	
    public void sortAndMerge() {
        ComparatorVariant c = new ComparatorVariant();
        variants.sort(c);
        
        int size = getSize();
        
        for(int i = 0; i < size; i++)
        {
            Variant variant = getVariant(i);
            variant.setVariant("" + (i + 1));
        }
    }
}
