package rs.ac.bg.etf.pp1;

import java.util.LinkedList;
import java.util.List;

import rs.ac.bg.etf.pp1.ast.LogicalAnd;
import rs.ac.bg.etf.pp1.ast.LogicalOr;
import rs.ac.bg.etf.pp1.ast.VisitorAdaptor;

public class LogicOperatorsCounter extends VisitorAdaptor {
	private static List<LogicalOperator> operatorsList = new LinkedList<>();
	private static LogicOperatorsCounter counter = null;
	private static int currIndex = 0;
	
	private LogicOperatorsCounter() {	}
	
	public void visit(LogicalOr logicalOr) {
		LogicalOperator operator = new LogicalOperator(OperatorType.OR, false);
		operatorsList.add(operator);
	}
	
	public void visit(LogicalAnd logicalAnd) {
		LogicalOperator operator = new LogicalOperator(OperatorType.AND, false);
		operatorsList.add(operator);
	}
	
	public static List<LogicalOperator> getOperatorsList() {
		return operatorsList;
	}
	
	public static LogicOperatorsCounter getLogicOperatorsCounter() {
		if(counter == null) {
			counter = new LogicOperatorsCounter();
		}
		return counter;
	}
	
	public static void resetLogicOperatorsCounter() {
		operatorsList.clear();
		counter = null;
		currIndex = 0;
	}
	
	public static boolean isOperatorsListEmpty() {
		return operatorsList.isEmpty();
	}
	
	public static void makeCurrOperatorVisited() {
		LogicalOperator operator = operatorsList.get(currIndex);
		operator.setVisited(true);
		operatorsList.set(currIndex, operator);
		currIndex++;
	}
	
	public static LogicalOperator getNextUnvisitedOperator() {
		for(int i = 0; i < operatorsList.size(); i++) {
			if(!operatorsList.get(i).isVisited()) return operatorsList.get(i);
		}
		return null;
	}
	
	public static LogicalOperator getNextUnvisitedOperator(OperatorType operatorType) {
		for(int i = 0; i < operatorsList.size(); i++) {
			if(!operatorsList.get(i).isVisited() && operatorsList.get(i).getType() == operatorType) return operatorsList.get(i);
		}
		return null;
	}
	
	public static void removeFromIndex(int index) {
		if(!isOperatorsListEmpty() && index >= 0 && index < operatorsList.size()) operatorsList.remove(index);
	}
	
	public static void printOperatorsList() {
		System.out.println(operatorsList.toString());
	}
	
}
