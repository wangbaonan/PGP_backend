package pog.pgp_alpha_v1.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtils {

    /**
     * 创建硬链接
     *
     * @param sourceFilePath 源文件路径
     * @param targetDirPath 目标文件路径（硬链接）
     * @throws IOException 文件操作异常
     */
    public static void createHardLink(String sourceFilePath, String targetDirPath) throws IOException {
        Path sourcePath = Paths.get(sourceFilePath);
        String sourceFileName = sourcePath.getFileName().toString();
        Path targetPath = Paths.get(targetDirPath, sourceFileName);

        // 确保源文件存在
        if (!Files.exists(sourcePath)) {
            throw new IOException("源文件不存在: " + sourceFilePath);
        }

        // 检查目标文件是否存在
        if (Files.exists(targetPath)) {
            // 跳过或覆盖，这里是跳过
            System.out.println("目标文件已存在。跳过...");
        } else {
            // 创建硬链接
            Files.createLink(targetPath, sourcePath);
        }
    }


    public static void createDirectoriesRecursively(String directory) {
        try {
            Path path = Paths.get(directory);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
                System.out.println("目录已创建: " + path.toAbsolutePath());
            } else {
                System.out.println("目录已存在: " + path.toAbsolutePath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
