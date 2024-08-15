package ru.practicum;

import lombok.Setter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Setter
public class StatClient {
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	private String statServerHost;
	private Integer statServerPort;
	private String hitPath;
	private String statPath;

	public void sendHitToSave(WebClient client,
							  String app,
							  String uri,
							  String ip,
							  LocalDateTime timestamp) {
		HitDto hitToSave = HitDto.builder()
				.app(app)
				.uri(uri)
				.ip(ip)
				.timestamp(timestamp)
				.build();
		WebClient.RequestHeadersSpec<?> request = client.post()
				.uri(uriBuilder -> uriBuilder.scheme("http")
						.host(statServerHost)
						.port(statServerPort)
						.path(hitPath)
						.build()
				)
				.bodyValue(hitToSave)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.header(HttpHeaders.ACCEPT, MediaType.ALL_VALUE);
		request.retrieve().bodyToMono(String.class).block();
	}

	public List<StatDto> getStats(WebClient client,
								  LocalDateTime start,
								  LocalDateTime end,
								  List<String> uris,
								  Boolean unique) {
		WebClient.RequestHeadersSpec<?> request = client.get()
				.uri((uriBuilder -> createUriToGetStats(uriBuilder, start, end, uris, unique)))
				.header(HttpHeaders.ACCEPT, MediaType.ALL_VALUE);
		return request.retrieve().bodyToFlux(StatDto.class).collectList().block();
	}

	private URI createUriToGetStats(UriBuilder uriBuilder,
									LocalDateTime start,
									LocalDateTime end,
									List<String> uris,
									Boolean unique) {

		uriBuilder = uriBuilder.scheme("http")
				.host(statServerHost)
				.port(statServerPort)
				.path(statPath)
				.queryParam("start", start.format(FORMATTER))
				.queryParam("end", end.format(FORMATTER));
		if (uris != null && !uris.isEmpty())
			uriBuilder = uriBuilder.queryParam("uris", uris);
		if (unique != null)
			uriBuilder = uriBuilder.queryParam("unique", unique);
		return uriBuilder.build();
	}
}