package ntou.soselab.dictionary.parse;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaCodeParse {

    public void getJavaTest() {

    }

    public void getJavaMethodUse() throws IOException {
        // creates an input stream for the file to be parsed
        FileInputStream in = new FileInputStream("E:\\STS_Test\\Dictionary\\src\\main\\resources\\CartServiceImpl.java");

        // parse it
        CompilationUnit cu = JavaParser.parse(in);

        // visit and print the methods names
        // cu.accept(new MethodVisitor(), null);
        // visit and print the class variable
        cu.accept(new ClassVisitor(), null);
//        for(ImportDeclaration str : cu.getImports()) {
//            System.out.println("Import :" + str);
//        }

    }

    private static class MethodVisitor extends VoidVisitorAdapter<Void> {
        @Override
        public void visit(MethodDeclaration n, Void arg) {
            /* here you can access the attributes of the method.
             this method will be called for all methods in this
             CompilationUnit, including inner class methods */
            System.out.println(n.getName()+n.getBody().toString());
            System.out.println("-----------------------------------------------------");
            super.visit(n, arg);
        }
    }

    private static class ClassVisitor extends VoidVisitorAdapter<Void> {
        @Override
        public void visit(ClassOrInterfaceDeclaration n, Void arg) {
        /* here you can access the attributes of the method.
         this method will be called for all methods in this
         CompilationUnit, including inner class methods */
            System.out.println(n.getFields());
            System.out.println("-----------------------------------------------------");
            for(FieldDeclaration field : n.getFields()) {
                String content[] = field.getVariables().toString().split("=");
                System.out.println(content[1].replaceAll("\\p{P}",""));
            }
            super.visit(n, arg);
        }
    }
}
