package ar.edu.utn.frba.dsi.climaalert.scheduler;

import ar.edu.utn.frba.dsi.climaalert.service.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

// ejecura cada 5 min, tiene los datos climaticos y persiste en la bd

@Slf4j
@Component
@RequiredArgsConstructor
public class WeatherFetchScheduler {
	private final WeatherService weatherService;

	@Scheduled(fixedRateString = "300000", initialDelayString = "0")

	public void fetchWeatherData() {
		log.info("[SCHEDULER] Iniciando obtencion de datos climaticos");
		try {
			weatherService.fetchAndStore();
		} catch (Exception e) {
			log.error("[SCHEDULER] Error al obtener datos climaticos: {}", e.getMessage(), e);
		}
	}
}
