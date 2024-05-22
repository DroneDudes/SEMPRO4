export interface AssemblyStation{
  name: String;
  blueprintName: String;
  state: number;
  processId: number;
  product: Item;
}

export interface Item{ 
  name: String;
}
