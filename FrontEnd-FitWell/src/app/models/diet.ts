import { Nutrient } from "./nutrient";
import { Recipe } from "./recipe";
import { User } from "./user";

export interface Diet {
    id: string,
    recipes: Recipe[],
    numberMeals: number,
    totalCalories: number,
    rmr: number,
    user: User,
    nutritionist: User,
    nutrients: Nutrient[]
}
