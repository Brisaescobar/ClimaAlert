package ar.edu.utn.frba.dsi.climaalert.service;

import ar.edu.utn.frba.dsi.climaalert.model.WeatherData;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/*se encarga de enviar las notificaciones cuando es critico*/

@Slf4j
@Service
@RequiredArgsConstructor
public class AlertService {
	private final JavaMailSender mailSender;

	@Value ("${spring.mail.from}")
	private String from;

	@Value("${alert.email.recipients}")
	private List<String> recipients;

	private static final DateTimeFormatter FORMATTER =
			DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

	public void sendAlert(WeatherData weatherData) {
		log.warn("¡Condicion critica detectada! temperatura:{}°C, Humedad: {}%. Enviando alertas... ",
				weatherData.getTemperatureCelsius(), weatherData.getHumidity());

		simpleMailMessage message = new SimpleMailMessage();
		message.setFrom(from);
		message.setTo(recipients.toArray(new String[0]));
		message.setSubject(buildSubject(weatherData));
		message.setText(buildBody(weatherData));

		try {
			mailSender.send(message);
			log.info("Alertas enviadas exitosamente a: {}", recipients);
		} catch (Exception e) {
			log.error("Error al enviar alertas por correo: {}", e.getMessage(), e);
		}
	}

	private String buildSubject(WeatherData data) {
		return String.format("ALERTA CLIMATICA - %s - %s",
				data.getLocation(),
				data.getRegisterdAt().format(FORMATTER));
	}

	private String buildBody(WeatherData data) {
		return String.format("""
				 ⚠️ ALERTA CLIMÁTICA - CONDICIONES CRÍTICAS DETECTADAS
				 ------------------------------------------------------
				 Se detectaron condiciones climaticas peligrosas 
				 📍Ubicacion: %s
				 🕐Fecha y Hora: %s
				 
				 🌡️  DATOS CLIMÁTICOS ACTUALES
				 ----------------------------------
				 Temperatura: 
				 Sensacion termica: 
				 Humedad: 
				 Condicion: 
				 Viento: 
				 Precipitacion:
				 Nubosidad: 
				 Indice UV: 
				 ----------------------------------
				 Este mensaje fue generado automaticamente por climalert 
				 No responda a este correo
				""",
				data.getLocation(),
				data.getRegisterdAt().format(FORMATTER),
				data.getTemperatureCelsius(),
				data.getFeelsLikeCelsius() != null ? data.getFeelsLikeCelsius() : 0.0,
				data.getHumidity(), //TODO cambiar
				data.getCondition(),
				data.getWindKph() != null ? data.getWindKph() : 0.0,
				data.getPrecipitationMm() != null ? data.getPrecipitationMm() : 0.0,
				data.getCloud() != null ? data.getCloud() : 0,
				data.getUvIndex() != null ? data.getUvIndex() : 0.0
		);
	}
}
