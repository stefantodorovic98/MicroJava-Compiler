// generated with ast extension for cup
// version 0.8
// 26/7/2021 20:23:6


package rs.ac.bg.etf.pp1.ast;

public class FactorChar extends Factor {

    private Character char_const;

    public FactorChar (Character char_const) {
        this.char_const=char_const;
    }

    public Character getChar_const() {
        return char_const;
    }

    public void setChar_const(Character char_const) {
        this.char_const=char_const;
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
        buffer.append("FactorChar(\n");

        buffer.append(" "+tab+char_const);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FactorChar]");
        return buffer.toString();
    }
}
