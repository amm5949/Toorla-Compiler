package compiler;

import gen.ToorlaListener;
import gen.ToorlaParser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;


import java.util.Vector;

public class ProgramPrinter implements ToorlaListener {

    Vector<Object> tables = new Vector<>();
    String entryClass = "";
    @Override
    public void enterProgram(ToorlaParser.ProgramContext ctx) {
        SymbolTable program = new SymbolTable();
        String className = ctx.classDeclaration().get(0).className.getText();
        String mainClass = (ctx.mainclass != null) ? ctx.mainclass.classDeclaration().className
                .getText() : "";
        entryClass = mainClass;
        String parentClass = (ctx.classDeclaration().get(0).classParent != null) ?
                ctx.classDeclaration().get(0).classParent.getText() : "[]";
        String parentMainClass = (ctx.mainclass.classDeclaration().classParent != null) ?
                ctx.mainclass.classDeclaration().classParent.getText() : "[]";
//        System.out.println(parentClass);
        String key_class = "Class_" + className;
        String value_class = "Class (name: " + className + ") (parent: " + parentClass +
                ") (isEntry: False)";
        program.setNameAndScopeNumber("Program", 1);
        program.makeHashTable(key_class, value_class);
//        System.out.println(mainClass);
        String keyMainClass = "Class_" + mainClass;
        String valueMainClass = "Class (name: " + mainClass + ") " +
                "(parent: " + parentMainClass + ") (isEntry: True)";
        program.makeHashTable(keyMainClass, valueMainClass);
        System.out.println(program);
        tables.add(program);
//        System.out.println(ctx.classDeclaration().get(0).className.getText());
    }

    @Override
    public void exitProgram(ToorlaParser.ProgramContext ctx) {
//        System.out.println("goodbye Program }");
    }

    @Override
    public void enterClassDeclaration(ToorlaParser.ClassDeclarationContext ctx) {

        SymbolTable classCriteria = new SymbolTable();
        if (!ctx.className.getText().equals(this.entryClass)) {
            String className = ctx.className.getText();
            int line = ctx.className.getLine();
            classCriteria.setNameAndScopeNumber(className, line);
        }

        String key;
        String value;

        for (ToorlaParser.MethodDeclarationContext method : ctx.methodDeclaration()) {
            String parameter1 = (method.param1 != null) ? method.param1.getText() : "";
            String parameterType1 = (method.typeP1 != null) ? method.typeP1.getText() : "";
            String parameter2 = (method.param2 != null) ? method.param2.getText() : "";
            String parameterType2 = (method.typeP2 != null) ? method.typeP2.getText() : "";
            String returnType = method.t.getText();
            String parentClass = method.parent.getChild(1).getText();
            String methodName = method.methodName.getText();
            boolean isConstructor;

            if (!parentClass.equals(this.entryClass)) {
                isConstructor = methodName.equals(parentClass);
                String type = (isConstructor) ? "Constructor" : "Method";
                key = type + "_" + methodName;
                value = type + " (name : " + methodName + ") (return type: [" + returnType + "]) " +
                        "(parameter list: " + ((!parameter1.equals("")) ? "[name: " + parameter1 +
                        ", type: " + parameterType1 + ", index: 1]" + ((!parameter2.equals("")) ?
                        ", [name: " + parameter2 + ", type: " + parameterType2 + ", index: 2]" : "")
                        : "[]") + ")";
                classCriteria.makeHashTable(key, value);
            }
        }
        for (ToorlaParser.FieldDeclarationContext field : ctx.fieldDeclaration()) {
            String fieldName = (field.fieldName != null) ? field.fieldName.getText() : "";
            String fieldType = (field.fieldType != null) ? field.fieldType.getText() : "";
            key = "Field_" + fieldName;
            value = "ClassField (name: " + fieldName + ")" +
                    " (type: " + fieldType + " isDefined: " +
                    ((!fieldName.equals("") || !fieldType.equals("")) ? "True)" : "False)");
            classCriteria.makeHashTable(key, value);
        }
        if (!ctx.className.getText().equals(this.entryClass)) {
            System.out.println(classCriteria);
        }

//        System.out.println(ctx.fieldDeclaration().iterator().next().fieldName.getText());
        tables.add(classCriteria);
    }

    @Override
    public void exitClassDeclaration(ToorlaParser.ClassDeclarationContext ctx) {

    }

    @Override
    public void enterEntryClassDeclaration(ToorlaParser.EntryClassDeclarationContext ctx) {
        SymbolTable entryClassCriteria = new SymbolTable();
        String key;
        String value;
        int entryClassLine = ctx.classDeclaration().className.getLine();
        entryClassCriteria.setNameAndScopeNumber(this.entryClass, entryClassLine);

        for (ToorlaParser.MethodDeclarationContext method : ctx.classDeclaration().
                methodDeclaration()) {
            String methodName = method.methodName.getText();
            String parameter1 = (method.param1 != null) ? method.param1.getText() : "";
            String parameterType1 = (method.typeP1 != null) ? method.typeP1.getText() : "";
            String parameter2 = (method.param2 != null) ? method.param2.getText() : "";
            String parameterType2 = (method.typeP2 != null) ? method.typeP2.getText() : "";
            String returnType = method.t.getText();
            key = "Method_" + methodName;
            value = "Method (name: " + methodName + ") (return type: [" + returnType + "]) " +
                    "(parameter list: " + ((!parameter1.equals("")) ? "[name: " + parameter1 +
                    ", type: " + parameterType1 + ", index: 1]" + ((!parameter2.equals("")) ?
                    ", [name: " + parameter2 + ", type: " + parameterType2 + ", index: 2]" : "")
                    : "[]") + ")";

            entryClassCriteria.makeHashTable(key, value);
        }
        System.out.println(entryClassCriteria);
        tables.add(entryClassCriteria);
    }

    @Override
    public void exitEntryClassDeclaration(ToorlaParser.EntryClassDeclarationContext ctx) {

    }

    @Override
    public void enterFieldDeclaration(ToorlaParser.FieldDeclarationContext ctx) {

    }

    @Override
    public void exitFieldDeclaration(ToorlaParser.FieldDeclarationContext ctx) {

    }

    @Override
    public void enterAccess_modifier(ToorlaParser.Access_modifierContext ctx) {

    }

    @Override
    public void exitAccess_modifier(ToorlaParser.Access_modifierContext ctx) {

    }

    @Override
    public void enterMethodDeclaration(ToorlaParser.MethodDeclarationContext ctx) {
//        TODO: vardef and assignments and params should be used to find the variables
        String methodName = ctx.methodName.getText();
        int methodLine = ctx.methodName.getLine();
        SymbolTable methodCriteria = new SymbolTable();
        methodCriteria.setNameAndScopeNumber(methodName, methodLine);
        boolean isDefined = false;
        String key;
        String value;

//        - Checking Parameters
        Token param1 = ctx.param1;
        if (param1 != null) {
            String parameterName = param1.getText();
            String parameterType = ctx.typeP1.getText();
            boolean isClassTyped = false;
            if (parameterType.contains("string") || parameterType.contains("int")
                    || parameterType.contains("bool")) {
                isDefined = true;
            }
            if (parameterType.contains("[") || !(parameterType.contains("string")
                    || parameterType.contains("bool") || parameterType.contains("int"))) {
                isClassTyped = true;
            }
            key = "Field_" + parameterName;
            value = "ParamField (name: " + parameterName + ") (type: " + ((isClassTyped) ?
                    "[ classtyped= " + parameterType + ", isDefined: " + ((isDefined) ?
                            "True])" : "False])") : parameterType + ", isDefined: " + ((isDefined) ?
                    "True)" : "False)"));
            methodCriteria.makeHashTable(key, value);
            Token param2 = ctx.param2;
            if (param2 != null) {
                String parameterName2 = param2.getText();
                String parameterType2 = ctx.typeP2.getText();

                if (parameterType2.contains("[") || !(parameterType2.contains("string")
                        || parameterType2.contains("bool") || parameterType2.contains("int"))) {
                    isClassTyped = true;
                }

                if (parameterType2.contains("string") || parameterType2.contains("int")
                        || parameterType2.contains("bool")) {
                    isDefined = true;
                }
                key = "Field_" + parameterName2;
                value = "ParamField (name: " + parameterName2 + ") (type: " + ((isClassTyped) ?
                        "[ classtyped= " + parameterType2 + ", isDefined: " + ((isDefined) ?
                                "True])" : "False])") : parameterType2 + ", isDefined: " + ((isDefined) ?
                        "True)" : "False)"));
                methodCriteria.makeHashTable(key, value);
            }
        }


        for (ToorlaParser.StatementContext statement : ctx.statement()) {
            ToorlaParser.StatementAssignmentContext assignmentContext = statement.s1.s5;
            ToorlaParser.StatementVarDefContext varDefContext = statement.s1.s7;
//            using assignmentContext to find the variables which declared by keyword "new"
            if (assignmentContext != null) {
                String variableName = assignmentContext.left.getText();
                if (assignmentContext.right.getText().contains("new")) {
                    String newOperatorVariablesType = assignmentContext.right.getText()
                            .substring(3);
                    isDefined = true;
                    if (newOperatorVariablesType.contains("int") ||
                            newOperatorVariablesType.contains("string") ||
                            newOperatorVariablesType.contains("bool")) {
                        newOperatorVariablesType = newOperatorVariablesType.
                                replaceAll("[0-9]+", "");
                    }
                    key = "Field_" + variableName;
                    value = "MethodVar (name: " + variableName + ") (type: [ localVar= " +
                            newOperatorVariablesType + ", isDefined: " + ((isDefined) ? "True)" : "False)");
                    methodCriteria.makeHashTable(key, value);

                }
            }
//            using varDefContext to find out the variables
            if (varDefContext != null) {
                final String Range = "0123456789";
                String variableName = varDefContext.i1.getText();
                String variableValue = varDefContext.e1.getText();
                String variableType = "";
                if (Range.contains(variableValue)) {
                    variableType = "int";
                    isDefined = true;

                } else if (variableValue.contains("\"")) {
                    variableType = "string";
                    isDefined = true;
                } else if (variableValue.contains("false") || variableValue.contains("true")) {
                    variableType = "bool";
                    isDefined = true;
                }
                key = "Field_" + variableName;
                value = "MethodVar (name: " + variableName + ") (type: [ localVar= " + variableType
                        + ", isDefined: " + ((isDefined) ? "True)" : "False)");
                methodCriteria.makeHashTable(key, value);

            }

        }
        System.out.println(methodCriteria);
    }

    @Override
    public void exitMethodDeclaration(ToorlaParser.MethodDeclarationContext ctx) {

    }

    @Override
    public void enterClosedStatement(ToorlaParser.ClosedStatementContext ctx) {

    }

    @Override
    public void exitClosedStatement(ToorlaParser.ClosedStatementContext ctx) {

    }

    @Override
    public void enterClosedConditional(ToorlaParser.ClosedConditionalContext ctx) {

    }

    @Override
    public void exitClosedConditional(ToorlaParser.ClosedConditionalContext ctx) {

    }

    @Override
    public void enterOpenConditional(ToorlaParser.OpenConditionalContext ctx) {

    }

    @Override
    public void exitOpenConditional(ToorlaParser.OpenConditionalContext ctx) {

    }

    @Override
    public void enterOpenStatement(ToorlaParser.OpenStatementContext ctx) {

    }

    @Override
    public void exitOpenStatement(ToorlaParser.OpenStatementContext ctx) {

    }

    @Override
    public void enterStatement(ToorlaParser.StatementContext ctx) {

    }

    @Override
    public void exitStatement(ToorlaParser.StatementContext ctx) {

    }

    @Override
    public void enterStatementVarDef(ToorlaParser.StatementVarDefContext ctx) {

    }

    @Override
    public void exitStatementVarDef(ToorlaParser.StatementVarDefContext ctx) {

    }

    @Override
    public void enterStatementBlock(ToorlaParser.StatementBlockContext ctx) {

    }

    @Override
    public void exitStatementBlock(ToorlaParser.StatementBlockContext ctx) {

    }

    @Override
    public void enterStatementContinue(ToorlaParser.StatementContinueContext ctx) {

    }

    @Override
    public void exitStatementContinue(ToorlaParser.StatementContinueContext ctx) {

    }

    @Override
    public void enterStatementBreak(ToorlaParser.StatementBreakContext ctx) {

    }

    @Override
    public void exitStatementBreak(ToorlaParser.StatementBreakContext ctx) {

    }

    @Override
    public void enterStatementReturn(ToorlaParser.StatementReturnContext ctx) {

    }

    @Override
    public void exitStatementReturn(ToorlaParser.StatementReturnContext ctx) {

    }

    @Override
    public void enterStatementClosedLoop(ToorlaParser.StatementClosedLoopContext ctx) {

    }

    @Override
    public void exitStatementClosedLoop(ToorlaParser.StatementClosedLoopContext ctx) {

    }

    @Override
    public void enterStatementOpenLoop(ToorlaParser.StatementOpenLoopContext ctx) {

    }

    @Override
    public void exitStatementOpenLoop(ToorlaParser.StatementOpenLoopContext ctx) {

    }

    @Override
    public void enterStatementWrite(ToorlaParser.StatementWriteContext ctx) {

    }

    @Override
    public void exitStatementWrite(ToorlaParser.StatementWriteContext ctx) {

    }

    @Override
    public void enterStatementAssignment(ToorlaParser.StatementAssignmentContext ctx) {

    }

    @Override
    public void exitStatementAssignment(ToorlaParser.StatementAssignmentContext ctx) {

    }

    @Override
    public void enterStatementInc(ToorlaParser.StatementIncContext ctx) {

    }

    @Override
    public void exitStatementInc(ToorlaParser.StatementIncContext ctx) {

    }

    @Override
    public void enterStatementDec(ToorlaParser.StatementDecContext ctx) {

    }

    @Override
    public void exitStatementDec(ToorlaParser.StatementDecContext ctx) {

    }

    @Override
    public void enterExpression(ToorlaParser.ExpressionContext ctx) {

    }

    @Override
    public void exitExpression(ToorlaParser.ExpressionContext ctx) {

    }

    @Override
    public void enterExpressionOr(ToorlaParser.ExpressionOrContext ctx) {

    }

    @Override
    public void exitExpressionOr(ToorlaParser.ExpressionOrContext ctx) {

    }

    @Override
    public void enterExpressionOrTemp(ToorlaParser.ExpressionOrTempContext ctx) {

    }

    @Override
    public void exitExpressionOrTemp(ToorlaParser.ExpressionOrTempContext ctx) {

    }

    @Override
    public void enterExpressionAnd(ToorlaParser.ExpressionAndContext ctx) {

    }

    @Override
    public void exitExpressionAnd(ToorlaParser.ExpressionAndContext ctx) {

    }

    @Override
    public void enterExpressionAndTemp(ToorlaParser.ExpressionAndTempContext ctx) {

    }

    @Override
    public void exitExpressionAndTemp(ToorlaParser.ExpressionAndTempContext ctx) {

    }

    @Override
    public void enterExpressionEq(ToorlaParser.ExpressionEqContext ctx) {

    }

    @Override
    public void exitExpressionEq(ToorlaParser.ExpressionEqContext ctx) {

    }

    @Override
    public void enterExpressionEqTemp(ToorlaParser.ExpressionEqTempContext ctx) {

    }

    @Override
    public void exitExpressionEqTemp(ToorlaParser.ExpressionEqTempContext ctx) {

    }

    @Override
    public void enterExpressionCmp(ToorlaParser.ExpressionCmpContext ctx) {

    }

    @Override
    public void exitExpressionCmp(ToorlaParser.ExpressionCmpContext ctx) {

    }

    @Override
    public void enterExpressionCmpTemp(ToorlaParser.ExpressionCmpTempContext ctx) {

    }

    @Override
    public void exitExpressionCmpTemp(ToorlaParser.ExpressionCmpTempContext ctx) {

    }

    @Override
    public void enterExpressionAdd(ToorlaParser.ExpressionAddContext ctx) {

    }

    @Override
    public void exitExpressionAdd(ToorlaParser.ExpressionAddContext ctx) {

    }

    @Override
    public void enterExpressionAddTemp(ToorlaParser.ExpressionAddTempContext ctx) {

    }

    @Override
    public void exitExpressionAddTemp(ToorlaParser.ExpressionAddTempContext ctx) {

    }

    @Override
    public void enterExpressionMultMod(ToorlaParser.ExpressionMultModContext ctx) {

    }

    @Override
    public void exitExpressionMultMod(ToorlaParser.ExpressionMultModContext ctx) {

    }

    @Override
    public void enterExpressionMultModTemp(ToorlaParser.ExpressionMultModTempContext ctx) {

    }

    @Override
    public void exitExpressionMultModTemp(ToorlaParser.ExpressionMultModTempContext ctx) {

    }

    @Override
    public void enterExpressionUnary(ToorlaParser.ExpressionUnaryContext ctx) {

    }

    @Override
    public void exitExpressionUnary(ToorlaParser.ExpressionUnaryContext ctx) {

    }

    @Override
    public void enterExpressionMethods(ToorlaParser.ExpressionMethodsContext ctx) {

    }

    @Override
    public void exitExpressionMethods(ToorlaParser.ExpressionMethodsContext ctx) {

    }

    @Override
    public void enterExpressionMethodsTemp(ToorlaParser.ExpressionMethodsTempContext ctx) {

    }

    @Override
    public void exitExpressionMethodsTemp(ToorlaParser.ExpressionMethodsTempContext ctx) {

    }

    @Override
    public void enterExpressionOther(ToorlaParser.ExpressionOtherContext ctx) {

    }

    @Override
    public void exitExpressionOther(ToorlaParser.ExpressionOtherContext ctx) {

    }

    @Override
    public void enterToorlaType(ToorlaParser.ToorlaTypeContext ctx) {

    }

    @Override
    public void exitToorlaType(ToorlaParser.ToorlaTypeContext ctx) {

    }

    @Override
    public void enterSingleType(ToorlaParser.SingleTypeContext ctx) {

    }

    @Override
    public void exitSingleType(ToorlaParser.SingleTypeContext ctx) {

    }

    @Override
    public void visitTerminal(TerminalNode terminalNode) {

    }

    @Override
    public void visitErrorNode(ErrorNode errorNode) {

    }

    @Override
    public void enterEveryRule(ParserRuleContext parserRuleContext) {

    }

    @Override
    public void exitEveryRule(ParserRuleContext parserRuleContext) {

    }
}
