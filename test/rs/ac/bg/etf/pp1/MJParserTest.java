package rs.ac.bg.etf.pp1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

import java_cup.runtime.Symbol;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import rs.ac.bg.etf.pp1.ast.Program;
import rs.ac.bg.etf.pp1.test.CompilerError;
import rs.ac.bg.etf.pp1.util.Log4JUtils;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;

public class MJParserTest {

	static {
		DOMConfigurator.configure(Log4JUtils.instance().findLoggerConfigFile());
		Log4JUtils.instance().prepareLogFile(Logger.getRootLogger());
	}
	
	public static void main(String[] args) throws Exception {
		
		CompilerImpl compiler = new CompilerImpl();
		List<CompilerError> errorsList = compiler.compile(args[0], args[1]);
		
		System.out.println(errorsList.toString());
		
//		for(int i = 0; i < AllMethArgs.list.size(); i++) {
//			MethArgs temp = AllMethArgs.list.get(i);
//			System.out.println(temp.method.toString());
//			System.out.println(temp.args.toString());
//		}
		
//		Logger log = Logger.getLogger(MJParserTest.class);
//		
//		Reader br = null;
//		try {
//			//File sourceCode = new File("test/program.mj");
//			File sourceCode = new File(args[0]);
//			log.info("Compiling source file: " + sourceCode.getAbsolutePath());
//			
//			br = new BufferedReader(new FileReader(sourceCode));
//			Yylex lexer = new Yylex(br);
//			
//			MJParser p = new MJParser(lexer);
//	        Symbol s = p.parse();  //pocetak parsiranja
//	        
//	        Program prog = (Program)(s.value);  
//	        Table.initialization();
//			// ispis sintaksnog stabla
//			log.info(prog.toString(""));
//			log.info("===================================");
//
//			// ispis prepoznatih programskih konstrukcija
//			SemanticAnalyzer v = new SemanticAnalyzer();
//			prog.traverseBottomUp(v);
//			
//			log.info("===================================");
//			tsdump();
//			if(!v.mainDetected) {
//				log.error("Parsiranje NIJE uspesno zavrseno!");
//				log.info("Mora postojati main metoda koja je void metoda bez argumenata! ");
//			} else {
//				if(!p.errorDetected && v.passed()) {
//					//File objFile = new File("test/program.obj");
//					File objFile = new File(args[1]);
//					if(objFile.exists()) objFile.delete();			
//					CodeGenerator codeGenerator = new CodeGenerator();
//					prog.traverseBottomUp(codeGenerator);
//					Code.dataSize = v.nVars;
//					Code.mainPc = codeGenerator.getMainPc();
//					Code.write(new FileOutputStream(objFile));
//					log.info("Parsiranje uspesno zavrseno! Nvars = " + v.nVars);
//					//Table.tsdump();
//				} else {
//					log.error("Parsiranje NIJE uspesno zavrseno!");
//				}
//			}
//		} 
//		finally {
//			if (br != null) try { br.close(); } catch (IOException e1) { log.error(e1.getMessage(), e1); }
//		}

	}
	
	public static void tsdump() {
		Table.tsdump();
	}
	
	
}
