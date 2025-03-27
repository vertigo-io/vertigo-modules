import { fileURLToPath, URL } from 'url'

const path = require('path')
import vue from '@vitejs/plugin-vue'
import { defineConfig } from 'vite'
import { viteExternalsPlugin } from 'vite-plugin-externals'

// https://vitejs.dev/config/
export default defineConfig({
  build: {
    sourcemap: true,
    lib: {
      entry: path.resolve(__dirname, 'src/main.js'),
      name: 'VertigoOrchestraUi',
      fileName: (format) => `vertigo-orchestra-ui.${format}.js`
    },
    rollupOptions: {
      // overwrite default .html entry
      input: '/src/main.js',
      // make sure to externalize deps that shouldn't be bundled
      // into your library

      external: ['vue', 'quasar'],
      output: {
        // Provide global variables to use in the UMD build
        // for externalized deps
        globals: {
          vue: 'Vue',
          quasar: 'Quasar'
        }
      }
    }
  },
  server: { },
  define: {
    "process.env": process.env
  },
  plugins: [
    vue(),
    viteExternalsPlugin({
      vue: 'Vue',
      quasar: 'Quasar'
    }),],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  }
})
