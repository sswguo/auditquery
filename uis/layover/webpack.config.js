const path = require('path');

module.exports = 
{
    "mode": "development",
    "entry": "./app/js/app.js",
    "output": {
        "path": path.resolve(__dirname, 'dist'),
        "filename": "bundle.js"
    },
    "module": {
        "rules": [
            {
                "test": /\.(js|jsx)$/,
                "exclude": /node_modules/,
                "loader": "babel-loader",
                "options": {
                  "plugins": ["@babel/plugin-proposal-class-properties"],
                  "presets": ["@babel/preset-react"]
                }
            },
            {
                test: /\.css$/,
                use: ['style-loader', 'css-loader'],
              }
        ]
    }
}
