import { Diet } from "./diet";

export interface StatsD {
    id: string,
    diet: Diet,
    date: Date,
    weight: number
}
