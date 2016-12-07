package com.waldo;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.google.common.collect.Lists;
import com.waldo.model.Contents;
import com.waldo.model.ListBucketResult;
import com.waldo.services.ExifDecoderService;

/**
 * @author Suresh
 * 
 * This process starts collecting the s3 bucket source and sends each image key to a decoder
 *
 */
@Component
public class PhotoProcessor implements ApplicationRunner {

	Logger logger = LoggerFactory.getLogger(PhotoProcessor.class);

	@Value("${s3.bucket.url}")
	String s3BucketUrl;
	
	@Value("${bulk.process.size}")
	Integer partitionSize;

	@Autowired
	private ApplicationContext ctx;

	@Override
	public void run(ApplicationArguments arg0) throws Exception {
		logger.debug("Starting Photo Processing using bucket URL: {}", s3BucketUrl);
		
		//Using rest client with JAXB mapped conversion of xml input to POJOs
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<?> entity = new HttpEntity<Object>(headers);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(s3BucketUrl);
		restTemplate.getMessageConverters().add(new MyGsonHttpMessageConverter());
		
		ListBucketResult bucket = restTemplate.exchange(builder.build().encode().toString(), HttpMethod.GET,
                entity, new ParameterizedTypeReference<ListBucketResult>(){}).getBody();
		
		logger.debug("Collected s3 bucket source");
		
		List<Contents> contents = bucket.contents;
		int bucketSize = contents.size();

		//Splitting into batches for processing n images in one concurrent process
		//It will be too expensive to spawn on ethread for one image
		//TODO: Use spring batch or even MQ to process EXIM decoding job
		List<List<Contents>> batches =  Lists.partition(contents, partitionSize);

		//Start decoding each batch in separate thread
		int count=0;
		for (List<Contents> batch : batches) {
			logger.debug("Processing batch {} with {} images", ++count, partitionSize);
			ExifDecoderService decoder = (ExifDecoderService) ctx.getBean("exifDecoderService");
			decoder.setContents(batch);
			decoder.setContentUri(s3BucketUrl);
			decoder.start();
		}
		
		logger.debug("Finished processing exim metadata decoding for {} images", bucketSize);
	}
	
}

//Adapter class for xml message converter
class MyGsonHttpMessageConverter extends GsonHttpMessageConverter {
    public MyGsonHttpMessageConverter() {
        List<MediaType> types = Arrays.asList(
                new MediaType("application", "xml", DEFAULT_CHARSET)
        );
        super.setSupportedMediaTypes(types);
    }
}