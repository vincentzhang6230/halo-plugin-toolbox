<script setup lang="ts">
import {onMounted, ref} from "vue";
import {type ExtensionList, type Product, type ProductQuery, ProductType} from "@/utils/types";
import {axiosInstance} from "@halo-dev/api-client";
import {
  VCard,
  VButton,
  VSpace,
  VPageHeader,
  VLoading,
  VEmpty,
  VEntity,
  VEntityField,
  VDropdownItem,
  IconToolsFill,
  IconAddCircle,
  VAvatar,
  VTag
} from "@halo-dev/components";
import {formatDatetime} from "@/utils/date";
import {useRouter} from "vue-router";

const products = ref<ExtensionList<Product>>()
const productQuery = ref<ProductQuery>({page: 1, pageSize: 20})
const isLoading = ref(true)
const router = useRouter();

const ProductListApi = () => {
  axiosInstance.get("/apis/product.plugin.toolbox.run/v1alpha1/product/-/page", {params: productQuery.value})
    .then((res) => {
      products.value = res.data
      isLoading.value = false
    })
}

onMounted(() => {
  ProductListApi()
});
</script>

<template>
  <VPageHeader title="商品列表">
    <template #icon>
      <IconToolsFill class="mr-2 self-center"></IconToolsFill>
    </template>
    <template #actions>
      <VSpace>
        <VButton type="secondary" @click="router.push({ name: 'ProductAddView', params:{ productId: 'newProduct' } })">
          <template #icon>
            <IconAddCircle class="h-full w-full" />
          </template>
          新建商品
        </VButton>
      </VSpace>
    </template>
  </VPageHeader>
  <div class="m-0 md:m-4">
    <VCard :body-class="['!p-0']">
      <template #header></template>
      
      <VLoading v-if="isLoading" />

      <Transition v-else-if="!products!!.items.length" appear name="fade">
        <VEmpty
          message="当前没有已创建的商品，你可以点击刷新或者创建新的商品"
          title="没有商品"
        >
          <template #actions>
            <VSpace>
              <VButton @click="ProductListApi">刷新</VButton>
              <VButton tpye="secondary" @click="router.push({ name: 'ProductAddView', params:{ productId: 'newProduct' } })">
                <template #icon>
                  <IconAddCircle class="h-full w-full" />
                </template>
                新建商品
              </VButton>
            </VSpace>
          </template>
        </VEmpty>
      </Transition>

      <Transition v-else appear name="fade">
        <ul class="box-border h-full w-full divide-y divide-gray-100" role="list">
          <li v-for="(product, index) in products?.items" :key="index">
            <VEntity>
              <template #start>
                <VEntityField>
                  <template #description>
                    <VAvatar
                    :alt="product.spec.productName"
                    :src="product.spec.image"
                    size="sm">
                    </VAvatar>
                  </template>
                </VEntityField>
                <VEntityField :title="product.spec.productName" :description="product.spec.description"></VEntityField>
                <VEntityField 
                  v-if="product.spec.shipType == 3"
                  title="销售情况">
                  <template #description>
                    <div class="flex flex-col gap-1.5">
                      <VSpace class="flex-wrap !gap-y-1">
                        <span class="text-xs text-gray-500">
                          销售量 {{ product.spec.sales }}
                        </span>
                        <span class="text-xs text-gray-500">
                          总量 {{ product.spec.total }}
                        </span>
                      </VSpace>
                    </div>
                  </template>
                </VEntityField>
              </template>
              <template #end>
                <VEntityField>
                  <template #description>
                    <VTag>
                      <template #leftIcon>
                        <IconAddCircle />
                      </template>
                      {{ProductType(product.spec.shipType)}}
                    </VTag>
                  </template>
                </VEntityField>
                <VEntityField>
                  <template #description>
                    <span class="truncate text-xs tabular-nums text-gray-500">
                      {{ formatDatetime(product.metadata.creationTimestamp) }}
                    </span>
                  </template>
                </VEntityField>
              </template>
              <template #dropdownItems>
                <VDropdownItem @click="router.push({ name: 'ProductAddView', params:{ productId: product.metadata.name } })">修改</VDropdownItem>
              </template>
            </VEntity>
          </li>
        </ul>
      </Transition>
    </VCard>
  </div>
</template>

<style lang="scss" scoped>
</style>
