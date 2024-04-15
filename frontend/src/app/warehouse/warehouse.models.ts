export interface Item {
  id: number;
  name: string;
  type: string;
  description: string;
}

export interface Warehouse {
  id: number;
  model: string;
  items: { [key: number]: Item };
  uri: string;
  name: string;
  size: number;
}
