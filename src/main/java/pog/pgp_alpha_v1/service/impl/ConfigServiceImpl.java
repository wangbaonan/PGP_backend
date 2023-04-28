package pog.pgp_alpha_v1.service.impl;

import org.springframework.beans.factory.annotation.Value;
import pog.pgp_alpha_v1.config.AnalysisConfig;
import pog.pgp_alpha_v1.model.request.AnalysisConfigRequest;
import pog.pgp_alpha_v1.service.ConfigService;

import javax.annotation.Resource;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ConfigServiceImpl implements ConfigService {
    @Resource
    private AnalysisConfig analysisConfig;
    @Value("${myapp.analysis.analysisPath}")
    private String analysisPath;

    @Override
    public void updateConfig(AnalysisConfigRequest request, Long analysisId) {
        analysisConfig.setAssemblyVersion(request.getAssemblyVersion());
        analysisConfig.setSvGeneAnnoFlag(request.getSvGeneAnnoFlag());
        analysisConfig.setSvOverlapPer(request.getSvOverlapPer());
        analysisConfig.setThread(request.getThread());
        try {
            Path configPath = Paths.get(analysisPath , analysisId.toString(), "config");
            Files.createDirectories(configPath);
            try (FileWriter fileWriter = new FileWriter(configPath.resolve("config.txt").toFile())) {
                fileWriter.write(analysisConfig.toFormattedString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
