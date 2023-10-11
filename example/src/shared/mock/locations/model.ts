export interface ILocation {
    id: number
    name: string
    latitude: number
    longitude: number
    altitude: number
}

export type ILocationList = ILocation[]