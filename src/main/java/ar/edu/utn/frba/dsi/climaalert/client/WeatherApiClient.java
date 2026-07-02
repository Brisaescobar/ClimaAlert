package ar.edu.utn.frba.dsi.climaalert.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

// cliente rest para el endpoint

@Slf4j
@Component
@RequiredArgsConstructor
public class WeatherApiClient {
	private final RestTemplate restTemplate;

	@Value ("${weatherapi.base-url}")
	private String baseUrl;

	@Value("${weatherapi.api-key}")
	private String apiKey;

	@Value("${weatherapi.location}")
	private String location;

	//@return
	//@throws WeatherApiException

	public WeatherApiResponse getCurrentWeather() {
		String url = UriComponentsBuilder
				.fromHttpUrl(baseUrl + "/current.json")
				.queryParam("key", apiKey)
				.queryParam("q", location)
				.queryParam("aqi", "no")
				.toUriString();

		log.debug("Consultando WeatherAPI: {}", url.replace(apiKey, "***"));

		try {
			WeatherApiResponse response = restTemplate.getForObject(url, WeatherApiResponse.class);
			if (response == null) {
				throw new WeatherApiException("La respuesta de WeatherAPI fue nula");
			}
			log.info("Los datos climaticos obtenidos para {}: {}C, humedad {}%",
					location,
					response.getCurrent().getTempC(),
					response.getCurrent().getHumidity()); //TODO ver si puedo cambiar a humedad
			return response;
		} catch (Exception e) {
				throw new WeatherApiException ("Error al consultar weatherApi: " + e.getMessage(), e);
		}
	}

		public static class WeatherApiClass extends RuntimeException {
			public WeatherApiExeption(String message) {
				super (message);
			}
			public WeatherApiException(String message, Throwable cause) {
				super(message, cause);
			}
		}
	}

