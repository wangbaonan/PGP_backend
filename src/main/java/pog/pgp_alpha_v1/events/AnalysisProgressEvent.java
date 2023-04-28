package pog.pgp_alpha_v1.events;

import org.springframework.context.ApplicationEvent;

public class AnalysisProgressEvent extends ApplicationEvent {
    private final Long userId;
    private final Long analysisId;
    private final String progress;

    public AnalysisProgressEvent(Object source, Long userId, Long analysisId, String progress) {
        super(source);
        this.userId = userId;
        this.analysisId = analysisId;
        this.progress = progress;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getAnalysisId() {
        return analysisId;
    }

    public String getProgress() {
        return progress;
    }
}
