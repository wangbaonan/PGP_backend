package pog.pgp_alpha_v1.service;

import pog.pgp_alpha_v1.model.AlleleRegionFrequency;

public interface AlleleRegionFrequencyService {
    AlleleRegionFrequency save(AlleleRegionFrequency data);
    void loadDataFromFile(String pathToFile);
    AlleleRegionFrequency getById(String id);
}
