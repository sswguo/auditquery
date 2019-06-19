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

import {Request, Response} from "express";
import {ChangeEvent, ChangeSummary} from '../model/ChangesType';
import {pagination} from './PageUtil';
import Config from '../appConfig';
import * as path from "path";

const MOCK_DATA_PATH=path.resolve(Config.MOCK_ROOT,'data/list/FakeAuditQueryList.json');
export default class SummaryService {
  readonly path = "/summary/by-store/:packageType/:type/:name";
  readonly handler = (req:Request, res:Response)=>{
    let key = `${req.params.packageType}:${req.params.type}:${req.params.name}`;
    if(key && key.length > 0){
      let list: Array<ChangeEvent> = require(MOCK_DATA_PATH).filter((item:ChangeEvent)=>item.storeKey===key);
      let summarylist: Array<ChangeSummary> = list.map((i:ChangeEvent)=>{
        return {
          "eventId": i.eventId, 
          "changeTime": i.changeTime, 
          "user": i.user, 
          "summary": i.summary
        };
      });
      if(summarylist){
        let result = pagination(req, summarylist);
        res.status(200).json(result);
      }else{
        res.status(404).json({error: "No such store!"});
      }
    }
  };
}