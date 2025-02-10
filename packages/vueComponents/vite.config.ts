import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import UnoCSS from 'unocss/vite'
import dts from 'vite-plugin-dts';
import { viteStaticCopy as StaticCopy } from 'vite-plugin-static-copy';
import {fileURLToPath} from "url";

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
          vue({customElement: true, template: {compilerOptions:{isCustomElement:(tag)=> tag.includes('-')}} }), UnoCSS({mode: 'shadow-dom'}), dts(),
      StaticCopy({
          targets: [
                  {
                      src: ['./dist/product-card.iife.js'],
                      dest: fileURLToPath(new URL('../../src/main/resources/static', import.meta.url)),
                  },
          ]
      })
  ],
    build: {
    lib: {
      entry: 'src/main.ts',
      name: 'product-card',
      fileName: 'product-card',
      formats: ['iife', 'es'],
    },
    emptyOutDir: true,
    rollupOptions: {
      output: {
        extend: true,
      },
    },
  },
  define: {
    "process.env.NODE_ENV": JSON.stringify(process.env.NODE_ENV),
  },
})
