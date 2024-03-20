import { Exercise } from "./exercise";
import { Set } from "./set";

export interface Workout {
    id: string,
    exercise: Exercise,
    sets: Set[]
}
