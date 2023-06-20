package pog.pgp_alpha_v1.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document(collection = "allelePopulationFrequencies")
@Data
public class AllelePopulationFrequency {
    @Id
    private String id; // chr_pos like "chr10_102837723"
    private Map<String, Map<String, Double>> data;
}
