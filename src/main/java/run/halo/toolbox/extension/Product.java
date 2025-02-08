package run.halo.toolbox.extension;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import run.halo.app.extension.AbstractExtension;
import run.halo.app.extension.GVK;

/**
 * 商品自定义模型
 * @author 小十一
 * */
@Data
@EqualsAndHashCode(callSuper = true)
@GVK(kind = "Product", group = "product.plugin.halo.run",
    version = "v1alpha1", singular = "product", plural = "products")
public class Product extends AbstractExtension {

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private Spec spec;

    @Data
    @Schema(name = "ProductSpec")
    public static class Spec {
        /**
         * 商品名称
         * */
        private String productName;
        /**
         * 商品描述
         * */
        private String description;
        /**
         * 商品价格
         * */
        private String price;
        /**
         * 商品图片
         * */
        private String image;

        /**
         * 商品类型（0-免费, 1-密码, 2-登录, 3-付费）
         */
        private Integer shipType;

        /**
         * 密码查看
         */
        private String password;

        /**
         * 商品内容
         */
        private String content;

        /**
         * 总量
         */
        private Integer total;

        /**
         * 销量
         */
        private Integer sales;
    }
}
