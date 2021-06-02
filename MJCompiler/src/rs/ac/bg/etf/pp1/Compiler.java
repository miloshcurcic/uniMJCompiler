package rs.ac.bg.etf.pp1;

import java_cup.runtime.*;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import rs.ac.bg.etf.pp1.*;
import rs.ac.bg.etf.pp1.util.*;
import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.*;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;

public class Compiler {

    static {
        DOMConfigurator.configure(Log4JUtils.instance().findLoggerConfigFile());
        Log4JUtils.instance().prepareLogFile(Logger.getRootLogger());
    }

    public static void main(String[] args) throws Exception {
        Logger log = Logger.getLogger(Compiler.class);
        if (args.length < 2) {
            log.error("Not enough arguments supplied! Usage: MJParser <source-file> <obj-file> ");
            return;
        }

        File sourceCode = new File(args[0]);
        if (!sourceCode.exists()) {
            log.error("Source file [" + sourceCode.getAbsolutePath() + "] not found!");
            return;
        }

        log.info("===================================");
        log.info("Compiling source file: " + sourceCode.getAbsolutePath());
        log.info("===================================");

        try (BufferedReader br = new BufferedReader(new FileReader(sourceCode))) {
            Yylex lexer = new Yylex(br);
            MJParser p = new MJParser(lexer);
            Symbol s = p.parse();  //pocetak parsiranja
            Program rootNode = (Program)(s.value);

            SemanticAnalyzer.initUniverseScope();

            log.info("===================================");
            log.info(rootNode.toString(""));
            log.info("===================================");

            SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer();
            rootNode.traverseBottomUp(semanticAnalyzer);

            Tab.dump(new CustomDumpSymbolTableVisitor());

            if (!p.errorDetected /*&& semanticAnalyzer.passed()*/) {
                File objFile = new File(args[1]);
                log.info("Generating bytecode file: " + objFile.getAbsolutePath());

                if (objFile.exists()) {
                    objFile.delete();
                }

                // Code generation...
                CodeGenerator codeGenerator = new CodeGenerator();
                rootNode.traverseBottomUp(codeGenerator);
                Code.dataSize = semanticAnalyzer.getnVars();
                Code.mainPc = codeGenerator.getMainPc();
                Code.write(new FileOutputStream(objFile));
                log.info("Parsiranje uspesno zavrseno!");
            }
            else {
                log.error("Parsiranje NIJE uspesno zavrseno!");
            }
        }
    }
}
