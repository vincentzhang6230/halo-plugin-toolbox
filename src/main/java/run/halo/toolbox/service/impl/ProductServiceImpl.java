package run.halo.toolbox.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import run.halo.app.extension.ListOptions;
import run.halo.app.extension.ListResult;
import run.halo.app.extension.ReactiveExtensionClient;
import run.halo.app.extension.router.selector.FieldSelector;
import run.halo.toolbox.extension.Product;
import run.halo.toolbox.query.ProductQuery;
import run.halo.toolbox.service.ProductService;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ReactiveExtensionClient client;

    @Override
    public Mono<ListResult<Product>> page(ServerWebExchange exchange) {
        var query = new ProductQuery(exchange);
        return client.listBy(Product.class, query.toListOptions(), query.toPageRequest());
    }

    @Override
    public Mono<List<Product>> list() {
        var listOptions = new ListOptions();
        listOptions.setFieldSelector(FieldSelector.all());
        return client.listAll(Product.class, listOptions, Sort.unsorted()).collectList();
    }
}
