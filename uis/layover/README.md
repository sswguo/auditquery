# This is standalone ui part of auditquery with reactjs framework

## Set up project

* Need to install node & npm: `sudo dnf install node -y`

## Install project dependencies

* Download whole project  
* Use npm to install deps: `npm ci`  

## Generate test data for mock rest service

* Run "npm run ts-node ./mock/data/GenFakeData.ts"

## Start and see project result in dev mode

* Run `npm run build` to build front-end code
* Run `npm run server` to start mock http service  
* Now you can see project result through [http://localhost:4000](http://localhost:4000)

### npm scripts

* build: compile sources and deploy all static files to `/dist`  
* compile: only compile sources to `dist`  
* dev: start webpack-dev-server to do front-end development(only front-end part, no test rest service data)
* server: start mock express server with all front-end code to do whole lifecycle debugging  

## Project structure

* public: hold static files, like index.html entryfile, favicon  
* app: client side sources  
* mock: a mock server using express.js and typescript to mock rest endpoints for client side data
