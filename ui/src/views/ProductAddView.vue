<script setup lang="ts">
import {
  VCard,
  VButton,
  VSpace,
  VPageHeader,
  IconToolsFill,
  IconArrowLeft,
  Toast,
} from "@halo-dev/components";

import { useRouteParams } from "@vueuse/router";
import {onMounted, ref} from "vue";
import type {Product, ProductSpec} from "@/utils/types";
import {axiosInstance} from "@halo-dev/api-client";
import {useRouter} from "vue-router";

const productId = useRouteParams("productId");
const router = useRouter();

const product = ref<Product>()
const spec = ref<ProductSpec>({
  content: "",
  description: "",
  image: "",
  productName: "",
  shipType: 0
})

onMounted(() => {
  if (productId.value !== "newProduct") {
    ProductData(productId.value as string)
  }
})
const shipTypeOptions = [
  { label: "免费商品", value: 0 },
  { label: "密码查看商品", value: 1 },
  { label: "登录查看商品", value: 2 },
  { label: "付费商品", value: 3 },
];

const ProductData = (id : string) => {
  axiosInstance.get(`/apis/product.plugin.halo.run/v1alpha1/products/${id}`).then((res) => {
    product.value = res.data
    spec.value = res.data.spec
  })
}
const handleProduct = () => {
  if (productId.value === "newProduct") {
    axiosInstance.post(`/apis/product.plugin.halo.run/v1alpha1/products`, {
      metadata: {
        generateName: "product-"
      },
      spec: spec.value,
      apiVersion: "product.plugin.halo.run/v1alpha1",
      kind: "Product"
    })
      .then(() => {
        Toast.success("添加成功")
        router.go(-1)
      })
  } else {
    product.value!!.spec = spec.value
    axiosInstance.put(`/apis/product.plugin.halo.run/v1alpha1/products/${productId.value}`, product.value)
      .then(() => {
        Toast.success("更新成功")
        router.go(-1)
      })
  }
}
</script>

<template>
  <VPageHeader title="添加商品">
    <template #icon>
      <IconToolsFill class="mr-2 self-center"></IconToolsFill>
    </template>
    <template #actions>
      <VSpace>
        <VButton type="secondary" @click="router.go(-1)">
          <template #icon>
            <IconArrowLeft class="h-full w-full" />
          </template>
          返回
        </VButton>
      </VSpace>
    </template>
  </VPageHeader>
  <div class="m-0 md:m-4">
    <VCard :body-class="['!p-0']">
      <Transition appear name="fade">
        <FormKit
          id="product-form"
          name="product-form"
          type="form"
          :form-class="'p-4'"
        >
          <FormKit
            id="name"
            name="name"
            v-model="spec.productName"
            label="商品名称"
            type="text"
            validation="required"
          ></FormKit>
          <FormKit
            id="description"
            name="description"
            v-model="spec.description"
            label="商品描述"
            type="text"
            validation="required"
          ></FormKit>
          <FormKit
            id="image"
            name="image"
            v-model="spec.image"
            label="商品图片"
            type="attachment"
            validation="required"
          ></FormKit>
          <FormKit
            id="shipType"
            v-model="spec.shipType"
            label="商品分类"
            type="select"
            name="shipType"
            :options="shipTypeOptions"
          ></FormKit>
          <FormKit
            v-if="spec.shipType === 1"
            id="isPassword"
            name="isPassword"
            v-model="spec.password"
            label="设置密码"
            type="text"
          ></FormKit>
          <FormKit
            v-if="spec.shipType === 3"
            id="sales"
            name="sales"
            v-model="spec.sales"
            label="设置销量"
            type="text"
          ></FormKit>
          <FormKit
            v-if="spec.shipType === 3"
            id="total"
            name="total"
            v-model="spec.total"
            label="设置总量"
            type="text"
          ></FormKit>
          <FormKit
            id="content"
            name="content"
            v-model="spec.content"
            label="商品内容"
            type="text"
            validation="required"
          ></FormKit>
        </FormKit>
      </Transition>
      <template #footer>
        <VSpace>
          <VButton type="secondary" @click="handleProduct"> 确定按钮</VButton>
        </VSpace>
      </template>
    </VCard>
  </div>
</template>

<style scoped lang="scss">

</style>
