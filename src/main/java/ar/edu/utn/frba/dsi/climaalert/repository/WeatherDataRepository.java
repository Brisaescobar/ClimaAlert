package ar.edu.utn.frba.dsi.climaalert.repository;

import org.springframework.stereotype.Repository;
import javax.swing.text.html.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// repositorio JPA para el historial climatico
@Repository
public interface WeatherDataRepository extends jpaRepository<WeatherData, Long> {

	Optional<WeatherData> findTopByOrderByRegisteredAtDesc();

}
