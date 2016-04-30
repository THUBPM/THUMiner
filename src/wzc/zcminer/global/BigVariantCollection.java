package wzc.zcminer.global;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

//variant集合类
public class BigVariantCollection {
	int count;

	public BigVariantCollection() {
		// TODO Auto-generated constructor stub
		count = 0;
	}

	public void addVariant(BigVariant c) {
	    c.save();
	    c.saveCases();
	    count++;
	}
	
	public int getSize(){
		return count;
	}
	
	public BigVariant getVariant(int pos) {
		return new BigVariant(pos + 1);
	}
}
