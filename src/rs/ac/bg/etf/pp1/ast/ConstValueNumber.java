// generated with ast extension for cup
// version 0.8
// 26/7/2021 20:23:6


package rs.ac.bg.etf.pp1.ast;

public class ConstValueNumber extends ConstItemValue {

    private Integer num_const;

    public ConstValueNumber (Integer num_const) {
        this.num_const=num_const;
    }

    public Integer getNum_const() {
        return num_const;
    }

    public void setNum_const(Integer num_const) {
        this.num_const=num_const;
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
        buffer.append("ConstValueNumber(\n");

        buffer.append(" "+tab+num_const);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstValueNumber]");
        return buffer.toString();
    }
}
