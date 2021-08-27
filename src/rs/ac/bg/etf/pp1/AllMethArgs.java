package rs.ac.bg.etf.pp1;

import java.util.LinkedList;
import java.util.List;

import rs.etf.pp1.symboltable.concepts.Obj;

public class AllMethArgs {
	public static List<MethArgs> list = new LinkedList<>();
	
	public static MethArgs getForObject(Obj meth) {
		for(int i = 0; i < list.size(); i++) {
			if(list.get(i).method == meth) return list.get(i);
		}
		return null;
	}
}
