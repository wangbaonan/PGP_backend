package pog.pgp_alpha_v1.model.message;

import lombok.Data;

import java.util.ArrayList;

@Data
public class AnalysisProgressInfo {
    private int analysisStatus;
    private long analysisId;
    private boolean isAllCompleted;
    private ArrayList<String> completedModules;
}
