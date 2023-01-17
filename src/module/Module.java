/**
 * NuSMV의 module
 * 
 * <Attribute>
 * 1. modelGenerator
 * 2. moduleName
 * 3. varDeclareBlock
 * 4. initBlock
 * 5. assignBlock
 * 6. stateSet: model이 가지고 있는 state의 set
 * 7. variableSet: model이 가지고 있는 state의 set
 */

package module;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Stack;

import block.AssignBlock;
import block.InitBlock;
import block.VarDeclareBlock;
import component.State;
import component.Transition;
import component.Variable;
import component.Predicate;

import modelGenerator.ModelGenerator;

public class Module {
    private ModelGenerator modelGenerator;
    private String moduleName;
    private VarDeclareBlock varDeclareBlock;
    private InitBlock initBlock;
    private AssignBlock assignBlock;
    private LinkedHashSet<State> stateSet;
    private LinkedHashSet<Variable> variableSet; 

    public Module(ModelGenerator modelGenerator, String moduleName, String filePath, String enumFilePath) throws IOException {
        this.modelGenerator = modelGenerator;
        setModuleName(moduleName);
        setStateSet();
        setVariableSet();
        setAssignBlock(filePath, enumFilePath);
        setVariableScope();
        setInitBlock();
        setVarDeclareBlock();
    }
    
    public ModelGenerator getModelGenerator() {
        return this.modelGenerator;
    }
    
    public void setModelGenerator(ModelGenerator modelGenerator) {
        this.modelGenerator = modelGenerator;
    }
    
    public String getModuleName() {
        return this.moduleName;
    }
    
    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }
    
    public VarDeclareBlock getVarDeclareBlock() {
        return this.varDeclareBlock;
    }
    
    public void setVarDeclareBlock() {
        VarDeclareBlock varDeclareBlock = new VarDeclareBlock(this, stateSet, variableSet);

        this.varDeclareBlock = varDeclareBlock;
    }

    public InitBlock getInitBlock() {
        return this.initBlock;
    }
    
    public void setInitBlock() {
        InitBlock initBlock = new InitBlock(this, stateSet, variableSet);

        this.initBlock = initBlock;
    }
    
    public AssignBlock getAssignBlock() {
        return this.assignBlock;
    }
    
    public void setAssignBlock(String filePath, String enumFilePath) throws IOException {
        AssignBlock assignBlock = new AssignBlock(this, filePath, enumFilePath);
        this.assignBlock = assignBlock;
    }

    public LinkedHashSet<State> getStateSet() {
        return this.stateSet;
    }
    
    public void setStateSet() {
        stateSet = new LinkedHashSet<>();
    }

    public void setStateSet(State state){
        stateSet.add(state);
    }
    
    public LinkedHashSet<Variable> getVariableSet() {
        return this.variableSet;
    }
    
    public void setVariableSet() {
        variableSet = new LinkedHashSet<>(); 
    }

    public void setVariableSet(Variable variable){
        variableSet.add(variable);
    }
    
    /**
     * Variable의 scope 처리 -> Prase tree를 root부터 타고 내려가면서 관계 연산자로 비교가 이루어지는 부분을 이용해 범위를 결정해준다. 
     * 1) node의 proposition이 관계 연산자일 때:
     *      i) child가 enumeration type이면
     *      ii) child가 둘 다 variable이라면
     *      iii) child가 variabe, integer라면
     * 2) node가 관계 연산자없이 variable이 나왔을 때: boolean
     * 3) node가 관계 연산자, variable이 아닐 때: child를 stack에 넘어놓고 다음 node로 넘어가도록 함.
     */
    public void setVariableScope(){
        ArrayList<Predicate> comparisonVariable = new ArrayList<>();
        
        for(Transition temp: assignBlock.getTransitionList()){
            Predicate root = temp.getRoot();

            Stack<Predicate> stack = new Stack<>();
            stack.push(root);

            while(!stack.isEmpty()){
                Predicate node = stack.pop();
                String proposition = node.getProposition();

                if(proposition.contains("<") | proposition.contains("=") | proposition.contains(">")){  //Parse tree에서 관계 연산자 발견 -> leftChild와 rightChild는 변수나 숫자일 것이다.
                    Predicate leftChild = node.getLeftChild();
                    Predicate rightChild = node.getRightChild();

                    if(leftChild.getVariable() != null){    //leftChild가 변수일 때 그 변수가 enumeration type 변수라면, 그에 대한 처리를 해줌. (rightChild는 value일 것임. 따라서, "enumeration value"로 범위를 지정해줌.)
                        if(leftChild.getVariable().getScope().contains("{")){ 
                            if(rightChild.getVariable() != null)
                                rightChild.getVariable().setScope("enumeration value");
                            else 
                                rightChild.setVariable(rightChild.getProposition(), "enumeration value");
                            continue;
                        }
                    }

                    if(rightChild.getVariable() != null){   //rightChild가 변수일 때 그 변수가 enumeration type 변수라면, 그에 대한 처리를 해줌. (lefthild는 value일 것임. 따라서, "enumeration value"로 범위를 지정해줌.)
                        if(rightChild.getVariable().getScope().contains("{")){
                            if(leftChild.getVariable() != null)
                                leftChild.getVariable().setScope("enumeration value");
                            else
                                rightChild.setVariable(rightChild.getProposition(), "enumeration value");
                            continue;
                        }
                    }

                    if(leftChild.getVariable() != null & rightChild.getVariable() != null){ //둘 다 변수라면, 숫자와 변수와의 관계로 변수의 scope이 어느정도 정해지고 처리하기 위해 따로 빼줌.
                        comparisonVariable.add(node);
                    }

                    else{   //숫자와 변수와의 관계 처리
                        Variable variable;
                        int num;

                        if(leftChild.getVariable() != null){    //leftChild가 변수, rightChild가 숫자 
                            variable = leftChild.getVariable();
                            num = Integer.parseInt(rightChild.getProposition());
                        }

                        else{   //rightChild가 변수, leftChild가 숫자 
                            variable = rightChild.getVariable();
                            num = Integer.parseInt(leftChild.getProposition());
                        }
    
                        //아직 scope이 전혀 정해지지 않은 상황이라면,
                        if(variable.getScope().equals("no scope")){
                            variable.setScope(Integer.toString(num-1)+".."+Integer.toString(num+1));
                        }

                        //scope이 정해져있는 상황이라면, num값과 이전의 scope값을 비교해 scope를 결정한다.
                        else{
                            int min = Integer.parseInt(variable.getScope().split("\\.\\.")[0]);
                            int max = Integer.parseInt(variable.getScope().split("\\.\\.")[1]);

                            min = (num-1 < min) ? num-1 : min;
                            max = (num+1 > max) ? num+1 : max;

                            variable.setScope(Integer.toString(min)+".."+Integer.toString(max));
                        }
                    }
                }

                //관계연산자 없이 변수가 나왔다면, boolean type의 변수이다.
                else if(node.getVariable()!=null){
                    node.getVariable().setScope("boolean");
                }

                //관계연산자가 나올 때까지 parse tree를 타고 내려간다.
                else{
                    if(node.getRightChild() != null) stack.push(node.getRightChild());
                    if(node.getLeftChild() != null) stack.push(node.getLeftChild());
                }
            }
        }

        //변수와 변수간의 비교 처리
        for(Predicate node: comparisonVariable){
            Variable leftVariable = node.getLeftChild().getVariable();
            Variable rightVariable = node.getRightChild().getVariable();

            //한 변수가 범위가 전혀 정해지지 않은 상태라면, 다른 변수의 범위를 가지도록 해준다.
            if(leftVariable.getScope().equals("no scope")){
                leftVariable.setScope(rightVariable.getScope());
            }
            else if(rightVariable.getScope().equals("no scope")){
                rightVariable.setScope(leftVariable.getScope());
            }
            //두 변수 모두 범위가 정해져있는 상태라면, 두 변수의 범위에서 더 넒은 범위가 세팅이 될 수 있도록 해준다.
            else{
                int leftVariableMin = Integer.parseInt(leftVariable.getScope().split("\\.\\.")[0]);
                int leftVariableMax = Integer.parseInt(leftVariable.getScope().split("\\.\\.")[1]);
                int rightVariableMin = Integer.parseInt(rightVariable.getScope().split("\\.\\.")[0]);
                int rightVariableMax = Integer.parseInt(rightVariable.getScope().split("\\.\\.")[1]);

                int min = (leftVariableMin < rightVariableMin) ? leftVariableMin : rightVariableMin;
                int max = (leftVariableMax > rightVariableMax) ? leftVariableMax : rightVariableMax;

                leftVariable.setScope(Integer.toString(min)+".."+Integer.toString(max));
                rightVariable.setScope(Integer.toString(min)+".."+Integer.toString(max));
            }
        }
    }
}