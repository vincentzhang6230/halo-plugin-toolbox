package run.halo.toolbox;

import org.springframework.stereotype.Component;
import run.halo.app.extension.SchemeManager;
import run.halo.app.extension.index.IndexSpec;
import run.halo.app.plugin.BasePlugin;
import run.halo.app.plugin.PluginContext;
import run.halo.toolbox.extension.Product;
import static run.halo.app.extension.index.IndexAttributeFactory.simpleAttribute;


/**
 * <p>Plugin main class to manage the lifecycle of the plugin.</p>
 * <p>This class must be public and have a public constructor.</p>
 * <p>Only one main class extending {@link BasePlugin} is allowed per plugin.</p>
 *
 * @author guqing
 * @since 1.0.0
 */
@Component
public class StarterPlugin extends BasePlugin {

    private final SchemeManager schemaManager;

    public StarterPlugin(PluginContext pluginContext, SchemeManager schemaManager) {
        super(pluginContext);
        this.schemaManager = schemaManager;
    }

    @Override
    public void start() {
        schemaManager.register(Product.class, indexSpecs -> {
            indexSpecs.add(new IndexSpec()
                .setName("spec.productName")
                .setIndexFunc(simpleAttribute(Product.class, product -> product.getSpec().getProductName())));
            indexSpecs.add(new IndexSpec()
                .setName("spec.shipType")
                .setIndexFunc(simpleAttribute(Product.class, product -> String.valueOf(
                    product.getSpec().getShipType()))));
        });
    }

    @Override
    public void stop() {
        System.out.println("插件停止！");
    }
}
