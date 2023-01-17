/**
 * 전이 관계를 나타낸다.
 * inputText 형식: [src state, condition, dst state]
 * 
 * <Attribute> 
 * 1. assingBlock: transition이 모여 AssignBlock을 구성함.
 * 2. condition: input으로 들어오는 condition
 * 3. source: input으로 들어오는 src state
 * 4. target: input으로 들어오는 dst state
 * 5. root: condition을 parsing하여 Predicate로 구성된 parse tree를 만들고, attribute로 parse tree의 root를 가짐.
 */

package component;

import java.util.HashMap;
import java.util.LinkedHashSet;

import block.AssignBlock;
import module.Module;

public class Transition {
    private AssignBlock assignBlock;
    private String condition;
    private State source;
    private State target;
    private Predicate root;

    public Transition(AssignBlock assignBlock, String inputText, HashMap<String, String> enumerations) {
        setAssignBlock(assignBlock);
        setSource(inputText);
        setTarget(inputText);
        setCondition(inputText);
        setRoot(enumerations);
    }
    
    public AssignBlock getAssignBlock() {
        return this.assignBlock;
    }

    public void setAssignBlock(AssignBlock assignBlock) {
        this.assignBlock = assignBlock;
    }

    public String getCondition() {
        return this.condition;
    }

    /**
     * input text로부터 condition 추출하는 함수
     */
    public void setCondition(String inputText) { 
        String parsingString = inputText.split(",")[1];
        String condition = parsingString.split(":")[0];

        condition = condition.replaceAll("\\'", "");
        condition = condition.replaceAll(" ", "");

        this.condition = condition;
    }

    public State getSource() {
        return this.source;
    }

    /**
     * input text로 부터 source state 추출하는 함수
     */
    public void setSource(String inputText) { 
        inputText = inputText.split(",")[0];
        String stateName = inputText.replaceAll("[\\[ ]", "");
        Module module = assignBlock.getModule();
        LinkedHashSet<State> stateSet = module.getStateSet();
        boolean inStateSet = false;

        //Module의 stateSet을 탐색해 이미 가지고 있는 state 객체라면, 그것을 attribute로 가지도록 함.
        for(State state: stateSet){
            if(state.getStateName().equals(stateName)){
                this.source = state;
                inStateSet = true;
                break;
            }
        }

        //이전에 만들어지지 않은 state라면 새로 생성해주고, Module의 stateSet에 추가해준다.
        if(inStateSet == false){
            this.source = new State(this, stateName);
            module.setStateSet(this.source);
        }
    }

    public State getTarget() {
        return this.target;
    }

    /**
     * input text로 부터 target state 추출하는 함수
     */
    public void setTarget(String inputText) {
        inputText = inputText.split(",")[2];
        String stateName = inputText.replaceAll("[\\] ]", "");
        Module module = assignBlock.getModule();
        LinkedHashSet<State> stateSet = module.getStateSet();
        boolean inStateSet = false;

        //Module의 stateSet을 탐색해 이미 가지고 있는 state 객체라면, 그것을 attribute로 가지도록 함.
        for(State state: stateSet){
            if(state.getStateName().equals(stateName)){
                this.target = state;
                inStateSet = true;
                break;
            }
        }

        //이전에 만들어지지 않은 state라면 새로 생성해주고, Module의 stateSet에 추가해준다.
        if(inStateSet == false){
            this.target = new State(this, stateName);
            module.setStateSet(this.target);
        }
    }

    /**
     * condition을 parsing하여 predicate로 구성된 parse tree를 만드는 함수
     * 우선 순위: 논리연산자 > 관계연산자 > 숫자, 변수 > '(', ')', '!'
     * 
     * @param parsingString parsing할 string
     * @param enumerations 입력받은 열거형 타입의 정보 <- predicate에서 variable을 생성할 때 필요함.
     * @return 생성된 부모 Predicate <- 최종적으로 root가 반환이 됨.
     */
    public Predicate parse(String parsingString, HashMap<String, String> enumerations){
        Predicate root = null;
        String[] logicalOper = {"and", "or"};
        String[] relationalOper = {"!=", "<=", ">=", "==" ,"<", ">"};
        int findIndex = -1;

        if(parsingString.equals("")) return null;

        //논리연산자를 추출한다. -> 논리연산자 있으면, 논리연산자를 parent로 하여 재귀로 child를 구해나감.
        for(String s: logicalOper){ 
            findIndex = parsingString.indexOf(s);    
            if(findIndex != -1){
                if(s.equals("and"))
                    root = new Predicate(this, "&", parse(parsingString.substring(0, findIndex), enumerations), parse(parsingString.substring(findIndex + s.length()), enumerations), enumerations);
                else
                    root = new Predicate(this, "|", parse(parsingString.substring(0, findIndex), enumerations), parse(parsingString.substring(findIndex + s.length()), enumerations), enumerations);
                break;
            }
        }

        //논리연산자 없으면, 관계연산자를 추출한다. -> 관계연산자 있으면, 관계연산자를 parent로 하여 재귀로 child를 구해나감.
        if(findIndex == -1){ 
            for(String s: relationalOper){
                findIndex = parsingString.indexOf(s);
                if(findIndex != -1){
                    if(s.equals("=="))
                        root = new Predicate(this, "=", parse(parsingString.substring(0, findIndex), enumerations), parse(parsingString.substring(findIndex + s.length()), enumerations), enumerations);
                    else
                        root = new Predicate(this, s, parse(parsingString.substring(0, findIndex), enumerations), parse(parsingString.substring(findIndex + s.length()), enumerations), enumerations);

                    break;
                }
            }
        }

        //관계연산자 없으면 남은 건 숫자, 변수, '(', '!', ')'로 이루어진 문자열이다. 
        if(findIndex == -1){
            String s = parsingString.replaceAll("[^0-9-]", ""); 

            if(!s.equals("")){  //숫자인 경우
                findIndex = parsingString.indexOf(s);
                root = new Predicate(this, s, parse(parsingString.substring(0, findIndex), enumerations), parse(parsingString.substring(findIndex + s.length()), enumerations), enumerations);
            }
            else{
                s = parsingString.replaceAll("[\\(\\)!]", "");

                if(!s.equals("")){  //변수인 경우
                    findIndex = parsingString.indexOf(s);
                    root = new Predicate(this, s, parse(parsingString.substring(0, findIndex), enumerations), parse(parsingString.substring(findIndex + s.length()), enumerations), enumerations);
                }
                else{   //'(', '!', ')'로 이루어진 문자열인 경우
                    findIndex = parsingString.length()/2;
                    root = new Predicate(this, parsingString.substring(findIndex, findIndex + 1), parse(parsingString.substring(0, findIndex), enumerations), parse(parsingString.substring(findIndex + 1), enumerations), enumerations);
                }
            }
        }

        return root;
    }
  
    public Predicate getRoot() {
        return this.root;
    }

    public void setRoot(HashMap<String, String> enumerations) {
        Predicate root = parse(condition, enumerations);
        this.root = root;
    }

    /**
     * @param node parse tree의 node
     * @return node로부터 inorder로 읽은 string의 모음
     */
    public String readInorder(Predicate node){ 
        String inorderStr = "";

        if(node != null){
            inorderStr += readInorder(node.getLeftChild());
            inorderStr += node.getProposition();
            inorderStr += readInorder(node.getRightChild());
        }

        return inorderStr;
    }
}