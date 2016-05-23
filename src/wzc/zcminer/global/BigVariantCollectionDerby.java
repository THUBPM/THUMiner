package wzc.zcminer.global;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

//variant集合类
public class BigVariantCollectionDerby {
	int count;

	public BigVariantCollectionDerby() {
		// TODO Auto-generated constructor stub
		count = 0;
	}

	public void addVariant(BigVariantDerby c) {
	    c.insert();
	    count++;
	}
	
	public int getSize(){
		return count;
	}
	
	public BigVariantDerby getVariant(int pos) {
		return new BigVariantDerby(pos + 1);
	}
}
