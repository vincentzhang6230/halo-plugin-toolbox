package run.halo.toolbox.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import run.halo.app.extension.ListResult;
import run.halo.app.plugin.ApiVersion;
import run.halo.toolbox.extension.Product;
import run.halo.toolbox.service.ProductService;
import java.util.List;

@ApiVersion("product.plugin.toolbox.run/v1alpha1")
@RequestMapping("product")
@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("-/page")
    public Mono<ListResult<Product>> page(ServerWebExchange exchange) {
        return productService.page(exchange);
    }

    @GetMapping("-/list")
    public Mono<List<Product>> list() {
        return productService.list();
    }
}
