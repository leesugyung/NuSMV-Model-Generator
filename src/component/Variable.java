/**
 * model이 가질 수 있는 Variable
 * 
 * <Attribute>
 * 1. predicate: predicate에서 Variable을 추출한다.
 * 2. variableName
 * 3. scope
 */

package component;
public class Variable {
    private Predicate predicate;
    private String variableName;
    private String scope;

    public Variable(Predicate predicate, String variableName, String scope) {
        setPredicate(predicate);
        setVariableName(variableName);
        setScope(scope);
    }

    public Predicate getPredicate() {
        return this.predicate;
    }

    public void setPredicate(Predicate predicate) {
        this.predicate = predicate;
    }

    public String getVariableName() {
        return this.variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    public String getScope() {
        return this.scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}