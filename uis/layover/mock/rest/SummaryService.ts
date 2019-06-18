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