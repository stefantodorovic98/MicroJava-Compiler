// generated with ast extension for cup
// version 0.8
// 26/7/2021 20:23:6


package rs.ac.bg.etf.pp1.ast;

public class ArrayDesignator extends Designator {

    private ArrayDesignatorName ArrayDesignatorName;
    private Expr Expr;

    public ArrayDesignator (ArrayDesignatorName ArrayDesignatorName, Expr Expr) {
        this.ArrayDesignatorName=ArrayDesignatorName;
        if(ArrayDesignatorName!=null) ArrayDesignatorName.setParent(this);
        this.Expr=Expr;
        if(Expr!=null) Expr.setParent(this);
    }

    public ArrayDesignatorName getArrayDesignatorName() {
        return ArrayDesignatorName;
    }

    public void setArrayDesignatorName(ArrayDesignatorName ArrayDesignatorName) {
        this.ArrayDesignatorName=ArrayDesignatorName;
    }

    public Expr getExpr() {
        return Expr;
    }

    public void setExpr(Expr Expr) {
        this.Expr=Expr;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ArrayDesignatorName!=null) ArrayDesignatorName.accept(visitor);
        if(Expr!=null) Expr.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ArrayDesignatorName!=null) ArrayDesignatorName.traverseTopDown(visitor);
        if(Expr!=null) Expr.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ArrayDesignatorName!=null) ArrayDesignatorName.traverseBottomUp(visitor);
        if(Expr!=null) Expr.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ArrayDesignator(\n");

        if(ArrayDesignatorName!=null)
            buffer.append(ArrayDesignatorName.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Expr!=null)
            buffer.append(Expr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ArrayDesignator]");
        return buffer.toString();
    }
}
