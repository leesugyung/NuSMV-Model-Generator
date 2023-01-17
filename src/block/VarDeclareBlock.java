/**
 * NuSMV의 Variable Declarations Block
 * 
 * <Attriabute>
 * 1. module: VarDeclareBlock, InitBlock, AssignBlock이 모여 module을 구성함.
 * 2. declStatementList: declStatement 모여 VarDeclareBlock을 구성함.
 */

package block;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import component.State;
import component.Variable;
import statement.DeclStatement;
import module.Module;

public class VarDeclareBlock {
    private Module module;
    private List<DeclStatement> declStatementList;
    
    public VarDeclareBlock(Module module, LinkedHashSet<State> stateSet, LinkedHashSet<Variable> variableSet) {
        setModule(module);
        setDeclStatementList(stateSet, variableSet);
    }

    public Module getModule() {
        return this.module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public List<DeclStatement> getDeclStatementList() {
        return this.declStatementList;
    }

    /**
     * state와 variable에 대한 declStatement를 생성한다.
     * 이때, variable은 boolean, interger가 있을 수 있다.
     * 
     * @param stateSet    model이 가지는 state들의 집합  
     * @param variableSet model이 가지는 variable들의 집합 
     */
    public void setDeclStatementList(LinkedHashSet<State> stateSet, LinkedHashSet<Variable> variableSet) {
        declStatementList = new ArrayList<>();
        DeclStatement declStatement = null;
        String typeSpecifier = new String();
        int count = 0;

        //state에 대한 DeclStatement -> stateSet을 읽어 필요한 stateName 정보를 모은다.
        for(State state: stateSet){
            if(count==stateSet.size()-1)
                typeSpecifier += state.getStateName();
            else
                typeSpecifier += state.getStateName()+", ";
            count += 1;
        }
        typeSpecifier = "{"+typeSpecifier+"}";
        declStatement = new DeclStatement(this, "state", typeSpecifier);
        declStatementList.add(declStatement);

        //variable에 대한 DeclStatement
        for(Variable v: variableSet){
            if(v.getScope().equals("enumeration value"))
                continue;

            declStatement = new DeclStatement(this, v.getVariableName(), v.getScope());
            declStatementList.add(declStatement);
        }
    }
}