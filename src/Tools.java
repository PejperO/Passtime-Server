import org.yaml.snakeyaml.Yaml;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tools {
    public static Options createOptionsFromYaml(String fileName) throws Exception{
        Yaml yaml = new Yaml();
        BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
        String line;
        StringBuilder stringBuilder = new StringBuilder();
        while((line=bufferedReader.readLine())!=null){
            stringBuilder.append(line).append("\n");
        }
        HashMap<String, String> yamlMap = yaml.load(stringBuilder.toString());

        Object port = yamlMap.get("port");
        Object concurMode = yamlMap.get("concurMode");
        Object showSendRes = yamlMap.get("showSendRes");
        Object clientsMap = yamlMap.get("clientsMap");
        String host = yamlMap.get("host");

        return new Options(host, (int)(port), (boolean)(concurMode), (boolean)(showSendRes), (Map<String, List<String>>)(clientsMap));
    }
}
