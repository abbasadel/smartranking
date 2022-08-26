package com.github.abbasadel.perpetua.smartranking.services.providers.sellicsClient;

import com.github.abbasadel.perpetua.smartranking.config.SellicsClientProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import software.amazon.awssdk.core.FileTransformerConfiguration;
import software.amazon.awssdk.core.async.AsyncResponseTransformer;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

@Service
@Slf4j
public class SellicsClient {
    private final S3AsyncClient s3Client;
    private final SellicsClientProperties props;
    private Path dataFileTempLocation;


    public SellicsClient(S3AsyncClient s3Client, SellicsClientProperties props) {
        this.s3Client = s3Client;
        this.props = props;
    }


    public Flux<CsvRecord> fetchAllRecords() {
        return parseDataFile(getDataFileTempLocation());
    }

    /**
     * Download the data file from S3 and save it into a temp file
     */
    public void download() {
        try {
            dataFileTempLocation = Files.createTempFile(props.getBucket(), props.getFilename().replace("/", "-"));
            downloadDataFile(props.getBucket(), props.getFilename(), dataFileTempLocation);
        } catch (IOException e) {
            log.error("Error creating temp file", e);
            throw new RuntimeException(e);
        }
    }


    static Flux<CsvRecord> parseDataFile(final Path dataFileLocation) {
        return Flux.using(
                () -> Files.lines(dataFileLocation)
                        .skip(1) //TODO: can be a config
                        .map(line -> CsvRecord.of(line.split(";"))), //TODO: can be a config
                Flux::fromStream,
                Stream::close
        );
    }


    private void downloadDataFile(final String bucket, final String filename, final Path destination) {
        log.info("Downloading file s3://{}/{}", bucket, filename);

        GetObjectRequest awsFileRequest = GetObjectRequest.builder()
                .bucket(props.getBucket()).key(props.getFilename())
                .build();

        s3Client.getObject(awsFileRequest,
                        AsyncResponseTransformer
                                .toFile(destination, FileTransformerConfiguration.defaultCreateOrReplaceExisting()))
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Error downloading file s3://{}/{}", bucket, filename, ex);
                    } else {
                        log.info("Finished downloading file s3://{}/{}", bucket, filename);
                    }
                })
                .join();

    }

    public Path getDataFileTempLocation() {
        return dataFileTempLocation;
    }
}
