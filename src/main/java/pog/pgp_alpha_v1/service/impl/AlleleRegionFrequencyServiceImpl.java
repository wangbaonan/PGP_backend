package pog.pgp_alpha_v1.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import pog.pgp_alpha_v1.model.AlleleRegionFrequency;
import pog.pgp_alpha_v1.repository.AlleleRegionFrequencyRepository;
import pog.pgp_alpha_v1.service.AlleleRegionFrequencyService;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Map;

@Service
public class AlleleRegionFrequencyServiceImpl implements AlleleRegionFrequencyService {
    @Resource
    private AlleleRegionFrequencyRepository alleleRegionFrequencyRepository;

    @Override
    public AlleleRegionFrequency save(AlleleRegionFrequency data) {
        return alleleRegionFrequencyRepository.save(data);
    }

    @Override
    public void loadDataFromFile(String pathToFile) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, Map<String, Map<String, Double>>> map =
                    objectMapper.readValue(new File(pathToFile), Map.class);

            map.forEach((key, value) -> {
                AlleleRegionFrequency data = new AlleleRegionFrequency();
                data.setId(key);
                data.setData(value);
                this.save(data);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public AlleleRegionFrequency getById(String id) {
        return alleleRegionFrequencyRepository.findById(id).orElse(null);
    }
}
