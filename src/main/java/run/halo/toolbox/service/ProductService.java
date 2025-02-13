package run.halo.toolbox.service;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import run.halo.app.extension.ListResult;
import run.halo.toolbox.extension.Product;
import java.util.List;

public interface ProductService {

    Mono<ListResult<Product>> page(ServerWebExchange exchange);

    Mono<List<Product>> list();

    Mono<Product> get(String productId);

    Mono<String> password(String productId, String password);

    Mono<Boolean> getShipType(String postId);
}
