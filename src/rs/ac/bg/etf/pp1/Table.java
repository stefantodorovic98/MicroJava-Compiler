package rs.ac.bg.etf.pp1;

import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Scope;
import rs.etf.pp1.symboltable.concepts.Struct;
import rs.etf.pp1.symboltable.visitors.SymbolTableVisitor;

public class Table extends Tab {
	
	public static final Struct boolType = new Struct(Struct.Bool);
	
	public static void initialization() {
		init();
		currentScope.addToLocals(new Obj(Obj.Type, "bool", boolType));
	}
	
	//Provera najuzeg opsega
	public static Obj existInCurrentScope(String name) {
		if (currentScope.getLocals() != null) {
			Obj resultObj = currentScope.getLocals().searchKey(name);
			if (resultObj != null) return resultObj; 
		}
		return noObj;
	}
	
	public static void tsdump(SymbolTableVisitor stv) {
		System.out.println("=====================SYMBOL TABLE DUMP=========================");
		if (stv == null)
			stv = new TSDumpSymbolTableVisitor();
		for (Scope s = currentScope; s != null; s = s.getOuter()) {
			s.accept(stv);
		}
		System.out.println(stv.getOutput());
	}
	
	public static void tsdump() {
		tsdump(null);
	}

}
