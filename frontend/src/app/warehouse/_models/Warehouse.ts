import { Item } from "./Item";

export interface Warehouse {
    id: number;
    model: string;
    items: {[key: number]: Item};
    uri: string;
    name: string;
    size: number;
}