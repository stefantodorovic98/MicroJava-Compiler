// generated with ast extension for cup
// version 0.8
// 26/7/2021 20:23:6


package rs.ac.bg.etf.pp1.ast;

public class ConstValueBool extends ConstItemValue {

    private String bool_const;

    public ConstValueBool (String bool_const) {
        this.bool_const=bool_const;
    }

    public String getBool_const() {
        return bool_const;
    }

    public void setBool_const(String bool_const) {
        this.bool_const=bool_const;
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
        buffer.append("ConstValueBool(\n");

        buffer.append(" "+tab+bool_const);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstValueBool]");
        return buffer.toString();
    }
}
