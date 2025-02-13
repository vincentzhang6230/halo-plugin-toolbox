<script setup lang="ts">

import {axiosInstance} from "@halo-dev/api-client";
import {onMounted, ref} from "vue";
import {type Product, ProductType} from "@/utils/types";
import { VTag} from "@halo-dev/components";

const props = defineProps({
  p: {
    type: String,
    default: ''
  }
});
console.log(props.p)
onMounted(() => {
  ProductData()
})
const isProduct = ref(false)

const ProductData = () => {
  axiosInstance.get(`/apis/product.plugin.toolbox.run/v1alpha1/product/-/type`,{params: {postId: props.p}}).then((res) => {
    isProduct.value = res.data
  })
}

</script>

<template>
  <VTag v-if="isProduct" theme="primary">
    商品
  </VTag>
</template>

<style scoped>

</style>
