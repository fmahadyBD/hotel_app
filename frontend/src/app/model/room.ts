export class Room{
    id!:number;
    name!:string;
    image!:string;
    price!:number;
    area!:number;
    adultNo!:number;
    childNo!:number;

    hotel!:{
        id: number;
        name: string;
        image: string;
        address: string;
        maximumPrice: number;
        minimumPrice: number;
        rating: string;
    }


}