package compiler;

import gen.ToorlaListener;
import gen.ToorlaParser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

public class ProgramPrinter implements ToorlaListener {
    String identation = "    ";
    @Override
    public void enterProgram(ToorlaParser.ProgramContext ctx) {
        System.out.println("program start {");
    }

    @Override
    public void exitProgram(ToorlaParser.ProgramContext ctx) {
        System.out.println("}");
    }

    @Override
    public void enterClassDeclaration(ToorlaParser.ClassDeclarationContext ctx) {

        String parentChecking = ctx.parent.getText();
        parentChecking = parentChecking.substring(0, 5);
        if (!parentChecking.equalsIgnoreCase("entry")) {
//            System.out.println("it is entry");
            if (ctx.classParent == null) {
                System.out.println(identation + "class: " + ctx.className.getText() + " / class parent: " +
                        "none" + " / isEntry: false {");
            }
            else {
                System.out.println(identation + "class: " + ctx.className.getText() + " / class parent: " +
                        ctx.classParent.getText() + " / isEntry: false {");
            }
        }



    }

    @Override
    public void exitClassDeclaration(ToorlaParser.ClassDeclarationContext ctx) {

        String parentCheckingInExit = ctx.parent.getText();
        parentCheckingInExit = parentCheckingInExit.substring(0, 5);
        if (!parentCheckingInExit.equalsIgnoreCase("entry")) {
            System.out.println(identation + "}");
        }
//        System.out.println("}");
    }

    @Override
    public void enterEntryClassDeclaration(ToorlaParser.EntryClassDeclarationContext ctx) {
//        System.out.println(ctx.getRuleIndex());
//        String classParent;
        if (ctx.classDeclaration().classParent == null) {
            System.out.println(identation + "class: " + ctx.classDeclaration().className.getText() + " / class parent: "
                + "none" +" / isEntry: true {");
        }
        else {
            System.out.println(identation + "class: " + ctx.classDeclaration().className.getText() + " / class parent: "
                + ctx.classDeclaration().classParent.getText() + " / isEntry: true {");
        }

    }

    @Override
    public void exitEntryClassDeclaration(ToorlaParser.EntryClassDeclarationContext ctx) {
        System.out.println(identation + "}");
    }

    @Override
    public void enterFieldDeclaration(ToorlaParser.FieldDeclarationContext ctx) {
        System.out.println(identation + identation + "field: " + ctx.fieldName.getText() +
                " / type: " + ctx.fieldType.getText());
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
//        System.out.println(ctx.t.getText()); // important
//        System.out.println(ctx.parent.getChild(1).getText()); // classname
//        System.out.println(ctx.t.getText()); // return type of methods
//        System.out.println(ctx.param1); // check whether it is null or not
//        System.out.println(ctx.methodAccessModifier); // if it is null, we should expect that it is public
//        System.out.println(ctx.param1);
//        System.out.println(identation +  identation + "1");
        String className = ctx.parent.getChild(1).getText();
        String method = ctx.methodName.getText();
        String returnType = ctx.t.getText();
        String access = ctx.methodAccessModifier == null ? "public" : ctx.methodAccessModifier.getText();
        if(method.equals(className)) {
            System.out.println(identation + identation + "class constructor: " + className + " / return type: "
                    + returnType + "/ type: " + access + "{");
        } else if (method.equals("main")) {
            System.out.println(identation + identation + method + " method / type: " + returnType + "{");
        } else {
            System.out.println(identation + identation + "class method: " + method + " / return type: "
                    + returnType + "/ type: " + access + "{");
        }

        if (ctx.param1 != null) {
            String parameterName1 = ctx.param1.getText();
            String paramType1 = ctx.typeP1.getText();
            if (ctx.param2 != null) {
                String parameterName2 = ctx.param2.getText();
                String paramType2 = ctx.typeP2.getText();
                System.out.println(identation + identation + identation + "parameter list: [type: " + paramType1 + " / name: "
                        + parameterName1 + ", type: " + paramType2 + " / name: " + parameterName2 + "]");
            }
            else {
                System.out.println(identation + identation + identation + "parameter list: [type: " + paramType1 + " / name: "+ parameterName1 + "]");
            }
        } else if (!method.equals("main")) {
            System.out.println(identation + identation + identation + "parameter list: []");
        }

    }

    @Override
    public void exitMethodDeclaration(ToorlaParser.MethodDeclarationContext ctx) {
        System.out.println(identation + identation + "}");
    }

    @Override
    public void enterClosedStatement(ToorlaParser.ClosedStatementContext ctx) {
//        System.out.println(ctx.s7 != null ? ctx.s7.children : null);
//        if (ctx.s7 != null) {
//            System.out.println(identation + identation + identation +"field: " + ctx.s7.children.get(1)
//                    + " / type: local var");
//        }
//        System.out.println(ctx.s5 != null ? ctx.s5.children.get(2).getText() : "");
        if (ctx.s5 != null) {
            String variableName = ctx.s5.left.getText();
            if (ctx.s5.right.getText().contains("new")) {
                String newOperatorVariablesType = ctx.s5.right.getText().substring(3);
//                System.out.println(newOperatorVariablesType);
                if (newOperatorVariablesType.contains("int") ||
                        newOperatorVariablesType.contains("string") ||
                        newOperatorVariablesType.contains("bool")) {
                    newOperatorVariablesType = newOperatorVariablesType.replaceAll("[0-9]+", "");
                    System.out.println(identation + identation + identation +"field: "
                            + variableName + " / type: " + newOperatorVariablesType);
                }
            }
        }
    }

    @Override
    public void exitClosedStatement(ToorlaParser.ClosedStatementContext ctx) {
//        System.out.println(ctx.getText());
    }

    @Override
    public void enterClosedConditional(ToorlaParser.ClosedConditionalContext ctx) {
//        System.out.println(ctx.getText());
    }

    @Override
    public void exitClosedConditional(ToorlaParser.ClosedConditionalContext ctx) {

    }

    @Override
    public void enterOpenConditional(ToorlaParser.OpenConditionalContext ctx) {
//        System.out.println(ctx.);
    }

    @Override
    public void exitOpenConditional(ToorlaParser.OpenConditionalContext ctx) {

    }

    @Override
    public void enterOpenStatement(ToorlaParser.OpenStatementContext ctx) {
//        System.out.println(ctx.);
    }

    @Override
    public void exitOpenStatement(ToorlaParser.OpenStatementContext ctx) {

    }

    @Override
    public void enterStatement(ToorlaParser.StatementContext ctx) {
//        System.out.println(ctx.);
    }

    @Override
    public void exitStatement(ToorlaParser.StatementContext ctx) {

    }

    @Override
    public void enterStatementVarDef(ToorlaParser.StatementVarDefContext ctx) {
//        System.out.println(ctx.i1.getText());
        System.out.println(ctx.parent.getText());
//        System.out.println(identation + identation + identation + "field: " + ctx.i1.getText() + " / type: local var");
    }

    @Override
    public void exitStatementVarDef(ToorlaParser.StatementVarDefContext ctx) {

    }

    @Override
    public void enterStatementBlock(ToorlaParser.StatementBlockContext ctx) {
//        System.out.println(ctx.getText());
//        System.out.println(ctx.children);
//        System.out.println("nested {");
////        System.out.println(ctx.children.get(1).getText());
//        String statementBlock = ctx.children.get(1).getText();
////        System.out.println(statementBlock.indexOf("="));
//        int index = statementBlock.indexOf("=");
//        if (index >= 0) {
//            String paramName = statementBlock.substring(3, index);
//            System.out.println(identation + "field: " + paramName + " / type: local var");
//        }
    }

    @Override
    public void exitStatementBlock(ToorlaParser.StatementBlockContext ctx) {
        System.out.println("}");
    }

    @Override
    public void enterStatementContinue(ToorlaParser.StatementContinueContext ctx) {
//        System.out.println(ctx.getText());
    }

    @Override
    public void exitStatementContinue(ToorlaParser.StatementContinueContext ctx) {

    }

    @Override
    public void enterStatementBreak(ToorlaParser.StatementBreakContext ctx) {
//        System.out.println(ctx.getText());
    }

    @Override
    public void exitStatementBreak(ToorlaParser.StatementBreakContext ctx) {

    }

    @Override
    public void enterStatementReturn(ToorlaParser.StatementReturnContext ctx) {
//        System.out.println(ctx.getText());
    }

    @Override
    public void exitStatementReturn(ToorlaParser.StatementReturnContext ctx) {

    }

    @Override
    public void enterStatementClosedLoop(ToorlaParser.StatementClosedLoopContext ctx) {
//        System.out.println(ctx.getText());
    }

    @Override
    public void exitStatementClosedLoop(ToorlaParser.StatementClosedLoopContext ctx) {

    }

    @Override
    public void enterStatementOpenLoop(ToorlaParser.StatementOpenLoopContext ctx) {
//        System.out.println(ctx.getText());
//        what is this?
    }

    @Override
    public void exitStatementOpenLoop(ToorlaParser.StatementOpenLoopContext ctx) {

    }

    @Override
    public void enterStatementWrite(ToorlaParser.StatementWriteContext ctx) {
//        System.out.println(ctx.getText());
    }

    @Override
    public void exitStatementWrite(ToorlaParser.StatementWriteContext ctx) {

    }

    @Override
    public void enterStatementAssignment(ToorlaParser.StatementAssignmentContext ctx) {
//        System.out.println(ctx.getText());
    }

    @Override
    public void exitStatementAssignment(ToorlaParser.StatementAssignmentContext ctx) {

    }

    @Override
    public void enterStatementInc(ToorlaParser.StatementIncContext ctx) {
//        System.out.println(ctx.getText());
    }

    @Override
    public void exitStatementInc(ToorlaParser.StatementIncContext ctx) {

    }

    @Override
    public void enterStatementDec(ToorlaParser.StatementDecContext ctx) {
//        System.out.println(ctx.getText());
    }

    @Override
    public void exitStatementDec(ToorlaParser.StatementDecContext ctx) {

    }

    @Override
    public void enterExpression(ToorlaParser.ExpressionContext ctx) {
//        System.out.println(ctx.getText());
    }

    @Override
    public void exitExpression(ToorlaParser.ExpressionContext ctx) {

    }

    @Override
    public void enterExpressionOr(ToorlaParser.ExpressionOrContext ctx) {
//        System.out.println(ctx.getText());
    }

    @Override
    public void exitExpressionOr(ToorlaParser.ExpressionOrContext ctx) {

    }

    @Override
    public void enterExpressionOrTemp(ToorlaParser.ExpressionOrTempContext ctx) {
//        System.out.println(ctx.getText());
//        what is this?
    }

    @Override
    public void exitExpressionOrTemp(ToorlaParser.ExpressionOrTempContext ctx) {

    }

    @Override
    public void enterExpressionAnd(ToorlaParser.ExpressionAndContext ctx) {
//        System.out.println(ctx.getText());
    }

    @Override
    public void exitExpressionAnd(ToorlaParser.ExpressionAndContext ctx) {

    }

    @Override
    public void enterExpressionAndTemp(ToorlaParser.ExpressionAndTempContext ctx) {
//        System.out.println(ctx.getText());
    }

    @Override
    public void exitExpressionAndTemp(ToorlaParser.ExpressionAndTempContext ctx) {

    }

    @Override
    public void enterExpressionEq(ToorlaParser.ExpressionEqContext ctx) {
//        System.out.println(ctx.getText());
    }

    @Override
    public void exitExpressionEq(ToorlaParser.ExpressionEqContext ctx) {

    }

    @Override
    public void enterExpressionEqTemp(ToorlaParser.ExpressionEqTempContext ctx) {
//        System.out.println(ctx.getText());
    }

    @Override
    public void exitExpressionEqTemp(ToorlaParser.ExpressionEqTempContext ctx) {

    }

    @Override
    public void enterExpressionCmp(ToorlaParser.ExpressionCmpContext ctx) {
//        System.out.println(ctx.getText());

    }

    @Override
    public void exitExpressionCmp(ToorlaParser.ExpressionCmpContext ctx) {

    }

    @Override
    public void enterExpressionCmpTemp(ToorlaParser.ExpressionCmpTempContext ctx) {
//        System.out.println(ctx.getText());
    }

    @Override
    public void exitExpressionCmpTemp(ToorlaParser.ExpressionCmpTempContext ctx) {

    }

    @Override
    public void enterExpressionAdd(ToorlaParser.ExpressionAddContext ctx) {
//        System.out.println(ctx.getText());
    }

    @Override
    public void exitExpressionAdd(ToorlaParser.ExpressionAddContext ctx) {

    }

    @Override
    public void enterExpressionAddTemp(ToorlaParser.ExpressionAddTempContext ctx) {
//        System.out.println(ctx.getText());
    }

    @Override
    public void exitExpressionAddTemp(ToorlaParser.ExpressionAddTempContext ctx) {

    }

    @Override
    public void enterExpressionMultMod(ToorlaParser.ExpressionMultModContext ctx) {
//        System.out.println(ctx.getText());
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
//        System.out.println(ctx.getText());
    }

    @Override
    public void exitExpressionUnary(ToorlaParser.ExpressionUnaryContext ctx) {

    }

    @Override
    public void enterExpressionMethods(ToorlaParser.ExpressionMethodsContext ctx) {
//        System.out.println(ctx.mt.i);
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
//        System.out.println(ctx.getText());
//        what is this?
    }

    @Override
    public void exitExpressionOther(ToorlaParser.ExpressionOtherContext ctx) {

    }

    @Override
    public void enterToorlaType(ToorlaParser.ToorlaTypeContext ctx) {
//        System.out.println(ctx.getText());
    }

    @Override
    public void exitToorlaType(ToorlaParser.ToorlaTypeContext ctx) {

    }

    @Override
    public void enterSingleType(ToorlaParser.SingleTypeContext ctx) {
//        System.out.println(ctx.getText());
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
