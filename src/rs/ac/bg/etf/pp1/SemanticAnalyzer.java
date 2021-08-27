package rs.ac.bg.etf.pp1;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;
import rs.ac.bg.etf.pp1.test.CompilerError;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

public class SemanticAnalyzer extends VisitorAdaptor {
	
	List<CompilerError> errorsList = new LinkedList<>();
	
	boolean returnFound = false;
	int formParamsNumber = 0;
	MethArgs currMethArgs = null;
	List<Struct> actualParamList = new LinkedList<>();

	int printCallCount = 0;
	int varDeclCount = 0;

	Struct currentType = Tab.noType;
	Obj currentMethod = Tab.noObj;
	
	boolean mainDetected = false;

	boolean errorDetected = false;

	int nVars;

	Logger log = Logger.getLogger(getClass());

	public void report_error(String message, SyntaxNode info) {
		errorDetected = true;
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0 : info.getLine();
		if (line != 0)
			msg.append(" na liniji ").append(line);
		CompilerError error = new CompilerError(line, msg.toString(), CompilerError.CompilerErrorType.SEMANTIC_ERROR);
		errorsList.add(error);
		log.error(msg.toString());
	}

	public void report_info(String message, SyntaxNode info) {
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0 : info.getLine();
		if (line != 0)
			msg.append(" na liniji ").append(line);
		log.info(msg.toString());
	}

	public void visit(ProgName progName) {
		//vecMulVec
		Obj vecMulVec = new Obj(Obj.Meth, "vecMulVec", Tab.intType, 0, 2);
		Tab.currentScope.addToLocals(vecMulVec);
		Tab.openScope();
		Tab.currentScope.addToLocals(new Obj(Obj.Var, "arr1", new Struct(Struct.Array, Tab.intType), 0, 1));
		Tab.currentScope.addToLocals(new Obj(Obj.Var, "arr2", new Struct(Struct.Array, Tab.intType), 1, 1));
		vecMulVec.setLocals(Tab.currentScope.getLocals());
		Tab.closeScope();
		
		//scaMulVec
		Obj scaMulVec = new Obj(Obj.Meth, "scaMulVec", new Struct(Struct.Array, Tab.intType), 0, 2);
		Tab.currentScope.addToLocals(scaMulVec);
		Tab.openScope();
		Tab.currentScope.addToLocals(new Obj(Obj.Var, "sca", Tab.intType, 0, 1));
		Tab.currentScope.addToLocals(new Obj(Obj.Var, "arr", new Struct(Struct.Array, Tab.intType), 1, 1));
		scaMulVec.setLocals(Tab.currentScope.getLocals());
		Tab.closeScope();
		
		//vecMulSca
		Obj vecMulSca = new Obj(Obj.Meth, "vecMulSca", new Struct(Struct.Array, Tab.intType), 0, 2);
		Tab.currentScope.addToLocals(vecMulSca);
		Tab.openScope();
		Tab.currentScope.addToLocals(new Obj(Obj.Var, "arr", new Struct(Struct.Array, Tab.intType), 0, 1));
		Tab.currentScope.addToLocals(new Obj(Obj.Var, "sca", Tab.intType, 1, 1));
		vecMulSca.setLocals(Tab.currentScope.getLocals());
		Tab.closeScope();
		
		//Glavni program
		progName.obj = Tab.insert(Obj.Prog, progName.getProgName(), Tab.noType);
		Tab.openScope();
		
		//chr
		currMethArgs = new MethArgs();
		currMethArgs.method = Tab.find("chr");
		currMethArgs.args.add(Tab.intType);
		AllMethArgs.list.add(currMethArgs);
		currMethArgs = null;
		
		//ord
		currMethArgs = new MethArgs();
		currMethArgs.method = Tab.find("ord");
		currMethArgs.args.add(Tab.charType);
		AllMethArgs.list.add(currMethArgs);
		currMethArgs = null;
		
		//len
		currMethArgs = new MethArgs();
		currMethArgs.method = Tab.find("len");
		currMethArgs.args.add(new Struct(Struct.Array, Tab.noType));
		AllMethArgs.list.add(currMethArgs);
		currMethArgs = null;
	}

	public void visit(Program program) {
		nVars = Tab.currentScope.getnVars();
		Tab.chainLocalSymbols(program.getProgName().obj);
		Tab.closeScope();
	}

	public void visit(Type type) {
		Obj typeNode = Tab.find(type.getTypeName());
		if (typeNode == Tab.noObj) {
			report_error("Nije pronadjen tip " + type.getTypeName() + " u tabeli simbola! Semanticka greska", type);
			type.struct = Tab.noType;
		} else {
			if (Obj.Type == typeNode.getKind()) {
				currentType = type.struct = typeNode.getType();
			} else {
				report_error("Ime " + type.getTypeName() + " ne predstavlja tip! Semanticka greska", type);
				type.struct = Tab.noType;
			}
		}
	}

	public void visit(VarDeclarationItem varDeclarationItem) {
		Obj varCheck = Table.existInCurrentScope(varDeclarationItem.getVarName());
		if (varCheck == Tab.noObj) {
			Tab.insert(Obj.Var, varDeclarationItem.getVarName(), currentType);
			report_info("Deklarisana promenljiva " + varDeclarationItem.getVarName(), varDeclarationItem);
		} else {
			report_error("Ime " + varDeclarationItem.getVarName() + " je vec deklarisano! Semanticka greska", varDeclarationItem);
		}
	}

	public void visit(ArrayDeclarationItem arrayDeclarationItem) {
		Obj arrCheck = Table.existInCurrentScope(arrayDeclarationItem.getArrName());
		if (arrCheck == Tab.noObj) {
			Tab.insert(Obj.Var, arrayDeclarationItem.getArrName(), new Struct(Struct.Array, currentType));
			report_info("Deklarisan niz " + arrayDeclarationItem.getArrName(), arrayDeclarationItem);
		} else {
			report_error("Ime " + arrayDeclarationItem.getArrName() + " je vec deklarisano! Semanticka greska", arrayDeclarationItem);
		}
	}

	public void visit(VarDeclarations varDeclarations) {
		currentType = Tab.noType;
	}

	public void visit(ConstValueNumber cnst) {
		cnst.struct = Tab.intType;
	}

	public void visit(ConstValueChar cnst) {
		cnst.struct = Tab.charType;
	}

	public void visit(ConstValueBool cnst) {
		cnst.struct = Table.boolType;
	}

	public void visit(ConstDeclarationItem constDeclarationItem) {
		Obj constCheck = Tab.find(constDeclarationItem.getConstName());
		if (!constDeclarationItem.getConstItemValue().struct.equals(currentType)) {
			report_error("Tip konstante se ne poklapa s vrednoscu koja je dodeljena! Semanticka greska", constDeclarationItem);
		} else {
			if (constCheck == Tab.noObj) {
				Obj constNode = Tab.insert(Obj.Con, constDeclarationItem.getConstName(), currentType);
				if (constDeclarationItem.getConstItemValue().struct.equals(Tab.intType)
						&& constDeclarationItem.getConstItemValue() instanceof ConstValueNumber) {
					ConstValueNumber t = (ConstValueNumber) constDeclarationItem.getConstItemValue();
					constNode.setAdr(t.getNum_const());
				} else if (constDeclarationItem.getConstItemValue().struct.equals(Tab.charType)
						&& constDeclarationItem.getConstItemValue() instanceof ConstValueChar) {
					ConstValueChar t = (ConstValueChar) constDeclarationItem.getConstItemValue();
					constNode.setAdr(t.getChar_const());
				} else if (constDeclarationItem.getConstItemValue().struct.equals(Table.boolType)
						&& constDeclarationItem.getConstItemValue() instanceof ConstValueBool) {
					ConstValueBool t = (ConstValueBool) constDeclarationItem.getConstItemValue();
					boolean b = Boolean.parseBoolean(t.getBool_const());
					constNode.setAdr((b) ? 1 : 0);
				}
				report_info("Deklarisana konstanta " + constDeclarationItem.getConstName(), constDeclarationItem);
			} else {
				report_error("Ime " + constDeclarationItem.getConstName() + " je vec deklarisano! Semanticka greska", constDeclarationItem);
			}
		}
	}

	public void visit(ConstDeclarations constDeclarations) {
		currentType = Tab.noType;
	}

	public void visit(MethodReturnType methodReturnType) {
		Obj methCheck = Tab.find(methodReturnType.getName());
		if (methCheck == Tab.noObj) {
			Obj methNode = currentMethod = Tab.insert(Obj.Meth, methodReturnType.getName(), currentType);
			report_info("Obradjuje se funkcija " + methodReturnType.getName(), methodReturnType);
			methodReturnType.obj = methNode;
			currMethArgs = new MethArgs();
			currMethArgs.method = currentMethod;
			Tab.openScope();
		} else {
			methodReturnType.obj = Tab.noObj;
			report_error("Ime " + methodReturnType.getName() + " je vec deklarisano! Semanticka greska", methodReturnType);
		}
	}

	public void visit(MethodReturnVoid methodReturnVoid) {
		Obj methCheck = Tab.find(methodReturnVoid.getName());
		if (methCheck == Tab.noObj) {
			Obj methNode = currentMethod = Tab.insert(Obj.Meth, methodReturnVoid.getName(), Tab.noType);
			report_info("Obradjuje se funkcija " + methodReturnVoid.getName(), methodReturnVoid);
			methodReturnVoid.obj = methNode;
			currMethArgs = new MethArgs();
			currMethArgs.method = currentMethod;
			Tab.openScope();
		} else {
			methodReturnVoid.obj = Tab.noObj;
			report_error("Ime " + methodReturnVoid.getName() + " je vec deklarisano! Semanticka greska", methodReturnVoid);
		}
	}

	public void visit(MethodDeclaration methodDeclaration) {
		if(!returnFound && !currentMethod.getType().equals(Tab.noType)) {
			report_error("Funkcija nema return naredbu! Semanticka greska", methodDeclaration);
		} else {
			MethodReturnTypeIdent t1= methodDeclaration.getMethodReturnTypeIdent();
			if(t1 instanceof MethodReturnVoid && methodDeclaration.getFormPars() instanceof NoFormParams) {
				MethodReturnVoid t2 = (MethodReturnVoid) t1;
				if("main".equals(t2.getName())) mainDetected = true;
			}
		}
		currentMethod.setLevel(formParamsNumber);
		AllMethArgs.list.add(currMethArgs);
		currMethArgs = null;
		Tab.chainLocalSymbols(currentMethod);
		Tab.closeScope();
		returnFound = false;
		currentMethod = Tab.noObj;
		formParamsNumber = 0;
	}

	public void visit(ScalarDesignator scalarDesignator) {
		Obj desCheck = Tab.find(scalarDesignator.getName());
		if (desCheck == Tab.noObj) {
			report_error("Ime " + scalarDesignator.getName() + " nije deklarisano! Semanticka greska", scalarDesignator);
		}
		scalarDesignator.obj = desCheck;
	}

	public void visit(ArrayDesignator arrayDesignator) {
		Obj desCheck = Tab.find(arrayDesignator.getArrayDesignatorName().getName());
		if (desCheck == Tab.noObj) {
			report_error("Ime " + arrayDesignator.getArrayDesignatorName().getName() + " nije deklarisano! Semanticka greska", arrayDesignator);
		} else {
			if (!(desCheck.getType().getKind() == Struct.Array && arrayDesignator.getExpr().struct == Tab.intType)) {
				report_error("Ime " + arrayDesignator.getArrayDesignatorName().getName() 
						+ " nije deklarisano kao niz ili indeks niza nije tipa int! Semanticka greska", arrayDesignator);
			}
		}
		arrayDesignator.obj = desCheck;
		arrayDesignator.getArrayDesignatorName().obj = desCheck;
	}

	public void visit(FactorDesignator factorDesignator) {
		if (factorDesignator.getDesignator() instanceof ArrayDesignator) {
			factorDesignator.struct = factorDesignator.getDesignator().obj.getType().getElemType();
		} else {
			factorDesignator.struct = factorDesignator.getDesignator().obj.getType();
		}
	}

	public void visit(FactorNumber factorNumber) {
		factorNumber.struct = Tab.intType;
	}

	public void visit(FactorChar factorChar) {
		factorChar.struct = Tab.charType;
	}

	public void visit(FactorExpr factorExpr) {
		factorExpr.struct = factorExpr.getExpr().struct;
	}

	public void visit(FactorBool factorBool) {
		factorBool.struct = Table.boolType;
	}

	// Za new ...
	public void visit(FactorArray factorArray) {
		if (factorArray.getExpr().struct == Tab.intType) {
			factorArray.struct = new Struct(Struct.Array, currentType);
		} else {
			report_error("Izraz u uglastim zagradama bi trebalo da bude celobrojnog tipa! Semanticka greska", factorArray);
			factorArray.struct = Tab.noType;
		}
	}

	public void visit(TermFactor termFactor) {
		termFactor.struct = termFactor.getFactor().struct;
	}

	public void visit(TermMul termMul) {
		Struct term_s = termMul.getTerm().struct;
		Struct factor_s = termMul.getFactor().struct;
		if (term_s.equals(factor_s) && term_s == Tab.intType) {
			termMul.struct = term_s;
		} else {
			report_error("Nisu kompatibilni tipovi u izrazu za mnozenje! Semanticka greska", termMul);
			termMul.struct = Tab.noType;
		}
	}

	public void visit(ExprTerm exprTerm) {
		exprTerm.struct = exprTerm.getTerm().struct;
	}

	public void visit(ExprMTerm exprMTerm) {
		if (exprMTerm.getTerm().struct.equals(Tab.intType)) {
			exprMTerm.struct = exprMTerm.getTerm().struct;
		} else {
			report_error("Mora biti broj uz znak - ! Semanticka greska", exprMTerm);
			exprMTerm.struct = Tab.noType;
		}
	}

	public void visit(ExprAdd exprAdd) {
		Struct expr_s = exprAdd.getExpr().struct;
		Struct term_s = exprAdd.getTerm().struct;
		if (expr_s.compatibleWith(term_s) && expr_s == Tab.intType) {
			exprAdd.struct = expr_s;
		} else {
			report_error("Nisu kompatibilni tipovi u izrazu za sabiranje! Semanticka greska", exprAdd);
			exprAdd.struct = Tab.noType;
		}
	}

//	// Za ternarni operator
//	public void visit(ExprTernary expr) {
//		Struct cond_s = expr.getExpr1().struct;
//		Struct first_s = expr.getExpr11().struct;
//		Struct second_s = expr.getExpr12().struct;
//		if (first_s.equals(second_s)) {
//			expr.struct = first_s;
//		} else {
//			report_error("Greska na liniji " + expr.getLine() + ".Nisu isti tipovi u drugom i trecem izrazu!", null);
//			expr.struct = Tab.noType;
//		}
//	}

	public void visit(DesignatorAssignValue assign) {
		if (assign.getDesignator() instanceof ScalarDesignator) {
			if (assign.getDesignator().obj.getKind() != Obj.Var) {
				report_error("Mora biti promenljiva s leve strane dodele vrednosti! Semanticka greska", assign);
			} else {
				if(assign.getExpr() == null) System.out.println("Expr je null");
				else if(assign.getDesignator() == null) System.out.println("Desig je null");
				if (!assign.getExpr().struct.assignableTo(assign.getDesignator().obj.getType())) {
					report_error("Nisu kompatibilni tipovi u naredbi dodele vrednosti! Semanticka greska", assign);
				}
			}
		} else if (assign.getDesignator() instanceof ArrayDesignator) {
			if(assign.getDesignator().obj.getKind() != Obj.Var || assign.getDesignator().obj.getType().getKind() != Struct.Array) {
				report_error("Nije element niza s leve strane dodele vrednosti! Semanticka greska", assign);
			} else {
				if (!assign.getExpr().struct.assignableTo(assign.getDesignator().obj.getType().getElemType())) {
					report_error("Nisu kompatibilni tipovi u naredbi dodele vrednosti! Semanticka greska", assign);
				}
			}
		}
	}

	public void visit(DesignatorIncValue inc) {
		if (inc.getDesignator() instanceof ScalarDesignator) {
			if (inc.getDesignator().obj.getKind() != Obj.Var) {
				report_error("Mora biti promenljiva u naredbi inkrementiranja! Semanticka greska", inc);
			} else {
				if (!inc.getDesignator().obj.getType().equals(Tab.intType)) {
					report_error("Promenljiva nije celobrojnog tipa u naredbi inkrementiranja! Semanticka greska", inc);
				}
			}
		} else if (inc.getDesignator() instanceof ArrayDesignator) {
			if(inc.getDesignator().obj.getKind() != Obj.Var || inc.getDesignator().obj.getType().getKind() != Struct.Array) {
				report_error("Nije element niza u naredbi inkrementiranja! Semanticka greska", inc);
			} else {
				if (!inc.getDesignator().obj.getType().getElemType().equals(Tab.intType)) {
					report_error("Element niza nije celobrojnog tipa u naredbi inkrementiranja! Semanticka greska", inc);
				}
			}
		}
	}

	public void visit(DesignatorDecValue dec) {
		if (dec.getDesignator() instanceof ScalarDesignator) {
			if (dec.getDesignator().obj.getKind() != Obj.Var) {
				report_error("Mora biti promenljiva u naredbi dekrementiranja! Semanticka greska", dec);
			} else {
				if (!dec.getDesignator().obj.getType().equals(Tab.intType)) {
					report_error("Promenljiva nije celobrojnog tipa u naredbi dekrementiranja! Semanticka greska", dec);
				}
			}
		} else if (dec.getDesignator() instanceof ArrayDesignator) {
			if(dec.getDesignator().obj.getKind() != Obj.Var || dec.getDesignator().obj.getType().getKind() != Struct.Array) {
				report_error("Nije element niza u naredbi dekrementiranja! Semanticka greska", dec);
			} else {
				if (!dec.getDesignator().obj.getType().getElemType().equals(Tab.intType)) {
					report_error("Element niza nije celobrojnog tipa u naredbi dekrementiranja! Semanticka greska", dec);
				}
			}
		}
	}

	public void visit(ReadStmt stmt) {
		if (stmt.getDesignator() instanceof ScalarDesignator) {
			if (stmt.getDesignator().obj.getKind() != Obj.Var) {
				report_error("Mora biti promenljiva u naredbi read! Semanticka greska", stmt);
			} else {
				if (!stmt.getDesignator().obj.getType().equals(Tab.intType)
						&& !stmt.getDesignator().obj.getType().equals(Tab.charType) 
						&& !stmt.getDesignator().obj.getType().equals(Table.boolType)) {
					report_error("Parametar u naredbi read bi trebalo da bude tipa int, char ili bool! Semanticka greska", stmt);
				}
			}
		} else if (stmt.getDesignator() instanceof ArrayDesignator) {
			if(stmt.getDesignator().obj.getKind() != Obj.Var || stmt.getDesignator().obj.getType().getKind() != Struct.Array) {
				report_error("Nije element niza u naredbi read! Semanticka greska", stmt);
			} else {
				if (!stmt.getDesignator().obj.getType().getElemType().equals(Tab.intType)
						&& !stmt.getDesignator().obj.getType().getElemType().equals(Tab.charType) 
						&& !stmt.getDesignator().obj.getType().getElemType().equals(Table.boolType)) {
					report_error("Parametar u naredbi read bi trebalo da bude tipa int, char ili bool! Semanticka greska", stmt);
				}
			}
		}

	}

	public void visit(PrintStmt stmt) {
		if (!stmt.getExpr().struct.equals(Tab.intType) && !stmt.getExpr().struct.equals(Tab.charType)
				&& !stmt.getExpr().struct.equals(Table.boolType)) {
			report_error("Prvi parametar u naredbi print bi trebalo da bude tipa int, char ili bool! Semanticka greska", stmt);
		}
	}

	public boolean passed() {
		return !errorDetected;
	}
	
	public void visit(CondFact condFact) {
		if(condFact.getSecondExpr() instanceof YesSecExpr) {
			YesSecExpr secExpr = (YesSecExpr) condFact.getSecondExpr();
			if(!condFact.getExpr().struct.compatibleWith(secExpr.getExpr().struct)) {
				report_error("Tipovi izraza nisu kompatibilni u relacionom izrazu! Semanticka greska", condFact);
				condFact.struct = Tab.noType;
			} else {
				if(condFact.getExpr().struct.isRefType() || secExpr.getExpr().struct.isRefType()) {
					if(!(secExpr.getRelop() instanceof EqualOperation || secExpr.getRelop() instanceof NotEqualOperation)) {
						report_error("Uz tipove klase ili niza mogu se koristiti operatori == i != ! Semanticka greska", condFact);
						condFact.struct = Tab.noType;
					} else {
						condFact.struct = Table.boolType;
					}
				} else {
					condFact.struct = Table.boolType;
				}
			}
		} else if (condFact.getSecondExpr() instanceof NoSecExpr) {
			if(!condFact.getExpr().struct.equals(Table.boolType)) {
				report_error("Ako nema relacionog operatora u proveri uslova, onda bi izraz trebalo da bude tipa bool! Semanticka greska", condFact);
				condFact.struct = Tab.noType;
			} else {
				condFact.struct = Table.boolType;
			}
		}
	}
	
	public void visit(ConditionFact conditionFact) {
		conditionFact.struct = conditionFact.getCondFact().struct;
	}
	
	public void visit(ConditionAnd conditionAnd) {
		conditionAnd.struct = conditionAnd.getCondFact().struct;
	}
	
	public void visit(ConditionTerm conditionTerm) {
		conditionTerm.struct = conditionTerm.getCondTerm().struct;
	}
	
	public void visit(ConditionOr conditionOr) {
		conditionOr.struct = conditionOr.getCondTerm().struct;
	}
	
	public void visit(IfStmt stmt) {
		if(!stmt.getConditionEnd().getCondition().struct.equals(Table.boolType)) {
			report_error("Condition u if naredbi mora biti tipa bool! Semanticka greska", stmt);
		}
	}
	
	//B nivo
	
	public void visit(DoWhileStmt stmt) {
		if(!stmt.getConditionEndWhile().getCondition().struct.equals(Table.boolType)) {
			report_error("Condition u do while petlji mora biti tipa bool! Semanticka greska", stmt);
		}
	}
	
	public void visit(BreakStmt stmt) {
		boolean cond = false;
		SyntaxNode father = stmt.getParent();
		while(father != null) {
			if(father instanceof DoWhileStmt) {
				cond = true;
				break;
			}
			father = father.getParent();
		}
		if(!cond) {
			report_error("Naredba break mora biti unutar do while petlje! Semanticka greska", stmt);
		}
	}
	
	public void visit(ContinueStmt stmt) {
		boolean cond = false;
		SyntaxNode father = stmt.getParent();
		while(father != null) {
			if(father instanceof DoWhileStmt) {
				cond = true;
				break;
			}
			father = father.getParent();
		}
		if(!cond) {
			report_error("Naredba continue mora biti unutar do while petlje! Semanticka greska", stmt);
		}
	}
	
	public void visit(YesReturnExpr stmt) {
		returnFound = true;
		if(currentMethod == Tab.noObj) {
			report_error("Naredba return mora biti unutar tela metode! Semanticka greska", stmt);
		} else {
			if(currentMethod.getType().equals(Tab.noType)) {
				report_error("Metoda tipa void ne vraca nikakvu vrednost! Semanticka greska", stmt);
			} else {
				if(!currentMethod.getType().equals(stmt.getExpr().struct)) {
					report_error("Izraz uz return nije ekvivalentan tipu tekuce metode! Semanticka greska", stmt);
				}
			}
		}
	}
	
	public void visit(NoReturnExpr stmt) {
		returnFound = true;
		if(currentMethod == Tab.noObj) {
			report_error("Naredba return mora biti unutar tela metode! Semanticka greska", stmt);
		} else {
			if(!currentMethod.getType().equals(Tab.noType)) {
				report_error("Return ne vraca vrednost iako bi trebalo da vraca! Semanticka greska", stmt);
			}
		}
	}
	
	public void visit(DesignatorProcCall stmt) {
		if(stmt.getDesignator().obj.getKind() != Obj.Meth) {
			report_error("Ime " + stmt.getDesignator().obj.getName() + " nije definisano kao globalna funkcija! Semanticka greska", stmt);
		} else {
			Obj meth = stmt.getDesignator().obj;
			MethArgs methArgs = AllMethArgs.getForObject(meth);
			if(methArgs != null) {
				if(methArgs.args.size() != actualParamList.size()) {
					report_error("Nije jednak broj formalnih i stvarnih argumenata! Semanticka greska", stmt);
				} else {
					for(int i = 0; i < actualParamList.size(); i++) {
						Struct actualParam = actualParamList.get(i);
						Struct formalParam = methArgs.args.get(i);
						if(!actualParam.assignableTo(formalParam)) {
							report_error((i + 1) + ". stvarni parametar nije kompatibilan pri dodeli! Semanticka greska", stmt);
						}
					}
				}
			} else {
				report_error("Greska u obradi argumenata procedure! Semanticka greska", stmt);
			}
		}
		actualParamList.clear();
	}
	
	public void visit(FactorFuncCall stmt) {
		if(stmt.getDesignator().obj.getKind() != Obj.Meth) {
			report_error("Ime " + stmt.getDesignator().obj.getName() + " nije definisano kao globalna funkcija! Semanticka greska", stmt);
		} else {
			if(stmt.getDesignator().obj.getType().equals(Tab.noType)) {
				report_error("Funkcija ne vraca vrednost pa ne moze ucestovati u izrazima! Semanticka greska", stmt);
				stmt.struct = Tab.noType;
			} else {
				Obj meth = stmt.getDesignator().obj;
				MethArgs methArgs = AllMethArgs.getForObject(meth);
				if(methArgs != null) {
					if(methArgs.args.size() != actualParamList.size()) {
						report_error("Nije jednak broj formalnih i stvarnih argumenata! Semanticka greska", stmt);
						stmt.struct = Tab.noType;
					} else {
						for(int i = 0; i < actualParamList.size(); i++) {
							Struct actualParam = actualParamList.get(i);
							Struct formalParam = methArgs.args.get(i);
							if(!actualParam.assignableTo(formalParam)) {
								report_error((i + 1) + ". stvarni parametar nije kompatibilan pri dodeli! Semanticka greska", stmt);
								stmt.struct = Tab.noType;
								actualParamList.clear();
								return;
							}
						}
						stmt.struct = stmt.getDesignator().obj.getType();
					}
				} else {
					report_error("Greska u obradi argumenata funkcije! Semanticka greska", stmt);
					stmt.struct = Tab.noType;
				}
				
			}
		}
		actualParamList.clear();
	}
	
	public void visit(ScalarParam scalarParam) {
		Obj varCheck = Table.existInCurrentScope(scalarParam.getParamName());
		if(varCheck == Tab.noObj) {
			Tab.insert(Obj.Var, scalarParam.getParamName(), currentType);
			formParamsNumber++;
			currMethArgs.args.add(currentType);
			currentType = Tab.noType; 
		} else {
			report_error("Ime " + scalarParam.getParamName() + " je vec deklarisano! Semanticka greska", scalarParam);
		}
	}
	
	public void visit(ArrayParam arrayParam) {
		Obj arrCheck = Table.existInCurrentScope(arrayParam.getParamName());
		if(arrCheck == Tab.noObj) {
			Struct type = new Struct(Struct.Array, currentType);
			Tab.insert(Obj.Var, arrayParam.getParamName(), type);
			currMethArgs.args.add(type);
			formParamsNumber++;
			currentType = Tab.noType;
		} else {
			report_error("Ime " + arrayParam.getParamName() + " je vec deklarisano! Semanticka greska", arrayParam);
		}
	}
	
	public void visit(ActualParam param) {
		actualParamList.add(param.getExpr().struct);
	}
	
	//B nivo - Dodatak
	
	public void visit(TermMulVec termMulVec) {
		Struct left = termMulVec.getTerm().struct;
		Struct right = termMulVec.getFactor().struct;
		if(left.equals(right) && right.getKind() == Struct.Array && right.getElemType() == Tab.intType) {
			termMulVec.struct = Tab.intType;
		} else if(right.equals(Tab.intType) && left.getKind() == Struct.Array && left.getElemType().equals(Tab.intType)) {
			termMulVec.struct = new Struct(Struct.Array, Tab.intType);
		} else if(left.equals(Tab.intType) && right.getKind() == Struct.Array && right.getElemType().equals(Tab.intType)) {
			termMulVec.struct = new Struct(Struct.Array, Tab.intType);
		} else {
			report_error("Dozvoljeno je mnoziti skalar s vektorom i vektor s vektorom! Semanticka greska", termMulVec);
			termMulVec.struct = Tab.noType;
		}
	}
	
	public void visit(FactorMaxDesignator factorMaxDesignator) {
		if(factorMaxDesignator.getDesignator().obj.getType().getKind() != Struct.Array || factorMaxDesignator.getDesignator().obj.getType().getElemType() != Tab.intType) {
			report_error("U max izrazu mora biti niz celih brojeva! Semanticka greska", factorMaxDesignator);
			factorMaxDesignator.struct = Tab.noType;
		} else {
			factorMaxDesignator.struct = Tab.intType;
		}
	}
	
}
