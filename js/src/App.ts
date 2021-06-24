import { getData, getColumnNames } from "./DbHandler"
import { getAccessToken } from "./AccessTokenHandler"
import Constants from './Constants'
import { createPrems } from './PremPostHandler'
import { createAllMovements, createMovementsForTimestampRange } from './MovementPostHandler'

doIt()

async function doIt() {
    let accessToken = await getAccessToken(Constants.NPB_BASE_URL, Constants.NPB_API_KEY, Constants.NPB_API_SECRET)

    let premColumnNames = await getColumnNames("../../db/prem.csv")
    console.log(`Prem column names: ${Array.from(premColumnNames)}`)
    let premAddressColumnNames = await getColumnNames("../../db/prem_address.csv")
    console.log(`Prem Address column names: ${Array.from(premAddressColumnNames)}`)
    let movementColumnNames = await getColumnNames("../../db/movement.csv")
    console.log(`Movement column names: ${Array.from(movementColumnNames)}`)
    let movementAddressesColumnNames = await getColumnNames("../../db/movement_addresses.csv")
    console.log(`Movement Addresses column names: ${Array.from(movementAddressesColumnNames)}`)

    let premResponse = await createPrems("../../db/prem.csv", "../../db/prem_address.csv")
    console.log(`Created prems with status ${premResponse!.status}`)

    let allMovementResponse = await createAllMovements("../../db/movement.csv", "../../db/movement_addresses.csv")
    console.log(`Created all movements with status ${allMovementResponse!.status}`)

    let movementWithinTimestampResponse = await createMovementsForTimestampRange("../../db/movement.csv", "../../db/movement_addresses.csv", "2021-06-07T13:45", "2021-06-08T13:45")
    console.log(`Created movements within timestamp range with status ${movementWithinTimestampResponse!.status}`)
}