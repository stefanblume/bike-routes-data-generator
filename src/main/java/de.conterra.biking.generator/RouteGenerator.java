package de.conterra.biking.generator;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

public class RouteGenerator {

    Random random = new Random();


    /**
     * Generate sample "routing" data based on münster's street open data
     *
     * @param maxDistance     maximum number of kilometers an euclidian distance of start/end shall not exceed
     * @param numberOfEntries number of entries to be generated
     * @param startDate       desired start date, i.e. "01.01.2021"
     * @param endDate         desired end date, i.e. "01.01.2021"
     * @param dataType,       valid values: "bike", "scooter"
     * @return
     */
    public void generate(int maxDistance, int numberOfEntries, String startDate, String endDate, String dataType) throws ParseException {
        List<AdressData> entries = getAdressData("https://www.stadt-muenster.de/ows/mapserv706/odstrasseserv?REQUEST=GetFeature&SERVICE=WFS&VERSION=1.0.0&TYPENAME=ms:Strassen&OUTPUTFORMAT=CSV&EXCEPTIONS=XML&MAXFEATURES=5000");

        List<Boolean> type = new ArrayList<>();
        type.add(true);
        type.add(false);

        System.out.println("START_TIME;END_TIME;START_X;START_Y;END_X;END_Y;DISTANCE;TIME_IN_HOURS");
        DecimalFormat df = (DecimalFormat) NumberFormat.getNumberInstance(Locale.GERMAN);
        df.setMaximumFractionDigits(2);
        int i = 0;
        while (i < numberOfEntries) {
            AdressData from = pickStreet(entries);
            AdressData to = pickStreet(entries);
            double distanceH = Math.abs(from.hochwert - to.hochwert) / 1000d;
            double distanceR = Math.abs(from.rechtswert - to.rechtswert) / 1000d;
            double velocity = 0;
            if (dataType.equalsIgnoreCase("bike")) {
                double velocityBike = 15; //km/h
                velocity = velocityBike;

            } else {
                double velocityRoller = 11; //km/h
                velocity = velocityRoller;
            }

            double distance = Math.sqrt(Math.exp(distanceH) + Math.exp(distanceR));

            if (distance < maxDistance) {
                Date startRange = DateFormat.getDateInstance().parse(startDate);
                Date endRange = DateFormat.getDateInstance().parse(endDate);
                Date start = pickRandomStartTime(startRange, endRange);
                double time = distance / velocity;

                long v = (long) (time * 60L * 60L * 1000L);
                long endTime = start.getTime() + v;
                System.out.println(start.getTime() + ";" + endTime + ";" + from.getRechtswert() + ";" + from.hochwert + ";" + to.getRechtswert() + ";" + to.getHochwert() + ";" + df.format(distance) + ";" + df.format(time));
                i++;
            }
        }
    }

    private Date pickRandomStartTime(Date rangeStart, Date rangeEnd) {
        Date randomDate = new Date(ThreadLocalRandom.current()
                .nextLong(rangeStart.getTime(), rangeEnd.getTime()));
        return randomDate;
    }

    private AdressData pickStreet(List<AdressData> entries) {
        return entries.get(random.nextInt(entries.size()));
    }

    private boolean pickType(List<Boolean> entries) {
        return entries.get(random.nextInt(entries.size()));
    }

    private List<AdressData> getAdressData(String location) {
        List<AdressData> entries = new ArrayList();
        try {
            URL url = new URL(location);
            URLConnection urlCon = url.openConnection();
            InputStream inputStream = urlCon.getInputStream();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            Reader reader = new InputStreamReader(bufferedInputStream);
            CSVReader csv = new CSVReader(reader);
            List<String[]> r = csv.readAll();
            boolean isFirst = true;
            for (String[] x : r) {
                if (!isFirst) entries.add(new AdressData(x));
                isFirst = false;
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
        return entries;
    }
}
