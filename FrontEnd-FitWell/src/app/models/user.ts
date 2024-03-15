import { NotebookD } from "./notebook-d";
import { NotebookW } from "./notebook-w";

export interface User {
    id: string;
    name: string;
    surname: string;
    email: string;
    avatar: string;
    gender: string;
    role: string[];
    noteBookW: NotebookW;
    noteBookD: NotebookD;
}
