import {ChangeEvent} from "../model/ChangesType";
import * as uuid from "uuid";
import * as fs from "fs";
import * as path from "path";
import Config from "../appConfig";

const genFakeData = ()=>{
  const repos = ["maven:remote:test0", "maven:hosted:test1", "maven:group:test2", "maven:remote:test3",
  "maven:hosted:test4", "maven:group:test5", "maven:remote:test6"];
  const events:Array<ChangeEvent> = new Array<ChangeEvent>();

  for (let j=0; j<repos.length; j++){
    let repoKey = repos[j];
    for ( let i = 0; i < 60; i++ ){
      const id = uuid.v1();      
      let idNum = id.replace(/-/g, "");
      var changeType = "";
      switch ( i%3 ){
        case 0:
            changeType = 'CREATE';
            break;
        case 1:
            changeType = 'UPDATE';
            break;
        case 2:
            changeType = 'DELETE';
            break;
      }
      let changeTime = new Date();
      let summary = `A simple change summary for ${repoKey} with a ${changeType} operation at ${changeTime}`;
      let e = {
        eventId: idNum,
        storeKey: repoKey,
        changeTime: changeTime,
        changeType: changeType,
        summary: summary,
        user: `user${i}`,
        version: `${j}.${i}`,
        diffContent: "A simple diff content"
      }
      events.push(e);      
    }
  }

  let jsonStr = JSON.stringify(events, null, 2);
  let listDir = path.resolve(Config.MOCK_ROOT, "data/list");
  let fakeFile = path.resolve(listDir, 'FakeAuditQueryList.json');
  if(fs.existsSync(fakeFile)){
    fs.unlinkSync(fakeFile);
  }
  if (!fs.existsSync(listDir)){
      fs.mkdirSync(listDir);
  }
  fs.writeFile(fakeFile, jsonStr, 'UTF-8',(err)=>{err && console.log(err)});
}

genFakeData();