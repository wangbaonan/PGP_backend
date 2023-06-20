package pog.pgp_alpha_v1.service;

import pog.pgp_alpha_v1.model.AllelePopulationFrequency;

public interface AllelePopulationFrequencyService {
    AllelePopulationFrequency  save(AllelePopulationFrequency data);
    void loadDataFromFile(String pathToFile);
    AllelePopulationFrequency getById(String id);
}
