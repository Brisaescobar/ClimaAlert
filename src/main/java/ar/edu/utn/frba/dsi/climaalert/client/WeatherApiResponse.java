package ar.edu.utn.frba.dsi.climaalert.client;

import com.fasterxml.jackson.annotation.JsonProperty;

// "mapeo" con json para la respuesta del endpoint

public class WeatherApiResponse {
	private LocationDto location;
	private currentDto current;

	public static class LocationDto {
		private String name;
		private String region;
		private String country;
	}

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

	public static class ConditionDto {
		private String text;
	}
}
