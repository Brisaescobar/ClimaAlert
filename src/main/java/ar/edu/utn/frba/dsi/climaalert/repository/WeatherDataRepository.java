package ar.edu.utn.frba.dsi.climaalert.repository;

import ar.edu.utn.frba.dsi.climaalert.model.WeatherData;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// repositorio JPA para el historial climatico
@Repository
public interface WeatherDataRepository extends JpaRepository<WeatherData, Long> {
	Optional<WeatherData> findTopByOrderByRegisteredAtDesc();
}
