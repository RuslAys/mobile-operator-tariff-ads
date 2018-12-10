package ru.javaschool.tariffads.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.javaschool.tariffads.dto.TariffPlanDto;

import javax.annotation.PostConstruct;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@Singleton
@Lock(LockType.WRITE)
public class TariffServiceImpl implements TariffService, Serializable {

    private List<TariffPlanDto> tariffPlans;
    private static ObjectMapper mapper = new ObjectMapper();
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);


    private static String httpGetRest(String url) throws IOException {
        URL urlRequest = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) urlRequest.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");
        int code = connection.getResponseCode();
        if(code != 200 ){
            throw new IOException("Result code is not OK");
        }
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String response = bufferedReader.readLine();
        connection.disconnect();
        return response;
    }

    private List<TariffPlanDto> fetchTariffs(){
        try {
            String response = httpGetRest("http://localhost:8080/mss/rest/tariffs");
            return mapper.readValue(response, new TypeReference<List<TariffPlanDto>>(){});
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @PostConstruct
    private void uploadTariffPlans(){
        tariffPlans = fetchTariffs();
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }


    @Override
    public List<TariffPlanDto> getAllTariffs() {
        if(tariffPlans == null){
            tariffPlans = fetchTariffs();
        }
        return tariffPlans;
    }

    @Override
    public void updateTariffs() {
        List<TariffPlanDto> tariffPlanDtos = fetchTariffs();
        pcs.firePropertyChange("tariffPlans", this.tariffPlans, tariffPlanDtos);
        tariffPlans = tariffPlanDtos;
    }
}
