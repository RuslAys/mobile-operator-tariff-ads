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

    private static final String TARIFFS_URL = "http://localhost:8080/mss/rest/tariffs";

    private List<TariffPlanDto> tariffPlans;
    private static ObjectMapper mapper = new ObjectMapper();
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);


    /**
     * Method to get request data by http
     * @param url address
     * @return response
     * @throws IOException
     */
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

    /**
     * Method to fetch tariff plans by http get {@link #httpGetRest(String)}
     * @return list with tariff plan dtos
     */
    private List<TariffPlanDto> fetchTariffs(){
        try {
            String response = httpGetRest(TARIFFS_URL);
            return mapper.readValue(response, new TypeReference<List<TariffPlanDto>>(){});
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Method to download tariff plans
     */
    @PostConstruct
    private void downloadTariffPlans(){
        tariffPlans = fetchTariffs();
    }

    /**
     * Method to add property change listener
     * @param listener
     */
    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }


    /**
     * Method to return all tariff plans {@link #tariffPlans}
     * @return list with tariff plan dtos
     */
    @Override
    public List<TariffPlanDto> getAllTariffs() {
        if(tariffPlans == null){
            tariffPlans = fetchTariffs();
        }
        return tariffPlans;
    }

    /**
     * Method to update tariff plans {@link #tariffPlans}
     */
    @Override
    public void updateTariffs() {
        List<TariffPlanDto> tariffPlanDtos = fetchTariffs();
        pcs.firePropertyChange("tariffPlans", this.tariffPlans, tariffPlanDtos);
        tariffPlans = tariffPlanDtos;
    }
}
