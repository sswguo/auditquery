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

type ChangeEvent = {
  eventId: string,
  storeKey: string,
  changeTime: Date,
  version: string,
  summary: string,
  changeType: string,
  user: string,
  diffContent: string
};

type ChangeSummary = {
  eventId: string,
  changeTime: Date,
  user: string,
  summary: string
};

type ChangeSummaryStat = {
  storeKey: string,
  creates: number,
  updates: number,
  deletes: number,
  total: number,
  lastUpdate: Date
};

export {ChangeEvent, ChangeSummary, ChangeSummaryStat};