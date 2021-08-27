// generated with ast extension for cup
// version 0.8
// 26/7/2021 20:23:6


package rs.ac.bg.etf.pp1.ast;

public class OneVarDeclarationItem extends VarDeclList {

    private VarDeclItem VarDeclItem;

    public OneVarDeclarationItem (VarDeclItem VarDeclItem) {
        this.VarDeclItem=VarDeclItem;
        if(VarDeclItem!=null) VarDeclItem.setParent(this);
    }

    public VarDeclItem getVarDeclItem() {
        return VarDeclItem;
    }

    public void setVarDeclItem(VarDeclItem VarDeclItem) {
        this.VarDeclItem=VarDeclItem;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(VarDeclItem!=null) VarDeclItem.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(VarDeclItem!=null) VarDeclItem.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(VarDeclItem!=null) VarDeclItem.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("OneVarDeclarationItem(\n");

        if(VarDeclItem!=null)
            buffer.append(VarDeclItem.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [OneVarDeclarationItem]");
        return buffer.toString();
    }
}
