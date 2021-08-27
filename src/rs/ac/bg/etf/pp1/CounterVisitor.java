package rs.ac.bg.etf.pp1;

import rs.ac.bg.etf.pp1.ast.ArrayDeclarationItem;
import rs.ac.bg.etf.pp1.ast.ArrayParam;
import rs.ac.bg.etf.pp1.ast.ScalarParam;
import rs.ac.bg.etf.pp1.ast.VarDeclarationItem;
import rs.ac.bg.etf.pp1.ast.VisitorAdaptor;

public class CounterVisitor extends VisitorAdaptor {
	
	protected int count;
	
	public int getCount() {
		return count;
	}
	
	public static class FormParamCounter extends CounterVisitor {
		
		public void visit(ScalarParam param) {
			count++;
		}
		
		public void visit(ArrayParam param) {
			count++;
		}
	}
	
	public static class VarCounter extends CounterVisitor {
		
		public void visit(VarDeclarationItem var) {
			count++;
		}
		
		public void visit(ArrayDeclarationItem var) {
			count++;
		}
	}
}
