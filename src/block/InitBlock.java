/**
 * NuSMV의 initialize Block
 * 
 * <Attriabute>
 * 1. module: VarDeclareBlock, InitBlock, AssignBlock이 모여 module을 구성함.
 * 2. initStatementList: initStatement가 모여 InitBlock을 구성함.
 */

package block;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator; 

import component.State;
import component.Variable;
import statement.InitStatement;
import module.Module;

public class InitBlock {
    private Module module;
    private List<InitStatement> initStatementList;

    public InitBlock(Module module, LinkedHashSet<State> stateSet, LinkedHashSet<Variable> variableSet) {
        setModule(module);
        setInitStatementList(stateSet, variableSet);
    }
    
    public Module getModule() {
        return this.module;
    }
    
    public void setModule(Module module) {
        this.module = module;
    }

    public List<InitStatement> getInitStatementList() {
        return this.initStatementList;
    }

    /**
     * state와 variable에 대한 initStatement들을 생성한다. 
     * 이때, Variable은 enumeration type, boolean, integer가 있을 수 있다.
     * 
     * @param stateSet    model이 가지는 state들의 집합  
     * @param variableSet model이 가지는 variable들의 집합 
     */
    public void setInitStatementList(LinkedHashSet<State> stateSet, LinkedHashSet<Variable> variableSet) {
        initStatementList = new ArrayList<>();
        Iterator<State> iter = stateSet.iterator();

        //state에 대한 initStatement -> stateSet의 첫 번째 state가 initial state가 된다.
        InitStatement initStatement = new InitStatement(this, "state", iter.next().getStateName());
        initStatementList.add(initStatement);

        //variable에 대한 initStatement
        for(Variable v: variableSet){
            String scope = v.getScope();

            if(scope.equals("enumeration value") | scope.contains("{"))
                continue;

            if(scope.equals("boolean"))
                initStatement = new InitStatement(this, v.getVariableName(), "FALSE");
            else{
                initStatement = new InitStatement(this, v.getVariableName(), v.getScope().split("\\.\\.")[0]);
            }
            initStatementList.add(initStatement);
        }
    }
}