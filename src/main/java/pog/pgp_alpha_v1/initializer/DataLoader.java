package pog.pgp_alpha_v1.initializer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pog.pgp_alpha_v1.service.AllelePopulationFrequencyService;
import pog.pgp_alpha_v1.service.AlleleRegionFrequencyService;

import javax.annotation.Resource;

@Component
@Order(1)
public class DataLoader implements CommandLineRunner {
    @Value("${my-app.data.allelePopulationFrequency}")
    String allelePopulationFrequencyDataPath;
    @Value("${my-app.data.alleleRegionFrequency}")
    String alleleRegionFrequencyDataPath;
    @Resource
    AlleleRegionFrequencyService alleleRegionFrequencyService;
    @Resource
    AllelePopulationFrequencyService allelePopulationFrequencyService;

    @Override
    public void run(String... args) throws Exception {
        alleleRegionFrequencyService.loadDataFromFile(alleleRegionFrequencyDataPath);
        allelePopulationFrequencyService.loadDataFromFile(allelePopulationFrequencyDataPath);
    }
}
