import { Nutrient } from "./nutrient";

export interface Food {
    id: string,
    name: string,
    amount: number,
    unit: string,
    nutrition: Nutrient[],
    categories: string[],
    calories: number,
}
