package rs.ac.bg.etf.pp1.util;

import org.apache.log4j.Logger;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Scope;
import rs.etf.pp1.symboltable.concepts.Struct;
import rs.etf.pp1.symboltable.visitors.SymbolTableVisitor;

public class CustomDumpSymbolTableVisitor extends SymbolTableVisitor {
	protected StringBuilder output = new StringBuilder();
	protected final String indent = "   ";
	protected StringBuilder currentIndent = new StringBuilder();

	Logger log = Logger.getLogger(getClass());
	
	protected void nextIndentationLevel() {
		currentIndent.append(indent);
	}
	
	protected void previousIndentationLevel() {
		if (currentIndent.length() > 0)
			currentIndent.setLength(currentIndent.length()-indent.length());
	}

	@Override
	public void visitObjNode(Obj objToVisit) {
		output.append(currentIndent.toString());

		switch (objToVisit.getKind()) {
			case Obj.Con:  {
				output.append("Constant ");
				break;
			}
			case Obj.Var: {
				output.append("Variable ");
				break;
			}
			case Obj.Type: {
				output.append("Type ");
				break;
			}
			case Obj.Meth: {
				output.append("Method ");
				break;
			}
			case Obj.Fld: {
				output.append("Field ");
				break;
			}
			case Obj.Prog: {
				output.append("Program ");
				break;
			}
			case Obj.Elem: {
				output.append("Element ");
				break;
			}
		}
		
		output.append(objToVisit.getName());
		output.append(": ");

		if (objToVisit.getKind() != Obj.Prog) {
			if ((objToVisit.getKind() == Obj.Var) && "this".equalsIgnoreCase(objToVisit.getName())) {
				output.append("this");
			} else {
				if (objToVisit.getKind() != Obj.Type) {
					if (objToVisit.getType().getKind() != Struct.Class) {
						objToVisit.getType().accept(this);
					}
					else
					{
						output.append("Class");
					}
				} else {
					if (objToVisit.getType().getKind() == Struct.Class) {
						output.append("Class");
					}
					else
					{
						objToVisit.getType().accept(this);
					}
				}
			}

			if (objToVisit.getKind() == Obj.Con) {
				output.append(", Value: ").append(objToVisit.getAdr());
			} else if (objToVisit.getKind() != Obj.Type) {
				output.append(", Offset: ").append(objToVisit.getAdr());
			}

			if (objToVisit.getKind() == Obj.Var) {
				output.append(", Scope: ");

				if (objToVisit.getLevel() == 1) {
					output.append("local");
				} else {
					output.append("global");
				}
			} else {
				if (objToVisit.getKind() == Obj.Meth) {
					output.append(", Formal params: ").append(objToVisit.getLevel());
				}
			}

			if (objToVisit.getKind() == Obj.Var) {
				output.append(", Formal param position: ").append(objToVisit.getFpPos());
			}

			if ((objToVisit.getKind() == Obj.Type) && (objToVisit.getType().getKind() == Struct.Class)) {
				output.append(", Num fields: ").append(objToVisit.getType().getNumberOfFields()).append("\n");

				objToVisit.getType().accept(this);
			}
			else
			{
				output.append("\n");
			}
		}
		else
		{
			output.append("\n");
		}

		if (objToVisit.getKind() == Obj.Prog || objToVisit.getKind() == Obj.Meth) {
			nextIndentationLevel();
		}
		

		for (Obj o : objToVisit.getLocalSymbols()) {
			o.accept(this);
		}
		
		if (objToVisit.getKind() == Obj.Prog || objToVisit.getKind() == Obj.Meth) {
			previousIndentationLevel();
		}
	}

	@Override
	public void visitScopeNode(Scope scope) {
		for (Obj o : scope.values()) {
			o.accept(this);
			output.append("\n");
		}
	}

	@Override
	public void visitStructNode(Struct structToVisit) {
		switch (structToVisit.getKind()) {
			case Struct.None: {
				output.append("notype");
				break;
			}
			case Struct.Int: {
				output.append("int");
				break;
			}
			case Struct.Char: {
				output.append("char");
				break;
			}
			case Struct.Bool: {
				output.append("bool");
				break;
			}
			case Struct.Array: {
				output.append("Array of ");

				switch (structToVisit.getElemType().getKind()) {
					case Struct.None: {
						output.append("notype");
						break;
					}
					case Struct.Int: {
						output.append("int");
						break;
					}
					case Struct.Char: {
						output.append("char");
						break;
					}
					case Struct.Bool: {
						output.append("bool");
						break;
					}
					case Struct.Class: {
						output.append("Class");
						break;
					}
				}
				break;
			}
			case Struct.Class: {
				nextIndentationLevel();

				for (Obj obj : structToVisit.getMembers()) {
					obj.accept(this);
				}

				previousIndentationLevel();
				break;
			}
		}

	}

	public String getOutput() {
		return output.toString();
	}
	
	
}
