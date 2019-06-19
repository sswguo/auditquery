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
import {pagination} from './PageUtil';
import Config from '../appConfig';
import * as path from "path";

const MOCK_DATA_PATH=path.resolve(Config.MOCK_ROOT,'data/list/FakeAuditQueryList.json');
export default class ChangeService {
  readonly pathAll = '/change/all';
  readonly allHandler =  (req:Request, res:Response) => {
    let list = require(MOCK_DATA_PATH);
    let result = pagination(req, list);
    res.status(200).json(result);
  };
  readonly pathKey = '/change/:packageType/:type/:name';
  readonly keyHandler = (req:Request, res:Response) => {
    let key = `${req.params.packageType}:${req.params.type}:${req.params.name}`;
    if(key && key.length > 0){
      let list = require(MOCK_DATA_PATH).filter((item:ChangeEvent)=>item.storeKey===key);
      if(list){
        let result = pagination(req, list);
        res.status(200).json(result);
      }else{
        res.status(404).json({error: "No such store!"});
      }
    }
  };
  readonly eventPath = '/change/:eventId';
  readonly eventHandler = (req:Request, res:Response) => {
    let eventId = `${req.params.eventId}`;
    if(eventId && eventId.length > 0){
      let event = require(MOCK_DATA_PATH).filter((item:ChangeEvent)=>item.eventId===eventId);
      if(event){
        res.status(200).json(event[0]);
      }else{
        res.status(404).json({error: "No such change!"});
      }
    }
  }
}