import axios from 'axios';
import { getMovementAddressesData, getMovementData } from "./MovementDbHandler"
import { getAccessToken } from "./AccessTokenHandler"
import Constants from './Constants'

export function createMovements(movementFilePath: string, movementAddressesFilePath: string) {
    let movementPromise = getMovementData(movementFilePath)
    movementPromise.then((movementData) => {
        let movementsAddressesPromise = getMovementAddressesData(movementAddressesFilePath)
        movementsAddressesPromise.then((movementsAddresses) => {
            let movementAddressesByMovementId: Map<string, Map<string, any>> = new Map()
            movementsAddresses.forEach(movementAddresses => {
                movementAddressesByMovementId.set(movementAddresses.get('movement_id'), movementAddresses)
            })

            /*let requestBody: Array<{}> = []
            movementAddressesByMovementId.forEach(movementAddresses => {
                requestBody.push(Object.fromEntries(movementAddresses))
            })
            console.log(requestBody)*/
        })
    })
}
