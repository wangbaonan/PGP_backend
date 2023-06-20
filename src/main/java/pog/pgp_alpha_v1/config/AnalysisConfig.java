package pog.pgp_alpha_v1.config;

import lombok.Data;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Data
@Configuration
@EnableAsync
public class AnalysisConfig {
    private String suffix = "vcf.gz";
    private String suffixGVCF = "g.vcf.gz";
    private int gVCFRegionalCallingSwitch = 0; // 默认是VCF模式
    private String grch38callingsh = "/home/pogadmin/dev_PGP/02_genotypeGVCF/AAGC031921D/160.haplotypecaller.gVCF.mode/AAGC031921D/00_concat/01.gatkRegionalCalling.sh";
    private String assemblyVersion = "GRCh38";
    private String pcash = "/home/pogadmin/PersonalGenome/src/scripts/PCA/000.PCA.sh";
    private String admixturesh = "/home/pogadmin/PersonalGenome/src/scripts/Admixture/00.run.sh";
    private String extractsh = "/home/pogadmin/PersonalGenome/src/scripts/Utils/extractAutosomesInputVCF.sh";
    private String mtysh = "/home/pogadmin/PersonalGenome/src/scripts/MT_Y_Classification/000.runMTY.sh";
    private String as2sh = "/home/pogadmin/PersonalGenome/src/scripts/AS2/000.AS2.sh";
    private String mtyscrdir = "/home/pogadmin/PersonalGenome/src/scripts/MT_Y_Classification/b38inference";
    private String hlash = "/home/pogadmin/PersonalGenome/src/scripts/HLA/000.imputation.sh";
    private String snpediash = "/home/pogadmin/PersonalGenome/src/scripts/SNPedia/01_Medicine_Version/000.SNPedia_annotation.sh";
    private String pgssh = "/home/pogadmin/PersonalGenome/src/scripts/PGS/003_pgsc_calc_version/00.pgsc_calc.sh";
    private String pgsRetrysh = "/home/pogadmin/PersonalGenome/src/scripts/PGS/003_pgsc_calc_version/01.pgsc_calcRetry.sh";
    private String provincesh = "/home/pogadmin/PersonalGenome/src/scripts/Province/Snapshot_V1/000.Province.sh";
    private String pgsdir = "/home/pogadmin/PersonalGenome/src/scripts/PGS/";
    private String pgsrefPanel = "/home/pogadmin/PersonalGenome/RefData/AS2/HGDP_Han.vcf.gz";
    private String pgstxtPath = "/home/pogadmin/PersonalGenome/RefData/PGS/PGS_score_file_less1million_sites/";
    private String pgsRef = "HGDP_Han";
    private String svAnnotationPy = "/home/pogadmin/PersonalGenome/src/scripts/SV/AnnotationComparison/annotation.py";
    private String svAnnotationExtractSh = "/home/pogadmin/PersonalGenome/src/scripts/SV/00.extractSVlink.sh";
    private String svAnnotationRef = "/home/pogadmin/PersonalGenome/src/scripts/SV/pggsv.annotation.ref";
    private String svAnnoDB = "pggsv,dbVar,DGV";
    private double svOverlapPer = 0.5;
    private int svGeneAnnoFlag = 1;
    private int thread = 6;
    private int moduleSwitchCode = 1022;

    public String toFormattedString(){
        return "suffix = " + suffix + "\n" +
                "suffixGVCF = " + suffixGVCF + "\n" +
                "gVCFRegionalCallingSwitch = " + gVCFRegionalCallingSwitch + "\n" +
                "grch38callingsh = " + grch38callingsh + "\n" +
                "assemblyVersion = " + assemblyVersion + "\n" +
                "pcash = " + pcash + "\n" +
                "admixturesh = " + admixturesh + "\n" +
                "extractsh = " + extractsh + "\n" +
                "mtysh = " + mtysh + "\n" +
                "as2sh = " + as2sh + "\n" +
                "mtyscrdir = " + mtyscrdir + "\n" +
                "hlash = " + hlash + "\n" +
                "snpediash = " + snpediash + "\n" +
                "pgssh = " + pgssh + "\n" +
                "pgsRetrysh = " + pgsRetrysh + "\n" +
                "provincesh = " + provincesh + "\n" +
                "pgsdir = " + pgsdir + "\n" +
                "pgsrefPanel = " + pgsrefPanel + "\n" +
                "pgstxtPath = " + pgstxtPath + "\n" +
                "pgsRef = " + pgsRef + "\n" +
                "svAnnotationPy = " + svAnnotationPy + "\n" +
                "svAnnotationExtractSh = " + svAnnotationExtractSh + "\n" +
                "svAnnotationRef = " + svAnnotationRef + "\n" +
                "svAnnoDB = " + svAnnoDB + "\n" +
                "svOverlapPer = " + svOverlapPer + "\n" +
                "svGeneAnnoFlag = " + svGeneAnnoFlag + "\n" +
                "Thread = " + thread + "\n" +
                "module = " + moduleSwitchCode + "\n";
    }

}
