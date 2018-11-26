package ru.javaschool.tariffads.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.javaschool.tariffads.dto.TariffPlanDto;

import javax.ejb.Stateless;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.List;

@Stateless
public class TariffServiceImpl implements TariffService {

    private static ObjectMapper mapper = new ObjectMapper();

    private static String httpGetRest(String url) throws IOException {
        URL urlRequest = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) urlRequest.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");
//        String base64ClientCredentials = new String(Base64.getEncoder().encodeToString("a:p".getBytes()));
//        connection.setRequestProperty("Authorization", "Basic " + base64ClientCredentials);
        int code = connection.getResponseCode();
        if(code != 200 ){
            System.out.println(code);
            throw new IOException("Result code is not OK");
        }
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String response = bufferedReader.readLine();
        System.out.println("Response = " + response);
        connection.disconnect();
        return response;
    }

    @Override
    public List<TariffPlanDto> getAllTariffs() {
        try{
            String response = httpGetRest("http://localhost:8080/mss/rest/tariffs");
            return mapper.readValue(response, new TypeReference<List<TariffPlanDto>>(){});
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }
}
