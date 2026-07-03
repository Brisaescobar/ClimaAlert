package ar.edu.utn.frba.dsi.climaalert.scheduler;

import ar.edu.utn.frba.dsi.climaalert.model.WeatherData;
import ar.edu.utn.frba.dsi.climaalert.service.AlertService;
import ar.edu.utn.frba.dsi.climaalert.service.WeatherService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/*se ejecuta cada un minuto, analiza el ultimo registro climatico
si supera t > 35 y h > 60 y envia alertas por correo */

@Slf4j
@Component
@RequiredArgsConstructor
public class AlertScheduler {
	private final WeatherService weatherService;
	private final AlertService alertService;

	@Scheduled(fixedRateString = "60000", initialDelayString = "10000")
	public void analyzeAndAlert() {
		log.info("[SCHEDULER] analizando condiciones climaticas");

		Optional<WeatherData> latestData = weatherService.getLatestWeatherData();

		if (latestData.isEmpty()) {
			log.warn("[SCHEDULER] no hay datos climaticos disponibles para analizar");
			return;
		}
			WeatherData data = latestData.get();
		if(Boolean.TRUE.equals((data.getAlertCondition()))) {
			log.info("[SCHEDULER] ¡Condicion critica! temperatura: {}°C > 35°C, Humedad: {}% > 60%",
					data.getTemperatureCelsius(), data.getHumidity());
			alertService.sendAlert(data);
		} else {
			log.info("[SCHEDULER] Condiciones normales, no se requiere alerta");
		}
	}
}
