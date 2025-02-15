import { definePlugin } from "@halo-dev/console-shared";
import ProductView from "./views/ProductView.vue";
import ProductAddView from "./views/ProductAddView.vue";
import { IconPlug } from "@halo-dev/components";
import {markRaw, type Ref} from "vue";
import {ExtensionProductCard} from "@/extension/product-cards";
import type {ListedPost} from "@halo-dev/api-client";
import ShipTypeTag from "./extension/ShipTypeTag.vue";

export default definePlugin({
  components: {},
  routes: [
    {
      parentName: "Root",
      route: {
        path: "/product",
        name: "ProductRoot",
        meta: {
          title: "商品管理",
          searchable: true,
          menu: {
            name: "商品管理",
            group: "商品",
            icon: markRaw(IconPlug),
            priority: 0,
          },
        },
        children: [
          {
            path: "",
            name: "Product",
            component: ProductView,
          },
          {
            path: "product-add-view/:productId",
            name: "ProductAddView",
            component: ProductAddView,
            meta: {
              title: "新增商品",
              hideFooter: true,
              permissions: ["*"],
            },
          },
        ]
      },
    },
  ],
  extensionPoints: {
    "default:editor:extension:create": () => {
      return [ExtensionProductCard]
    },
    "post:list-item:field:create": (post: Ref<ListedPost>) => {
      return [
        {
          priority: 0,
          position: "end",
          component: markRaw(ShipTypeTag),
          props: {
            p: post.value.post.metadata.name
          },
          permissions:[],
          hidden: false
        }
      ]
    }
  },
});
