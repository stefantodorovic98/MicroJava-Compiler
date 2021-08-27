// generated with ast extension for cup
// version 0.8
// 26/7/2021 20:23:6


package rs.ac.bg.etf.pp1.ast;

public class ConstDeclarationItem extends ConstDeclItem {

    private String constName;
    private ConstItemValue ConstItemValue;

    public ConstDeclarationItem (String constName, ConstItemValue ConstItemValue) {
        this.constName=constName;
        this.ConstItemValue=ConstItemValue;
        if(ConstItemValue!=null) ConstItemValue.setParent(this);
    }

    public String getConstName() {
        return constName;
    }

    public void setConstName(String constName) {
        this.constName=constName;
    }

    public ConstItemValue getConstItemValue() {
        return ConstItemValue;
    }

    public void setConstItemValue(ConstItemValue ConstItemValue) {
        this.ConstItemValue=ConstItemValue;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ConstItemValue!=null) ConstItemValue.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConstItemValue!=null) ConstItemValue.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConstItemValue!=null) ConstItemValue.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstDeclarationItem(\n");

        buffer.append(" "+tab+constName);
        buffer.append("\n");

        if(ConstItemValue!=null)
            buffer.append(ConstItemValue.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstDeclarationItem]");
        return buffer.toString();
    }
}
