package pog.pgp_alpha_v1.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class JsonFileUtils {

    // 创建ObjectMapper实例
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Object readJsonFromFile(String filePath) {
        // 读取JSON文件并转换为JSON对象
        File jsonFile = new File(filePath);
        Object json = null;
        try {
            json = objectMapper.readValue(jsonFile, Object.class);
        } catch (IOException e) {
            e.printStackTrace();
            // 你应当在这里添加你的错误处理逻辑，例如抛出一个异常或者返回null
            return null;
        }
        // 返回解析后的JSON对象
        return json;
    }
}