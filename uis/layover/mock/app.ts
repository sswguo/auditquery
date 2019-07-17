///
/// Copyright (C) 2018 Red Hat, Inc. (jdcasey@commonjava.org)
///
/// Licensed under the Apache License, Version 2.0 (the "License");
/// you may not use this file except in compliance with the License.
/// You may obtain a copy of the License at
///
///         http://www.apache.org/licenses/LICENSE-2.0
///
/// Unless required by applicable law or agreed to in writing, software
/// distributed under the License is distributed on an "AS IS" BASIS,
/// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
/// See the License for the specific language governing permissions and
/// limitations under the License.
///

import * as path from "path";
import {Request, Response} from "express";
import express from "express";
import compression from "compression";
import SummaryStatsService from "./rest/SummaryStatsService";
import SummaryService from "./rest/SummaryService";
import ChangeService from "./rest/ChangeService";


const app = express();

const projectRoot = path.resolve(__dirname, "..");
const APP_ROOT="/#";
const indexHtml=path.join(projectRoot+'dist/index.html');
const apiRoot = "/api/rest/history/stores";

console.log(projectRoot);

app.use(compression());
app.listen(4000, () => {
  console.log("Example app listening at http://localhost:4000");
});
app.use(express.static('dist'));

// For direct url bar addressing, will send home page directly for client router rendering
app.get([APP_ROOT, `${APP_ROOT}/*`, '/'],(req:Request, res:Response) => {
    res.sendFile(indexHtml);
});

var sumStatService = new SummaryStatsService();
app.get(apiRoot+sumStatService.path, sumStatService.handler);

var summaryService = new SummaryService();
app.get(apiRoot+summaryService.path, summaryService.handler);

var changeService = new ChangeService();
app.get(apiRoot+changeService.pathAll, changeService.allHandler);
app.get(apiRoot+changeService.pathKey, changeService.keyHandler);
app.get(apiRoot+changeService.eventPath, changeService.eventHandler);