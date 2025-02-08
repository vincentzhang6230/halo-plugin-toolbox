package run.halo.toolbox.query;

import org.springframework.web.server.ServerWebExchange;
import run.halo.app.extension.ListOptions;
import run.halo.app.extension.router.SortableRequest;

import java.util.Optional;

import static run.halo.app.extension.index.query.QueryFactory.equal;


public class ProductQuery extends SortableRequest {

    public ProductQuery(ServerWebExchange exchange) {
        super(exchange);
    }

    public String getProductName() {
        return queryParams.getFirst("productName");
    }

    public String getShipType() {
        return queryParams.getFirst("shipType");
    }

    @Override
    public ListOptions toListOptions() {
        var builder = ListOptions.builder(super.toListOptions());

        Optional.ofNullable(getProductName())
            .ifPresent(productName -> builder.andQuery(equal("spec.productName", productName)));

        Optional.ofNullable(getShipType())
            .ifPresent(shipType -> builder.andQuery(equal("spec.shipType", shipType)));

        return builder.build();
    }
}
