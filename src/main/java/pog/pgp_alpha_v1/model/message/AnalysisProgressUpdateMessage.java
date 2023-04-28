package pog.pgp_alpha_v1.model.message;

import lombok.Data;

@Data
public class AnalysisProgressUpdateMessage {
    private Long userId;
    private Long analysisId;
    private String progress;
}

