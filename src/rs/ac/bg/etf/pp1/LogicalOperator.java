package rs.ac.bg.etf.pp1;

public class LogicalOperator {
	private OperatorType type;
	private boolean visited;

	public LogicalOperator(OperatorType type, boolean visited) {
		this.type = type;
		this.visited = visited;
	}

	public OperatorType getType() {
		return type;
	}
	
	public void setType(OperatorType type) {
		this.type = type;
	}
	
	public boolean isVisited() {
		return visited;
	}
	
	public void setVisited(boolean visited) {
		this.visited = visited;
	}
	
	@Override
	public String toString() {
		return type.name() + " " + new Boolean(visited).toString();
	}
}
