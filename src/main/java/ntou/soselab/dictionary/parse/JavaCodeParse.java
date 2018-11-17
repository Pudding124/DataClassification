package ntou.soselab.dictionary.parse;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.TypeParameter;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import ntou.soselab.dictionary.bean.CodeFragment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaCodeParse {

    Logger log = LoggerFactory.getLogger(JavaCodeParse.class);


    static String URI = "";

    CodeFragment codeFragment;

    // record all method result
    ArrayList<String> methodResult;

    public void getJavaMethodUse(String uri, String javaDocPath) throws IOException {

        codeFragment = new CodeFragment();

        methodResult = new ArrayList<>();

        URI = uri;
        // creates an input stream for the file to be parsed
        FileInputStream in = new FileInputStream(javaDocPath);

        // parse it
        CompilationUnit cu = JavaParser.parse(in);

        // visit and print the class variable
//        cu.accept(new ClassVisitor(), null);

        // get java class import
//        for(ImportDeclaration str : cu.getImports()) {
//            System.out.println("Import :" + str);
//        }

        ClassVisitor classVisitor = new ClassVisitor();
        classVisitor.visit(cu, null);
        // log.info("Match Methods Name :{}", codeFragment.getFragment());
        log.info("Match Methods Name :{}", methodResult);


//        for(TypeDeclaration type : cu.getTypes()) {
//            // first give all this java doc member
//            List<BodyDeclaration> members = type.getMembers();
//            // check all member content
//            for(BodyDeclaration member : members) {
//                // if member state equal ClassOrInterfaceDeclaration, and you can identify it which is inner class
//                if(member.isClassOrInterfaceDeclaration()) {
//                    log.info("class name :{}", member.asClassOrInterfaceDeclaration().getName());
//                    // get inner class method
//                    for(MethodDeclaration method : member.asClassOrInterfaceDeclaration().getMethods()) {
//                        log.info("Method Name :{}", method.getName());
//                    }
//                    VerifyInnerClassAndParse(member.asClassOrInterfaceDeclaration());
//                }
//            }
//        }
    }

    class ClassVisitor extends VoidVisitorAdapter<Void> {
        @Override
        public void visit(ClassOrInterfaceDeclaration n, Void arg) {
        /* here you can access the attributes of the method.
         this method will be called for all methods in this
         CompilationUnit, including inner class methods */

            // find url fragment
            findAPIUseFragment(n);
            // verify inner class
            verifyInnerClassAndParse(n);
        }
    }

    public void findAPIUseFragment(ClassOrInterfaceDeclaration n) {
        // get class variable name and content
        for(FieldDeclaration field : n.getFields()) {
            //System.out.println(field.getVariables());
            for(VariableDeclarator variable : field.getVariables()) {
                // System.out.println(variable.getName() + " : " + variable.getInitializer().get());
                log.info("Variable Name :{}", variable.getName());
                String api[] = URI.replaceAll("[\\pP\\p{Punct}]"," ").split(" ");

                // 避免抓到空值
                if(variable.getInitializer().orElse(null) != null){
                    String fragmentUri[] = variable.getInitializer().get().toString().replaceAll("[\\pP\\p{Punct}]"," ").split(" ");

                    if(precisionCompareCode(api, fragmentUri)) {
                        String variableName = variable.getName().toString();
                        log.info("Representative Uri :{}", variableName);
                        for(MethodDeclaration method : n.getMethods()) {
                            //System.out.println("Name :" + method.getName());
                            //System.out.println("Body :" + method.getBody().get());
                            log.info("Search Representative Method Name :{}", method.getName());
                            String vName[] = variableName.replaceAll("[\\pP\\p{Punct}]"," ").split(" ");
                            String methodBody[] = method.getBody().get().toString().replaceAll("[\\pP\\p{Punct}]"," ").split(" ");
                            // 比對 variable api
                            if(compareCode(vName, methodBody)) {
                                // codeFragment.setFragment(method.getName().toString());
                                methodResult.add(method.getName().toString());
                            }
                        }
                    }
                }
            }
        }

        // get class methods
        for(MethodDeclaration method : n.getMethods()) {
            //System.out.println("Name :" + method.getName());
            //System.out.println("Body :" + method.getBody().get());
            log.info("Method Name :{}", method.getName());
            String api[] = URI.replaceAll("[\\pP\\p{Punct}]"," ").split(" ");
            String methodBody[] = method.getBody().get().toString().replaceAll("[\\pP\\p{Punct}]"," ").split(" ");
            if(precisionCompareCode(api, methodBody)) {
                // codeFragment.setFragment(method.getName().toString());
                methodResult.add(method.getName().toString());
            }
        }
    }

    // 檢查 inner class 遞迴
    public void verifyInnerClassAndParse(ClassOrInterfaceDeclaration innerClass) {
        for(BodyDeclaration member : innerClass.getMembers()) {
            if(member.isClassOrInterfaceDeclaration()) {
                log.info("inner class name :{}", member.asClassOrInterfaceDeclaration().getName());
                // 再次尋找 api fragment
                findAPIUseFragment(member.asClassOrInterfaceDeclaration());
                // 遞迴檢查 inner class
                verifyInnerClassAndParse(member.asClassOrInterfaceDeclaration());
            }
        }
    }

    // 當 variables 變數是名稱時 比對
    public boolean compareCode(String[] api, String[] fragmentUri){
        for(String key : api){
            boolean flag = false;
            for(String key1 : fragmentUri){
                if(key.equals(key1)){
                    //System.out.println(key);
                    flag = true;
                    break;
                }
            }
            if(!flag){
                log.info("Miss on:{}", key);
                // 避免 api 本身是用 http , 我們檢查是用 https
                if(key.equals("https")) {
                    for(String key1 : fragmentUri){
                        if(key.equals("http")){
                            break;
                        }
                    }
                }else if(key.equals("http")) {
                    for(String key1 : fragmentUri){
                        if(key.equals("https")){
                            break;
                        }
                    }
                }else {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean precisionCompareCode(String[] api, String[] fragmentUri) {
        for(int i = 0;i < fragmentUri.length;i++) {
            // 考慮 API 不一定用 http or https
            if(fragmentUri[i].equals("http") || fragmentUri[i].equals("https")) {
                int offset = i;
                for(int j = 1;j < api.length;j++) {
                    int count = 0;
                    while(++offset < fragmentUri.length) {
                        if(count == 2) break;
                        if(!fragmentUri[offset].equals(api[j])) {
                            count++;
                        }else if(fragmentUri[offset].equals(api[j])) {
                            // 比到最後一個 api token 皆相等 回傳 true
                            if(j == api.length-1) {
                                return true;
                            }else {
                                break;
                            }
                        }
                    }
                    if(count == 2) {
                        log.info("No Same");
                        break;
                    }
                }
            }
        }
        return false;
    }

//    private static class ClassVisitor extends VoidVisitorAdapter<Void> {
//        @Override
//        public void visit(ClassOrInterfaceDeclaration n, Void arg) {
//        /* here you can access the attributes of the method.
//         this method will be called for all methods in this
//         CompilationUnit, including inner class methods */
//            System.out.println(n.getFields());
//            System.out.println("-----------------------------------------------------");
//
//            // get class variable name and content
//            for(FieldDeclaration field : n.getFields()) {
//                System.out.println(field.getVariables());
//                for(VariableDeclarator variable : field.getVariables()) {
//                    System.out.println(variable.getName() + " : " + variable.getInitializer().get());
//                }
//            }
//
//            // get class methods
//            for(MethodDeclaration method : n.getMethods()) {
//                System.out.println("Name :" + method.getName());
//                System.out.println("Body :" + method.getBody().get());
//            }
//
//            super.visit(n, arg);
//        }
//    }
}