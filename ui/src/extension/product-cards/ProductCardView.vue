<script setup lang="ts">
import type {NodeViewProps} from "@halo-dev/richtext-editor";
import { NodeViewWrapper } from "@halo-dev/richtext-editor";
import {computed, onMounted, ref, watch} from "vue";
import {type Product, ProductType} from "@/utils/types";
import { axiosInstance } from "@halo-dev/api-client";

const props = defineProps<NodeViewProps>();

const productList = ref<[Product]>()
const product = ref<Product>()

const productId = computed({
  get: () => {
    return props.node.attrs.product
  }, 
  set: (str: string) => {
    props.updateAttributes({product: str})
  },
});
onMounted(() => {
  if (productId.value) {
    ProductData(productId.value)
  } else {
    ProductListData()
  }
})
watch(productId, (newVal) => {
  if (newVal != '') {
    ProductData(newVal)
  } else {
    ProductListData()
  }
})
const ProductListData = () => {
  axiosInstance.get("/apis/product.plugin.toolbox.run/v1alpha1/product/-/list").then((res) => {
    productList.value = res.data
  })
}
const ProductData = (id : string) => {
  axiosInstance.get(`/apis/product.plugin.toolbox.run/v1alpha1/product/${id}`).then((res) => {
    product.value = res.data
  })
}
const closeDetail = () => {
  product.value = undefined
  productId.value = ''
}

</script>

<template>
  <!-- 卡片容器 -->
  <node-view-wrapper as="div" class="max-w-7xl mx-auto p-8">
    <!-- 商品详情模态框 -->
    <div v-if="product" class="flex items-center justify-center p-4">
      <div class="bg-white rounded-xl max-w-4xl w-full max-h-[90vh] overflow-hidden flex flex-col md:flex-row">
        <!-- 关闭按钮 -->
        <button
          @click="closeDetail"
          class="absolute top-4 right-4 text-gray-500 hover:text-gray-700 transition-colors"
        >
          ✕
        </button>

        <!-- 图片区域 -->
        <div class="md:w-1/2 bg-gray-100 flex items-center justify-center p-6 w-40 h-40">
          <img
            :src="product.spec.image"
            alt="商品图片"
            class="w-full object-cover rounded-t-lg h-40"
          >
        </div>

        <!-- 信息表单区域 -->
        <div class="md:w-1/2 p-8 overflow-y-auto">
          <h2 class="text-2xl font-bold mb-4">{{ product.spec.productName }}</h2>
          <div class="space-y-4 text-gray-600">
            <!-- 商品类型 -->
            <div>
              <label class="text-gray-500 text-sm">商品类型</label>
              <div class="mt-1">
                <span class="px-2 py-1 bg-green-100 text-green-800 rounded-lg text-sm">
                  {{ ProductType(product.spec.shipType) }}
                </span>
              </div>
            </div>

            <!-- 价格（条件显示） -->
            <div v-if="product.spec.shipType === 3">
              <label class="text-gray-500 text-sm">价格</label>
              <div class="mt-1 font-bold text-red-600">
                ¥{{ product.spec.price }}
              </div>
            </div>

            <!-- 销售信息 -->
            <div class="grid grid-cols-2 gap-4">
              <div>
                <label class="text-gray-500 text-sm">已售数量</label>
                <div class="mt-1 font-medium">{{ product.spec.sales }}</div>
              </div>
              <div>
                <label class="text-gray-500 text-sm">商品总量</label>
                <div class="mt-1 font-medium">{{ product.spec.total }}</div>
              </div>
            </div>

            <!-- 商品描述 -->
            <div>
              <label class="text-gray-500 text-sm">商品描述</label>
              <div class="mt-1 whitespace-pre-line">{{ product.spec.description }}</div>
            </div>

            <!-- 商品内容 -->
            <div>
              <label class="text-gray-500 text-sm">商品内容</label>
              <div class="mt-1 whitespace-pre-line bg-gray-50 p-3 rounded-lg">
                {{ product.spec.content }}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div v-else class="flex flex-wrap gap-6">
      <!-- 单个卡片 -->
      <article v-for="p in productList"
               :key="p.metadata.name"
               @click="productId = p.metadata.name"
               class="flex-1 basis-[calc(50%-12px)] md:basis-[calc(33.333%-16px)] lg:basis-[calc(25%-18px)] min-w-[300px]"
      >
        <div class="bg-white rounded-lg shadow-md hover:shadow-lg transition-shadow duration-300">
          <!-- 卡片图片 -->
          <img
            :src="p.spec.image"
            alt="featured image"
            class="w-full object-cover rounded-t-lg h-40"
          >
          <!-- 卡片内容 -->
          <div class="p-6">
            <!-- 标题 -->
            <h2 class="text-xl font-bold mb-2 text-gray-800 hover:text-blue-600 cursor-pointer">
              {{ p.spec.productName }}
            </h2>
            <!-- 摘要 -->
            <p class="text-gray-600 mb-4 line-clamp-3">
              {{ p.spec.description }}
            </p>
            <!-- 元信息 -->
            <div class="flex justify-between items-center text-sm text-gray-500">
              <span>{{ p.spec.total }}</span>
              <div class="flex items-center">
                <span class="mr-2">👤</span>
                {{ p.spec.sales }}
              </div>
            </div>
          </div>
        </div>
      </article>
    </div>
  </node-view-wrapper>
</template>

<style scoped></style>
