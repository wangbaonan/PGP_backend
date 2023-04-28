package pog.pgp_alpha_v1.service;

import pog.pgp_alpha_v1.model.request.AnalysisConfigRequest;

public interface ConfigService {
    void updateConfig(AnalysisConfigRequest request, Long analysisId);
}
