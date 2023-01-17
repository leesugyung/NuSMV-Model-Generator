/**
 * NuSMV의 Variable Declaration statement
 * state, boolean, enumeration type, integer가 올 수 있음.
 * 
 * <Attriabute>
 * 1. VarDeclareBlock: DeclStatement가 모여서 VarDeclareBlock을 구성함.
 * 2. VarName: NuSMV에서 사용할 variable 이름
 * 3. typeSpecifier: NuSMV에서 사용할 variable의 형식
 */

package statement;

import block.VarDeclareBlock;

public class DeclStatement {
    private VarDeclareBlock varDeclareBlock;
    private String VarName;
    private String typeSpecifier;

    public DeclStatement(VarDeclareBlock varDeclareBlock, String VarName, String typeSpecifier) {
        setVarDeclareBlock(varDeclareBlock);
        setVarName(VarName);
        setTypeSpecifier(typeSpecifier);
    }
    
    public VarDeclareBlock getVarDeclareBlock() {
        return this.varDeclareBlock;
    }

    public void setVarDeclareBlock(VarDeclareBlock varDeclareBlock) {
        this.varDeclareBlock = varDeclareBlock;
    }
    
    public String getVarName() {
        return this.VarName;
    }

    public void setVarName(String VarName) {
        this.VarName = VarName;
    }

    public String getTypeSpecifier() {
        return this.typeSpecifier;
    }

    public void setTypeSpecifier(String typeSpecifier) {
        this.typeSpecifier = typeSpecifier;
    }

    public String makeDeclStatement(){
        String stat = VarName + ": " + typeSpecifier;

        return stat;
    }
}