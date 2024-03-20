import { User } from "./user";
import { Workout } from "./workout";

export interface CardWorkout {
    id: string,
    name: string,
    workouts: Workout[],
    restTimer: number,
    user: User,
    coach: User
}
