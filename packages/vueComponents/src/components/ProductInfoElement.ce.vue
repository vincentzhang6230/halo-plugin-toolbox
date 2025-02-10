<script setup lang="ts">

import {Product, ProductType} from "../types";
import {onMounted, ref} from "vue";

const props = defineProps({
    product:String,
})
const product = ref<Product>()

onMounted(() => {
    ProductData(props.product as string)
})

const ProductData = async (id : string) => {
    await fetch(`/apis/product.plugin.toolbox.run/v1alpha1/product/${id}`).then((res) => {
        res.ok && res.json().then(data => {
            product.value = data
        })
    })
}

</script>

<template>
    <div v-if="product" class="max-w-7xl mx-auto p-8">
        <div class="bg-white rounded-xl max-w-4xl w-full max-h-[90vh] overflow-hidden flex flex-col md:flex-row">
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

</template>

<style>
@unocss-placeholder
</style>
