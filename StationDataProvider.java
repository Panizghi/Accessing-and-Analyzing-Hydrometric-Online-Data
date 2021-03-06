package sample;

import java.net.URL;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.Closeable;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.Instant;
import java.util.stream.Stream;

public class StationDataProvider implements Closeable {
    private static final String[] HEADERS = {
            "ID",
            "Date",
            "Water Level / Niveau d'eau (m)"
    };

    private static final CSVFormat CUSTOM_CSV_FORMAT = CSVFormat.DEFAULT
            .withHeader(HEADERS)
            .withSkipHeaderRecord();

    private static WaterLevel parseRecord(CSVRecord record) {
        
        var  rawDate = record.get(HEADERS[1]);
        var rawValue = record.get(HEADERS[2]);
        if ( rawDate == null || rawValue==null){
            return null;
        }
        var parsedInstant = Instant.parse(rawDate);
        var parsedValue = Double.parseDouble(rawValue);
        return new WaterLevel(parsedInstant,parsedValue);
    }

    private final CSVParser csvParser;

    public StationDataProvider(URL source) throws IOException {
        this.csvParser = CSVParser.parse(source, Charset.defaultCharset(), CUSTOM_CSV_FORMAT);
    }

    public Stream<WaterLevel> getData() throws IOException {
      

        return this.csvParser.getRecords().stream().map(s->parseRecord(s));
    }

    @Override
    public void close() throws IOException {
        csvParser.close();
    }
}
