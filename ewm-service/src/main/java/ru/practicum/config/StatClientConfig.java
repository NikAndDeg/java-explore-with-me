package ru.practicum.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.StatClient;

@Configuration
public class StatClientConfig {
	@Value("${server.stat.url.host}")
	private String statServerHost;
	@Value("${server.stat.url.port}")
	private Integer statServerPort;
	@Value("${server.stat.url.path.hit}")
	private String hitPath;
	@Value("${server.stat.url.path.stat}")
	private String statPath;

	@Bean
	public StatClient getStatClient() {
		StatClient statClient = new StatClient();
		statClient.setStatServerHost(statServerHost);
		statClient.setStatServerPort(statServerPort);
		statClient.setHitPath(hitPath);
		statClient.setStatPath(statPath);
		return statClient;
	}
}
