/**
 * NuSMV의 initialize statement
 * INIT(identifier) := initialValue
 * 
 * <Attriabute>
 * 1. initBlock: InitStatement가 모여서 initBlock을 구성함.
 * 2. identifier: 초기화할 식별자
 * 3. initialValue: 초기값
 */

package statement;

import block.InitBlock;

public class InitStatement {
    private InitBlock initBlock;
    private String identifier;
    private String initialValue;

    public InitStatement(InitBlock initBlock, String identifier, String initialValue) {
        setInitBlock(initBlock);
        setIdentifier(identifier);
        setInitialValue(initialValue);
    }

    public InitBlock getInitBlock() {
        return this.initBlock;
    }

    public void setInitBlock(InitBlock initBlock) {
        this.initBlock = initBlock;
    }

    
    public String getIdentifier() {
        return this.identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getInitialValue() {
        return this.initialValue;
    }

    public void setInitialValue(String initialValue) {
        this.initialValue = initialValue;
    }
}