// generated with ast extension for cup
// version 0.8
// 26/7/2021 20:23:6


package rs.ac.bg.etf.pp1.ast;

public class ScalarDesignator extends Designator {

    private String name;

    public ScalarDesignator (String name) {
        this.name=name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name=name;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ScalarDesignator(\n");

        buffer.append(" "+tab+name);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ScalarDesignator]");
        return buffer.toString();
    }
}
