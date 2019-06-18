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