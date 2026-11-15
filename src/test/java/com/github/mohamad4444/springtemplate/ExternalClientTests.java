package com.github.mohamad4444.springtemplate;

import com.github.mohamad4444.springtemplate.config.HttpClientConfig;
import com.github.mohamad4444.springtemplate.service.PokemonClientService;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.http.MediaType;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.assertj.core.api.Assertions.*;

class ExternalClientTests {
    @Test void nativeHttpInterfaceUsesBookDto() {
        var builder = RestClient.builder();
        var server = MockRestServiceServer.bindTo(builder).build();
        var client = new HttpClientConfig().bookServiceClient(builder, "https://books.example");
        server.expect(requestTo("https://books.example/books/7")).andRespond(withSuccess(
                "{\"id\":7,\"title\":\"Spring\",\"author\":\"Example\"}", MediaType.APPLICATION_JSON));
        assertThat(client.getBookById(7L).title()).isEqualTo("Spring");
        server.verify();
    }
    @Test void rejectsInvalidExternalResourceNamesBeforeCallingNetwork() {
        var client = new PokemonClientService(RestClient.builder(), "https://pokemon.example");
        assertThatThrownBy(() -> client.getPokeResponse("../admin")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> client.getPokeResponse(null)).isInstanceOf(IllegalArgumentException.class);
    }
}
