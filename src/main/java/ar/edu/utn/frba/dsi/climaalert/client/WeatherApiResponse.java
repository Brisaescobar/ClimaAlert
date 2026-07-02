package ar.edu.utn.frba.dsi.climaalert.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

// "mapeo" con json para la respuesta del endpoint
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherApiResponse {
	private LocationDto location;
	private CurrentDto current;

	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class LocationDto {
		private String name;
		private String region;
		private String country;
	}
	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class CurrentDto {
		@JsonProperty("temp_c")
		private Double tempC;

		@JsonProperty("feelslike_c")
		private Double feelslikeC;

		private Double humidity; //TODO cambiar

		@JsonProperty("wind_kph")
		private Double windKph;

		@JsonProperty("precip_mm")
		private Double precipMm;

		private Integer cloud;

		@JsonProperty("uv")
		private Double uv;

		private ConditionDto condition;
	}

	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class ConditionDto {
		private String text;
	}
}
