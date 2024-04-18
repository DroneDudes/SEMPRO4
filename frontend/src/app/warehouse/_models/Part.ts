import { Item } from "./Item";

export class Part implements Item{
    id: number;
    name: string;
    type: string;
    description: string;
    specifications: string;
    supplierDetails: string;
    price: number;
    blueprints: Object[];

     constructor (id: number, name: string, description: string, specifications: string, supplierDetails: string, price: number, blueprints: Object[], type: string) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.specifications = specifications;
        this.supplierDetails = supplierDetails;
        this.price = price;
        this.blueprints = blueprints;
        this.type = type;
     }

}