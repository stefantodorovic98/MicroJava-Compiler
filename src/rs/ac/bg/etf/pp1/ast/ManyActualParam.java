// generated with ast extension for cup
// version 0.8
// 26/7/2021 20:23:6


package rs.ac.bg.etf.pp1.ast;

public class ManyActualParam extends ActualParsList {

    private ActualParsList ActualParsList;
    private ActualParam ActualParam;

    public ManyActualParam (ActualParsList ActualParsList, ActualParam ActualParam) {
        this.ActualParsList=ActualParsList;
        if(ActualParsList!=null) ActualParsList.setParent(this);
        this.ActualParam=ActualParam;
        if(ActualParam!=null) ActualParam.setParent(this);
    }

    public ActualParsList getActualParsList() {
        return ActualParsList;
    }

    public void setActualParsList(ActualParsList ActualParsList) {
        this.ActualParsList=ActualParsList;
    }

    public ActualParam getActualParam() {
        return ActualParam;
    }

    public void setActualParam(ActualParam ActualParam) {
        this.ActualParam=ActualParam;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ActualParsList!=null) ActualParsList.accept(visitor);
        if(ActualParam!=null) ActualParam.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ActualParsList!=null) ActualParsList.traverseTopDown(visitor);
        if(ActualParam!=null) ActualParam.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ActualParsList!=null) ActualParsList.traverseBottomUp(visitor);
        if(ActualParam!=null) ActualParam.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ManyActualParam(\n");

        if(ActualParsList!=null)
            buffer.append(ActualParsList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ActualParam!=null)
            buffer.append(ActualParam.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ManyActualParam]");
        return buffer.toString();
    }
}
