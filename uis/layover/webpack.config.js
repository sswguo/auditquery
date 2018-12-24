const path = require('path');

module.exports = 
{
    "mode": "development",
    "entry": "./app/js/app.js",
    "output": {
        "path": path.resolve(__dirname, 'app/js/dist'),
        "filename": "bundle.js"
    },
    "module": {
        "rules": [
            {
                "test": /\.(js|jsx)$/,
                "exclude": /node_modules/,
                "loader": "babel-loader",
                "options": {
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
