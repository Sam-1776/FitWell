import { StatsD } from './stats-d';

export interface NotebookD {
  id: string;
  stats: StatsD[];
  weight: number;
  rmr: number;
}
