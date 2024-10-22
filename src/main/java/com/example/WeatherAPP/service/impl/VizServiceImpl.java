package com.example.WeatherAPP.service.impl;

import com.example.WeatherAPP.model.ResponseModel;
import com.example.WeatherAPP.model.VizModel;
import com.example.WeatherAPP.repository.VizRepository;
import com.example.WeatherAPP.service.VizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class VizServiceImpl implements VizService {

    @Autowired
    private VizRepository vizRepository;


    @Override
    public VizModel getAvgTempsForCity(String city) {

        Optional<List<ResponseModel>> responseModelsOptional = vizRepository.findByCity(city);

        if(responseModelsOptional.isEmpty()) {
            return new VizModel();
        }

        VizModel vizModel = new VizModel();

        List<ResponseModel> responseModels = responseModelsOptional.get();
        vizModel.setCity(city);
        responseModels.forEach(
                responseModel -> {
                    vizModel.getAvgTemps().add(responseModel.getAvgTemp());
                    vizModel.getTimestamps().add(responseModel.getTimestamp());

                }
        );
//        saveVizModelToDb(vizModel);
        return vizModel;
    }
}
