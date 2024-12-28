const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  configureWebpack: {
    mode: 'production'
  }
  // publicPath: '/backend-1.0-SNAPSHOT/',
  // publicPath: '/',
})

