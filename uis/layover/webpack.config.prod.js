/*
 * Copyright (C) 2018 Red Hat, Inc. (jdcasey@commonjava.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
const path = require('path');

module.exports = 
{
    "mode": "production",
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
