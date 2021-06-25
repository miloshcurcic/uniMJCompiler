package rs.ac.bg.etf.pp1.error;

import rs.ac.bg.etf.pp1.test.CompilerError;

public class SemanticError extends CompilerError {
    public static final String ARRAY_REFERENCE_STRING = "an Array reference";
    public static final String CLASS_REFERENCE_STRING = "a Class reference";
    public static final String METHOD_OR_FUNCTION_STRING = "a method or a global function";

    public static final String INVALID_DESIGNATOR_ACCESS_TEMPLATE = "Invalid access, accessed designator [%s] is not %s!";

    public static final String INVALID_RETURN_WITHIN_SWITCH = "Invalid return statement, return statement cannot be used within a switch expression!";
    public static final String INVALID_RETURN_TYPE_MISMATCH = "Invalid return statement, return expression type differs from the function return type!";
    public static final String INVALID_RETURN_NO_EXPRESSION = "Invalid return statement, return expression expected!";

    public static final String INVALID_YIELD_OUTSIDE_SWITCH = "Invalid yield statement, yield statement can only be used within a switch expression!";

    public static final String INVALID_DESIGNATOR_MUST_BE_INT = "Invalid designator, designator must be of Integer type!";
    public static final String INVALID_EXPRESSION_MUST_BE_INT = "Invalid expression, expression result must be of Integer type!";

    public static final String INVALID_ACCESS_UNKNOWN_COMPLEX_DESIGNATOR_TEMPLATE = "Undeclared symbol used as %s!";
    public static final String INVALID_ACCESS_UNKNOWN_CLASS_MEMBER_TEMPLATE = "Invalid access, %s is not a member field or method of the accessed Class object!";
    public static final String INVALID_NEW_OBJECT_EXPRESSION_NOT_CLASS_TYPE_TEMPLATE = "Invalid new object expression, %s is not a Class name!";

    public static final String INVALID_FUNCTION_CALL_MISSING_PARAMETERS = "Invalid function call, not enough actual parameters provided!";
    public static final String INVALID_FUNCTION_CALL_EXCESS_PARAMETERS = "Invalid function call, too many actual parameters provided";

    public static final String INCOMPATIBLE_FUNCTION_PARAMETERS = "Invalid function call, actual parameter is incompatible with formal parameter type!";

    public static final String INCOMPATIBLE_TYPES_FOR_RELATIONAL_OPERATIONS = "Expression results are not compatible with each other for relational operations!";
    public static final String INVALID_RELATIONAL_OPERATION_FOR_CLASS_OR_ARRAY = "Invalid relational operator for Class or Array type, only '==' and '!=' are allowed!";

    public static final String INCOMPATIBLE_ASSIGNMENT = "Designator is not compatible with the given expression!";

    public static final String INVALID_SWITCH_DUPLICATE_CASES = "Invalid switch, duplicate case statement!";
    public static final String INVALID_CONTINUE_OUTSIDE_LOOP = "Continue statement can only be used within a loop!";

    public static final String INVALID_DESIGNATOR_NOT_ASSIGNABLE = "Designator is neither Field, Variable or Array Element!";

    public static final String INVALID_TYPE_UNKNOWN_NAME = "Unknown name %s used as type";
    public static final String INVALID_TYPE_NOT_A_TYPE = "The symbol %s is not a type!";
    public static final String INVALID_BASE_CLASS_TYPE = "Base class must be a Class Type!";

    public static final String DUPLICATE_NAME_DECLARATION_TEMPLATE = "Duplicate name declaration for symbol %s!";
    public static final String UNDECLARED_SYMBOL_TEMPLATE = "Undeclared symbol %s used!";

    public static final String MAIN_MISSING = "No valid main method found!";

    public SemanticError(int line, String message) {
        super(line, message, CompilerErrorType.SEMANTIC_ERROR);
    }
}
