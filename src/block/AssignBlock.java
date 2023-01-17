/**
 * NuSMV의 next state에 대한 Block(전이 관계를 정의하는 Block)
 * 
 * <Attribute>
 * 1. module: VarDeclareBlock, InitBlock, AssignBlock이 모여 module을 구성함.
 * 2. transitionList: transition이 모여 AssignBlock을 구성함.
 */

package block;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import module.Module;
import component.Transition;

public class AssignBlock {
    private Module module;
    private List<Transition> transitionList;

    public AssignBlock(Module module, String filePath, String enumFilePath) throws IOException {
        setModule(module);
        setTransitionList(filePath, enumFilePath);
    }
    
    public Module getModule() {
        return this.module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public List<Transition> getTransitionList() {
        return this.transitionList;
    }
    
    /**
     * 파일을 읽어 transition들을 setting함.
     * 
     * @param filePath model의 transition 정보들이 담긴 파일 경로
     * @param enumFilePath enumeration type 정보가 담긴 파일 경로 
     * @throws IOException
     */
    public void setTransitionList(String filePath, String enumFilePath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        transitionList = new ArrayList<>();
        String[] inputLines = null;
        HashMap<String, String> enumerations = new HashMap<>();
        
        //transition 정보가 담긴 file을 읽어서 input을 받아오고, '],'로 split한다. (하나의 text로 들어온 transition들을 분리)
        
        String inputText = br.readLine();

        inputLines = inputText.split("\\],");

        br.close();

        /*
         * enumeration type의 정보가 담긴 file을 읽어와 
         * <key = enumeration type 변수의 이름, value = enumeration type 변수의 value들>
         * 형태의 HashMap에 담아서 transition을 생성할 때 전달한다.
         */
        if(!enumFilePath.equals("")){
            br = new BufferedReader(new FileReader(enumFilePath));
    
            while(true){
                inputText = br.readLine();
                if(inputText==null) break;
    
                String[] enumeration = inputText.split(",", 2);
                enumerations.put(enumeration[0], enumeration[1]);
            }
    
            br.close();
        }

        //transition정보와 enumeration type의 정보를 전달하면서 transition을 생성한다.

        for(String line : inputLines){
            if(line.contains(":")){
                Transition transition = new Transition(this, line, enumerations);
                transitionList.add(transition);
            }
        }
    }
}