package org.example;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

public class Main {
    public static void main(String[] args) {

        try {

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -1);
            int month = calendar.get(Calendar.MONTH)+1;
            String monthStr = String.valueOf(month);
            if(month<10) monthStr = "0"+monthStr;
            String yesterday = calendar.get(Calendar.YEAR)+"-"+monthStr+"-"+calendar.get(Calendar.DAY_OF_MONTH);
            double todayRUS = getRub(null);
            double yesterdayRUB = getRub(yesterday);
            System.out.println("Курс на сегодня: "+todayRUS);
            System.out.println("Курс на вчера: "+yesterdayRUB);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    static double getRub(String date) throws IOException, ParseException {
        String appKey = "ad1c3a856f32428db34c670bfefe84c1";
        String endPoint = "latest.json"; // Для сегодняшнего дня
        if(date != null){ // Если мы передали дату, значит нас интересует не сегодняшний курс
            endPoint = "historical/"+date+".json"; // Для дня переданного в аргумент функции
        }
        String spec = "https://openexchangerates.org/api/"+endPoint+"?app_id="+appKey+"&symbols=RUB";
        URL url = new URL(spec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        InputStream is = connection.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        StringBuilder result = new StringBuilder();
        while ((line = br.readLine()) != null){
            result.append(line);
        }
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(result.toString());
        String rates = jsonObject.get("rates").toString();
        jsonObject = (JSONObject) jsonParser.parse(rates);
        double rub = Double.parseDouble(jsonObject.get("RUB").toString());
        return rub;
    }
}