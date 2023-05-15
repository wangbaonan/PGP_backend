package pog.pgp_alpha_v1.model.request;

import lombok.Data;

@Data
public class AnalysisConfigRequest {
    private double svOverlapPer;
    private int svGeneAnnoFlag;
    private int thread;
    private String assemblyVersion;
    private Long analysisId;
    private int moduleSwitchCode;
}
