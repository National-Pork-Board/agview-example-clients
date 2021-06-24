import axios, { AxiosResponse } from 'axios';
import { getData } from "./DbHandler"
import { getAccessToken } from "./AccessTokenHandler"
import Constants from './Constants'

export async function createAllMovements(movementFilePath: string, movementAddressesFilePath: string) {
    let movements = await getData(movementFilePath)
    let movementsAddresses = await getData(movementAddressesFilePath)

    return createMovements(movements, movementsAddresses)
}

async function createMovements(movements: Map<string, any>[], movementsAddresses: Map<string, any>[]) {
    let movementAddressesByMovementId: Map<string, Map<string, any>> = new Map()
    movementsAddresses.forEach(movementAddresses => {
        movementAddressesByMovementId.set(movementAddresses.get('movement_id'), movementAddresses)
    })

    let requestBody: Array<any> = createRequestBody(movements, movementAddressesByMovementId)

    let accessToken = await getAccessToken(Constants.NPB_BASE_URL, Constants.NPB_API_KEY, Constants.NPB_API_SECRET)

    return axios.post(`${Constants.NPB_BASE_URL}/api/v1/movements/`, requestBody,
        {
            headers: { Authorization: `Bearer ${accessToken.access}` }
        })
        .then((res: AxiosResponse<any>) => res)
        .catch(error => {
            console.error(error)
        })
}

function createRequestBody(movements: Map<string, any>[], movementAddressesByMovementId: Map<string, Map<string, any>>): Array<any> {
    let requestBody: Array<any> = []
    movements.forEach(movement => {
        let source = getOrNull(getCorrespondingAddressesRecord(movementAddressesByMovementId, movement).get('source'))
        var sourceLatitude = getOrNull(getCorrespondingAddressesRecord(movementAddressesByMovementId, movement).get('source_latitude'))
        var target = getOrNull(getCorrespondingAddressesRecord(movementAddressesByMovementId, movement).get('destination'))
        var targetLatitude = getOrNull(getCorrespondingAddressesRecord(movementAddressesByMovementId, movement).get('destination_latitude'))

        let requestBodyEntry: any = {}
        requestBodyEntry['species'] = getOrNull(movement.get('species'))
        requestBodyEntry["numberInShipment"] = getOrNull(movement.get('number_in_shipment'))
        requestBodyEntry["movementType"] = getOrNull(movement.get('movement_type'))
        requestBodyEntry["movementDatetime"] = getOrNull(movement.get('movement_datetime'))

        requestBodyEntry["source"] = source;
        requestBodyEntry["sourceLatitude"] = sourceLatitude;
        requestBodyEntry["sourceLongitude"] = getOrNull(getCorrespondingAddressesRecord(movementAddressesByMovementId, movement).get('source_longitude'))
        if (!source && !sourceLatitude) {
            requestBodyEntry["sourceAddress"] =
            {
                "streetAddress": getCorrespondingAddressesRecord(movementAddressesByMovementId, movement).get('source_street_address'),
                "city": getCorrespondingAddressesRecord(movementAddressesByMovementId, movement).get('source_city'),
                "state": getCorrespondingAddressesRecord(movementAddressesByMovementId, movement).get('source_state'),
                "zip": getCorrespondingAddressesRecord(movementAddressesByMovementId, movement).get('source_zip')
            }
        }

        requestBodyEntry["destination"] = target;
        requestBodyEntry["destinationLatitude"] = targetLatitude;
        requestBodyEntry["destinationLongitude"] = getOrNull(getCorrespondingAddressesRecord(movementAddressesByMovementId, movement).get('destination_longitude'))
        if (!target && !targetLatitude) {
            requestBodyEntry["destinationAddress"] =
            {
                "streetAddress": getCorrespondingAddressesRecord(movementAddressesByMovementId, movement).get('destination_street_address'),
                "city": getCorrespondingAddressesRecord(movementAddressesByMovementId, movement).get('destination_city'),
                "state": getCorrespondingAddressesRecord(movementAddressesByMovementId, movement).get('destination_state'),
                "zip": getCorrespondingAddressesRecord(movementAddressesByMovementId, movement).get('destination_zip')
            }
        }

        requestBody.push(requestBodyEntry)
    })

    return requestBody
}

function getCorrespondingAddressesRecord(movementAddressesByMovementId: Map<string, Map<string, any>>, movement: Map<string, any>): Map<string, any> {

    return movementAddressesByMovementId.get(movement.get('movement_id'))!
}

function getOrNull(value: any) {
    return value ? value : null
}

export async function createMovementsForTimestampRange(movementFilePath: string, movementAddressesFilePath: string, startTimestamp: string, endTimestamp: string) {
    let allMovements = await getData(movementFilePath)
    let movements = allMovements.filter(movementsWithinTimerange(startTimestamp, endTimestamp));
    let movementsAddresses = await getData(movementAddressesFilePath)

    return createMovements(movements, movementsAddresses)
}

function movementsWithinTimerange(startTimestamp: string, endTimestamp: string) {

    return function (movement: Map<string, any>): Boolean {
        let movementTimestamp = movement.get('movement_datetime');

        return new Date(movementTimestamp) >= new Date(new Date(startTimestamp))
            && new Date(movementTimestamp) <= new Date(new Date(endTimestamp))
    }
}

