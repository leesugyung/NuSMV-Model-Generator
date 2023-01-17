/**
 * transition을 이루는 요소(logical operator, relational operator, variable, constant, (, ), !)
 * 
 * <Attribute>
 * 1. transition: Predicate가 모여 transition을 구성한다.
 * 2. proposition: Predicate의 내용
 * 3. leftChild
 * 4. rightChild
 * 5. variable: proposition이 variable이면 variable을 생성하고, variable이 아니라면 null을 가진다.
 */

package component;

import java.util.HashMap;
import java.util.LinkedHashSet;

import block.AssignBlock;
import module.Module;

public class Predicate {
    private Transition transition;
    private String proposition;
    private Predicate leftChild;
    private Predicate rightChild;
    private Variable variable;

    public Predicate(Transition transition, String proposition, Predicate leftChild, Predicate rightChild, HashMap<String, String> enumerations) {
        setTransition(transition);
        setProposition(proposition);
        setLeftChild(leftChild);
        setRightChild(rightChild);
        setVariable(enumerations);        
    }

    public Transition getTransition() {
        return this.transition;
    }

    public void setTransition(Transition transition) {
        this.transition = transition;
    }

    public String getProposition() {
        return this.proposition;
    }

    public void setProposition(String proposition) {
        this.proposition = proposition;
    }

    public Predicate getLeftChild() {
        return this.leftChild;
    }

    public void setLeftChild(Predicate leftChild) {
        this.leftChild = leftChild;
    }

    public Predicate getRightChild() {
        return this.rightChild;
    }

    public void setRightChild(Predicate rightChild) {
        this.rightChild = rightChild;
    }

    public Variable getVariable() {
        return this.variable;
    }

    /**
     * 해당 Predicate의 propostion이 변수라면, Variable을 생성하고 attribute로 가지도록 한다.
     * 변수가 아니라면, null값을 가지도록 한다.
     * 
     * @param enumerations enumeration type 변수에 대한 정보가 담긴 hashmap, <형태: key = enumeration type 변수의 이름, value = {enumeration type 변수의 value들}>
     */
    public void setVariable(HashMap<String, String> enumerations){
        String variableName = proposition.replaceAll("[^a-zA-Z_.]","");
        AssignBlock assignBlock = transition.getAssignBlock();
        Module module = assignBlock.getModule();
        LinkedHashSet<Variable> variableSet = module.getVariableSet();
        boolean inVariableSet = false;

        if(!variableName.equals("")){   
            //이미 가지고 있는 variable이라면, 새로 생성한지 않고 이전에 만들어놓은 객체를 가져와 attribute로 가지도록 한다.
            for(Variable variable: variableSet){
                if(variable.getVariableName().equals(variableName)){
                    this.variable = variable;
                    inVariableSet = true;
                    break;
                }
            }
            /*
             * 가지고 있지 않은 variable이라면, 새로 생성하고 variableSet에 add해준다.
             * 이때, enumeration type 변수라면, enumeration type 변수의 value들을 scope로 setting해준다.
             * enumeration type 변수가 아닌 모든 변수들은 "no scope"로 setting한다. <- 이후 module에서 scope에 대한 setting을 해준다. 
             */
            if(inVariableSet == false){
                if(enumerations.containsKey(variableName) == true)
                    this.variable = new Variable(this, proposition, enumerations.get(variableName));
                else
                    this.variable = new Variable(this, proposition, "no scope");
                
                module.setVariableSet(this.variable);
            }
        }
        else
            this.variable = null;
    }

    /**
     * enumeration type 변수의 value가 숫자라면, parse tree가 생성될 때 해당 predicate에서 variable로 생성되지 않고 null로 setting이 되었을 것이다.
     * module에서 scope를 setting할 때 위의 경우에 대해서 variable을 생성해줄 때 필요한 함수
     */
    public void setVariable(String variableName, String scope){
        AssignBlock assignBlock = transition.getAssignBlock();
        Module module = assignBlock.getModule();
        LinkedHashSet<Variable> variableSet = module.getVariableSet();
        boolean inVariableSet = false;

        for(Variable variable: variableSet){
            if(variable.getVariableName().equals(variableName)){
                this.variable = variable;
                inVariableSet = true;
                break;
            }
        }

        if(inVariableSet == false){
            this.variable = new Variable(this, proposition, scope);
            module.setVariableSet(this.variable);
        }
    }
}