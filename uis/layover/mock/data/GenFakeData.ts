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
      let repoKeySlice = repoKey.split(":");      
      let e = {
        eventId: idNum,
        storeKey: repoKey,
        changeTime: changeTime,
        changeType: changeType,
        summary: summary,
        user: `user${i}`,
        version: `${j}.${i}`,
        diffContent: `--- a/central.json +++ b/central.json @@ -1,1 +1,27 @@ -null +{ + "type" : "${repoKeySlice[1]}", + "key" : "${repoKey}", + "disabled" : false, + "host" : "repo.maven.apache.org", + "port" : 80, + "packageType" : "${repoKeySlice[0]}", + "name" : "${repoKeySlice[2]}", + "type" : "${repoKeySlice[1]}", + "disable_timeout" : 0, + "path_style" : "plain", + "authoritative_index" : false, + "allow_snapshots" : false, + "allow_releases" : true, + "url" : "http://repo.maven.apache.org/maven2/", + "timeout_seconds" : 0, + "max_connections" : 30, + "ignore_hostname_verification" : false, + "nfc_timeout_seconds" : 0, + "is_passthrough" : false, + "cache_timeout_seconds" : 86400, + "metadata_timeout_seconds" : 0, + "proxy_port" : 0, + "prefetch_priority" : 0, + "prefetch_rescan" : false, + "prefetch_listing_type" : "html" +}`
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