package rs.ac.bg.etf.pp1;

import java_cup.runtime.*;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import rs.ac.bg.etf.pp1.test.CompilerError;
import rs.ac.bg.etf.pp1.util.*;
import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.*;

import java.io.*;
import java.util.*;

public class Compiler implements rs.ac.bg.etf.pp1.test.Compiler {
    static Logger log;

    static {
        DOMConfigurator.configure(Log4JUtils.instance().findLoggerConfigFile());
        Log4JUtils.instance().prepareLogFile(Logger.getRootLogger());
    }

    public static void main(String[] args) throws Exception {
        log  = Logger.getLogger(Compiler.class);

        if (args.length < 2) {
            log.error("Not enough arguments supplied! Usage: MJParser <source-file> <obj-file> ");

            return;
        }

        Compiler compiler = new Compiler();
        compiler.compile(args[0], args[1]);
    }

    public static void tsdump() {
        Tab.dump(new CustomDumpSymbolTableVisitor());
    }

    @Override
    public List<CompilerError> compile(String sourceFilePath, String outputFilePath) {
        File sourceCode = new File(sourceFilePath);
        if (!sourceCode.exists()) {
            log.error("Source file [" + sourceCode.getAbsolutePath() + "] not found!");

            // ToDo: What is this error
            return null;
        }

        log.info("===================================");
        log.info("Compiling source file: " + sourceCode.getAbsolutePath());
        log.info("===================================");

        try (BufferedReader br = new BufferedReader(new FileReader(sourceCode))) {
            Lexer lexer = new Lexer(br);
            MJParser parser = new MJParser(lexer);
            Symbol symbol = parser.parse();
            //Symbol symbol = parser.debug_parse();

            // continue if non fatal error?
            if (!lexer.isErrorDetected() && !parser.isErrorDetected()) {
                Program rootNode = (Program) (symbol.value);

                SemanticAnalyzer.initUniverseScope();

                log.info("===================================");
                log.info(rootNode.toString(""));
                log.info("===================================");

                SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer();
                rootNode.traverseBottomUp(semanticAnalyzer);

                if (!semanticAnalyzer.isErrorDetected()) {
                    // Dump symbols table
                    tsdump();

                    log.info("===================================");
                    log.info("Parsing successfully completed. Generating output.");
                    log.info("===================================");

                    File objFile = new File(outputFilePath);
                    log.info("Generating bytecode file: " + objFile.getAbsolutePath());

                    // Overwrite output file
                    if (objFile.exists()) {
                        log.info(objFile.getName() + " already exists, it will be overwritten.");

                        objFile.delete();
                    }

                    CodeGenerator codeGenerator = new CodeGenerator(semanticAnalyzer.getNVars());

                    rootNode.traverseBottomUp(codeGenerator);
                    Code.dataSize = codeGenerator.getDataSize();
                    Code.mainPc = codeGenerator.getMainPc();

                    Code.write(new FileOutputStream(objFile));
                } else {
                    log.info("===================================");
                    log.info("One or more semantic errors detected.");
                    log.info("===================================");
                }
            }
            else {
                log.info("===================================");
                log.info("One or more lexical or syntax errors detected.");
                log.info("===================================");

                List<CompilerError> resultList = new ArrayList<>();
                resultList.addAll(lexer.getErrorList());
                resultList.addAll(parser.getErrorList());

                return resultList;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }
}
