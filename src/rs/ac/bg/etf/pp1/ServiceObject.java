package rs.ac.bg.etf.pp1;

import java.util.LinkedList;
import java.util.List;

public class ServiceObject {
	public static ServiceObject current = null;
	private ServiceObject node =  null;
	
	public List<Integer> nextOrList = new LinkedList<>();
	public List<Integer> elseEndList = new LinkedList<>();
	public List<Integer> justEndList = new LinkedList<>(); 
	public List<Integer> thenList = new LinkedList<>();
	
	public int labelDoWhile = 0;
	// 1 za if, 2 za do...while
	public int type = 0;
	
	public static void beginConditionBlock() {
		if(current == null) current = new ServiceObject();
		else {
			ServiceObject temp = new ServiceObject();
			temp.setNode(current);
			current = temp;
		}
	}
	
	public static void endConditionBlock() {
		if(current.node == null) current = null;
		else {
			current = current.getNode();  
		}
	}
	
	public void setNode(ServiceObject node) {
		this.node = node;
	}
	
	public ServiceObject getNode() {
		return this.node;
	}
}
