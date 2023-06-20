package pog.pgp_alpha_v1.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import pog.pgp_alpha_v1.model.AllelePopulationFrequency;
import pog.pgp_alpha_v1.repository.AllelePopulationFrequencyRepository;
import pog.pgp_alpha_v1.service.AllelePopulationFrequencyService;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Map;

@Service
public class AllelePopulationFrequencyServiceImpl implements AllelePopulationFrequencyService {
    @Resource
    private AllelePopulationFrequencyRepository allelePopulationFrequencyRepository;

    @Override
    public AllelePopulationFrequency save(AllelePopulationFrequency data) {
        return allelePopulationFrequencyRepository.save(data);
    }

    @Override
    public void loadDataFromFile(String pathToFile) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, Map<String, Map<String, Double>>> map =
                    objectMapper.readValue(new File(pathToFile), Map.class);

            map.forEach((key, value) -> {
                AllelePopulationFrequency data = new AllelePopulationFrequency();
                data.setId(key);
                data.setData(value);
                this.save(data);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public AllelePopulationFrequency getById(String id) {
        return allelePopulationFrequencyRepository.findById(id).orElse(null);
    }
}
