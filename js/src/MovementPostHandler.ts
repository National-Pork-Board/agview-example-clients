import axios from 'axios';
import { getMovementAddressesData, getMovementData } from "./MovementDbHandler"
import { getAccessToken } from "./AccessTokenHandler"
import Constants from './Constants'

export function createMovements(movementFilePath: string, movementAddressesFilePath: string) {
    let movementsPromise = getMovementData(movementFilePath)
    movementsPromise.then((movements) => {
        let movementsAddressesPromise = getMovementAddressesData(movementAddressesFilePath)
        movementsAddressesPromise.then((movementsAddresses) => {
            let movementAddressesByMovementId: Map<string, Map<string, any>> = new Map()
            movementsAddresses.forEach(movementAddresses => {
                movementAddressesByMovementId.set(movementAddresses.get('movement_id'), movementAddresses)
            })

            //Create request body
            let requestBody: Array<{}> = []
            movements.forEach(movement => {
                let source = getCorrespondingAddressesRecord(movementAddressesByMovementId, movement).get('source')
                var sourceLatitude = getCorrespondingAddressesRecord(movementAddressesByMovementId, movement).get('source_latitude')
                var target = getCorrespondingAddressesRecord(movementAddressesByMovementId, movement).get('destination')
                var targetLatitude = getCorrespondingAddressesRecord(movementAddressesByMovementId, movement).get('destination_latitude')

                let requestBodyEntry: any = {}
                requestBodyEntry['species'] = movement.get('species')
                requestBodyEntry["numberInShipment"] = movement.get('number_in_shipment')
                requestBodyEntry["movementType"] = movement.get('movement_type')
                requestBodyEntry["movementDatetime"] = movement.get('movement_datetime')

                requestBodyEntry["source"] = source;
                requestBodyEntry["sourceLatitude"] = sourceLatitude;
                requestBodyEntry["sourceLongitude"] = getCorrespondingAddressesRecord(movementAddressesByMovementId, movement).get('source_longitude')
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
                requestBodyEntry["destinationLongitude"] = getCorrespondingAddressesRecord(movementAddressesByMovementId, movement).get('destination_longitude')
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
        })
    })
}

function getCorrespondingAddressesRecord(movementAddressesByMovementId: Map<string, Map<string, any>>, movement: Map<string, any>): Map<string, any> {

    return movementAddressesByMovementId.get(movement.get('movement_id'))!
}
