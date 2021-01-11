// generated with ast extension for cup
// version 0.8
// 11/0/2021 2:21:35


package rs.ac.bg.etf.pp1.ast;

public interface Visitor { 

    public void visit(Mulop Mulop);
    public void visit(MethodDecl MethodDecl);
    public void visit(MatchedStatement MatchedStatement);
    public void visit(VarIdent VarIdent);
    public void visit(Relop Relop);
    public void visit(FuncCallActPars FuncCallActPars);
    public void visit(Addop Addop);
    public void visit(Factor Factor);
    public void visit(CondTerm CondTerm);
    public void visit(ConstNameValuePair ConstNameValuePair);
    public void visit(Designator Designator);
    public void visit(PrintStatementAdditionalParam PrintStatementAdditionalParam);
    public void visit(Term Term);
    public void visit(Condition Condition);
    public void visit(MethodFormPars MethodFormPars);
    public void visit(VarIdents VarIdents);
    public void visit(Statements Statements);
    public void visit(TermExpr TermExpr);
    public void visit(ConstNameValuePairs ConstNameValuePairs);
    public void visit(DataDecl DataDecl);
    public void visit(Expr Expr);
    public void visit(SwitchCases SwitchCases);
    public void visit(ExprPrefix ExprPrefix);
    public void visit(ActPars ActPars);
    public void visit(ClassMethodDecls ClassMethodDecls);
    public void visit(DesignatorStatement DesignatorStatement);
    public void visit(VarDecls VarDecls);
    public void visit(UnmatchedStatement UnmatchedStatement);
    public void visit(Statement Statement);
    public void visit(DataDecls DataDecls);
    public void visit(VarDecl VarDecl);
    public void visit(ReturnExpr ReturnExpr);
    public void visit(ClassDecl ClassDecl);
    public void visit(BracedStatements BracedStatements);
    public void visit(CondFact CondFact);
    public void visit(FormPar FormPar);
    public void visit(ActPar ActPar);
    public void visit(SingleStatement SingleStatement);
    public void visit(MethodTypeNamePair MethodTypeNamePair);
    public void visit(FormPars FormPars);
    public void visit(MethodDecls MethodDecls);
    public void visit(MulopModulo MulopModulo);
    public void visit(MulopDivide MulopDivide);
    public void visit(MulopMultiply MulopMultiply);
    public void visit(AddopMinus AddopMinus);
    public void visit(AddopPlus AddopPlus);
    public void visit(RelopLEquals RelopLEquals);
    public void visit(RelopLess RelopLess);
    public void visit(RelopGEquals RelopGEquals);
    public void visit(RelopGreater RelopGreater);
    public void visit(RelopCNEquals RelopCNEquals);
    public void visit(RelopCEquals RelopCEquals);
    public void visit(Assignop Assignop);
    public void visit(ArrayAccessDesignator ArrayAccessDesignator);
    public void visit(ObjectAccessDesignator ObjectAccessDesignator);
    public void visit(ScalarDesignator ScalarDesignator);
    public void visit(ExprResultFactor ExprResultFactor);
    public void visit(NewArrayObjectFactor NewArrayObjectFactor);
    public void visit(NewObjectFactor NewObjectFactor);
    public void visit(BoolConstFactor BoolConstFactor);
    public void visit(CharConstFactor CharConstFactor);
    public void visit(NumConstFactor NumConstFactor);
    public void visit(FunctionCallResultFactor FunctionCallResultFactor);
    public void visit(VarFactor VarFactor);
    public void visit(TermListHead TermListHead);
    public void visit(TermListElement TermListElement);
    public void visit(TermExprListHead TermExprListHead);
    public void visit(TermExprListElement TermExprListElement);
    public void visit(NoExpressionPrefix NoExpressionPrefix);
    public void visit(NegativeExpressionPrefix NegativeExpressionPrefix);
    public void visit(Expr1 Expr1);
    public void visit(Expression Expression);
    public void visit(TernaryExpression TernaryExpression);
    public void visit(SingleCondFact SingleCondFact);
    public void visit(RelationalCondFact RelationalCondFact);
    public void visit(CondTermListHead CondTermListHead);
    public void visit(CondTermListElement CondTermListElement);
    public void visit(ConditionListHead ConditionListHead);
    public void visit(ConditionListElement ConditionListElement);
    public void visit(ActualParameter ActualParameter);
    public void visit(ActualParametersListHead ActualParametersListHead);
    public void visit(ActualParametersListElement ActualParametersListElement);
    public void visit(NoFunctionCallActualParameters NoFunctionCallActualParameters);
    public void visit(FunctionCallActualParameters FunctionCallActualParameters);
    public void visit(FunctionCallDesignator FunctionCallDesignator);
    public void visit(FunctionCall FunctionCall);
    public void visit(PostDecDesignatorStatement PostDecDesignatorStatement);
    public void visit(PostIncDesignatorStatement PostIncDesignatorStatement);
    public void visit(FuncCallDesignatorStatement FuncCallDesignatorStatement);
    public void visit(AssignmentDesignatorStatement AssignmentDesignatorStatement);
    public void visit(SwitchCase SwitchCase);
    public void visit(EmptySwitchCaseListHead EmptySwitchCaseListHead);
    public void visit(SwitchCaseListElement SwitchCaseListElement);
    public void visit(SwitchExpression SwitchExpression);
    public void visit(UnmatchedIfElseStatement UnmatchedIfElseStatement);
    public void visit(UnmatchedIfStatement UnmatchedIfStatement);
    public void visit(NoReturnExpression NoReturnExpression);
    public void visit(ReturnExpression ReturnExpression);
    public void visit(NoPrintStatementAdditionalParameter NoPrintStatementAdditionalParameter);
    public void visit(PrintStatementAdditionalParameter PrintStatementAdditionalParameter);
    public void visit(MatchedPrintStatement MatchedPrintStatement);
    public void visit(MatchedReadStatement MatchedReadStatement);
    public void visit(MatchedExprReturnStatement MatchedExprReturnStatement);
    public void visit(MatchedContinueStatement MatchedContinueStatement);
    public void visit(MatchedBreakStatement MatchedBreakStatement);
    public void visit(MatchedSwitchStatement MatchedSwitchStatement);
    public void visit(MatchedDoWhileStatement MatchedDoWhileStatement);
    public void visit(MatchedIfStatement MatchedIfStatement);
    public void visit(MatchedAssignmentStatement MatchedAssignmentStatement);
    public void visit(SingleUnmatchedStatement SingleUnmatchedStatement);
    public void visit(SingleMatchedStatement SingleMatchedStatement);
    public void visit(EmptyBracedStatementListHead EmptyBracedStatementListHead);
    public void visit(BracedStatementListElement BracedStatementListElement);
    public void visit(BracedStatement BracedStatement);
    public void visit(SingleStatementType SingleStatementType);
    public void visit(BracedStatementType BracedStatementType);
    public void visit(EmptyStatementListHead EmptyStatementListHead);
    public void visit(StatementListElement StatementListElement);
    public void visit(Type Type);
    public void visit(ArrayFormalParameter ArrayFormalParameter);
    public void visit(ScalarFormalParameter ScalarFormalParameter);
    public void visit(FormParametersListHead FormParametersListHead);
    public void visit(FormalParametersListElement FormalParametersListElement);
    public void visit(VoidMethodTypeNamePair VoidMethodTypeNamePair);
    public void visit(TypeMethodTypeNamePair TypeMethodTypeNamePair);
    public void visit(NoMethodFormalParameters NoMethodFormalParameters);
    public void visit(MethodFormalParameters MethodFormalParameters);
    public void visit(MethodDeclaration MethodDeclaration);
    public void visit(EmptyMethodDeclarationListHead EmptyMethodDeclarationListHead);
    public void visit(MethodDeclarationListElement MethodDeclarationListElement);
    public void visit(ExtendedClassSplitter ExtendedClassSplitter);
    public void visit(BaseClassName BaseClassName);
    public void visit(ClassName ClassName);
    public void visit(NoClassMethodDeclarations NoClassMethodDeclarations);
    public void visit(ClassMethodDeclarations ClassMethodDeclarations);
    public void visit(ClassDeclaration ClassDeclaration);
    public void visit(ExtendedClassDeclaration ExtendedClassDeclaration);
    public void visit(ArrayVarIdent ArrayVarIdent);
    public void visit(ScalarVarIdent ScalarVarIdent);
    public void visit(VarIdentError VarIdentError);
    public void visit(VarIdentListHead VarIdentListHead);
    public void visit(VarIdentListElement VarIdentListElement);
    public void visit(VarDeclType VarDeclType);
    public void visit(VariableDeclarationError VariableDeclarationError);
    public void visit(VariableDeclaration VariableDeclaration);
    public void visit(EmptyVarDeclList EmptyVarDeclList);
    public void visit(VarDeclListElement VarDeclListElement);
    public void visit(BoolConstNameValuePair BoolConstNameValuePair);
    public void visit(CharConstNameValuePair CharConstNameValuePair);
    public void visit(NumberConstNameValuePair NumberConstNameValuePair);
    public void visit(ConstNameValuePairListHead ConstNameValuePairListHead);
    public void visit(ConstNameValuePairListElement ConstNameValuePairListElement);
    public void visit(ConstDeclType ConstDeclType);
    public void visit(ConstDecl ConstDecl);
    public void visit(ClassDataDecl ClassDataDecl);
    public void visit(VarDataDecl VarDataDecl);
    public void visit(ConstDataDecl ConstDataDecl);
    public void visit(EmptyDataDeclListHead EmptyDataDeclListHead);
    public void visit(DataDeclListElement DataDeclListElement);
    public void visit(ProgramName ProgramName);
    public void visit(Program Program);

}
