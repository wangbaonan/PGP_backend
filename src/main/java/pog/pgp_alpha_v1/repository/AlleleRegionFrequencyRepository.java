package pog.pgp_alpha_v1.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import pog.pgp_alpha_v1.model.AlleleRegionFrequency;

public interface AlleleRegionFrequencyRepository extends MongoRepository<AlleleRegionFrequency, String> {
}
