// generated with ast extension for cup
// version 0.8
// 26/7/2021 20:23:6


package rs.ac.bg.etf.pp1.ast;

public class IfStmt extends Statement {

    private IfKeyword IfKeyword;
    private ConditionEnd ConditionEnd;
    private IfStatements IfStatements;
    private ElsePart ElsePart;

    public IfStmt (IfKeyword IfKeyword, ConditionEnd ConditionEnd, IfStatements IfStatements, ElsePart ElsePart) {
        this.IfKeyword=IfKeyword;
        if(IfKeyword!=null) IfKeyword.setParent(this);
        this.ConditionEnd=ConditionEnd;
        if(ConditionEnd!=null) ConditionEnd.setParent(this);
        this.IfStatements=IfStatements;
        if(IfStatements!=null) IfStatements.setParent(this);
        this.ElsePart=ElsePart;
        if(ElsePart!=null) ElsePart.setParent(this);
    }

    public IfKeyword getIfKeyword() {
        return IfKeyword;
    }

    public void setIfKeyword(IfKeyword IfKeyword) {
        this.IfKeyword=IfKeyword;
    }

    public ConditionEnd getConditionEnd() {
        return ConditionEnd;
    }

    public void setConditionEnd(ConditionEnd ConditionEnd) {
        this.ConditionEnd=ConditionEnd;
    }

    public IfStatements getIfStatements() {
        return IfStatements;
    }

    public void setIfStatements(IfStatements IfStatements) {
        this.IfStatements=IfStatements;
    }

    public ElsePart getElsePart() {
        return ElsePart;
    }

    public void setElsePart(ElsePart ElsePart) {
        this.ElsePart=ElsePart;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(IfKeyword!=null) IfKeyword.accept(visitor);
        if(ConditionEnd!=null) ConditionEnd.accept(visitor);
        if(IfStatements!=null) IfStatements.accept(visitor);
        if(ElsePart!=null) ElsePart.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(IfKeyword!=null) IfKeyword.traverseTopDown(visitor);
        if(ConditionEnd!=null) ConditionEnd.traverseTopDown(visitor);
        if(IfStatements!=null) IfStatements.traverseTopDown(visitor);
        if(ElsePart!=null) ElsePart.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(IfKeyword!=null) IfKeyword.traverseBottomUp(visitor);
        if(ConditionEnd!=null) ConditionEnd.traverseBottomUp(visitor);
        if(IfStatements!=null) IfStatements.traverseBottomUp(visitor);
        if(ElsePart!=null) ElsePart.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("IfStmt(\n");

        if(IfKeyword!=null)
            buffer.append(IfKeyword.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ConditionEnd!=null)
            buffer.append(ConditionEnd.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(IfStatements!=null)
            buffer.append(IfStatements.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ElsePart!=null)
            buffer.append(ElsePart.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [IfStmt]");
        return buffer.toString();
    }
}
