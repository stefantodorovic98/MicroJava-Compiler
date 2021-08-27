package rs.ac.bg.etf.pp1;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import rs.ac.bg.etf.pp1.CounterVisitor.FormParamCounter;
import rs.ac.bg.etf.pp1.CounterVisitor.VarCounter;
import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;

public class CodeGenerator extends VisitorAdaptor {
	
	private int mainPc;
	
	public int getMainPc() {
		return mainPc;
	}
	
	public void visit(ProgName programName) {		
		//chr
		Obj chr = Tab.find("chr");
		chr.setAdr(Code.pc);
		Code.put(Code.enter);
		Code.put(1);
		Code.put(1);
		Code.put(Code.load_n);
		Code.put(Code.exit);
		Code.put(Code.return_);
		
		//ord
		Obj ord = Tab.find("ord");
		ord.setAdr(Code.pc);
		Code.put(Code.enter);
		Code.put(1);
		Code.put(1);
		Code.put(Code.load_n);
		Code.put(Code.exit);
		Code.put(Code.return_);
		
		//len
		Obj len = Tab.find("len");
		len.setAdr(Code.pc);
		Code.put(Code.enter);
		Code.put(1);
		Code.put(1);
		Code.put(Code.load_n);
		Code.put(Code.arraylength);
		Code.put(Code.exit);
		Code.put(Code.return_);
		
		//vecMulVec
		vecMulVecInit();
		scaMulVecInit();
		vecMulScaInit();
	}
	
	public void visit(Program program) {
		 Iterator<Map.Entry<String, Integer>> itr = adrMap.entrySet().iterator();
		 while(itr.hasNext()) {
	             Map.Entry<String, Integer> entry = itr.next();
	             String label = entry.getKey();
	             int adr = entry.getValue();
	             int labelAdr = labelMap.get(label);
	             jumpOnLabel(adr, labelAdr);
	       }
	}
	
	public void visit(MethodReturnType meth) {
		if("main".equalsIgnoreCase(meth.getName())) {
			mainPc = Code.pc;
		}
		meth.obj.setAdr(Code.pc);
		
		SyntaxNode methNode = meth.getParent();
		VarCounter varCnt = new VarCounter();
		methNode.traverseTopDown(varCnt);
		FormParamCounter fpCnt = new FormParamCounter();
		methNode.traverseTopDown(fpCnt);
		
		Code.put(Code.enter);
		Code.put(fpCnt.getCount());
		Code.put(fpCnt.getCount() + varCnt.getCount());
	}
	
	public void visit(MethodReturnVoid meth) {
		if("main".equalsIgnoreCase(meth.getName())) {
			mainPc = Code.pc;
		}
		meth.obj.setAdr(Code.pc);
		
		SyntaxNode methNode = meth.getParent();
		VarCounter varCnt = new VarCounter();
		methNode.traverseTopDown(varCnt);
		FormParamCounter fpCnt = new FormParamCounter();
		methNode.traverseTopDown(fpCnt);

		Code.put(Code.enter);
		Code.put(fpCnt.getCount());
		Code.put(fpCnt.getCount() + varCnt.getCount());
	}
	
	public void visit(MethodDeclaration meth) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	public void visit(ArrayDesignatorName desig) {
		Code.load(desig.obj);
	}
	
	public void visit(ArrayDesignator desig) {
		Code.put(Code.dup2);
		Code.put(Code.pop);
		Code.put(Code.arraylength);
		Code.put(Code.dup2);
		Code.putFalseJump(Code.lt, 0);
		int adr1 = Code.pc - 2;
		Code.put(Code.pop);
		Code.put(Code.dup);
		Code.loadConst(0);
		Code.putFalseJump(Code.ge, 0);
		int adr2 = Code.pc - 2;
		Code.putJump(0);
		int adr3 = Code.pc - 2;
		
		Code.fixup(adr1);
		Code.fixup(adr2);
		Code.put(Code.trap);
		Code.put(1);
		
		Code.fixup(adr3);
	}
	
	public void visit(FactorDesignator desig) {
		if(desig.getDesignator() instanceof ScalarDesignator) {
			Code.load(desig.getDesignator().obj);
		} else if(desig.getDesignator() instanceof ArrayDesignator) {
			ArrayDesignator arr = (ArrayDesignator) desig.getDesignator();
			if(!arr.obj.getType().getElemType().equals(Tab.charType)) {
				Code.put(Code.aload);
			} else {
				Code.put(Code.baload);
			}
		}
	}
	
	public void visit(FactorNumber fact) {
		Code.loadConst(fact.getNum_const());
	}
	
	public void visit(FactorChar fact) {
		Code.loadConst(fact.getChar_const());
	}
	
	public void visit(FactorBool fact) {
		String res = fact.getBool_const();
		boolean b = Boolean.valueOf(res);
		Code.loadConst((b)? 1 : 0);
	}
	
	//za new
	public void visit(FactorArray arr) {
		Code.put(Code.newarray);
		if(!arr.struct.getElemType().equals(Tab.charType)) {
			Code.put(1);
		} else {
			Code.put(0);
		}
	}
	
	public void visit(TermMul op) {
		if(op.getMulop() instanceof MulOperation) {
			Code.put(Code.mul);
		} else if (op.getMulop() instanceof DivOperation) {
			Code.put(Code.div);
		} else if (op.getMulop() instanceof RemOperation) {
			Code.put(Code.rem);
		}
	}
	
	public void visit(ExprAdd op) {
		if(op.getAddop() instanceof PlusOperation) {
			Code.put(Code.add);
		} else if (op.getAddop() instanceof MinusOperation) {
			Code.put(Code.sub);
		}
	}
	
	public void visit(ExprMTerm op) {
		Code.put(Code.neg);
	}
	
//	public void visit(ExprTernary expr) {
//		Code.put(Code.dup_x2);
//		Code.put(Code.pop);
//		Code.put(Code.dup_x2);
//		Code.put(Code.pop);
//		Code.loadConst(1);
//		Code.putFalseJump(Code.ge, 0);
//		int adr = Code.pc - 2;
//		Code.put(Code.pop);
//		Code.putJump(0);
//		int adr2 = Code.pc - 2;
//		Code.fixup(adr);
//		Code.put(Code.dup_x1);
//		Code.put(Code.pop);
//		Code.put(Code.pop);
//		Code.fixup(adr2);
//	}
	
	public void visit(DesignatorAssignValue assign) {
		if(assign.getDesignator() instanceof ScalarDesignator) {
			Code.store(assign.getDesignator().obj);
		} else if (assign.getDesignator() instanceof ArrayDesignator) {
			ArrayDesignator arr = (ArrayDesignator) assign.getDesignator();
			if(!arr.obj.getType().getElemType().equals(Tab.charType)) {
				Code.put(Code.astore);
			} else {
				Code.put(Code.bastore);
			}
		}
		
	}
	
	public void visit(DesignatorIncValue inc) {
		if(inc.getDesignator() instanceof ScalarDesignator) {
			Code.load(inc.getDesignator().obj);
			Code.loadConst(1);
			Code.put(Code.add);
			Code.store(inc.getDesignator().obj);
		} else if (inc.getDesignator() instanceof ArrayDesignator) {
			ArrayDesignator arr = (ArrayDesignator) inc.getDesignator();
			Code.put(Code.dup2);
			if(!arr.obj.getType().getElemType().equals(Tab.charType)) {
				Code.put(Code.aload);
			} else {
				Code.put(Code.baload);
			}
			Code.loadConst(1);
			Code.put(Code.add);
			if(!arr.obj.getType().getElemType().equals(Tab.charType)) {
				Code.put(Code.astore);
			} else {
				Code.put(Code.bastore);
			}
		}
	}
	
	public void visit(DesignatorDecValue dec) {
		if(dec.getDesignator() instanceof ScalarDesignator) {
			Code.load(dec.getDesignator().obj);
			Code.loadConst(1);
			Code.put(Code.sub);
			Code.store(dec.getDesignator().obj);
		} else if (dec.getDesignator() instanceof ArrayDesignator) {
			ArrayDesignator arr = (ArrayDesignator) dec.getDesignator();
			Code.put(Code.dup2);
			if(!arr.obj.getType().getElemType().equals(Tab.charType)) {
				Code.put(Code.aload);
			} else {
				Code.put(Code.baload);
			}
			Code.loadConst(1);
			Code.put(Code.sub);
			if(!arr.obj.getType().getElemType().equals(Tab.charType)) {
				Code.put(Code.astore);
			} else {
				Code.put(Code.bastore);
			}
		}
	}
	
	public void visit(PrintStmt stmt) {
		if(stmt.getNumConst() instanceof NumberConstant) {
			NumberConstant t = (NumberConstant) stmt.getNumConst();
			Code.loadConst(t.getNumber());
		} else {
			Code.loadConst(5);
		}
		if(stmt.getExpr().struct == Tab.intType || stmt.getExpr().struct == Table.boolType) {
			Code.put(Code.print);
		} else {
			Code.put(Code.bprint);
		}
	}
	
	public void visit(ReadStmt stmt) {
		if(stmt.getDesignator() instanceof ScalarDesignator) {
			if(!stmt.getDesignator().obj.getType().equals(Tab.charType)) {
				Code.put(Code.read);
			} else {
				Code.put(Code.bread);
			}
			Code.store(stmt.getDesignator().obj);
		} else if (stmt.getDesignator() instanceof ArrayDesignator) {
			ArrayDesignator arr = (ArrayDesignator) stmt.getDesignator();
			//!stmt.getDesignator().obj.getType().equals(Tab.charType)
			if(!arr.obj.getType().getElemType().equals(Tab.charType)) {
				Code.put(Code.read);
			} else {
				Code.put(Code.bread);
			}
			if(!arr.obj.getType().getElemType().equals(Tab.charType)) {
				Code.put(Code.astore);
			} else {
				Code.put(Code.bastore);
			}
		}
	}
	
	public void visit(IfKeyword ifKeyword) {
		SyntaxNode ifNode = ifKeyword.getParent();
		IfStmt ifStmt = null;
		if(ifNode instanceof IfStmt) {
			ifStmt = (IfStmt) ifNode;
			Condition condition = ifStmt.getConditionEnd().getCondition();
			LogicOperatorsCounter counter = LogicOperatorsCounter.getLogicOperatorsCounter();
			condition.traverseTopDown(counter);
			ServiceObject.beginConditionBlock();
			ServiceObject.current.type = 1;
		}
	}
	
	public void visit(IfStmt ifStmt) {
		List<Integer> list = ServiceObject.current.justEndList;
		for(int i = 0; i < list.size(); i++) {
			Code.fixup(list.get(i));
		}
		list.clear();
		ServiceObject.endConditionBlock();
	}
	
	public void visit(ConditionEnd conditionEnd) {
		List<Integer> list = ServiceObject.current.thenList;
		for(int i = 0; i < list.size(); i++) {
			Code.fixup(list.get(i));
		}
		list.clear();
		if(!LogicOperatorsCounter.isOperatorsListEmpty()) LogicOperatorsCounter.resetLogicOperatorsCounter();
	}
	
	public void visit(LogicalOr logicalOr) {
		List<Integer> list = ServiceObject.current.nextOrList;
		for(int i = 0; i < list.size(); i++) {
			Code.fixup(list.get(i));
		}
		list.clear();
		LogicOperatorsCounter.makeCurrOperatorVisited();
	}
	
	public void visit(LogicalAnd logicalAnd) {
		LogicOperatorsCounter.makeCurrOperatorVisited();
	}
	
	public void visit(IfStatements ifStatements) {
		SyntaxNode ifNode = ifStatements.getParent();
		IfStmt ifStmt = null;
		if(ifNode instanceof IfStmt) {
			ifStmt = (IfStmt) ifNode;
			ElsePart part = ifStmt.getElsePart();
			if(part instanceof YesElsePart) {
				Code.putJump(0);
				int adr = Code.pc - 2;
				ServiceObject.current.justEndList.add(adr);
				List<Integer> list = ServiceObject.current.elseEndList;
				for(int i = 0; i < list.size(); i++) {
					Code.fixup(list.get(i));
				}
				list.clear();
			} else if(part instanceof NoElsePart) {
				List<Integer> list = ServiceObject.current.elseEndList;
				for(int i = 0; i < list.size(); i++) {
					Code.fixup(list.get(i));
				}
				list.clear();
			}
		}
	}
	
	public void visit(CondFact condFact) {
		if(condFact.getSecondExpr() instanceof YesSecExpr) {
			YesSecExpr secExpr = (YesSecExpr) condFact.getSecondExpr();
			if(secExpr.getRelop() instanceof EqualOperation) {
				jumpHelper2Expr(Code.eq, Code.inverse[Code.eq]);
			} else if (secExpr.getRelop() instanceof NotEqualOperation) {
				jumpHelper2Expr(Code.ne, Code.inverse[Code.ne]);
			} else if (secExpr.getRelop() instanceof GreatOperation) {
				jumpHelper2Expr(Code.gt, Code.inverse[Code.gt]);
			} else if (secExpr.getRelop() instanceof GreatEqualOperation) {
				jumpHelper2Expr(Code.ge, Code.inverse[Code.ge]);
			} else if (secExpr.getRelop() instanceof LessOperation) {
				jumpHelper2Expr(Code.lt, Code.inverse[Code.lt]);
			} else if (secExpr.getRelop() instanceof LessEqualOperation) {
				jumpHelper2Expr(Code.le, Code.inverse[Code.le]);
			}
		} else if(condFact.getSecondExpr() instanceof NoSecExpr) {
			jumpHelper1Expr();
		}
	}
	
	private void jumpHelper2Expr(int directCond, int inverseCond) {
		if(LogicOperatorsCounter.getNextUnvisitedOperator() == null) {
			Code.putFalseJump(directCond, 0);
			int adr = Code.pc - 2;
			ServiceObject.current.elseEndList.add(adr);
		} else if(LogicOperatorsCounter.getNextUnvisitedOperator().getType() == OperatorType.AND) {
			Code.putFalseJump(directCond, 0);
			int adr = Code.pc - 2;
			if(LogicOperatorsCounter.getNextUnvisitedOperator(OperatorType.OR) == null) {
				ServiceObject.current.elseEndList.add(adr);
			} else {
				ServiceObject.current.nextOrList.add(adr);
			}
		} else if(LogicOperatorsCounter.getNextUnvisitedOperator().getType() == OperatorType.OR) {
			Code.putFalseJump(inverseCond, 0);
			int adr = Code.pc - 2;
			ServiceObject.current.thenList.add(adr);
		}
	}
	
	private void jumpHelper1Expr() {
		if(LogicOperatorsCounter.getNextUnvisitedOperator() == null) {
			Code.loadConst(0);
			Code.putFalseJump(Code.gt, 0);
			int adr = Code.pc - 2;
			ServiceObject.current.elseEndList.add(adr);
		} else if(LogicOperatorsCounter.getNextUnvisitedOperator().getType() == OperatorType.AND) {
			Code.loadConst(0);
			Code.putFalseJump(Code.gt, 0);
			int adr = Code.pc - 2;
			if(LogicOperatorsCounter.getNextUnvisitedOperator(OperatorType.OR) == null) {
				ServiceObject.current.elseEndList.add(adr);
			} else {
				ServiceObject.current.nextOrList.add(adr);
			}
		} else if(LogicOperatorsCounter.getNextUnvisitedOperator().getType() == OperatorType.OR) {
			Code.loadConst(0);
			Code.putFalseJump(Code.le, 0);
			int adr = Code.pc - 2;
			ServiceObject.current.thenList.add(adr);
		}
	}
	
	//B nivo
	
	public void visit(DoKeyword doKeyword) {
		ServiceObject.beginConditionBlock();
		ServiceObject.current.labelDoWhile = Code.pc;
		ServiceObject.current.type = 2;
	}
	
	public void visit(WhileKeyword whileKeyword) {
		SyntaxNode doWhileNode = whileKeyword.getParent();
		DoWhileStmt doWhileStmt = null;
		if(doWhileNode instanceof DoWhileStmt) {
			doWhileStmt = (DoWhileStmt) doWhileNode;
			Condition condition = doWhileStmt.getConditionEndWhile().getCondition();
			LogicOperatorsCounter counter = LogicOperatorsCounter.getLogicOperatorsCounter();
			condition.traverseTopDown(counter);
		}
	}
	
	public void visit(ConditionEndWhile conditionEndWhile) {
		List<Integer> list = ServiceObject.current.thenList;
		for(int i = 0; i < list.size(); i++) {
			//skoci na labelu
			jumpOnLabel(list.get(i), ServiceObject.current.labelDoWhile);
		}
		list.clear();
		//bezuslovni skok na pocetak do while bloka
		Code.putJump(ServiceObject.current.labelDoWhile);
		if(!LogicOperatorsCounter.isOperatorsListEmpty()) LogicOperatorsCounter.resetLogicOperatorsCounter();
	}
	
	public void visit(DoWhileStmt doWhileStmt) {
		List<Integer> list = ServiceObject.current.elseEndList;
		for(int i = 0; i < list.size(); i++) {
			Code.fixup(list.get(i));
		}
		list.clear();
		ServiceObject.endConditionBlock();
	}
	
	public void jumpOnLabel(int adr, int label) {
		Code.put2(adr, (label - adr + 1));
	}
	
	public void visit(BreakStmt breakStmt) {
		Code.putJump(0);
		int adr = Code.pc - 2;
		ServiceObject temp = ServiceObject.current;
		while(temp != null && temp.type == 1) {
			temp = temp.getNode();
		}
		if(temp != null) {
			temp.elseEndList.add(adr);
		}
	}
	
	public void visit(ContinueStmt continueStmt) {
		Code.putJump(0);
		int adr = Code.pc - 2;
		ServiceObject temp = ServiceObject.current;
		while(temp != null && temp.type == 1) {
			temp = temp.getNode();
		}
		if(temp != null) {
			temp.thenList.add(adr);
		}
	}
	
	public void visit(DesignatorProcCall stmt) {
		int offset = stmt.getDesignator().obj.getAdr() - Code.pc;
		Code.put(Code.call);
		Code.put2(offset);
		if(!stmt.getDesignator().obj.getType().equals(Tab.noType) ){
			Code.put(Code.pop);
		}
	}
	
	public void visit(FactorFuncCall stmt) {
		int offset = stmt.getDesignator().obj.getAdr() - Code.pc;
		Code.put(Code.call);
		Code.put2(offset);
	}
	
	public void visit(YesReturnExpr stmt) {
//		Code.put(Code.exit);
//		Code.put(Code.return_);
	}
	
	public void visit(NoReturnExpr stmt) {
//		Code.put(Code.exit);
//		Code.put(Code.return_);
	}
	
	//B nivo - Dodatak
	
	public void vecMulVecInit() {
		Obj vecMulVec = Tab.find("vecMulVec");
		vecMulVec.setAdr(Code.pc);
		Code.put(Code.enter);
		Code.put(2);
		Code.put(2);
		
		Code.put(Code.load_n);
		Code.put(Code.arraylength);
		Code.put(Code.load_1);
		Code.put(Code.arraylength);
		Code.putFalseJump(Code.ne, 0);
		int adr1 = Code.pc - 2;
		
		Code.put(Code.trap);
		Code.put(1);
		
		Code.fixup(adr1);
		
		Code.put(Code.load_n);
		Code.put(Code.arraylength);
		Code.loadConst(0);
		Code.putFalseJump(Code.ne, 0);
		int adr2 = Code.pc - 2;
		
		Code.loadConst(0);
		Code.put(Code.load_n);
		Code.put(Code.arraylength);
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.put(Code.dup_x1);
		
		int label = Code.pc;	//do
		Code.put(Code.load_n);
		Code.put(Code.dup2);
		Code.put(Code.pop);
		Code.put(Code.aload);
		Code.put(Code.dup_x1);
		Code.put(Code.pop);
		Code.put(Code.load_1);
		Code.put(Code.dup2);
		Code.put(Code.pop);
		Code.put(Code.aload);
		Code.put(Code.dup_x1);
		Code.put(Code.pop);
		Code.put(Code.pop);
		Code.put(Code.mul);
		Code.put(Code.add);
		Code.put(Code.dup2);
		Code.put(Code.pop);
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.put(Code.dup_x2);//
		Code.put(Code.pop);
		Code.put(Code.dup_x1);
		Code.put(Code.pop);
		Code.put(Code.pop);
		Code.put(Code.dup2);
		Code.put(Code.pop);
		Code.put(Code.dup);
		Code.loadConst(0);
		Code.putFalseJump(Code.lt, 0); // while
		int adr3 = Code.pc - 2;
		jumpOnLabel(adr3, label);
		
		Code.put(Code.pop);
		Code.put(Code.dup_x1);
		Code.put(Code.pop);
		Code.put(Code.pop);
		
		Code.put(Code.exit);
		Code.put(Code.return_);
		
		Code.fixup(adr2);	// ako je duzina oba niza 0 vrati 0
		
		Code.loadConst(0);
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	public void scaMulVecInit() {
		Obj scaMulVec = Tab.find("scaMulVec");
		scaMulVec.setAdr(Code.pc);
		Code.put(Code.enter);
		Code.put(2);
		Code.put(2);
		
		Code.put(Code.load_1);
		Code.put(Code.arraylength);
		Code.loadConst(0);
		Code.putFalseJump(Code.ne, 0);
		int adr1 = Code.pc - 2;
		
		Code.put(Code.load_1);
		Code.put(Code.arraylength);
		Code.put(Code.newarray);
		Code.put(1);
		
		Code.put(Code.load_1);
		Code.put(Code.arraylength);
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.put(Code.dup_x1);
		
		int label = Code.pc;
		Code.put(Code.dup2);
		Code.put(Code.load_1);
		Code.put(Code.dup2);
		Code.put(Code.pop);
		Code.put(Code.aload);
		Code.put(Code.load_n);
		Code.put(Code.mul);
		Code.put(Code.astore);
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.put(Code.dup_x2);
		Code.put(Code.pop);
		Code.put(Code.dup_x1);
		Code.put(Code.pop);
		Code.put(Code.pop);
		Code.put(Code.dup2);
		Code.put(Code.pop);
		Code.put(Code.dup);
		Code.loadConst(0);
		Code.putFalseJump(Code.lt, 0);
		int adr2 = Code.pc - 2;
		jumpOnLabel(adr2, label);
		
		Code.put(Code.pop);
		Code.put(Code.dup_x1);
		Code.put(Code.pop);
		Code.put(Code.pop);
		
		Code.put(Code.exit);
		Code.put(Code.return_);
		
		Code.fixup(adr1);
		
		Code.put(Code.load_1);
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	public void vecMulScaInit() {
		Obj vecMulSca = Tab.find("vecMulSca");
		vecMulSca.setAdr(Code.pc);
		Code.put(Code.enter);
		Code.put(2);
		Code.put(2);
		
		Code.put(Code.load_n);
		Code.put(Code.arraylength);
		Code.loadConst(0);
		Code.putFalseJump(Code.ne, 0);
		int adr1 = Code.pc - 2;
		
		Code.put(Code.load_n);
		Code.put(Code.arraylength);
		Code.put(Code.newarray);
		Code.put(1);
		
		Code.put(Code.load_n);
		Code.put(Code.arraylength);
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.put(Code.dup_x1);
		
		int label = Code.pc;
		Code.put(Code.dup2);
		Code.put(Code.load_n);
		Code.put(Code.dup2);
		Code.put(Code.pop);
		Code.put(Code.aload);
		Code.put(Code.load_1);
		Code.put(Code.mul);
		Code.put(Code.astore);
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.put(Code.dup_x2);
		Code.put(Code.pop);
		Code.put(Code.dup_x1);
		Code.put(Code.pop);
		Code.put(Code.pop);
		Code.put(Code.dup2);
		Code.put(Code.pop);
		Code.put(Code.dup);
		Code.loadConst(0);
		Code.putFalseJump(Code.lt, 0);
		int adr2 = Code.pc - 2;
		jumpOnLabel(adr2, label);
		
		Code.put(Code.pop);
		Code.put(Code.dup_x1);
		Code.put(Code.pop);
		Code.put(Code.pop);
		
		Code.put(Code.exit);
		Code.put(Code.return_);
		
		Code.fixup(adr1);
		
		Code.put(Code.load_1);
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	public void visit(TermMulVec termMulVec) {
		if(termMulVec.getTerm().struct == Tab.intType) {
			Obj scaMulVec = Tab.find("scaMulVec");
			int offset = scaMulVec.getAdr() - Code.pc;
			Code.put(Code.call);
			Code.put2(offset);
		} else if(termMulVec.getFactor().struct == Tab.intType) {
			Obj vecMulSca = Tab.find("vecMulSca");
			int offset = vecMulSca.getAdr() - Code.pc;
			Code.put(Code.call);
			Code.put2(offset);
		} else {
			Obj vecMulVec = Tab.find("vecMulVec");
			int offset = vecMulVec.getAdr() - Code.pc;
			Code.put(Code.call);
			Code.put2(offset);
		}
	}
	
	//maksimum niza
	public void visit(FactorMaxDesignator factor) {
		Code.load(factor.getDesignator().obj);
		Code.load(factor.getDesignator().obj);
		Code.put(Code.arraylength);
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.put(Code.aload);
		
		Code.load(factor.getDesignator().obj);
		Code.put(Code.arraylength);
		Code.loadConst(2);
		Code.put(Code.sub);
		Code.put(Code.dup_x1);
		
		int label = Code.pc;
		Code.put(Code.dup2);
		Code.load(factor.getDesignator().obj);
		Code.put(Code.dup2);
		Code.put(Code.pop);
		Code.put(Code.aload);
		Code.put(Code.dup_x1);
		Code.put(Code.pop);
		Code.put(Code.pop);
		Code.put(Code.dup2);
		Code.putFalseJump(Code.ge, 0);
		int adr1 = Code.pc - 2;
		Code.put(Code.pop);
		Code.put(Code.pop);
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.put(Code.dup_x2);
		Code.put(Code.pop);
		Code.put(Code.dup_x1);
		Code.put(Code.pop);
		Code.put(Code.pop);
		Code.put(Code.dup2);
		Code.put(Code.pop);
		Code.put(Code.dup);
		Code.loadConst(0);
		Code.putFalseJump(Code.lt, 0); // while
		int adr3 = Code.pc - 2;
		jumpOnLabel(adr3, label);
		//na kraj
		Code.putJump(0);
		int adr4 = Code.pc - 2;
		
		Code.fixup(adr1);
		Code.put(Code.dup_x2);
		Code.put(Code.pop);
		Code.put(Code.pop);
		Code.put(Code.pop);
		Code.put(Code.dup_x1);
		Code.put(Code.pop);
		Code.put(Code.pop);
		Code.put(Code.dup2);
		Code.put(Code.pop);
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.put(Code.dup_x2);
		Code.put(Code.pop);
		Code.put(Code.dup_x1);
		Code.put(Code.pop);
		Code.put(Code.pop);
		Code.put(Code.dup2);
		Code.put(Code.pop);
		Code.put(Code.dup);
		Code.loadConst(0);
		Code.putFalseJump(Code.lt, 0); // while
		int adr5 = Code.pc - 2;
		jumpOnLabel(adr5, label);
		
		//na kraj
		
		Code.fixup(adr4);
		Code.put(Code.pop);
		Code.put(Code.dup_x1);
		Code.put(Code.pop);
		Code.put(Code.pop);
	}
	
	//swap dva elementa niza
	public void visit(SwapStmt swap) {
		Code.put(Code.dup2);
		Code.load(swap.getDesignator().obj);
		Code.put(Code.dup_x2);
		Code.put(Code.dup_x1);
		Code.put(Code.pop);
		Code.put(Code.aload);
		Code.put(Code.dup_x2);
		Code.put(Code.pop);
		Code.put(Code.aload);
		Code.put(Code.dup_x1);
		Code.put(Code.pop);
		Code.put(Code.dup_x2);
		Code.put(Code.pop);
		Code.load(swap.getDesignator().obj);
		Code.put(Code.dup_x2);
		Code.put(Code.pop);
		Code.put(Code.astore);
		Code.load(swap.getDesignator().obj);
		Code.put(Code.dup_x2);
		Code.put(Code.pop);
		Code.put(Code.astore);
	}
	
	public Map<String, Integer> labelMap = new HashMap<>();
	public Map<String, Integer> adrMap = new HashMap<>();
	
	public void visit(Label label) {
		labelMap.put(label.getName(), Code.pc);
	}
	
	public void visit(GotoStmt stmt) {
		Code.putJump(0);
		int adr = Code.pc - 2;
		adrMap.put(stmt.getName(), adr);
	}
}
