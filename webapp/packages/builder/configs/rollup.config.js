const path = require('path');
const { nodeResolve } = require('@rollup/plugin-node-resolve');
const { babel } = require('@rollup/plugin-babel');
const commonjs = require('@rollup/plugin-commonjs');
const postcssPlugin = require('rollup-plugin-postcss');
const peerDepsExternal = require('rollup-plugin-peer-deps-external');
const includePaths = require('rollup-plugin-includepaths');
const typescriptPlugin = require('rollup-plugin-typescript2');
const reshadowLoader = require('./reshadow-loader');

const includePathOptions = {
  include: {},
  paths: ['src'],
  external: [],
  extensions: ['.ts', '.tsx', '.js']
};

function configBuilder() {

  return {
    input: 'src/index.ts',
    output: {
      dir: 'dist',
      format: 'esm',
      sourcemap: true,
    },
    external: [
      // externals beyond peer dependencies - common dependencies of the whole application
      'mobx',
      // todo check that @dbeaver/core/blocks provide wrapper for go-split
      // todo add go-split to @dbeaver/core dependencies and remove it from here
      'go-split',
      'mobx-react',
      'react',
      'react-dom',
      // todo add reakit to @dbeaver/core dependencies and remove it from here
      // todo be sure that @dbeaver/core provides all necessary wrappers around reakit
      'reakit',
      // todo magic. It nested dependency of reakit and should not be mentioned here
      // todo but by unknown reasons without this build fails
      // todo should be removed when reakit will be incorporated into @dbeaver/core
      'body-scroll-lock',
      'reshadow',
      'rxjs',
    ],
    onwarn(warning, warn) {
      // hides some warning drom ag-grid-plugin todo to investigate later
      if (warning.code === 'THIS_IS_UNDEFINED') return;
      warn(warning); // this requires Rollup 0.46
    },
    plugins: [
      includePaths(includePathOptions),
      nodeResolve({
        preferBuiltins: false, // fix crypto import in core/utils/uuid library. todo replace uuid library with simple uuid generation
      }),
      typescriptPlugin({
        tsconfig: 'tsconfig.json',
        useTsconfigDeclarationDir: true,
      }),
      babel({
        exclude: ['../../node_modules/**', 'node_modules/**'], // exclude package node_modules and lerna node_modules
        extensions: ['.js', '.ts', '.tsx'],
        babelHelpers: 'bundled',
        configFile: path.join(__dirname, 'babel-plugin.config.js'),
      }),
      commonjs({
        sourceMap: false,
        exclude: [
          /**
           * todo Now all plugins after build contain reshadow source code
           * todo Expected next line should remove reshadow code from build artifact but it doesnt work.
           * todo It was found that reshadow code disappear if to comment 'reshadow/babel' in babel-plugin.config.js
           * todo So it is reshadow/babel plugin inject code into the artifact. Further investigations required.
           */
          'node_modules/@reshadow/**',
          'node_modules/react-dom',
          'node_modules/react-dom/server',
          'node_modules/react',
        ],
      }),
      postcssPlugin({
        extract: false,
        // enable CSS modules for .module.css .module.scss files
        autoModules: true,
        // modules: {
        //   generateScopedName: "[local]___[hash:base64:5]"
        // },
        inject: true,
        use: [
          [
            'reshadow-loader',
            {}
          ],
          [
            'sass',
            {
              includePaths: ['node_modules', '../../node_modules'],
            }
          ],
        ],
        loaders: [reshadowLoader],
        plugins: [
          require('postcss-preset-env')({stage: 0}),
          require('postcss-discard-comments'),
        ]
      }),
      peerDepsExternal(),
    ],
  };

}

module.exports = configBuilder();
