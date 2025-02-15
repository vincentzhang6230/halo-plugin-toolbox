package run.halo.toolbox.service.impl;

import cn.hutool.core.util.ReUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import run.halo.app.content.PostContentService;
import run.halo.app.extension.ListOptions;
import run.halo.app.extension.ListResult;
import run.halo.app.extension.ReactiveExtensionClient;
import run.halo.app.extension.router.selector.FieldSelector;
import run.halo.toolbox.extension.Product;
import run.halo.toolbox.query.ProductQuery;
import run.halo.toolbox.service.ProductService;
import run.halo.toolbox.utils.RegexUtils;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ReactiveExtensionClient client;
    private final PostContentService postContentService;

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

    @Override
    public Mono<Product> get(String productId) {
        return client.fetch(Product.class, productId).flatMap(product ->
            switch (product.getSpec().getShipType()) {
            case 0: yield isFree(product);
            case 1: yield isPassword(product);
            case 2: yield isLogin(product);
            case 3: yield isPaid(product);
            default: yield Mono.empty();
        });
    }

    @Override
    public Mono<String> password(String productId, String password) {
        return client.fetch(Product.class, productId).flatMap(product -> {
            if (!product.getSpec().getPassword().equals(password)) {
                return Mono.just("Password is wrong");
            }
            return Mono.just(product.getSpec().getContent());
        });
    }

    @Override
    public Mono<Boolean> getShipType(String postId) {
        return postContentService.getReleaseContent(postId).flatMap(contentWrapper -> {

            boolean contains = ReUtil.contains(RegexUtils.pRegex, contentWrapper.getContent());
            return Mono.just(contains);
            }
        );
    }

    private Mono<Product> isFree(Product product) {
        return Mono.just(product).map(p -> {
            p.getSpec().setContent("Product is free");
            return p;
        });
    }

    private Mono<Product> isPassword(Product product) {
        return Mono.just(product).map(p -> {
            p.getSpec().setContent("Product is password");
            return p;
        });
    }

    private Mono<Product> isLogin(Product product) {
        return ReactiveSecurityContextHolder.getContext()
            .flatMap(securityContext -> {
                Authentication authentication = securityContext.getAuthentication();
                if (authentication.getPrincipal() instanceof String) {
                    product.getSpec().setContent("User is not login");
                }
                return Mono.just(product);
            });
    }

    private Mono<Product> isPaid(Product product) {
        return ReactiveSecurityContextHolder.getContext()
            .flatMap(securityContext -> {
                Authentication authentication = securityContext.getAuthentication();
                if (authentication.getPrincipal() instanceof String) {
                    product.getSpec().setContent("User is not login");
                }
                return Mono.just(product);
            });
    }
}
