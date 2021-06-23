import axios from 'axios';
import { getMovementAddressesData, getMovementData } from "./MovementDbHandler"
import { getAccessToken } from "./AccessTokenHandler"
import Constants from './Constants'

export function createMovements(movementFilePath: string, movementAddressesFilePath: string) {
    let movementPromise = getMovementData(movementFilePath)
    movementPromise.then((movementData) => {
        let movementAddressesPromise = getMovementAddressesData(movementAddressesFilePath)
        movementAddressesPromise.then((movementAddresses) => {

        })
    })
}
