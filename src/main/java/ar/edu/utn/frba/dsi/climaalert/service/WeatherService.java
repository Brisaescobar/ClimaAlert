package ar.edu.utn.frba.dsi.climaalert.service;

import ar.edu.utn.frba.dsi.climaalert.client.WeatherApiClient;
import ar.edu.utn.frba.dsi.climaalert.client.WeatherApiResponse;
import ar.edu.utn.frba.dsi.climaalert.model.WeatherData;
import ar.edu.utn.frba.dsi.climaalert.repository.WeatherDataRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

// consulta el clima actual, lo persiste y expone el ultimo registro
@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherService {

	private static final double ALERT_TEMP_THRESHOLD = 35.0;
	private static final double ALERT_HUMIDITY_THRESHOLD = 60.0;

	private final WeatherApiClient weatherApiClient;
	private final WeatherDataRepository weatherDataRepository;

	public void fetchAndStore() {
		WeatherApiResponse response = weatherApiClient.getCurrentWeather();
		WeatherApiResponse.CurrentDto current = response.getCurrent();

		boolean alertCondition = current.getTempC() != null && current.getHumidity() != null
				&& current.getTempC() > ALERT_TEMP_THRESHOLD
				&& current.getHumidity() > ALERT_HUMIDITY_THRESHOLD;

		WeatherData data = WeatherData.builder()
				.registeredAt(LocalDateTime.now())
				.location(response.getLocation() != null ? response.getLocation().getName() : "desconocida")
				.temperatureCelsius(current.getTempC())
				.humidity(current.getHumidity())
				.feelsLikeCelsius(current.getFeelslikeC())
				.condition(current.getCondition() != null ? current.getCondition().getText() : null)
				.windKph(current.getWindKph())
				.precipitationMm(current.getPrecipMm())
				.cloud(current.getCloud())
				.uvIndex(current.getUv())
				.alertCondition(alertCondition)
				.build();

		weatherDataRepository.save(data);
		log.info("Datos climaticos guardados: {}", data);
	}

	public Optional<WeatherData> getLatestWeatherData() {
		return weatherDataRepository.findTopByOrderByRegisteredAtDesc();
	}
}