package ru.practicum;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

public class StatClient {
	@Value("${stat-server.url}")
	private static String statServerUrl;
	@Value("${stat-server.url.path.hit}")
	private static String hitPath;
	@Value("${stat-server.url.path.stat}")
	private static String statPath;

	public static Mono<?> sendHitToSave(WebClient client,
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
				.uri(statServerUrl + hitPath)
				.bodyValue(hitToSave)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.header(HttpHeaders.ACCEPT, MediaType.ALL_VALUE);
		return handleResponseSpec(request.retrieve());
	}

	public static Mono<?> getStats(WebClient client,
								   LocalDateTime start,
								   LocalDateTime end,
								   List<String> uris,
								   Boolean unique) {
		WebClient.RequestHeadersSpec<?> request = client.get()
				.uri(uriBuilder -> createUriToGetStats(uriBuilder, start, end, uris, unique))
				.header(HttpHeaders.ACCEPT, MediaType.ALL_VALUE);
		return handleResponseSpec(request.retrieve());
	}

	private static Mono<?> handleResponseSpec(WebClient.ResponseSpec responseSpec) {
		return responseSpec.bodyToMono(String.class);
	}

	private static URI createUriToGetStats(UriBuilder uriBuilder,
										   LocalDateTime start,
										   LocalDateTime end,
										   List<String> uris,
										   Boolean unique) {
		uriBuilder = uriBuilder.path(statServerUrl + statPath)
				.queryParam("start", start)
				.queryParam("end", end);
		if (uris != null && !uris.isEmpty())
			uriBuilder = uriBuilder.queryParam("uris", uris);
		if (unique != null)
			uriBuilder = uriBuilder.queryParam("unique", unique);
		return uriBuilder.build();
	}
}