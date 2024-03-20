import { Exercise } from "./exercise";

export interface PageExercise {
content: Exercise[];
empty: boolean;
first: boolean;
last: boolean;
number: number;
numberOfElements: number;
pageable: {pageNumber: number, pageSize: number, sort: {empty: boolean; sorted: boolean; unsorted: boolean}, offset: number, unpaged: boolean}
size: number;
sort: {empty: boolean; sorted: boolean; unsorted: boolean};
totalElements: number;
totalPages: number;
}
