package rs.ac.bg.etf.pp1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import java_cup.runtime.Symbol;
import rs.ac.bg.etf.pp1.ast.Program;
import rs.ac.bg.etf.pp1.test.Compiler;
import rs.ac.bg.etf.pp1.test.CompilerError;
import rs.etf.pp1.mj.runtime.Code;

public class CompilerImpl implements Compiler{

	@Override
	public List<CompilerError> compile(String sourceFilePath, String outputFilePath) {
		List<CompilerError> errorsList = new LinkedList<>();
		
		Logger log = Logger.getLogger(CompilerImpl.class);
		
		Reader br = null;
		try {
			//File sourceCode = new File("test/program.mj");
			File sourceCode = new File(sourceFilePath);
			log.info("Compiling source file: " + sourceCode.getAbsolutePath());
			
			br = new BufferedReader(new FileReader(sourceCode));
			Yylex lexer = new Yylex(br);
			
			MJParser p = new MJParser(lexer);
	        Symbol s = p.parse();  //pocetak parsiranja
	        
	        errorsList.addAll(lexer.errorsList);
	        errorsList.addAll(p.errorsList);
	        
	        Program prog = (Program)(s.value);  
	        Table.initialization();
			// ispis sintaksnog stabla
			log.info(prog.toString(""));
			log.info("===================================");

			// ispis prepoznatih programskih konstrukcija
			SemanticAnalyzer v = new SemanticAnalyzer();
			prog.traverseBottomUp(v);
			
			errorsList.addAll(v.errorsList);
			
			log.info("===================================");
			Table.tsdump();
			if(!v.mainDetected) {
				log.error("Parsiranje NIJE uspesno zavrseno!");
				log.info("Mora postojati main metoda koja je void metoda bez argumenata! ");
			} else {
				if(!p.errorDetected && v.passed()) {
					//File objFile = new File("test/program.obj");
					File objFile = new File(outputFilePath);
					if(objFile.exists()) objFile.delete();			
					CodeGenerator codeGenerator = new CodeGenerator();
					prog.traverseBottomUp(codeGenerator);
					Code.dataSize = v.nVars;
					Code.mainPc = codeGenerator.getMainPc();
					Code.write(new FileOutputStream(objFile));
					log.info("Parsiranje uspesno zavrseno! Nvars = " + v.nVars);
				} else {
					log.error("Parsiranje NIJE uspesno zavrseno!");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		finally {
			if (br != null) try { br.close(); } catch (IOException e1) { log.error(e1.getMessage(), e1); }
		}

		return errorsList;
	}


}
