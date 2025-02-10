import { defineCustomElement } from 'vue'
import ProductInfo from './components/ProductInfoElement.ce.vue'


const TestElement = defineCustomElement(ProductInfo)

customElements.define('product-card', TestElement)
