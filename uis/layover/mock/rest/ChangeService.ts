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