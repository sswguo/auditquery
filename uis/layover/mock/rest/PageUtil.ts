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

import {Request} from "express";

export const pagination = function(req:Request, result:any){
  let pageSize = req.query.pageSize?parseInt(req.query.pageSize):0;
  let page = req.query.page?parseInt(req.query.page):0;
  // console.log(`pageSize:${pageSize}, page:${page}`);
  let realResult = result;
  if (pageSize>0 && page>=0){
    let startIndex = page * pageSize;
    let endIndex = (page + 1) * pageSize -1;
    // console.log(`startIndex:${startIndex}, endIndex:${endIndex}`);
    realResult = result.filter((item:any, index:number)=>index>=startIndex&&index<=endIndex);
  }
  let finalResult = {
    total: result.length,
    page,
    pageSize,
    items: realResult
  }
  return finalResult;
}