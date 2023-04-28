package pog.pgp_alpha_v1.constants;

public enum AnalysisModule {
    NGS_SWITCH(1),
    PCA_SWITCH(2),
    AS2_SWITCH(4),
    HLA_SWITCH(8),
    MTY_SWITCH(16),
    ADMIXTURE_SWITCH(32),
    PRS_SWITCH(64),
    SNPEDIA_SWITCH(128),
    SV_SWITCH(256),
    PROVINCE_SWITCH(512),
    ALL_MODULE(1022),
    NO_PROVINCE(510);

    private final int value;

    AnalysisModule(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
