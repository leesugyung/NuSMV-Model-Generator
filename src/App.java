/* NuSMV code generate
 * java vesion: openjdk version "11.0.2" 2019-01-15
 */
import modelGenerator.ModelGenerator;

public class App {
   public static void main(String[] args) throws Exception {  
        String path = "";
        String enumPath = "";
        String moduleName = "main";

        for(int i = 0; i < args.length; i+=2){
            if(args[i].equals("-filePath")){
                path = args[i+1];
            }
            else if(args[i].equals("-enumFilePath")){
                enumPath = args[i+1];
            }
            else if(args[i].equals("-moduleName")){
                moduleName = args[i+1];
            }
        }
        
        if(path.equals("")){
            System.out.println("usage: -filePath <file> [-enumFilePath <file>] [-moduleName <string>]");
        }
        else{
            new ModelGenerator(moduleName, path, enumPath);
        }
    }
}