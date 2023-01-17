/**
 * module code generator
 * 
 * <Attribute>
 * 1. module: setting이 된 module을 가지고 NuSMV 코드를 생성함.
 */

package modelGenerator;
import java.io.IOException;
import java.io.PrintWriter;

import block.AssignBlock;
import block.InitBlock;
import block.VarDeclareBlock;
import component.Transition;
import module.Module;
import statement.DeclStatement;
import statement.InitStatement;

public class ModelGenerator {
    private Module module;

    public ModelGenerator(String moduleName, String filePath, String enumFilePath) throws IOException {
        setModule(moduleName, filePath, enumFilePath);
        generatorModel();
    }

    public Module getModule() {
        return this.module;
    }
    
    public void setModule(String moduleName, String filePath, String enumFilePath) throws IOException {
        module = new Module(this,moduleName, filePath, enumFilePath);
    }

    //setting이 완료된 객체들을 사용해 NuSMV 코드를 생성한다.
    public void generatorModel() throws IOException{
        PrintWriter pw = new PrintWriter("output.smv");

        AssignBlock assignBlock = module.getAssignBlock();
        InitBlock initBlock = module.getInitBlock();
        VarDeclareBlock varDeclareBlock = module.getVarDeclareBlock();
        
        pw.println("MODULE "+ module.getModuleName());
        pw.println("VAR");

        for(DeclStatement declStatement: varDeclareBlock.getDeclStatementList()){
            pw.println("\t"+declStatement.getVarName()+": "+declStatement.getTypeSpecifier()+";");
        }

        pw.println("ASSIGN");
        for(InitStatement initStatement: initBlock.getInitStatementList()){
            pw.println("\tinit("+initStatement.getIdentifier()+"):= "+initStatement.getInitialValue()+";");
        }
        pw.println();

        pw.println("\tnext(state):= case");
        for(Transition transition: assignBlock.getTransitionList()){
            pw.println("\t\t"+"state = "+ transition.getSource().getStateName()+ " & "+ transition.readInorder(transition.getRoot())+" : "+transition.getTarget().getStateName()+";");
        }
        pw.println("\t\tTRUE : state;");
        pw.println("\tesac;");

        pw.close();
    }
}