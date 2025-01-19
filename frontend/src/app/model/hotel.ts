
export class Hotel {

    id!: number;
    name!: string;
    image!: string;
    address!: string;
    maximumPrice!: number;
    minimumPrice!: number;
    rating!: string;
    location!: {
        id: number;
        name: string;
        image: string;
    }

}