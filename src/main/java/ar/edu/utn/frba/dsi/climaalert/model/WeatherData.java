package ar.edu.utn.frba.dsi.climaalert.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// registro climatico almacenado localmente cada 5 min
@Entity
@Table (name = "weather_data")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class WeatherData {
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private LocalDateTime registerdAt;

	@Column(nullable = false)
	private String location;

	@Column(nullable = false)
	private Double temperatureCelsius;

	@Column(nullable = false)
	private Double humidity; //TODO cambiar

	private Double feelsLikeCelsius;
	private String condition; //TODO en español
	private Double windKph;
	private Double precipitationMm;
	private Integer cloud;
	private Double uvIndex;

	// condicion de alerta
	// temp > 35 y humedad > 60%
	@Column(nullable = false)
	private Boolean alertCondition;

}
