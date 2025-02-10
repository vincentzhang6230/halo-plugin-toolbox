export interface Metadata {
  name: string;
  labels?: {
    [key: string]: string;
  } | null;
  annotations?: {
    [key: string]: string;
  } | null;
  version?: number | null;
  creationTimestamp?: string | null;
  deletionTimestamp?: string | null;
}

export interface ProductSpec {
    productName: string;
    description: string;
    price: string;
    image: string;
    shipType: number;
    password: string;
    content: string;
    total: number;
    sales: number;
}

/**
 * 商品自定义模型
 */
export interface Product {
  spec: ProductSpec;
  apiVersion: "product.plugin.halo.run/v1alpha1";
  kind: "Product";
  metadata: Metadata;
}

/**
 * 自定义模型生成 list API 所对应的类型
 */
export interface ExtensionList<T> {
  page: number;
  size: number;
  total: number;
  items: Array<T>;
  first: boolean;
  last: boolean;
  hasNext: boolean;
  hasPrevious: boolean;
  totalPages: number;
}

export const ProductType = (type: number) => {
    switch (type) {
        case 0:
            return '免费商品'
        case 1:
            return '密码查看商品'
        case 2:
            return '登录查看商品'
        case 3:
            return '登录查看商品'
        default:
            return '未知商品'
    }
}
