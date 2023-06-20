package pog.pgp_alpha_v1.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import pog.pgp_alpha_v1.model.AllelePopulationFrequency;

public interface AllelePopulationFrequencyRepository extends MongoRepository<AllelePopulationFrequency, String> {
}
