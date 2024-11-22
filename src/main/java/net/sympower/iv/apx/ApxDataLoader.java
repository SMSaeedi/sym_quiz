package net.sympower.iv.apx;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.out;
import static net.sympower.iv.apx.FileUtils.readInputFile;
import static net.sympower.iv.apx.Utils.formatDate;
import static net.sympower.iv.apx.Utils.getValueForLabel;

@Service
public class ApxDataLoader {
    @Value("${apxDataLoader.url}")
    String urlStr;

    URL url;

    public void init() throws MalformedURLException {
        if (this.url == null)
            this.url = new URL(urlStr);
    }

    protected QuoteWrapper load() {
        List<Quote> quotes = new ArrayList<>();

        for (Quote quote : readInputFile()) {
            String date = formatDate(quote.getDateApplied());
            String hour = getValueForLabel(quote, "Hour");
            String netVolume = getValueForLabel(quote, "Net Volume");
            String price = getValueForLabel(quote, "Price");

            out.printf("Date: %s, Hour: %s, Net Volume: %s, Price: %s%n",
                    date, hour, netVolume, price);
            quotes.add(quote);
        }

        QuoteWrapper quoteWrapper = new QuoteWrapper();
        quoteWrapper.setQuote(quotes);
        return quoteWrapper;
    }
}