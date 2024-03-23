import { FoodsIntermediate } from "./foods-intermediate";
import { Nutrient } from "./nutrient";

export interface Recipe {
    id: string,
    name: string,
    ingredients: FoodsIntermediate[],
    calories: number,
    nutrition: Nutrient[]
}
