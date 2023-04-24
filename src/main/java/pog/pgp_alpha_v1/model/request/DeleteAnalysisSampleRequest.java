package pog.pgp_alpha_v1.model.request;

import lombok.Data;

@Data
public class DeleteAnalysisSampleRequest {
    private Long analysisId;
    private Long[] dataIds;
}
