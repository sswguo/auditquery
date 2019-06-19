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
import {ChangeEvent, ChangeSummaryStat} from '../model/ChangesType';
import Config from '../appConfig';
import * as path from "path";

const MOCK_DATA_PATH=path.resolve(Config.MOCK_ROOT,'data/list/FakeAuditQueryList.json');

export default class SummaryStatsService {
  readonly path = "/summary/stats";
  readonly handler = (req:Request, res:Response)=>{
    let pattern = req.query.pattern;
    let list = require(MOCK_DATA_PATH);
    if(pattern && pattern!==""){
      let p = new RegExp(pattern);
      list = list.filter((f: ChangeEvent)=>p.test(f.storeKey));
    }
    if(!list || list.length<=0){
      res.status(404).json({error: "No entries found!"});
    }else{
      let stats = new Map<String, ChangeSummaryStat>();
      list.forEach((e:ChangeEvent) => {
        let stat = stats.get(e.storeKey);
        if(!stat){
          stat = {
            storeKey: e.storeKey,
            creates: 0,
            updates: 0,
            deletes: 0,
            total:0,
            lastUpdate: e.changeTime
          }
          stats.set(e.storeKey, stat);
        }
        // console.log(stat);
        switch(e.changeType){
          case "CREATE":
            stat.creates = stat.creates+1;
            break;
          case "UPDATE":
            stat.updates = stat.updates+1;
            break;
          case "DELETE":
            stat.deletes = stat.deletes+1;
            break;
        }
      });
      let finalStats = [];
      for ( let key of stats.keys() ) {
        let finalStat = stats.get(key);
        if(finalStat){
          finalStat.total = finalStat.creates + finalStat.updates + finalStat.deletes;
        }
        finalStats.push(finalStat);
      }
      res.status(200).json(finalStats);
    }
  };
}