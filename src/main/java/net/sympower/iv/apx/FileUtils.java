package net.sympower.iv.apx;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

class FileUtils {
    private final static Path path = Paths.get("src/test/resources/net/sympower/cityzen/apx/apx-data.json");

    public static List<Quote> readInputFile() {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(path.toFile());
        } catch (IOException e) {
            throw new RuntimeException(e); // proper logging
        }
        return FileUtils.convertToStringJson(inputStream).getQuote();
    }

    public static QuoteWrapper convertToStringJson(InputStream inputStream) {
        ObjectMapper objectMapper = new ObjectMapper();
        QuoteWrapper quoteWrapper = null;

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder result = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null)
                result.append(line);

            reader.close();

            quoteWrapper = objectMapper.readValue(result.toString(), QuoteWrapper.class);
        } catch (IOException e) {
            e.printStackTrace(); // proper logging
        }
        return quoteWrapper;
    }
}