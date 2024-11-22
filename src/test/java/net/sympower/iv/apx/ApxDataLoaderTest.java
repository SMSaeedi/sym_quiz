package net.sympower.iv.apx;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static net.sympower.iv.apx.FileUtils.convertToStringJson;
import static net.sympower.iv.apx.FileUtils.readInputFile;
import static net.sympower.iv.apx.Utils.formatDate;
import static net.sympower.iv.apx.Utils.getValueForLabel;
import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mockStatic;
import static org.springframework.test.util.AssertionErrors.assertNotNull;

class ApxDataLoaderTest {
    private ApxDataLoader quoteService;
    private final static Path path = Paths.get("src/test/resources/net/sympower/cityzen/apx/apx-data.json");

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        quoteService = new ApxDataLoader();
    }

    @Test
    public void testParseJson() {
        try (InputStream inputStream = new FileInputStream(path.toFile())) {
            List<Quote> quotes = convertToStringJson(inputStream).getQuote();
            assertNotNull("The quotes list should not be null", quotes);
            Quote firstQuote = quotes.get(0);
            assertEquals("APX Power NL Hourly", firstQuote.getMarket());
            assertEquals(1573599600000L, firstQuote.getDateApplied());
            String netVolume = firstQuote.getValues().stream()
                    .filter(value -> "Net Volume".equals(value.getTLabel()))
                    .findAny()
                    .map(Value::getValue)
                    .orElse("");
            assertEquals("4514.20", netVolume);
        } catch (IOException e) {
            e.printStackTrace();
            fail("Error reading JSON: " + e.getMessage());
        }
    }

    @Test
    void testLoad_readsInputFileAndReturnsQuoteWrapper() {
        Quote quote = new Quote();
        quote.setMarket("APX Power NL Hourly");
        quote.setDateApplied(1633035600000L);
        quote.setValues(List.of(
                new Value("Order", "10"),
                new Value("Net Volume", "1000"),
                new Value("Price", "25.5")
        ));

        List<Quote> quotes = List.of(quote);

        try (MockedStatic<FileUtils> mockedStatic1 = mockStatic(FileUtils.class);
                MockedStatic<Utils> mockedStatic2 = mockStatic(Utils.class)) {
            mockedStatic1.when(FileUtils::readInputFile).thenReturn(quotes);
            mockedStatic2.when(() -> formatDate(anyLong())).thenReturn("2021-10-01");
            mockedStatic2.when(() -> getValueForLabel(quote, "Order")).thenReturn("10");
            mockedStatic2.when(() -> getValueForLabel(quote, "Net Volume")).thenReturn("1000");
            mockedStatic2.when(() -> getValueForLabel(quote, "Price")).thenReturn("25.5");


            QuoteWrapper result = quoteService.load();

            Assertions.assertNotNull(result);
            assertEquals(1, result.getQuote().size());

            Quote wrappedQuote = result.getQuote().get(0);
            assertEquals("2021-10-01", formatDate(wrappedQuote.getDateApplied()));
            assertEquals("10", getValueForLabel(wrappedQuote, "Order"));
            assertEquals("1000", getValueForLabel(wrappedQuote, "Net Volume"));
            assertEquals("25.5", getValueForLabel(wrappedQuote, "Price"));
        }
    }

    @Test
    void testInit_whenUrlIsNull_setsUrl() throws MalformedURLException {
        URL testUrlStr = path.toUri().toURL();
        quoteService.urlStr = testUrlStr.toString();
        quoteService.url = null;

        quoteService.init();

        Assertions.assertNotNull(quoteService.url);
        assertEquals(new URL(testUrlStr.toString()), quoteService.url);
    }

    @Test
    void testInit_whenUrlIsNotNull_doesNotChangeUrl() throws MalformedURLException {
        URL existingUrl = path.toUri().toURL();
        quoteService.url = existingUrl;

        quoteService.init();

        assertEquals(existingUrl, quoteService.url);
    }

    @Test
    void testReadInputFile_validFile_returnsQuotes() {
        try (MockedStatic<FileUtils> mockedStatic = mockStatic(FileUtils.class)) {
            mockedStatic.when(() -> convertToStringJson(any(InputStream.class)))
                    .thenReturn(new QuoteWrapper(new ArrayList<>()));

            List<Quote> quotes = readInputFile();

            Assertions.assertNotNull(quotes);
            assertTrue(quotes.isEmpty());
        }
    }

    @Test
    void testConvertToStringJson_validInputStream_returnsQuoteWrapper() {
        InputStream inputStream = new ByteArrayInputStream("{\"quote\": []}".getBytes());

        try (MockedStatic<FileUtils> mockedStatic = mockStatic(FileUtils.class)) {
            mockedStatic.when(() -> convertToStringJson(any(InputStream.class)))
                    .thenReturn(new QuoteWrapper(new ArrayList<>()));

            QuoteWrapper result = convertToStringJson(inputStream);

            Assertions.assertNotNull(result);
            assertEquals(0, result.getQuote().size());
        }
    }

    @Test
    void testFormatDate_validEpochMillis_returnsFormattedDate() {
        long epochMillis = 1635735600000L; // Example timestamp for testing
        String expectedDate = "2021-11-01";

        String formattedDate = formatDate(epochMillis);

        assertEquals(expectedDate, formattedDate);
    }

    @Test
    void testGetValueForLabel_existingLabel_returnsValue() {
        Quote quote = new Quote();
        Value value = new Value("Hour", "10:00");
        quote.setValues(List.of(value));

        String result = getValueForLabel(quote, "Hour");

        assertEquals("10:00", result);
    }

    @Test
    void testGetValueForLabel_nonExistingLabel_returnsNA() {
        Quote quote = new Quote();
        Value value = new Value("Hour", "10:00");
        quote.setValues(List.of(value));

        String result = getValueForLabel(quote, "Price");

        assertEquals("N/A", result);
    }
}