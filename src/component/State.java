/**
 * model이 가질 수 있는 state
 * 
 * <Attribute<
 * 1. transition: State가 모여 transition을 구성한다.
 * 2. stateName
 */

package component;
public class State {
    private Transition transition;
    private String stateName;

    public State(Transition transition, String stateName) {
        setTransition(transition);
        setStateName(stateName);
    }

    public Transition getTransition() {
        return this.transition;
    }
    
    public void setTransition(Transition transition) {
        this.transition = transition;
    }

    public String getStateName() {
        return this.stateName;
    }
    
    public void setStateName(String stateName) {
        this.stateName = stateName;
    }
}