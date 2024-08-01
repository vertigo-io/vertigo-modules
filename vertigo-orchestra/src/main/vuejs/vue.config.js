module.exports = {
  configureWebpack: {
    output: {
      library: "VertigoOrchestraUi", // Add this line to expose the library in the devServer
      libraryTarget: "umd",
    },
    devServer: {
      headers: { "Access-Control-Allow-Origin": "*" },
      https: true,
    },
    externals: {
      quasar: {
        commonjs: "quasar",
        commonjs2: "quasar",
        root: "Quasar",
      },
      vue: {
        commonjs: "vue",
        commonjs2: "vue",
        root: "Vue",
      },
    },
  },
};
