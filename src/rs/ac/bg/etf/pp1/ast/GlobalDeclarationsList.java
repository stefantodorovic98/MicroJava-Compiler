// generated with ast extension for cup
// version 0.8
// 26/7/2021 20:23:6


package rs.ac.bg.etf.pp1.ast;

public class GlobalDeclarationsList extends GlobalDeclList {

    private GlobalDeclList GlobalDeclList;
    private GlobalDeclItem GlobalDeclItem;

    public GlobalDeclarationsList (GlobalDeclList GlobalDeclList, GlobalDeclItem GlobalDeclItem) {
        this.GlobalDeclList=GlobalDeclList;
        if(GlobalDeclList!=null) GlobalDeclList.setParent(this);
        this.GlobalDeclItem=GlobalDeclItem;
        if(GlobalDeclItem!=null) GlobalDeclItem.setParent(this);
    }

    public GlobalDeclList getGlobalDeclList() {
        return GlobalDeclList;
    }

    public void setGlobalDeclList(GlobalDeclList GlobalDeclList) {
        this.GlobalDeclList=GlobalDeclList;
    }

    public GlobalDeclItem getGlobalDeclItem() {
        return GlobalDeclItem;
    }

    public void setGlobalDeclItem(GlobalDeclItem GlobalDeclItem) {
        this.GlobalDeclItem=GlobalDeclItem;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(GlobalDeclList!=null) GlobalDeclList.accept(visitor);
        if(GlobalDeclItem!=null) GlobalDeclItem.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(GlobalDeclList!=null) GlobalDeclList.traverseTopDown(visitor);
        if(GlobalDeclItem!=null) GlobalDeclItem.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(GlobalDeclList!=null) GlobalDeclList.traverseBottomUp(visitor);
        if(GlobalDeclItem!=null) GlobalDeclItem.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("GlobalDeclarationsList(\n");

        if(GlobalDeclList!=null)
            buffer.append(GlobalDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(GlobalDeclItem!=null)
            buffer.append(GlobalDeclItem.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [GlobalDeclarationsList]");
        return buffer.toString();
    }
}
