package br.dev.guisleri.health;

import br.dev.guisleri.model.Jogo;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;

@Readiness
@ApplicationScoped
public class DatabaseHealthCheck implements HealthCheck {
    @Override
    public HealthCheckResponse call() {
        try {
            long total = Jogo.count();
            return HealthCheckResponse.named("DatabaseHealthCheck")
                    .up()
                    .withData("jogos_cadastrados", total)
                    .build();
        } catch (Exception e) {
            return HealthCheckResponse.named("database")
                    .down()
                    .withData("erro", e.getMessage())
                    .build();
        }
    }
}
