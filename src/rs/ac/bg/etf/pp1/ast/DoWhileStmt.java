// generated with ast extension for cup
// version 0.8
// 26/7/2021 20:23:6


package rs.ac.bg.etf.pp1.ast;

public class DoWhileStmt extends Statement {

    private DoKeyword DoKeyword;
    private DoWhileStatements DoWhileStatements;
    private WhileKeyword WhileKeyword;
    private ConditionEndWhile ConditionEndWhile;

    public DoWhileStmt (DoKeyword DoKeyword, DoWhileStatements DoWhileStatements, WhileKeyword WhileKeyword, ConditionEndWhile ConditionEndWhile) {
        this.DoKeyword=DoKeyword;
        if(DoKeyword!=null) DoKeyword.setParent(this);
        this.DoWhileStatements=DoWhileStatements;
        if(DoWhileStatements!=null) DoWhileStatements.setParent(this);
        this.WhileKeyword=WhileKeyword;
        if(WhileKeyword!=null) WhileKeyword.setParent(this);
        this.ConditionEndWhile=ConditionEndWhile;
        if(ConditionEndWhile!=null) ConditionEndWhile.setParent(this);
    }

    public DoKeyword getDoKeyword() {
        return DoKeyword;
    }

    public void setDoKeyword(DoKeyword DoKeyword) {
        this.DoKeyword=DoKeyword;
    }

    public DoWhileStatements getDoWhileStatements() {
        return DoWhileStatements;
    }

    public void setDoWhileStatements(DoWhileStatements DoWhileStatements) {
        this.DoWhileStatements=DoWhileStatements;
    }

    public WhileKeyword getWhileKeyword() {
        return WhileKeyword;
    }

    public void setWhileKeyword(WhileKeyword WhileKeyword) {
        this.WhileKeyword=WhileKeyword;
    }

    public ConditionEndWhile getConditionEndWhile() {
        return ConditionEndWhile;
    }

    public void setConditionEndWhile(ConditionEndWhile ConditionEndWhile) {
        this.ConditionEndWhile=ConditionEndWhile;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DoKeyword!=null) DoKeyword.accept(visitor);
        if(DoWhileStatements!=null) DoWhileStatements.accept(visitor);
        if(WhileKeyword!=null) WhileKeyword.accept(visitor);
        if(ConditionEndWhile!=null) ConditionEndWhile.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DoKeyword!=null) DoKeyword.traverseTopDown(visitor);
        if(DoWhileStatements!=null) DoWhileStatements.traverseTopDown(visitor);
        if(WhileKeyword!=null) WhileKeyword.traverseTopDown(visitor);
        if(ConditionEndWhile!=null) ConditionEndWhile.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DoKeyword!=null) DoKeyword.traverseBottomUp(visitor);
        if(DoWhileStatements!=null) DoWhileStatements.traverseBottomUp(visitor);
        if(WhileKeyword!=null) WhileKeyword.traverseBottomUp(visitor);
        if(ConditionEndWhile!=null) ConditionEndWhile.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DoWhileStmt(\n");

        if(DoKeyword!=null)
            buffer.append(DoKeyword.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(DoWhileStatements!=null)
            buffer.append(DoWhileStatements.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(WhileKeyword!=null)
            buffer.append(WhileKeyword.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ConditionEndWhile!=null)
            buffer.append(ConditionEndWhile.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DoWhileStmt]");
        return buffer.toString();
    }
}
