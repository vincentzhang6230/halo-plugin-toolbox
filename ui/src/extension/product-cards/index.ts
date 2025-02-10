import {
  type Editor,
  mergeAttributes,
  Node,
  VueNodeViewRenderer,
  ToolboxItem,
} from "@halo-dev/richtext-editor";
import ProductCardView from "./ProductCardView.vue";
import { markRaw } from "vue";
import RiArrowRightSLine from "~icons/ri/arrow-right-s-line";

export const ExtensionProductCard = Node.create({
  name: "product-card",
  
  fakeSelection: true,
  group: "block",
  
  addAttributes() {
    return {
      ...this.parent?.(),
      product: {
        default: "",
        parseHTML(element) {
          return element.getAttribute("product");
        },
        renderHTML(attributes) {
          return { product: attributes.product };
        },
      },
    };
  },
  renderHTML({ HTMLAttributes }) {
    return ["product-card", mergeAttributes(HTMLAttributes)];
  },
  parseHTML() {
      return [{tag: 'product-card'}]
  },
  addNodeView() {
    return VueNodeViewRenderer(ProductCardView);
  },
  addOptions() {
    return {
      getToolboxItems({ editor }: { editor: Editor }) {
        return [
          {
            priority: 100,
            component: markRaw(ToolboxItem),
            props: {
              editor,
              icon: markRaw(RiArrowRightSLine),
              title: "商品卡片",
              action: () => {
                editor
                  .chain()
                  .focus()
                  .insertContent([{ type: "product-card" }])
                  .run();
              },
            },
          },
        ];
      },
    };
  },
});
