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